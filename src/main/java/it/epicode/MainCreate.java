package it.epicode;

import com.github.javafaker.Faker;
import it.epicode.dao.ParcoMezziDAO;
import it.epicode.dao.RivenditoreDAO;
import it.epicode.dao.TrattaDAO;
import it.epicode.entity.biglietteria.Rivenditore;
import it.epicode.entity.biglietteria.Tratta;
import it.epicode.entity.parco_mezzi.Autobus;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Locale;
import java.util.TreeMap;

public class MainCreate {
    public static void main(String[] args) {
        Faker faker = new Faker(new Locale("it"));
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
        EntityManager em = emf.createEntityManager();

        TrattaDAO trattaDAO = new TrattaDAO(em);
        ParcoMezziDAO parcoMezziDAO = new ParcoMezziDAO(em);
        RivenditoreDAO rivenditoreDAO = new RivenditoreDAO(em);

        em.getTransaction().begin();

        //per prima cosa creiamo differenti tratte
        for (int i = 0; i < 10; i++) {
            Tratta tratta = new Tratta();
            tratta.setZonaPartenza(faker.address().city());
            tratta.setZonaArrivo(faker.address().city());
            tratta.setDurataEffettiva(faker.number().randomDouble(2, 1, 10));
            trattaDAO.save(tratta);
        }



    }
}

