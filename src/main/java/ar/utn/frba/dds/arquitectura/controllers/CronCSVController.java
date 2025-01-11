package ar.utn.frba.dds.arquitectura.controllers;

import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.csv.CSV;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.*;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.entidades.personas.PersonaFisica;
import ar.utn.frba.dds.modelos.entidades.personas.TipoDocumento;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.repositorios.Repositorio;
import ar.utn.frba.dds.modelos.repositorios.RepositorioPersonaFisica;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import ar.utn.frba.dds.modulos.lector.Lector;
import ar.utn.frba.dds.modulos.notificador.Mensaje;
import ar.utn.frba.dds.modulos.notificador.MensajeCredencialesDeUsuario;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import lombok.AllArgsConstructor;

import javax.persistence.NonUniqueResultException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

@AllArgsConstructor
public class CronCSVController {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Repositorio repositorio;
    private RepositorioPersonaFisica repositorioPersonaFisica;
    private Notificador notificador;
    private MapperImportador mapperImportador;
    private Lector lector;
    private CalculadoraDePuntos calculadoraDePuntos;

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                this.comenzarProcesamiento();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 24, TimeUnit.HOURS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    public void comenzarProcesamiento() {
        List<Object> csvs = repositorio.buscarTodos("CSV")
                .stream()
                .filter(obj -> {
                    CSV csv = (CSV) obj;
                    return !csv.getFueProcesado();
                })
                .toList();

        for (Object objeto : csvs) {
            CSV csv = (CSV) objeto;
            this.procesarCSV(csv);
            csv.setFueProcesado(true);
            repositorio.guardar(csv);
        }
    }

    public void procesarCSV(CSV csv) {
        String rutaArchivoRecibido = csv.getPath();
        List<String[]> lineasLeidas = lector.leerCSV(rutaArchivoRecibido);
        for(String[] linea : lineasLeidas){
            String tipoDocumento = linea[0];
            TipoDocumento tipoDocumentoMapeado = mapperImportador.tipoDocumentoMapper(tipoDocumento);
            String numeroDocumento = linea[1];

            List<PersonaFisica> personasFisica = repositorioPersonaFisica.buscarPersonas(tipoDocumentoMapeado,numeroDocumento);
            PersonaFisica personaFisica;

            if(personasFisica.isEmpty()){
                String nombre = linea[2];
                String apellido = linea[3];
                String email = linea[4];
                personaFisica = PersonaFisica.crearPersonaFisica(
                        tipoDocumentoMapeado,numeroDocumento,nombre,apellido,email);

                Usuario usuario = new Usuario(
                        email,
                        numeroDocumento,
                        TipoRol.PERSONA_FISICA,
                        personaFisica,
                        null,
                        null,
                        null
                );

                repositorioPersonaFisica.guardar(personaFisica);
                repositorio.guardar(usuario);
                csv.agregarPersonaFisica(personaFisica);
                repositorio.actualizar(csv);
                Mensaje mensaje = MensajeCredencialesDeUsuario.generarMensaje(usuario);
                notificador.notificar(mensaje, usuario.getCorreoElectronico());
            } else if (personasFisica.size() == 1) {
                // Si ya existe, recuperar la persona existente
                personaFisica = personasFisica.get(0);
            } else {
                // Manejar el caso donde hay más de una persona encontrada
                throw new NonUniqueResultException("Más de una PersonaFisica encontrada con el mismo tipo y número de documento.");
            }

            LocalDate fechaColaboracion = mapperImportador.localDateMapper(linea[5]);
            FormaDeColaboracion formaDeColaboracion = mapperImportador.formaDeColaboracionMapper(linea[6]);
            Double puntosAAcreditar = 0.0;

            switch (formaDeColaboracion.name()) {
                case "DONACION_DE_DINERO" -> {
                    Double monto = parseDouble(linea[7]);
                    Integer frecuenciaEnDia = 0;
                    DonacionDeDinero donacionDeDinero = DonacionDeDinero.crearDonacionDeDineroConFechaDonacion(
                            formaDeColaboracion, personaFisica, null, monto, frecuenciaEnDia,fechaColaboracion);
                    repositorio.guardar(donacionDeDinero);
                    csv.agregarDonacionDeDinero(donacionDeDinero);
                    repositorio.actualizar(csv);
                    Double pesosDonadosActuales = personaFisica.getPesosDonados();
                    Double montoAAcreditar = parseDouble(linea[7]);
                    personaFisica.setPesosDonados(pesosDonadosActuales + montoAAcreditar);
                    puntosAAcreditar = calculadoraDePuntos.calcularPuntosPesosDonados(montoAAcreditar);
                }
                case "DONACION_DE_VIANDA" -> {
                    DonacionDeVianda donacionDeVianda = DonacionDeVianda.crearDonacionDeVianda(
                            formaDeColaboracion, personaFisica, fechaColaboracion);
                    repositorio.guardar(donacionDeVianda);
                    csv.agregarDonacionDeVianda(donacionDeVianda);
                    repositorio.actualizar(csv);
                    Integer viandasDonadasActuales = personaFisica.getViandasDonadas();
                    personaFisica.setViandasDonadas(viandasDonadasActuales + 1);
                    puntosAAcreditar = calculadoraDePuntos.calcularPuntosViandasDonadas(1);
                }
                case "DISTRIBUCION_DE_VIANDA" -> {
                    Integer cantidadDeViandasDistribuidas = parseInt(linea[7]);
                    DistribucionDeVianda distribucionDeVianda = DistribucionDeVianda.crearDistribucionDeVianda(
                            formaDeColaboracion, personaFisica, fechaColaboracion, cantidadDeViandasDistribuidas);
                    repositorio.guardar(distribucionDeVianda);
                    csv.agregarDistribucionDeVianda(distribucionDeVianda);
                    repositorio.actualizar(csv);
                    Integer viandasDistribuidasActuales = personaFisica.getViandasDistribuidas();
                    personaFisica.setViandasDistribuidas(viandasDistribuidasActuales + cantidadDeViandasDistribuidas);
                    puntosAAcreditar = calculadoraDePuntos.calcularPuntosViandasDistribuidas(cantidadDeViandasDistribuidas);
                }
                case "DISTRIBUCION_DE_TARJETAS" -> {
                    DistribucionDeTarjeta distribucionDeTarjeta = DistribucionDeTarjeta.crearDistribucionDeTarjeta(
                            formaDeColaboracion, personaFisica, fechaColaboracion);
                    repositorio.guardar(distribucionDeTarjeta);
                    csv.agregarDistribucionDeTarjeta(distribucionDeTarjeta);
                    repositorio.actualizar(csv);
                    Integer tarjetasDistribuidasActuales = personaFisica.getTarjetasDistribuidas();
                    personaFisica.setTarjetasDistribuidas(tarjetasDistribuidasActuales + 1);
                    puntosAAcreditar = calculadoraDePuntos.calcularPuntosTarjetasDistribuidas(1);
                }
            }
            personaFisica.sumarPuntosYResetearAtributosDeColaboraciones(puntosAAcreditar);
            repositorioPersonaFisica.guardar(personaFisica);
        }
    }
}