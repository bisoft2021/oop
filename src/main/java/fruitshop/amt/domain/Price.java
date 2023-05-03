package fruitshop.amt.domain;

import fruitshop.amt.PricingType;
import fruitshop.amt.rule.DeductPricingType;
import fruitshop.amt.rule.DiscountPricingType;
import fruitshop.amt.rule.UnitPricePricingType;
import fruitshop.comm.KeyedItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class Price extends KeyedItem {
    // flag
    // bit 0-15  规则类型 0-15
    // bit 16-19 引用目标类型
    // bit 20-22 规则应用阶段
    // bit 23    同一规则 是否允许多个
    // bit 24    不同规则 是否允许多个
    // bit 25-27 限时类型
    // bit 28    是否满额兑换
    // bit 29-31 优先级
    public static final int RULE_TYPE_UNIT_PRICE = 0;
    public static final int RULE_TYPE_UNIT_DISCOUNT = 1;
    public static final int RULE_TYPE_UNIT_COUPON = 2;
    public static final int RULE_SAME_MODE_EXCLUSION = 1 << 23;
    public static final int RULE_DIFF_MODE_EXCLUSION = 1 << 24;
    public static final int REF_TYPE_PRODUCT_TYPE = 1 << 16;
    public static final int REF_TYPE_PRODUCT = 1 << 17;
    public static final int REF_TYPE_ORDER_TYPE = 1 << 18;
    public static final int REF_TYPE_ORDER = 1 << 19;
    public static final int RULE_APPLY_SUMMERY_RECORD = 1 << 20;
    public static final int RULE_APPLY_SUMMARY_ALL = 1 << 21;
    public static final int FULL_DEDUCT = 1 << 28;
    public static final int PRIORITY_1 = 0;
    public static final int PRIORITY_2 = 1 << 29;
    public static final int PRIORITY_3 = 1 << 30;
    public static final int PRIORITY_4 = 1 << 31;

    private String refKey;
    private int flags;
    private BigDecimal unitPrice;
    private int discountRate;
    private Date effectTime;
    private Date expireTime;
    private BigDecimal deductAmount;
    private BigDecimal metDeductAmount;
    private BigDecimal orderTotalAmount; // 订单总金额

    public static PricingType getPricingRule(int ruleType) {
        PricingType pricingType;
        switch (ruleType) {
            case 1:
                pricingType = new DiscountPricingType();
                break;
            case 2:
                pricingType = new DeductPricingType();
                break;
            default:
                pricingType = new UnitPricePricingType();
                break;
        }
        return pricingType;
    }

    public static Price create(String pricingRuleKey, int flags, String refKey, BigDecimal unitPrice, int discountRate, Date effectTime, Date expireTime) {
        Price price = new Price();
        price.setKey(pricingRuleKey);
        price.setFlags(flags);
        price.setRefKey(refKey);
        price.setUnitPrice(unitPrice);
        price.setDiscountRate(discountRate);
        price.setEffectTime(effectTime);
        price.setExpireTime(expireTime);
        return price;
    }

    public static Price create(String pricingRuleKey, int flags, String refKey, BigDecimal deductAmount, BigDecimal metDeductAmount, Date effectTime, Date expireTime) {
        Price price = new Price();
        price.setKey(pricingRuleKey);
        price.setFlags(flags);
        price.setRefKey(refKey);
        price.setDeductAmount(deductAmount);
        price.setMetDeductAmount(metDeductAmount);
        price.setEffectTime(effectTime);
        price.setExpireTime(expireTime);
        return price;
    }

    public static Price create(String pricingRuleKey, int flags, String refKey, BigDecimal unitPrice) {
        Price price = new Price();
        price.setKey(pricingRuleKey);
        price.setFlags(flags);
        price.setRefKey(refKey);
        price.setUnitPrice(unitPrice);
        return price;
    }

}
