package it.epicode.entity.biglietteria;

import it.epicode.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Tessera", query = "SELECT a FROM Tessera a")
public class Tessera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_tessera", nullable = false)
    private String numeroTessera;

    @Column(name = "data_emissione", nullable = false)
    private LocalDate dataEmissione;

    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza; //data emissione + 1 anno

    @Column(nullable = false)
    private boolean attiva;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;


    @OneToOne
    private Abbonamento abbonamento;

    @ManyToOne
    @JoinColumn(name = "rivenditore_id")
    private Rivenditore rivenditore;





}