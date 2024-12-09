package it.epicode.dao;

import it.epicode.entity.biglietteria.Biglietto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BigliettoDAO {
    private EntityManager em;

    public void save(Biglietto oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public Biglietto findById(Long id) {
        return em.find(Biglietto.class, id);
    }

    public List<Biglietto> findAll() {
        return em.createNamedQuery("Trova_tutto_Biglietto", Biglietto.class).getResultList();
    }

    public void update(Biglietto oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(Biglietto oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }

    public void saveAllBiglietti(List<Biglietto> biglietti) {
        em.getTransaction().begin();
        for (Biglietto biglietto : biglietti) {
            em.persist(biglietto);
        }
        em.getTransaction().commit();

    }


}