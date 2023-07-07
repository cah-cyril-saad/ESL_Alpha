package main.java.wavemark.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.java.wavemark.entities.ESLProduct;
import org.apache.commons.lang3.StringUtils;

public class DatabaseUtilities {
    
    
    public static boolean productAlreadyExists(Connection cnx, String binsetNum) throws SQLException {
        String query = "SELECT EXISTS(SELECT 1  FROM binsets WHERE binset_number = ?);";
        
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, binsetNum);
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
        String query = "INSERT INTO binsets (binset_number,product_description,quantity,bin_set_flg,model_number,display_binset_number,requisition_date_bin_a,requisition_date_bin_b,order_status_bin_a,order_status_bin_b,item_master_number, out_of_stock_flg, hospital_id, display_single_bin_barcode, display_alert, binset_status) VALUES" + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
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
            statement.setBoolean(12, product.isOutOfStockFlg());
            statement.setString(13, product.getHospitalId());
            statement.setString(14, product.getDisplaySingleBinBarcode());
            statement.setString(15, product.getDisplayAlert());
            statement.setString(16, product.getBinSetStatus());
            
            statement.executeUpdate();
        }
    }
    
    public static HashMap<String, String> fetchAllProductsInDB(Connection con) {
        HashMap<String, String> displayAlerts = new HashMap<>();
        String query = "SELECT * FROM binsets;";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                String binsetNumber = resultSet.getString("binset_number");
                String displayAlert = resultSet.getString("display_alert");
                displayAlerts.put(binsetNumber, displayAlert);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return displayAlerts;
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
                boolean isOutOfStock = resultSet.getBoolean("out_of_stock_flg");
                String hospitalId = resultSet.getString("hospital_id");
                String displaySingleBinBarcode = resultSet.getString("display_single_bin_barcode");
                String displayAlert = resultSet.getString("display_alert");
                String binsetStatus = resultSet.getString("binset_status");
                
                newState = new ESLProduct(itemMasterNumber, productDescription, binSetNumber, displayBinSetNumber, quantity, modelNumber, orderStatusBinA, orderDateBinA, orderDateBinB, orderStatusBinB, binSetFlg, hospitalId, displaySingleBinBarcode, displayAlert, binsetStatus, isOutOfStock);
                
            }
            hasChanged = EntityHandler.compareReceivedProductFromAppToProductInDaatabase(eslProduct, newState);
            
            resultSet.close();
        }
        return hasChanged;
    }
    
    public static List<ESLProduct> getProds(List<String> ids, Connection con) {
        ArrayList<ESLProduct> products = new ArrayList<>();
        for (String id : ids) {
            String query = "SELECT * FROM binsets WHERE binset_number = ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();
                //TODO: use try with resources
                ESLProduct newState = new ESLProduct();
                while (resultSet.next()) {
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
                    boolean isOutOfStock = resultSet.getBoolean("out_of_stock_flg");
                    String hospitalId = resultSet.getString("hospital_id");
                    String displaySingleBinBarcode = resultSet.getString("display_single_bin_barcode");
                    String displayAlert = resultSet.getString("display_alert");
                    String binsetStatus = resultSet.getString("binset_status");
                    boolean isExpired = resultSet.getBoolean("expiration_flag");
                    boolean isExpiryRisk = resultSet.getBoolean("expiry_risk_flg");
                    newState = new ESLProduct(itemMasterNumber, productDescription, binSetNumber, displayBinSetNumber, quantity, modelNumber, orderStatusBinA, orderDateBinA, orderDateBinB, orderStatusBinB, binSetFlg, hospitalId, displaySingleBinBarcode, displayAlert, binsetStatus, isOutOfStock, isExpired, isExpiryRisk);
                    
                    products.add(newState);
                }
                
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        return products;
    }
    
    public static void updateExistingProducts(Connection cnx, List<ESLProduct> products) throws SQLException {
        for (ESLProduct product : products) {

            String query = (product.isOutOfStockFlg() || StringUtils.isBlank(product.getDisplayAlert()) ? "UPDATE binsets SET product_description = ?, quantity = ?, bin_set_flg = ?, " + "model_number = ?, display_binset_number = ?, requisition_date_bin_a = ?, requisition_date_bin_b = ?, order_status_bin_a = ?, order_status_bin_b = ?, item_master_number = ?, out_of_stock_flg = ?, hospital_id = ?, display_single_bin_barcode = ?, display_alert = ?, binset_status = ? WHERE binset_number = ?;" : "UPDATE binsets SET product_description = ?, quantity = ?, bin_set_flg = ?, " + "model_number = ?, display_binset_number = ?, requisition_date_bin_a = ?, requisition_date_bin_b = ?, order_status_bin_a = ?, order_status_bin_b = ?, item_master_number = ?, out_of_stock_flg = ?, hospital_id = ?, display_single_bin_barcode = ?, binset_status = ? WHERE binset_number = ?;");
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
                statement.setBoolean(11, product.isOutOfStockFlg());
                statement.setString(12, product.getHospitalId());
                statement.setString(13, product.getDisplaySingleBinBarcode());
                int index = 14;
                if (product.isOutOfStockFlg() || StringUtils.isBlank(product.getDisplayAlert())) {
                    statement.setString(index, product.getDisplayAlert());
                    index++;
                }
                statement.setString(index++, product.getBinSetStatus());
                statement.setString(index, product.getBinSetNumber());

                statement.executeUpdate();
            }
        }
        
    }
    
    public static void updateDisplayAlerts(Connection cnx, HashMap<String, String> products) throws SQLException {
        for (Map.Entry<String, String> entry : products.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String query = "UPDATE binsets SET display_alert = ?, expiry_risk_flg = ?, expiration_flag = ? WHERE binset_number = ?;";
            try (PreparedStatement statement = cnx.prepareStatement(query)) {
                String[] values = value.split(",");
                statement.setString(1, values[0]);
                statement.setBoolean(2, Boolean.parseBoolean(values[2]));
                statement.setBoolean(3, Boolean.parseBoolean(values[1]));
                statement.setString(4, key);
                
                statement.executeUpdate();
            }
        }
        
    }
}
