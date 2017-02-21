package com.silverbars;

import java.util.UUID;

public class LiveOrderBoard {

    private final OrderInventory orderInventory;

    public LiveOrderBoard() {
        this.orderInventory = new OrderInventory();
    }

    public UUID registerOrder(final Order order) {
        return orderInventory.registerOrder(order);
    }

    public void cancelOrder(final UUID orderId) throws OrderNotFoundException {
        orderInventory.cancelOrder(orderId);
    }

    public String summarize(){
        return orderInventory.summarize();
    }

    public int totalOrders() {
        return orderInventory.totalOrder();
    }
}
