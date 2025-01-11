package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.personas.MedioDeContacto;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOTecnico {
    private String id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private String cuil;
    private PuntoEstrategico puntoEstrategico;
}
