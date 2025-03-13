//package com.example.graphql.config;
//
//import com.azure.cosmos.CosmosClientBuilder;
//import com.azure.identity.DefaultAzureCredential;
//import com.azure.identity.DefaultAzureCredentialBuilder;
//import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
//import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCosmosRepositories
//public class CosmosConfiguration extends AbstractCosmosConfiguration {
//
//    @Bean
//    public CosmosClientBuilder getCosmosClientBuilder() {
//        DefaultAzureCredential credential = new DefaultAzureCredentialBuilder()
//                .build();
//
//        return new CosmosClientBuilder()
//                .endpoint("https://target-db.documents.azure.com:443/;AccountKey=EVRnPBmqcVNlMYqwbQwlvoSUvsRyRv318fq1vveNkY2MKsK8TNF8Zjva5WSbuuyQahfezt5WZxhXACDbVpbPNw==;")
//                .credential(credential);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return "";
//    }
//}
////public class CosmosConfiguration extends AbstractCosmosConfiguration {
////
////    private static final Logger LOGGER = LoggerFactory.getLogger(CosmosConfiguration.class);
////
////    @Value("${azure.cosmos.uri}")
////    private String uri;
////
////    @Value("${azure.cosmos.key}")
////    private String key;
////
////    @Value("${azure.cosmos.secondaryKey}")
////    private String secondaryKey;
////
////    @Value("${azure.cosmos.database}")
////    private String dbName;
////
////    @Override
////    protected String getDatabaseName() {
////        return "target-db";
////    }
////}
