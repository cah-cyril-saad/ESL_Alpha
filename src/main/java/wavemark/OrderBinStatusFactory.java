package main.java.wavemark;

import org.apache.commons.lang3.StringUtils;

public class OrderBinStatusFactory {
    
    
    public static String orderBinStatusResolver(Bin bin) {
        boolean isBlankOrderBinStatus;
        boolean isBackOrderedOrderBinStatus;
        boolean isOrderedBinStatus;
        
        String requisitionStatus = bin.getRequisitionStatus();
        String requisitionProductStatus = bin.getRequisitionProductStatus();
        String requisitionProductPO = bin.getRequisitionProductPo();
        
//        isBlankOrderBinStatus = requisitionStatus.equals(RequisitionStatus.NEW.toString());
//        isBlankOrderBinStatus |= requisitionStatus.equals(RequisitionStatus.RELEASED.toString());
//        isBlankOrderBinStatus |= (requisitionStatus.equals(RequisitionStatus.SUBMITTED.toString()) && !StringUtils.isBlank(requisitionProductPO));
//        isBlankOrderBinStatus |= (requisitionStatus.equals(RequisitionStatus.OPEN.toString()) && requisitionProductStatus.equals(RequisitionStatus.OPEN.toString()) && StringUtils.isBlank(requisitionProductPO));
//        isBlankOrderBinStatus |= requisitionStatus.equals(RequisitionStatus.CLOSED.toString());
//        isBlankOrderBinStatus |= requisitionStatus.equals(RequisitionStatus.COMPLETED.toString());
//
//        if (isBlankOrderBinStatus) {
//            return "";
//        }
        
        isBackOrderedOrderBinStatus = requisitionStatus.equals(RequisitionStatus.OPEN.toString()) && requisitionProductStatus.equals(RequisitionProductStatus.BACKORDERED.toString());
        isBackOrderedOrderBinStatus |= requisitionStatus.equals(RequisitionStatus.BACKORDERED.toString());
        if (isBackOrderedOrderBinStatus) {
            return RequisitionStatus.BACKORDERED.toString();
        }
        
        isOrderedBinStatus = requisitionStatus.equals(RequisitionStatus.SUBMITTED.toString()) && !StringUtils.isBlank(requisitionProductPO);
        isOrderedBinStatus |= (requisitionStatus.equals(RequisitionStatus.OPEN.toString()) && requisitionProductStatus.equals(RequisitionStatus.OPEN.toString()) && !StringUtils.isBlank(requisitionProductPO));
    
        if(isOrderedBinStatus) {
            return RequisitionStatus.ORDERED.toString();
        }
        
        return "";
    }
    
}
