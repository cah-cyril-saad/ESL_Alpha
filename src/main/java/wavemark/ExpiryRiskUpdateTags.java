package main.java.wavemark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemark.wmrestlib.authenticator.WardenTokenProvider;
import com.wavemark.wmrestlib.caller.HttpRestCaller;
import com.wavemark.wmrestlib.entity.RequestParam;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import main.java.wavemark.controller.ConnectionManager;
import main.java.wavemark.controller.DatabaseUtilities;
import main.java.wavemark.entities.ESLProduct;
import main.java.wavemark.entities.ExpiredBin;
import main.java.wavemark.entities.ExpiredBinset;

public class ExpiryRiskUpdateTags {
    
    public static void main(String[] args) throws Exception {
        Logger logger;
        logger = Logger.getLogger("MyLog2");
        FileHandler fh;
        try (Connection cnx = ConnectionManager.connect()) {
            fh = new FileHandler(System.getenv("ESL_HOME") + File.separator + "expiry.log", true);
            logger.addHandler(fh);
            WardenTokenProvider.getInstance().initializeWardenTokenConfigurations("InterfaceDevice", "QUer775N4nVNDrn9G9ty8K8Ht45XrfjSUaWfT9f9", "https://testonline.wavemark.net", "ESL101", "42:01:0A:33:23:57");
            WardenTokenProvider.getInstance().refreshWardenToken();
            System.out.println(WardenTokenProvider.getInstance().getAccessToken());
            String webserviceURI = "https://testonline.wavemark.net/kanban/expiration/binsets";
            HttpRestCaller restProxy = new HttpRestCaller(webserviceURI, 100000, 100000, null, 0, null);
            restProxy.printRawResponse(true);
            restProxy.setContentType("application/json");
            RequestParam[] requestParams = new RequestParam[0];
            
            ObjectMapper objectMapper = new ObjectMapper();
//            ExpiredBinset[] products = objectMapper.readValue(new File("C:\\Development\\Wavemark\\EslResearchAndDevelopment\\mock2"), ExpiredBinset[].class);
            ExpiredBinset[] products = restProxy.call(requestParams, ExpiredBinset[].class, HttpRestCaller.REQUEST_METHOD_GET, null, null, 3, 1, true);
            logger.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products));
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
            SolumWebservices.updateSolumProductsExpiryData(prods);
            
            if (prods.size() == 0) {
                logger.info("No products were changed.");
            }
            
            for (ESLProduct prod : prods) {
                logger.info("Changed the display alert of: " + prod.getBinSetNumber() + " to " + prod.getDisplayAlert());
            }
            
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
    
    //            AppProduct[] bins = SolumWebservices.getEslBins("May 11, 2023 12:02 PM GMT+3");
}

