package ai.satis.burgerjoint.domain;

import ai.satis.burgerjoint.BurgerJointTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RestaurantTest extends BurgerJointTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputIsNull() {

        new Restaurant(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputIsVoid() {

        new Restaurant("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputHasLessArgumentsThanExpected() {

        new Restaurant("R1,4C,1,3A,2,2P,1,100,200,200,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputHasMoreArgumentsThanExpected() {

        new Restaurant("R1,4C,1,3A,2,2P,1,100,200,200,100,100,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputContainsInvalidEquipment() {

        new Restaurant("R1,4Z,1,3A,2,2P,1,100,200,200,100,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputContainsNoEquipmentCount() {

        new Restaurant("R1,AC,1,3A,2,2P,1,100,200,200,100,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputContainsInvalidEquipmentCount() {

        new Restaurant("R1,AC,1,3A,2,2P,1,100,200,200,100,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputContainsDuplicateEquipmentCount() {

        new Restaurant("R1,4C,1,3C,2,2P,1,100,200,200,100,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputContainsInvalidEquipmentTime() {

        new Restaurant("R1,4C,A,3A,2,2P,1,100,200,200,100,100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnRestaurantConstructorWhenInputContainsInvalidIngredientCount() {

        new Restaurant("R1,3C,1,3A,2,2P,1,A,200,200,100,100");
    }

    @Test
    public void shouldInitializeRestaurantWhenInputIsCorrect() {

        final Restaurant restaurant = new Restaurant("R1,4C,1,3A,2,2P,1,100,200,200,100,100");

        assertEquals("R1", restaurant.getId());
        assertEquals(3, restaurant.getEquipmentCounts().size());
        assertEquals(3, restaurant.getEquipmentTimes().size());
        assertEquals(5, restaurant.getIngredients().size());
    }
}
