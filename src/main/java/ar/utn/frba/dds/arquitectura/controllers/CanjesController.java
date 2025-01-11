package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.arquitectura.dtos.DTOCanje;
import ar.utn.frba.dds.arquitectura.mappers.MapperDTOCanje;
import ar.utn.frba.dds.arquitectura.utils.ICrudViewsHandler;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Canje;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaJuridica;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioCanje;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CanjesController implements ICrudViewsHandler {
    private Repositorio repositorio;
    private RepositorioCanje repositorioCanje;
    @Override
    public void index(Context context) {
    }

    @Override
    public void show(Context context) {
    }

    @Override
    public void create(Context context) {
    }

    @Override
    public void save(Context context) {
    }

    @Override
    public void edit(Context context) {
    }

    @Override
    public void update(Context context) {
    }

    @Override
    public void delete(Context context) {
    }

    public void historialCanjes(Context context) {
        String userId = context.sessionAttribute("userId");
        String tipoRol = context.sessionAttribute("tipoRol");

        List<Canje> historialCanjes;

        if (tipoRol.equals(TipoRol.PERSONA_FISICA.name())) {
            PersonaFisica personaFisica = (PersonaFisica) repositorio.buscarPorId(userId, PersonaFisica.class.getSimpleName());
            historialCanjes = this.repositorioCanje.buscarCanjePorPersonaFisica(personaFisica);
        } else if (tipoRol.equals(TipoRol.PERSONA_JURIDICA.name())) {
            PersonaJuridica personaJuridica = (PersonaJuridica) repositorio.buscarPorId(userId, PersonaJuridica.class.getSimpleName());
            historialCanjes = this.repositorioCanje.buscarCanjesPorPersonaJuridica(personaJuridica);
        } else {
            context.status(400).result("Tipo de rol no reconocido");
            return;
        }

        List<Object> historialCanjesCasteados = (List<Object>) (List<?>) historialCanjes;

        List<DTOCanje> dtoHistorialCanjes = MapperDTOCanje.convertirListaADTOs(historialCanjesCasteados);

        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Historial de Canjes");
        model.put("historialCanjes", dtoHistorialCanjes);

        context.render("ofertasYCanjes/historial_canje.hbs", model);
    }
}
