package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Abbonamento", query = "SELECT a FROM Abbonamento a")
public class Abbonamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "rivenditore_id")
    private Rivenditore rivenditore;



}