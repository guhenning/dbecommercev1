package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.entity.Brand;
import com.gustavohenning.dbecommercev1.entity.Category;
import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.exception.BrandNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.CategoryNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.ItemNotFoundException;
import com.gustavohenning.dbecommercev1.entity.exception.ItemStockCannotBeNegative;
import com.gustavohenning.dbecommercev1.repository.BrandRepository;
import com.gustavohenning.dbecommercev1.repository.CategoryRepository;
import com.gustavohenning.dbecommercev1.repository.ItemRepository;
import com.gustavohenning.dbecommercev1.service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Item> getItems() {
        return new ArrayList<>(itemRepository.findAll());
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElseThrow( () -> new ItemNotFoundException(id));
    }

    public List<Item> findByKeywordIgnoreCase(String keyword) {
        return itemRepository.findByKeywordIgnoreCase(keyword);
    }

    public Page<Item> findByKeywordIgnoreCaseWithPagination(String keyword, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if(pageSize > 100) {
            pageSize = 100;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        return itemRepository.findByKeywordIgnoreCaseWithPagination(keyword, pageable);
    }

    public Page<Item> getItemsOrderedBySalesPrice(boolean ascending, String keyword, Pageable pageable) {
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        int pageSize = pageable.getPageSize();
        if(pageSize > 100) {
            pageSize = 100;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        return ascending
                ? itemRepository.findByKeywordOrderBySalePriceAsc(keyword, pageable)
                : itemRepository.findByKeywordOrderBySalePriceDesc(keyword, pageable);
    }

    public Page<Item> getItemsOrderedByRecentUpdatedDate(String keyword, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if(pageSize > 100) {
            pageSize = 100;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        return itemRepository.findByKeywordOrderByUpdatedDateDesc(keyword, pageable);
    }

    //TODO Controller Method
    public Page<Item> getItemsOrderedByBiggerDiscount(String keyword, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if(pageSize > 100) {
            pageSize = 100;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize);
        return itemRepository.findByKeywordOrderByDiscountDesc(keyword, pageable);
    }

    public List<Item> getItemsByCategoryIds(List<Long> categoryIds) {
        return itemRepository.findByCategoriesIdIn(categoryIds);
    }

    public List<Item> getItemsByBrandId(Long brandId) {
        return itemRepository.findByBrandId(brandId);
    }

    @Transactional
    public Item addItem(Item item, List<Long> categoryIds, Long brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException(brandId));
        item.setBrand(brand);

        Item addedItem = itemRepository.save(item);

        List<Category> categories = new ArrayList<>();

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryId));
            categories.add(category);
        }

        addedItem.setCategories(categories);

        return itemRepository.save(addedItem);
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
        //TODO fix editing categories
        if (item.getCategories() != null){
            itemToEdit.setCategories(item.getCategories());
        }
        return itemToEdit;
    }

    @Transactional
    public Item editItemStock(Long id, int updateStockQnt) {
        Item itemToEditStock = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        int previousStockQtn = itemToEditStock.getStockQuantity();
        int newStockQuantity = previousStockQtn + updateStockQnt;

        if (newStockQuantity < 0) {
            throw new ItemStockCannotBeNegative(id, previousStockQtn, newStockQuantity);
        }
        itemToEditStock.setStockQuantity(newStockQuantity);
        return itemToEditStock;
    }

    public Item deleteItem(Long id) {
        Item item = getItem(id);
        itemRepository.delete(item);
        return item;
    }
}