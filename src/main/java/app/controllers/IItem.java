package app.controllers;

import java.util.List;

public interface IItem<T, U , I>
{
    List<T> getAllItems();
    T getItemById(I id);
    T createItem(T item);
    T updateItem(T item);
    void deleteItem(I id);
    T addItemToStudent(I itemId, I studentId);
}
