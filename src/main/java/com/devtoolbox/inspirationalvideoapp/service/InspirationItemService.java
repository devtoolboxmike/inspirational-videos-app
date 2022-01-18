package com.devtoolbox.inspirationalvideoapp.service;

import com.devtoolbox.inspirationalvideoapp.model.InspirationItem;

import java.util.List;

public interface InspirationItemService {

    void voteOnItem(String hashKey, boolean upvote);
    void save(InspirationItem inspirationItem);
    InspirationItem get(String id);
    List<InspirationItem> getItems(String type, int limit);

}
