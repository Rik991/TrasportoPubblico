package it.epicode.entity.biglietteria;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "rivenditori")
@NamedQuery(name = "Trova_tutto_Rivenditore", query = "SELECT a FROM Rivenditore a")
public class Rivenditore extends Vendita{

}