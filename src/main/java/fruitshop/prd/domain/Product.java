package fruitshop.prd.domain;

import fruitshop.comm.KeyedItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品: apple strawberry mango coupon ...
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Product extends KeyedItem {
    private String productTypeKey; // 产品类型KEY
    private String name; // 产品名字
    private Date effectTime; // 生效时间
    private Date expireTime; // 失效时间

    public static Product create(String productTypeKey) {
        Product product = new Product();
        product.setKey(productTypeKey);
        product.setName(productTypeKey);
        product.setProductTypeKey(productTypeKey);
        product.setEffectTime(new Date());
        return product;
    }

}
