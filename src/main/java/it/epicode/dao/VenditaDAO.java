package it.epicode.dao;

import it.epicode.entity.biglietteria.Vendita;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class VenditaDAO {
    private EntityManager em;

    public void save(Vendita vendita) {
        em.getTransaction().begin();
        em.persist(vendita);
        em.getTransaction().commit();
    }

    public Vendita findById(Long id) {
        return em.find(Vendita.class, id);
    }

    public List<Vendita> findAll() {
        return em.createNamedQuery("Trova_tutto_Vendita", Vendita.class).getResultList();
    }

    public void update(Vendita vendita) {
        em.getTransaction().begin();
        em.merge(vendita);
        em.getTransaction().commit();
    }

    public void delete(Vendita vendita) {
        em.getTransaction().begin();
        em.remove(vendita);
        em.getTransaction().commit();
    }


}