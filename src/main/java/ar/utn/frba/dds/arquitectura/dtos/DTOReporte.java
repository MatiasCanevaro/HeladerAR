package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOReporte {
    private String nombre;
    private Integer tamanio;
    private LocalDate fechaCreacion;
    private String path;
}
