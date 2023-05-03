package fruitshop.comm.util;

import java.util.Date;

public final class DateUtil {
    private DateUtil() {
    }

    public static Date getDateOffset(long offset) {
        return new Date(new Date().getTime() + offset);
    }

    public static boolean inRange(Date effectTime, Date expireTime) {
        Date now = new Date();
        return effectTime != null
                && expireTime != null
                && expireTime.getTime() > effectTime.getTime()
                && now.getTime() > effectTime.getTime()
                && now.getTime() <= expireTime.getTime();
    }
}
