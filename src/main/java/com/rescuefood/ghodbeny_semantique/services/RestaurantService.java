package com.rescuefood.ghodbeny_semantique.services;

import com.rescuefood.ghodbeny_semantique.model.Restaurant;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;
import org.apache.jena.rdf.model.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.OutputStream;
@Service
public class RestaurantService {
    private static final String SPARQL_ENDPOINT = "http://localhost:3030/dataSet/sparql";
    private static final String UPDATE_ENDPOINT = "http://localhost:3030/dataSet/update"; // Pour les requêtes d'insertion
    private static final String OWL_FILE_PATH = "C:/Users/MSI/Downloads/project (1).owl";
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


    public boolean addRestaurant(Restaurant restaurant) {
        String sparqlInsertQuery = "PREFIX ex: <http://example.com/restaurant#> " +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "INSERT DATA { " +
                "<http://example.com/restaurant#" + restaurant.getId() + "> rdf:type ex:Restaurant ; " +
                "ex:hasName \"" + restaurant.getName() + "\"^^<http://www.w3.org/2001/XMLSchema#string> ; " +
                (restaurant.getEmail() != null ? "ex:hasEmail \"" + restaurant.getEmail() + "\"^^<http://www.w3.org/2001/XMLSchema#string> ; " : "") +
                (restaurant.getSpeciality() != null ? "ex:hasSpeciality \"" + restaurant.getSpeciality() + "\"^^<http://www.w3.org/2001/XMLSchema#string> ; " : "") +
                (restaurant.getLocation() != null ? "ex:hasLocation \"" + restaurant.getLocation() + "\"^^<http://www.w3.org/2001/XMLSchema#string> ; " : "") +
                "}";

        try {
            UpdateRequest update = UpdateFactory.create(sparqlInsertQuery);
            UpdateExecutionFactory.createRemote(update, UPDATE_ENDPOINT).execute();
            writeRestaurantToOWL(restaurant);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void writeRestaurantToOWL(Restaurant restaurant) {
        // Créer ou charger un modèle RDF existant
        Model model = ModelFactory.createDefaultModel();

        // Charger le contenu du fichier OWL existant
        try {
            model.read(OWL_FILE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ajouter les propriétés du restaurant
        Resource restaurantResource = model.createResource("http://example.com/restaurant#" + restaurant.getId());
        restaurantResource.addProperty(RDF.type, model.createResource("http://example.com/restaurant#Restaurant"));
        restaurantResource.addProperty(model.createProperty("http://example.com/restaurant#hasName"), restaurant.getName());
        if (restaurant.getEmail() != null) {
            restaurantResource.addProperty(model.createProperty("http://example.com/restaurant#hasEmail"), restaurant.getEmail());
        }
        if (restaurant.getSpeciality() != null) {
            restaurantResource.addProperty(model.createProperty("http://example.com/restaurant#hasSpeciality"), restaurant.getSpeciality());
        }
        if (restaurant.getLocation() != null) {
            restaurantResource.addProperty(model.createProperty("http://example.com/restaurant#hasLocation"), restaurant.getLocation());
        }

        // Écrire le modèle RDF modifié dans le fichier OWL
        try (OutputStream out = new FileOutputStream(OWL_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
