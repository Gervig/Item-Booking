package app.rest;

import app.controllers.impl.ItemController;
import app.dtos.ErrorMessage;
import app.dtos.ItemDTO;
import app.entities.Item;
import app.populators.ItemPopulator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.*;


public class ItemRoutes
{
    // declare static controllers here
    private static Logger logger = LoggerFactory.getLogger(ItemRoutes.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static EndpointGroup getRoutes(EntityManagerFactory emf)
    {
        // instantiates controller with emf
        ItemController itemController = new ItemController(emf);

        return () ->
        {
            path("items", () ->
            {
                get("/", ctx ->
                {
                    logger.info("Information about the resource that was accessed: " + ctx.path());
                    List<ItemDTO> itemDTOS = itemController.getAllItems();
                    ctx.json(itemDTOS);
                });
                post("/populate", ctx ->
                {
                    List<Item> items = ItemPopulator.populate();
                    try
                    {
                        List<ItemDTO> itemDTOS = items.stream()
                                .map(item -> new ItemDTO(item, true))
                                .map(itemController::createItem)
                                .toList();
                        ctx.json(itemDTOS);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    }
                });
            });
        };
    }
}