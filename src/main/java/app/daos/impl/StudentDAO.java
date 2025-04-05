package app.daos.impl;

import app.daos.IDAO;
import app.entities.Student;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class StudentDAO implements IDAO<Student, Long>
{
    private static EntityManagerFactory emf;
    private static StudentDAO instance;

    public static StudentDAO getInstance(EntityManagerFactory _emf)
    {
        if (instance == null)
        {
            instance = new StudentDAO();
        }
        emf = _emf;
        return instance;
    }

    @Override
    public Student create(Student student)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
            return student;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error creating student", e);
        }
    }

    @Override
    public Student read(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(Student.class, id);
        } catch (Exception e)
        {
            throw new ApiException(404, "Error could not find student", e);
        }
    }

    @Override
    public List<Student> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
        } catch (Exception e)
        {
            throw new ApiException(404, "Error finding list of students", e);
        }
    }

    @Override
    public Student update(Student student)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Student updatedStudent = em.merge(student);
            em.getTransaction().commit();
            return updatedStudent;
        } catch (Exception e)
        {
            throw new ApiException(401, "Error updating student", e);
        }
    }

    @Override
    public void delete(Long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Student student = em.find(Student.class, id);
            if (student == null)
            {
                em.getTransaction().rollback();
                throw new NullPointerException();
            }
            em.remove(student);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            throw new ApiException(401, "Error removing student", e);
        }
    }
}
