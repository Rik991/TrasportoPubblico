package it.epicode;

import it.epicode.dao.*;
import it.epicode.entity.biglietteria.*;
import it.epicode.entity.exceptions.TesseraNotFoundException;
import it.epicode.entity.exceptions.TrattaException;
import it.epicode.entity.exceptions.VenditoreException;
import it.epicode.entity.parco_mezzi.Autobus;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import it.epicode.entity.parco_mezzi.Tram;
import it.epicode.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
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
                List<Tratta> tratte = trattaDAO.findAll();

                switch (scelta) {
                    case 1:
                        System.out.println("Inserire numero di tessera!");
                        int numeroTessera = scanner.nextInt();
                        scanner.nextLine();
                        if (tesseraDAO.checkTessera(numeroTessera)) {
                            throw new TesseraNotFoundException("Tessera non trovata!");
                        }

                        Tessera tessera = tesseraDAO.findTessera(numeroTessera);
                        if (tesseraDAO.checkRuolo(tessera)) {
                            System.out.println("Buongiorno Amministratore: " + tessera.getUser().getNome() + " " + tessera.getUser().getCognome());

                            System.out.println("1- Gestisci tratte e linee \n2-Controllo Mezzi \n3-Statistiche venditori");

                            int sceltaAmministratore = scanner.nextInt();
                            scanner.nextLine();
                            switch (sceltaAmministratore) { //switch admin gestione tratte
                                case 1:
                                    System.out.println("Cosa vuoi fare? 1. Modifica linea 2. Elimina linea 3. Dichiara ritardo(in Viaggio) 4. Esci");
                                    int sceltaAmministratoreGestioneTratta = scanner.nextInt();
                                    switch (sceltaAmministratoreGestioneTratta) {
                                        case 1:
                                            System.out.println("1. Autobus o 2. Tram?");
                                            int tipoMezzo = scanner.nextInt();
                                            System.out.println("In servizio? (Inserisci true o false)");
                                            boolean inServizio = scanner.nextBoolean();
                                            scanner.nextLine();
                                            if (!inServizio) {
                                                if (tipoMezzo == 1) {
                                                    Autobus nuovoAutobus = new Autobus();
                                                    nuovoAutobus.setInServizio(false);
                                                    nuovoAutobus.setTempoInManutenzione(LocalTime.now());
                                                    nuovoAutobus.setTempoInServizio(LocalTime.MIN);
                                                    parcoMezziDAO.save(nuovoAutobus);
                                                    System.out.println("Il mezzo è in manutenzione. Tempo di manutenzione: " + nuovoAutobus.getTempoInManutenzione());
                                                } else if (tipoMezzo == 2) {
                                                    Tram nuovoTram = new Tram();
                                                    nuovoTram.setInServizio(false);
                                                    nuovoTram.setTempoInManutenzione(LocalTime.now());
                                                    nuovoTram.setTempoInServizio(LocalTime.MIN);
                                                    parcoMezziDAO.save(nuovoTram);
                                                    System.out.println("Il mezzo è in manutenzione. Tempo di manutenzione: " + nuovoTram.getTempoInManutenzione());
                                                }
                                                break;
                                            }
                                            ;
                                            System.out.println("Inserisci il linea");
                                            String linea = scanner.nextLine();
                                            scanner.nextLine();

                                            System.out.println("Inserisci la zona di partenza");
                                            String zonaPartenza = scanner.nextLine();
                                            System.out.println("Inserisci la zona di arrivo");
                                            String zonaArrivo = scanner.nextLine();
                                            System.out.println("Inserisci l'ora di partenza (hh:mm)");
                                            LocalTime oraPartenza = LocalTime.parse(scanner.nextLine());
                                            System.out.println("Inserisci l'ora di arrivo (hh:mm)");
                                            LocalTime oraArrivo = LocalTime.parse(scanner.nextLine());

                                            if (tipoMezzo == 1) {
                                                parcoMezziDAO.createBus(linea, inServizio, zonaPartenza, zonaArrivo, oraPartenza, oraArrivo);
                                                System.out.println("Autobus creato con successo");
                                            } else if (tipoMezzo == 2) {
                                                parcoMezziDAO.createTram(linea, inServizio, zonaPartenza, zonaArrivo, oraPartenza, oraArrivo);
                                                System.out.println("Tram creato con successo");
                                            } else {
                                                System.out.println("Opzione non valida!");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Che tratta vuoi eliminare?");
                                            List<Tratta> trattaDaEliminare = trattaDAO.findAll();
                                            System.out.println("Seleziona la tratta:");
                                            for (int i = 0; i < tratte.size(); i++) {
                                                System.out.println((i + 1) + ". Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
                                            }
                                            int chooseTratta = scanner.nextInt() - 1;
                                            scanner.nextLine();
                                            if (chooseTratta >= 0 && chooseTratta < tratte.size()) {
                                                Tratta trattaDaElinimare = tratte.get(chooseTratta);
                                                trattaDAO.delete(trattaDaElinimare);
                                                System.out.println("Tratta eliminata con successo.");
                                            } else {
                                                System.out.println("Selezione non valida.");
                                            }
                                            break;
                                        case 3:
                                            List<ParcoMezzi> mezziInViaggio = parcoMezziDAO.findAll();
                                            System.out.println("Su quale mezzo stai viaggiando? 1. Autobus o 2. Tram?");
                                            int tipoMezzoScelto = scanner.nextInt();
                                            scanner.nextLine();
                                            if (tipoMezzoScelto == 1) {//autobus
                                                parcoMezziDAO.isAutobusOrTram(mezziInViaggio, tipoMezzoScelto);
                                            } else if (tipoMezzoScelto == 2) {//tram
                                                parcoMezziDAO.isAutobusOrTram(mezziInViaggio, tipoMezzoScelto);
                                            } else {
                                                throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                                            }
                                            Long LineaScelta = scanner.nextLong();
                                            scanner.nextLine();
                                            ParcoMezzi mezzoScelto = parcoMezziDAO.findById(LineaScelta);
                                            System.out.println("Linea selezionata: " + mezzoScelto.getLinea());
                                            System.out.println("Durata prevista: " + mezzoScelto.getTratta().getDurataEffettiva()
                                                    + "\nArrivo previsto alle: " + mezzoScelto.getTratta().getOraDiArrivo());
                                            System.out.println("C'è traffico? Inserisci true o false");
                                            boolean traffico = scanner.nextBoolean();
                                            if (traffico) {
                                                System.out.println("Inserisci i minuti di ritardo: ");
                                                int ritardo = scanner.nextInt();
                                                scanner.nextLine();
                                                mezzoScelto.viaggia(mezzoScelto, ritardo);
                                                System.out.println("Durata effettiva: " + mezzoScelto.getTratta().getDurataEffettiva().plusMinutes(ritardo)
                                                        + " \nArrivo previsto alle: " + mezzoScelto.getTratta().getOraDiArrivo().plusMinutes(ritardo));
                                                System.out.println("Segnalazione avvenuta correttamente.");
                                            }
                                        case 4:
                                            System.out.println("Arrivederci e buon lavoro!");
                                            return;
                                        default:
                                            throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                                    }
                                    //switch principale (amministratore)
                                    break;
                                case 2:

                                    break;
                                case 3://statistiche venditori
                                    //                List<Biglietto> listaBigliettiVenditore1 = venditaDAO.findBigliettiByVenditore(venditaDAO.findById(1L));
//                List<Biglietto> listaBigliettiVenditore2 = venditaDAO.findBigliettiByVenditore(venditaDAO.findById(2L));
//                List<Biglietto> listaBigliettiVenditore3 = venditaDAO.findBigliettiByVenditore(venditaDAO.findById(3L));//
//                System.out.println("Biglietti venduti da Rivenditore1: " + listaBigliettiVenditore1.size());
//                System.out.println("Biglietti venduti da Distributore1: " + listaBigliettiVenditore2.size());

                                    break;
                                default:
                                    throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                            }


                        } else {
                            System.out.println("Buongiorno " + tessera.getUser().getNome() + " " + tessera.getUser().getCognome());
                            System.out.println("Dove vuoi andare?");

                            System.out.println("Seleziona la tratta:");
                            for (int i = 0; i < tratte.size(); i++) {
                                System.out.println((i + 1) + ". Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
                            }
                            int trattaScelta = scanner.nextInt() - 1;
                            scanner.nextLine();
                            System.out.println("Hai selezionato la tratta: " + tratte.get(trattaScelta).getZonaPartenza() + " a " + tratte.get(trattaScelta).getZonaArrivo());
                            System.out.println("il viaggio durerà circa " + tratte.get(trattaScelta).getDurataEffettiva() + " minuti");

                            if (abbonamentoDAO.checkAbbonamento(tessera)) {
                                if (abbonamentoDAO.findAbbonamentoByTessera(tessera).getDataScadenza().isBefore(LocalDate.now())) {
                                    System.out.println("Il tuo abbonamento è scaduto");

                                    System.out.println("Vuoi rinnovare il tuo abbonamento? si/no");
                                    String risposta = scanner.nextLine().toLowerCase();
                                    if (risposta.equals("no")) {
                                        System.out.println("Grazie per aver scelto il trasporto pubblico!");
                                        return;
                                    } else if (risposta.equals("si")) {
                                        System.out.println("1- Settimanale, 2- Mensile");
                                        int tipoAbbonamento = scanner.nextInt();
                                        scanner.nextLine();
                                        Abbonamento abbonamentoAggiornato = abbonamentoDAO.findAbbonamentoByTessera(tessera);
                                        if (tipoAbbonamento == 1) {
                                            abbonamentoAggiornato.setDataScadenza(LocalDate.now().plusDays(7));
                                            abbonamentoDAO.update(abbonamentoAggiornato);
                                            System.out.println("Abbonamento rinnovato fino al: " + abbonamentoAggiornato.getDataScadenza());
                                        } else if (tipoAbbonamento == 2) {
                                            abbonamentoAggiornato.setDataScadenza(LocalDate.now().plusDays(30));
                                            abbonamentoDAO.update(abbonamentoAggiornato);
                                            System.out.println("Abbonamento rinnovato fino al: " + abbonamentoAggiornato.getDataScadenza());
                                        } else {
                                            throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                                        }
                                        break;
                                    } else {
                                        throw new InputMismatchException("Errore d'inserimento, digita si o no!");
                                    }
                                } else {
                                    System.out.println("Hai già un abbonamento valido! Buon Viaggio");
                                    return;
                                }
                            }

                            System.out.println("1- per comprare un biglietto, 2- per comprare un abbonamento");
                            int titoloDiViaggio = scanner.nextInt();
                            scanner.nextLine();

                            switch (titoloDiViaggio) {
                                case 1:
                                    System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
                                    int acquistoBiglietto = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.println("Inserisci il tuo userID per completare l'acquisto");
                                    Long userID = scanner.nextLong();
                                    scanner.nextLine();
                                    User user = userDAO.findById(userID);
                                    venditore = chooseVenditore(acquistoBiglietto);
                                    tratta = chooseTratta(trattaScelta);
                                    if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                                        bigliettoDAO.emettiBiglietto(venditore, tratta, user);
                                        System.out.println("Biglietto acquistato con successo!");

                                        System.out.println("Vuoi partire adesso? (si/no)");
                                        String partenza = scanner.nextLine().toLowerCase();
                                        if (partenza.equals("si")) {
                                            bigliettoDAO.vidimaBiglietto(user.getBiglietti().getLast(), tratta);
                                            System.out.println("Biglietto vidimato con successo. \nBuon viaggio!");
                                        } else if (partenza.equals("no")) {
                                            System.out.println("Grazie per aver scelto il trasporto pubblico!");
                                            return;
                                        } else {
                                            throw new InputMismatchException("Errore d'inserimento, per favore digitare si o no!");
                                        }
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
                                        abbonamentoDAO.emettiAbbonamento(venditore, tratta, tessera, tipoAbbonamento);
                                        System.out.println("Abbonamento acquistato con successo!");
                                    }
                                    break;

                                default:
                                    throw new VenditoreException("Venditore non presente nella lista!");
                            }
                        }
                        break;
                    case 2:
                        System.out.println("1- Acquista biglietto, 2- Fai una tessera");
                        scelta = scanner.nextInt();
                        scanner.nextLine();
                        switch (scelta) {
                            case 1:
                                System.out.println("Dove vuoi andare?");
                                tratte = trattaDAO.findAll();//forse superflua
                                System.out.println("Seleziona la tratta:");
                                for (int i = 0; i < tratte.size(); i++) {
                                    System.out.println((i + 1) + ". Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
                                }
                                int trattaScelta = scanner.nextInt() - 1;
                                scanner.nextLine();
                                System.out.println("Hai selezionato la tratta: " + tratte.get(trattaScelta).getZonaPartenza() + " a " + tratte.get(trattaScelta).getZonaArrivo());
                                System.out.println("il viaggio durerà circa " + tratte.get(trattaScelta).getDurataEffettiva() + " minuti");

                                System.out.println("Dove vuoi acquistarlo? 1 per rivenditore, 2 per distributore");
                                int acquistoBiglietto = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Inserisci il tuo userID per completare l'acquisto");
                                Long userID = scanner.nextLong();
                                scanner.nextLine();
                                User user = userDAO.findById(userID);
                                venditore = chooseVenditore(acquistoBiglietto);
                                tratta = chooseTratta(trattaScelta);
                                if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                                    bigliettoDAO.emettiBiglietto(venditore, tratta, user);
                                    System.out.println("Biglietto acquistato con successo!");
                                    System.out.println("Vuoi partire adesso? (si/no)");
                                    String partenza = scanner.nextLine().toLowerCase();
                                    if (partenza.equals("si")) {
                                        bigliettoDAO.vidimaBiglietto(user.getBiglietti().getLast(), tratta);
                                        System.out.println("Biglietto vidimato con successo. \nBuon viaggio!");
                                    } else if (partenza.equals("no")) {
                                        System.out.println("Grazie per aver scelto il trasporto pubblico!");
                                        return;
                                    } else {
                                        throw new InputMismatchException("Errore d'inserimento, per favore digitare si o no!");
                                    }


                                }
                                break;
                            case 2:
                                System.out.println("Dove vuoi acquistarla? 1 per rivenditore, 2 per distributore");
                                int acquistoTessera = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Inserisci il tuo userID per completare l'acquisto");
                                Long userId = scanner.nextLong();
                                scanner.nextLine();
                                User userWithoutT = userDAO.findById(userId);
                                venditore = chooseVenditore(acquistoTessera);
                                Tessera nuovaTessera = tesseraDAO.emettiTessera(venditore, userWithoutT);
                                System.out.println("Grazie, " + userWithoutT.getNome() + " " + userWithoutT.getCognome() + "\nLa tua nuova tessera ha numero: " + nuovaTessera.getNumeroTessera());
                                break;
                            default:
                                throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                        }

                        break;
                    default:
                        throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");

                }
                //nella home page, l'utente può decidere di partire subito (check sul biglietto)
                //chiedere se ha già il biglietto (quindi mostrarli, ricorda che il biglietto è legato alla tratta!)



            } catch (VenditoreException | TrattaException | TesseraNotFoundException | InputMismatchException | IllegalArgumentException e) {
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


