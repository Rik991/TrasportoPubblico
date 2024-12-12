package it.epicode.entity.parco_mezzi;

import it.epicode.entity.biglietteria.Tratta;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_ParcoMezzi", query = "SELECT a FROM ParcoMezzi a")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ParcoMezzi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean inServizio; //in servizio o in manutenzione?

    @Column(nullable = false)
    private String linea;

    @OneToOne
    private Tratta tratta;

    @Column(name = "tempo_in_servizio", nullable = false)
    private LocalTime tempoInServizio;

    @Column(name = "tempo_in_manutenzione", nullable = false)
    private LocalTime tempoInManutenzione;

    @Column(name = "numero_biglietti_vidimati", nullable = false)
    private int numeroBigliettiVidimati = 0;

    @Column(name = "totale_tratte_effettuate", nullable = false)
    private int totaleTratteEffettuate = 0;

    public abstract void viaggia(ParcoMezzi mezzo, int traffico);


}