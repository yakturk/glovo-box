package com.glovoapp.backender;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;


class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Spy
    CourierRepository courierRepository;

    @Spy
    OrderRepository orderRepository;

    // Before test, parameters in application.properties are set
    @BeforeEach
    void setUp() {
        List<String> motorVehicles = Arrays.asList("MOTORCYCLE, ELECTRIC_SCOOTER".split("\\s*,\\s*"));
        List<String> glovoWords = Arrays.asList("pizza,cake,flamingo".split("\\s*,\\s*"));
        List<String> compareOrders = Arrays.asList("vip,food".split("\\s*,\\s*"));
        int vehicleDistanceParam = 5;
        int slot = 500;

        MockitoAnnotations.initMocks(this);
        orderService.motorVehicles = motorVehicles;
        orderService.compareOrders = compareOrders;
        orderService.glovoWords = glovoWords;
        orderService.vehicleDistanceParam = vehicleDistanceParam;
        orderService.slot = slot;
    }

    // To change compareOrders of parametric variable
    private void makeVipFirst()
    {
        List<String> compareOrders = Arrays.asList("vip,food".split("\\s*,\\s*"));
        orderService.compareOrders = compareOrders;
    }

    // To change compareOrders of parametric variable
    private void makeFoodFirst()
    {
        List<String> compareOrders = Arrays.asList("food,vip".split("\\s*,\\s*"));
        orderService.compareOrders = compareOrders;
    }

    /*
    hasGlovoBox = false => courier does not have glovo box
    hasMotoVehicle = false => courier does not have motor vehicle
    isVipCheck = true => vip first to prioritise
     */
    @Test
    void getOrdersByCourier2() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeVipFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-2");
            expectedList = getList(false,false,true);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = false => courier does not have glovo box
    hasMotoVehicle = false => courier does not have motor vehicle
    isVipCheck = false => food first to prioritise
    */
    @Test
    void getOrdersByCourier2_2() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeFoodFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-2");
            expectedList = getList(false,false,false);

        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = true => courier have glovo box
    hasMotoVehicle = false => courier does not have motor vehicle
    isVipCheck = true => vip first to prioritise
    */
    @Test
    void getOrdersByCourier3() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeVipFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-3");
            expectedList = getList(true,false,true);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = true => courier has glovo box
    hasMotoVehicle = false => courier does not have motor vehicle
    isVipCheck = false => food first to prioritise
    */
    @Test
    void getOrdersByCourier3_2() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeFoodFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-3");
            expectedList = getList(true,false,false);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = false => courier does not have glovo box
    hasMotoVehicle = true => courier have motor vehicle
    isVipCheck = true => vip first to prioritise
    */
    @Test
    void getOrdersByCourier4() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeVipFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-4");
            expectedList = getList(false,true,true);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = false => courier does not have glovo box
    hasMotoVehicle = true => courier have motor vehicle
    isVipCheck = false => food first to prioritise
    */
    @Test
    void getOrdersByCourier4_2() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeFoodFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-4");
            expectedList = getList(false,true,false);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = true => courier have glovo box
    hasMotoVehicle = true => courier has motor vehicle
    isVipCheck = true => vip first to prioritise
    */
    @Test
    void getOrdersByCourier5() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeVipFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-5");
            expectedList = getList(true,true,true);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    /*
    hasGlovoBox = true => courier have glovo box
    hasMotoVehicle = true => courier has motor vehicle
    isVipCheck = false => food first to prioritise
    */
    @Test
    void getOrdersByCourier5_2() {
        List<Order> orderList = null;
        List<Order> expectedList = null;
        makeFoodFirst();
        try
        {
            orderList= orderService.getOrdersByCourier("courier-5");
            expectedList = getList(true,true,false);
        }
        catch (CourierNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedList,orderList));
    }

    @Test
    void getOrdersByWrongCourierId()
    {
        assertThrows(CourierNotFoundException.class, () -> {
            orderService.getOrdersByCourier("courier-666");
        });
    }

    /*
    Creates test data
    This results are compared with results of OrderService
     */
    private List<Order> getList(boolean hasGlovoBox, boolean hasMotoVehicle, boolean isVipCheck)
    {
        Order order1 = new Order().withId("order-1")
                .withVip(false)
                .withFood(true)
                .withDescription("I want a pizza cut into very small slices")
                .withPickup(new Location( 41.3965463,2.1963997))
                .withDelivery(new Location(41.407834,2.1675979));

        Order order2 = new Order().withId("order-2")
                .withVip(false)
                .withFood(true)
                .withDescription("2x Tuna poke with Salad")
                .withPickup(new Location( 41.36440729233118,2.1780786691277085))
                .withDelivery(new Location(41.3883091953211,2.165498064910881));

        Order order3 = new Order().withId("order-3")
                .withVip(true)
                .withFood(false)
                .withDescription("An adult flamingo")
                .withPickup(new Location( 41.36440729233118,2.1780786691277085))
                .withDelivery(new Location(41.4083091953211,2.165498064910881));

        Order order4 = new Order().withId("order-4")
                .withVip(false)
                .withFood(true)
                .withDescription("1x Pizza with Fries\\n2x Burger with Fries\\nCheesecake")
                .withPickup(new Location( 41.36440729233118,2.1780786691277085))
                .withDelivery(new Location(41.4083091953211,2.165498064910881));

        Order order5 = new Order().withId("order-5")
                .withVip(false)
                .withFood(true)
                .withDescription("1x Tuna poke with Fries\\n1x Kebab with Fries\\n1x with Salad\\n1x Hot dog with Salad")
                .withPickup(new Location( 41.38490681247524,2.1661962085744095))
                .withDelivery(new Location(41.39682466438664,2.1702260386271477));

        Order order6 = new Order().withId("order-6")
                .withVip(true)
                .withFood(false)
                .withDescription("1x Burger with Salad")
                .withPickup(new Location( 41.38490681247524,2.1661962085744095))
                .withDelivery(new Location(41.39682466438664,2.1702260386271477));

        Order order7 = new Order().withId("order-7")
                .withVip(false)
                .withFood(false)
                .withDescription("Keys")
                .withPickup(new Location( 41.38490681247524,2.1661962085744095))
                .withDelivery(new Location(41.39682466438664,2.1702260386271477));

        Order order8 = new Order().withId("order-8")
                .withVip(false)
                .withFood(true)
                .withDescription("Small box delivery")
                .withPickup(new Location( 41.45711242196780,2.1611940451935747))
                .withDelivery(new Location(41.46711242196780,2.1630517090320933));

        List<Order> orderList = new ArrayList<>();

        if(!hasGlovoBox)
        {
            if(isVipCheck)
            {
                orderList.add(order6);
                orderList.add(order5);
                orderList.add(order7);
                orderList.add(order2);
            }
            else
            {
                orderList.add(order5);
                orderList.add(order6);
                orderList.add(order7);
                orderList.add(order2);
            }
        }
        else {
            if(isVipCheck)
            {
                orderList.add(order6);
                orderList.add(order5);
                orderList.add(order7);
                orderList.add(order1);
                orderList.add(order2);
                orderList.add(order3);
                orderList.add(order4);
            }
            else
            {
                orderList.add(order5);
                orderList.add(order6);
                orderList.add(order7);
                orderList.add(order1);
                orderList.add(order2);
                orderList.add(order3);
                orderList.add(order4);
            }
        }
        if(hasMotoVehicle)
            orderList.add(order8);

        return orderList;
    }
}