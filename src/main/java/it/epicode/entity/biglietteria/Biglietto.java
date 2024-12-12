package it.epicode.entity.biglietteria;

import it.epicode.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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

    @Column(name = "data_vidimazione")
    private LocalDateTime dataVidimazione; // Timestamp della vidimazione

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}