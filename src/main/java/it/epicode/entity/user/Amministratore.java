package it.epicode.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Amministratore", query = "SELECT a FROM Amministratore a")
public class Amministratore extends User {

//    public double calcolaTempoMedioTratta (){ //da implementare
//        return 0;
//    }

}

