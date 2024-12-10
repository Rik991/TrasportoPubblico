package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "distributori_automatici")
@NamedQuery(name = "Trova_tutto_DistributoreAutomatico", query = "SELECT a FROM DistributoreAutomatico a")
public class DistributoreAutomatico extends Vendita {

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