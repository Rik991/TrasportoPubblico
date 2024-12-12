package it.epicode.entity.parco_mezzi;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Tram", query = "SELECT a FROM Tram a")
public class Tram extends ParcoMezzi {

    private int capienza = 200;

    @Override
    public void viaggia(ParcoMezzi mezzo, int traffico) {
        mezzo.setTotaleTratteEffettuate( mezzo.getTotaleTratteEffettuate() + 1);
       LocalTime durata =  mezzo.getTratta().getDurataEffettiva().plusMinutes(traffico);

    }
}