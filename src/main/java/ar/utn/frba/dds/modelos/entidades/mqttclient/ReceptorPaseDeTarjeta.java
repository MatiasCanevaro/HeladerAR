package ar.utn.frba.dds.modelos.entidades.mqttclient;

import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modelos.entidades.heladeras.*;
import ar.utn.frba.dds.modelos.entidades.tarjetas.AccionSolicitada;
import ar.utn.frba.dds.modelos.entidades.tarjetas.PaseDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.tarjetas.ResultadoPaseDeTarjeta;
import ar.utn.frba.dds.modelos.entidades.tarjetas.SolicitudDeApertura;
import ar.utn.frba.dds.modelos.repositorios.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public class ReceptorPaseDeTarjeta {
    private RepositorioHeladera repositorioHeladera;
    private RepositorioTarjeta repositorioTarjeta;
    private Repositorio repositorio;
    public void recibir(String[] mensaje) throws JsonProcessingException, ClassNotFoundException {
        //idHeladera,codigoAlfanumerico, fechaYHoraPaseDeTarjetaString,idSolicitudDeApertura

        String idHeladera = mensaje[0].trim();
        String codigoTarjeta = mensaje[1].trim();
        String fechaYHoraPaseDeTarjetaString = mensaje[2].trim();
        String idSolicitudDeApertura = mensaje[3].trim();

        Heladera heladera = (Heladera) repositorioHeladera.buscarPorId(
                idHeladera,"Heladera");

        Tarjeta tarjeta = repositorioTarjeta.buscar(codigoTarjeta);

        SolicitudDeApertura solicitudDeApertura = (SolicitudDeApertura) repositorio.buscarPorId(
                idSolicitudDeApertura, "SolicitudDeApertura");

        if (solicitudDeApertura == null) {
            System.out.println("No se encontró ninguna solicitud de apertura con el ID proporcionado.");
            return;
        }

        AccionSolicitada accionSolicitada = solicitudDeApertura.getAccionSolicitada();

        DateTimeFormatter formatterPersonalizado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime fechaYHoraPaseDeTarjeta;

        try {
            // Intentar primero con el formato ISO (esperado en el log)
            fechaYHoraPaseDeTarjeta = LocalDateTime.parse(fechaYHoraPaseDeTarjetaString);
        } catch (Exception e1) {
            try {
                // Si falla, intentar con el formato personalizado
                fechaYHoraPaseDeTarjeta = LocalDateTime.parse(fechaYHoraPaseDeTarjetaString, formatterPersonalizado);
            } catch (Exception e2) {
                System.out.println("Error al parsear la fecha y hora: " + fechaYHoraPaseDeTarjetaString);
                e2.printStackTrace();
                return; // Finalizar el método si no se puede parsear
            }
        }
        ResultadoPaseDeTarjeta resultadoPaseDeTarjeta;

        if(this.cumpleRequisitos(heladera,tarjeta,solicitudDeApertura)){
           resultadoPaseDeTarjeta = ResultadoPaseDeTarjeta.ACEPTADO;
           System.out.println("Pase de tarjeta aceptado");
            solicitudDeApertura.setFueUsada(true);
        }else{
            resultadoPaseDeTarjeta = ResultadoPaseDeTarjeta.RECHAZADO;
            System.out.println("Pase de tarjeta rechazado");
        }

        List<Vianda> viandas = solicitudDeApertura.getViandas();
        PaseDeTarjeta paseDeTarjeta = PaseDeTarjeta.crearPaseDeTarjeta(
                heladera,tarjeta,viandas,
                accionSolicitada,fechaYHoraPaseDeTarjeta,
                solicitudDeApertura,resultadoPaseDeTarjeta);

        repositorio.guardar(paseDeTarjeta);
        System.out.println("Pase de tarjeta guardado");
    }

    private Boolean cumpleRequisitos(Heladera heladera, Tarjeta tarjeta, SolicitudDeApertura solicitudDeApertura) {
        if (!Objects.equals(heladera.getId(), solicitudDeApertura.getHeladera().getId())) {
            System.out.println("La heladera no coincide con la de la solicitud de apertura.");
            return false;
        } else if (!Objects.equals(tarjeta.getCodigoAlfanumerico(), solicitudDeApertura.getPersonaFisica().getTarjetaActiva().getCodigoAlfanumerico())) {
            System.out.println("La tarjeta no coincide con la de la solicitud de apertura.");
            return false;
        } else if (solicitudDeApertura.getFechaYHoraVencimiento().isBefore(LocalDateTime.now())) {
            System.out.println("La solicitud de apertura está vencida.");
            return false;
        }
        return true;
    }


    public static ReceptorPaseDeTarjeta crearReceptorPaseDeTarjeta(){
        RepositorioHeladera repositorioHeladera = new RepositorioHeladera();
        RepositorioTarjeta repositorioTarjeta = new RepositorioTarjeta();
        Repositorio repositorio = new Repositorio();

        return ReceptorPaseDeTarjeta
                .builder()
                .repositorio(repositorio)
                .repositorioHeladera(repositorioHeladera)
                .repositorioTarjeta(repositorioTarjeta)
                .build();
    }
    public List<Vianda> parsearViandas(String jsonViandas) {
        List<Vianda> viandas = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonViandas);
            for (int i = 0; i < jsonArray.length(); i++) {
                String id = jsonArray.getString(i);
                Vianda vianda = (Vianda) repositorio.buscarPorId(id,"Vianda");
                viandas.add(vianda);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viandas;
    }
}