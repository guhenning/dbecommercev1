package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.exception.BrandNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.ItemNotFoundException;
import com.gustavohenning.dbecommercev1.repository.BrandRepository;
import com.gustavohenning.dbecommercev1.repository.CategoryRepository;
import com.gustavohenning.dbecommercev1.repository.ItemRepository;
import com.gustavohenning.dbecommercev1.service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }
    @Transactional
    public Item addItem(Item item, List<Long> categoryIds, Long brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException(brandId));
        item.setBrand(brand);

        Item addedItem = itemRepository.save(item);

        List<Category> categories = new ArrayList<>(categoryRepository.findAllById(categoryIds));

        addedItem.setCategories(categories);

        return itemRepository.save(addedItem);
    }


    public List<Item> getItems() {
        return new ArrayList<>(itemRepository.findAll());
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElseThrow( () -> new ItemNotFoundException(id));
    }

    public List<Item> getItemsByCategoryIds(List<Long> categoryIds) {
        return itemRepository.findByCategoriesIdIn(categoryIds);
    }
    public List<Item> getItemsByBrandId(Long brandId) {
        return itemRepository.findByBrandId(brandId);
    }

    public List<Item> findByNameContainingIgnoreCaseOrShortDescriptionContainingIgnoreCaseOrLongDescriptionContainingIgnoreCase(String nameKeyword, String shortDescKeyword, String longDescKeyword) {
        return itemRepository.findByNameContainingIgnoreCaseOrShortDescriptionContainingIgnoreCaseOrLongDescriptionContainingIgnoreCase(nameKeyword, shortDescKeyword, longDescKeyword);
    }





    public Item deleteItem(Long id) {
        Item item = getItem(id);
        itemRepository.delete(item);
        return item;
    }

    @Transactional
    public Item editItem(Long id, Item item) {
        Item itemToEdit = getItem(id);



        if (item.getName() != null) {
            itemToEdit.setName(item.getName());
        }
        if (item.getShortDescription() != null) {
            itemToEdit.setShortDescription(item.getShortDescription());
        }
        if (item.getLongDescription() != null) {
            itemToEdit.setLongDescription(item.getLongDescription());
        }
        if (item.getImageUrl() != null) {
            itemToEdit.setImageUrl(item.getImageUrl());
        }
        if (item.getStockQuantity() > 0) {
            itemToEdit.setStockQuantity(item.getStockQuantity());
        }
        if (item.getSalePrice() > 0) {
            itemToEdit.setSalePrice(item.getSalePrice());
        }
        if (item.getCostPrice() > 0) {
            itemToEdit.setCostPrice(item.getCostPrice());
        }
        if (item.getDiscount() > 0.0) {
            itemToEdit.setDiscount(item.getDiscount());
        }

        if (item.getBrand() != null){
            itemToEdit.setBrand(item.getBrand());
        }

        if (item.getCategories() != null){
            itemToEdit.setCategories(item.getCategories());
        }

        return itemToEdit;
    }
}