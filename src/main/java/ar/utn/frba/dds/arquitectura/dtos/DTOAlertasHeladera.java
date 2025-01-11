package ar.utn.frba.dds.arquitectura.dtos;

import ar.utn.frba.dds.modelos.entidades.geografia.PuntoEstrategico;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import ar.utn.frba.dds.modelos.entidades.personas.Tecnico;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOAlertasHeladera {
    private String id;
    private TipoIncidente tipoIncidente;
    private LocalDateTime fechaYHora;
    private DTOPuntoEstrategico puntoEstrategico;
    private DTOHeladera heladera;
    private Boolean estaSolucionado;
    private LocalDateTime fechaYHoraFueSolucionado;
    private List<DTOTecnico> tecnicosSugeridos;
    private DTOTecnico tecnicoQueAcepto;
}
