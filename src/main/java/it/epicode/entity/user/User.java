package it.epicode.entity.user;

import it.epicode.entity.biglietteria.Biglietto;
import it.epicode.entity.biglietteria.Ruolo;
import it.epicode.entity.biglietteria.Tessera;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="utenti")
@NamedQuery(name = "Trova_tutto_User", query = "SELECT a FROM User a")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @OneToMany(mappedBy = "user")
    private List<Biglietto> biglietti;
}