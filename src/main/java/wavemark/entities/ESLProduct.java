package main.java.wavemark.entities;

public class ESLProduct extends BinsetProduct {
    
    String binsetStatus;
    boolean outOfStockFlg;
    boolean expiryRiskFlg;
    boolean expirationFlag;
    private String itemMasterNumber;
    private String productDescription;
    private String hospitalId;
    private int displayBinSetNumber;
    private int quantity;
    private String modelNumber;
    private String orderStatusBinA;
    //check data type
    private String orderDateBinA;
    private String orderDateBinB;
    private String orderStatusBinB;
    
    private String displayAlert;
    private String displaySingleBinBarcode;
    private String binSetFlg;
    
    public ESLProduct() {
    }
    
    public ESLProduct(String itemMasterNumber, String productDescription, String binSetNumber, int displayBinSetNumber, int quantity, String modelNumber, String orderStatusBinA, String orderDateBinA, String orderDateBinB, String orderStatusBinB, String binSetFlg) {
        this.itemMasterNumber = itemMasterNumber;
        this.productDescription = productDescription;
        this.binSetNumber = binSetNumber;
        this.displayBinSetNumber = displayBinSetNumber;
        this.quantity = quantity;
        this.modelNumber = modelNumber;
        this.orderStatusBinA = orderStatusBinA;
        this.orderDateBinA = orderDateBinA;
        this.orderDateBinB = orderDateBinB;
        this.orderStatusBinB = orderStatusBinB;
        this.binSetFlg = binSetFlg;
    }
    
    
    public ESLProduct(String binSetNumber, boolean isOutOfStock, boolean expiryRisk, boolean expired) {
        this.binSetNumber = binSetNumber;
        this.outOfStockFlg = isOutOfStock;
        this.expiryRiskFlg = expiryRisk;
        this.expirationFlag = expired;
    }
    
    
    public ESLProduct(String itemMasterNumber, String productDescription, String binSetNumber, int displayBinSetNumber, int quantity, String modelNumber, String orderStatusBinA, String orderDateBinA, String orderDateBinB, String orderStatusBinB, String binSetFlg, String hospitalId, String displaySingleBinBarcode, String displayAlert, String binsetStatus, boolean outOfStockFlg, boolean isExpired, boolean isExpiryRisk) {
        this.itemMasterNumber = itemMasterNumber;
        this.productDescription = productDescription;
        this.binSetNumber = binSetNumber;
        this.displayBinSetNumber = displayBinSetNumber;
        this.quantity = quantity;
        this.modelNumber = modelNumber;
        this.orderStatusBinA = orderStatusBinA;
        this.orderDateBinA = orderDateBinA;
        this.orderDateBinB = orderDateBinB;
        this.orderStatusBinB = orderStatusBinB;
        this.binSetFlg = binSetFlg;
        this.hospitalId = hospitalId;
        this.displaySingleBinBarcode = displaySingleBinBarcode;
        this.displayAlert = displayAlert;
        this.binsetStatus = binsetStatus;
        this.outOfStockFlg = outOfStockFlg;
        this.expirationFlag = isExpired;
        this.expiryRiskFlg = isExpiryRisk;
    }
    
    public ESLProduct(String itemMasterNumber, String productDescription, String binSetNumber, int displayBinSetNumber, int quantity, String modelNumber, String orderStatusBinA, String orderDateBinA, String orderDateBinB, String orderStatusBinB, String binSetFlg, String hospitalId, String displaySingleBinBarcode, String displayAlert, String binsetStatus, boolean outOfStockFlg) {
        this.itemMasterNumber = itemMasterNumber;
        this.productDescription = productDescription;
        this.binSetNumber = binSetNumber;
        this.displayBinSetNumber = displayBinSetNumber;
        this.quantity = quantity;
        this.modelNumber = modelNumber;
        this.orderStatusBinA = orderStatusBinA;
        this.orderDateBinA = orderDateBinA;
        this.orderDateBinB = orderDateBinB;
        this.orderStatusBinB = orderStatusBinB;
        this.binSetFlg = binSetFlg;
        this.hospitalId = hospitalId;
        this.displaySingleBinBarcode = displaySingleBinBarcode;
        this.displayAlert = displayAlert;
        this.binsetStatus = binsetStatus;
        this.outOfStockFlg = outOfStockFlg;
    }
    
