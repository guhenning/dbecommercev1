package com.gustavohenning.dbecommercev1.controller;

import com.gustavohenning.dbecommercev1.entity.Item;
import com.gustavohenning.dbecommercev1.entity.dto.ItemDTO;
import com.gustavohenning.dbecommercev1.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@CrossOrigin("*")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Get All Items", description = "Get All Items in DB")
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<ItemDTO>> getItems() {
        List<Item> items = itemService.getItems();
        List<ItemDTO> itemsDto = items.stream().map(ItemDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(itemsDto, HttpStatus.OK);
    }

    @Operation(summary = "Get Item By ID", description = "Get Item from ID in DB")
    @GetMapping(value = "{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable final Long id) {
        Item item = itemService.getItem(id);
        return new ResponseEntity<>(ItemDTO.from(item), HttpStatus.OK);
    }

    @Operation(summary = "Get Items By Keyword", description = "Get Items By Keyword in name or shortDescription or longDescription")
    @GetMapping("/searchitems")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<ItemDTO>> searchItemsByKeyword(@RequestParam String keyword) {
        List<Item> items = (List<Item>) itemService.findByKeywordIgnoreCase(keyword);
        List<ItemDTO> itemDtos = items.stream().map(ItemDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get Items By Keyword with Pagination", description = "Get Items By Keyword in name or shortDescription or longDescription with Pagination")
    @GetMapping("/search/{keyword}/{page}/{pageSize}")
    public ResponseEntity<Page<ItemDTO>> searchItemsByKeyword(
            @PathVariable String keyword,
            @PathVariable int page,
            @PathVariable int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Item> itemsPage = itemService.findByKeywordIgnoreCaseWithPagination(keyword, pageable);

        Page<ItemDTO> itemDtos = itemsPage.map(ItemDTO::from);
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get Items By Keyword Ordered by Price with Pagination", description = "Get Items By Keyword Ordered by Price with Pagination true = ASC false = DESC")
    @GetMapping("/search/{keyword}/byprice/{ascending}/{page}/{pageSize}")
    public ResponseEntity<Page<ItemDTO>> searchItemsByKeywordOrderedByPrice(
            @PathVariable String keyword,
            @PathVariable boolean ascending,
            @PathVariable int page,
            @PathVariable int pageSize
    ) {
        Sort.Direction sortDirection = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortDirection, "salePrice"));
        Page<Item> itemsPage = itemService.getItemsOrderedBySalesPrice(ascending, keyword, pageable);

        Page<ItemDTO> itemDtos = itemsPage.map(ItemDTO::from);
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }
    @Operation(summary = "Get Items By Keyword Ordered by UpdateDate with Pagination", description = "Get Items By Keyword Ordered by UpdateDate with Pagination")
    @GetMapping("/search/{keyword}/new/{page}/{pageSize}")
    public ResponseEntity<Page<ItemDTO>> searchItemsByKeywordOrderedByPrice(
            @PathVariable String keyword,
            @PathVariable int page,
            @PathVariable int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "updatedDate"));
        Page<Item> itemsPage = itemService.getItemsOrderedByRecentUpdatedDate(keyword, pageable);

        Page<ItemDTO> itemDtos = itemsPage.map(ItemDTO::from);
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get Items By Category", description = "Get Items By Category")
    @GetMapping("/category")
    public ResponseEntity<List<ItemDTO>> getItemsByCategory(@RequestParam List<Long> categoryIds) {
        List<Item> items = itemService.getItemsByCategoryIds(categoryIds);
        List<ItemDTO> itemDtos = items.stream().map(ItemDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @Operation(summary = "Get Items By Brand", description = "Get Items By Brand")
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ItemDTO>> getItemsByBrand(@PathVariable Long brandId) {
        List<Item> items = itemService.getItemsByBrandId(brandId);
        List<ItemDTO> itemDtos = items.stream().map(ItemDTO::from).collect(Collectors.toList());
        return new ResponseEntity<>(itemDtos, HttpStatus.OK);
    }

    @Operation(summary = "Add New Item", description = "Add a new Item to DB")
    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ItemDTO> addItem(@RequestBody final ItemDTO itemDto) {

        Item item = Item.from(itemDto);
        Item addedItem = itemService.addItem(item, itemDto.getCategoryIds(), itemDto.getBrand().getId());

        return new ResponseEntity<>(ItemDTO.from(addedItem), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Item by ID", description = "Update the Item in DB")
    @PutMapping(value = "/update/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ItemDTO> editItem(@PathVariable final Long id, @RequestBody final ItemDTO itemDto) {
        Item editedItem = itemService.editItem(id, Item.from(itemDto));
        return new ResponseEntity<>(ItemDTO.from(editedItem), HttpStatus.OK);
    }

    @Operation(summary = "Update Item Stock by ID", description = "Update the Item Stock in DB")
    @PutMapping(value = "/updatestock/{id}/{updateStockQnt}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ItemDTO> editItemStock(@PathVariable final Long id, @PathVariable final int updateStockQnt) {
        Item stockEditedItem = itemService.editItemStock(id, updateStockQnt);
        return new ResponseEntity<>(ItemDTO.from(stockEditedItem), HttpStatus.OK);
    }

    @Operation(summary = "Delete Item by ID", description = "Delete the Item from ID in DB")
    @DeleteMapping(value = "/delete/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ItemDTO> deleteItem(@PathVariable final Long id) {
        Item item = itemService.deleteItem(id);
        return new ResponseEntity<>(ItemDTO.from(item), HttpStatus.OK);
    }
}
