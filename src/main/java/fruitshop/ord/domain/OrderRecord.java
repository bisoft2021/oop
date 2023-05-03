package fruitshop.ord.domain;

import fruitshop.comm.KeyedItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRecord extends KeyedItem {
    private String parentKey; // 订单KEY
    private int quality; // 数量
    private int unitCode; // 单位
    private BigDecimal deductAmount; // 抵扣金额
    private BigDecimal totalPrice; // 总价格
    private BigDecimal discountAmount; // 折扣金额

    public static OrderRecord clone(OrderRecord record) {
        if (!OrderRecord.isValid(record)) {
            return new OrderRecord();
        }
        OrderRecord item = new OrderRecord();
        int quality = record.getQuality();
        String key = record.getKey();
        item.setKey(key); // may be invalid or repeat
        item.setQuality(quality);
        item.setDeductAmount(BigDecimal.ZERO);
        item.setDiscountAmount(BigDecimal.ZERO);
        item.setTotalPrice(BigDecimal.ZERO);
        return item;
    }

    private static boolean isValid(OrderRecord item) {
        return null != item && item.getQuality() > 0 && null != item.getKey() && !item.getKey().isEmpty();
    }
}