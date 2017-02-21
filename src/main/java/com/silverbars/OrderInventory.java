package com.silverbars;

import static java.math.BigDecimal.ZERO;
import static java.util.UUID.randomUUID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.silverbars.Order;
import com.silverbars.OrderNotFoundException;
import com.silverbars.OrderType;

public class OrderInventory {

    private final ConcurrentHashMap<UUID, Order> orderBookings =
            new ConcurrentHashMap<>();
    
    public synchronized UUID registerOrder(final Order order) {
        final UUID orderId = randomUUID();
        orderBookings.put(orderId, order);
        return orderId;
    }

    public synchronized void cancelOrder(final UUID orderId) throws OrderNotFoundException {
        if (orderBookings.containsKey(orderId)) {
            orderBookings.remove(orderId);
        } else {
            throw new OrderNotFoundException();
        }
    }

    public synchronized String summarize() {
        StringBuilder summaryBuilder = new StringBuilder("Live com.silverbars.Order Summary\n");
        summaryBuilder.append("=== SELL ORDERS ===\n");
        summaryBuilder.append(summarizeOrders(sellOrders()));
        summaryBuilder.append("=== BUY ORDERS ===\n");
        summaryBuilder.append(summarizeOrders(buyOrders()));
        return summaryBuilder.toString();
    }

    private String summarizeOrders(final Stream<Order> orders) {
        StringBuilder summaryBuilder = new StringBuilder();
        orders.forEach(order -> {
            summaryBuilder.append(order.summary());
            summaryBuilder.append("\n");
        });
        return summaryBuilder.toString();
    }

    public Stream<Order> buyOrders() {
        Stream<Order> buyOrders = orderBookings
                .values()
                .stream()
                .filter(order -> order.getOrderType() == OrderType.BUY);

        List<Order> mergedBuyOrders = mergeOrdersOfSamePrice(buyOrders);
        return mergedBuyOrders.stream().sorted();
    }

    public Stream<Order> sellOrders() {
        Stream<Order> sellOrders = orderBookings
                .values()
                .stream()
                .filter(order -> order.getOrderType() == OrderType.SELL);

        List<Order> mergedSellOrders = mergeOrdersOfSamePrice(sellOrders);
        return mergedSellOrders.stream().sorted();
    }

    protected List<Order> mergeOrdersOfSamePrice(final Stream<Order> orderStream) {
        final Map<BigDecimal, List<Order>> collect = orderStream.collect(Collectors.groupingBy(Order::getPricePerKg));
        List<Order> mergedOrders = new ArrayList<>();

        collect.values().stream().filter(orderList->orderList.size() > 1).forEach(ordersOfSamePricePerKg -> {
            final BigDecimal[] totalOrderQuantity = {ZERO};
            final Order orderOfSamePricePerKg = ordersOfSamePricePerKg.get(0);
            ordersOfSamePricePerKg.forEach(order -> totalOrderQuantity[0] = totalOrderQuantity[0].add(order.getQuantityInKg()));
            mergedOrders.add(OrderBuilder.order()
                    .by("MergedOrder")
                    .ofType(orderOfSamePricePerKg.getOrderType())
                    .forQuantityInKg(totalOrderQuantity[0])
                    .atPricePerKg(orderOfSamePricePerKg.getPricePerKg()).create()
            );
        });
        collect.values().stream().filter(orderList->orderList.size()==1).forEach(
                singleOrderList->mergedOrders.add(singleOrderList.get(0)));

        return mergedOrders;
    }

    public int totalOrder() {
        return orderBookings.size();
    }

}
