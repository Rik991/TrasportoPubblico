package it.epicode.entity.biglietteria;

import it.epicode.entity.parco_mezzi.ParcoMezzi;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "tratte")
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
    private LocalTime oraDiPartenza;

    @Column(name = "ora_di_arrivo", nullable = false)
    private LocalTime oraDiArrivo;

    @Column(name = "durata_effettiva")
    private LocalTime durataEffettiva;

    @OneToMany(mappedBy = "tratta")
    private List<Biglietto> biglietti;

    @OneToMany(mappedBy = "tratta")
    private List<Abbonamento> abbonamenti;

}