package fruitshop;

import fruitshop.comm.Factory;
import fruitshop.inv.service.InventoryService;
import fruitshop.ord.domain.Order;
import fruitshop.ord.domain.OrderRecord;
import fruitshop.ord.service.OrderService;
import fruitshop.amt.domain.Price;
import fruitshop.amt.service.PriceService;
import fruitshop.comm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import static fruitshop.amt.domain.Price.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class FruitShopTest {
    OrderService orderService = (OrderService) Factory.getService("orderService");
    PriceService priceService = (PriceService) Factory.getService("priceService");
    InventoryService inventoryService = (InventoryService) Factory.getService("inventoryService");

    @BeforeEach
    void init() {
        // 初始化库存
        inventoryService.reset();
        // 商品
        String appleProductKey = "apple";
        inventoryService.addInventoryProduct(appleProductKey, Integer.MAX_VALUE);
        String strawberryProductKey = "strawberry";
        inventoryService.addInventoryProduct(strawberryProductKey, Integer.MAX_VALUE);
        // 配置商品定价规则
        Price applePrice = Price.create(appleProductKey, RULE_TYPE_UNIT_PRICE | REF_TYPE_PRODUCT_TYPE | RULE_SAME_MODE_EXCLUSION, appleProductKey, new BigDecimal("8"));
        priceService.create(applePrice);

        Price strawberryPrice = Price.create(strawberryProductKey, RULE_TYPE_UNIT_PRICE | REF_TYPE_PRODUCT_TYPE | RULE_SAME_MODE_EXCLUSION, strawberryProductKey, new BigDecimal("13"));
        priceService.create(strawberryPrice);

        log.info("上架商品: {}, {}", appleProductKey, strawberryProductKey);
    }

    @Test
    void testCustomerA() throws SQLDataException {
        // 客户A 购买水果(苹果 草莓)
        log.info("客户A下单: ");
        Order order = buyAppleStrawberry();
        // 计算总价格
        BigDecimal totalPrice = orderService.getTotalAmt(order.getKey());

        log.info("计算总价格: 客户A订单总价格: {}", totalPrice.doubleValue());
        assertEquals(142.0D, totalPrice.doubleValue(), "计算客户A订单总价格错误");
    }

    @Test
    void testCustomerB() throws SQLDataException {
        // 添加芒果到库存
        addMango();
        // 客户B 购买水果(苹果 草莓 芒果)
        log.info("客户B下单: ");
        Order order = buyAppleStrawberryMango();
        // 计算总价格
        BigDecimal totalPrice = orderService.getTotalAmt(order.getKey());

        log.info("计算总价格: 客户B订单总价格: {}", totalPrice.doubleValue());
        assertEquals(182.0D, totalPrice.doubleValue(), "计算客户B订单总价格错误");
    }

    @Test
    void testCustomerC() throws SQLDataException {
        // 添加芒果到库存
        addMango();
        // 添加促销活动(草莓限时折扣)
        log.info("草莓限时折扣: ");
        addStrawberryPromotionActivity();
        // 客户C 购买水果(苹果 草莓 芒果)
        log.info("客户C下单: ");
        Order order = buyAppleStrawberryMango();
        // 计算总价格
        BigDecimal totalPrice = orderService.getTotalAmt(order.getKey());
        log.info("计算总价格: 客户C订单总价格: {}", totalPrice.doubleValue());
        assertEquals(166.4D, totalPrice.doubleValue(), "计算客户C订单总价格错误");
    }

    @Test
    void testCustomerD() throws SQLDataException {
        // 添加芒果到库存
        addMango();
        // 添加订单满减抵扣券到库存
        addCoupon();
        // 添加促销活动(草莓限时折扣)
        log.info("草莓限时折扣: ");
        addStrawberryPromotionActivity();
        // 客户C 购买水果(苹果 草莓 芒果)并使用满减抵扣券
        log.info("客户D下单: ");
        Order order = buyAppleStrawberryMangoWithCoupon();
        // 计算总价格
        BigDecimal totalPrice = orderService.getTotalAmt(order.getKey());
        log.info("计算总价格: 客户D订单总价格: {}", totalPrice.doubleValue());
        assertEquals(156.4D, totalPrice.doubleValue(), "计算订单总价格错误");
    }

    private void addStrawberryPromotionActivity() {
        // 配置商品定价规则
        String strawberryProductKey = "strawberry";
        Price strawberryPrice = Price.create(strawberryProductKey, RULE_TYPE_UNIT_DISCOUNT | REF_TYPE_PRODUCT_TYPE | RULE_SAME_MODE_EXCLUSION, strawberryProductKey, new BigDecimal("13"), 80, DateUtil.getDateOffset(-1000 * 60 * 60 * 12), DateUtil.getDateOffset(1000 * 60 * 60 * 12));
        priceService.create(strawberryPrice);
    }

    private void addMango() {
        // 商品
        String mangoProductKey = "mango";
        inventoryService.addInventoryProduct(mangoProductKey, Integer.MAX_VALUE);
        // 配置商品定价规则
        Price mangoPrice = Price.create(mangoProductKey, RULE_TYPE_UNIT_PRICE | REF_TYPE_PRODUCT_TYPE | RULE_SAME_MODE_EXCLUSION, mangoProductKey, new BigDecimal("20"));
        priceService.create(mangoPrice);

        log.info("上架商品: {}", mangoProductKey);
    }

    private void addCoupon() {
        // 商品
        String couponProductKey = "coupon";
        inventoryService.addInventoryProduct(couponProductKey, Integer.MAX_VALUE);
        // 配置商品定价规则
        Price couponPrice = Price.create(couponProductKey, (RULE_TYPE_UNIT_COUPON | REF_TYPE_ORDER | RULE_SAME_MODE_EXCLUSION),
                couponProductKey, new BigDecimal("10"), new BigDecimal("100"), DateUtil.getDateOffset(-1000 * 60 * 60 * 12), DateUtil.getDateOffset(1000 * 60 * 60 * 12));
        priceService.create(couponPrice);

        log.info("上架商品: {}", couponProductKey);
    }

    private Order buyAppleStrawberry() {
        List<OrderRecord> list = new ArrayList<>();
        OrderRecord appleOrderRecord = new OrderRecord();
        appleOrderRecord.setKey("apple");
        appleOrderRecord.setQuality(8); // 8 斤
        list.add(appleOrderRecord);
        OrderRecord strawberryOrderRecord = new OrderRecord();
        strawberryOrderRecord.setKey("strawberry");
        strawberryOrderRecord.setQuality(6); // 6 斤
        list.add(strawberryOrderRecord);

        Order order = orderService.order(list);

        String formatStr = "购买: 苹果{}斤 单价8元/斤 草莓{}斤 单价13元/斤";
        log.info(formatStr, appleOrderRecord.getQuality(), strawberryOrderRecord.getQuality());

        return order;
    }

    private Order buyAppleStrawberryMango() {
        List<OrderRecord> list = new ArrayList<>();
        OrderRecord appleOrderRecord = new OrderRecord();
        appleOrderRecord.setKey("apple");
        appleOrderRecord.setQuality(8); // 8 斤
        list.add(appleOrderRecord);
        OrderRecord strawberryOrderRecord = new OrderRecord();
        strawberryOrderRecord.setKey("strawberry");
        strawberryOrderRecord.setQuality(6); // 6 斤
        list.add(strawberryOrderRecord);
        OrderRecord mangoOrderRecord = new OrderRecord();
        mangoOrderRecord.setKey("mango");
        mangoOrderRecord.setQuality(2); // 2 斤
        list.add(mangoOrderRecord);

        Order order = orderService.order(list);

        String formatStr = "购买: 苹果{}斤 单价8元/斤 草莓{}斤 单价13元/斤 芒果{}斤 单价20元/斤";
        log.info(formatStr, appleOrderRecord.getQuality(), strawberryOrderRecord.getQuality(), mangoOrderRecord.getQuality());
        return order;
    }

    private Order buyAppleStrawberryMangoWithCoupon() {
        List<OrderRecord> list = new ArrayList<>();
        OrderRecord appleOrderRecord = new OrderRecord();
        appleOrderRecord.setKey("apple");
        appleOrderRecord.setQuality(8); // 8 斤
        list.add(appleOrderRecord);
        OrderRecord strawberryOrderRecord = new OrderRecord();
        strawberryOrderRecord.setKey("strawberry");
        strawberryOrderRecord.setQuality(6); // 6 斤
        list.add(strawberryOrderRecord);
        OrderRecord mangoOrderRecord = new OrderRecord();
        mangoOrderRecord.setKey("mango");
        mangoOrderRecord.setQuality(2); // 2 斤
        list.add(mangoOrderRecord);
        OrderRecord couponOrderRecord = new OrderRecord();
        couponOrderRecord.setKey("coupon");
        couponOrderRecord.setQuality(1); // 1 张
        list.add(couponOrderRecord);

        Order order = orderService.order(list);

        String formatStr = "购买: 苹果{}斤 单价8元/斤 草莓{}斤 单价13元/斤 芒果{}斤 单价20元/斤";
        log.info(formatStr, appleOrderRecord.getQuality(), strawberryOrderRecord.getQuality(), mangoOrderRecord.getQuality());
        log.info("满减券: 1张");
        return order;
    }

}
