package it.epicode;

import com.github.javafaker.Faker;
import it.epicode.dao.*;
import it.epicode.entity.biglietteria.Biglietto;
import it.epicode.entity.biglietteria.DistributoreAutomatico;
import it.epicode.entity.biglietteria.Vendita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MainUser {


    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
        EntityManager em = emf.createEntityManager();

        TrattaDAO trattaDAO = new TrattaDAO(em);
        ParcoMezziDAO parcoMezziDAO = new ParcoMezziDAO(em);
        UserDAO userDAO = new UserDAO(em);
        TesseraDAO tesseraDAO = new TesseraDAO(em);
        AbbonamentoDAO abbonamentoDAO = new AbbonamentoDAO(em);
        BigliettoDAO bigliettoDAO = new BigliettoDAO(em);
        VenditaDAO venditaDAO = new VenditaDAO(em);



        Scanner scanner = new Scanner(System.in);
        System.out.println("1 per acquistare biglietto, 2 per acquistare abbonamento");
        int scelta = scanner.nextInt();
        scanner.nextLine();

        switch (scelta) {
            case 1: //vendita biglietti
                System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
                int acquisto = scanner.nextInt();
                scanner.nextLine();
                if (acquisto == 1) {
                    bigliettoDAO.emettiBiglietto(venditaDAO.findById(1L), trattaDAO.findById(1L));
                } else {
                    List<DistributoreAutomatico> distributori = venditaDAO.findAllDistributors();
                    System.out.println("Seleziona il distributore:");
                    for (int i = 0; i < distributori.size(); i++) {
                        System.out.println((i + 2) + ". Distributore " + distributori.get(i).getId());
                    }
                    int distributoreScelto = scanner.nextInt() - 2;
                    scanner.nextLine();
                    if (distributoreScelto >= 0 && distributoreScelto < distributori.size()) {
                        DistributoreAutomatico distributore = distributori.get(distributoreScelto);
                        if (distributore.isInServizio()) {
                            bigliettoDAO.emettiBiglietto(distributore, trattaDAO.findById(1L));
                        } else {
                            System.out.println("Distributore fuori servizio!");
                        }
                    } else {
                        System.out.println("Selezione non valida!");
                    }
                }
                break;
            case 2:  //vendita abbonamenti

                break;
            default:
                System.out.println("Scelta non valida!");
                break;
        }



    }
}
