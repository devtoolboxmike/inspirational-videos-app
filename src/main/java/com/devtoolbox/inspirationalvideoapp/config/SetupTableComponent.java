package com.devtoolbox.inspirationalvideoapp.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SetupTableComponent {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Value("${dynamodb.table.name}")
    private String dynamodbTableName;

    @PostConstruct
    public void createTableIfDoesntExist() throws InterruptedException {
        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

        //check if table exists: ref: https://muthupandian.medium.com/check-table-exist-in-dynamodb-from-aws-lambda-java-af4761fdd3fb
        try {
            TableDescription tableDescription = dynamoDB.getTable(dynamodbTableName).describe();
            log.info("Table already exists.");
            return;
        } catch (com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException rnfe) {
            //continue with creation..
            log.info("Table did not exists so system is creating it now.");
        }

        List<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("type").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("voteCount").withAttributeType("N"));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH));

        ProvisionedThroughput ptIndex = new ProvisionedThroughput().withReadCapacityUnits(5L)
                .withWriteCapacityUnits(5L);

        GlobalSecondaryIndex createTypeVoteCountIndex = new GlobalSecondaryIndex().withIndexName("type-voteCount-index")
                .withProvisionedThroughput(ptIndex)
                .withKeySchema(new KeySchemaElement().withAttributeName("type").withKeyType(KeyType.HASH),
                        new KeySchemaElement().withAttributeName("voteCount").withKeyType(KeyType.RANGE))
                .withProjection(
                        new Projection().withProjectionType("ALL"));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(dynamodbTableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withGlobalSecondaryIndexes(createTypeVoteCountIndex)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(5L));

        Table table = dynamoDB.createTable(request);

        table.waitForActive();
    }
}
