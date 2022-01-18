package com.devtoolbox.inspirationalvideoapp.controller;

import com.devtoolbox.inspirationalvideoapp.model.InspirationItem;
import com.devtoolbox.inspirationalvideoapp.service.InspirationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class InspirationItemController {

    private InspirationItemService inspirationItemService;

    @Autowired
    public InspirationItemController(InspirationItemService inspirationItemService) {
        this.inspirationItemService = inspirationItemService;
    }

    @PostMapping("/inspiration-item")
    public InspirationItem newItem(@RequestBody InspirationItem inspirationItem) {
        inspirationItem.setId(UUID.randomUUID().toString());
        inspirationItem.setVoteCount(0L);
        inspirationItem.setCreatedAt(new Date());

        inspirationItemService.save(inspirationItem);
        return inspirationItem;
    }

    @GetMapping("/inspiration-item/{id}")
    public InspirationItem getOne(@PathVariable String id) {
        return inspirationItemService.get(id);
    }

    @PutMapping("/inspiration-item/upvote/{id}")
    public ResponseEntity<String> updateOne(@PathVariable String id) {
        inspirationItemService.voteOnItem(id, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/inspiration-item")
    public List<InspirationItem> getAll() {
        return inspirationItemService.getItems("video", 100);
    }
}
