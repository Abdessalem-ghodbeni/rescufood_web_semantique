package com.rescuefood.ghodbeny_semantique.controller;

import com.rescuefood.ghodbeny_semantique.model.Restaurant;
import com.rescuefood.ghodbeny_semantique.services.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "add")
    public ResponseEntity<?> AjouterRestaurant(@RequestBody Restaurant restaurant){
        try {
            return new ResponseEntity<>(restaurantService.addRestaurant(restaurant),HttpStatus.CREATED);
        }catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}