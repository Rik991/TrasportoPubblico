package it.epicode.dao;

import it.epicode.entity.biglietteria.Abbonamento;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AbbonamentoDAO {
    private EntityManager em;

    public void save(Abbonamento oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public Abbonamento findById(Long id) {
        return em.find(Abbonamento.class, id);
    }

    public List<Abbonamento> findAll() {
        return em.createNamedQuery("Trova_tutto_Abbonamento", Abbonamento.class).getResultList();
    }

    public void update(Abbonamento oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(Abbonamento oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }

    public void saveAllAbbonamenti(List<Abbonamento> abbonamenti) {
        em.getTransaction().begin();
        for (Abbonamento abbonamento : abbonamenti) {
            em.persist(abbonamento);
        }
        em.getTransaction().commit();
    }
}