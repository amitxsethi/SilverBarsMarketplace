package com.silverbars;

import java.util.UUID;

public class LiveOrderBoard {

    private final OrderInventory orderInventory;

    public LiveOrderBoard(final OrderInventory orderInventory) {
        this.orderInventory = orderInventory;
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
