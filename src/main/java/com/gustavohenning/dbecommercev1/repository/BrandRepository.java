package com.gustavohenning.dbecommercev1.repository;

import com.gustavohenning.dbecommercev1.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Iterable<Brand> findByNameContainingIgnoreCase(@Param("name") String name);
}

