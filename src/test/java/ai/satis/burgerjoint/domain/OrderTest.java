package ai.satis.burgerjoint.domain;

import ai.satis.burgerjoint.BurgerJointTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest extends BurgerJointTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnOrderConstructorWhenInputIsNull() {

        new Order(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnOrderConstructorWhenInputIsVoid() {

        new Order("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnOrderConstructorWhenInputHasLessThanMinimumArguments() {

        new Order("R1,2020-12-08 21:15:31,ORDER1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnOrderConstructorWhenInputHasInvalidDateTime() {

        new Order("R1,A,ORDER1,BLT,LT,VLT");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnOrderConstructorWhenInputContainsBurgerWithInvalidIngredient() {

        new Order("R1,2020-12-08 21:15:31,ORDER1,ALT,LT,VLT");
    }

    @Test
    public void shouldInitializeOrderWhenInputIsCorrect() {

        final Order order = new Order("R1,2020-12-08 21:15:31,ORDER1,BLT,LT,VLT");

        assertEquals("R1", order.getRestaurantId());
        assertEquals("2020-12-08T21:15:31", order.getDateTime().toString());
        assertEquals("ORDER1", order.getId());
        assertEquals(3, order.getBurgers().size());
    }
}
