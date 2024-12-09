package it.epicode.entity.parco_mezzi;

import it.epicode.entity.biglietteria.Tratta;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_ParcoMezzi", query = "SELECT a FROM ParcoMezzi a")
public abstract class ParcoMezzi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean stato; //in servizio o in manutenzione?

    @OneToOne
    private Tratta tratta;

    @Column(name = "tempo_in_servizio", nullable = false)
    private double tempoInServizio;

    @Column(name = "tempo_in_manutenzione", nullable = false)
    private double tempoInManutenzione;

    @Column(name = "numero_biglietti_vidimati", nullable = false)
    private int numeroBigliettiVidimati;

    @Column(name = "totale_tratte_effettuate", nullable = false)
    private int totaleTratteEffettuate;



}