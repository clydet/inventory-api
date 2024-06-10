package com.zaga.inventory;

import com.zaga.inventory.model.Item;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class InventoryResourceTest {

    @Test
    @Order(1)
    public void testAddItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setVendorId(1L);

        Response response = given()
            .contentType(ContentType.JSON)
            .body(item)
            .when()
            .post("/items");
        String blah = response.body().prettyPrint();

        response.then()
            .statusCode(201)
            .body("id", notNullValue());
    }

    @Test
    @Order(2)
    public void testGetItems() {
        Response response = given()
            .when().get("/items");
        String blah = response.body().prettyPrint();
        response.then()
            .statusCode(200)
            .body("$.size()", is(1));
    }

    @Test
    @Order(3)
    public void testUpdateItem() {
        Item updatedItem = new Item();
        updatedItem.setName("Updated Item");
        updatedItem.setDescription("Updated Description");
        updatedItem.setVendorId(1L);

        given()
            .contentType(ContentType.JSON)
            .body(updatedItem)
            .when()
            .put("/items/1")
            .then()
            .statusCode(200);
        
        given()
            .when().get("/items/1")
            .then()
            .statusCode(200)
            .body("name", is("Updated Item"));
    }

    @Test
    @Order(4)
    public void testDeleteItem() {
        given()
            .when()
            .delete("/items/1")
            .then()
            .statusCode(204);

        given()
            .when().get("/items")
            .then()
            .statusCode(200)
            .body("$.size()", is(0));
    }
}
