package main.java.wavemark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemark.wmrestlib.authenticator.WardenTokenProvider;
import com.wavemark.wmrestlib.caller.HttpRestCaller;
import com.wavemark.wmrestlib.entity.RequestParam;
import io.sentry.Sentry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import main.java.wavemark.controller.ConnectionManager;
import main.java.wavemark.controller.DatabaseUtilities;
import main.java.wavemark.entities.ESLProduct;
import main.java.wavemark.entities.ExpiredBin;
import main.java.wavemark.entities.ExpiredBinset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class ExpiryRiskUpdateTags {
    
    public static void main(String[] args) throws Exception {
        String store = args[0];
        String interfaceConsumer = args[1];
        String interfaceSecret = args[2];
        String appUrl = args[3];
        
        Configurator.initialize(null, System.getenv("ESL_HOME") + "/log4j2.xml");
        
        Logger logger = LogManager.getLogger("expiry");
        
        try (Connection cnx = ConnectionManager.connect()) {
            ExpiredBinset[] products = getExpiryData(appUrl, interfaceConsumer, interfaceSecret, logger);
            List<ExpiredBinset> filteredProds = Arrays.stream(products).filter(product -> {
                try {
                    return DatabaseUtilities.productAlreadyExists(cnx, product.getBinSetNumber());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            HashMap<String, String> displayAlerts = DatabaseUtilities.fetchAllProductsInDB(cnx);
            
            HashMap<String, String> displayAlertsToUpdate = checkForExpiryChanges(filteredProds, displayAlerts);
            
            DatabaseUtilities.updateDisplayAlerts(cnx, displayAlertsToUpdate);
            List<ESLProduct> prods = DatabaseUtilities.getProds(new ArrayList<>(displayAlertsToUpdate.keySet()), cnx);
            SolumWebservices.updateSolumProductsExpiryData(prods, store, logger);
            
            if (prods.size() == 0) {
                logger.info("No products were changed.");
            }
            
            for (ESLProduct prod : prods) {
                logger.info("Changed the display alert of: " + prod.getBinSetNumber() + " to " + prod.getDisplayAlert());
            }
            
        } catch (Exception e) {
            logger.error("There was an error while trying to update ESL's expiry status", e);
            Sentry.capture(e);
            throw e;
        }
    }
    
    private static HashMap<String, String> checkForExpiryChanges(List<ExpiredBinset> products, HashMap<String, String> binsetToDisplayAlert) {
        HashMap<String, String> displayAlertsToUpdate = new HashMap<>();
        
        String currentDisplayAlert;
        String newDisplayAlert;
        for (ExpiredBinset product : products) {
            newDisplayAlert = resolveNewProductDisplayAlert(product.getBins());
            currentDisplayAlert = binsetToDisplayAlert.get(product.getBinSetNumber());
            
            addBinsetToListOfDisplayMessagesToUpdate(displayAlertsToUpdate, product.getBinSetNumber(), currentDisplayAlert, newDisplayAlert);
        }
        return displayAlertsToUpdate;
    }
    
    private static void addBinsetToListOfDisplayMessagesToUpdate(HashMap<String, String> displayAlertsToUpdate, String binSetNumber, String currentDisplayAlert, String newDisplayAlert) {
        boolean shouldBeChanged = checkIfDisplayAlertShouldBeChanged(currentDisplayAlert.split(",")[0], newDisplayAlert);
        
        if (shouldBeChanged) {
            displayAlertsToUpdate.put(binSetNumber, newDisplayAlert);
        }
    }
    
    private static boolean checkIfDisplayAlertShouldBeChanged(String currentDisplayAlert, String newDisplayAlert) {
        return !currentDisplayAlert.equals("Out of Stock") && !currentDisplayAlert.equals(newDisplayAlert.split(",")[0]);
    }
    
    
    private static String resolveNewProductDisplayAlert(ExpiredBin[] bins) {
        String statusBinA = bins[0].getExpirationFlag();
        String statusBinB = bins[1].getExpirationFlag();
        
        boolean isExpired = statusBinA.equals("E") || statusBinB.equals("E");
        boolean isExpiryRisk = statusBinA.equals("R") || statusBinB.equals("R");
        boolean isBlank = statusBinA.equals("N") && statusBinB.equals("N");
        
        return resolveDisplayAlertInExpiry(isExpired, isExpiryRisk, isBlank);
        
    }
    
    private static String resolveDisplayAlertInExpiry(boolean expiryFlag, boolean expiryRiskFlag, boolean isBlank) {
        String res = "";
        if (expiryFlag) {
            res = "Expired";
        } else if (expiryRiskFlag) {
            res = "Expiry Risk";
        } else if (isBlank) {
            res = "";
        }
        
        return res + "," + expiryFlag + "," + expiryRiskFlag;
        
    }
    
    private static ExpiredBinset[] getExpiryData(String appUrl, String interfaceConsumer, String interfaceSecret, Logger logger) throws Exception {
        try {
            WardenTokenProvider.getInstance().initializeWardenTokenConfigurations("InterfaceDevice", "QUer775N4nVNDrn9G9ty8K8Ht45XrfjSUaWfT9f9", appUrl, interfaceConsumer, interfaceSecret);
            WardenTokenProvider.getInstance().refreshWardenToken();
            System.out.println(WardenTokenProvider.getInstance().getAccessToken());
            String webserviceURI = appUrl + "/kanban/expiration/binsets";
            HttpRestCaller restProxy = new HttpRestCaller(webserviceURI, 100000, 100000, null, 0, null);
            restProxy.printRawResponse(true);
            restProxy.setContentType("application/json");
            RequestParam[] requestParams = new RequestParam[0];
            
            ObjectMapper objectMapper = new ObjectMapper();
            //            ExpiredBinset[] products = objectMapper.readValue(new File("C:\\Development\\Wavemark\\EslResearchAndDevelopment\\mock2"), ExpiredBinset[].class);
            ExpiredBinset[] products = restProxy.call(requestParams, ExpiredBinset[].class, HttpRestCaller.REQUEST_METHOD_GET, null, null, 3, 1, true);
            logger.info("Received expiry data for binsets: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products));
            
            return products;
        } catch (Exception e) {
            logger.error("Failed to download expiry data for binsets from App", e);
            throw e;
        }
    }
}

