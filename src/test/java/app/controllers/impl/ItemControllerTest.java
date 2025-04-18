package app.controllers.impl;

import app.config.HibernateConfig;
import app.daos.impl.ItemDAO;
import app.dtos.ErrorMessage;
import app.dtos.ItemDTO;
import app.dtos.StudentDTO;
import app.entities.Item;
import app.entities.Student;
import app.enums.ItemCategory;
import app.populators.ItemPopulator;
import app.rest.ApplicationConfig;
import app.rest.ItemRoutes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ItemControllerTest
{
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final ItemController itemController = new ItemController(emf);
    private static final ItemDAO itemDAO = ItemDAO.getInstance(emf);
    private static final String contentType = "application/json";

    @BeforeEach
    void setup()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Item").executeUpdate();
            em.createQuery("DELETE FROM Student").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE item_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE student_id_seq RESTART WITH 1").executeUpdate();

            List<Item> items = ItemPopulator.populate();

            // persist all students first
            items.stream()
                    .map(Item::getStudent)
                    .filter(student -> student != null)
                    .distinct()
                    .forEach(em::persist);

            // then persist items
            items.forEach(item ->
            {
                if (item.getStudent() != null)
                {
                    item.setStudent(em.find(Student.class, item.getStudent().getId()));
                }
                em.persist(item);
            });

            em.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ApplicationConfig.getInstance()
                .initiateServer()
                .setRoute(ItemRoutes.getRoutes(emf))
                .startServer(7777);

        RestAssured.baseURI = "http://localhost:7777/api";
    }


    @Test
    @DisplayName("Test getting all items")
    void getAllItems()
    {
        List<ItemDTO> itemDTOTest = given()
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .body("size()", is(5))
                .log().all()
                .extract()
                .as(new TypeRef<List<ItemDTO>>()
                {
                });

        assertThat(itemDTOTest.size(), is(5));

    }

    @Test
    @DisplayName("Test getting item with ID 1")
    void getItemById()
    {
        given()
                .when()
                .get("/items/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    @DisplayName("Test creating a new Item")
    void createItem()
    {
        ItemDTO itemDTO = getTestItemDTO();

        try
        {
            String json = objectMapper.writeValueAsString(itemDTO);
            given()
                    .when()
                    .body(json)
                    .contentType(contentType)
                    .accept(contentType)
                    .post("/items")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Test item"));
        } catch (JsonProcessingException jpe)
        {
            jpe.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Test updating an Item")
    void updateItem()
    {
        ItemDTO itemDTO = getTestItemDTO();
        try
        {
            String json = objectMapper.writeValueAsString(itemDTO);

            given()
                    .when()
                    .body(json)
                    .contentType(contentType)
                    .accept(contentType)
                    .put("items/1")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Test item"));
        } catch (JsonProcessingException jpe)
        {
            jpe.printStackTrace();
            fail();
        }
    }

    @Test
    void deleteItem()
    {
        when()
                .delete("items/1")
                .then()
                .statusCode(204);

        when()
                .get("items/1")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Test assigning an item to a student")
    void addItemToStudent()
    {
        given()
                .when()
                .contentType(contentType)
                .accept(contentType)
                .put("items/5/students/1")
                .then()
                .statusCode(200)
                .body("student.name", equalTo("Student 1"));

    }

    // TODO create this route ?
    @Test
    void getItemsByStudent()
    {

    }

    // helper methods to create test DTOs
    private ItemDTO getTestItemDTO()
    {
        ItemDTO itemDTO = ItemDTO.builder()
                .name("Test item")
                .purchasePrice(BigDecimal.valueOf(11.11))
                .category(ItemCategory.PRINT)
                .acquisitionDate(null) // jackson can't handle LocalDate for some reason
                .description("This is a test item")
                .build();
        return itemDTO;
    }

    private StudentDTO getTestStudentDTO()
    {
        StudentDTO studentDTO = StudentDTO.builder()
                .id(42L)
                .name("Test student")
                .enrollmentDate(null) // jackson can't handle LocalDate for some reason
                .phone(13276548)
                .itemList(new HashSet<>())
                .build();

        return studentDTO;
    }
}