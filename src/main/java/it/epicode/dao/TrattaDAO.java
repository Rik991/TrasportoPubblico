package it.epicode.dao;

import it.epicode.entity.biglietteria.Tratta;
import it.epicode.entity.parco_mezzi.Autobus;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TrattaDAO {
    private EntityManager em;

    public void save(Tratta oggetto) {
        em.getTransaction().begin();
        em.persist(oggetto);
        em.getTransaction().commit();
    }

    public Tratta findById(Long id) {
        return em.find(Tratta.class, id);
    }

    public List<Tratta> findAll() {
        return em.createNamedQuery("Trova_tutto_Tratta", Tratta.class).getResultList();
    }

    public void update(Tratta oggetto) {
        em.getTransaction().begin();
        em.merge(oggetto);
        em.getTransaction().commit();
    }

    public void delete(Tratta oggetto) {
        em.getTransaction().begin();
        em.remove(oggetto);
        em.getTransaction().commit();
    }


    public void createTratta(String linea, boolean inServizio){
        em.getTransaction().begin();
        Autobus nuovoAutobus = new Autobus();
        nuovoAutobus.setLinea(linea);
        nuovoAutobus.setInServizio(inServizio);
        em.persist(nuovoAutobus);
        em.getTransaction().commit();
    }

    public ParcoMezzi findMezzoByTratta(Tratta tratta){
        return em.createQuery("SELECT p FROM ParcoMezzi p WHERE p.tratta = :tratta", ParcoMezzi.class)
                .setParameter("tratta", tratta)
                .getSingleResult();
    }
}