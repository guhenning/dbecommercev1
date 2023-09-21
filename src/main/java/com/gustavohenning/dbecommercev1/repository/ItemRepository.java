package com.gustavohenning.dbecommercev1.repository;

import com.gustavohenning.dbecommercev1.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    List<Item> findByKeywordIgnoreCase(String keyword);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    Page<Item> findByKeywordIgnoreCaseWithPagination(String keyword, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    Page<Item> findByOrderBySalePriceAsc(String keyword, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    Page<Item> findByOrderBySalePriceDesc(String keyword, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    Page<Item> findByOrderByUpdatedDateDesc(String keyword, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:keyword% OR LOWER(i.shortDescription) LIKE %:keyword% OR LOWER(i.longDescription) LIKE %:keyword%")
    Page<Item> findByOrderByDiscountDesc(String keyword, Pageable pageable);

    List<Item> findByCategoriesIdIn(List<Long> categoryIds);

    List<Item> findByBrandId(Long brandId);
}
