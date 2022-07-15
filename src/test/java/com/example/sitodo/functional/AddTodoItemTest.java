package com.example.sitodo.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, SeleniumJupiter.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("User Story 1: Add Todo Item")
@Tag("e2e")
class AddTodoItemTest extends BaseFunctionalTest {

    @Test
    @DisplayName("A user can create a single todo item")
    void addTodoItem_single() {
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        // Create a new item
        postNewTodoItem("Buy milk");

        // See the list for the newly inserted item
        checkItemsInList(List.of("Buy milk"));

        // The page can be accessed at the new, unique URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);
    }

    @Test
    @DisplayName("A user can create multiple todo items")
    void addTodoItem_multiple() {
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        // Create a new item
        postNewTodoItem("Buy milk");

        // See the list for the newly inserted item
        checkItemsInList(List.of("Buy milk"));

        // The page can be accessed at the new, unique URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.matches(".+/list/\\d+$"), "The URL was: " + currentUrl);

        // Create another item
        postNewTodoItem("Cut grass");

        // See the list again to see the new items
        checkItemsInList(List.of("Buy milk", "Cut grass"));

        // The URL is still the same from previous items
        assertEquals(currentUrl, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("A user can create two todo lists consecutively")
    void addTodoItem_twoUsers() {
        // First list
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        postNewTodoItem("Buy milk");
        checkItemsInList(List.of("Buy milk"));

        String firstUrl = driver.getCurrentUrl();
        assertTrue(firstUrl.matches(".+/list/\\d+$"), "The URL was: " + firstUrl);

        // Second list
        driver.get(createBaseUrl("localhost", serverPort));
        checkOverallPageLayout();

        postNewTodoItem("Buy coffee");
        checkItemsInList(List.of("Buy coffee"));

        String secondUrl = driver.getCurrentUrl();
        assertTrue(secondUrl.matches(".+/list/\\d+$"), "The URL was: " + secondUrl);

        // Ensure first and second list have different URL
        assertNotEquals(firstUrl, secondUrl, "Both lists must not have the same URL");
    }

    private void checkOverallPageLayout() {
        WebElement heading = driver.findElement(By.tagName("caption"));
        WebElement inputField = driver.findElement(By.tagName("input"));
        String headingText = heading.getText();
        String placeholderText = inputField.getAttribute("placeholder");

        assertEquals("Your Todo List", heading.getText(), "The heading title was: " + headingText);
        assertEquals("Enter an item", inputField.getAttribute("placeholder"), "The placeholder text was: " + placeholderText);
    }
}
