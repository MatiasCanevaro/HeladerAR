package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTODisplayFallasTecnicas {
    private String id;
    private DTOHeladera heladera;
    private DTOProvincia provincia;
    private String direccion;
    private Integer codigo_postal;
    private String modelo;
    private String tipoIncidente;
    private String fechaYHoraFalla;
    private String fechaYHoraReporte;

    public DTODisplayFallasTecnicas(String id, DTOHeladera heladera, DTOProvincia provincia, String direccion, Integer codigo_postal, String tipoIncidente, String fechaYHoraFalla, String fechaYHoraReporte) {
        this.id = id;
        this.heladera = heladera;
        this.provincia = provincia;
        this.direccion = direccion;
        this.codigo_postal = codigo_postal;
        this.tipoIncidente = tipoIncidente;
        this.fechaYHoraFalla = fechaYHoraFalla;
        this.fechaYHoraReporte = fechaYHoraReporte;
    }
}
