package main.java.wavemark.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import main.java.wavemark.entities.ESLProduct;

public class DatabaseUtilities {
    
    
    public static void updateExistingProducts(Connection cnx, List<ESLProduct> products) throws SQLException {
        //TODO: try with resources
        for (ESLProduct product : products) {
            String query = "UPDATE binsets SET product_description = ?, quantity = ?, bin_set_flg = ?, " + "model_number = ?, display_binset_number = ?, requisition_date_bin_a = ?, requisition_date_bin_b = ?, " + "order_status_bin_a = ?, order_status_bin_b = ?, item_master_number = ? WHERE binset_number = ?;";
            try (PreparedStatement statement = cnx.prepareStatement(query)) {
                
                statement.setString(1, product.getProductDescription());
                statement.setInt(2, product.getQuantity());
                statement.setString(3, product.getBinSetFlg());
                statement.setString(4, product.getModelNumber());
                statement.setInt(5, product.getDisplayBinSetNumber());
                statement.setString(6, product.getOrderDateBinA());
                statement.setString(7, product.getOrderDateBinB());
                statement.setString(8, product.getOrderStatusBinA());
                statement.setString(9, product.getOrderStatusBinB());
                statement.setString(10, product.getItemMasterNumber());
                statement.setString(11, product.getBinSetNumber());
                
                statement.executeUpdate();
            }
        }
        
    }
    
    public static boolean productAlreadyExists(Connection cnx, ESLProduct product) throws SQLException {
        boolean productExistsInDb;
        //SELECT EXISTS(SELECT 1  FROM binsets WHERE binset_number = 'BMC0007632');
        String query = "SELECT EXISTS(SELECT 1  FROM binsets WHERE binset_number = ?);";
        
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, product.getBinSetNumber());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.getBoolean(1);
        }
    }
    
    public static void persistNewProducts(Connection cnx, List<ESLProduct> eslProducts) throws SQLException {
        for (ESLProduct product : eslProducts) {
            insertBinsetIntoDb(cnx, product);
        }
    }
    
    private static void insertBinsetIntoDb(Connection cnx, ESLProduct product) throws SQLException {
        String query = "INSERT INTO binsets (binset_number,product_description,quantity,bin_set_flg,model_number,display_binset_number,requisition_date_bin_a,requisition_date_bin_b,order_status_bin_a,order_status_bin_b,item_master_number) VALUES" + " (?,?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, product.getBinSetNumber());
            statement.setString(2, product.getProductDescription());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getBinSetFlg());
            statement.setString(5, product.getModelNumber());
            statement.setInt(6, product.getDisplayBinSetNumber());
            statement.setString(7, product.getOrderDateBinA());
            statement.setString(8, product.getOrderDateBinB());
            statement.setString(9, product.getOrderStatusBinA());
            statement.setString(10, product.getOrderStatusBinB());
            statement.setString(11, product.getItemMasterNumber());
            
            statement.executeUpdate();
        }
    }
    
    static boolean productHasChanged(Connection con, ESLProduct eslProduct) throws SQLException {
        String query = "SELECT * FROM binsets WHERE binset_number = ?";
        boolean hasChanged = false;
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, eslProduct.getBinSetNumber());
            ResultSet resultSet = statement.executeQuery();
            //TODO: use try with resources
            ESLProduct newState = new ESLProduct();
            while (resultSet.next()) {
                //TODO: put vaar in static final class
                String binSetNumber = resultSet.getString("binset_number");
                String itemMasterNumber = resultSet.getString("item_master_number");
                String productDescription = resultSet.getString("product_description");
                int quantity = resultSet.getInt("quantity");
                String binSetFlg = resultSet.getString("bin_set_flg");
                String modelNumber = resultSet.getString("model_number");
                int displayBinSetNumber = resultSet.getInt("display_binset_number");
                String orderDateBinA = resultSet.getString("requisition_date_bin_a");
                String orderDateBinB = resultSet.getString("requisition_date_bin_b");
                String orderStatusBinA = resultSet.getString("order_status_bin_a");
                String orderStatusBinB = resultSet.getString("order_status_bin_b");
                
                newState = new ESLProduct(itemMasterNumber, productDescription, binSetNumber, displayBinSetNumber, quantity, modelNumber, orderStatusBinA, orderDateBinA, orderDateBinB, orderStatusBinB, binSetFlg);
                
            }
            hasChanged = EntityHandler.compareReceivedProductFromAppToProductInDaatabase(eslProduct, newState);
            
            resultSet.close();
        }
        return hasChanged;
    }
}
