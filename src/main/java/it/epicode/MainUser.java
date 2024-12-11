package it.epicode;

import it.epicode.dao.*;
import it.epicode.entity.biglietteria.*;
import it.epicode.entity.exceptions.TrattaException;
import it.epicode.entity.exceptions.VenditoreException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class MainUser {
    public static final Logger LOGGER = LoggerFactory.getLogger(MainUser.class);

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

    public static Vendita chooseVenditore(int numero) throws VenditoreException {
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
                throw new VenditoreException("Rivenditore non presente nella lista!");
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
                    throw new VenditoreException("Distributore fuori servizio!");
                }
            } else {
                throw new VenditoreException("Distributore non presente nella lista!");
            }
        } else {
            throw new VenditoreException("Per favore inserire 1 o 2 per scegliere il venditore!");
        }

        return venditore;
    }

    public static Tratta chooseTratta(int numero) throws TrattaException {
        Tratta tratta = null;
        List<Tratta> tratte = trattaDAO.findAll();
        if (numero >= 0 && numero < tratte.size()) {
            tratta = tratte.get(numero);
        } else {
            throw new TrattaException("Tratta non presente nella lista!");
        }
        return tratta;
    }

    public static void main(String[] args) {

        System.out.println("Benvenuto nel sistema di trasporto pubblico!");

        boolean esecuzione = true;

        while (esecuzione) {
            try {
                System.out.println("Buongiorno, 1- Inserisci numero di tessera, 2- Non ho la tessera");
                int scelta = scanner.nextInt();
                scanner.nextLine();
                Vendita venditore = null;
                Tratta tratta = null;

                switch (scelta) {
                    case 1:
                        System.out.println("Inserire numero di tessera!");
                        int numeroTessera = scanner.nextInt();
                        scanner.nextLine();
                        Tessera tessera = tesseraDAO.findUserByNumeroTessera(numeroTessera);
                        if (tesseraDAO.checkRuolo(tessera)) {
                            System.out.println("Buongiorno Amministratore: " + tessera.getUser().getNome() + " " + tessera.getUser().getCognome());


                        } else {
                            System.out.println("Dove vuoi andare?");
                            List<Tratta> tratte = trattaDAO.findAll();
                            System.out.println("Seleziona la tratta:");
                            for (int i = 0; i < tratte.size(); i++) {
                                System.out.println((i + 1) + ". Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
                            }
                            int trattaScelta = scanner.nextInt() - 1;
                            scanner.nextLine();
                            System.out.println("Perfetto! Pronto a partire da " + tratte.get(trattaScelta).getZonaPartenza() + " a " + tratte.get(trattaScelta).getZonaArrivo());
                            System.out.println("il viaggio durerà circa " + tratte.get(trattaScelta).getDurataEffettiva() + " minuti");

                         if(abbonamentoDAO.findAbbonamentoByTessera(tessera)) {
                             System.out.println("Hai già"); // DIREMO SE L'ABBONAMENTO é VALIDO O MENO!

                         }

                            System.out.println("1- per comprare un biglietto, 2- per comprare un abbonamento");
                            int titoloDiViaggio = scanner.nextInt();
                            scanner.nextLine();

                            switch (titoloDiViaggio) {
                                case 1:
                                    System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
                                    int acquistoBiglietto = scanner.nextInt();
                                    scanner.nextLine();
                                    venditore = chooseVenditore(acquistoBiglietto);
                                    tratta = chooseTratta(trattaScelta);
                                    if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                                        bigliettoDAO.emettiBiglietto(venditore, tratta);
                                    }
                                    break;

                                case 2:
                                    System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
                                    int acquistoAbbonamento = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.println("1- Settimanale, 2- Mensile");
                                    int tipoAbbonamento = scanner.nextInt();
                                    scanner.nextLine();
                                    venditore = chooseVenditore(acquistoAbbonamento);
                                    tratta = chooseTratta(trattaScelta);
                                    if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                                        abbonamentoDAO.emettiAbbonamento(venditore, tratta, tessera,tipoAbbonamento );
                                    }
                                default:

                            }
                            break;

                        }
                }


//
//                            System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
//                            int acquistoAbbonamento = scanner.nextInt();
//                            scanner.nextLine();
//
//                            venditore = chooseVenditore(acquistoAbbonamento);
//
//                            if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
//                                abbonamentoDAO.emettiAbbonamento(venditore, tratta, tessera, tipoAbbonamento);
//                            }
//                        }
//
//                        break;
//
//                    case 2:
//                        break;


//                List<Biglietto> listaBigliettiVenditore1 = venditaDAO.findBigliettiByVenditore(venditaDAO.findById(1L));
//                List<Biglietto> listaBigliettiVenditore2 = venditaDAO.findBigliettiByVenditore(venditaDAO.findById(2L));
//                List<Biglietto> listaBigliettiVenditore3 = venditaDAO.findBigliettiByVenditore(venditaDAO.findById(3L));
//
//                System.out.println("Biglietti venduti da Rivenditore1: " + listaBigliettiVenditore1.size());
//                System.out.println("Biglietti venduti da Distributore1: " + listaBigliettiVenditore2.size());


            } catch (VenditoreException | TrattaException e) {
                LOGGER.error(e::getMessage);
            }

            System.out.print("Hai bisogno di altro? (si/no): ");
            String risposta = scanner.nextLine().toLowerCase();
            if (risposta.equals("no")) {
                System.out.println("Grazie per aver scelto il trasporto pubblico!");
                esecuzione = false;
            } else if (!risposta.equals("si")) {
                System.out.println("Per favore digitare soltanto si o no!");
                break;
            }
        }
    }
}


