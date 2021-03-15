package ai.satis.burgerjoint;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BurgerJointTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnBurgerJointConstructorWhenInputIsVoid() {

        new BurgerJoint(Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnBurgerJointConstructorWhenInputHasNoRestaurants() {

        new BurgerJoint(Lists.newArrayList(
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnBurgerJointConstructorWhenInputHasNoOrders() {

        new BurgerJoint(Lists.newArrayList(
                "R1,4C,1,3A,2,2P,1,100,200,200,100,100"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionOnBurgerJointConstructorWhenInputHasDuplicateRestaurants() {

        new BurgerJoint(Lists.newArrayList(
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT",
                "R1,4C,1,3A,2,2P,1,100,200,200,100,100"));
    }

    @Test
    public void shouldInitializeBurgerJointWhenInputIsCorrect() {

        final BurgerJoint burgerJoint = new BurgerJoint(Lists.newArrayList(
                "R1,4C,1,3A,2,2P,1,100,200,200,100,100",
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT",
                "R1,2020-12-08 19:15:32,O2,VLT,VT,BLT,LT,VLT",
                "R1,2020-12-08 19:16:05,O3,VLT,VT,BLT,LT,VLT",
                "R1,2020-12-08 19:17:15,O4,BT,BLT,VLT,BLT,BT,LT,VLT",
                "R1,2020-12-08 19:19:10,O5,BLT,LT,VLT",
                "R1,2020-12-08 19:15:32,O6,VLT,VT,BLT,VLT,BT",
                "R1,2020-12-08 19:16:05,O7,VLT,LT,BLT,LT,VLT",
                "R1,2020-12-08 19:17:15,O8,BT,BLT,VLT,BLT,BLT",
                "R1,2020-12-08 19:18:15,O9,BT,BLT,VLT,BLT,BLT",
                "R1,2020-12-08 19:21:10,O10,BLT,VLT",
                "R1,2020-12-08 19:25:17,O11,VT,VLT",
                "R1,2020-12-08 19:28:17,O12,VT,VLT"));

        assertEquals(1, burgerJoint.getRestaurants().size());
        assertEquals(12, burgerJoint.getRestaurants().get("R1").getOrders().size());
    }

    @Test
    public void shouldRejectOrderOnProcessOrdersWhenCookingTimeRunsOut() {

        final BurgerJoint burgerJoint = new BurgerJoint(Lists.newArrayList(
                "R1,1C,7,3A,2,2P,1,100,200,200,100,100",
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O2,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O3,BLT,LT,VLT"));

        burgerJoint.processOrders();
    }

    @Test
    public void shouldRejectOrderOnProcessOrdersWhenAssemblingTimeRunsOut() {

        final BurgerJoint burgerJoint = new BurgerJoint(Lists.newArrayList(
                "R1,4C,3,1A,8,2P,1,100,200,200,100,100",
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O2,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O3,BLT,LT,VLT"));

        burgerJoint.processOrders();
    }

    @Test
    public void shouldRejectOrderOnProcessOrdersWhenPackagingTimeRunsOut() {

        final BurgerJoint burgerJoint = new BurgerJoint(Lists.newArrayList(
                "R1,4C,3,3A,2,1P,7,100,200,200,100,100",
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O2,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O3,BLT,LT,VLT"));

        burgerJoint.processOrders();
    }

    @Test
    public void shouldRejectOrderOnProcessOrdersWhenIngredientRunsOut() {

        final BurgerJoint burgerJoint = new BurgerJoint(Lists.newArrayList(
                "R1,4C,1,3A,2,2P,1,1,200,200,100,100",
                "R1,2020-12-08 19:15:31,O1,BLT,LT,VLT",
                "R1,2020-12-08 19:15:31,O3,BLT,LT,VLT"));

        burgerJoint.processOrders();
    }
}
