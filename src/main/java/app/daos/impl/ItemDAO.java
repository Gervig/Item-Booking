package app.daos.impl;

import app.daos.IDAO;
import app.entities.Item;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ItemDAO implements IDAO<Item, Long>
{
    private static EntityManagerFactory emf;
    private static ItemDAO instance;

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
}
