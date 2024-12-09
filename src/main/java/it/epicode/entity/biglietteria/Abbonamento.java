package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "abbonamenti")
@NamedQuery(name = "Trova_tutto_Abbonamento", query = "SELECT a FROM Abbonamento a")
public class Abbonamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendita_id")
    private Vendita vendita;

    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;

    @OneToOne
    private Tessera tessera;
}