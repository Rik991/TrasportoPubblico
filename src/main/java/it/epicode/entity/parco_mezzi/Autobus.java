package it.epicode.entity.parco_mezzi;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Autobus", query = "SELECT a FROM Autobus a")
@Inheritance(strategy = InheritanceType.JOINED)
public class Autobus extends ParcoMezzi {


    private int capienza = 50;
}