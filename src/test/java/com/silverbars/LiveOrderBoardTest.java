package com.silverbars;

import static java.math.BigDecimal.valueOf;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.UUID;

import com.silverbars.Order;
import com.silverbars.OrderNotFoundException;
import com.silverbars.OrderType;
import org.junit.Before;
import org.junit.Test;

public class LiveOrderBoardTest {

    private LiveOrderBoard liveOrderBoard;

    @Before
    public void setUp(){
        liveOrderBoard = new LiveOrderBoard();
    }

    @Test
    public void shouldRegisterOrder() {

        final String userId = "user1";
        final OrderType buy = OrderType.BUY;
        final BigDecimal quantityInKg = valueOf(50.5);
        final BigDecimal pricePerKg = valueOf(302);

        //Given
        final Order order = new Order(userId, buy, quantityInKg, pricePerKg);

        //when
        final UUID order1Id = liveOrderBoard.registerOrder(order);

        //then
        assertNotNull(order1Id);
        assertEquals(1, liveOrderBoard.totalOrders());

        //when
        final UUID order2Id = liveOrderBoard.registerOrder(order);

        //then
        assertNotNull(order2Id);
        assertEquals(2, liveOrderBoard.totalOrders());
        assertNotEquals(order1Id, order2Id);
    }

    @Test
    public void shouldCancelOrder() {
        final String userId = "user1";
        final OrderType buy = OrderType.BUY;
        final BigDecimal quantityInKg = valueOf(50.5);
        final BigDecimal pricePerKg = valueOf(302);

        //Given
        final Order order = new Order(userId, buy, quantityInKg, pricePerKg);
        final UUID orderId = liveOrderBoard.registerOrder(order);
        assertEquals(1, liveOrderBoard.totalOrders());

        //when
        try {
            liveOrderBoard.cancelOrder(orderId);
        } catch (final OrderNotFoundException e) {
            fail();
        }
        assertEquals(0, liveOrderBoard.totalOrders());
    }

    @Test
    public void shouldThrowOrderNotFoundExceptionWhenOrderIsNotFoundWhileCancellingOrder() {
        //Given
        final UUID randomOrderId = randomUUID();

        //when
        try {
            liveOrderBoard.cancelOrder(randomOrderId);
            fail();
        } catch (final OrderNotFoundException e) {
            // Expected this
        }
    }

    @Test
    public void testSummarize() {

        //Given
        final Order order1 = new Order("user1", OrderType.SELL, valueOf(3.5),  valueOf(306));
        liveOrderBoard.registerOrder(order1);

        final Order order2 = new Order("user2", OrderType.SELL, valueOf(1.2),  valueOf(310));
        liveOrderBoard.registerOrder(order2);

        final Order order3 = new Order("user3", OrderType.SELL, valueOf(1.5),  valueOf(307));
        liveOrderBoard.registerOrder(order3);

        final Order order4 = new Order("user4", OrderType.SELL, valueOf(2.0),  valueOf(306));
        liveOrderBoard.registerOrder(order4);

        //Given
        final Order order5 = new Order("user5", OrderType.BUY, valueOf(3.5),  valueOf(306));
        liveOrderBoard.registerOrder(order5);

        final Order order6 = new Order("user6", OrderType.BUY, valueOf(1.2),  valueOf(310));
        liveOrderBoard.registerOrder(order6);

        final Order order7 = new Order("user7", OrderType.BUY, valueOf(1.5),  valueOf(307));
        liveOrderBoard.registerOrder(order7);

        final Order order8 = new Order("user8", OrderType.BUY, valueOf(2.0),  valueOf(306));
        liveOrderBoard.registerOrder(order8);

        final String expectedSummary =
            "Live com.silverbars.Order Summary\n"+
            "=== SELL ORDERS ===\n"+
            "5.5kg for £306\n"+
            "1.5kg for £307\n"+
            "1.2kg for £310\n"+
            "=== BUY ORDERS ===\n"+
            "1.2kg for £310\n"+
            "1.5kg for £307\n"+
            "5.5kg for £306\n";

        assertEquals(expectedSummary, liveOrderBoard.summarize());
    }
}
