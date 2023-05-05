package main.java.wavemark;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppProduct {
    
    private String itemMasterNumber;
    private String modelNumber;
    private String productDescription;
    private String endpointProductName;
    private String binsetNumber;
    private int logum;
    private int binsetFlag;
    private String status;
    private Bin[] bins;
    
    public AppProduct() {
    }
    
    public AppProduct(String itemMasterNumber, String modelNumber, String productDescription, String endpointProductName, String binsetNumber, int logum, int binsetFlag, Bin[] bins, String status) {
        this.itemMasterNumber = itemMasterNumber;
        this.modelNumber = modelNumber;
        this.productDescription = productDescription;
        this.endpointProductName = endpointProductName;
        this.binsetNumber = binsetNumber;
        this.logum = logum;
        this.binsetFlag = binsetFlag;
        this.bins = bins;
        this.status = status;
    }
    
    @Override public String toString() {
        return "Product{" +
               "itemMasterNumber='" + itemMasterNumber + '\'' +
               ", modelNumber='" + modelNumber + '\'' +
               ", productDescription='" + productDescription + '\'' +
               ", endpointProductName='" + endpointProductName + '\'' +
               ", binsetNumber='" + binsetNumber + '\'' +
               ", logum=" + logum +
               ", binsetFlag=" + binsetFlag +
               ", bins=" + Arrays.toString(bins) +
               '}';
    }
    
    public String getItemMasterNumber() {
        return itemMasterNumber;
    }
    
    public void setItemMasterNumber(String itemMasterNumber) {
        this.itemMasterNumber = itemMasterNumber;
    }
    
    public String getModelNumber() {
        return modelNumber;
    }
    
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public String getEndpointProductName() {
        return endpointProductName;
    }
    
    public void setEndpointProductName(String endpointProductName) {
        this.endpointProductName = endpointProductName;
    }
    
    public String getBinsetNumber() {
        return binsetNumber;
    }
    
    public void setBinsetNumber(String binsetNumber) {
        this.binsetNumber = binsetNumber;
    }
    
    public int getLogum() {
        return logum;
    }
    
    public void setLogum(int logum) {
        this.logum = logum;
    }
    
    public int getBinsetFlag() {
        return binsetFlag;
    }
    
    public void setBinsetFlag(int binsetFlag) {
        this.binsetFlag = binsetFlag;
    }
    
    public Bin[] getBins() {
        return bins;
    }
    
    public void setBins(Bin[] bins) {
        this.bins = bins;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
}