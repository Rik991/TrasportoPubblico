package it.epicode;

import com.github.javafaker.Faker;
import it.epicode.dao.ParcoMezziDAO;
import it.epicode.dao.RivenditoreDAO;
import it.epicode.entity.biglietteria.Rivenditore;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Locale;

public class MainCreate {
    public static void main(String[] args) {
        Faker faker = new Faker(new Locale("it"));
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
        EntityManager em = emf.createEntityManager();

        ParcoMezziDAO parcoMezziDAO = new ParcoMezziDAO(em);
        RivenditoreDAO rivenditoreDAO = new RivenditoreDAO(em);






    }
}

