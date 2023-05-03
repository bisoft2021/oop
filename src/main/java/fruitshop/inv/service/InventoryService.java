package fruitshop.inv.service;

import fruitshop.comm.Factory;
import fruitshop.comm.KeyedItemRepo;
import fruitshop.comm.Service;
import fruitshop.inv.domain.Inventory;
import fruitshop.inv.domain.InventoryRecord;
import fruitshop.prd.service.ProductService;

/**
 * 库存领域服务
 */
public class InventoryService implements Service {
    public void addInventoryProduct(String productTypeKey, int quality) {
        ProductService productService = (ProductService) Factory.getService("productService");
        productService.create(productTypeKey);

        InventoryRecord item = Inventory.getInstance().createInventoryRecord(productTypeKey, quality);
        KeyedItemRepo.getInstance().save("INVENTORY", item);
    }

    public void reset() {
        KeyedItemRepo.getInstance().delete();
    }

}
