package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "abbonamenti")
@NamedQuery(name = "Trova_tutto_Abbonamento", query = "SELECT a FROM Abbonamento a")
public class Abbonamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_emissione", nullable = false)
    private LocalDate dataEmissione;

    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAbbonamento validita;

    @ManyToOne
    @JoinColumn(name = "vendita_id")
    private Vendita vendita;

    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;

    @OneToOne
    private Tessera tessera;
}