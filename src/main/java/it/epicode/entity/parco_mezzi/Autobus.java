package it.epicode.entity.parco_mezzi;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Autobus", query = "SELECT a FROM Autobus a")
@Inheritance(strategy = InheritanceType.JOINED)
public class Autobus extends ParcoMezzi {


    private int capienza = 50;

    @Override
    public void viaggia(ParcoMezzi mezzo, int traffico) {
        mezzo.setTotaleTratteEffettuate( mezzo.getTotaleTratteEffettuate() + 1);
        LocalTime durata =  mezzo.getTratta().getDurataEffettiva().plusMinutes(traffico);
    }
}