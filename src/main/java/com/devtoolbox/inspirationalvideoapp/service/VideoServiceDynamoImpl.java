package com.devtoolbox.inspirationalvideoapp.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.devtoolbox.inspirationalvideoapp.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceDynamoImpl implements VideoService {

    @Value("${dynamodb.table.name}")
    private String dynamodbTableName;

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDB dynamoDB;
    private Table table;

    @Autowired
    public VideoServiceDynamoImpl(DynamoDBMapper dynamoDBMapper, DynamoDB dynamoDB) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDB = dynamoDB;
    }

    @PostConstruct
    public void init() {
       this.table = dynamoDB.getTable(dynamodbTableName);
    }

    @Override
    public void voteOnVideo(String hashKey, boolean upvote) {

        int vote = upvote ? 1 : -1;

        Map<String,String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#v", "voteCount");

        Map<String,Object> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":val", vote);

        UpdateItemOutcome outcome = table.updateItem(
                "id", hashKey,
                "set #v = #v + :val",
                expressionAttributeNames,
                expressionAttributeValues);

        //todo check if it actually worked? Or it would throw an exception if it didn't work right?
    }

    @Override
    public void saveVideo(Video video) {
        dynamoDBMapper.save(video);
    }

    @Override
    public Video getVideo(String id) {
        Video video = Video.builder().id(id).build();
        return dynamoDBMapper.load(video);
    }

    @Override
    public List<Video> getVideos(String optionalSearchCriteria) {
        return null;
    }
}
