package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "vendite")
@NamedQuery(name = "Trova_tutto_Vendita", query = "SELECT a FROM Vendita a")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vendita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "vendita")

    private List<Biglietto> biglietti = new ArrayList<>();

    @OneToMany(mappedBy = "vendita")

    private List<Abbonamento> abbonamenti = new ArrayList<>();

    @OneToMany(mappedBy = "vendita")

    private List<Tessera> tessere = new ArrayList<>();

}