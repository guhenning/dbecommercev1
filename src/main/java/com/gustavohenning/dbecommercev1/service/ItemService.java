package com.gustavohenning.dbecommercev1.service;

import com.gustavohenning.dbecommercev1.entity.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Item item, List<Long> categoryIds, Long brandId);

    List<Item> getItems();

    Item getItem(Long id);
    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findByKeywordIgnoreCase(String keyword);

    List<Item> getItemsByCategoryIds(List<Long> categoryIds);

    List<Item> getItemsByBrandId(Long brandId);

    Item deleteItem(Long id);

    Item editItem(Long id, Item item);


}
