package app.controllers.impl;

import app.controllers.IItem;
import app.daos.impl.ItemDAO;
import app.daos.impl.StudentDAO;
import app.dtos.ItemDTO;
import app.dtos.StudentDTO;
import app.entities.Item;
import app.entities.Student;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class ItemController implements IItem<ItemDTO, StudentDTO, Long>
{
    private static EntityManagerFactory emf;
    private ItemDAO itemDAO;
    private StudentDAO studentDAO;

    public ItemController(EntityManagerFactory _emf)
    {
        if (emf == null)
        {
            emf = _emf;
        }
        this.itemDAO = ItemDAO.getInstance(emf);
        this.studentDAO = StudentDAO.getInstance(emf);
    }

    @Override
    public List<ItemDTO> getAllItems() throws ApiException
    {
        List<Item> items = itemDAO.readAll();
        List<ItemDTO> itemDTOS = items
                .stream()
                .map(item -> new ItemDTO(item, true))
                .toList();
        return itemDTOS;
    }

    @Override
    public ItemDTO getItemById(Long id) throws ApiException
    {
        Item item = itemDAO.read(id);
        ItemDTO itemDTO = new ItemDTO(item, true);
        return itemDTO;
    }

    @Override
    public ItemDTO createItem(ItemDTO itemDTO) throws ApiException
    {
        Item item = itemDTO.toEntity();
        Student student = item.getStudent();
        // checks if the student exists,
        // if not creates them and adds them to the item
//        if (student != null && student.getId() != null)
//        {
//            student = studentDAO.create(student);
//            item.setStudent(student);
//        }
        // checks if item has already been created
        if (itemDTO.getId() == null)
        {
            item = itemDAO.create(item);
        }
        ItemDTO newItemDTO = new ItemDTO(item, true);
        return newItemDTO;
    }

    @Override
    public ItemDTO updateItem(ItemDTO itemDTO) throws ApiException
    {
        Item existingItem = itemDAO.read(itemDTO.getId());
        if (existingItem == null)
        {
            throw new ApiException(404, "Item not found");
        }
        //TODO clean this up

        // ensures no null values
        if (itemDTO.getName() != null) existingItem.setName(itemDTO.getName());
        if (itemDTO.getPurchasePrice() != null) existingItem.setPurchasePrice(itemDTO.getPurchasePrice());
        if (itemDTO.getCategory() != null) existingItem.setCategory(itemDTO.getCategory());
        if (itemDTO.getAcquisitionDate() != null) existingItem.setAcquisitionDate(itemDTO.getAcquisitionDate());
        if (itemDTO.getDescription() != null) existingItem.setDescription(itemDTO.getDescription());
        if (itemDTO.getStudent() != null)
        {
            // tries to find the student
            Student existingStudent = studentDAO.read(itemDTO.getStudent().getId());
            if (existingStudent == null)
            {
                // if it's a new student, creates them
                Student newStudent = itemDTO.getStudent().toEntity();
                newStudent = studentDAO.create(newStudent);
                // sets newly persisted student to item
                existingItem.setStudent(newStudent);
            } else
            {
                // if they already exists, sets existing student to item
                existingItem.setStudent(existingStudent);
            }
        }

        Item updatedItem = itemDAO.update(existingItem);
        return new ItemDTO(updatedItem, true);
    }

    @Override
    public void deleteItem(Long id) throws ApiException
    {
        itemDAO.delete(id);
    }

    @Override
    public ItemDTO addItemToStudent(Long itemId, Long studentId)
    {
        Item item = itemDAO.addItemToStudent(itemId, studentId);
        ItemDTO itemDTO = new ItemDTO(item, true);
        return itemDTO;
    }

    public Set<ItemDTO> getItemsByStudent(Long studentId)
    {
        Set<ItemDTO> itemDTOS = itemDAO.getItemsByStudent(studentId);
        return itemDTOS;
    }
}
