package com.devtoolbox.inspirationalvideoapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;

@Configuration
public class SpringConfig {

    @Value("${amazon.dynamodb.endpoint:http://localhost:8000}")
    private String amazonDynamoDBCustomEndpoint;

    @Value("${amazon.aws.accesskey:dummyAccessKey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey:dummySecretKey}")
    private String amazonAWSSecretKey;

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB){
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
        return mapper;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials) {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBCustomEndpoint, "us-west-2"))
                .withCredentials(credentialsProvider)
                .build();

        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
                amazonAWSAccessKey, amazonAWSSecretKey);
    }

}
