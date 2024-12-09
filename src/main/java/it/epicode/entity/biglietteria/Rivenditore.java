package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Rivenditore", query = "SELECT a FROM Rivenditore a")
public class Rivenditore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "rivenditore")
    private List<Biglietto> biglietti = new ArrayList<>();

    @OneToMany(mappedBy = "rivenditore")
    private List<Abbonamento> abbonamenti = new ArrayList<>();

    @OneToMany(mappedBy = "rivenditore")
    private List<Tessera> tessere = new ArrayList<>();



}