package com.silverbars;

import static com.silverbars.OrderType.BUY;
import static com.silverbars.OrderType.SELL;
import static java.math.BigDecimal.valueOf;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LiveOrderBoardTest {

    private LiveOrderBoard liveOrderBoard;
    private OrderBuilder orderBuilder;

    @Before
    public void setUp(){
        liveOrderBoard = new LiveOrderBoard(new OrderInventory());
        orderBuilder = OrderBuilder.order();
    }

    @Test
    public void shouldRegisterOrder() {

        //Given
        final Order order = orderBuilder
                .by("user1")
                .ofType(BUY)
                .forQuantityInKg(valueOf(50.5))
                .atPricePerKg(valueOf(302))
                .create();

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

        //Given
        final Order order = orderBuilder
                .by("user1")
                .ofType(BUY)
                .forQuantityInKg(valueOf(50.5))
                .atPricePerKg(valueOf(302))
                .create();
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
    public void shouldSummarizeOrdersAndSortThemBasedOnPricePerKgAscForSellAndDescForBuy() {

        //Given
        final Order order1 = orderBuilder.by("user1").ofType(SELL).forQuantityInKg(valueOf(3.5)).atPricePerKg(valueOf(306)).create();
        liveOrderBoard.registerOrder(order1);

        final Order order2 = orderBuilder.by("user2").ofType(SELL).forQuantityInKg(valueOf(1.2)).atPricePerKg(valueOf(310)).create();
        liveOrderBoard.registerOrder(order2);

        final Order order3 = orderBuilder.by("user3").ofType(SELL).forQuantityInKg(valueOf(1.5)).atPricePerKg(valueOf(307)).create();
        liveOrderBoard.registerOrder(order3);

        final Order order4 = orderBuilder.by("user4").ofType(SELL).forQuantityInKg(valueOf(2.0)).atPricePerKg(valueOf(306)).create();
        liveOrderBoard.registerOrder(order4);

        final Order order5 = orderBuilder.by("user5").ofType(BUY).forQuantityInKg(valueOf(3.5)).atPricePerKg(valueOf(306)).create();
        liveOrderBoard.registerOrder(order5);

        final Order order6 = orderBuilder.by("user6").ofType(BUY).forQuantityInKg(valueOf(1.2)).atPricePerKg(valueOf(310)).create();
        liveOrderBoard.registerOrder(order6);

        final Order order7 = orderBuilder.by("user7").ofType(BUY).forQuantityInKg(valueOf(1.5)).atPricePerKg(valueOf(307)).create();
        liveOrderBoard.registerOrder(order7);

        final Order order8 = orderBuilder.by("user8").ofType(BUY).forQuantityInKg(valueOf(2.0)).atPricePerKg(valueOf(306)).create();
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
