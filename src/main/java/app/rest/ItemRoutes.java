package app.rest;

import app.controllers.impl.ItemController;
import app.dtos.ErrorMessage;
import app.dtos.ItemDTO;
import app.entities.Item;
import app.entities.Student;
import app.populators.ItemPopulator;
import app.populators.StudentPopulator;
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
                get("/{id}", ctx ->
                {
                    try
                    {
                        Long id = Long.valueOf(ctx.pathParam("id"));
                        ItemDTO itemDTO = itemController.getItemById(id);
                        if (itemDTO == null)
                        {
                            throw new NullPointerException();
                        }
                        ctx.json(itemDTO);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No Item with that ID");
                        ctx.status(404).json(error);
                    }
                });
                post("/", ctx ->
                {
                    try
                    {
                        ItemDTO incomingItem = ctx.bodyAsClass(ItemDTO.class);
                        ItemDTO returnedItem = itemController.createItem(incomingItem);
                        if(returnedItem == null)
                        {
                            throw new NullPointerException();
                        }
                        ctx.json(returnedItem);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    } catch (NullPointerException npe)
                    {
                        ErrorMessage error = new ErrorMessage("Item with that name already exists");
                        ctx.status(400).json(error);
                    }
                });
                put("/{id}", ctx ->
                {
                    Long id = Long.valueOf(ctx.pathParam("id"));
                    try
                    {
                        ItemDTO test = itemController.getItemById(id);
                        if (test == null)
                        {
                            throw new NullPointerException();
                        }
                        ItemDTO incomingTrip = ctx.bodyAsClass(ItemDTO.class);
                        if(incomingTrip.getId() == null)
                        {
                            incomingTrip.setId(id); // in case the body forgot to add the id
                        }
                        ItemDTO returnedTrip = itemController.updateItem(incomingTrip);
                        ctx.json(returnedTrip);
                    } catch (IllegalStateException ise)
                    {
                        ErrorMessage error = new ErrorMessage("Incorrect JSON");
                        ctx.status(400).json(error);
                    } catch (Exception e)
                    {
                        ErrorMessage error = new ErrorMessage("No trip with that ID");
                        ctx.status(404).json(error);
                    }
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