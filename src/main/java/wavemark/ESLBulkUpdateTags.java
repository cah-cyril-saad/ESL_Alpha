package main.java.wavemark;

import com.wavemark.wmrestlib.authenticator.WardenTokenProvider;
import java.awt.AWTException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import main.java.wavemark.controller.ConnectionManager;
import main.java.wavemark.controller.DatabaseUtilities;
import main.java.wavemark.controller.EntityHandler;
import main.java.wavemark.entities.AppProduct;
import main.java.wavemark.entities.ESLProduct;

public class ESLBulkUpdateTags {
    
    public ESLBulkUpdateTags() throws IOException {
    }
    
    private static void initWardenAuthenticationSettings() throws Exception {
        WardenTokenProvider.getInstance().initializeWardenTokenConfigurations("InterfaceDevice", "QUer775N4nVNDrn9G9ty8K8Ht45XrfjSUaWfT9f9", "https://testonline.wavemark.net", "ESL101", "42:01:0A:33:23:57");
        WardenTokenProvider.getInstance().refreshWardenToken();
    }
    
    public static void main(String[] args) throws IOException, SQLException, AWTException, InterruptedException {
        
        try (Connection cnx = ConnectionManager.connect()) {
            cnx.setAutoCommit(true);

            initWardenAuthenticationSettings();
            ArrayList<AppProduct> bins = SolumWebservices.getEslBins(System.getenv("ESL_DATE"));
            System.out.println(bins);
            //TODO: use ArrayList
            List<ESLProduct> eslProducts = EntityHandler.translateFromAppToESL(bins);
            //
            Map<Boolean, List<ESLProduct>> existingProducts = EntityHandler.partitionByAvailabilityInDatabase(cnx, eslProducts);
            DatabaseUtilities.persistNewProducts(cnx, existingProducts.get(false));
            System.out.println("Added " + existingProducts.get(false).size() + " new product(s).");
            //            //Todo: create logic to skip new products when getting products to update
            List<ESLProduct> productsToUpdate = EntityHandler.getProductsToUpdate(cnx, existingProducts.get(true));
            //
            DatabaseUtilities.updateExistingProducts(cnx, productsToUpdate);
            SolumWebservices.updateSolumProducts(productsToUpdate);
            //
            //            System.out.printf("Updated %s product(s) \n", productsToUpdate.size());
            System.out.println("Done");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error occurred while k to refresh ESL's...\n");
            e.printStackTrace();
        }
        
    }
}
