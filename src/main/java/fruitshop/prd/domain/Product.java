package fruitshop.prd.domain;

import fruitshop.comm.KeyedItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品: apple strawberry mango coupon ...
 */
@Data
public class Product extends KeyedItem {
    private String productTypeKey; // 产品类型KEY
    private String name; // 产品名字
    private Date effectTime; // 生效时间
    private Date expireTime; // 失效时间
    private boolean isVirtual; // 虚拟产品
    private BigDecimal unitPrice; // 单价

    public static Product create(String productTypeKey) {
        Product product = new Product();
        product.setKey(productTypeKey);
        product.setName(productTypeKey);
        product.setProductTypeKey(productTypeKey);
        product.setEffectTime(new Date());
        product.setUnitPrice(BigDecimal.ZERO);
        product.setVirtual(productTypeKey != "'Coupon" ? true : false);
        return product;
    }

}
