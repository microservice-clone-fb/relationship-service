package com.tam.relationship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tam.relationship.entity.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {

    @Query("SELECT p FROM Page p WHERE p.isActive = true")
    List<Page> findAllActive();

    @Query("SELECT p FROM Page p WHERE p.isActive = true")
    org.springframework.data.domain.Page<Page> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Page p WHERE p.id = ?1 AND p.isActive = true")
    Optional<Page> findByIdAndActive(String id);

    @Query(
            "SELECT p FROM Page p WHERE (p.name LIKE %?1% OR p.description LIKE %?1% OR p.category LIKE %?1%) AND p.isActive = true")
    org.springframework.data.domain.Page<Page> searchByKeywordAndActive(String keyword, Pageable pageable);

    @Query("SELECT p FROM Page p WHERE p.category = ?1 AND p.isActive = true")
    org.springframework.data.domain.Page<Page> findByCategoryAndActive(String category, Pageable pageable);
}
