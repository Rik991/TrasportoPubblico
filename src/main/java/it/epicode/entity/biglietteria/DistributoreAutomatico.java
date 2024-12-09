package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_DistributoreAutomatico", query = "SELECT a FROM DistributoreAutomatico a")
public class DistributoreAutomatico extends Rivenditore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "in_servizio", nullable = false)
    private boolean inServizio;




//    @Override
//    public void emettiBiglietto() {
//        super.emettiBiglietto();
//    }
//
//    @Override
//    public void emettiAbbonamento() {
//        super.emettiAbbonamento();
//    }
}