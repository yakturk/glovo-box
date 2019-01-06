package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.*;

@EnableAutoConfiguration
@Component
public class OrderService {
    private final static String VIP = "vip";

    @Autowired
    CourierRepository courierRepository;

    @Autowired
    OrderRepository orderRepository;

    // gives minumum distance that a bicycle can not go
    @Value("${backender.distance_parameter_vehicle}")
    int vehicleDistanceParam;

    // if courier does not have glovo box,
    // can not transport orders which have glovo words
    @Value("#{'${backender.glovo_words}'.split(',')}")
    List<String> glovoWords;

    // gives motor vehicle lists that if courier does not have
    // one of them, can not transport every order
    @Value("#{'${backender.motor_vehicles}'.split(',')}")
     List<String> motorVehicles;

    //gives slot in meter
    @Value("${backender.slot}")
     int slot;

    // gives rank to compare orders (vip, food)
    @Value("#{'${backender.compare.order}'.split(',')}")
    List<String> compareOrders;

    /*
    finds courier via its id
    create a list and adds appropriate orders into the list
    sort the list
     */
    List<Order> getOrdersByCourier(String courierId) throws CourierNotFoundException {
        Courier courier = courierRepository.findById(courierId);
        if(courier == null)
            throw new CourierNotFoundException("Courier who has id "+ courierId +" was not found");

        List<Order> ordersForCourier =  new ArrayList<>();
        List<Order> totalOrders = orderRepository.findAll();
        for(int i=0; totalOrders != null && i<totalOrders.size(); i++)
        {
            if(!isHide(courier,totalOrders.get(i)))
                ordersForCourier.add(totalOrders.get(i));
        }

        prioritiseList(ordersForCourier, courier);
        return ordersForCourier;
    }

    /*
    sorts list before sending to controller
    sorts by vip, food and distance
     */
    private void prioritiseList(List<Order> orders, Courier courier)
    {
        Collections.sort(orders, new Comparator<Order>() {

            DistanceCalculator distanceCalculator = new DistanceCalculator();
            @Override
            public int compare(Order o1, Order o2) {
                double firstOrder_DistanceDiff = Math.abs(distanceCalculator.calculateDistance(o1.getPickup(),courier.getLocation()));
                double secondOrder_DistanceDiff = Math.abs(distanceCalculator.calculateDistance(o2.getPickup(),courier.getLocation()));
                if(firstOrder_DistanceDiff<1 && secondOrder_DistanceDiff<1)
                {
                    // convert from km to meter, if both of orders are in same slot,
                    // check firstly for vip, then food or vise versa (parametric)
                    if((int)(firstOrder_DistanceDiff * 1000)<=slot && (int)(secondOrder_DistanceDiff * 1000)<=slot)
                    {
                        int result = 0;
                        if(compareOrders.get(0).equals(VIP))
                        {
                            result = o2.getVip().compareTo(o1.getVip());
                            if(result == 0)
                                result = o2.getFood().compareTo(o1.getFood());
                        }
                        else
                        {
                            result = o2.getFood().compareTo(o1.getFood());
                            if(result == 0)
                                result = o2.getVip().compareTo(o1.getVip());
                        }
                        if(result !=0)
                            return result;
                    }
                }
                return (int)(firstOrder_DistanceDiff - secondOrder_DistanceDiff);
            }
        });
    }

    /*
    returns true to hide order from courier,
    if order description has glovo words or
    if courier's location is more than 5 km away from order location
     */
    private boolean isHide(Courier courier, Order order)
    {
        if(isHideForDistance(courier, order) || isHideForGlovoWord(courier, order))
            return true;
        return false;
    }

    /*
    returns true to hide order from courier
    if order description has a glovo word and the courier does not have a box
    */
    private boolean isHideForGlovoWord(Courier courier, Order order)
    {
        if(!courier.getBox() && glovoWords.stream().anyMatch(s->order.getDescription().toString().toLowerCase().contains(s.toLowerCase())) )
            return true;

        return false;
    }

    /*
    returns true to hide order from courier
    if the distance between order pickup location and courier location bigger than vehicleDistanceParam
    and the courier does not have a motorcycle or scooter
    */
    private boolean isHideForDistance(Courier courier, Order order)
    {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        double distance = Math.abs(distanceCalculator.calculateDistance(courier.getLocation(), order.getPickup()));
        if(distance > vehicleDistanceParam && !motorVehicles.stream().anyMatch(s->courier.getVehicle().toString().contains(s)))
            return true;
        return false;
    }
}
