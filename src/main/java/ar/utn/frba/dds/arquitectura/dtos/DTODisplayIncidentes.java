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
public class DTODisplayIncidentes {
    private String id;
    private String provincia;
    private String direccion;
    private Integer codigo_postal;
    private String modelo;
    private String tipoIncidente;
    private String ultima_vez_activa;
    private String fechaYHoraFalla;
}
