package fruitshop.amt.rule;

import fruitshop.amt.domain.Price;
import fruitshop.amt.PricingType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 单价定价
 */
@Data
public class UnitPricePricingType implements PricingType {
    @Override
    public BigDecimal computeAmount(Price price) {
        if (match(price)) {
            return price.getUnitPrice();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean match(Price price) {
        return true;
    }
}
