package com.silverbars;

import java.math.BigDecimal;

public class OrderBuilder {

    private String userId;
    private BigDecimal quantityInKg;
    private BigDecimal pricePerKg;
    private OrderType orderType;

    private OrderBuilder(){
    }

    public static OrderBuilder order(){
        return new OrderBuilder();
    }

    public OrderBuilder by(final String userId) {
        this.userId = userId;
        return this;
    }

    public OrderBuilder forQuantityInKg(final BigDecimal quantityInKg) {
        this.quantityInKg = quantityInKg;
        return this;
    }

    public OrderBuilder atPricePerKg(final BigDecimal pricePerKg) {
        this.pricePerKg = pricePerKg;
        return this;
    }

    public OrderBuilder ofType(final OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public Order create() {
        return new Order(userId, orderType, quantityInKg, pricePerKg);
    }
}
