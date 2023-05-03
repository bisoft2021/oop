package fruitshop.comm.util;

import java.util.UUID;

public final class IdGenerator {
    private IdGenerator() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
