package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class DTOPersonaVulnerable {
    private String id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private DTODireccion direccion;
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
    private List<DTOPersonaVulnerable> menoresACargo;
}
