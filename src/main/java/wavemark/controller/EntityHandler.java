package main.java.wavemark.controller;

import static main.java.wavemark.controller.DatabaseUtilities.productHasChanged;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import main.java.wavemark.entities.AppProduct;
import main.java.wavemark.entities.Bin;
import main.java.wavemark.entities.ESLProduct;
import main.java.wavemark.entities.EslRemoteEntity;
import main.java.wavemark.enums.RequisitionStatus;
import org.apache.commons.lang3.StringUtils;

public class EntityHandler {
    
    public static ESLProduct translateFromAppToESLHelper(AppProduct product) {
        ESLProduct result = new ESLProduct();
        Bin binA = product.getBins()[0];
        
        Bin binB = product.getBins().length == 1 ? new Bin() : product.getBins()[1];
        
        result.setBinSetStatus(product.getStatus());
        result.setItemMasterNumber(product.getItemMasterNumber());
        result.setProductDescription(StringUtils.isBlank(product.getEndpointProductName()) ? product.getProductDescription() : product.getEndpointProductName());
        result.setBinSetNumber(product.getBinsetNumber());
        result.setDisplayBinSetNumber(resolveDisplayBinSetNumber(product.getBinsetNumber()));
        result.setQuantity(product.getLogum());
        result.setModelNumber(product.getModelNumber());
        result.setBinSetFlg(resolveBinSetFlag(product.getBinsetFlag()));
        result.setOrderStatusBinA(resolveOrderBinStatus(binA));
        result.setOrderStatusBinB(resolveOrderBinStatus(binB));
        result.setOrderDateBinA(resolveDisplayDate(result.getOrderStatusBinA(), binA.getRequisitionDate()));
        result.setOrderDateBinB(resolveDisplayDate(result.getOrderStatusBinB(), binB.getRequisitionDate()));
        result.setOutOfStockFlg(resolveOutOfStockFlag(product.getBins()));
        result.setDisplayAlert(resolveDisplayAlert(result.isOutOfStockFlg()));
        
        result.setHospitalId("HOSP_ID");
        //TODO: these fields should be handled by second WS
        //        result.setExpiryRiskFlg(resolveExpiryRiskFlag(product));
        //        result.setExpirationFlag(resolveExpirationFlag(product));
        //TODO: what to do if is expired, or risk of expiry???
        result.setDisplaySingleBinBarcode(product.getBins()[0].getBinSerialNumber());
        result.setBinSetStatus(product.getStatus());
        return result;
    }
    
    private static boolean resolveExpirationFlag(AppProduct product) {
        Bin[] bins = product.getBins();
        boolean isOutOfStock = resolveOutOfStockFlag(bins);
        
        if (isOutOfStock) {
            return false;
        }
        
        return bins[0].getExpirationFlag().equals("E");
        
    }
    
    private static boolean resolveExpiryRiskFlag(AppProduct product) {
        Bin[] bins = product.getBins();
        boolean isOutOfStock = resolveOutOfStockFlag(bins);
        
        if (isOutOfStock) {
            return false;
        }
        
        return bins[0].getExpirationFlag().equals("R");
        
    }
    
    private static String resolveBinSetFlag(int binsetFlag) {
        return binsetFlag == 0 ? "2BK" : "1BK";
    }
    
    public static List<ESLProduct> translateFromAppToESL(List<AppProduct> products) {
        return products.stream().map(EntityHandler::translateFromAppToESLHelper).collect(Collectors.toList());
    }
    
    private static String resolveDisplayAlert(boolean outOfStockFlg) {
        return outOfStockFlg ? "Out of Stock" : "";
    }
    
    public static boolean resolveOutOfStockFlag(Bin[] bins) {
        
        return bins[0].getState().equals(RequisitionStatus.EMPTY.toString()) && bins[1].getState().equals(RequisitionStatus.EMPTY.toString());
    }
    
    private static String resolveOrderBinStatus(Bin bin) {
        String binState = bin.getState();
        boolean isEmpty = StringUtils.isBlank(binState);
        boolean isFilled = !isEmpty && binState.equals(RequisitionStatus.FILLED.toString());
        return (isEmpty || isFilled) ? "" : OrderBinStatusFactory.orderBinStatusResolver(bin);
        
    }
    
    
    private static int resolveDisplayBinSetNumber(String binsetNumber) {
        String lastFourChars = "";
        
        if (binsetNumber.length() > 4) {
            lastFourChars = binsetNumber.substring(binsetNumber.length() - 4);
        } else {
            lastFourChars = binsetNumber;
        }
        return Integer.parseInt(lastFourChars);
    }
    
    private static String resolveDisplayDate(String binStatus, String binOrderDate) {
        String date = "";
        if (binStatus.equals(RequisitionStatus.ORDERED.toString())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDateTime dateTime = LocalDateTime.parse(binOrderDate, formatter);
            // Convert the date time object to a different format
            formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            date = dateTime.format(formatter);
        }
        return date;
    }
    
