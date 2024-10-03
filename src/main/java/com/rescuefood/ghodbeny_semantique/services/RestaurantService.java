package com.rescuefood.ghodbeny_semantique.services;

import com.rescuefood.ghodbeny_semantique.model.Restaurant;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {
    private static final String SPARQL_ENDPOINT = "http://localhost:3030/dataSet/sparql";

    // Method to retrieve restaurants located in Tunis
    public List<Restaurant> getRestaurantsInTunis() {
        List<Restaurant> restaurants = new ArrayList<>();


        String sparqlQuery = "PREFIX ex: <http://example.com/restaurant#> " +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "SELECT ?restaurant ?name ?email ?speciality ?location WHERE { " +
                "?restaurant rdf:type ex:Restaurant . " +
                "?restaurant ex:hasName ?name . " +
                "OPTIONAL { ?restaurant ex:hasEmail ?email . } " +
                "OPTIONAL { ?restaurant ex:hasSpeciality ?speciality . } " +
                "OPTIONAL { ?restaurant ex:hasLocation ?location . } " +
                "FILTER (regex(?location, \"Tunis\", \"i\")) " +
                "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, sparqlQuery)) {
            ResultSet results = qexec.execSelect();

            // Loop through results and build Restaurant objects
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Restaurant restaurant = new Restaurant();

                // Handle restaurant ID
                if (soln.contains("restaurant")) {
                    restaurant.setId(soln.getResource("restaurant").getURI());
                } else {
                    restaurant.setId("Unknown ID");
                }

                // Handle name field
                if (soln.contains("name")) {
                    restaurant.setName(soln.getLiteral("name").getString());
                } else {
                    restaurant.setName("Unknown Name");
                }

                // Handle email field
                if (soln.contains("email")) {
                    restaurant.setEmail(soln.getLiteral("email").getString());
                } else {
                    restaurant.setEmail(null); // Or set default if needed
                }

                // Handle speciality field
                if (soln.contains("speciality")) {
                    restaurant.setSpeciality(soln.getLiteral("speciality").getString());
                } else {
                    restaurant.setSpeciality(null); // Or set default if needed
                }

                // Handle location field
                if (soln.contains("location")) {
                    restaurant.setLocation(soln.getLiteral("location").getString());
                } else {
                    restaurant.setLocation(null); // Or set default if needed
                }

                // Add restaurant to the list
                restaurants.add(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        // Requête SPARQL pour récupérer toutes les informations des restaurants
        String sparqlQuery = "PREFIX ex: <http://example.com/restaurant#> " +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "SELECT ?restaurant ?name ?email ?speciality ?location WHERE { " +
                "?restaurant rdf:type ex:Restaurant . " +
                "?restaurant ex:hasName ?name . " +
                "OPTIONAL { ?restaurant ex:hasEmail ?email . } " +
                "OPTIONAL { ?restaurant ex:hasSpeciality ?speciality . } " +
                "OPTIONAL { ?restaurant ex:hasLocation ?location . } " +
                "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, sparqlQuery)) {
            ResultSet results = qexec.execSelect();

            // Parcourir les résultats et créer des objets Restaurant
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Restaurant restaurant = new Restaurant();

                // Gérer l'ID du restaurant
                if (soln.contains("restaurant")) {
                    restaurant.setId(soln.getResource("restaurant").getURI());
                } else {
                    restaurant.setId("Unknown ID");
                }

                // Gérer le champ name
                if (soln.contains("name")) {
                    restaurant.setName(soln.getLiteral("name").getString());
                } else {
                    restaurant.setName("Unknown Name");
                }

                // Gérer le champ email
                if (soln.contains("email")) {
                    restaurant.setEmail(soln.getLiteral("email").getString());
                } else {
                    restaurant.setEmail(null); // ou une valeur par défaut
                }

                // Gérer le champ speciality
                if (soln.contains("speciality")) {
                    restaurant.setSpeciality(soln.getLiteral("speciality").getString());
                } else {
                    restaurant.setSpeciality(null); // ou une valeur par défaut
                }

                // Gérer le champ location
                if (soln.contains("location")) {
                    restaurant.setLocation(soln.getLiteral("location").getString());
                } else {
                    restaurant.setLocation(null); // ou une valeur par défaut
                }

                // Ajouter le restaurant à la liste
                restaurants.add(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurants;
    }

}
