package it.epicode.dao;

import it.epicode.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserDAO {
    private EntityManager em;

    public void save(User oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createNamedQuery("Trova_tutto_User", User.class).getResultList();
    }

    public void update(User oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(User oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }

    //lato amministratore
//    public double calcolaTempoMedioTratta (){ //da implementare
//        return 0;
//    }

}