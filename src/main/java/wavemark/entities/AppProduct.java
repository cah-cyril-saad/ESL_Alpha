package main.java.wavemark.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppProduct {
    
    private String itemMasterNumber;
    private String modelNumber;
    private String productDescription;
    private String endpointProductName;
    private String binsetNumber;
    private String hospitalId;
    private int logum;
    private int binsetFlag;
    private String status;
    private Bin[] bins;
    
    @Override public String toString() {
        return "AppProduct{" +
               "itemMasterNumber='" + itemMasterNumber + '\'' +
               ", modelNumber='" + modelNumber + '\'' +
               ", productDescription='" + productDescription + '\'' +
               ", endpointProductName='" + endpointProductName + '\'' +
               ", binsetNumber='" + binsetNumber + '\'' +
               ", hospitalId='" + hospitalId + '\'' +
               ", logum=" + logum +
               ", binsetFlag=" + binsetFlag +
               ", status='" + status + '\'' +
               ", bins=" + Arrays.toString(bins) +
               '}';
    }
    
    public String getHospitalId() {
        return hospitalId;
    }
    
    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
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