import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;
    static LocalTime openingTime;
    static LocalTime closingTime;
    //REFACTOR ALL THE REPEATED LINES OF CODE

    @BeforeAll
    public static void prepare(){
        openingTime = LocalTime.parse("10:30:00");
        closingTime = LocalTime.parse("22:00:00");
    }

    @BeforeEach // No need for after each to reset, as this automatically resets the value in 1st statement
    public void setUp(){
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        //WRITE UNIT TEST CASE HERE
        Restaurant spiedRestaurant = Mockito.spy(restaurant);

        // Regular case scenario
        Mockito.when(spiedRestaurant.getCurrentTime()).thenReturn(restaurant.closingTime.minusMinutes(30));
        boolean restaurantStatus = spiedRestaurant.isRestaurantOpen();
        assertTrue(restaurantStatus);

        // Edge case scenario: Opening time
        Mockito.when(spiedRestaurant.getCurrentTime()).thenReturn(restaurant.openingTime);
        restaurantStatus = spiedRestaurant.isRestaurantOpen();
        assertTrue(restaurantStatus);
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        //WRITE UNIT TEST CASE HERE
        Restaurant spiedRestaurant = Mockito.spy(restaurant);

        // Regular scenario
        Mockito.when(spiedRestaurant.getCurrentTime()).thenReturn(restaurant.openingTime.minusMinutes(10));
        boolean restaurantStatus = spiedRestaurant.isRestaurantOpen();
        assertFalse(restaurantStatus);

        Mockito.when(spiedRestaurant.getCurrentTime()).thenReturn(restaurant.closingTime.plusHours(1));
        restaurantStatus = spiedRestaurant.isRestaurantOpen();
        assertFalse(restaurantStatus);

        // Edge case scenario
        Mockito.when(spiedRestaurant.getCurrentTime()).thenReturn(restaurant.closingTime);
        restaurantStatus = spiedRestaurant.isRestaurantOpen();
        assertFalse(restaurantStatus);
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Test
    public void calculate_order_value_should_return_0_on_adding_nothing_from_menu(){
        Integer totalValue = restaurant.calculateTotalOrderValue(new ArrayList<>());
        assertEquals(0, totalValue);
    }

    @Test
    public void calculate_order_value_should_return_0_on_passing_null_in_selected_food_item(){
        // Null check/ Edge case scenario
        Integer totalValue = restaurant.calculateTotalOrderValue(null);
        assertEquals(0, totalValue);
    }

    @Test
    public void calculate_order_value_should_return_total_value_on_adding_food_items_from_menu(){
        // Select all items in menu of restaurant and check total value
        List<String> selectedItems = restaurant.getMenu().stream().map(Item::getName).collect(Collectors.toList());
        Integer totalValue = restaurant.calculateTotalOrderValue(selectedItems);
        assertEquals(388, totalValue);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<ORDER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}