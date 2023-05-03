package fruitshop.prd.service;

import fruitshop.comm.KeyedItemRepo;
import fruitshop.comm.Service;
import fruitshop.prd.domain.Product;

public class ProductService implements Service {
    public void create(String productTypeKey) {
        Product product = Product.create(productTypeKey);
        KeyedItemRepo.getInstance().save("PRODUCT", product);
    }

}
