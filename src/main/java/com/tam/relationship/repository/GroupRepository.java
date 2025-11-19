package com.tam.relationship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tam.relationship.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

    @Query("SELECT g FROM Group g WHERE g.isActive = true")
    List<Group> findAllActive();

    @Query("SELECT g FROM Group g WHERE g.isActive = true")
    Page<Group> findAllActive(Pageable pageable);

    @Query("SELECT g FROM Group g WHERE g.id = ?1 AND g.isActive = true")
    Optional<Group> findByIdAndActive(String id);

    @Query("SELECT g FROM Group g WHERE g.name LIKE %?1% AND g.isActive = true")
    Page<Group> findByNameContainingAndActive(String name, Pageable pageable);
}
