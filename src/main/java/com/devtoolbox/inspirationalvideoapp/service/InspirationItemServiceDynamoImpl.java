package com.devtoolbox.inspirationalvideoapp.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.devtoolbox.inspirationalvideoapp.model.InspirationItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InspirationItemServiceDynamoImpl implements InspirationItemService {

    @Value("${dynamodb.table.name}")
    private String dynamodbTableName;

    private DynamoDBMapper dynamoDBMapper;
    private DynamoDB dynamoDB;
    private Table table;

    @Autowired
    public InspirationItemServiceDynamoImpl(DynamoDBMapper dynamoDBMapper, DynamoDB dynamoDB) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDB = dynamoDB;
    }

    @PostConstruct
    public void init() {
       this.table = dynamoDB.getTable(dynamodbTableName);
    }

    @Override
    public void voteOnItem(String hashKey, boolean upvote) {

        int vote = upvote ? 1 : -1;

        Map<String,String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#v", "voteCount");

        Map<String,Object> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":val", vote);

        table.updateItem(
                "id", hashKey,
                "set #v = #v + :val",
                expressionAttributeNames,
                expressionAttributeValues);
    }

    @Override
    public void save(InspirationItem inspirationItem) {
        dynamoDBMapper.save(inspirationItem);
    }

    @Override
    public InspirationItem get(String id) {

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<InspirationItem> queryExpression = new DynamoDBQueryExpression<InspirationItem>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(eav);

        List<InspirationItem> inspirationItems = dynamoDBMapper.query(InspirationItem.class, queryExpression);

        if (inspirationItems.isEmpty()) {
            throw new IllegalArgumentException("video cannot be found, wrong id passed");
        }

        return inspirationItems.get(0);
    }

    @Override
    public List<InspirationItem> getItems(String type, int limit) {
        final DynamoDBQueryExpression<InspirationItem> queryExpression =
                new DynamoDBQueryExpression<>();
        queryExpression.setLimit(limit);
        queryExpression.setScanIndexForward(false); //provides sort order in descending order
        InspirationItem inspirationItem = new InspirationItem();
        inspirationItem.setType(type);
        queryExpression.setHashKeyValues(inspirationItem);
        queryExpression.setIndexName("type-voteCount-index");
        queryExpression.setConsistentRead(false); //gsi's don't support consistent reads

        PaginatedQueryList<InspirationItem> results =
                dynamoDBMapper.query(InspirationItem.class, queryExpression);

        return results;
    }
}
