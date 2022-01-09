package com.devtoolbox.inspirationalvideoapp.service;

import com.devtoolbox.inspirationalvideoapp.model.Video;

import java.util.List;

public interface VideoService {

    void voteOnVideo(String hashKey, boolean upvote);
    void saveVideo(Video video);
    Video getVideo(String id);
    List<Video> getVideos(String optionalSearchCriteria);

}
