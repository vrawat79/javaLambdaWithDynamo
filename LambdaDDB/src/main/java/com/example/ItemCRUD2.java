package com.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
//
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
////import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
//import com.amazonaws.services.dynamodbv2.document.Item;
//import com.amazonaws.services.dynamodbv2.document.Table;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

public class ItemCRUD2 {

	// Create the DynamoDbClient object
	static Region region = Region.US_EAST_2;
	static DynamoDbClient ddb = DynamoDbClient.builder().region(region).build();

//	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
//	static DynamoDB dynamoDB = new DynamoDB(client);

	static String tableName = "products";
	static String productId = UUID.randomUUID().toString();

	public static void main(String[] args) throws IOException {

		System.out.println("--- Program Initiated !! ----");

		createItems();

		retrieveItem();

//        // Perform various updates.
//        updateMultipleAttributes();
//        updateAddNewAttribute();
//        updateExistingAttributeConditionally();
//
//        // Delete the item.
//        deleteItem();

	}

	public static String handleRequest(Object args) {
		System.out.println("--- handleRequest() Initiated !! ----");

		createItems();

		return retrieveItem();

//        return "-- Success !! --";

	}

	private static void createItems() {
//		 putItemInTable(ddb, tableName, key, keyVal, albumTitle, albumTitleValue, awards, awardVal, songTitle, songTitleVal);		

		String key = "productId";
		String keyVal = productId;
		String bookTitle = "Title";
		String bookTitleValue = "Book 120 Title";
		String author = "Author";
		String authorValue = "John Hopkins";

		HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

		// Add all content to the table
		itemValues.put(key, AttributeValue.builder().s(keyVal).build());
		itemValues.put(bookTitle, AttributeValue.builder().s(bookTitleValue).build());
		itemValues.put(author, AttributeValue.builder().s(authorValue).build());

		// Create a PutItemRequest object
		PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(itemValues).build();

		try {
			ddb.putItem(request);
			System.out.println(tableName + " was successfully updated");

		} catch (ResourceNotFoundException e) {
			System.err.format("Error: The table \"%s\" can't be found.\n", tableName);
			System.err.println("Be sure that it exists and that you've typed its name correctly!");
			System.exit(1);
		} catch (DynamoDbException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		// snippet-end:[dynamodb.java2.put_item.main]

	}

	private static String retrieveItem() {

		String key = "productId";
		String keyVal = productId;

		HashMap<String, AttributeValue> keyToGet = new HashMap<String, AttributeValue>();

		keyToGet.put(key, AttributeValue.builder().s(keyVal).build());

		// Create a GetItemRequest object
		GetItemRequest request = GetItemRequest.builder().key(keyToGet).tableName(tableName).build();

		try {
			String item = "";
			Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();

			if (returnedItem != null) {
				Set<String> keys = returnedItem.keySet();
				System.out.println("Table Attributes: \n");

				for (String key1 : keys) {
					System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
					item = returnedItem.get(key1).toString();
				}

				return item;
			} else {
				System.out.format("No item found with the key %s!\n", key);
			}
		} catch (DynamoDbException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return "--- Error ---";
	}
}
