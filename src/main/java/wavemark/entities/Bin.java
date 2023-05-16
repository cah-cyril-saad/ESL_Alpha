package main.java.wavemark.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bin {
    
    String requisitionStatus;
    String state;
    String requisitionDate;
    String requisitionProductStatus;
    String requisitionProductPo;
    String binSerialnumber;
    
    public Bin() {
    }
    
    public Bin(String requisitionStatus, String state, String requisitionDate, String requisitionProductStatus, String requisitionProductPo, String binSerialnumber) {
        this.requisitionStatus = requisitionStatus;
        this.state = state;
        this.requisitionDate = requisitionDate;
        this.requisitionProductStatus = requisitionProductStatus;
        this.requisitionProductPo = requisitionProductPo;
        this.binSerialnumber = binSerialnumber;
    }
    
    public String getRequisitionStatus() {
        return requisitionStatus;
    }
    
    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }
    
    public String getState() {
        return state == null ? "" : state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getRequisitionDate() {
        return requisitionDate;
    }
    
    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
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
    
    public String getBinSerialnumber() {
        return binSerialnumber;
    }
    
    ;
    
    public void setBinSerialnumber(String binSerialnumber) {
        this.binSerialnumber = binSerialnumber;
    }
    
    @Override public String toString() {
        return "Bin{" +
               "requisitionStatus='" + requisitionStatus + '\'' +
               ", state='" + state + '\'' +
               ", requisitionDate='" + requisitionDate + '\'' +
               ", requisitionProductStatus='" + requisitionProductStatus + '\'' +
               ", requisitionProductPo='" + requisitionProductPo + '\'' +
               ", binSerialnumber='" + binSerialnumber + '\'' +
               '}';
    }
}