    public static JsonNode translateESLProductToJsonString(ESLProduct product) throws JsonProcessingException {
        Map<String, String> data = new HashMap<>();
        //TODO: set vars static final class
        data.put("HOSPITAL_ID", "HOSP_ID");
        data.put("BINSET_NUMBER", product.getBinSetNumber());
        data.put("ITEM_MASTER_NUMBER", product.getItemMasterNumber());
        data.put("PRODUCT_DESCRIPTION", product.getProductDescription());
        data.put("QUANTITY", String.valueOf(product.getQuantity()));
        data.put("BIN_SET_FLG", product.getBinSetFlg());
        data.put("BINSET_STATUS", product.getBinSetStatus());
        data.put("DISPLAY_SINGLE_BIN_BARCODE", product.getDisplaySingleBinBarcode());
        data.put("DISPLAY_BINSET_NUMBER", String.valueOf(product.getDisplayBinSetNumber()));
        data.put("MODEL_NUMBER", product.getModelNumber());
        data.put("ORDER_STATUS_BIN_A", product.getOrderStatusBinA());
        data.put("ORDER_DATE_BIN_A", product.getOrderDateBinA());
        data.put("ORDER_STATUS_BIN_B", product.getOrderStatusBinB());
        data.put("ORDER_DATE_BIN_B", product.getOrderDateBinB());
        if (product.getDisplayAlert().equalsIgnoreCase("Out of Stock") || StringUtils.isBlank(product.getDisplayAlert())) {
            data.put("DISPLAY_ALERT", product.getDisplayAlert());
        }
        data.put("OUT_OF_STOCK_FLG", String.valueOf(product.isOutOfStockFlg()).toUpperCase());
        //TODO: get expiry risk flag
        //data.put("EXPIRATION_FLG", String.valueOf(product.getExpiryRiskFlg()));
        //data.put("EXPIRY_RISK_FLG", "FALSE");
        
        EslRemoteEntity remoteEslProduct = new EslRemoteEntity(product.getBinSetNumber(), product.getProductDescription(), "", data);
        
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.valueToTree(remoteEslProduct);
    }
    
    public static JsonNode createExpiryDataNode(ESLProduct product) {
        Map<String, String> data = new HashMap<>();
        data.put("DISPLAY_ALERT", product.getDisplayAlert());
        data.put("OUT_OF_STOCK_FLG", product.getDisplayAlert().equalsIgnoreCase("out of stock") ? "TRUE" : "FALSE");
        data.put("EXPIRATION_FLG", String.valueOf(product.getExpirationFlag()).toUpperCase());
        data.put("EXPIRY_RISK_FLG", String.valueOf(product.getExpiryRiskFlg()).toUpperCase());
        
        EslRemoteEntity remoteEslProduct = new EslRemoteEntity(product.getBinSetNumber(), product.getProductDescription(), "", data);
        
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.valueToTree(remoteEslProduct);
    }
    
    public static boolean compareReceivedProductFromAppToProductInDaatabase(ESLProduct product1, ESLProduct product2) {
        //TODO: add hospital ID, define product comparator, java 8 stream comparators (a, b) -> return a.quantity > b.quantity;
        
        return !(product1.getItemMasterNumber().equals(product2.getItemMasterNumber()) && product1.getBinSetNumber().equals(product2.getBinSetNumber()) && product1.getProductDescription().equals(product2.getProductDescription()) && product1.getQuantity() == product2.getQuantity() && product1.getModelNumber().equals(product2.getModelNumber()) && product1.getDisplayBinSetNumber() == product2.getDisplayBinSetNumber() && product1.getOrderDateBinA().equals(product2.getOrderDateBinA()) && product1.getOrderDateBinB().equals(product2.getOrderDateBinB()) && product1.getOrderStatusBinA().equals(product2.getOrderStatusBinA()) && product1.getOrderStatusBinB().equals(product2.getOrderStatusBinB())
                 && (product1.isOutOfStockFlg() == product2.isOutOfStockFlg()) && product1.getHospitalId().equals(product2.getHospitalId()) && product1.getDisplaySingleBinBarcode().equals(product2.getDisplaySingleBinBarcode()) && product1.getDisplayAlert().equals(product2.getDisplayAlert()) && product1.getBinSetStatus().equals(product2.getBinSetStatus()));
        
    }
    
    public static Map<Boolean, List<ESLProduct>> partitionByAvailabilityInDatabase(Connection cnx, List<ESLProduct> eslProducts) {
        return eslProducts.stream().collect(Collectors.partitioningBy(product -> {
            try {
                return DatabaseUtilities.productAlreadyExists(cnx, product.getBinSetNumber());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }
    
    public static List<ESLProduct> getProductsToUpdate(Connection cnx, List<ESLProduct> products) throws SQLException {
        ArrayList<ESLProduct> includedProducts = new ArrayList<>();
        
        for (ESLProduct product : products) {
            if (productHasChanged(cnx, product)) {
                includedProducts.add(product);
            }
        }
        
        return includedProducts;
    }
}
