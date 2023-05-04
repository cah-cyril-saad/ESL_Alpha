public class ESLProduct {
    
    boolean outOfStockFlg;
    boolean expiryRiskFlg;
    boolean expirationFlg;
    private String binSetStatus;
    private String itemMasterNumber;
    private String productDescription;
    private String binSetNumber;
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

    public String getOrderDateBinB() {
        return orderDateBinB;
    }

    public void setOrderDateBinB(String orderDateBinB) {
        this.orderDateBinB = orderDateBinB;
    }
    
    public String getBinSetStatus() {
        return binSetStatus;
    }
    
    public void setBinSetStatus(String binSetStatus) {
        this.binSetStatus = binSetStatus;
    }
    
    public String getItemMasterNumber() {
        return itemMasterNumber;
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
    
    public String getBinSetNumber() {
        return binSetNumber;
    }
    
    public void setBinSetNumber(String binSetNumber) {
        this.binSetNumber = binSetNumber;
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
    
    public boolean isExpiryRiskFlg() {
        return expiryRiskFlg;
    }
    
    public void setExpiryRiskFlg(boolean expiryRiskFlg) {
        this.expiryRiskFlg = expiryRiskFlg;
    }
    
    public boolean isExpirationFlg() {
        return expirationFlg;
    }
    
    public void setExpirationFlg(boolean expirationFlg) {
        this.expirationFlg = expirationFlg;
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
