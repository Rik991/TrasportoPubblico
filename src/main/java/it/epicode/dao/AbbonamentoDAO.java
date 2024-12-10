package it.epicode.dao;

import it.epicode.entity.biglietteria.*;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
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

    public void emettiAbbonamento(Vendita vendita, Tratta tratta, Tessera tessera, int tipoAbbonamento) {

        em.getTransaction().begin();
        Abbonamento abbonamento = new Abbonamento();
        if(tipoAbbonamento == 1) abbonamento.setValidita(TipoAbbonamento.SETTIMANALE);
        else abbonamento.setValidita(TipoAbbonamento.MENSILE);
        abbonamento.setVendita(vendita);
        abbonamento.setDataEmissione(LocalDate.now());
        if (abbonamento.getValidita().equals(TipoAbbonamento.SETTIMANALE))
            abbonamento.setDataScadenza(abbonamento.getDataEmissione().plusDays(7));
        else abbonamento.setDataScadenza(abbonamento.getDataEmissione().plusDays(30));
        abbonamento.setTratta(tratta);
        abbonamento.setTessera(tessera);
        em.persist(abbonamento);
        em.getTransaction().commit();

    }
}