package main.java.wavemark.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bin {
    
    String requisitionStatus;
    String state;
    String lastEmptyScan;
    String requisitionProductStatus;
    String requisitionProductPo;
    String binSerialNumber;
    String expirationFlag;
    String expirationDate;
    
    public String getBinSerialNumber() {
        return binSerialNumber;
    }
    
    public void setBinSerialNumber(String binSerialNumber) {
        this.binSerialNumber = binSerialNumber;
    }
    
    public String getExpirationFlag() {
        return expirationFlag;
    }
    
    public void setExpirationFlag(String expirationFlag) {
        this.expirationFlag = expirationFlag;
    }
    
    public String getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public Bin() {
    }
    
    public Bin(String requisitionStatus, String state, String lastEmptyScan, String requisitionProductStatus, String requisitionProductPo, String binSerialNumber) {
        this.requisitionStatus = requisitionStatus;
        this.state = state;
        this.lastEmptyScan = lastEmptyScan;
        this.requisitionProductStatus = requisitionProductStatus;
        this.requisitionProductPo = requisitionProductPo;
        this.binSerialNumber = binSerialNumber;
    }
    
    public String getRequisitionStatus() {
        return requisitionStatus;
    }
    
    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }
    
    public String getState() {
        return state == null ? "FILLED" : state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getLastEmptyScan() {
        return lastEmptyScan;
    }
    
    public void setLastEmptyScan(String lastEmptyScan) {
        this.lastEmptyScan = lastEmptyScan;
    }
    
    public String getRequisitionProductStatus() {
        return requisitionProductStatus == null ? "" : requisitionProductStatus;
    }
    
    public void setRequisitionProductStatus(String requisitionProductStatus) {
        this.requisitionProductStatus = requisitionProductStatus;
    }
    
    public String getRequisitionProductPo() {
        return requisitionProductPo;
    }
    
    public void setRequisitionProductPo(String requisitionProductPo) {
        this.requisitionProductPo = requisitionProductPo;
    }
    
    @Override public String toString() {
        return "Bin{" +
               "requisitionStatus='" + requisitionStatus + '\'' +
               ", state='" + state + '\'' +
               ", requisitionDate='" + lastEmptyScan + '\'' +
               ", requisitionProductStatus='" + requisitionProductStatus + '\'' +
               ", requisitionProductPo='" + requisitionProductPo + '\'' +
               ", binSerialNumber='" + binSerialNumber + '\'' +
               ", expirationFlag='" + expirationFlag + '\'' +
               ", expirationDate='" + expirationDate + '\'' +
               '}';
    }
}
