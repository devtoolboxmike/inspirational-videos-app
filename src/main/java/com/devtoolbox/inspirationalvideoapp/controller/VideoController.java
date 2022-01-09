package com.devtoolbox.inspirationalvideoapp.controller;

import com.devtoolbox.inspirationalvideoapp.model.Video;
import com.devtoolbox.inspirationalvideoapp.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
public class VideoController {

    private VideoService videoService;

    @Autowired
    public VideoController (VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/video")
    public Video newEmployee(@RequestBody Video video) {
        video.setId(UUID.randomUUID().toString());
        video.setVoteCount(0L);
        video.setCreatedAt(new Date());

        videoService.saveVideo(video);
        return video;
    }

    @GetMapping("/video/{id}")
    public Video getOneVideo(@PathVariable String id) {
        return videoService.getVideo(id);
    }


}
