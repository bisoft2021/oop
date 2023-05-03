package fruitshop.inv.domain;

/**
 * 库存.
 * <p>
 * 上架商品
 */
public class Inventory {
    private static volatile Inventory instance;

    public static Inventory getInstance() {
        if (instance == null) {
            synchronized (Inventory.class) {
                if (instance == null) {
                    instance = new Inventory();
                }
            }
        }
        return instance;
    }

    private Inventory() {
    }

    public InventoryRecord createInventoryRecord(String productTypeKey, int quality) {
        InventoryRecord item = new InventoryRecord();
        item.setKey(productTypeKey);
        item.setProductTypeKey(productTypeKey);
        item.setQuality(quality);
        return item;
    }

}
