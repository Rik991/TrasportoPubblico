package it.epicode;

import it.epicode.dao.*;
import it.epicode.entity.biglietteria.*;
import it.epicode.entity.exceptions.TesseraNotFoundException;
import it.epicode.entity.exceptions.TicketNotFoundException;
import it.epicode.entity.exceptions.TrattaException;
import it.epicode.entity.exceptions.VenditoreException;
import it.epicode.entity.parco_mezzi.ParcoMezzi;
import it.epicode.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

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
            System.out.println("Seleziona il Rivenditore: ");
            for (int i = 0; i < rivenditori.size(); i++) {
                System.out.println((i + 1) + "- Rivenditore " + rivenditori.get(i).getNome());
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
                System.out.println((i + 1) + "- " + distributori.get(i).getNome());
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
                System.out.println("Buongiorno \n1- Inserisci numero di tessera \n2- Non ho la tessera");
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
                        Tessera tessera = null;
                        if (tesseraDAO.checkTessera(numeroTessera)) {
                            throw new TesseraNotFoundException("Tessera non trovata!");
                        } else if (tesseraDAO.findTessera(numeroTessera).getDataScadenza().isBefore(LocalDate.now())) {
                            tessera = tesseraDAO.findTessera(numeroTessera);
                            System.out.println("Tessera scaduta! Vuoi rinnovarla? si/no");
                            String sceltaRinnovoTessera = scanner.nextLine().toLowerCase();
                            switch (sceltaRinnovoTessera) {
                                case "si":
                                    tessera.setDataScadenza(tessera.getDataScadenza().plusYears(1));
                                    tesseraDAO.update(tessera);
                                    System.out.println("Tessera aggiornata con successo, nuova scadenza: " + tessera.getDataScadenza());
                                    System.out.println();
                                    break;
                                case "no":
                                    System.out.println("Grazie per aver scelto il trasporto pubblico,\nle ricordiamo che può comunque acquistare un biglietto per viaggiare senza tessera!");
                                    System.out.println();
                                    break;
                                default:
                                    throw new InputMismatchException("Per favore inserire soltanto si o no!");
                            }
                        }

                        tessera = tesseraDAO.findTessera(numeroTessera);
                        if (tesseraDAO.checkRuolo(tessera)) {
                            System.out.println("Buongiorno Amministratore: " + tessera.getUser().getNome() + " " + tessera.getUser().getCognome());

                            System.out.println("1- Gestisci tratte e linee \n2- Controllo Mezzi \n3- Statistiche vendite e vidimazione biglietti");

                            int sceltaAmministratore = scanner.nextInt();
                            scanner.nextLine();
                            switch (sceltaAmministratore) {
                                case 1:
                                    System.out.println("Cosa vuoi fare? \n1- Aggiorna stato servizio mezzo \n2- Dichiara ritardo \n3- Esci");
                                    int sceltaAmministratoreGestioneTratta = scanner.nextInt();
                                    switch (sceltaAmministratoreGestioneTratta) {
                                        case 1:
                                            List<ParcoMezzi> mezzi = parcoMezziDAO.findAll();
                                            System.out.println("Lista mezzi: ");
                                            for (int i = 0; i < parcoMezziDAO.findAll().size(); i++) {
                                                System.out.println((i + 1) + " " + mezzi.get(i).getLinea());
                                            }
                                            System.out.println("Scegli il mezzo da controllare");
                                            Long sceltaMezzo = scanner.nextLong();
                                            scanner.nextLine();
                                            ParcoMezzi mezzoScelto = parcoMezziDAO.findById(sceltaMezzo);
                                            if (mezzoScelto.isInServizio()) {
                                                System.out.println("Il mezzo scelto è in servizio!, vuoi metterlo in manutenzione? si/no");
                                                String checkServizio = scanner.nextLine().toLowerCase();
                                                if (checkServizio.equals("si")) {
                                                    mezzoScelto.setInServizio(false);
                                                    mezzoScelto.setTempoInManutenzione(LocalTime.of(3, 0));
                                                    parcoMezziDAO.update(mezzoScelto);
                                                    System.out.println("Mezzo messo in manutenzione con successo!");
                                                } else if (!checkServizio.equals("no")) {
                                                    throw new InputMismatchException("Errore d'inserimento, per favore digitare si o no!");
                                                }
                                                break;
                                            } else {
                                                System.out.println("Il mezzo scelto è in manutenzione!, vuoi rimetterlo in servizio? si/no");
                                                String checkServizio = scanner.nextLine().toLowerCase();
                                                if (checkServizio.equals("si")) {
                                                    mezzoScelto.setInServizio(true);
                                                    mezzoScelto.setTempoInServizio(LocalTime.of(12, 0));
                                                    parcoMezziDAO.update(mezzoScelto);
                                                    System.out.println("Mezzo rimesso in servizio con successo!");
                                                } else if (!checkServizio.equals("no")) {
                                                    throw new InputMismatchException("Errore d'inserimento, per favore digitare si o no!");
                                                }
                                                break;
                                            }
                                        case 2:
                                            List<ParcoMezzi> mezziInViaggio = parcoMezziDAO.findAll();
                                            System.out.println("Su quale mezzo stai viaggiando? \n1- Autobus \n2- Tram?");
                                            int tipoMezzoScelto = scanner.nextInt();
                                            scanner.nextLine();
                                            if (tipoMezzoScelto == 1) {
                                                parcoMezziDAO.isAutobusOrTram(mezziInViaggio, tipoMezzoScelto);
                                            } else if (tipoMezzoScelto == 2) {
                                                parcoMezziDAO.isAutobusOrTram(mezziInViaggio, tipoMezzoScelto);
                                            } else {
                                                throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                                            }
                                            Long LineaScelta = scanner.nextLong();
                                            scanner.nextLine();
                                            mezzoScelto = parcoMezziDAO.findById(LineaScelta);
                                            System.out.println("Linea selezionata: " + mezzoScelto.getLinea());
                                            System.out.println("Durata: " + mezzoScelto.getTratta().getDurataEffettiva()
                                                    + "\nArrivo previsto alle: " + mezzoScelto.getTratta().getOraDiArrivo());
                                            System.out.println("C'è traffico? si/no");
                                            String traffico = scanner.nextLine();
                                            switch (traffico) {
                                                case "si":
                                                    System.out.println("Inserisci i minuti di ritardo: ");
                                                    int ritardo = scanner.nextInt();
                                                    scanner.nextLine();
                                                    mezzoScelto.viaggia(mezzoScelto, ritardo);
                                                    System.out.println("Durata effettiva: " + mezzoScelto.getTratta().getDurataEffettiva().plusMinutes(ritardo)
                                                            + " \nArrivo previsto alle: " + mezzoScelto.getTratta().getOraDiArrivo().plusMinutes(ritardo));
                                                    System.out.println("Segnalazione avvenuta correttamente.");
                                                    System.out.println();
                                                    continue;
                                                case "no":
                                                    System.out.println("Grazie, buon lavoro!");
                                                    continue;

                                            }
                                        case 3:
                                            System.out.println("Arrivederci e buon lavoro!");
                                            return;
                                        default:
                                            throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                                    }
                                    break;
                                case 2:
                                    List<ParcoMezzi> mezzi = parcoMezziDAO.findAll();
                                    System.out.println("Lista mezzi: ");
                                    for (int i = 0; i < parcoMezziDAO.findAll().size(); i++) {
                                        System.out.println((i + 1) + " " + mezzi.get(i).getLinea());
                                    }
                                    System.out.println("Scegli il mezzo da controllare");
                                    Long sceltaMezzo = scanner.nextLong();
                                    scanner.nextLine();
                                    ParcoMezzi mezzoScelto = parcoMezziDAO.findById(sceltaMezzo);
                                    if (mezzoScelto.isInServizio()) {
                                        System.out.println("Il mezzo scelto è in servizio!");
                                    } else {
                                        System.out.println("Il mezzo scelto è in manutenzione!");
                                    }
                                    System.out.println(mezzoScelto.getLinea() + " Tempo in servizio: " + mezzoScelto.getTempoInServizio() + " h" + "\nTempo in manutenzione: " + mezzoScelto.getTempoInManutenzione() + " h");
                                    break;
                                case 3:
                                    System.out.println("Contralla statistiche vendite!");
                                    System.out.println("1- Totale biglietti/abbonamenti venduti da un venditore \n2- Abbonamenti venduti in un periodo di tempo da un venditore" +
                                            "\n3- Totale biglietti vidimati su un mezzo \n4- Totale biglietti vidimati in un periodo di tempo");
                                    int sceltaStatistiche = scanner.nextInt();
                                    scanner.nextLine();
                                    switch (sceltaStatistiche) {
                                        case 1:
                                            List<Vendita> venditori = venditaDAO.findAll();
                                            System.out.println("Lista venditori");
                                            for (int i = 0; i < venditori.size(); i++) {
                                                System.out.println(venditori.get(i).getId() + " " + venditori.get(i).getNome());
                                            }
                                            System.out.println("Scegli il venditore da controllare");
                                            Long sceltaVenditore = scanner.nextLong();
                                            scanner.nextLine();
                                            Vendita venditoreScelto = venditaDAO.findById(sceltaVenditore);
                                            List<Biglietto> listaBigliettiVenditore = venditaDAO.findBigliettiByVenditore(venditoreScelto);
                                            List<Abbonamento> listaAbbonamentiVenditore = venditaDAO.findAbbonamentiByVenditore(venditoreScelto);
                                            System.out.println(venditoreScelto.getNome());
                                            System.out.println("Biglietti venduti: " + listaBigliettiVenditore.size());
                                            System.out.println("Abbonamenti venduti: " + listaAbbonamentiVenditore.size());
                                            continue;
                                        case 2:
                                            venditori = venditaDAO.findAll();
                                            System.out.println("Lista venditori");
                                            for (int i = 0; i < venditori.size(); i++) {
                                                System.out.println(venditori.get(i).getId() + " " + venditori.get(i).getNome());
                                            }
                                            System.out.println("Scegli il venditore da controllare");
                                            sceltaVenditore = scanner.nextLong();
                                            scanner.nextLine();
                                            venditoreScelto = venditaDAO.findById(sceltaVenditore);
                                            System.out.println("Inserisci il periodo da controllare: YYYY-MM-DD");
                                            System.out.println("Inserisci la data di inizio: ");
                                            LocalDate dataInizio = LocalDate.parse(scanner.nextLine());
                                            System.out.println("Inserisci la data di fine: ");
                                            LocalDate dataFine = LocalDate.parse(scanner.nextLine());
                                            List<Abbonamento> abbonamentiVendutiTra = venditaDAO.findAbbonamentiByDate(dataInizio, dataFine, venditoreScelto);
                                            System.out.println("Abbonamenti venduti tra " + dataInizio + " e " + dataFine + " : " + abbonamentiVendutiTra.size());
                                            continue;
                                        case 3://Totale biglietti vidimati su un mezzo
                                            mezzi = parcoMezziDAO.findAll();
                                            System.out.println("Lista mezzi: ");
                                            for (int i = 0; i < parcoMezziDAO.findAll().size(); i++) {
                                                System.out.println((i + 1) + " " + mezzi.get(i).getLinea());
                                            }
                                            System.out.println("Scegli il mezzo da controllare");
                                            sceltaMezzo = scanner.nextLong();
                                            scanner.nextLine();
                                            mezzoScelto = parcoMezziDAO.findById(sceltaMezzo);
                                            System.out.println("Totale biglietti vidimati sul mezzo " + mezzoScelto.getLinea() + ": " + mezzoScelto.getTratta().getNumeroBigliettiVidimati());
                                            continue;
                                        case 4://Totale biglietti vidimati in un periodo di tempo
                                            System.out.println("Inserisci il periodo da controllare: YYYY-MM-DD");
                                            System.out.println("Inserisci la data di inizio: ");
                                            dataInizio = LocalDate.parse(scanner.nextLine());
                                            System.out.println("Inserisci la data di fine: ");
                                            dataFine = LocalDate.parse(scanner.nextLine());
                                            List<Biglietto> listaTotaleBigliettiVidimati = bigliettoDAO.findBigliettiVidimatiByDate(dataInizio, dataFine);
                                            System.out.println("Totale biglietti vidimati nel periodo inserito: " + listaTotaleBigliettiVidimati.size());
                                            continue;
                                        default:
                                            throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                                    }

                                default:
                                    throw new InputMismatchException("Errore d'inserimento, per favore digitare 1 o 2!");
                            }


                        } else {
                            System.out.println("Buongiorno " + tessera.getUser().getNome() + " " + tessera.getUser().getCognome());
                            System.out.println("Dove vuoi andare?");

                            System.out.println("Seleziona la tratta: ");
                            for (int i = 0; i < tratte.size(); i++) {
                                System.out.println(tratte.get(i).getId() + "- Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
                            }
                            int trattaScelta = scanner.nextInt() - 1;
                            scanner.nextLine();
                            System.out.println("Hai selezionato la tratta: " + tratte.get(trattaScelta).getZonaPartenza() + " a " + tratte.get(trattaScelta).getZonaArrivo());
                            System.out.println("il viaggio durerà circa: " + tratte.get(trattaScelta).getDurataEffettiva() + " minuti");


                            if (tessera.getDataScadenza().isBefore(LocalDate.now())) {
                                System.out.println("Devi prima rinnovare la tua tessera!");
                                break;
                            }

                            if (abbonamentoDAO.checkAbbonamento(tessera)) {
                                if (abbonamentoDAO.findAbbonamentoByTessera(tessera).getDataScadenza().isBefore(LocalDate.now())) {
                                    System.out.println("Il tuo abbonamento è scaduto");

                                    System.out.println("Vuoi rinnovare il tuo abbonamento? si/no");
                                    String risposta = scanner.nextLine().toLowerCase();
                                    if (risposta.equals("no")) {
                                        System.out.println("Grazie per aver scelto il trasporto pubblico!");
                                        return;
                                    } else if (risposta.equals("si")) {
                                        System.out.println("1- Settimanale \n2- Mensile");
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
                                    if (trattaDAO.findMezzoByTratta(tratte.get(trattaScelta)).isInServizio()) {
                                        System.out.println("Hai già un abbonamento valido! Buon Viaggio");
                                        return;
                                    }
                                    System.out.println("Ci scusiamo per l'inconveniente, il mezzo non è in servizio!");
                                    return;
                                }
                            }

                            System.out.println("1- Ho già un biglietto \n2- Acquista un biglietto \n3- Acquista un abbonamento");
                            int titoloDiViaggio = scanner.nextInt();
                            scanner.nextLine();

                            switch (titoloDiViaggio) {
                                case 1:
                                    User user = tessera.getUser();
                                    List<Biglietto> bigliettiUtente = user.getBiglietti().stream()
                                            .filter(biglietto -> !biglietto.isVidimato())
                                            .filter(biglietto -> biglietto.getTratta().equals(trattaDAO.findById((long) trattaScelta + 1))).toList();
                                    if (bigliettiUtente.size() <= 0) {
                                        throw new TicketNotFoundException("Nessun biglietto disponibile!");
                                    }
                                    System.out.println("I tuoi biglietti: ");
                                    for (int i = 0; i < bigliettiUtente.size(); i++) {
                                        System.out.println(bigliettiUtente.get(i).getId() + " " + bigliettiUtente.get(i).getTratta().getZonaPartenza() + " - " + bigliettiUtente.get(i).getTratta().getZonaArrivo());
                                    }
                                    System.out.println("Quale Biglietto vuoi usare?");
                                    Long sceltaBiglietto = scanner.nextLong();
                                    scanner.nextLine();
                                    Biglietto bigliettoScelto = bigliettoDAO.findById(sceltaBiglietto);
                                    if (trattaDAO.findMezzoByTratta(bigliettoScelto.getTratta()).isInServizio()) {
                                        bigliettoDAO.vidimaBiglietto(bigliettoScelto, bigliettoScelto.getTratta());

                                        System.out.println("Biglietto vidimato con successo, buon viaggio!");
                                    } else {
                                        System.out.println("Ci scusiamo per l'inconveniente, il mezzo non è in servizio!");
                                    }
                                    break;

                                case 2:
                                    user = tessera.getUser();

                                    System.out.println("Dove vuoi acquistarlo? \n1- Per rivenditore \n2- Per distributore");
                                    int acquistoBiglietto = scanner.nextInt();
                                    scanner.nextLine();

                                    venditore = chooseVenditore(acquistoBiglietto);
                                    tratta = chooseTratta(trattaScelta);
                                    if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                                        bigliettoDAO.emettiBiglietto(venditore, tratta, user);
                                        System.out.println("Biglietto acquistato con successo!");

                                        System.out.println("Vuoi partire adesso? si/no");
                                        String partenza = scanner.nextLine().toLowerCase();
                                        if (partenza.equals("si")) {
                                            if (trattaDAO.findMezzoByTratta(tratta).isInServizio()) {
                                                bigliettoDAO.vidimaBiglietto(user.getBiglietti().getLast(), user.getBiglietti().getLast().getTratta());
                                                System.out.println("Biglietto vidimato con successo, buon viaggio!");
                                            } else {
                                                System.out.println("Ci scusiamo per l'inconveniente, il mezzo non è in servizio!");
                                            }
                                            return;
                                        } else if (partenza.equals("no")) {
                                            System.out.println("Grazie per aver scelto il trasporto pubblico!");
                                            break;
                                        } else {
                                            throw new InputMismatchException("Errore d'inserimento, per favore digitare si o no!");
                                        }
                                    }
                                    break;

                                case 3:
                                    System.out.println("Dove vuoi acquistarlo? \n1- Per rivenditore \n2- Per distributore");
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
                        System.out.println("1- Ho già un Biglietto \n2- Acquista un Biglietto \n3- Fai una tessera");
                        scelta = scanner.nextInt();
                        scanner.nextLine();
                        switch (scelta) {
                            case 1:
                                System.out.println("Inserisci il tuo userID per completare l'acquisto");
                                Long userID = scanner.nextLong();
                                scanner.nextLine();
                                User user = userDAO.findById(userID);
                                List<Biglietto> bigliettiUtente = user.getBiglietti().stream()
                                        .filter(biglietto -> !biglietto.isVidimato()).toList();
                                if (bigliettiUtente.size() <= 0) {
                                    throw new TicketNotFoundException("Nessun biglietto disponibile!");

                                }
                                System.out.println("I tuoi biglietti: ");
                                for (int i = 0; i < bigliettiUtente.size(); i++) {
                                    System.out.println(bigliettiUtente.get(i).getId() + " " + bigliettiUtente.get(i).getTratta().getZonaPartenza() + " - " + bigliettiUtente.get(i).getTratta().getZonaArrivo());
                                }
                                System.out.println("Quale Biglietto vuoi usare?");
                                Long sceltaBiglietto = scanner.nextLong();
                                scanner.nextLine();
                                Biglietto bigliettoScelto = bigliettoDAO.findById(sceltaBiglietto);
                                if (trattaDAO.findMezzoByTratta(bigliettoScelto.getTratta()).isInServizio()) {
                                    bigliettoDAO.vidimaBiglietto(bigliettoScelto, bigliettoScelto.getTratta());
                                    System.out.println("Biglietto vidimato con successo, buon viaggio!");
                                } else {
                                    System.out.println("Ci scusiamo per l'inconveniente, il mezzo non è in servizio!");
                                }
                                return;
                            case 2:
                                System.out.println("Inserisci il tuo userID per completare l'acquisto");
                                userID = scanner.nextLong();
                                scanner.nextLine();
                                user = userDAO.findById(userID);

                                System.out.println("Dove vuoi andare?");
                                tratte = trattaDAO.findAll();//forse superflua
                                System.out.println("Seleziona la tratta: ");
                                for (int i = 0; i < tratte.size(); i++) {
                                    System.out.println(tratte.get(i).getId() + "- Da " + tratte.get(i).getZonaPartenza() + " a " + tratte.get(i).getZonaArrivo());
                                }
                                int trattaScelta = scanner.nextInt() - 1;
                                scanner.nextLine();
                                System.out.println("Hai selezionato la tratta: " + tratte.get(trattaScelta).getZonaPartenza() + " a " + tratte.get(trattaScelta).getZonaArrivo());
                                System.out.println("il viaggio durerà circa: " + tratte.get(trattaScelta).getDurataEffettiva() + " min");

                                System.out.println("Dove vuoi acquistarlo? \n1- Per rivenditore \n2- Per distributore");
                                int acquistoBiglietto = scanner.nextInt();
                                scanner.nextLine();

                                venditore = chooseVenditore(acquistoBiglietto);
                                tratta = chooseTratta(trattaScelta);
                                if ((venditore instanceof DistributoreAutomatico && ((DistributoreAutomatico) venditore).isInServizio()) || venditore instanceof Rivenditore) {
                                    bigliettoDAO.emettiBiglietto(venditore, tratta, user);
                                    System.out.println("Biglietto acquistato con successo!");
                                    System.out.println("Vuoi partire adesso? si/no");
                                    String partenza = scanner.nextLine().toLowerCase();
                                    if (partenza.equals("si")) {
                                        if (trattaDAO.findMezzoByTratta(tratta).isInServizio()) {
                                            bigliettoDAO.vidimaBiglietto(user.getBiglietti().getLast(), user.getBiglietti().getLast().getTratta());
                                            System.out.println("Biglietto vidimato con successo, buon viaggio!");
                                        } else {
                                            System.out.println("Ci scusiamo per l'inconveniente, il mezzo non è in servizio!");
                                        }
                                        return;
                                    } else if (partenza.equals("no")) {
                                        System.out.println("Grazie per aver scelto il trasporto pubblico!");
                                        break;
                                    } else {
                                        throw new InputMismatchException("Errore d'inserimento, per favore digitare si o no!");
                                    }
                                }
                                break;
                            case 3:
                                System.out.println("Inserisci il tuo userID per completare l'acquisto");
                                Long userId = scanner.nextLong();
                                scanner.nextLine();
                                User userWithoutT = userDAO.findById(userId);

                                System.out.println("Dove vuoi acquistarla? \n1- Per rivenditore \n2- Per distributore");
                                int acquistoTessera = scanner.nextInt();
                                scanner.nextLine();

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


            } catch
            (VenditoreException | TrattaException | TesseraNotFoundException | TicketNotFoundException |
             InputMismatchException |
             IllegalArgumentException e) {
                LOGGER.error(e::getMessage);
            }

            System.out.print("Hai bisogno di altro? si/no: ");
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


