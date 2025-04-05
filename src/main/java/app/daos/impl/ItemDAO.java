package app.daos.impl;

import app.daos.IDAO;
import app.entities.Item;
import app.entities.Student;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ItemDAO implements IDAO<Item, Long>
{
    private static EntityManagerFactory emf;
    private static ItemDAO instance;
    private static StudentDAO studentDAO = StudentDAO.getInstance(emf);

    public static ItemDAO getInstance(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            instance = new ItemDAO();
        }
        emf = _emf;
        return instance;
    }

    @Override
    public Item create(Item item)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
            return item;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error creating item", e);
        }
    }

    @Override
    public Item read(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(Item.class, id);
        } catch (Exception e)
        {
            throw new ApiException(404, "Error could not find item", e);
        }
    }

    @Override
    public List<Item> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(404, "Error finding list of items", e);
        }
    }

    @Override
    public Item update(Item item)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Item updatedItem = em.merge(item);
            em.getTransaction().commit();
            return updatedItem;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating item", e);
        }
    }

    @Override
    public void delete(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Item item = em.find(Item.class, id);
            if (item == null)
            {
                em.getTransaction().rollback();
                throw new NullPointerException();
            }
            em.remove(item);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error removing item", e);
        }
    }

    public void addItemToStudent(Long itemId, Long studentId)
    {
        try(EntityManager em = emf.createEntityManager())
        {
            Item item = em.find(Item.class, itemId);
            Student student = em.find(Student.class, studentId);
            if(item == null || student == null)
            {
                throw new NullPointerException();
            }
            student.addItem(item);
            item.setStudent(student);
            studentDAO.update(student);
            update(item);
        } catch (Exception e)
        {
            throw new ApiException(401, "Error adding item to student", e);
        }
    }
}
