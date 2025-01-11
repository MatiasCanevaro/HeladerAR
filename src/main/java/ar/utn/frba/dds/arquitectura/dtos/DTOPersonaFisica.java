package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class DTOPersonaFisica {
    private String id;
    private String nombre;
    private String apellido;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private DTODireccion direccion;
    private Double puntosAcumulados;
    private Double pesosDonados;
    private Integer viandasDonadas;
    private Integer viandasDistribuidas;
    private Integer tarjetasDistribuidas;
    private List<FormaDeColaboracion> formasDeColaboracion;
    private DTOCuestionarioRespondido cuestionarioRespondido;
    private DTOEmail email;
    private List<DTOTarjeta> tarjetas;
}
