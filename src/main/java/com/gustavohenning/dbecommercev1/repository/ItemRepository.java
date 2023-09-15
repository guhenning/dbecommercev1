package com.gustavohenning.dbecommercev1.repository;

import com.gustavohenning.dbecommercev1.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoriesIdIn(List<Long> categoryIds);

    List<Item> findByBrandId(Long brandId);

    List<Item> findByNameContainingIgnoreCase(String name);

    List<Item> findByNameContainingIgnoreCaseOrShortDescriptionContainingIgnoreCaseOrLongDescriptionContainingIgnoreCase(String nameKeyword, String shortDescKeyword, String longDescKeyword);
}
