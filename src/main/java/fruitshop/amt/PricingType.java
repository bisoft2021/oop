package fruitshop.amt;

import fruitshop.amt.domain.Price;

import java.math.BigDecimal;

/**
 * 定价规则
 */
public interface PricingType {
    BigDecimal computeAmount(Price price);

    boolean match(Price price);
}
