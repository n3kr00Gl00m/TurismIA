package com.turismea.repository;

import com.turismea.model.entity.City;
import com.turismea.model.entity.Route;
import com.turismea.model.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findRouteByOwner_Id(Long ownerId);
    List<Route> getRoutesByCity(City city);
    @Query(value = "SELECT DISTINCT R FROM Route R WHERE R.owner = :owner")
    List<Route> getRouteByOwner(@Param("owner") Tourist owner);

    List<Route> getRoutesByOwner(Tourist tourist);
}
