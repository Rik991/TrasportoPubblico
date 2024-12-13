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
        VenditaDAO venditaDAO = new VenditaDAO(em);


        //creiamo nuove tratte

        Tratta tratta1 = new Tratta();
        Tratta tratta2 = new Tratta();
        Tratta tratta3 = new Tratta();
        Tratta tratta4 = new Tratta();
        Tratta tratta5 = new Tratta();
        Tratta tratta6 = new Tratta();

        tratta1.setZonaPartenza("Roma");
        tratta1.setZonaArrivo("Milano");
        tratta1.setOraDiPartenza(LocalTime.of(8, 0));
        tratta1.setOraDiArrivo(LocalTime.of(16, 30));
        tratta1.setDurataEffettiva(LocalTime.of(8, 30));
        trattaDAO.save(tratta1);

        tratta2.setZonaPartenza("Palermo");
        tratta2.setZonaArrivo("Catania");
        tratta2.setOraDiPartenza(LocalTime.of(9, 0));
        tratta2.setOraDiArrivo(LocalTime.of(12, 30));
        tratta2.setDurataEffettiva(LocalTime.of(3, 30));
        trattaDAO.save(tratta2);

        tratta3.setZonaPartenza("Napoli");
        tratta3.setZonaArrivo("Bologna");
        tratta3.setOraDiPartenza(LocalTime.of(9, 0));
        tratta3.setOraDiArrivo(LocalTime.of(15, 30));
        tratta3.setDurataEffettiva(LocalTime.of(6, 30));
        trattaDAO.save(tratta3);

        //tratta per il tram
        tratta4.setZonaPartenza("Stazione Termini");
        tratta4.setZonaArrivo("Fontana di Trevi");
        tratta4.setOraDiPartenza(LocalTime.of(8, 0));
        tratta4.setOraDiArrivo(LocalTime.of(8, 30));
        tratta4.setDurataEffettiva(LocalTime.of(0, 30));
        trattaDAO.save(tratta4);

        tratta5.setZonaPartenza("Stazione Lambrate (MI)");
        tratta5.setZonaArrivo("Piazza del Duomo");
        tratta5.setOraDiPartenza(LocalTime.of(16, 30));
        tratta5.setOraDiArrivo(LocalTime.of(17, 30));
        tratta5.setDurataEffettiva(LocalTime.of(1, 30));
        trattaDAO.save(tratta5);

        tratta6.setZonaPartenza("Lorenteggio");
        tratta6.setZonaArrivo("Duomo");
        tratta6.setOraDiPartenza(LocalTime.of(12, 0));
        tratta6.setOraDiArrivo(LocalTime.of(12, 30));
        tratta6.setDurataEffettiva(LocalTime.of(0,30));
        trattaDAO.save(tratta6);

        List<Tratta> tratte = new ArrayList<>();
        tratte.add(tratta1);
        tratte.add(tratta2);
        tratte.add(tratta3);
        tratte.add(tratta4);
        tratte.add(tratta5);
        tratte.add(tratta6);

        //creiamo nuovi mezzi
        ParcoMezzi autobus1 = new Autobus();
        ParcoMezzi autobus2 = new Autobus();
        ParcoMezzi autobus3 = new Autobus();
        ParcoMezzi tram1 = new Tram();
        ParcoMezzi tram2 = new Tram();
        ParcoMezzi tram3 = new Tram();

        autobus1.setInServizio(true);
        autobus1.setTratta(tratta1);
        autobus1.setLinea("A1");
        autobus1.setTempoInServizio(LocalTime.of(8, 0));
        autobus1.setTempoInManutenzione(LocalTime.of(0, 0));
        autobus1.setTotaleTratteEffettuate(1);
        parcoMezziDAO.save(autobus1);

        autobus2.setInServizio(false);
        autobus2.setTratta(tratta2);
        autobus2.setLinea("A2");
        autobus2.setTempoInServizio(LocalTime.of(0, 0));
        autobus2.setTempoInManutenzione(LocalTime.of(23, 0));
        autobus2.setTotaleTratteEffettuate(0);
        parcoMezziDAO.save(autobus2);

        autobus3.setInServizio(true);
        autobus3.setTratta(tratta3);
        autobus3.setLinea("A3");
        autobus3.setTempoInServizio(LocalTime.of(23, 0));
        autobus3.setTempoInManutenzione(LocalTime.of(0, 0));
        autobus3.setTotaleTratteEffettuate(0);
        parcoMezziDAO.save(autobus3);

        tram1.setInServizio(true);
        tram1.setTratta(tratta4);
        tram1.setLinea("T1");
        tram1.setTempoInServizio(LocalTime.of(18, 0));
        tram1.setTempoInManutenzione(LocalTime.of(0, 0));
        tram1.setTotaleTratteEffettuate(2);
        parcoMezziDAO.save(tram1);

        tram2.setInServizio(false);
        tram2.setTratta(tratta5);
        tram2.setLinea("T2");
        tram2.setTempoInServizio(LocalTime.of(0, 0));
        tram2.setTempoInManutenzione(LocalTime.of(23, 0));
        tram2.setTotaleTratteEffettuate(0);
        parcoMezziDAO.save(tram2);

        tram3.setInServizio(false);
        tram3.setTratta(tratta6);
        tram3.setLinea("T3");
        tram3.setTempoInServizio(LocalTime.of(10, 0));
        tram3.setTempoInManutenzione(LocalTime.of(0, 0));
        tram3.setTotaleTratteEffettuate(0);
        parcoMezziDAO.save(tram3);

        //creiamo rivenditori

        Rivenditore rivenditore = new Rivenditore();
        Rivenditore rivenditore2 = new Rivenditore();

        rivenditore.setNome("Tabacchino");
        rivenditore2.setNome("Edicola");

        DistributoreAutomatico distributore = new DistributoreAutomatico();
        DistributoreAutomatico distributore2 = new DistributoreAutomatico();

        distributore.setNome("Distributore Tabacchino");
        distributore2.setNome("Distributore Edicola");

        venditaDAO.save(rivenditore);
        venditaDAO.save(rivenditore2);

        venditaDAO.save(distributore);
        venditaDAO.save(distributore2);

        //creiamo nuovi user

        User amministratore = new Amministratore();
        User amministratore2 = new Amministratore();

        amministratore.setNome("Danilo");
        amministratore.setCognome("Fumuso");
        amministratore.setRuolo(Ruolo.AMMINISTRATORE);

        userDAO.save(amministratore);

        amministratore2.setNome("Tammaro");
        amministratore2.setCognome("Miele");
        amministratore2.setRuolo(Ruolo.AMMINISTRATORE);

        userDAO.save(amministratore2);
        List<User> passeggeri = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            User user = new Passeggero();
            user.setNome(faker.name().firstName());
            user.setCognome(faker.name().lastName());
            user.setRuolo(Ruolo.PASSEGGERO);
            passeggeri.add(user);
        }

        userDAO.saveAllUtenti(passeggeri);

        //creiamo tessere
        List<Tessera> tessere = new ArrayList<>();
        List<Tessera> tessere2 = new ArrayList<>();


        Tessera tessera1 = new Tessera();
        tessera1.setNumeroTessera(1001L);
        tessera1.setDataEmissione(LocalDate.now());//sistemare date per scadenza
        tessera1.setDataScadenza(tessera1.getDataEmissione().plusYears(1));
        tessera1.setAttiva(true);
        tessera1.setUser(amministratore);
        tessera1.setVendita(rivenditore);
        tessere.add(tessera1);

        Tessera tessera2 = new Tessera();
        tessera2.setNumeroTessera(1002L);
        tessera2.setDataEmissione(LocalDate.now());//sistemare date per scadenza
        tessera2.setDataScadenza(tessera2.getDataEmissione().plusYears(1));
        tessera2.setAttiva(true);
        tessera2.setUser(amministratore2);
        tessera2.setVendita(rivenditore);
        tessere.add(tessera2);

        tesseraDAO.saveAllTessere(tessere);

        rivenditore.setTessere(tessere);
        rivenditore2.setTessere(tessere2);

        venditaDAO.update(rivenditore);
        venditaDAO.update(rivenditore2);

        distributore.setInServizio(true);
        venditaDAO.update(distributore);

        distributore2.setInServizio(false);
        venditaDAO.update(distributore2);


    }
}

