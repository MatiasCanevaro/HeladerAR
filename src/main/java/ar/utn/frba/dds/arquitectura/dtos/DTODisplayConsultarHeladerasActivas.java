package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTODisplayConsultarHeladerasActivas {
    private String id;
    private DTOProvincia provincia;
    private String direccion;
    private DTOModeloDeHeladera modelo;
    private LocalDateTime activaDesde;
}
