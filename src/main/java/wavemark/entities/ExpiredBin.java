package main.java.wavemark.entities;

public class ExpiredBin {
    
    String binSerialNumber;
    String expirationDate;
    String expirationFlag;
    
    public String getBinSerialNumber() {
        return binSerialNumber;
    }
    
    public void setBinSerialNumber(String binSerialNumber) {
        this.binSerialNumber = binSerialNumber;
    }
    
    public String getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public String getExpirationFlag() {
        return expirationFlag;
    }
    
    public void setExpirationFlag(String expirationFlag) {
        this.expirationFlag = expirationFlag;
    }
    
    @Override public String toString() {
        return "ExpiredBin{" + "binSerialNumber='" + binSerialNumber + '\'' + ", expirationDate='" + expirationDate + '\'' + ", expirationFlag='" + expirationFlag + '\'' + '}';
    }
}
