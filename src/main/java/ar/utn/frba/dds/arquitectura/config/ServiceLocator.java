package ar.utn.frba.dds.arquitectura.config;

import ar.utn.frba.dds.arquitectura.controllers.*;
import ar.utn.frba.dds.arquitectura.controllers.CronHeladerasActivasController;
import ar.utn.frba.dds.arquitectura.controllers.CronFallaConexionController;
import ar.utn.frba.dds.modelos.entidades.heladeras.ModeloDeHeladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.ReportadorFallaTecnica;
import ar.utn.frba.dds.modelos.entidades.incidentes.Sugeridor;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.CalculadoraDePuntos;
import ar.utn.frba.dds.modelos.entidades.reportes.*;
import ar.utn.frba.dds.modelos.repositorios.*;
import ar.utn.frba.dds.modulos.datadog.DDMetricsUtils;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import ar.utn.frba.dds.modulos.lector.Lector;
import ar.utn.frba.dds.modulos.notificador.AdapterConcretoApacheEmail;
import ar.utn.frba.dds.modulos.notificador.Notificador;
import ar.utn.frba.dds.modulos.recomendadoraDeLugaresDeDonacion.RecomendadoraDeLugaresDeDonacionAPI;
import io.micrometer.core.instrument.step.StepMeterRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.List;

public class ServiceLocator {
    private static Map<String, Object> instances = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    public static <T> T instanceOf(Class<T> componentClass) {
        String componentName = componentClass.getName();

        if (!instances.containsKey(componentName)) {
            //HeladerasController
            if(componentName.equals(HeladerasController.class.getName())) {
                HeladerasController instance = new HeladerasController(
                        instanceOf(RepositorioHeladera.class),
                        instanceOf(Repositorio.class)
                        );
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioHeladera.class.getName())) {
                RepositorioHeladera instance = new RepositorioHeladera();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }

            //TecnicoController
            if(componentName.equals(TecnicoController.class.getName())) {
                TecnicoController instance = new TecnicoController(
                    instanceOf(Repositorio.class),
                    instanceOf(MapperImportador.class)
                );
                instances.put(componentName, instance);
            }

            //RubrosController
            if(componentName.equals(RubrosController.class.getName())) {
                RubrosController instance = new RubrosController(
                        instanceOf(Repositorio.class)
                );
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }

            //ModeloDeHeladeraController
            if(componentName.equals(ModeloDeHeladeraController.class.getName())) {
                ModeloDeHeladeraController instance = new ModeloDeHeladeraController(
                        instanceOf(Repositorio.class)
                );
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }

            //CuestionarioController
            if(componentName.equals(CuestionarioController.class.getName())) {
                CuestionarioController instance = new CuestionarioController(
                        instanceOf(Repositorio.class),
                        instanceOf(RepositorioCuestionario.class),
                        instanceOf(MapperImportador.class)
                );
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioCuestionario.class.getName())) {
                RepositorioCuestionario instance = new RepositorioCuestionario();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(MapperImportador.class.getName())) {
                MapperImportador instance = new MapperImportador();
                instances.put(componentName, instance);
            }

            //HomeController
            if(componentName.equals(HomeController.class.getName())) {
                HomeController instance = new HomeController(instanceOf(RepositorioHeladera.class));
                instances.put(componentName, instance);
            }

            //ExitoController
            if(componentName.equals(ExitoController.class.getName())) {
                ExitoController instance = new ExitoController();
                instances.put(componentName, instance);
            }

