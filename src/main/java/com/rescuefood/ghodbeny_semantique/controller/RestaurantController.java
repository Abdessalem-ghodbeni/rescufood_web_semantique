package com.rescuefood.ghodbeny_semantique.controller;

import com.rescuefood.ghodbeny_semantique.model.Restaurant;
import com.rescuefood.ghodbeny_semantique.services.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")

public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(path = "situe/tunisie")
    public List<Restaurant> getRestaurantsInTunis() {
        return restaurantService.getRestaurantsInTunis();
    }


    @GetMapping(path = "all")
    public List<Restaurant> getAllRestaurant() {
        return restaurantService.getAllRestaurants();
    }
}