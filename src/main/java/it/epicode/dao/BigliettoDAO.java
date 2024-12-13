package it.epicode.dao;

import it.epicode.entity.biglietteria.Biglietto;
import it.epicode.entity.biglietteria.Tratta;
import it.epicode.entity.biglietteria.Vendita;

import it.epicode.entity.user.User;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
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

    public void emettiBiglietto(Vendita vendita, Tratta tratta, User user) {
        em.getTransaction().begin();
        Biglietto biglietto = new Biglietto();
        biglietto.setVidimato(false);
        biglietto.setVendita(vendita);
        biglietto.setTratta(tratta);
        biglietto.setUser(user);
        em.persist(biglietto);
        em.getTransaction().commit();
    }

    public void vidimaBiglietto(Biglietto biglietto, Tratta tratta) {
        if (biglietto.isVidimato()) {
            throw new IllegalArgumentException("Biglietto già vidimato");
        }
        em.getTransaction().begin();
        biglietto.setVidimato(true);
        biglietto.getTratta().setNumeroBigliettiVidimati(biglietto.getTratta().getNumeroBigliettiVidimati() + 1);
        biglietto.setTratta(tratta);
        biglietto.setDataVidimazione(LocalDate.now());
        em.merge(biglietto);
        em.getTransaction().commit();
    }

    public List<Biglietto> findBigliettiVidimatiByDate(LocalDate dataInizio, LocalDate dataFine) {
        return em.createQuery("SELECT b FROM Biglietto b " +
                        "WHERE b.dataVidimazione " +
                        "BETWEEN :dataInizio AND :dataFine", Biglietto.class)
                .setParameter("dataInizio", dataInizio)
                .setParameter("dataFine", dataFine)
                .getResultList();
    }

}