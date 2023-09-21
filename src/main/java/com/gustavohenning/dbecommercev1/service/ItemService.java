package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    Item addItem(Item item, List<Long> categoryIds, Long brandId);

    List<Item> getItems();

    Item getItem(Long id);

    List<Item> findByKeywordIgnoreCase(String keyword);

    Page<Item> findByKeywordIgnoreCaseWithPagination(String keyword, Pageable pageable);

    Page<Item> getItemsOrderedBySalesPrice(boolean ascending, String keyword, Pageable pageable);

    Page<Item> getItemsOrderedByRecentUpdatedDate(String keyword, Pageable pageable);

    Page<Item> getItemsOrderedByBiggerDiscount(String keyword, Pageable pageable);

    List<Item> getItemsByCategoryIds(List<Long> categoryIds);

    List<Item> getItemsByBrandId(Long brandId);

    Item deleteItem(Long id);

    Item editItem(Long id, Item item);


}
