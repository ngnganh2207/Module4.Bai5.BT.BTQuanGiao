package com.codegym.repository.implStudent;

import com.codegym.model.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class StudentRepository implements IStudentRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query= em.createQuery("select s from Student s", Student.class);
        return query.getResultList();
    }

    @Override
    public Student findById(Long id) {
        TypedQuery<Student> query= em.createQuery("select s from Student s where s.id=:id",Student.class);
        query.setParameter("id", id);
        return query.getSingleResult();

    }

    @Override
    public List<Student> findByName(String name) {
        TypedQuery<Student> query= em.createQuery("select s from Student s where s.name like:name",Student.class);
        query.setParameter("name", "%"+name+"%");
        return query.getResultList();
    }

    @Override
    public void save(Student student) {
        if(student.getId()!=null){
            em.merge(student);
        }else{
            em.persist(student);
        }
    }

    @Override
    public void remove(Student student) {
        if(student!=null){
            em.remove(student);
        }
    }
}
