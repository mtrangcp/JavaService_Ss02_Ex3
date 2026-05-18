package com.btvn.btvnex3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {
    private final List<Item> inventory = new ArrayList<>();

    @JacksonXmlRootElement(localName = "item")
    static class Item {
        private String id;
        private String name;
        private int quantity;

        public Item() {}
        public Item(String id, String name, int quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public ItemController() {
        inventory.add(new Item("IT001", "Sữa tươi tiệt trùng 1L", 150));
        inventory.add(new Item("IT002", "Mì ăn liền Hảo Hảo", 500));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable String id) {
        Optional<Item> item = inventory.stream().filter(i -> i.getId().equals(id)).findFirst();

        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item newItem) {
        inventory.add(newItem);
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item updatedItem) {
        Optional<Item> existingItem = inventory.stream().filter(i -> i.getId().equals(id)).findFirst();

        if (existingItem.isPresent()) {
            Item item = existingItem.get();
            item.setName(updatedItem.getName());
            item.setQuantity(updatedItem.getQuantity());
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        Optional<Item> item = inventory.stream().filter(i -> i.getId().equals(id)).findFirst();

        if (item.isPresent()) {
            inventory.remove(item.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
