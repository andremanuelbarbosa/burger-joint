package ai.satis.burgerjoint.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

public class Order {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final int MIN_ARGUMENTS_COUNT = 4;

    private final String restaurantId;
    private final LocalDateTime dateTime;
    private final String id;
    private final List<Burger> burgers;

    public Order(String input) {

        if (StringUtils.isBlank(input)) {

            throw new IllegalArgumentException();
        }

        final String[] arguments = input.split(",");

        if (arguments.length < MIN_ARGUMENTS_COUNT) {

            throw new IllegalArgumentException();
        }

        restaurantId = arguments[0];

        try {

            dateTime = LocalDateTime.parse(arguments[1], DATE_TIME_FORMATTER);

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(e);
        }

        id = arguments[2];

        burgers = Lists.newArrayListWithExpectedSize(arguments.length - 3);

        for (int i = 3; i < arguments.length; i++) {

            burgers.add(new Burger(arguments[i]));
        }
    }

    public String getRestaurantId() {

        return restaurantId;
    }

    public LocalDateTime getDateTime() {

        return dateTime;
    }

    public String getId() {

        return id;
    }

    public List<Burger> getBurgers() {

        return burgers;
    }

    public static class Burger {

        private final Set<Ingredient> ingredients;

        public Burger(String input) {

            ingredients = Sets.newHashSetWithExpectedSize(input.length());

            for (int i = 0; i < input.length(); i++) {

                ingredients.add(Ingredient.valueOf(input.substring(i, i + 1)));
            }
        }

        public Set<Ingredient> getIngredients() {

            return ingredients;
        }
    }
}
