package app.controllers.impl;

import app.config.HibernateConfig;
import app.daos.impl.ItemDAO;
import app.dtos.ErrorMessage;
import app.dtos.ItemDTO;
import app.entities.Item;
import app.entities.Student;
import app.populators.ItemPopulator;
import app.rest.ApplicationConfig;
import app.rest.ItemRoutes;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ItemControllerTest
{

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final ItemController itemController = new ItemController(emf);
    private static final ItemDAO itemDAO = ItemDAO.getInstance(emf);

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
                .as(new TypeRef<List<ItemDTO>>(){});

        assertThat(itemDTOTest.size(), is(5));

    }

    @Test
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
    void createItem()
    {
        given()
                .when()
                .post()
    }

    @Test
    void updateItem()
    {
    }

    @Test
    void deleteItem()
    {
    }

    @Test
    void addItemToStudent()
    {
    }

    @Test
    void getItemsByStudent()
    {
    }
}