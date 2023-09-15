package com.gustavohenning.dbecommercev1.repository;

import com.gustavohenning.dbecommercev1.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT b FROM Category b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Iterable<Category> findByNameContainingIgnoreCase(@Param("name") String name);
}
