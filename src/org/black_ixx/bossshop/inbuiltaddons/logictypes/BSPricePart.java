package org.black_ixx.bossshop.inbuiltaddons.logictypes;

import org.black_ixx.bossshop.core.prices.BSPriceType;

public class BSPricePart {

    private BSPriceType pricetype;
    private Object price;

    public BSPricePart(BSPriceType pricetype, Object price) {
        this.price = price;
        this.pricetype = pricetype;
    }

    public BSPriceType getPriceType() {
        return pricetype;
    }

    public Object getPrice() {
        return price;
    }
}
