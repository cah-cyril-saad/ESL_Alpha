package main.java.wavemark.controller;

import main.java.wavemark.entities.Bin;
import main.java.wavemark.enums.RequisitionProductStatus;
import main.java.wavemark.enums.RequisitionStatus;
import org.apache.commons.lang3.StringUtils;

public class OrderBinStatusFactory {
    
    
    public static String orderBinStatusResolver(Bin bin) {
        boolean isBlankOrderBinStatus;
        boolean isBackOrderedOrderBinStatus;
        boolean isOrderedBinStatus;
        
        String requisitionStatus = bin.getRequisitionStatus() == null ? "" : bin.getRequisitionStatus();
        String requisitionProductStatus = bin.getRequisitionProductStatus() == null ? "" : bin.getRequisitionProductStatus();
        String requisitionProductPO = bin.getRequisitionProductPo() == null ? "" : bin.getRequisitionProductPo();
        
        isBackOrderedOrderBinStatus = requisitionStatus.equals(RequisitionStatus.OPEN.toString()) && requisitionProductStatus.equals(RequisitionProductStatus.BACKORDERED.toString());
        isBackOrderedOrderBinStatus |= requisitionStatus.equals(RequisitionStatus.BACKORDERED.toString());
        if (isBackOrderedOrderBinStatus) {
            return RequisitionStatus.BACKORDERED.toString();
        }
        
        isOrderedBinStatus = requisitionStatus.equals(RequisitionStatus.SUBMITTED.toString()) && !StringUtils.isBlank(requisitionProductPO);
        isOrderedBinStatus |= (requisitionStatus.equals(RequisitionStatus.OPEN.toString()) && requisitionProductStatus.equals(RequisitionStatus.OPEN.toString()) && !StringUtils.isBlank(requisitionProductPO));
        
        if (isOrderedBinStatus) {
            return RequisitionStatus.ORDERED.toString();
        }
        
        return "";
    }
    
}