    public boolean getExpirationFlag() {
        return expirationFlag;
    }
    
    public void setExpirationFlag(boolean expirationFlag) {
        this.expirationFlag = expirationFlag;
    }
    
    
    @Override public String toString() {
        return "ESLProduct{" + "outOfStockFlg=" + outOfStockFlg + ", expiryRiskFlg=" + expiryRiskFlg + ", expiryRiskFlg=" + expiryRiskFlg + ", binSetStatus='" + binsetStatus + '\'' + ", itemMasterNumber='" + itemMasterNumber + '\'' + ", productDescription='" + productDescription + '\'' + ", binSetNumber='" + binSetNumber + '\'' + ", hospitalId='" + hospitalId + '\'' + ", displayBinSetNumber=" + displayBinSetNumber + ", quantity=" + quantity + ", modelNumber='" + modelNumber + '\'' + ", orderStatusBinA='" + orderStatusBinA + '\'' + ", orderDateBinA='" + orderDateBinA + '\'' + ", orderDateBinB='" + orderDateBinB + '\'' + ", orderStatusBinB='" + orderStatusBinB + '\'' + ", displayAlert='" + displayAlert + '\'' + ", displaySingleBinBarcode='" + displaySingleBinBarcode + '\'' + ", binSetFlg='" + binSetFlg + '\'' + '}';
    }
    
    
    public boolean getExpiryRiskFlg() {
        return expiryRiskFlg;
    }
    
    public void setExpiryRiskFlg(boolean expiryRiskFlg) {
        this.expiryRiskFlg = expiryRiskFlg;
    }
    
    public String getHospitalId() {
        return hospitalId;
    }
    
    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
    
    public String getOrderDateBinB() {
        return orderDateBinB;
    }
    
    public void setOrderDateBinB(String orderDateBinB) {
        this.orderDateBinB = orderDateBinB;
    }
    
    public String getBinSetStatus() {
        return binsetStatus;
    }
    
    public void setBinSetStatus(String binSetStatus) {
        this.binsetStatus = binSetStatus;
    }
    
    public String getItemMasterNumber() {
        return itemMasterNumber == null ? "" : itemMasterNumber;
    }
    
    public void setItemMasterNumber(String itemMasterNumber) {
        this.itemMasterNumber = itemMasterNumber;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    
    public int getDisplayBinSetNumber() {
        return displayBinSetNumber;
    }
    
    public void setDisplayBinSetNumber(int displayBinSetNumber) {
        this.displayBinSetNumber = displayBinSetNumber;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getModelNumber() {
        return modelNumber;
    }
    
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
    
    public String getOrderStatusBinA() {
        return orderStatusBinA;
    }
    
    public void setOrderStatusBinA(String orderStatusBinA) {
        this.orderStatusBinA = orderStatusBinA;
    }
    
    public String getOrderDateBinA() {
        return orderDateBinA;
    }
    
    public void setOrderDateBinA(String orderDateBinA) {
        this.orderDateBinA = orderDateBinA;
    }
    
    public String getOrderStatusBinB() {
        return orderStatusBinB;
    }
    
    public void setOrderStatusBinB(String orderStatusBinB) {
        this.orderStatusBinB = orderStatusBinB;
    }
    
    public boolean isOutOfStockFlg() {
        return outOfStockFlg;
    }
    
    public void setOutOfStockFlg(boolean outOfStockFlg) {
        this.outOfStockFlg = outOfStockFlg;
    }
    
    
    public String getDisplayAlert() {
        return displayAlert;
    }
    
    public void setDisplayAlert(String displayAlert) {
        this.displayAlert = displayAlert;
    }
    
    public String getDisplaySingleBinBarcode() {
        return displaySingleBinBarcode;
    }
    
    public void setDisplaySingleBinBarcode(String displaySingleBinBarcode) {
        this.displaySingleBinBarcode = displaySingleBinBarcode;
    }
    
    public String getBinSetFlg() {
        return binSetFlg;
    }
    
    public void setBinSetFlg(String binSetFlg) {
        this.binSetFlg = binSetFlg;
    }
    
}
