package it.epicode.entity.biglietteria;

import it.epicode.entity.parco_mezzi.ParcoMezzi;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Tratta", query = "SELECT a FROM Tratta a")
public class Tratta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zona_di_partenza", nullable = false)
    private String zonaPartenza;

    @Column(name = "zona_di_arrivo", nullable = false)
    private String zonaArrivo;

    @Column(name = "ora_di_partenza", nullable = false)
    private LocalDateTime oraDiPartenza;

    @Column(name = "ora_di_arrivo", nullable = false)
    private LocalDateTime oraDiArrivo;
    //LocalDateTime partenzaDateTime = partenza;
    //LocalDateTime arrivoDateTime = arrivo;
    //Duration durata = Duration.between(partenzaDateTime, arrivoDateTime);
    //this.durata = durata.toMillis();
    //In questo modo, la durata della tratta sarà memorizzata in millisecondi, il che consente un calcolo preciso e semplifica la logica di programmazione.
    //Quando è necessario visualizzare la durata di una tratta in un formato leggibile per gli utenti (ad esempio, ore e minuti), è possibile utilizzare una semplice conversione:
    //javaCopylong durataMillis = tratta.getDurata();
    //long ore = durataMillis / (1000 * 60 * 60);
    //long minuti = (durataMillis % (1000 * 60 * 60)) / (1000 * 60);

    @Column(name = "durata_effettiva")
    private double durataEffettiva;

}