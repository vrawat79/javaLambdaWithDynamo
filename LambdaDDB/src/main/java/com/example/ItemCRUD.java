package com.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class ItemCRUD {

	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
	static DynamoDB dynamoDB = new DynamoDB(client);

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

		Table table = dynamoDB.getTable(tableName);
		try {

			Item item = new Item().withPrimaryKey("productId", productId).withString("Title", "Book 120 Title")
					.withString("ISBN", "120-1111111111")
					.withStringSet("Authors", new HashSet<String>(Arrays.asList("Author12", "Author22")))
					.withNumber("Price", 20).withString("Dimensions", "8.5x11.0x.75").withNumber("PageCount", 500)
					.withBoolean("InPublication", false).withString("ProductCategory", "Book");
			table.putItem(item);

			System.out.println("---- Product Added ----");

			productId = UUID.randomUUID().toString();

			item = new Item().withPrimaryKey("productId", productId).withString("Title", "Book 121 Title")
					.withString("ISBN", "121-1111111111")
					.withStringSet("Authors", new HashSet<String>(Arrays.asList("Author21", "Author 22")))
					.withNumber("Price", 20).withString("Dimensions", "8.5x11.0x.75").withNumber("PageCount", 500)
					.withBoolean("InPublication", true).withString("ProductCategory", "Book");
			table.putItem(item);

			System.out.println("---- Product Added ----");

		} catch (Exception e) {
			System.err.println("Create items failed.");
			System.err.println(e.getMessage());

		}
	}

	private static String retrieveItem() {
		Table table = dynamoDB.getTable(tableName);

		try {

			Item item = table.getItem("productId", productId, "productId, ISBN, Title, Authors", null);

			System.out.println("Printing item after retrieving it....");
			System.out.println(item.toJSONPretty());

			return item.toJSONPretty();

		} catch (Exception e) {
			System.err.println("GetItem failed.");
			System.err.println(e.getMessage());
		}
		return "-- Error Occurred --";
	}

}
