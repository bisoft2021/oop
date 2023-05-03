package fruitshop.comm.util;

import java.math.BigDecimal;

public final class BigDecimalUtil {
    private BigDecimalUtil() {
    }

    public static boolean positive(BigDecimal decimal) {
        return decimal.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean gte(BigDecimal decimal, BigDecimal other) {
        return decimal.compareTo(other) >= 0;
    }

    public static BigDecimal ensureNotNegative(BigDecimal decimal) {
        return decimal.compareTo(BigDecimal.ZERO) >= 0 ? decimal : BigDecimal.ZERO;
    }

}
