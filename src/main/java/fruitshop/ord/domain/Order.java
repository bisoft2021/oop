package fruitshop.ord.domain;

import fruitshop.comm.KeyedItem;
import fruitshop.comm.util.IdGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * <p>
 * 订单可以添加商品/虚拟商品(优惠券) 在同一订单优惠券只能使用一张
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Order extends KeyedItem {
    private BigDecimal totalAmount;
    private BigDecimal originalTotalAmount;
    private int status;
    private List<OrderRecord> records = new ArrayList<>();

    public static Order create(List<OrderRecord> list) {
        String orderKey = IdGenerator.uuid();
        Order order = new Order();
        order.setTotalAmount(BigDecimal.ZERO);
        order.setOriginalTotalAmount(BigDecimal.ZERO);
        order.setKey(orderKey);
        for (OrderRecord record : list) {
            OrderRecord item = OrderRecord.clone(record);
            item.setParentKey(order.getKey());
            order.getRecords().add(item);
        }
        return order;
    }
}

