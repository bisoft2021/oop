package fruitshop.comm;

import lombok.Data;

@Data
public abstract class KeyedItem implements Item {
    private String key;
}
