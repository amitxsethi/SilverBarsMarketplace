package com.silverbars;

import static java.lang.String.format;

import java.math.BigDecimal;

public class Order implements Comparable<Order> {
    private final OrderType orderType;
    private final String userId;
    private final BigDecimal quantityInKg;
    private final BigDecimal pricePerKg;

    public Order(String userId, OrderType orderType,
                 BigDecimal quantityInKg, BigDecimal pricePerKg) {
        this.userId = userId;
        this.orderType = orderType;
        this.quantityInKg = quantityInKg;
        this.pricePerKg = pricePerKg;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public BigDecimal getQuantityInKg() {
        return quantityInKg;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public String getUserId() {
        return userId;
    }

    public int compareTo(final Order order) {
        if (this.orderType == order.orderType) {
            if (this.orderType.equals(OrderType.BUY)) {
                return order.getPricePerKg().compareTo(this.pricePerKg);
            } else {
                return this.pricePerKg.compareTo(order.getPricePerKg());
            }
        }
        else {
            return this.orderType.compareTo(order.orderType);
        }
    }

    public String summary() {
        return format("%skg for £%s", quantityInKg, pricePerKg);
    }

}
