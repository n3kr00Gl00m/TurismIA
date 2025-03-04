package com.turismea.service;

import com.turismea.exception.NotTheOwnerOfRouteEception;
import com.turismea.exception.RouteNotFoundException;
import com.turismea.exception.UserNotFoundException;
import com.turismea.model.Route;
import com.turismea.model.Tourist;
import com.turismea.repository.RouteRepository;
import com.turismea.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final TouristRepository touristRepository;


    public RouteService(RouteRepository routeRepository, TouristRepository touristRepository) {
        this.routeRepository = routeRepository;
        this.touristRepository = touristRepository;
    }

    public RouteRepository getRouteRepository() {
        return routeRepository;
    }

    public List<Route> saveRoute(Long touristId, Route route) { //Save both types of routes
        Tourist tourist = touristRepository.findById(touristId)
                .orElseThrow(() -> new UserNotFoundException(touristId));

        route.setOwner(tourist);
        tourist.getSavedRoutes().add(route);

        routeRepository.save(route);
        touristRepository.save(tourist);

        return routeRepository.getRouteByOwner(tourist);
    }

    public Route editRoute(Long originalRouteId, Route newRoute, Long touristId) {
        // Find the existing route by ID
        Route OGRoute = routeRepository.findById(originalRouteId)
                .orElseThrow(() -> new RouteNotFoundException(originalRouteId));

        // Check if the touristId is the owner of the route
        if (!OGRoute.getOwner().getId().equals(touristId)) {
            throw new NotTheOwnerOfRouteEception();
        }


        // Update route properties
        OGRoute.setName(newRoute.getName());
        OGRoute.setCity(newRoute.getCity());
        OGRoute.setOwner(newRoute.getOwner());
        OGRoute.setRate(newRoute.getRate());
        OGRoute.setLocations(newRoute.getLocations());
        OGRoute.setDescription(newRoute.getDescription());

        // Save the updated route
        return routeRepository.save(OGRoute);
    }

    public List<Route> getSavedRoutes(Long touristId) {
        Tourist tourist = touristRepository.findById(touristId)
                .orElseThrow(() -> new UserNotFoundException(touristId));
        return routeRepository.getRouteByOwner(tourist);
    }

    public List<Route> getRoutesByCity(String city) {
        return routeRepository.getRoutesByCity(city);
    }

    public List<Route> getRoutesByOwner(Long ownerId) {
        return routeRepository.findRouteByOwner_Id(ownerId);
    }

    public void deleteRoute(Long routeId) {
        routeRepository.deleteById(routeId);
    }

    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException(routeId));
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }


}
