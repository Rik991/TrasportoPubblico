package it.epicode;

import it.epicode.dao.*;
import it.epicode.entity.biglietteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

public class MainUser {

    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("unit-jpa");
    public static EntityManager em = emf.createEntityManager();

    public static TrattaDAO trattaDAO = new TrattaDAO(em);
    public static ParcoMezziDAO parcoMezziDAO = new ParcoMezziDAO(em);
    public static UserDAO userDAO = new UserDAO(em);
    public static TesseraDAO tesseraDAO = new TesseraDAO(em);
    public static AbbonamentoDAO abbonamentoDAO = new AbbonamentoDAO(em);
    public static BigliettoDAO bigliettoDAO = new BigliettoDAO(em);
    public static VenditaDAO venditaDAO = new VenditaDAO(em);
    public static Scanner scanner = new Scanner(System.in);

    public static Vendita chooseVenditore(int numero) {
        Vendita venditore = null;

        if (numero == 1) {
            List<Rivenditore> rivenditori = venditaDAO.findAllRivenditori();
            System.out.println("Seleziona il Rivenditore:");
            for (int i = 0; i < rivenditori.size(); i++) {
                System.out.println((i + 1) + ". Rivenditore " + rivenditori.get(i).getId());
            }
            int rivenditoreScelto = scanner.nextInt() - 1;
            scanner.nextLine();
            if (rivenditoreScelto >= 0 && rivenditoreScelto < rivenditori.size()) {
                venditore = rivenditori.get(rivenditoreScelto);
            } else {
                System.out.println("Rivenditore non presente nella lista!");
            }
        } else if (numero == 2) {
            List<DistributoreAutomatico> distributori = venditaDAO.findAllDistributori();
            System.out.println("Seleziona il distributore:");
            for (int i = 0; i < distributori.size(); i++) {
                System.out.println((i + 1) + ". Distributore " + (distributori.get(i).getId() - 1));
            }
            int distributoreScelto = scanner.nextInt() - 1;
            scanner.nextLine();
            if (distributoreScelto >= 0 && distributoreScelto < distributori.size()) {
                if (distributori.get(distributoreScelto).isInServizio()) {
                    venditore = distributori.get(distributoreScelto);
                } else {
                    System.out.println("Il distributore non è in servizio");
                }

            } else {
                System.out.println("Distributore non presente nella lista!");
            }
        } else {
            System.out.println("Scegliere 1 o 2 per acquistare dalla lista dei venditori!");
        }

        return venditore;
    }
    public static Tratta chooseTratta (int numero){
        Tratta tratta = null;
        List<Tratta> tratte = trattaDAO.findAll();
        if (numero >= 0 && numero < tratte.size()) {
            tratta = tratte.get(numero);
        } else {
            System.out.println("Tratta non presente nella lista!");
        }
        return tratta;
    }

    public static void main(String[] args) {

        System.out.println("Salve, dove vuoi andare?");
        List<Tratta> tratte = trattaDAO.findAll();
        System.out.println("Seleziona la tratta:");
        for (int i = 0; i < tratte.size(); i++) {
            System.out.println((i + 1) + ". Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
        }
        int trattaScelta = scanner.nextInt() - 1;
        scanner.nextLine();
        if (trattaScelta >= 0 && trattaScelta < tratte.size()) {
            System.out.println("Perfetto! Pronto a partire da " + tratte.get(trattaScelta).getZonaPartenza() + " a " + tratte.get(trattaScelta).getZonaArrivo());
            System.out.println("il viaggio durerà circa " + tratte.get(trattaScelta).getDurataEffettiva() + " minuti");
        } else {
            System.out.println("Tratta non presente nella lista!");
        }



        System.out.println(" 1- per comprare un biglietto, 2- per comprare un abbonamento (tessera richiesta!)");
        int titoloDiViaggio = scanner.nextInt();
        scanner.nextLine();

        switch (titoloDiViaggio) {
            case 1: //vendita biglietti
                System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
                int acquisto = scanner.nextInt();
                scanner.nextLine();
                Vendita venditore = chooseVenditore(acquisto);
                Tratta tratta = chooseTratta(trattaScelta);
                if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                    bigliettoDAO.emettiBiglietto(venditore, tratta);
                }
                break;

            case 2:
                System.out.println("Hai una tessera? Rispondi si o no");
                String scelta = scanner.nextLine().toLowerCase();

                switch (scelta) {
                    case "si":
                        System.out.println("Salve, inserire numero di tessera!");
                        int numeroTessera = scanner.nextInt();
                        scanner.nextLine();
                        Tessera tessera = tesseraDAO.findUserByNumeroTessera(numeroTessera);
                        if (tesseraDAO.checkRuolo(tessera)) {

                            // CODICE PER AMMINISTRATORE

                        } else {

                            // CODICE PER PASSEGGERO CON TESSERA
                        }
                        break;

                    case "no":
                        System.out.println("Inserisci il tuo userID");
                        Long userId = scanner.nextLong();
                        scanner.nextLine();

                        System.out.println("Dove vuoi acquistare la tessera? 1 per rivenditore, 2 per distributore");
                        int acquistoTessera = scanner.nextInt();
                        scanner.nextLine();
                        chooseVenditore(acquistoTessera);
                }

//                        tesseraDAO.emettiTessera(venditaDAO.findById(), userDAO.findById(userId));

                break;
            default:
                System.out.println("Scegliere 1 o 2 per acquistare il prodotto desiderato!");

        }


    }


    //Menu

//        System.out.println("1 per acquistare biglietto, 2 per acquistare abbonamento");
//    int scelta = scanner.nextInt();
//        scanner.nextLine();


//           //vendita abbonamenti
//                System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
//                int acquisto2Abbonamento = scanner.nextInt();
//                System.out.println("1. Settimanale - 2. Mensile");
//                int tipoabbonamento = scanner.nextInt();
//                scanner.nextLine();
//                if (acquistoAbbonamento == 1) {
//                    abbonamentoDAO.emettiAbbonamento(venditaDAO.findById(1L), trattaDAO.findById(1L), tesseraDAO.findById(11L), tipoabbonamento);
//                } else {
//                    List<DistributoreAutomatico> distributori = venditaDAO.findAllDistributors();
//                    System.out.println("Seleziona il distributore:");
//                    for (int i = 0; i < distributori.size(); i++) {
//                        System.out.println((i + 2) + ". Distributore " + distributori.get(i).getId());
//                    }
//                    int distributoreScelto = scanner.nextInt() - 2;
//                    scanner.nextLine();
//                    if (distributoreScelto >= 0 && distributoreScelto < distributori.size()) {
//                        DistributoreAutomatico distributore = distributori.get(distributoreScelto);
//                        if (distributore.isInServizio()) {
//                            abbonamentoDAO.emettiAbbonamento(distributore, trattaDAO.findById(1L), tesseraDAO.findById(12L), tipoabbonamento);
//                        } else {
//                            System.out.println("Distributore fuori servizio!");
//                        }
//                    } else {
//                        System.out.println("Selezione non valida!");
//                    }
//                }


}


