package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "biglietti")
@NamedQuery(name = "Trova_tutto_Biglietto", query = "SELECT a FROM Biglietto a")
public class Biglietto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean vidimato;

    @ManyToOne
    @JoinColumn(name = "vendita_id")
    private Vendita vendita;


    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;

}