import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class EntityTranslator {
    
    public static ESLProduct translateFromAppToESLHelper(AppProduct product) {
        ESLProduct result = new ESLProduct();
        result.setBinSetStatus(product.getStatus());
        result.setItemMasterNumber(product.getItemMasterNumber());
        result.setProductDescription(product.getProductDescription());
        result.setBinSetNumber(product.getBinsetNumber());
        result.setDisplayBinSetNumber(resolveDisplayBinSetNumber(product.getBinsetNumber()));
        result.setQuantity(product.getLogum());
        result.setModelNumber(product.getModelNumber());
        result.setBinSetFlg(product.getBinsetFlag());
        result.setOrderStatusBinA(resolveOrderBinStatus(product.getBins()[0]));
        result.setOrderStatusBinB(resolveOrderBinStatus(product.getBins()[1]));
        result.setOrderDateBinA(resolveDisplayDate(product.getBins()[0]));
        result.setOrderDateBinB(resolveDisplayDate(product.getBins()[1]));
        result.setOutOfStockFlg(resolveOutOfStockFlag(product.getBins()));
        result.setDisplayAlert(resolveDisplayAlert(result.isOutOfStockFlg()));
        return result;
    }
    
    public static ESLProduct[] translateFromAppToESL(AppProduct[] products) {
        ESLProduct[] eslProducts = new ESLProduct[products.length];
        for (int i = 0; i < eslProducts.length; i++) {
            eslProducts[i] = translateFromAppToESLHelper(products[i]);
        }
        
        return eslProducts;
    }
    
    private static String resolveDisplayAlert(boolean outOfStockFlg) {
        return outOfStockFlg ? "Out of Stock" : "";
    }
    
    private static boolean resolveOutOfStockFlag(Bin[] bins) {
        return bins[0].getState().equals(RequisitionStatus.EMPTY.toString()) && bins[1].getState().equals(RequisitionStatus.EMPTY.toString());
    }
    
    private static String resolveOrderBinStatus(Bin bin) {
        String binState = bin.getState();
        boolean isEmpty = StringUtils.isBlank(binState);
        boolean isFilled = binState.equals(RequisitionStatus.FILLED.toString());
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
    
    private static String resolveDisplayDate(Bin bin) {
        String date = "";
        if (bin.getState().equals(RequisitionStatus.ORDERED.toString())) {
            String dateString = bin.getRequisitionDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            // Convert the date time object to a different format
            formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            date = dateTime.format(formatter);
        }
        
        return date;
    }
    
    public static String translateESLProductToJsonString(ESLProduct product) throws JsonProcessingException {
        Map<String, String> data = new HashMap<>();
        
        data.put("HOSPITAL_ID", "");
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
        
        data.put("DISPLAY_ALERT", product.getDisplayAlert());
        data.put("EXPIRATION_FLG", "FALSE");
        data.put("EXPIRY_RISK_FLG", "FALSE");
        data.put("OUT_OF_STOCK_FLG", "FALSE");
        
        EslRemoteEntity remoteEslProduct = new EslRemoteEntity(product.getBinSetNumber(), product.getProductDescription(), "", data);
        
        ObjectMapper mapper = new ObjectMapper();
        
        return "[" + mapper.writeValueAsString(remoteEslProduct) + "]";
        
        
    }
}
