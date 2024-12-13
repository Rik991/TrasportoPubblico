package it.epicode.dao;

import it.epicode.entity.biglietteria.*;
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

    //mensili nominativi per utenti con tessera
    public void emettiAbbonamento() {
    }


    public List<DistributoreAutomatico> findAllDistributori() {
        return em.createQuery("SELECT d FROM DistributoreAutomatico d", DistributoreAutomatico.class)
                .getResultList();
    }

    public List<Rivenditore> findAllRivenditori() {
        return em.createQuery("SELECT r FROM Rivenditore r", Rivenditore.class)
                .getResultList();
    }

    public List<Biglietto> findBigliettiByVenditore(Vendita venditore){
        return em.createQuery("SELECT b FROM Biglietto b WHERE b.vendita = :vendita", Biglietto.class)
                .setParameter("vendita", venditore)
                .getResultList();

    } public List<Abbonamento> findAbbonamentiByVenditore(Vendita venditore){
        return em.createQuery("SELECT a FROM Abbonamento a WHERE a.vendita = :vendita", Abbonamento.class)
                .setParameter("vendita", venditore)
                .getResultList();
    }

}