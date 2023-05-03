package fruitshop.inv.domain;

import fruitshop.comm.KeyedItem;
import lombok.Data;

@Data
public class InventoryRecord extends KeyedItem {
    private int quality; // 库存数量
    private String productTypeKey; // 产品类型编码

}
