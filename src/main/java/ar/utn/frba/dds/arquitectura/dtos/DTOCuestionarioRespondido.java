package ar.utn.frba.dds.arquitectura.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DTOCuestionarioRespondido {
    private String id;
    private DTOCuestionario cuestionario;
    private LocalDateTime fechaYHoraRespondido;
}
