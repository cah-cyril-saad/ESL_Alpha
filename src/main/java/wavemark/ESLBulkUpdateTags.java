package main.java.wavemark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemark.wmrestlib.authenticator.WardenTokenProvider;
import java.awt.AWTException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Stream;
import main.java.wavemark.controller.ConnectionManager;
import main.java.wavemark.controller.DatabaseUtilities;
import main.java.wavemark.controller.EntityHandler;
import main.java.wavemark.entities.AppProduct;
import main.java.wavemark.entities.ESLProduct;
import org.apache.commons.lang3.StringUtils;

public class ESLBulkUpdateTags {
    
    
    private static void initWardenAuthenticationSettings() throws Exception {
        WardenTokenProvider.getInstance().initializeWardenTokenConfigurations("InterfaceDevice", "QUer775N4nVNDrn9G9ty8K8Ht45XrfjSUaWfT9f9", "https://testonline.wavemark.net", "ESL101", "42:01:0A:33:23:57");
        WardenTokenProvider.getInstance().refreshWardenToken();
    }
    
    private static String getCurrentDate() {
        
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a");
        
        return currentTime.format(formatter) + " GMT+3";
        
    }
    
    private static String readDateFromFile(String path) throws IOException {
        Path file = Paths.get(path);
        Optional<String> date = Optional.empty();
        
        try (Stream<String> lines = Files.lines(file)) {
            date = Optional.of(lines.findFirst().get());
        }
        
        return date.orElse(StringUtils.EMPTY);
    }
    
    private static void writeDateToFile(String path) throws IOException {
        Path file = Paths.get(path);
        String date = getCurrentDate();
        
        Files.write(file, date.getBytes());
    }
    
    public static void main(String[] args) throws IOException, SQLException, AWTException, InterruptedException {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        
        try (Connection cnx = ConnectionManager.connect()) {
            fh = new FileHandler("esl.log");
            
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            
            logger.info("My first log");
            cnx.setAutoCommit(true);
            ObjectMapper objectMapper = new ObjectMapper();
            initWardenAuthenticationSettings();
            String date = readDateFromFile("src/main/java/wavemark/date");
            
            logger.info("Fetching all binsets from: " + date);
            String currDate = getCurrentDate();
            AppProduct[] bins = SolumWebservices.getEslBins(date);
            logger.info("Downloaded the " + bins.length + " binset(s) from App:\n " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bins));
            
            ESLProduct[] eslProducts = (EntityHandler.translateFromAppToESL(Arrays.asList(bins))).toArray(new ESLProduct[0]);
            
            Map<Boolean, List<ESLProduct>> existingProducts = EntityHandler.partitionByAvailabilityInDatabase(cnx, Arrays.asList(eslProducts));
            DatabaseUtilities.persistNewProducts(cnx, existingProducts.get(false));
            logger.info("Added " + existingProducts.get(false).size() + " new product(s).");
            
            List<ESLProduct> productsToUpdate = EntityHandler.getProductsToUpdate(cnx, existingProducts.get(true));
            productsToUpdate.addAll(existingProducts.get(false));
            DatabaseUtilities.updateExistingProducts(cnx, productsToUpdate);
            SolumWebservices.updateSolumProducts(productsToUpdate);
            logger.info("Uploading the binsets to Solum: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(productsToUpdate));
            //            System.out.printf("Updated %s product(s) \n", productsToUpdate.size());
            logger.info("A total of " + productsToUpdate.size() + " product(s) were updated.");
            writeDateToFile("src/main/java/wavemark/date");
            logger.info("All products up-to date " + currDate);
            
            logger.info("Done");
            System.exit(0);
        } catch (Exception e) {
            logger.info("Error occurred while trying to refresh ESL's...\n");
            e.printStackTrace();
        }
        
    }
}
