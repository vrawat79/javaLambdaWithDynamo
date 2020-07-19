package com.example;

import java.time.Instant;
import java.util.UUID;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


@DynamoDbBean
public class ItemCRUD {

	// Create a DynamoDbClient object
	static Region region = Region.US_EAST_2;
	static DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

	// Create a DynamoDbEnhancedClient and use the DynamoDbClient object
	static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();

	static DynamoDbTable<ItemCRUD> customerTable = enhancedClient.table("ItemCRUD",
			TableSchema.fromBean(ItemCRUD.class));

	public static String handleRequest(Object args) {
		System.out.println("--- handleRequest() Initiated !! ----");

		ItemCRUD customer1 = new ItemCRUD();
		customer1.setId(UUID.randomUUID().toString());
		customer1.setName("Vikram Rawat");
		customer1.setRegistrationDate(Instant.now());

		customerTable.putItem(customer1);

		Key key = Key.builder().partitionValue("1").build();

		ItemCRUD customer2 = customerTable.getItem(key);
		
		System.out.println("-- customer2: " + customer2.toString());

		return customer2.toString();

	}

	public ItemCRUD() {

	}

	String id;
	String name;
	Instant registrationDate;

	@DynamoDbPartitionKey
	public String getId() {
		return id;
	};

	public void setId(String id) {
		this.id = id;
	};

	public String getName() {
		return name;
	};

	public void setName(String name) {
		this.name = name;
	};

	public Instant getRegistrationDate() {
		return registrationDate;
	};

	public void setRegistrationDate(Instant registrationDate) {
		this.registrationDate = registrationDate;
	};
	
	public String toString() {
		return id + ", " + registrationDate;
		
	}

}
