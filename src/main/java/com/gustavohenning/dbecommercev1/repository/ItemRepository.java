package com.gustavohenning.dbecommercev1.repository;

import com.gustavohenning.dbecommercev1.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCase(String name);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    List<Item> findByKeywordIgnoreCase(String keyword);

    List<Item> findByCategoriesIdIn(List<Long> categoryIds);

    List<Item> findByBrandId(Long brandId);
}
