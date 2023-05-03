package fruitshop.comm;

import fruitshop.inv.service.InventoryService;
import fruitshop.amt.service.PriceService;
import fruitshop.ord.service.OrderService;
import fruitshop.prd.service.ProductService;

public class Factory {
    public static Service getService(String key) {
        Service service = new DummyService();
        switch (key) {
            case "orderService":
                service = new OrderService();
                break;
            case "productService":
                service = new ProductService();
                break;
            case "priceService":
                service = new PriceService();
                break;
            case "inventoryService":
                service = new InventoryService();
                break;
            default:
                break;
        }
        return service;
    }
}
