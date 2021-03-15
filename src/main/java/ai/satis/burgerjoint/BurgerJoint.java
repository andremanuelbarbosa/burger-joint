package ai.satis.burgerjoint;

import ai.satis.burgerjoint.domain.Equipment;
import ai.satis.burgerjoint.domain.Ingredient;
import ai.satis.burgerjoint.domain.Order;
import ai.satis.burgerjoint.domain.Restaurant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BurgerJoint {

    private static final int MAX_ORDER_TIME = 20;

    private final Map<String, Restaurant> restaurants;

    public BurgerJoint(List<String> lines) {

        int orders = 0;
        boolean restaurantsInitialized = false;

        restaurants = Maps.newHashMap();

        for (String line : lines) {

            if (!restaurantsInitialized) {

                try {

                    final Restaurant restaurant = new Restaurant(line);

                    if (restaurants.containsKey(restaurant.getId())) {

                        throw new IllegalArgumentException();
                    }

                    restaurants.put(restaurant.getId(), restaurant);

                    continue;

                } catch (IllegalArgumentException e) {

                    restaurantsInitialized = true;
                }
            }

            final Order order = new Order(line);

            if (!restaurants.containsKey(order.getRestaurantId())
                    || restaurants.get(order.getRestaurantId()).getOrders().containsKey(order.getId())) {

                throw new IllegalArgumentException();
            }

            restaurants.get(order.getRestaurantId()).getOrders().put(order.getId(), order);

            orders++;
        }

        if (restaurants.size() == 0 || orders == 0) {

            throw new IllegalArgumentException();
        }
    }

    public Map<String, Restaurant> getRestaurants() {

        return restaurants;
    }

    public void processOrders() {

        for (Restaurant restaurant : restaurants.values()) {

            final List<Order> orders = Lists.newArrayList(restaurant.getOrders().values());

            Collections.sort(orders);

            final Map<Equipment, List<LocalDateTime>> equipmentCompletedTimes = Maps.newHashMapWithExpectedSize(Equipment.values().length);
            Lists.newArrayList(Equipment.values()).stream().forEach(equipment ->
                    equipmentCompletedTimes.put(equipment, Lists.newArrayListWithExpectedSize(orders.size())));

            for (int i = 0; i < orders.size(); i++) {

                final Order order = orders.get(i);

                LocalDateTime orderCompletedTime = order.getDateTime();

                if (equipmentCompletedTimes.get(Equipment.C).size() < restaurant.getEquipmentCounts().get(Equipment.C)) {

                    orderCompletedTime = orderCompletedTime.plusMinutes(restaurant.getEquipmentTimes().get(Equipment.C));

                } else {

                    boolean added = false;

                    for (int j = 0; j < equipmentCompletedTimes.get(Equipment.C).size(); j++) {

                        if (equipmentCompletedTimes.get(Equipment.C).get(j).isBefore(order.getDateTime())) {

                            orderCompletedTime = orderCompletedTime.plusMinutes(restaurant.getEquipmentTimes().get(Equipment.C));

                            added = true;

                            break;
                        }
                    }

                    if (!added) {

                        orderCompletedTime = equipmentCompletedTimes.get(Equipment.C).get(equipmentCompletedTimes.get(Equipment.C).size() - 1).plusMinutes(restaurant.getEquipmentTimes().get(Equipment.C));
                    }
                }

                if (order.getDateTime().until(orderCompletedTime, ChronoUnit.MINUTES) > MAX_ORDER_TIME) {

                    order.setStatus(Order.Status.REJECTED);

                    continue;
                }

                equipmentCompletedTimes.get(Equipment.C).add(orderCompletedTime);

                if (equipmentCompletedTimes.get(Equipment.A).size() < restaurant.getEquipmentCounts().get(Equipment.A)) {

                    orderCompletedTime = orderCompletedTime.plusMinutes(restaurant.getEquipmentTimes().get(Equipment.A));

                } else {

                    boolean added = false;

                    for (int j = 0; j < equipmentCompletedTimes.get(Equipment.A).size(); j++) {

                        if (equipmentCompletedTimes.get(Equipment.A).get(j).isBefore(order.getDateTime())) {

                            orderCompletedTime = orderCompletedTime.plusMinutes(restaurant.getEquipmentTimes().get(Equipment.A));

                            added = true;

                            break;
                        }
                    }

                    if (!added) {

                        orderCompletedTime = equipmentCompletedTimes.get(Equipment.A).get(equipmentCompletedTimes.get(Equipment.A).size() - 1).plusMinutes(restaurant.getEquipmentTimes().get(Equipment.A));
                    }
                }

                if (order.getDateTime().until(orderCompletedTime, ChronoUnit.MINUTES) > MAX_ORDER_TIME) {

                    order.setStatus(Order.Status.REJECTED);

                    continue;
                }

                equipmentCompletedTimes.get(Equipment.A).add(orderCompletedTime);

                if (equipmentCompletedTimes.get(Equipment.P).size() < restaurant.getEquipmentCounts().get(Equipment.P)) {

                    orderCompletedTime = orderCompletedTime.plusMinutes(restaurant.getEquipmentTimes().get(Equipment.P));

                } else {

                    boolean added = false;

                    for (int j = 0; j < equipmentCompletedTimes.get(Equipment.P).size(); j++) {

                        if (equipmentCompletedTimes.get(Equipment.P).get(j).isBefore(order.getDateTime())) {

                            orderCompletedTime = orderCompletedTime.plusMinutes(restaurant.getEquipmentTimes().get(Equipment.P));

                            added = true;

                            break;
                        }
                    }

                    if (!added) {

                        orderCompletedTime = equipmentCompletedTimes.get(Equipment.P).get(equipmentCompletedTimes.get(Equipment.P).size() - 1).plusMinutes(restaurant.getEquipmentTimes().get(Equipment.P));
                    }
                }

                if (order.getDateTime().until(orderCompletedTime, ChronoUnit.MINUTES) > MAX_ORDER_TIME) {

                    order.setStatus(Order.Status.REJECTED);

                    continue;
                }

                final Map<Ingredient, Integer> ingredientsAfterOrder = Maps.newHashMapWithExpectedSize(Ingredient.values().length);

                for (Order.Burger burger : order.getBurgers()) {

                    ingredientsAfterOrder.put(Ingredient.BP, restaurant.getIngredients().get(Ingredient.BP) - 1);

                    for (Ingredient ingredient : burger.getIngredients()) {

                        if (!ingredientsAfterOrder.containsKey(ingredient)) {

                            ingredientsAfterOrder.put(ingredient, restaurant.getIngredients().get(ingredient));
                        }

                        ingredientsAfterOrder.put(ingredient, ingredientsAfterOrder.get(ingredient) - 1);
                    }
                }

                if (ingredientsAfterOrder.values().stream().anyMatch(count -> count < 0)) {

                    order.setStatus(Order.Status.REJECTED);

                    continue;
                }

                equipmentCompletedTimes.get(Equipment.P).add(orderCompletedTime);

                for (Ingredient ingredient : ingredientsAfterOrder.keySet()) {

                    restaurant.getIngredients().put(ingredient, ingredientsAfterOrder.get(ingredient));
                }

                order.setStatus(Order.Status.ACCEPTED);
                order.setCompletedTime(orderCompletedTime);
            }

            final AtomicLong totalTime = new AtomicLong(0);

            restaurant.getOrders().values().stream().filter(order -> order.getStatus() == Order.Status.ACCEPTED).forEach(order ->
                    totalTime.addAndGet(order.getDateTime().until(order.getCompletedTime(), ChronoUnit.MINUTES)));

            for (Order order : restaurant.getOrders().values()) {

                System.out.println(restaurant.getId() + "," + order.getId() + "," + order.getStatus() + (order.getStatus() == Order.Status.ACCEPTED ? "," + order.getDateTime().until(order.getCompletedTime(), ChronoUnit.MINUTES) : ""));
            }

            System.out.println(restaurant.getId() + ",TOTAL," + totalTime);
        }
    }

    public static void main(String[] args) {

        final List<String> lines = Lists.newArrayList();

        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        (new BurgerJoint(lines)).processOrders();
    }
}
