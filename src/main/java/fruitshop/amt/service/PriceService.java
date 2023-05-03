package fruitshop.amt.service;

import fruitshop.comm.KeyedItemRepo;
import fruitshop.comm.Service;
import fruitshop.amt.domain.Price;

import java.util.List;

public class PriceService implements Service {
    public void create(Price price) {
        KeyedItemRepo.getInstance().save("PRICE", price);
    }

    public List<Price> get(String chargingRuleKey) {
        return (List) KeyedItemRepo.getInstance().findList("PRICE", chargingRuleKey);
    }
}
