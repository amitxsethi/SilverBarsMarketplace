package com.silverbars;

import static com.silverbars.OrderType.BUY;
import static com.silverbars.OrderType.SELL;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;

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
        final Order order1 = OrderBuilder.order().by("user1").ofType(BUY).forQuantityInKg(valueOf(10)).atPricePerKg(pricePerKg).create();
        final Order order2 = OrderBuilder.order().by("user2").ofType(BUY).forQuantityInKg(valueOf(20)).atPricePerKg(pricePerKg).create();

        final List<Order> orders = new OrderInventory().mergeOrdersOfSamePrice(Stream.of(order1, order2));

        assertEquals(1, orders.size());
        assertEquals(valueOf(30), orders.get(0).getQuantityInKg());
    }

    @Test
    public void shouldNotMergeDifferentPriceOrders() {
        final Order order1 = OrderBuilder.order().by("user1").ofType(SELL).forQuantityInKg(valueOf(10.1)).atPricePerKg(valueOf(100.1)).create();
        final Order order2 = OrderBuilder.order().by("user2").ofType(SELL).forQuantityInKg(valueOf(20.2)).atPricePerKg(valueOf(100.2)).create();

        final List<Order> orders = new OrderInventory().mergeOrdersOfSamePrice(Stream.of(order1, order2));

        assertEquals(2, orders.size());
    }

}