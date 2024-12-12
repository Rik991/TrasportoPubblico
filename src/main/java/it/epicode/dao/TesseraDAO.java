package it.epicode.dao;

import it.epicode.entity.biglietteria.Abbonamento;
import it.epicode.entity.biglietteria.Ruolo;
import it.epicode.entity.biglietteria.Tessera;

import it.epicode.entity.biglietteria.Vendita;
import it.epicode.entity.user.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
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

    public void saveAllTessere(List<Tessera> tessere) {
        em.getTransaction().begin();
        tessere.forEach(em::persist);
        em.getTransaction().commit();
    }

    public Tessera findTessera(int numeroTessera){
      return em.createQuery("SELECT t FROM Tessera t WHERE t.numeroTessera = :numeroTessera", Tessera.class)
               .setParameter("numeroTessera", numeroTessera).getSingleResult();

    }

    public boolean checkTessera(int numeroTessera){
      return em.createQuery("SELECT t FROM Tessera t WHERE t.numeroTessera = :numeroTessera", Tessera.class)
              .setParameter("numeroTessera", numeroTessera).getResultList().isEmpty();
    }


    public boolean checkRuolo(Tessera tessera) {
        return tessera.getUser().getRuolo().equals(Ruolo.AMMINISTRATORE);
    }

    public Tessera emettiTessera(Vendita vendita, User user){
        em.getTransaction().begin();
        Tessera nuovaTessera = new Tessera();
        nuovaTessera.setNumeroTessera(4000 + user.getId());
        nuovaTessera.setDataEmissione(LocalDate.now());
        nuovaTessera.setDataScadenza(nuovaTessera.getDataEmissione().plusYears(1));
        nuovaTessera.setAttiva(true);
        nuovaTessera.setVendita(vendita);
        nuovaTessera.setUser(user);
        em.persist(nuovaTessera);
        em.getTransaction().commit();
        return nuovaTessera;
    }


}