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
        
        boolean isBinA = isBinA(product.getBins()[0]);
        Bin binA = isBinA ? product.getBins()[0] : product.getBins()[1];
        Bin binB = isBinA ? product.getBins()[1] : product.getBins()[0];
        
        result.setBinSetStatus(product.getStatus());
        result.setItemMasterNumber(product.getItemMasterNumber());
        result.setProductDescription(StringUtils.isBlank(product.getEndpointProductName()) ? product.getProductDescription() : product.getEndpointProductName());
        result.setBinSetNumber(product.getBinsetNumber());
        result.setDisplayBinSetNumber(resolveDisplayBinSetNumber(product.getBinsetNumber()));
        result.setQuantity(product.getLogum());
        result.setModelNumber(product.getModelNumber());
        result.setBinSetFlg(resolveBinSetFlag(product.getBinsetFlag()));
    
        result.setOrderStatusBinA(resolveOrderBinStatus(binA));
        result.setOrderDateBinA(resolveDisplayDate(result.getOrderStatusBinA(), binA.getLastEmptyScan()));
    
        result.setOrderStatusBinB(resolveOrderBinStatus(binB));
        result.setOrderDateBinB(resolveDisplayDate(result.getOrderStatusBinB(), binB.getLastEmptyScan()));
        
        result.setOutOfStockFlg(resolveOutOfStockFlag(product.getBins()));
        result.setDisplayAlert(resolveDisplayAlert(result.isOutOfStockFlg()));
        
        result.setHospitalId("HOSP_ID");
        result.setDisplaySingleBinBarcode(binA.getBinSerialNumber());
        result.setBinSetStatus(product.getStatus());
        return result;
    }
    
    private static boolean isBinA(Bin bin) {
        return bin.getBinSerialNumber().endsWith("A");
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
        String statusA = bins[0].getState();
        String statusB = bins.length == 1 ? RequisitionStatus.FILLED.toString() : bins[1].getState();
        return statusA.equals(RequisitionStatus.EMPTY.toString()) && statusB.equals(RequisitionStatus.EMPTY.toString());
    }
    
    private static String resolveOrderBinStatus(Bin bin) {
        if (bin == null) {
            return "";
        }
        String binState = bin.getState();
        boolean isBlank = StringUtils.isBlank(binState);
        boolean isFilled = !isBlank && binState.equalsIgnoreCase(RequisitionStatus.FILLED.toString());
        boolean isEmpty = !isBlank && binState.equalsIgnoreCase(RequisitionStatus.EMPTY.toString());
        if (isBlank || isFilled) {
            return "";
        } else if (isEmpty) {
            return StringUtils.capitalize(RequisitionStatus.ORDERED.toString());
        }
        
        return binState;
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
        data.put("ORDER_STATUS_BIN_A", StringUtils.capitalize(product.getOrderStatusBinA().toLowerCase()));
        data.put("ORDER_DATE_BIN_A", product.getOrderDateBinA());
        data.put("ORDER_STATUS_BIN_B", StringUtils.capitalize(product.getOrderStatusBinB().toLowerCase()));
        data.put("ORDER_DATE_BIN_B", product.getOrderDateBinB());
        if (product.getDisplayAlert().equalsIgnoreCase("Out of Stock") || StringUtils.isBlank(product.getDisplayAlert())) {
            data.put("DISPLAY_ALERT", product.getDisplayAlert());
            data.put("EXPIRATION_FLG", "FALSE");
            data.put("EXPIRY_RISK_FLG", "FALSE");
        }
        data.put("OUT_OF_STOCK_FLG", String.valueOf(product.isOutOfStockFlg()).toUpperCase());
        //TODO: get expiry risk flag
        
        EslRemoteEntity remoteEslProduct = new EslRemoteEntity(product.getBinSetNumber(), product.getProductDescription(), "", data);
        
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.valueToTree(remoteEslProduct);
    }
    
    public static JsonNode createExpiryDataNode(ESLProduct product) {
        Map<String, String> data = new HashMap<>();
        data.put("DISPLAY_ALERT", product.getDisplayAlert());
        boolean isOutOfStock = product.getDisplayAlert().equalsIgnoreCase("out of stock");
        
        if (isOutOfStock) {
            data.put("OUT_OF_STOCK_FLG", "TRUE");
        } else {
            data.put("OUT_OF_STOCK_FLG", "FALSE");
            data.put("EXPIRATION_FLG", String.valueOf(product.getExpirationFlag()).toUpperCase());
            data.put("EXPIRY_RISK_FLG", String.valueOf(product.getExpiryRiskFlg()).toUpperCase());
        }
        
        EslRemoteEntity remoteEslProduct = new EslRemoteEntity(product.getBinSetNumber(), product.getProductDescription(), "", data);
        
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.valueToTree(remoteEslProduct);
    }
    
    public static boolean compareReceivedProductFromAppToProductInDaatabase(ESLProduct product1, ESLProduct product2) {
        //TODO: add hospital ID, define product comparator, java 8 stream comparators (a, b) -> return a.quantity > b.quantity;
        boolean lastEmptyScanIsSameA = product1.getOrderDateBinA().equals(product2.getOrderDateBinA());
        boolean lastEmptyScanIsSameB = product1.getOrderDateBinB().equals(product2.getOrderDateBinB());
    
        boolean productStatusBinAIsSame = lastEmptyScanIsSameA || !product1.getOrderStatusBinA().equals(product2.getOrderStatusBinA());
        boolean productStatusBinBIsSame = lastEmptyScanIsSameB || !product1.getOrderStatusBinB().equals(product2.getOrderStatusBinB());
        
        return !(productStatusBinAIsSame && productStatusBinBIsSame && lastEmptyScanIsSameA && lastEmptyScanIsSameB && product1.getItemMasterNumber().equals(product2.getItemMasterNumber())
                 && product1.getBinSetNumber().equals(product2.getBinSetNumber()) && product1.getProductDescription().equals(product2.getProductDescription())
                 && product1.getQuantity() == product2.getQuantity() && product1.getModelNumber().equals(product2.getModelNumber()) && product1.getDisplayBinSetNumber() == product2.getDisplayBinSetNumber()
                 && product1.getOrderDateBinB().equals(product2.getOrderDateBinB()) && (product1.isOutOfStockFlg() == product2.isOutOfStockFlg()) && product1.getHospitalId().equals(product2.getHospitalId())
                 && product1.getDisplaySingleBinBarcode().equals(product2.getDisplaySingleBinBarcode()) && product1.getDisplayAlert().equals(product2.getDisplayAlert()) && product1.getBinSetStatus().equals(product2.getBinSetStatus()) && product1.getBinSetFlg().equals(product2.getBinSetFlg()));
        
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
