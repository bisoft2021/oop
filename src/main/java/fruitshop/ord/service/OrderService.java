package fruitshop.ord.service;

import com.google.common.collect.Lists;
import fruitshop.comm.Factory;
import fruitshop.amt.PricingType;
import fruitshop.amt.domain.Price;
import fruitshop.amt.rule.DeductPricingType;
import fruitshop.amt.service.PriceService;
import fruitshop.comm.KeyedItemRepo;
import fruitshop.comm.Service;
import fruitshop.ord.domain.Order;
import fruitshop.ord.domain.OrderRecord;

import java.math.BigDecimal;
import java.util.List;

import static fruitshop.amt.domain.Price.*;
import static fruitshop.comm.util.BigDecimalUtil.ensureNotNegative;

public class OrderService implements Service {
    /**
     * 客户下单.
     *
     * @param list 下单列表
     */
    public Order order(List<OrderRecord> list) {
        Order order = Order.create(list);
        KeyedItemRepo.getInstance().save("ORDER", order);
        return order;
    }

    /**
     * 计算订单总金额
     * <p>
     * 1 计算订单中所有商品折扣后的总金额并汇总
     * 2 计算订单的满减后的总金额（限制满减只能使用一张）
     *
     * @param orderKey 订单KEY
     * @return 订单总金额
     */
    public BigDecimal getTotalAmt(String orderKey) {
        List<Price> couponList = Lists.newArrayList();
        // summary order record amount
        BigDecimal sum = summaryOrderRecordAmt(orderKey, couponList);
        // summary order amount
        sum = summaryOrderAmt(sum, couponList);

        return ensureNotNegative(sum);
    }

    private static BigDecimal summaryOrderRecordAmt(String orderKey, List<Price> couponList) {
        Order order = (Order) KeyedItemRepo.getInstance().find("ORDER", orderKey);
        PriceService priceService = (PriceService) Factory.getService("priceService");
        BigDecimal sum = BigDecimal.ZERO;

        for (OrderRecord item : order.getRecords()) {
            String productTypeKey = item.getKey();
            List<Price> priceList = priceService.get(productTypeKey);
            for (Price price : priceList) {
                int ruleType = price.getFlags() & 0xFFFF;
                if (ruleType == RULE_TYPE_UNIT_COUPON) {
                    couponList.add(price);
                } else {
                    PricingType pricingType = Price.getPricingRule(ruleType);
                    BigDecimal unitPrice = pricingType.computeAmount(price);
                    int quality = item.getQuality();
                    sum = sum.add(unitPrice.multiply(new BigDecimal(quality)));
                }
            }
        }
        order.setTotalAmount(sum);
        return sum;
    }

    private BigDecimal summaryOrderAmt(BigDecimal sum, List<Price> list) {
        for (Price price : list) {
            int ruleType = price.getFlags() & 0xFFFF;
            if (ruleType == RULE_TYPE_UNIT_COUPON) {
                price.setOrderTotalAmount(sum);
                DeductPricingType pricing = (DeductPricingType) Price.getPricingRule(ruleType);
                BigDecimal unitPrice = pricing.computeAmount(price);
                sum = sum.subtract(unitPrice);

                if ((price.getFlags() & RULE_SAME_MODE_EXCLUSION) == 1) {
                    break;
                }
            }
        }
        return sum;
    }
}
