package com.silverbars;

import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import com.silverbars.Order;
import com.silverbars.OrderType;
import org.junit.Assert;
import org.junit.Test;

public class OrderInventoryTest {

    @Test
    public void shouldMergeSamePriceOrders() {
        final BigDecimal pricePerKg = valueOf(300);
        final OrderType buy = OrderType.BUY;
        Order order1 = new Order("user1", buy, valueOf(10), pricePerKg);
        Order order2 = new Order("user2", buy, valueOf(20), pricePerKg);

        final List<Order> orders = new OrderInventory().mergeOrdersOfSamePrice(Stream.of(order1, order2));
        Assert.assertEquals(1, orders.size());
        Assert.assertEquals(valueOf(30), orders.get(0).getQuantityInKg());
    }

    @Test
    public void shouldNotMergeDifferentPriceOrders() {
        final OrderType sell = OrderType.SELL;
        Order order1 = new Order("user1", sell, valueOf(10), valueOf(300.1));
        Order order2 = new Order("user2", sell, valueOf(20), valueOf(300.2));

        final List<Order> orders = new OrderInventory().mergeOrdersOfSamePrice(Stream.of(order1, order2));
        Assert.assertEquals(2, orders.size());
    }

}