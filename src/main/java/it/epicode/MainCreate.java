package it.epicode;

import com.github.javafaker.Faker;
import it.epicode.dao.*;
import it.epicode.entity.biglietteria.*;
import it.epicode.entity.parco_mezzi.Autobus;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import it.epicode.entity.parco_mezzi.Tram;
import it.epicode.entity.user.Amministratore;
import it.epicode.entity.user.Passeggero;
import it.epicode.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class MainCreate {
    public static void main(String[] args) {
        Faker faker = new Faker(new Locale("it"));
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
        EntityManager em = emf.createEntityManager();

        TrattaDAO trattaDAO = new TrattaDAO(em);
        ParcoMezziDAO parcoMezziDAO = new ParcoMezziDAO(em);
        UserDAO userDAO = new UserDAO(em);
        TesseraDAO tesseraDAO = new TesseraDAO(em);
        AbbonamentoDAO abbonamentoDAO = new AbbonamentoDAO(em);
        BigliettoDAO bigliettoDAO = new BigliettoDAO(em);
        RivenditoreDAO rivenditoreDAO = new RivenditoreDAO(em);


        //creiamo nuove tratte

        Tratta tratta1 = new Tratta();
        Tratta tratta2 = new Tratta();
        Tratta tratta3 = new Tratta();
        Tratta tratta4 = new Tratta();

        tratta1.setZonaPartenza("Roma");
        tratta1.setZonaArrivo("Milano");
        tratta1.setOraDiPartenza(LocalTime.of(8, 0));
        tratta1.setOraDiArrivo(LocalTime.of(16, 30));
        tratta1.setDurataEffettiva(LocalTime.of(8, 30));
        trattaDAO.save(tratta1);

        tratta2.setZonaPartenza("Marsala");
        tratta2.setZonaArrivo("Messina");
        tratta2.setOraDiPartenza(LocalTime.of(9, 0));
        tratta2.setOraDiArrivo(LocalTime.of(12, 30));
        tratta2.setDurataEffettiva(LocalTime.of(3, 30));
        trattaDAO.save(tratta2);

        //tratta per il tram
        tratta3.setZonaPartenza("Stazione Termini");
        tratta3.setZonaArrivo("Fontana di Trevi");
        tratta3.setOraDiPartenza(LocalTime.of(8, 0));
        tratta3.setOraDiArrivo(LocalTime.of(8, 30));
        tratta3.setDurataEffettiva(LocalTime.of(0, 30));
        trattaDAO.save(tratta3);

        tratta4.setZonaPartenza("Stazione Lambrate (MI)");
        tratta4.setZonaArrivo("Piazza del Duomo");
        tratta4.setOraDiPartenza(LocalTime.of(16, 0));
        tratta4.setOraDiArrivo(LocalTime.of(17, 30));
        tratta4.setDurataEffettiva(LocalTime.of(1, 30));
        trattaDAO.save(tratta4);

        List<Tratta> tratte = new ArrayList<>();
        tratte.add(tratta1);
        tratte.add(tratta2);
        tratte.add(tratta3);
        tratte.add(tratta4);



        //creiamo nuovi mezzi
        ParcoMezzi autobus1 = new Autobus();
        ParcoMezzi autobus2 = new Autobus();
        ParcoMezzi tram1 = new Tram();
        ParcoMezzi tram2 = new Tram();

        autobus1.setInServizio(true); //in servizio
        autobus1.setTratta(tratta1); //roma-milano
        autobus1.setTempoInServizio(LocalTime.of(8, 0));
        autobus1.setTempoInManutenzione(LocalTime.of(0, 0));
        autobus1.setNumeroBigliettiVidimati(40);
        autobus1.setTotaleTratteEffettuate(1);
        parcoMezziDAO.save(autobus1);

        autobus2.setInServizio(false); //in manutenzione
        autobus2.setTratta(tratta2); //marsala-messina
        autobus2.setTempoInServizio(LocalTime.of(0, 0));
        autobus2.setTempoInManutenzione(LocalTime.of(23, 0));
        autobus2.setNumeroBigliettiVidimati(0);
        autobus2.setTotaleTratteEffettuate(0);
        parcoMezziDAO.save(autobus2);

        tram1.setInServizio(true); //in servizio
        tram1.setTratta(tratta3); //roma-milano
        tram1.setTempoInServizio(LocalTime.of(18, 0));
        tram1.setTempoInManutenzione(LocalTime.of(0, 0));
        tram1.setNumeroBigliettiVidimati(400);
        tram1.setTotaleTratteEffettuate(20);
        parcoMezziDAO.save(tram1);

        tram2.setInServizio(false); //in manutenzione
        tram2.setTratta(tratta4);
        tram2.setTempoInServizio(LocalTime.of(0, 0));
        tram2.setTempoInManutenzione(LocalTime.of(23, 0));
        tram2.setNumeroBigliettiVidimati(0);
        tram2.setTotaleTratteEffettuate(0);
        parcoMezziDAO.save(tram2);

        //creiamo tessere
        for (int i = 0; i < 10; i++) {
            Tessera tessera = new Tessera();
            tessera.setNumeroTessera(1000 + i);
            tessera.setDataEmissione(LocalDate.now());//sistemare date per scadenza
            tessera.setDataScadenza(tessera.getDataEmissione().plusYears(1));
            tessera.setAttiva(true);
            tessera.setRuolo(faker.options().option(Ruolo.class));
            tesseraDAO.save(tessera);
        }

        //creiamo biglietti
        List<Biglietto> biglietti = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Biglietto biglietto = new Biglietto();
            biglietto.setVidimato(false);
            biglietto.setTratta(tratte.get(faker.number().numberBetween(0, tratte.size())));
            biglietti.add(biglietto);
        }
        bigliettoDAO.saveAllBiglietti(biglietti);


        //creiamo abbonamenti
        List<Abbonamento> abbonamenti = new ArrayList<>();









        //creiamo nuovi user



























    }
}

