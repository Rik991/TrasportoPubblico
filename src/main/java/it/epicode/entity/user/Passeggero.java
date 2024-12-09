package it.epicode.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@NamedQuery(name = "Trova_tutto_Passeggero", query = "SELECT a FROM Passeggero a")
public class Passeggero extends User {

}
