package it.epicode.entity.parco_mezzi;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Tram", query = "SELECT a FROM Tram a")
public class Tram extends ParcoMezzi {

    private int capienza = 200;
}