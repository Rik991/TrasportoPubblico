package it.epicode.dao;

import it.epicode.entity.parco_mezzi.ParcoMezzi;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ParcoMezziDAO {
    private EntityManager em;

    public void save(ParcoMezzi oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public ParcoMezzi findById(Long id) {
        return em.find(ParcoMezzi.class, id);
    }

    public List<ParcoMezzi> findAll() {
        return em.createNamedQuery("Trova_tutto_ParcoMezzi", ParcoMezzi.class).getResultList();
    }

    public void update(ParcoMezzi oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(ParcoMezzi oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }


}