            //OfertasController
            if(componentName.equals(OfertasController.class.getName())) {
                OfertasController instance = new OfertasController(instanceOf(Repositorio.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }

            //FallaTecnicaController
            if(componentName.equals(FallaTecnicaController.class.getName())) {
                FallaTecnicaController instance = new FallaTecnicaController(instanceOf(Repositorio.class),
                        instanceOf(ReportadorFallaTecnica.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(ReportadorFallaTecnica.class.getName())) {
                ReportadorFallaTecnica instance = new ReportadorFallaTecnica();
                instances.put(componentName, instance);
            }

            //LoginController
            if(componentName.equals(LoginController.class.getName())) {
                LoginController instance = new LoginController(instanceOf(RepositorioUsuario.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioUsuario.class.getName())) {
                RepositorioUsuario instance = new RepositorioUsuario();
                instances.put(componentName, instance);
            }

            //LogoutController
            if(componentName.equals(LogoutController.class.getName())) {
                LogoutController instance = new LogoutController();
                instances.put(componentName, instance);
            }

            //PerfilController
            if(componentName.equals(PerfilController.class.getName())) {
                PerfilController instance = new PerfilController(
                        instanceOf(Repositorio.class),
                        instanceOf(RepositorioProvincia.class),
                        instanceOf(RepositorioUsuario.class),
                        instanceOf(Notificador.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioProvincia.class.getName())) {
                RepositorioProvincia instance = new RepositorioProvincia();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioUsuario.class.getName())) {
                RepositorioUsuario instance = new RepositorioUsuario();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(Notificador.class.getName())){
                AdapterConcretoApacheEmail adapterConcretoApacheEmail = new AdapterConcretoApacheEmail();
                Notificador instance = new Notificador(adapterConcretoApacheEmail);
                instances.put(componentName, instance);
            }

            //PersonaVulnerableController
            if(componentName.equals(PersonaVulnerableController.class.getName())) {
                PersonaVulnerableController instance =
                        new PersonaVulnerableController(
                                instanceOf(RepositorioUsuario.class),
                                instanceOf(RepositorioProvincia.class),
                                instanceOf(RepositorioTarjeta.class),
                                instanceOf(MapperImportador.class),
                                instanceOf(CalculadoraDePuntos.class)
                                );
                instances.put(componentName, instance);
            }
            else if(componentName.equals(Repositorio.class.getName())){
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioProvincia.class.getName())){
                RepositorioProvincia instance = new RepositorioProvincia();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(MapperImportador.class.getName())){
                MapperImportador instance = new MapperImportador();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(CalculadoraDePuntos.class.getName())){
                CalculadoraDePuntos instance = CalculadoraDePuntos.crearCalculadoraDePuntos();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioTarjeta.class.getName())){
                RepositorioTarjeta instance = new RepositorioTarjeta();
                instances.put(componentName, instance);
            }

            //PersonaFisicaController
            if(componentName.equals(PersonaFisicaController.class.getName())) {
                PersonaFisicaController instance = new PersonaFisicaController();
                instances.put(componentName, instance);
            }

            //PersonaJuridicaController
            if(componentName.equals(PersonaJuridicaController.class.getName())) {
                PersonaJuridicaController instance = new PersonaJuridicaController();
                instances.put(componentName, instance);
            }

            //RegistroController
            if(componentName.equals(RegistroController.class.getName())) {
                RegistroController instance = new RegistroController(
                        instanceOf(Repositorio.class),
                        instanceOf(RepositorioPersonaFisica.class),
                        instanceOf(RepositorioProvincia.class),
                        instanceOf(RepositorioRubro.class),
                        instanceOf(RepositorioUsuario.class),
                        instanceOf(RepositorioCuestionario.class),
                        instanceOf(RepositorioRespuesta.class),
                        instanceOf(MapperImportador.class),
                        instanceOf(Notificador.class)
                        );
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioProvincia.class.getName())){
                RepositorioProvincia instance = new RepositorioProvincia();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(MapperImportador.class.getName())){
                MapperImportador instance = new MapperImportador();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(Repositorio.class.getName())){
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioPersonaFisica.class.getName())){
                RepositorioPersonaFisica instance = new RepositorioPersonaFisica();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioRubro.class.getName())){
                RepositorioRubro instance = new RepositorioRubro();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioUsuario.class.getName())){
                RepositorioUsuario instance = new RepositorioUsuario();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioCuestionario.class.getName())){
                RepositorioCuestionario instance = new RepositorioCuestionario();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioRespuesta.class.getName())){
                RepositorioRespuesta instance = new RepositorioRespuesta();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(Notificador.class.getName())){
                AdapterConcretoApacheEmail adapterConcretoApacheEmail = new AdapterConcretoApacheEmail();
                Notificador instance = new Notificador(adapterConcretoApacheEmail);
                instances.put(componentName, instance);
            }

            //DashboardController
            if(componentName.equals(DashboardController.class.getName())) {
                DashboardController instance = new DashboardController();
                instances.put(componentName, instance);
            }

            //ContribucionesController
            if(componentName.equals(ContribucionesController.class.getName())) {
                RecomendadoraDeLugaresDeDonacionAPI recomendadoraDeLugaresDeDonacionAPI = RecomendadoraDeLugaresDeDonacionAPI.instancia();
                CalculadoraDePuntos calculadoraDePuntos = CalculadoraDePuntos.crearCalculadoraDePuntos();
                ContribucionesController instance = new ContribucionesController(
                        instanceOf(Repositorio.class),
                        instanceOf(RepositorioRubro.class),
                        instanceOf(RepositorioMotivoDistribucion.class),
                        instanceOf(RepositorioProvincia.class),
                        instanceOf(RepositorioCiudad.class),
                        instanceOf(RepositorioModeloHeladera.class),
                        instanceOf(MapperImportador.class),
                        calculadoraDePuntos,
                        recomendadoraDeLugaresDeDonacionAPI,
                        instanceOf(Lector.class),
                        instanceOf(RepositorioCSV.class)
                );
                instance.leerArchivoProperties();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(Repositorio.class.getName())){
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(MapperImportador.class.getName())){
                MapperImportador instance = new MapperImportador();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioRubro.class.getName())){
                RepositorioRubro instance = new RepositorioRubro();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioMotivoDistribucion.class.getName())){
                RepositorioMotivoDistribucion instance = new RepositorioMotivoDistribucion();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioProvincia.class.getName())){
                RepositorioProvincia instance = new RepositorioProvincia();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioModeloHeladera.class.getName())){
                RepositorioModeloHeladera instance = new RepositorioModeloHeladera();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(Lector.class.getName())){
                Lector instance = new Lector();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioPersonaFisica.class.getName())){
                RepositorioPersonaFisica instance = new RepositorioPersonaFisica();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioCSV.class.getName())){
                RepositorioCSV instance = new RepositorioCSV();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioCiudad.class.getName())){
                RepositorioCiudad instance = new RepositorioCiudad();
                instances.put(componentName, instance);
            }

            //ContactoController
            if(componentName.equals(ContactoController.class.getName())) {
                ContactoController instance = new ContactoController(instanceOf(Repositorio.class));
                instances.put(componentName, instance);
            }

            //CanjesController
            if(componentName.equals(CanjesController.class.getName())) {
                CanjesController instance = new CanjesController(instanceOf(Repositorio.class),
                        instanceOf(RepositorioCanje.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioCanje.class.getName())) {
                RepositorioCanje instance = new RepositorioCanje();
                instances.put(componentName, instance);
            }

            //CronCSVController
            if(componentName.equals(CronCSVController.class.getName())) {
                Notificador notificador = Notificador.crearNotificadorApache();
                CronCSVController instance = new CronCSVController(
                        instanceOf(Repositorio.class),
                        instanceOf(RepositorioPersonaFisica.class),
                        notificador,
                        instanceOf(MapperImportador.class),
                        instanceOf(Lector.class),
                        instanceOf(CalculadoraDePuntos.class)
                );
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioPersonaFisica.class.getName())) {
                RepositorioPersonaFisica instance = new RepositorioPersonaFisica();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(MapperImportador.class.getName())) {
                MapperImportador instance = new MapperImportador();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Lector.class.getName())) {
                Lector instance = new Lector();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(CalculadoraDePuntos.class.getName())) {
                CalculadoraDePuntos instance = new CalculadoraDePuntos();
                instances.put(componentName, instance);
            }

            //CronDonacionDeDineroController
            if(componentName.equals(CronDonacionDeDineroController.class.getName())) {
                CronDonacionDeDineroController instance = new CronDonacionDeDineroController(
                        instanceOf(CalculadoraDePuntos.class),
                        instanceOf(Repositorio.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(CalculadoraDePuntos.class.getName())){
                CalculadoraDePuntos instance = CalculadoraDePuntos.crearCalculadoraDePuntos();
                instances.put(componentName, instance);
            }

            //CronHeladerasActivas
            if(componentName.equals(CronHeladerasActivasController.class.getName())) {
                CronHeladerasActivasController instance = new CronHeladerasActivasController(
                        instanceOf(CalculadoraDePuntos.class),
                        instanceOf(Repositorio.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(CalculadoraDePuntos.class.getName())){
                CalculadoraDePuntos instance = CalculadoraDePuntos.crearCalculadoraDePuntos();
                instances.put(componentName, instance);
            }

            //CronFallaConexion
            if(componentName.equals(CronFallaConexionController.class.getName())) {
                CronFallaConexionController instance = new CronFallaConexionController(
                        1,
                        1,
                        Executors.newScheduledThreadPool(1),
                        instanceOf(RepositorioAlertaHeladera.class),
                        instanceOf(RepositorioHeladera.class));
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioAlertaHeladera.class.getName())) {
                RepositorioAlertaHeladera instance = new RepositorioAlertaHeladera();
                instances.put(componentName, instance);
            }
            else if(componentName.equals(RepositorioHeladera.class.getName())){
                RepositorioHeladera instance = new RepositorioHeladera();
                instances.put(componentName, instance);
            }

            if (componentName.equals(CronReportesController.class.getName())) {
                GenerarPDF generarPDFFallaTecnica = instanceOf(GenerarPDFFallaTecnica.class);
                GenerarPDF generarPDFDonacionDeViandas = instanceOf(GenerarPDFDonacionDeViandas.class);
                GenerarPDF generarPDFViandas = instanceOf(GenerarPDFViandas.class);
                GenerarPDF generarPDFVisitaTecnica = instanceOf(GenerarPDFVisitaTecnica.class);


                List<GenerarPDF> generadoresPDF = new ArrayList<>();
                generadoresPDF.add(generarPDFFallaTecnica);
                generadoresPDF.add(generarPDFDonacionDeViandas);
                generadoresPDF.add(generarPDFViandas);
                generadoresPDF.add(generarPDFVisitaTecnica);

                CronReportesController instance = new CronReportesController(
                        Executors.newScheduledThreadPool(1),
                        generadoresPDF,
                        1
                );
                instances.put(componentName, instance);
            } else if (componentName.equals(GenerarPDFFallaTecnica.class.getName())) {
                GenerarPDFFallaTecnica instance = GenerarPDFFallaTecnica.crearGenerarPDFFallaTecnica();
                instances.put(componentName, instance);
            } else if (componentName.equals(GenerarPDFDonacionDeViandas.class.getName())) {
                GenerarPDFDonacionDeViandas instance = GenerarPDFDonacionDeViandas.crearGenerarPDFDonacionDeViandas();
                instances.put(componentName, instance);
            } else if (componentName.equals(GenerarPDFViandas.class.getName())) {
                GenerarPDFViandas instance = GenerarPDFViandas.crearGenerarPDFViandas();
                instances.put(componentName, instance);
            }else if (componentName.equals(GenerarPDFVisitaTecnica.class.getName())) {
                GenerarPDFVisitaTecnica instance = GenerarPDFVisitaTecnica.crearGenerarPDFVisitaTecnica();
                instances.put(componentName, instance);
            }

            //ReportesController
            if(componentName.equals(ReportesController.class.getName())) {
                String rutaCarpetaReportes = ReportesController.leerArchivoProperties();
                ReportesController instance = new ReportesController(
                        instanceOf(Repositorio.class),
                        rutaCarpetaReportes);
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }

            //TarjetaController
            if(componentName.equals(TarjetaController.class.getName())) {
                TarjetaController instance = new TarjetaController(
                        instanceOf(Repositorio.class),
                        instanceOf(RepositorioTarjeta.class)
                );
                instances.put(componentName, instance);
            }
            else if (componentName.equals(Repositorio.class.getName())) {
                Repositorio instance = new Repositorio();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(RepositorioTarjeta.class.getName())) {
                RepositorioTarjeta instance = new RepositorioTarjeta();
                instances.put(componentName, instance);
            }

            //Datadog
            if(componentName.equals(StepMeterRegistry.class.getName())){
                DDMetricsUtils ddMetricsUtils = instanceOf(DDMetricsUtils.class);
                StepMeterRegistry instance = ddMetricsUtils.getRegistry();
                instances.put(componentName, instance);
            }
            else if (componentName.equals(DDMetricsUtils.class.getName())) {
                DDMetricsUtils instance = new DDMetricsUtils("acceso_alimentario");
                instances.put(componentName, instance);
            }
        }

        return (T) instances.get(componentName);
    }
}
