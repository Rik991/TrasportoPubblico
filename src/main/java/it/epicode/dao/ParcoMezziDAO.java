package it.epicode.dao;

import it.epicode.entity.biglietteria.Tratta;
import it.epicode.entity.parco_mezzi.Autobus;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import it.epicode.entity.parco_mezzi.Tram;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
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

    public void createBus(String linea, boolean inServizio, String zonaPartenza, String zonaArrivo, LocalTime oraDiPartenza, LocalTime oraDiArrivo ) {
        em.getTransaction().begin();
        Autobus nuovoAutobus = new Autobus();
        nuovoAutobus.setLinea(linea);
        nuovoAutobus.setInServizio(inServizio);
        em.persist(nuovoAutobus);

        Duration durata = Duration.between(oraDiPartenza, oraDiArrivo);


        Tratta nuovaTratta = new Tratta();
        nuovaTratta.setZonaPartenza(zonaPartenza);
        nuovaTratta.setZonaArrivo(zonaArrivo);
        nuovaTratta.setOraDiPartenza(oraDiPartenza);
        nuovaTratta.setOraDiArrivo(oraDiArrivo);
        nuovaTratta.setDurataEffettiva(LocalTime.of((int) durata.toHours(), (int) durata.toMinutes() % 60));
        em.persist(nuovaTratta);

        em.getTransaction().commit();
    }

    public void createTram(String linea, boolean inServizio, String zonaPartenza, String zonaArrivo, LocalTime oraDiPartenza, LocalTime oraDiArrivo) {
        em.getTransaction().begin();
        Tram nuovoTram = new Tram();
        nuovoTram.setLinea(linea);
        nuovoTram.setInServizio(inServizio);
        em.persist(nuovoTram);

        Duration durata = Duration.between(oraDiPartenza, oraDiArrivo);

        Tratta nuovaTratta = new Tratta();
        nuovaTratta.setZonaPartenza(zonaPartenza);
        nuovaTratta.setZonaArrivo(zonaArrivo);
        nuovaTratta.setOraDiPartenza(oraDiPartenza);
        nuovaTratta.setOraDiArrivo(oraDiArrivo);
        nuovaTratta.setDurataEffettiva(LocalTime.of((int) durata.toHours(), (int) durata.toMinutes()%60));
        em.persist(nuovaTratta);

        em.getTransaction().commit();
    }

    public void isAutobusOrTram(List<ParcoMezzi> mezzi, int tipoMezzoScelto) {
        for (int i = 0; i < mezzi.size(); i++) {
           ParcoMezzi mezzo = mezzi.get(i);
            if (mezzo instanceof Autobus && tipoMezzoScelto == 1) {
                System.out.println((i + 1) + " Autobus " + mezzo.getLinea() + " che viaggia da: " + mezzo.getTratta().getZonaPartenza() + " a " + mezzo.getTratta().getZonaArrivo());
            } else if (mezzo instanceof Tram && tipoMezzoScelto == 2) {
                System.out.println((i + 1) + " Tram " + mezzo.getLinea() + " che viaggia da: " + mezzo.getTratta().getZonaPartenza() + " a " + mezzo.getTratta().getZonaArrivo());
            }
        }

    }
}