package fruitshop.amt.rule;

import fruitshop.amt.domain.Price;
import fruitshop.comm.util.BigDecimalUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 满减抵扣券定价
 */
@Data
public class DeductPricingType extends DiscountPricingType {
    @Override
    public BigDecimal computeAmount(Price price) {
        BigDecimal detectAmount = price.getDeductAmount();
        if (match(price)) {
            return detectAmount;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean match(Price price) {
        BigDecimal orderTotalAmount = price.getOrderTotalAmount();
        BigDecimal metDeductAmount = price.getMetDeductAmount();
        BigDecimal detectAmount = price.getDeductAmount();

        if (orderTotalAmount != null && metDeductAmount != null && detectAmount != null
                && BigDecimalUtil.positive(orderTotalAmount)
                && BigDecimalUtil.gte(orderTotalAmount, metDeductAmount)
                && BigDecimalUtil.gte(orderTotalAmount, detectAmount)) {
            return true;
        }
        return false;
    }
}
