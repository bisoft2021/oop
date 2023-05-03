package fruitshop.amt.rule;

import fruitshop.amt.domain.Price;
import fruitshop.comm.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 折扣定价
 */
@Data
public class DiscountPricingType extends UnitPricePricingType {
    private static final int FREE_DISCOUNT = 0;
    private static final int NO_DISCOUNT = 100;

    @Override
    public BigDecimal computeAmount(Price price) {
        int discountRate = price.getDiscountRate();
        BigDecimal unitPrice = price.getUnitPrice();
        if (match(price)) {
            return unitPrice.multiply(BigDecimal.valueOf(0.01D).multiply(new BigDecimal(discountRate - 100)));
        }
        return BigDecimal.ZERO;
    }

    public boolean match(Price price) {
        int discountRate = price.getDiscountRate();
        Date effectTime = price.getEffectTime();
        Date expireTime = price.getExpireTime();

        if (DateUtil.inRange(effectTime, expireTime)
                && discountRate > FREE_DISCOUNT // 不允许免费
                && discountRate <= NO_DISCOUNT // 不打折
        ) {
            return true;
        }
        return false;
    }
}
