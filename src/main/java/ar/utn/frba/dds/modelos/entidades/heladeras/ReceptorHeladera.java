package ar.utn.frba.dds.modelos.entidades.heladeras;

import ar.utn.frba.dds.modelos.entidades.Persistente;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="receptor_heladera")
public class ReceptorHeladera extends Persistente {
     @OneToOne
     @JoinColumn(name="heladera_id",referencedColumnName = "id")
     private Heladera heladera;
     public void recibir(String jsonPasesDeTarjeta){
          //TODO
          //this.parsearJson(jsonPasesDeTarjeta);
          // for(){
          // PaseDeTarjeta.crearPaseDeTarjeta();
          //}
     }
}
