package com.tam.relationship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tam.relationship.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.isActive = true")
    List<Location> findAllActive();

    @Query("SELECT l FROM Location l WHERE l.isActive = true")
    Page<Location> findAllActive(Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.id = ?1 AND l.isActive = true")
    Optional<Location> findByIdAndActive(String id);

    @Query(
            "SELECT l FROM Location l WHERE (l.name LIKE %?1% OR l.city LIKE %?1% OR l.country LIKE %?1%) AND l.isActive = true")
    Page<Location> searchByKeywordAndActive(String keyword, Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.city = ?1 AND l.isActive = true")
    Page<Location> findByCityAndActive(String city, Pageable pageable);
}
