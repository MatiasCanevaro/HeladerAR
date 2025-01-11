package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.cuestionarios.CuestionarioRespondido;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import ar.utn.frba.dds.modelos.entidades.personas.TipoOrganizacion;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DTOPersonaJuridica {
    private String id;
    private String razonSocial;
    private TipoOrganizacion tipoOrganizacion;
    private Double puntosAcumulados;
    private Double pesosDonados;
    private List<FormaDeColaboracion> formasDeColaboracion;
    private List<DTOOferta> ofertas;
    private List<DTOHeladera> heladeras;
    private DTOCuestionarioRespondido cuestionarioRespondido;
    private DTORubro rubro;
    private DTODireccion direccion;
}
