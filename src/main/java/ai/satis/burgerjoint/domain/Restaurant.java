package ai.satis.burgerjoint.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Restaurant {

    private static final int ARGUMENTS_COUNT = 12;

    private final String id;
    private final Map<Equipment, Integer> equipmentCounts;
    private final Map<Equipment, Integer> equipmentTimes;
    private final Map<Ingredient, Integer> ingredients;

    public Restaurant(String input) {

        if (StringUtils.isBlank(input)) {

            throw new IllegalArgumentException();
        }

        final String[] arguments = input.split(",");

        if (arguments.length != ARGUMENTS_COUNT) {

            throw new IllegalArgumentException();
        }

        this.id = arguments[0];

        equipmentCounts = new HashMap<>();
        equipmentTimes = new HashMap<>();
        ingredients = new HashMap<>();

        initEquipment(arguments[1], arguments[2]);
        initEquipment(arguments[3], arguments[4]);
        initEquipment(arguments[5], arguments[6]);

        initIngredient(Ingredient.BP, arguments[7]);
        initIngredient(Ingredient.L, arguments[8]);
        initIngredient(Ingredient.T, arguments[9]);
        initIngredient(Ingredient.V, arguments[10]);
        initIngredient(Ingredient.B, arguments[11]);
    }

    private void initEquipment(String count, String time) {

        final int countLength = count.length();

        if (countLength < 2) {

            throw new IllegalArgumentException();
        }

        final Equipment equipment = Equipment.valueOf(count.substring(countLength - 1));

        if (equipmentCounts.containsKey(equipment)) {

            throw new IllegalArgumentException();
        }

        try {

            final int equipmentCount = Integer.parseInt(count.substring(0, countLength - 1));
            final int equipmentTime = Integer.parseInt(time);

            equipmentCounts.put(equipment, equipmentCount);
            equipmentTimes.put(equipment, equipmentTime);

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(e);
        }
    }

    private void initIngredient(Ingredient ingredient, String count) {

        try {

            ingredients.put(ingredient, Integer.parseInt(count));

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(e);
        }
    }

    public String getId() {

        return id;
    }

    public Map<Equipment, Integer> getEquipmentCounts() {

        return equipmentCounts;
    }

    public Map<Equipment, Integer> getEquipmentTimes() {

        return equipmentTimes;
    }

    public Map<Ingredient, Integer> getIngredients() {

        return ingredients;
    }
}
