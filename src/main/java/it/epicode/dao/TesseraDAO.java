package it.epicode.dao;

import it.epicode.entity.biglietteria.Tessera;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TesseraDAO {
    private EntityManager em;

    public void save(Tessera oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public Tessera findById(Long id) {
        return em.find(Tessera.class, id);
    }

    public List<Tessera> findAll() {
        return em.createNamedQuery("Trova_tutto_Tessera", Tessera.class).getResultList();
    }

    public void update(Tessera oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(Tessera oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }


}