package ai.satis.burgerjoint;

import ai.satis.burgerjoint.domain.Order;
import ai.satis.burgerjoint.domain.Restaurant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class BurgerJoint {

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

            if(!restaurants.containsKey(order.getRestaurantId())) {

                throw new IllegalArgumentException();
            }

            restaurants.get(order.getRestaurantId()).getOrders().add(order);

            orders++;
        }

        if (restaurants.size() == 0 || orders == 0) {

            throw new IllegalArgumentException();
        }
    }

    public Map<String, Restaurant> getRestaurants() {

        return restaurants;
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

        new BurgerJoint(lines);
    }
}
