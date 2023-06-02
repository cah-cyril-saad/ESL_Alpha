package main.java.wavemark.entities;

import java.util.Arrays;

public class ExpiredBinset extends BinsetProduct {
    
    ExpiredBin[] bins;
    
    public ExpiredBin[] getBins() {
        return bins;
    }
    
    public void setBins(ExpiredBin[] bins) {
        this.bins = bins;
    }
    
    @Override public String toString() {
        return "ExpiredBinset{" + "binSetNumber='" + binSetNumber + '\'' + ", bins=" + Arrays.toString(bins) + '}';
    }
}
