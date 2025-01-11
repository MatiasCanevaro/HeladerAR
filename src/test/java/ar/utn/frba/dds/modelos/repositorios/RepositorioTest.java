package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.contacto.Administrador;
import ar.utn.frba.dds.modelos.entidades.contacto.Usuario;
import ar.utn.frba.dds.modelos.entidades.cuestionarios.*;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.FormaDeColaboracion;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.GeneradorDeCodigoAlfanumerico;
import ar.utn.frba.dds.modelos.entidades.formasDeColaboracion.MotivoDistribucion;
import ar.utn.frba.dds.modelos.entidades.geografia.*;
import ar.utn.frba.dds.modelos.entidades.heladeras.*;
import ar.utn.frba.dds.modelos.entidades.incidentes.AlertaHeladera;
import ar.utn.frba.dds.modelos.entidades.incidentes.FallaTecnica;
import ar.utn.frba.dds.modelos.entidades.incidentes.Sugeridor;
import ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Oferta;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;
import ar.utn.frba.dds.modelos.entidades.personas.*;
import ar.utn.frba.dds.modelos.entidades.reportes.GenerarPDFDonacionDeViandasTest;
import ar.utn.frba.dds.modelos.entidades.reportes.GenerarPDFFallaTecnicaTest;
import ar.utn.frba.dds.modelos.entidades.rolesYPermisos.TipoRol;
import ar.utn.frba.dds.modelos.entidades.tarjetas.Tarjeta;
import ar.utn.frba.dds.modulos.importador.MapperImportador;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static ar.utn.frba.dds.modelos.entidades.incidentes.TipoIncidente.FALLA_TECNICA;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositorioTest {
    Repositorio repositorio = new Repositorio();
    RepositorioHeladera repositorioHeladera = new RepositorioHeladera();
    RepositorioProvincia repositorioProvincia = new RepositorioProvincia();
    RepositorioPersonaFisica repositorioPersonaFisica = new RepositorioPersonaFisica();

    RepositorioUsuario repositorioUsuario = new RepositorioUsuario();
    RepositorioCiudad repositorioCiudad = new RepositorioCiudad();
    RepositorioRubro repositorioRubro = new RepositorioRubro();

    RepositorioTarjeta repositorioTarjeta = new RepositorioTarjeta();


    private static final String IMAGE_URL_1 = "https://raw.githubusercontent.com/rxhack/productImage/main/2.jpg";
    private static final String IMAGE_URL_2 = "https://raw.githubusercontent.com/rxhack/productImage/main/3.jpg";
    private static final String IMAGE_URL_3 = "https://raw.githubusercontent.com/rxhack/productImage/main/4.jpg";


    private String descripcion = "LOREM IPSUM DOLOR SIT AMET, CONSECTETUR ADIPISCING ELIT. SED DO EIUSMOD TEMPOR INCIDIDUNT UT LABORE ET DOLO ";

    @BeforeAll
    static void setup() {
        RepositorioTest instance = new RepositorioTest();
        instance.repositorio.limpiar();
        instance.repositorioHeladera.limpiar();
        instance.repositorioProvincia.limpiar();
        instance.repositorioUsuario.limpiar();
        instance.repositorioPersonaFisica.limpiar();
        instance.repositorioCiudad.limpiar();
        instance.repositorioRubro.limpiar();
        instance.repositorioTarjeta.limpiar();
    }
    @Test
    @Order(0)
    void guardarRubros() {
        String[] rubros = {"Tecnología", "Alimentos", "Ropa", "Electrodomésticos", "Juguetes"};
        Arrays.stream(rubros).forEach(nombre -> repositorio.guardar(new Rubro(nombre)));
    }

    @Test
    @Order(1)
    void guardarProvinciasDeArgentina() {
        String[] provincias = {"Buenos Aires", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes", "Entre Ríos",
                "Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "Neuquén",
                "Río Negro", "Salta", "San Juan", "San Luis", "Santa Cruz", "Santa Fe",
                "Santiago del Estero", "Tierra del Fuego", "Tucumán"};
        Arrays.stream(provincias).forEach(nombre -> repositorioProvincia.guardar(new Provincia(nombre)));
    }
    @Test
    @Order(2)
    void guardarMotivoDistribucion() {
        List<String> motivos = Arrays.asList("Desperfecto en la heladera origen", "Faltan viandas en la heladera destino");
        motivos.forEach(motivo -> repositorio.guardar(new MotivoDistribucion(motivo)));
    }
    @Test
    @Order(3)
    public void guardarCiudades() {
        this.guardarCiudadesBuenosAires();
        this.guardarCiudadesCordoba();
        this.guardarCiudadesSanJuan();
        this.guardarCiudadesTierraDelFuego();
        this.guardarCiudadesJujuy();
        this.guardarCiudadesNeuquen();
        this.guardarCiudadesMisiones();
        this.guardarCiudadesSantaFe();
        this.guardarCiudadesCorrientes();
        this.guardarCiudadesLaRioja();
        this.guardarCiudadesChubut();
        this.guardarCiudadesSalta();
        this.guardarCiudadesLaPampa();
        this.guardarCiudadesFormosa();
        this.guardarCiudadesChaco();
        this.guardarCiudadesSantaCruz();
        this.guardarCiudadesCatamarca();
        this.guardarCiudadesSanLuis();
        this.guardarCiudadesSanLuis();
        this.guardarCiudadesMendoza();
        this.guardarCiudadesTucuman();
        this.guardarCiudadesEntreRios();
        this.guardarCiudadesRioNegro();
        this.guardarCiudadesSantiagoDelEstero();
    }

    @Test
    @Order(6)
    public void guardarPersonaFisica() {
        PersonaFisica personaFisica = new PersonaFisica();
        personaFisica.setNombre("Juan Fernando");
        personaFisica.setApellido("Pérez");
        personaFisica.setTipoDocumento(TipoDocumento.DNI);
        personaFisica.setNumeroDocumento("12345678");
        CentroDeLaCiudad centroDeCABA = repositorio.buscarPorNombre("Buenos Aires","CentroDeLaCiudad",CentroDeLaCiudad.class);
        Ciudad ciudad = repositorio.buscarPorNombre("Buenos Aires","Ciudad",Ciudad.class);
        Provincia provincia = repositorio.buscarPorNombre("Buenos Aires","Provincia",Provincia.class);
        Direccion direccion = this.crearDireccion(
                provincia.getNombre(),
                centroDeCABA,
                ciudad.getNombre(),
                Arrays.asList("Balcarce", "78", "1064", null));
        personaFisica.setFechaNacimiento(LocalDate.now());
        personaFisica.setDireccion(direccion);

        Email email = new Email("admin-pf@gmail.com");
        personaFisica.setEmail(email);


        personaFisica.setPuntosAcumulados(90000D);
        personaFisica.setViandasDonadas(650);
        personaFisica.setViandasDistribuidas(97);
        personaFisica.setTarjetasDistribuidas(166);


        List<FormaDeColaboracion> formasDeColaboracion  = Arrays.asList(
                FormaDeColaboracion.DONACION_DE_DINERO,
                FormaDeColaboracion.DISTRIBUCION_DE_TARJETAS,
                FormaDeColaboracion.DONACION_DE_VIANDA,
                FormaDeColaboracion.DISTRIBUCION_DE_VIANDA
        );
        personaFisica.setFormasDeColaboracion(formasDeColaboracion);


        repositorioPersonaFisica.guardar(personaFisica);

        Usuario usuario = new Usuario(email.getCorreoElectronico(), "admin", TipoRol.PERSONA_FISICA,
                personaFisica, null,null,null);
        repositorioUsuario.guardar(usuario);
    }

    @Test
    @Order(7)
    void guardarPersonaJuridica() {
        PersonaJuridica personaJuridica = new PersonaJuridica();
        personaJuridica.setRazonSocial("Arcos Plateados SA");
        personaJuridica.setTipoOrganizacion(TipoOrganizacion.EMPRESA);
        personaJuridica.setPuntosAcumulados(1500.0);
        personaJuridica.setPesosDonados(5000.0);

        List<FormaDeColaboracion> formasDeColaboracion = Arrays.asList(
                FormaDeColaboracion.DONACION_DE_DINERO,
                FormaDeColaboracion.OFRECER_PRODUCTOS_Y_SERVICIOS
                );
        personaJuridica.setFormasDeColaboracion(formasDeColaboracion);
        // Busca las ofertas previamente guardadas y las asigna a PersonaJuridica
        List<String> nombresOfertas = Arrays.asList("Beat Solo3 Wireless", "Nike Air Max 270", "Samsung Galaxy S21");
        List<Oferta> ofertasAsociadas = nombresOfertas.stream()
                .map(this::obtenerOfertaPorNombre)  // Este método busca las ofertas en la base de datos
                .filter(Objects::nonNull)
                .toList();
        personaJuridica.setOfertas(ofertasAsociadas);


        @SuppressWarnings("unchecked")
        List<Heladera> heladerasGuardadas = (List<Heladera>) (List<?>) repositorioHeladera.buscarTodos("Heladera");
        personaJuridica.setHeladeras(heladerasGuardadas);
        CentroDeLaCiudad centroDeCABA = repositorio.buscarPorNombre(
                "Buenos Aires","CentroDeLaCiudad",CentroDeLaCiudad.class);
        Ciudad ciudad = repositorio.buscarPorNombre("Buenos Aires","Ciudad",Ciudad.class);

        Provincia provincia = repositorio.buscarPorNombre("Buenos Aires","Provincia",Provincia.class);
        Direccion direccion = this.crearDireccion(
                provincia.getNombre(),
                centroDeCABA,
                ciudad.getNombre(),
                Arrays.asList("Fraga", "1101", "1427", "2"));
        Rubro rubro = obtenerRubroPorNombre("Tecnología");
        personaJuridica.setRubro(rubro);
        personaJuridica.setDireccion(direccion);


        repositorio.guardar(personaJuridica);

        Usuario usuario = new Usuario("admin-pj@gmail.com", "admin", TipoRol.PERSONA_JURIDICA, null, personaJuridica,null,null);
        repositorioUsuario.guardar(usuario);
    }

    @Test
    @Order(11)
    void guardarPuntosEstrategicosYViandas() {
        List<ModeloDeHeladera> modelosDeHeladera = (List<ModeloDeHeladera>) (List<?>) repositorio.buscarTodos("ModeloDeHeladera");

        List<PuntoEstrategico> puntosEstrategicos = Arrays.asList(
                guardarPuntoEstrategicoEnCABA("Corrientes", "-34.6044422", "-58.3919939", Arrays.asList("Av. Corrientes", "1234", "1043")),
                guardarPuntoEstrategicoEnCABA("Santa Fe", "-34.5750345", "-58.4355647", Arrays.asList("Av. Santa Fe", "5678", "1425")),
                guardarPuntoEstrategicoEnCABA("Rivadavia", "-34.6096426", "-58.3997323", Arrays.asList("Av. Rivadavia", "2345", "1402")),
                guardarPuntoEstrategicoEnCABA("Alvear", "-34.5962430", "-58.37500645516002", Arrays.asList("Marcelo T. Alvear", "590", "1005")),
                guardarPuntoEstrategicoEnCABA("de Mayo", "-34.6084164", "-58.3742021", Arrays.asList("Av. de Mayo", "543", "1080", "3")),
                guardarPuntoEstrategicoEnCABA("Independencia", "-34.6171998", "-58.371718728841785", Arrays.asList("Avenida Independencia", "401", "1100", "2")),
                guardarPuntoEstrategicoEnCABA("Lavalle", "-34.6024823", "-58.3793363", Arrays.asList("Calle Lavalle", "890", "1043")),
                guardarPuntoEstrategicoEnCABA("Pueyrredón", "-34.5990337", "-58.4039195", Arrays.asList("Av. Pueyrredón", "7654", "1120", "5"))
        );

        Vianda vianda1 = guardarVianda("Arroz con Pollo", LocalDate.now().plusDays(5), 300.0, 500.0);
        Vianda vianda2 = guardarVianda("Vegetales Asados", LocalDate.now().plusDays(7), 200.0, 400.0);
        Vianda vianda3 = guardarVianda("Fideos con Tuco", LocalDate.now().plusDays(3), 350.0, 450.0);

        for (int i = 0; i < puntosEstrategicos.size(); i++) {
            ModeloDeHeladera modeloHeladera = modelosDeHeladera.get(i % modelosDeHeladera.size());
            Heladera heladera = crearHeladera(modeloHeladera, puntosEstrategicos.get(i));
            heladera.agregarViandas(Arrays.asList(vianda1, vianda2, vianda3));
        }
    }

    @Test
    @Order(12)
    public void guardarPersonasFisicas() {

        List<String[]> personasData = Arrays.asList(
                new String[]{"LE", "02392562", "Ana", "García", "matiascanevaro@hotmail.com", "09/06/2014", "DONACION_VIANDAS", "52893"},
                new String[]{"LC", "262008655", "Carlos", "Sánchez", "maticanevaro@gmail.com", "28/03/2004", "ENTREGA_TARJETAS", "67015"},
                new String[]{"DNI", "00712794", "Luis", "Gómez", "florenciazanoni1@gmail.com", "23/03/2006", "REDISTRIBUCION_VIANDAS", "8128"},
                new String[]{"DNI", "50037748", "Ana", "Gómez", "mgaraventa@frba.utn.edu.ar", "23/07/1952", "DONACION_VIANDAS", "7"},
                new String[]{"LE", "777439450", "Carlos", "Sánchez", "maticanevaro@gmail.com", "05/05/1974", "DONACION_VIANDAS", "35414"}
                );

        for (String[] data : personasData) {
            MapperImportador mapperImportador = new MapperImportador();
            PersonaFisica personaFisica = PersonaFisica.crearPersonaFisica(
                    mapperImportador.tipoDocumentoMapper(data[0]),
                    data[1],
                    data[2],
                    data[3],
                    null)
                    ;

            Email email = new Email(data[4]);
            personaFisica.setEmail(email);
            personaFisica.setFechaNacimiento(LocalDate.parse(data[5], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            personaFisica.setPuntosAcumulados(Double.parseDouble(data[7]));

            Provincia provincia = repositorio.buscarPorNombre("Buenos Aires", "Provincia", Provincia.class);
            Ciudad ciudad = repositorio.buscarPorNombre("Buenos Aires","Ciudad", Ciudad.class);
            CentroDeLaCiudad centroDeCABA = repositorio.buscarPorNombre(
                    "Buenos Aires","CentroDeLaCiudad",CentroDeLaCiudad.class);
            Direccion direccion = this.crearDireccion(
                    "Buenos Aires",
                    centroDeCABA,
                    "Buenos Aires",
                    Arrays.asList("Rivadavia", "123", "4567", null));
            personaFisica.setDireccion(direccion);

            List<FormaDeColaboracion> formasDeColaboracion = Arrays.asList(mapperImportador.formaDeColaboracion(data[6]));
            personaFisica.setFormasDeColaboracion(formasDeColaboracion);

            repositorioPersonaFisica.guardar(personaFisica);

            Usuario usuario = new Usuario(email.getCorreoElectronico(), "password", TipoRol.PERSONA_FISICA, personaFisica, null,null,null);
            repositorioUsuario.guardar(usuario);
        }
    }

   /* @Test
    @Order(13)
    void generarReportes() {
        String[] args = {};
        GenerarPDFDonacionDeViandasTest.main(args);
        GenerarPDFDonacionDeViandasTest.main(args);
        GenerarPDFFallaTecnicaTest.main(args);
    }*/

    @Test
    @Order(14)
    void guardarAdmin() {
        Administrador administrador = new Administrador();
        repositorioUsuario.guardar(administrador);
        Usuario usuario = new Usuario("admin@gmail.com", "admin", TipoRol.ADMIN, null, null,administrador,null);
        repositorioUsuario.guardar(usuario);
    }

    @Test
    @Order(15)
    void guardarCuestionarioPersonaFisica() {
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setNombre("Cuestionario Persona Fisica");
        List<TipoRol> rol = new ArrayList<>();
        rol.add(TipoRol.PERSONA_FISICA);
        cuestionario.setRolesQueAcepta(rol);
        repositorio.guardar(cuestionario);

        Pregunta pregunta1 = new Pregunta("Número de CUIT/CUIL",TipoPregunta.ABIERTA,null);
        repositorio.guardar(pregunta1);

        Pregunta pregunta2 = new Pregunta("¿Cómo se enteró de la existencia de este sistema?", TipoPregunta.UNICA,null);
        Opcion opcion1 = new Opcion("Internet");
        Opcion opcion2 = new Opcion("Recomendación de un familiar");
        Opcion opcion3 = new Opcion("Recomendación de un amigo");
        Opcion opcion4 = new Opcion("Otro");
        pregunta2.agregarOpcion(opcion1);
        pregunta2.agregarOpcion(opcion2);
        pregunta2.agregarOpcion(opcion3);
        pregunta2.agregarOpcion(opcion4);
        repositorio.guardar(opcion1);
        repositorio.guardar(opcion2);
        repositorio.guardar(opcion3);
        repositorio.guardar(opcion4);
        repositorio.guardar(pregunta2);

        Pregunta pregunta3 = new Pregunta("¿En qué rubros ha trabajado? (puede seleccionar más de una)", TipoPregunta.MULTIPLE,null);
        Opcion opcion5 = new Opcion("Tecnología");
        Opcion opcion6 = new Opcion("Salud");
        Opcion opcion7 = new Opcion("Otro");
        pregunta3.agregarOpcion(opcion5);
        pregunta3.agregarOpcion(opcion6);
        pregunta3.agregarOpcion(opcion7);
        repositorio.guardar(opcion5);
        repositorio.guardar(opcion6);
        repositorio.guardar(opcion7);
        repositorio.guardar(pregunta3);

        Pregunta pregunta4 = new Pregunta("¿Qué expectativas tiene de este sistema?",TipoPregunta.ABIERTA,null);
        repositorio.guardar(pregunta4);

        Pregunta pregunta5 = new Pregunta("Seleccione su género", TipoPregunta.UNICA,null);
        Opcion opcion8 = new Opcion("Femenino");
        Opcion opcion9 = new Opcion("Masculino");
        Opcion opcion10 = new Opcion("Otro");
        pregunta5.agregarOpcion(opcion8);
        pregunta5.agregarOpcion(opcion9);
        pregunta5.agregarOpcion(opcion10);
        repositorio.guardar(opcion8);
        repositorio.guardar(opcion9);
        repositorio.guardar(opcion10);
        repositorio.guardar(pregunta5);

        Pregunta pregunta6 = new Pregunta("Seleccione los lenguajes que puede hablar (puede seleccionar más de una)", TipoPregunta.MULTIPLE,null);
        Opcion opcion11 = new Opcion("Inglés");
        Opcion opcion12 = new Opcion("Francés");
        Opcion opcion13 = new Opcion("Alemán");
        Opcion opcion14 = new Opcion("Portugués");
        Opcion opcion15 = new Opcion("Otro");
        pregunta6.agregarOpcion(opcion11);
        pregunta6.agregarOpcion(opcion12);
        pregunta6.agregarOpcion(opcion13);
        repositorio.guardar(opcion11);
        repositorio.guardar(opcion12);
        repositorio.guardar(opcion13);
        repositorio.guardar(opcion14);
        repositorio.guardar(opcion15);
        repositorio.guardar(pregunta6);

        cuestionario.agregarPreguntas(List.of(pregunta1, pregunta2, pregunta3, pregunta4, pregunta5, pregunta6));
        repositorio.guardar(cuestionario);
    }
    @Test
    @Order(16)
    void guardarTecnicos() {
        List<String[]> tecnicosData = Arrays.asList(
                new String[]{"DNI", "12345678", "Pedro", "González", "EMAIL", "tpdisenio99@gmail.com", "123456","Doblas","2075","C1424BMO","admin-t@gmail.com","admin"},
                new String[]{"DNI", "87654321", "María", "López","EMAIL", "maria@example.com", "456789","Cachimayo","1955","C1424ARY","tpdisenio99@gmail.com","admin"}
        );

        Ciudad caba = repositorio.buscarPorNombre("Buenos Aires","Ciudad",Ciudad.class);
        for (String[] data : tecnicosData) {
            MapperImportador mapperImportador = new MapperImportador();
            Tecnico tecnico = new Tecnico();

            tecnico.setTipoDocumento(mapperImportador.tipoDocumentoMapper(data[0]));
            tecnico.setNumeroDocumento(data[1]);
            tecnico.setNombre(data[2]);
            tecnico.setApellido(data[3]);

            if (tecnico.getMediosDeContacto() == null) {
                tecnico.setMediosDeContacto(new ArrayList<>());
            }

            TipoContacto tipoContacto = mapperImportador.medioDeContactoMapper(data[4]);
            String contacto = data[5];
            if (tipoContacto != null && contacto != null) {
                MedioDeContacto nuevoMedio = new MedioDeContacto(tipoContacto, contacto);
                tecnico.getMediosDeContacto().add(nuevoMedio);
            }
            tecnico.setCuil(data[6]);
            Map<String,String> coordenadas = Nominative.obtenerCoordenadas(data[7],data[8],caba.getNombre());
            String latitud = coordenadas.get("lat");
            String longitud = coordenadas.get("lon");
            Direccion direccion = new Direccion(
                    data[7],
                    data[8],
                    null,
                    data[9],
                    caba,
                    caba.getProvincia()
            );
            repositorio.guardar(direccion);
            PuntoEstrategico puntoEstrategico = new PuntoEstrategico(
                    data[7],
                    latitud,
                    longitud,
                    direccion
            );
            tecnico.setPuntoEstrategico(puntoEstrategico);
            repositorio.guardar(puntoEstrategico);
            Usuario usuario = new Usuario(data[10], data[11], TipoRol.TECNICO, null,null,null, tecnico);
            repositorio.guardar(tecnico);
            repositorio.guardar(usuario);
        }
    }

    @Test
    @Order(17)
    void generarViandas(){
        String[] nombresComida = {"Asado", "Empanada","Locro", "Milanesa", "Matambre a la Pizza", "Humita", "Provoleta", "Choripán", "Porción de Pizza", "Pasta"};
        LocalDate[] fechasCaducidad = {LocalDate.of(2023, 11, 25),
                LocalDate.of(2024, 11, 27),
                LocalDate.of(2024, 11, 29),
                LocalDate.of(2025, 2, 2),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 3),
                LocalDate.of(2025, 2, 5),
                LocalDate.of(2025, 1, 7),
                LocalDate.of(2025, 1, 7),
                LocalDate.of(2024, 2, 9)};
        double[] calorias = {800, 350, 700, 500, 600, 350, 400, 400, 500, 500};
        double[] pesos = {350, 150, 600, 300, 400, 200, 300, 200, 300, 250};

        for (int i = 0; i < nombresComida.length; i++) {
            Vianda vianda = new Vianda();
            vianda.setNombreComida(nombresComida[i]);
            vianda.setFechaCaducidad(fechasCaducidad[i]);
            vianda.setCalorias(calorias[i]);
            vianda.setPeso(pesos[i]);
            repositorio.guardar(vianda);
        }
    }

    @Test
    @Order(18)
    void guardarViandasEnHeladeras() {
        List<Heladera> heladeras = (List<Heladera>) (List<?>) repositorioHeladera.buscarTodos("Heladera");
        List<Vianda> viandas = (List<Vianda>) (List<?>) repositorio.buscarTodos("Vianda");

        for (Heladera heladera : heladeras) {
            heladera.agregarViandas(viandas);
            repositorio.guardar(heladera);
        }
    }

    //TODO arreglar el guardado de fechas para la heladera
    /*@Test
    @Order(14)
    void guardarHeladera() {
        Heladera heladera = new Heladera();
        this.cargarFechasYHoras(heladera);
        repositorio.guardar(heladera);
    }*/
    @Test
    @Order(19)
    void guardarTarjetas() {

        List<String[]> tarjetasData = Arrays.asList(
                new String[]{"4", "50037748", "DNI"},
                new String[]{"5", "262008655", "LIBRETA_CIVICA"},
                new String[]{"6", "02392562", "LIBRETA_ENROLAMIENTO"},
                new String[]{"1", "12345678", "DNI"}
        );

        for (String[] tarjetaData : tarjetasData) {
            Tarjeta tarjeta = Tarjeta.crearTarjeta(tarjetaData[0]);
            List<PersonaFisica> personasFisicas = repositorioPersonaFisica.buscarPersonas(TipoDocumento.valueOf(tarjetaData[2]), tarjetaData[1]);

            if (!personasFisicas.isEmpty()) {
                PersonaFisica personaFisica = personasFisicas.get(0);  // Toma el primer elemento para evitar problemas con el índice
                if (tarjetaData[0].equals("1")) {
                    List<LocalDateTime> fechasRetiro = Arrays.asList(
                            LocalDateTime.of(2024, 9, 30, 18, 31, 20),
                            LocalDateTime.now()
                    );
                    tarjeta.setFechasRetirosDeViandas(fechasRetiro);
                }

                personaFisica.setTarjetas(new ArrayList<>());  // Inicializa la lista de tarjetas de la persona
                tarjeta.setPersonaFisica(personaFisica);
                tarjeta.setCantidadDeVecesQuePuedeSerUtilizadaPorDia(4);
                repositorioTarjeta.guardar(tarjeta);
                String codigoAlfanumerico = GeneradorDeCodigoAlfanumerico.generarCodigo(repositorioTarjeta);
                tarjeta.setCodigoAlfanumerico(codigoAlfanumerico);

            }
        }
    }
    //TODO no funciona guardar el id de solicitud

    @Test
    @Order(20)
    void guardarFallaTecnica() {

        List<Heladera> heladeras = (List<Heladera>) (List<?>) repositorioHeladera.buscarTodos("Heladera");
        Heladera heladera = heladeras.get(0);
        PuntoEstrategico puntoEstrategico = heladera.getPuntoEstrategico();
        PuntoEstrategico puntoEstrategico1 = (PuntoEstrategico) repositorio.buscarPorId(puntoEstrategico.getId(), "PuntoEstrategico");
        PersonaFisica personaFisica = repositorioPersonaFisica.buscar(TipoDocumento.DNI, "12345678");

        FallaTecnica fallaTecnica = new FallaTecnica(
                LocalDateTime.of(2024, 10, 30, 18, 31, 20),
                LocalDateTime.of(2024, 10, 30, 19, 3, 20),
                puntoEstrategico1,
                heladera,
                false,
                null,
                personaFisica,
                "Falla en el sensor de temperatura",
                "/img/camiseta_boca.png",
                TipoIncidente.FALLA_TECNICA,
                null,
                null
                );

        repositorio.guardar(fallaTecnica);
    }

 /*   @Test
    @Order(18)
    public void guardarSolicitudesDeApertura(){
        PersonaFisica personaFisica = repositorioPersonaFisica.buscar(TipoDocumento.DNI, "12345678");
        Heladera heladera = (Heladera) repositorioHeladera.buscarTodos("Heladera").get(0);
        LocalDateTime fechaYHoraSolicitud = LocalDateTime.now();
        List<Vianda> viandas = (List<Vianda>) (List<?>) repositorio.buscarTodos("Vianda");
        AccionSolicitada accionSolicitada = AccionSolicitada.INGRESO;
        Boolean fueUsada = false;
        SolicitudDeApertura solicitudDeApertura = new SolicitudDeApertura(personaFisica, viandas, heladera, fechaYHoraSolicitud, accionSolicitada, fueUsada);

        repositorio.guardar(solicitudDeApertura);

    }*/
   @Test
    @Order(19)
    void guardarConfiguracionDeTemperatura() {
        List<Object> heladeras = repositorioHeladera.buscarTodos("Heladera");
        List<Object> personasFisicas = repositorio.buscarTodos("PersonaFisica");

        for (Object obj : personasFisicas) {
            PersonaFisica personaFisica = (PersonaFisica) obj;

            for (Object heladeraObj : heladeras) {
                Double minima = ThreadLocalRandom.current().nextDouble(-5.0, -1.0);
                Double maxima = ThreadLocalRandom.current().nextDouble(0.0, 3.0);
                ConfiguracionDeTemperatura configuracionDeTemperatura = new ConfiguracionDeTemperatura(maxima
                        ,minima, personaFisica, LocalDateTime.now());

                Heladera heladera = (Heladera) heladeraObj;
                heladera.setConfiguracionesDeTemperaturas(null);
                repositorioHeladera.guardar(heladera);

            }
        }
    }



    @Test
    @Order(20)
    void guardarCuestionarioRespondidoPersonaFisica(){
        List<Object> personasFisicas = repositorio.buscarTodos("PersonaFisica");
        List<Object> cuestionarios = repositorio.buscarTodos("Cuestionario");
        for (Object obj : personasFisicas) {
            PersonaFisica personaFisica = (PersonaFisica) obj;
            Cuestionario cuestionario = (Cuestionario) cuestionarios.get(0);
            CuestionarioRespondido cuestionarioRespondido = new CuestionarioRespondido(cuestionario, LocalDateTime.now());
            repositorio.guardar(cuestionarioRespondido);
            personaFisica.setCuestionarioRespondido(cuestionarioRespondido);
        }
    }
    @Test
    @Order(21)
    void guardarCuestionarioRespondidoPersonaJuridica(){
        List<Object> personasJuridicas = repositorio.buscarTodos("PersonaJuridica");
        List<Object> cuestionarios = repositorio.buscarTodos("Cuestionario");
        for (Object obj : personasJuridicas) {
            PersonaJuridica personaJuridica = (PersonaJuridica) obj;
            Cuestionario cuestionario = (Cuestionario) cuestionarios.get(0);
            CuestionarioRespondido cuestionarioRespondido = new CuestionarioRespondido(cuestionario,LocalDateTime.now());
            repositorio.guardar(cuestionarioRespondido);
            personaJuridica.setCuestionarioRespondido(cuestionarioRespondido);
        }
    }
//    @Test
//    @Order(4)
//    void guardarOfertasTest() {
//        Rubro rubroTecnologia = obtenerRubroPorNombre("Tecnología");
//        Rubro rubroRopa = obtenerRubroPorNombre("Ropa");
//
//        if (rubroTecnologia == null || rubroRopa == null) {
//            throw new RuntimeException("Alguno de los rubros no está disponible.");
//        }
//
//        List<Oferta> ofertas = Arrays.asList(
//                new Oferta("Beat Solo3 Wireless", descripcion, IMAGE_URL_1, 20000.0, rubroTecnologia),
//                new Oferta("Nike Air Max 270", descripcion, IMAGE_URL_2, 15000.0, rubroTecnologia),
//                new Oferta("Samsung Galaxy S21", descripcion, IMAGE_URL_3, 25000.0, rubroTecnologia),
//                new Oferta("Apple Watch Series 6", descripcion, IMAGE_URL_1, 30000.0, rubroTecnologia),
//                new Oferta("Sony WH-1000XM4", descripcion, IMAGE_URL_2, 22000.0, rubroTecnologia),
//                new Oferta("Dell XPS 13", descripcion, IMAGE_URL_3, 40000.0, rubroTecnologia),
//                new Oferta("Canon EOS Rebel T7", descripcion, IMAGE_URL_1, 35000.0, rubroTecnologia),
//                new Oferta("Fitbit Charge 4", descripcion, IMAGE_URL_2, 18000.0, rubroTecnologia),
//                new Oferta("GoPro HERO9 Black", descripcion, IMAGE_URL_3, 45000.0, rubroTecnologia)
//        );
//
//        ofertas.forEach(oferta -> repositorio.guardar(oferta));
//    }

    @Test
    @Order(5)
    void guardarHeladeraTest() {
        ModeloDeHeladera modeloHeladera1 = new ModeloDeHeladera("Neba A280",250, -10.0, -1.0);
        PuntoEstrategico puntoEstrategico1 = guardarPuntoEstrategicoEnCABA("Avenida Corrientes", "-34.60372222", "-58.3816", Arrays.asList("Avenida Corrientes", "1104", "1043"));
        Heladera heladera1 = crearHeladera(modeloHeladera1, puntoEstrategico1);

        ModeloDeHeladera modeloHeladera2 = new ModeloDeHeladera("LG B200", 250,-5.0, 0.0);
        PuntoEstrategico puntoEstrategico2 = guardarPuntoEstrategicoEnCABA("Avenida Pueyrredón", "-34.5875555", "-58.3974", Arrays.asList("Avenida Pueyrredón", "2187", "1425"));
        Heladera heladera2 = crearHeladera(modeloHeladera2, puntoEstrategico2);

        assertTrue(repositorioHeladera.buscarTodos("Heladera").contains(heladera1));
        assertTrue(repositorioHeladera.buscarTodos("Heladera").contains(heladera2));
    }



    @Test
    @Order(22)
    void guardarPersonaVulnerable() {
        PersonaVulnerable personaVulnerable = new PersonaVulnerable();
        personaVulnerable.setNombre("Juan");
        personaVulnerable.setApellido("Pérez");
        personaVulnerable.setTipoDocumento(TipoDocumento.DNI);
        personaVulnerable.setNumeroDocumento("12345678");
        personaVulnerable.setMenoresACargo(null);
        CentroDeLaCiudad centroDeCABA = repositorio.buscarPorNombre(
                "Buenos Aires","CentroDeLaCiudad",CentroDeLaCiudad.class);

        Ciudad ciudad = repositorio.buscarPorNombre("Buenos Aires","Ciudad",Ciudad.class);
        Provincia provincia = repositorio.buscarPorNombre("Buenos Aires","Provincia",Provincia.class);

        Direccion direccion = this.crearDireccion(
                provincia.getNombre(),
                centroDeCABA,
                ciudad.getNombre(),
                Arrays.asList("Balcarce", "78", "1064", null));
        personaVulnerable.setDireccion(direccion);
        repositorio.guardar(personaVulnerable);
    }

    @Test
    @Order(23)
    void obtenerCoordenadas() {
        Map <String, String> coordenadas = Nominative.obtenerCoordenadas("Yatasto", "627", "san carlos de bariloche");
        System.out.println(coordenadas.get("lat"));
        System.out.println(coordenadas.get("lon"));
    }
/* TODO: arreglar el guardado de alertas
    @Test
    @Order(24)
    void guardarAlertas() {
        PuntoEstrategico corrientes = repositorio.buscarPorNombre("Corrientes","PuntoEstrategico",PuntoEstrategico.class);
        PuntoEstrategico lavalle = repositorio.buscarPorNombre("Lavalle","PuntoEstrategico",PuntoEstrategico.class);
        PuntoEstrategico pueyrredon = repositorio.buscarPorNombre("Pueyrredón","PuntoEstrategico",PuntoEstrategico.class);

        Heladera heladeraCorrientes = repositorioHeladera.buscarHeladeraPorNombrePuntoEstrategico("Corrientes");
        Heladera heladeraLavalle = repositorioHeladera.buscarHeladeraPorNombrePuntoEstrategico("Lavalle");
        Heladera heladeraPueyrredon = repositorioHeladera.buscarHeladeraPorNombrePuntoEstrategico("Pueyrredón");

        Sugeridor sugeridor = Sugeridor.crearSugeridor();
        AlertaHeladera alertaCorrientes = new AlertaHeladera(TipoIncidente.TEMPERATURA, LocalDateTime.now(), corrientes, heladeraCorrientes, false, null, null, null, sugeridor);
        AlertaHeladera alertaLavalle = new AlertaHeladera(TipoIncidente.FRAUDE, LocalDateTime.now(), lavalle, heladeraLavalle, false, null, null, null, sugeridor);
        AlertaHeladera alertaPueyrredon = new AlertaHeladera(TipoIncidente.FALLA_CONEXION, LocalDateTime.now(), pueyrredon, heladeraPueyrredon, false, null, null, null, sugeridor);

        Hibernate.initialize(heladeraCorrientes.getFechasYHorasDejoDeEstarActiva());
        Hibernate.initialize(heladeraLavalle.getFechasYHorasDejoDeEstarActiva());
        Hibernate.initialize(heladeraPueyrredon.getFechasYHorasDejoDeEstarActiva());

        heladeraCorrientes.setEstaActiva(false);
        heladeraCorrientes.getFechasYHorasDejoDeEstarActiva().add(LocalDateTime.now());
        heladeraLavalle.setEstaActiva(false);
        heladeraLavalle.getFechasYHorasDejoDeEstarActiva().add(LocalDateTime.now());
        heladeraPueyrredon.setEstaActiva(false);
        heladeraPueyrredon.getFechasYHorasDejoDeEstarActiva().add(LocalDateTime.now());

        repositorio.actualizar(heladeraCorrientes);
        repositorio.actualizar(heladeraLavalle);
        repositorio.actualizar(heladeraPueyrredon);

        repositorio.guardar(alertaCorrientes);
        repositorio.guardar(alertaLavalle);
        repositorio.guardar(alertaPueyrredon);
    }
*/
@Test
@Order(24)
void guardarCuestionarioPersonaJuridica() {
    Cuestionario cuestionario = new Cuestionario();
    cuestionario.setNombre("Cuestionario Persona Juridica");
    List<TipoRol> rol = new ArrayList<>();
    rol.add(TipoRol.PERSONA_JURIDICA);
    cuestionario.setRolesQueAcepta(rol);
    repositorio.guardar(cuestionario);

    Pregunta pregunta1 = new Pregunta("¿Cuál es la misión principal de su organización?",TipoPregunta.ABIERTA,null);
    repositorio.guardar(pregunta1);

    Pregunta pregunta2 = new Pregunta("¿Cómo se enteró de la existencia de este sistema?", TipoPregunta.UNICA,null);
    Opcion opcion1 = new Opcion("Internet");
    Opcion opcion2 = new Opcion("Recomendación de un familiar");
    Opcion opcion3 = new Opcion("Recomendación de un amigo");
    Opcion opcion4 = new Opcion("Otro");
    pregunta2.agregarOpcion(opcion1);
    pregunta2.agregarOpcion(opcion2);
    pregunta2.agregarOpcion(opcion3);
    pregunta2.agregarOpcion(opcion4);
    repositorio.guardar(opcion1);
    repositorio.guardar(opcion2);
    repositorio.guardar(opcion3);
    repositorio.guardar(opcion4);
    repositorio.guardar(pregunta2);

    Pregunta pregunta3 = new Pregunta("¿En qué rubros se desempeña su organización? (puede seleccionar más de una)", TipoPregunta.MULTIPLE,null);
    Opcion opcion5 = new Opcion("Tecnología");
    Opcion opcion6 = new Opcion("Salud");
    Opcion opcion7 = new Opcion("Otro");
    pregunta3.agregarOpcion(opcion5);
    pregunta3.agregarOpcion(opcion6);
    pregunta3.agregarOpcion(opcion7);
    repositorio.guardar(opcion5);
    repositorio.guardar(opcion6);
    repositorio.guardar(opcion7);
    repositorio.guardar(pregunta3);

    Pregunta pregunta4 = new Pregunta("¿Qué expectativas tiene de este sistema?",TipoPregunta.ABIERTA,null);
    repositorio.guardar(pregunta4);

    Pregunta pregunta5 = new Pregunta("Seleccione su tipo societario", TipoPregunta.UNICA,null);
    Opcion opcion8 = new Opcion("SA");
    Opcion opcion9 = new Opcion("SRL");
    Opcion opcion10 = new Opcion("Otro");
    pregunta5.agregarOpcion(opcion8);
    pregunta5.agregarOpcion(opcion9);
    pregunta5.agregarOpcion(opcion10);
    repositorio.guardar(opcion8);
    repositorio.guardar(opcion9);
    repositorio.guardar(opcion10);
    repositorio.guardar(pregunta5);

    Pregunta pregunta6 = new Pregunta("¿Qué resultados le gustaría alcanzar con su participación en este sistema? (puede seleccionar más de una)", TipoPregunta.MULTIPLE,null);
    Opcion opcion11 = new Opcion("Mejorar la calidad de vida de personas vulnerables");
    Opcion opcion12 = new Opcion("Promover la responsabilidad social de la organización");
    Opcion opcion13 = new Opcion("Establecer vínculos con otras entidades solidarias");
    Opcion opcion14 = new Opcion("Generar conciencia sobre la problemática alimentaria");
    Opcion opcion15 = new Opcion("Otro");
    pregunta6.agregarOpcion(opcion11);
    pregunta6.agregarOpcion(opcion12);
    pregunta6.agregarOpcion(opcion13);
    repositorio.guardar(opcion11);
    repositorio.guardar(opcion12);
    repositorio.guardar(opcion13);
    repositorio.guardar(opcion14);
    repositorio.guardar(opcion15);
    repositorio.guardar(pregunta6);

    cuestionario.agregarPreguntas(List.of(pregunta1, pregunta2, pregunta3, pregunta4, pregunta5, pregunta6));
    repositorio.guardar(cuestionario);
}

    @Test
    @Order(25)
    void obtenerDireccion() {
        Map<String, String> direccion = Nominative.obtenerDireccion("-34.63987327965148", "-58.4108092361999");
        System.out.println("Calle: " + direccion.get("calle"));
        System.out.println("Altura: " + direccion.get("altura"));
        System.out.println("Ciudad: " + direccion.get("ciudad"));
        System.out.println("Provincia: " + direccion.get("provincia"));
    }

    private Rubro obtenerRubroPorNombre(String nombreRubro) {
        return (Rubro) repositorio.buscarTodos("Rubro").stream()
                .filter(r -> nombreRubro.equals(((Rubro) r).getNombre()))
                .findFirst()
                .orElse(null);
    }

    @Test
    @Order(26)
    void getProvincia(){
        Provincia provincia = repositorioProvincia.buscarPorNombre("Buenos Aires","Provincia",Provincia.class);
        Ciudad ciudad = repositorioCiudad.buscarPrimeraCiudadPorProvinciaId(provincia.getId());
        System.out.println("ciudad: " + ciudad.getNombre());
    }

    private void guardarOfertas(Repositorio repositorio, List<Oferta> ofertas, String nombreRubro) {
        Rubro rubro = new Rubro(nombreRubro);
        repositorio.guardar(rubro);

        for (Oferta oferta : ofertas) {
            oferta.setRubro(rubro);
            repositorio.guardar(oferta);
        }
    }

    private Direccion crearDireccion(String provincia, CentroDeLaCiudad centro, String nombreCiudad, List<String> direccion) {
        Provincia nombreProvincia = (Provincia) repositorio.buscarTodos("Provincia").stream()
                .filter(prov -> provincia.equals(((Provincia) prov).getNombre()))
                .findFirst()
                .orElse(null);

        Ciudad ciudad = repositorio.buscarPorNombre(nombreCiudad,"Ciudad", Ciudad.class);

        String calle = direccion.get(0);
        String numero = direccion.get(1);
        String codigoPostal = direccion.get(2);
        String piso = direccion.size() > 3 ? direccion.get(3) : null; // piso opcional
        Direccion direccionFinal = new Direccion(calle, numero, piso, codigoPostal, ciudad, nombreProvincia);

        repositorio.guardar(direccionFinal);

        return direccionFinal;
    }

    private Heladera crearHeladera(ModeloDeHeladera modelo, PuntoEstrategico puntoEstrategico) {
        Heladera heladera = new Heladera(modelo, puntoEstrategico);

        this.cargarFechasYHoras(heladera);
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura(heladera);
        SensorDeMovimiento sensorDeMovimiento = new SensorDeMovimiento(heladera);
        repositorioHeladera.guardar(heladera);
        repositorio.guardar(sensorDeMovimiento);
        repositorio.guardar(sensorDeTemperatura);

        return heladera;
    }

    private PuntoEstrategico guardarPuntoEstrategicoEnCABA(String puntoNombre, String latitud, String longitud, List<String> direccion) {
        CentroDeLaCiudad centroDeCABA = repositorio.buscarPorNombre(
                "Buenos Aires","CentroDeLaCiudad",CentroDeLaCiudad.class);

        Ciudad ciudad = repositorio.buscarPorNombre("Buenos Aires","Ciudad",Ciudad.class);
        Provincia provincia = repositorio.buscarPorNombre("Buenos Aires","Provincia",Provincia.class);

        Direccion direccionCreada = this.crearDireccion(
                provincia.getNombre(),
                centroDeCABA,
                ciudad.getNombre(),
                direccion
        );
        PuntoEstrategico puntoEstrategico = new PuntoEstrategico(puntoNombre, latitud, longitud, direccionCreada);
        repositorio.guardar(direccionCreada);
        repositorio.guardar(puntoEstrategico);
        return puntoEstrategico;
    }

    private Vianda guardarVianda(String nombreComida, LocalDate fechaCaducidad, Double calorias, Double peso) {
    Vianda vianda = new Vianda();
    vianda.setNombreComida(nombreComida);
    vianda.setFechaCaducidad(fechaCaducidad);
    vianda.setCalorias(calorias);
    vianda.setPeso(peso);
    repositorio.guardar(vianda);
    return vianda;
}
    private Oferta obtenerOfertaPorNombre(String nombreOferta) {
        return (Oferta) repositorio.buscarTodos("Oferta").stream()
                .filter(o -> nombreOferta.equals(((Oferta) o).getNombre()))
                .findFirst()
                .orElse(null);
    }

    public void cargarFechasYHoras(Heladera heladera) {
        heladera.setFechasYHorasDejoDeEstarActiva(Arrays.asList(
                LocalDateTime.of(2022, 1, 10, 10, 0),
                LocalDateTime.of(2022, 2, 15, 12, 30),
                LocalDateTime.of(2022, 3, 20, 14, 45)
        ));
        heladera.setFechasYHorasVolvioAEstarActiva(Arrays.asList(
                LocalDateTime.of(2022, 1, 11, 9, 0),
                LocalDateTime.of(2022, 2, 16, 11, 30),
                LocalDateTime.of(2022, 3, 21, 13, 45)
        ));
        heladera.setFechasYHorasReubicada(Arrays.asList(
                LocalDateTime.of(2022, 4, 10, 10, 0),
                LocalDateTime.of(2022, 5, 15, 12, 30)
        ));
    }

    private void guardarCiudadesBuenosAires(){
        Provincia buenosAires = repositorio.buscarPorNombre("Buenos Aires", "Provincia", Provincia.class);

        List<Ciudad> ciudadesBuenosAires = Arrays.asList(
                new Ciudad("Bahía Blanca", new CentroDeLaCiudad("Bahía Blanca", "-38.715545", "-62.264346"), buenosAires),
                new Ciudad("Buenos Aires", new CentroDeLaCiudad("Buenos Aires", "-34.603722", "-58.381556"), buenosAires),
                new Ciudad("La Plata", new CentroDeLaCiudad("La Plata", "-34.921450", "-57.954533"), buenosAires),
                new Ciudad("Mar del Plata", new CentroDeLaCiudad("Mar del Plata", "-38.005477", "-57.542611"), buenosAires),
                new Ciudad("Tandil", new CentroDeLaCiudad("Tandil", "-37.321674", "-59.133162"), buenosAires),
                new Ciudad("Olavarría", new CentroDeLaCiudad("Olavarría", "-36.892728", "-60.322546"), buenosAires),
                new Ciudad("Junín", new CentroDeLaCiudad("Junín", "-34.585000", "-60.958000"), buenosAires),
                new Ciudad("Pergamino", new CentroDeLaCiudad("Pergamino", "-33.894500", "-60.573600"), buenosAires),
                new Ciudad("Necochea", new CentroDeLaCiudad("Necochea", "-38.556200", "-58.739600"), buenosAires),
                new Ciudad("Tres Arroyos", new CentroDeLaCiudad("Tres Arroyos", "-38.373100", "-60.279000"), buenosAires),
                new Ciudad("Chivilcoy", new CentroDeLaCiudad("Chivilcoy", "-34.896900", "-60.016700"), buenosAires),
                new Ciudad("San Nicolás", new CentroDeLaCiudad("San Nicolás", "-33.334200", "-60.226900"), buenosAires),
                new Ciudad("Zárate", new CentroDeLaCiudad("Zárate", "-34.098900", "-59.028600"), buenosAires),
                new Ciudad("Campana", new CentroDeLaCiudad("Campana", "-34.159200", "-58.959300"), buenosAires),
                new Ciudad("Luján", new CentroDeLaCiudad("Luján", "-34.570300", "-59.105000"), buenosAires),
                new Ciudad("Mercedes", new CentroDeLaCiudad("Mercedes", "-34.651500", "-59.430000"), buenosAires),
                new Ciudad("San Pedro", new CentroDeLaCiudad("San Pedro", "-33.679800", "-59.666500"), buenosAires),
                new Ciudad("Pinamar", new CentroDeLaCiudad("Pinamar", "-37.107200", "-56.861000"), buenosAires),
                new Ciudad("Villa Gesell", new CentroDeLaCiudad("Villa Gesell", "-37.263900", "-56.973000"), buenosAires)
        );

        for (Ciudad ciudad : ciudadesBuenosAires) {
                repositorio.guardar(ciudad.getCentroDeLaCiudad());
                repositorio.guardar(ciudad);
        }

    }

    private void guardarCiudadesCordoba(){
        Provincia cordoba = repositorio.buscarPorNombre("Córdoba", "Provincia", Provincia.class);
        List<Ciudad> ciudadesCordoba = Arrays.asList(
                new Ciudad("Córdoba", new CentroDeLaCiudad("Córdoba", "-31.420083", "-64.188776"), cordoba),
                new Ciudad("Villa Carlos Paz", new CentroDeLaCiudad("Villa Carlos Paz", "-31.424000", "-64.497000"), cordoba),
                new Ciudad("Río Cuarto", new CentroDeLaCiudad("Río Cuarto", "-33.123000", "-64.349000"), cordoba),
                new Ciudad("Villa María", new CentroDeLaCiudad("Villa María", "-32.407000", "-63.241000"), cordoba),
                new Ciudad("San Francisco", new CentroDeLaCiudad("San Francisco", "-31.427000", "-62.082000"), cordoba),
                new Ciudad("Alta Gracia", new CentroDeLaCiudad("Alta Gracia", "-31.667000", "-64.433000"), cordoba),
                new Ciudad("Río Tercero", new CentroDeLaCiudad("Río Tercero", "-32.173000", "-64.114000"), cordoba),
                new Ciudad("La Falda", new CentroDeLaCiudad("La Falda", "-31.082000", "-64.489000"), cordoba),
                new Ciudad("Cosquín", new CentroDeLaCiudad("Cosquín", "-31.245000", "-64.465000"), cordoba),
                new Ciudad("Jesús María", new CentroDeLaCiudad("Jesús María", "-30.981000", "-64.094000"), cordoba),
                new Ciudad("Villa Allende", new CentroDeLaCiudad("Villa Allende", "-31.294000", "-64.294000"), cordoba),
                new Ciudad("Arroyito", new CentroDeLaCiudad("Arroyito", "-31.420000", "-63.050000"), cordoba),
                new Ciudad("Villa Dolores", new CentroDeLaCiudad("Villa Dolores", "-31.945000", "-65.190000"), cordoba),
                new Ciudad("Deán Funes", new CentroDeLaCiudad("Deán Funes", "-30.420000", "-64.350000"), cordoba),
                new Ciudad("Capilla del Monte", new CentroDeLaCiudad("Capilla del Monte", "-30.855000", "-64.523000"), cordoba),
                new Ciudad("Cruz del Eje", new CentroDeLaCiudad("Cruz del Eje", "-30.726000", "-64.803000"), cordoba),
                new Ciudad("La Cumbre", new CentroDeLaCiudad("La Cumbre", "-30.982000", "-64.490000"), cordoba),
                new Ciudad("Villa General Belgrano", new CentroDeLaCiudad("Villa General Belgrano", "-31.972000", "-64.573000"), cordoba)
        );

        for (Ciudad ciudad : ciudadesCordoba) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }
    private void guardarCiudadesSanJuan(){
        Provincia sanJuan = repositorio.buscarPorNombre("San Juan", "Provincia", Provincia.class);
        List<Ciudad> ciudadesSanJuan = Arrays.asList(
                new Ciudad("San Juan", new CentroDeLaCiudad("San Juan", "-31.537500", "-68.536389"), sanJuan),
                new Ciudad("Rivadavia", new CentroDeLaCiudad("Rivadavia", "-31.533333", "-68.583333"), sanJuan),
                new Ciudad("Rawson", new CentroDeLaCiudad("Rawson", "-31.566667", "-68.516667"), sanJuan),
                new Ciudad("Chimbas", new CentroDeLaCiudad("Chimbas", "-31.500000", "-68.533333"), sanJuan),
                new Ciudad("Santa Lucía", new CentroDeLaCiudad("Santa Lucía", "-31.533333", "-68.500000"), sanJuan),
                new Ciudad("Pocito", new CentroDeLaCiudad("Pocito", "-31.650000", "-68.583333"), sanJuan),
                new Ciudad("Caucete", new CentroDeLaCiudad("Caucete", "-31.650000", "-68.283333"), sanJuan),
                new Ciudad("Jáchal", new CentroDeLaCiudad("Jáchal", "-30.233333", "-68.750000"), sanJuan),
                new Ciudad("Albardón", new CentroDeLaCiudad("Albardón", "-31.400000", "-68.466667"), sanJuan),
                new Ciudad("Angaco", new CentroDeLaCiudad("Angaco", "-31.450000", "-68.366667"), sanJuan),
                new Ciudad("San Martín", new CentroDeLaCiudad("San Martín", "-31.550000", "-68.400000"), sanJuan),
                new Ciudad("Sarmiento", new CentroDeLaCiudad("Sarmiento", "-31.600000", "-68.700000"), sanJuan),
                new Ciudad("Zonda", new CentroDeLaCiudad("Zonda", "-31.550000", "-68.700000"), sanJuan),
                new Ciudad("Ullum", new CentroDeLaCiudad("Ullum", "-31.533333", "-68.650000"), sanJuan),
                new Ciudad("Calingasta", new CentroDeLaCiudad("Calingasta", "-31.350000", "-69.333333"), sanJuan),
                new Ciudad("Iglesia", new CentroDeLaCiudad("Iglesia", "-30.400000", "-69.333333"), sanJuan),
                new Ciudad("Valle Fértil", new CentroDeLaCiudad("Valle Fértil", "-30.800000", "-67.583333"), sanJuan),
                new Ciudad("9 de Julio", new CentroDeLaCiudad("9 de Julio", "-31.550000", "-68.400000"), sanJuan)
        );

        for (Ciudad ciudad : ciudadesSanJuan) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesTierraDelFuego(){
        Provincia tierraDelFuego = repositorio.buscarPorNombre("Tierra del Fuego", "Provincia", Provincia.class);
        List<Ciudad> ciudadesTierraDelFuego = Arrays.asList(
                new Ciudad("Ushuaia", new CentroDeLaCiudad("Ushuaia", "-54.801912", "-68.302951"), tierraDelFuego),
                new Ciudad("Río Grande", new CentroDeLaCiudad("Río Grande", "-53.787700", "-67.709457"), tierraDelFuego),
                new Ciudad("Tolhuin", new CentroDeLaCiudad("Tolhuin", "-54.521800", "-67.191400"), tierraDelFuego)
        );

        for (Ciudad ciudad : ciudadesTierraDelFuego) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesJujuy(){
        Provincia jujuy = repositorio.buscarPorNombre("Jujuy", "Provincia", Provincia.class);
        List<Ciudad> ciudadesJujuy = Arrays.asList(
                new Ciudad("San Salvador de Jujuy", new CentroDeLaCiudad("San Salvador de Jujuy", "-24.185786", "-65.299477"), jujuy),
                new Ciudad("Palpalá", new CentroDeLaCiudad("Palpalá", "-24.256000", "-65.211000"), jujuy),
                new Ciudad("Perico", new CentroDeLaCiudad("Perico", "-24.382000", "-65.111000"), jujuy),
                new Ciudad("Libertador General San Martín", new CentroDeLaCiudad("Libertador General San Martín", "-23.806000", "-64.787000"), jujuy),
                new Ciudad("San Pedro de Jujuy", new CentroDeLaCiudad("San Pedro de Jujuy", "-24.231000", "-64.866000"), jujuy),
                new Ciudad("El Carmen", new CentroDeLaCiudad("El Carmen", "-24.390000", "-65.259000"), jujuy),
                new Ciudad("La Quiaca", new CentroDeLaCiudad("La Quiaca", "-22.102000", "-65.594000"), jujuy),
                new Ciudad("Tilcara", new CentroDeLaCiudad("Tilcara", "-23.577000", "-65.350000"), jujuy),
                new Ciudad("Humahuaca", new CentroDeLaCiudad("Humahuaca", "-23.205000", "-65.349000"), jujuy),
                new Ciudad("Maimará", new CentroDeLaCiudad("Maimará", "-23.620000", "-65.400000"), jujuy),
                new Ciudad("Purmamarca", new CentroDeLaCiudad("Purmamarca", "-23.742000", "-65.496000"), jujuy),
                new Ciudad("Abra Pampa", new CentroDeLaCiudad("Abra Pampa", "-22.720000", "-65.697000"), jujuy),
                new Ciudad("Calilegua", new CentroDeLaCiudad("Calilegua", "-23.776000", "-64.771000"), jujuy),
                new Ciudad("Monterrico", new CentroDeLaCiudad("Monterrico", "-24.400000", "-65.200000"), jujuy),
                new Ciudad("Yala", new CentroDeLaCiudad("Yala", "-24.183000", "-65.300000"), jujuy),
                new Ciudad("San Antonio", new CentroDeLaCiudad("San Antonio", "-24.300000", "-65.300000"), jujuy),
                new Ciudad("La Esperanza", new CentroDeLaCiudad("La Esperanza", "-24.250000", "-64.800000"), jujuy),
                new Ciudad("Caimancito", new CentroDeLaCiudad("Caimancito", "-23.780000", "-64.620000"), jujuy)
        );

        for (Ciudad ciudad : ciudadesJujuy) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesNeuquen(){
        Provincia neuquen = repositorio.buscarPorNombre("Neuquén", "Provincia", Provincia.class);
        List<Ciudad> ciudadesNeuquen = Arrays.asList(
                new Ciudad("Neuquén", new CentroDeLaCiudad("Neuquén", "-38.951611", "-68.059230"), neuquen),
                new Ciudad("San Martín de los Andes", new CentroDeLaCiudad("San Martín de los Andes", "-40.157900", "-71.353000"), neuquen),
                new Ciudad("Zapala", new CentroDeLaCiudad("Zapala", "-38.899200", "-70.054400"), neuquen),
                new Ciudad("Plottier", new CentroDeLaCiudad("Plottier", "-38.970000", "-68.233000"), neuquen),
                new Ciudad("Centenario", new CentroDeLaCiudad("Centenario", "-38.796000", "-68.150000"), neuquen),
                new Ciudad("Cutral Có", new CentroDeLaCiudad("Cutral Có", "-38.940000", "-69.230000"), neuquen),
                new Ciudad("Plaza Huincul", new CentroDeLaCiudad("Plaza Huincul", "-38.930000", "-69.230000"), neuquen),
                new Ciudad("Villa La Angostura", new CentroDeLaCiudad("Villa La Angostura", "-40.763000", "-71.646000"), neuquen),
                new Ciudad("Chos Malal", new CentroDeLaCiudad("Chos Malal", "-37.378000", "-70.270000"), neuquen),
                new Ciudad("Junín de los Andes", new CentroDeLaCiudad("Junín de los Andes", "-39.933000", "-71.066000"), neuquen),
                new Ciudad("Rincón de los Sauces", new CentroDeLaCiudad("Rincón de los Sauces", "-37.390000", "-68.925000"), neuquen),
                new Ciudad("Añelo", new CentroDeLaCiudad("Añelo", "-38.350000", "-68.780000"), neuquen),
                new Ciudad("San Patricio del Chañar", new CentroDeLaCiudad("San Patricio del Chañar", "-38.600000", "-68.583000"), neuquen),
                new Ciudad("Loncopué", new CentroDeLaCiudad("Loncopué", "-38.072000", "-70.616000"), neuquen),
                new Ciudad("Aluminé", new CentroDeLaCiudad("Aluminé", "-39.236000", "-70.918000"), neuquen),
                new Ciudad("Las Lajas", new CentroDeLaCiudad("Las Lajas", "-38.520000", "-70.370000"), neuquen),
                new Ciudad("Villa Pehuenia", new CentroDeLaCiudad("Villa Pehuenia", "-38.880000", "-71.183000"), neuquen),
                new Ciudad("El Chocón", new CentroDeLaCiudad("El Chocón", "-39.266000", "-68.750000"), neuquen)
        );

        for (Ciudad ciudad : ciudadesNeuquen) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesMisiones(){
        Provincia misiones = repositorio.buscarPorNombre("Misiones", "Provincia", Provincia.class);
        List<Ciudad> ciudadesMisiones = Arrays.asList(
                new Ciudad("Posadas", new CentroDeLaCiudad("Posadas", "-27.367084", "-55.896080"), misiones),
                new Ciudad("Oberá", new CentroDeLaCiudad("Oberá", "-27.487000", "-55.119000"), misiones),
                new Ciudad("Eldorado", new CentroDeLaCiudad("Eldorado", "-26.408000", "-54.694000"), misiones),
                new Ciudad("Puerto Iguazú", new CentroDeLaCiudad("Puerto Iguazú", "-25.611000", "-54.551000"), misiones),
                new Ciudad("Leandro N. Alem", new CentroDeLaCiudad("Leandro N. Alem", "-27.622000", "-55.324000"), misiones),
                new Ciudad("San Vicente", new CentroDeLaCiudad("San Vicente", "-26.616000", "-54.135000"), misiones),
                new Ciudad("Apóstoles", new CentroDeLaCiudad("Apóstoles", "-27.914000", "-55.755000"), misiones),
                new Ciudad("Montecarlo", new CentroDeLaCiudad("Montecarlo", "-26.566000", "-54.757000"), misiones),
                new Ciudad("Jardín América", new CentroDeLaCiudad("Jardín América", "-27.043000", "-55.230000"), misiones),
                new Ciudad("San Pedro", new CentroDeLaCiudad("San Pedro", "-26.622000", "-54.108000"), misiones),
                new Ciudad("Puerto Rico", new CentroDeLaCiudad("Puerto Rico", "-26.795000", "-55.024000"), misiones),
                new Ciudad("Aristóbulo del Valle", new CentroDeLaCiudad("Aristóbulo del Valle", "-27.097000", "-54.896000"), misiones),
                new Ciudad("Candelaria", new CentroDeLaCiudad("Candelaria", "-27.459000", "-55.742000"), misiones),
                new Ciudad("San Javier", new CentroDeLaCiudad("San Javier", "-27.882000", "-55.137000"), misiones),
                new Ciudad("Garupá", new CentroDeLaCiudad("Garupá", "-27.481000", "-55.829000"), misiones),
                new Ciudad("Wanda", new CentroDeLaCiudad("Wanda", "-25.975000", "-54.573000"), misiones),
                new Ciudad("Capioví", new CentroDeLaCiudad("Capioví", "-26.927000", "-55.057000"), misiones),
                new Ciudad("San Ignacio", new CentroDeLaCiudad("San Ignacio", "-27.256000", "-55.530000"), misiones)
        );

        for (Ciudad ciudad : ciudadesMisiones) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesSantaFe(){
        Provincia santaFe = repositorio.buscarPorNombre("Santa Fe", "Provincia", Provincia.class);
        List<Ciudad> ciudadesSantaFe = Arrays.asList(
                new Ciudad("Santa Fe", new CentroDeLaCiudad("Santa Fe", "-31.623000", "-60.700000"), santaFe),
                new Ciudad("Rosario", new CentroDeLaCiudad("Rosario", "-32.957000", "-60.639000"), santaFe),
                new Ciudad("Rafaela", new CentroDeLaCiudad("Rafaela", "-31.250000", "-61.500000"), santaFe),
                new Ciudad("Venado Tuerto", new CentroDeLaCiudad("Venado Tuerto", "-33.745000", "-61.968000"), santaFe),
                new Ciudad("Reconquista", new CentroDeLaCiudad("Reconquista", "-29.144000", "-59.650000"), santaFe),
                new Ciudad("Villa Gobernador Gálvez", new CentroDeLaCiudad("Villa Gobernador Gálvez", "-33.020000", "-60.640000"), santaFe),
                new Ciudad("San Lorenzo", new CentroDeLaCiudad("San Lorenzo", "-32.750000", "-60.733000"), santaFe),
                new Ciudad("Esperanza", new CentroDeLaCiudad("Esperanza", "-31.448000", "-60.930000"), santaFe),
                new Ciudad("Sunchales", new CentroDeLaCiudad("Sunchales", "-30.944000", "-61.561000"), santaFe),
                new Ciudad("Cañada de Gómez", new CentroDeLaCiudad("Cañada de Gómez", "-32.816000", "-61.400000"), santaFe),
                new Ciudad("San Justo", new CentroDeLaCiudad("San Justo", "-30.780000", "-60.583000"), santaFe),
                new Ciudad("Casilda", new CentroDeLaCiudad("Casilda", "-33.040000", "-61.170000"), santaFe),
                new Ciudad("Arroyo Seco", new CentroDeLaCiudad("Arroyo Seco", "-33.150000", "-60.516000"), santaFe),
                new Ciudad("Funes", new CentroDeLaCiudad("Funes", "-32.933000", "-60.816000"), santaFe),
                new Ciudad("Gálvez", new CentroDeLaCiudad("Gálvez", "-32.030000", "-61.220000"), santaFe),
                new Ciudad("Tostado", new CentroDeLaCiudad("Tostado", "-29.230000", "-61.766000"), santaFe),
                new Ciudad("San Javier", new CentroDeLaCiudad("San Javier", "-30.580000", "-59.933000"), santaFe),
                new Ciudad("Coronda", new CentroDeLaCiudad("Coronda", "-31.967000", "-60.917000"), santaFe)
        );

        for (Ciudad ciudad : ciudadesSantaFe) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesCorrientes(){
        Provincia corrientes = repositorio.buscarPorNombre("Corrientes", "Provincia", Provincia.class);
        List<Ciudad> ciudadesCorrientes = Arrays.asList(
                new Ciudad("Corrientes", new CentroDeLaCiudad("Corrientes", "-27.480600", "-58.834100"), corrientes),
                new Ciudad("Goya", new CentroDeLaCiudad("Goya", "-29.140000", "-59.265000"), corrientes),
                new Ciudad("Paso de los Libres", new CentroDeLaCiudad("Paso de los Libres", "-29.710000", "-57.090000"), corrientes),
                new Ciudad("Mercedes", new CentroDeLaCiudad("Mercedes", "-29.180000", "-58.080000"), corrientes),
                new Ciudad("Curuzú Cuatiá", new CentroDeLaCiudad("Curuzú Cuatiá", "-29.790000", "-58.050000"), corrientes),
                new Ciudad("Santo Tomé", new CentroDeLaCiudad("Santo Tomé", "-28.550000", "-56.040000"), corrientes),
                new Ciudad("Esquina", new CentroDeLaCiudad("Esquina", "-30.010000", "-59.530000"), corrientes),
                new Ciudad("Bella Vista", new CentroDeLaCiudad("Bella Vista", "-28.510000", "-59.050000"), corrientes),
                new Ciudad("Monte Caseros", new CentroDeLaCiudad("Monte Caseros", "-30.250000", "-57.640000"), corrientes),
                new Ciudad("Ituzaingó", new CentroDeLaCiudad("Ituzaingó", "-27.580000", "-56.680000"), corrientes),
                new Ciudad("Saladas", new CentroDeLaCiudad("Saladas", "-28.250000", "-58.620000"), corrientes),
                new Ciudad("San Luis del Palmar", new CentroDeLaCiudad("San Luis del Palmar", "-27.510000", "-58.550000"), corrientes),
                new Ciudad("Santa Lucía", new CentroDeLaCiudad("Santa Lucía", "-28.980000", "-59.100000"), corrientes),
                new Ciudad("Virasoro", new CentroDeLaCiudad("Virasoro", "-28.050000", "-56.030000"), corrientes),
                new Ciudad("Empedrado", new CentroDeLaCiudad("Empedrado", "-27.950000", "-58.810000"), corrientes),
                new Ciudad("San Roque", new CentroDeLaCiudad("San Roque", "-28.570000", "-58.710000"), corrientes),
                new Ciudad("Mocoretá", new CentroDeLaCiudad("Mocoretá", "-30.620000", "-57.960000"), corrientes),
                new Ciudad("Sauce", new CentroDeLaCiudad("Sauce", "-30.080000", "-58.510000"), corrientes)
        );

        for (Ciudad ciudad : ciudadesCorrientes) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesLaRioja(){
        Provincia laRioja = repositorio.buscarPorNombre("La Rioja", "Provincia", Provincia.class);
        List<Ciudad> ciudadesLaRioja = Arrays.asList(
                new Ciudad("La Rioja", new CentroDeLaCiudad("La Rioja", "-29.411050", "-66.850670"), laRioja),
                new Ciudad("Chilecito", new CentroDeLaCiudad("Chilecito", "-29.162000", "-67.497000"), laRioja),
                new Ciudad("Arauco", new CentroDeLaCiudad("Arauco", "-28.573000", "-66.800000"), laRioja),
                new Ciudad("Chamical", new CentroDeLaCiudad("Chamical", "-30.360000", "-66.313000"), laRioja),
                new Ciudad("Chepes", new CentroDeLaCiudad("Chepes", "-31.350000", "-66.600000"), laRioja),
                new Ciudad("Villa Unión", new CentroDeLaCiudad("Villa Unión", "-29.320000", "-68.220000"), laRioja),
                new Ciudad("Aimogasta", new CentroDeLaCiudad("Aimogasta", "-28.560000", "-66.800000"), laRioja),
                new Ciudad("Famatina", new CentroDeLaCiudad("Famatina", "-28.916000", "-67.500000"), laRioja),
                new Ciudad("Sanagasta", new CentroDeLaCiudad("Sanagasta", "-29.350000", "-67.000000"), laRioja),
                new Ciudad("Olta", new CentroDeLaCiudad("Olta", "-30.616000", "-66.283000"), laRioja)
        );

        for (Ciudad ciudad : ciudadesLaRioja) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesChubut(){
        Provincia chubut = repositorio.buscarPorNombre("Chubut", "Provincia", Provincia.class);
        List<Ciudad> ciudadesChubut = Arrays.asList(
                new Ciudad("Rawson", new CentroDeLaCiudad("Rawson", "-43.300000", "-65.100000"), chubut),
                new Ciudad("Trelew", new CentroDeLaCiudad("Trelew", "-43.250000", "-65.310000"), chubut),
                new Ciudad("Puerto Madryn", new CentroDeLaCiudad("Puerto Madryn", "-42.770000", "-65.040000"), chubut),
                new Ciudad("Esquel", new CentroDeLaCiudad("Esquel", "-42.900000", "-71.316700"), chubut),
                new Ciudad("Comodoro Rivadavia", new CentroDeLaCiudad("Comodoro Rivadavia", "-45.866700", "-67.500000"), chubut),
                new Ciudad("Sarmiento", new CentroDeLaCiudad("Sarmiento", "-45.600000", "-69.083300"), chubut),
                new Ciudad("Gaiman", new CentroDeLaCiudad("Gaiman", "-43.283300", "-65.483300"), chubut),
                new Ciudad("Dolavon", new CentroDeLaCiudad("Dolavon", "-43.300000", "-65.700000"), chubut),
                new Ciudad("Lago Puelo", new CentroDeLaCiudad("Lago Puelo", "-42.080000", "-71.616700"), chubut),
                new Ciudad("El Maitén", new CentroDeLaCiudad("El Maitén", "-42.050000", "-71.166700"), chubut)
        );

        for (Ciudad ciudad : ciudadesChubut) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesSalta(){
        Provincia salta = repositorio.buscarPorNombre("Salta", "Provincia", Provincia.class);
        List<Ciudad> ciudadesSalta = Arrays.asList(
                new Ciudad("Salta", new CentroDeLaCiudad("Salta", "-24.782932", "-65.423197"), salta),
                new Ciudad("San Ramón de la Nueva Orán", new CentroDeLaCiudad("San Ramón de la Nueva Orán", "-23.137000", "-64.324000"), salta),
                new Ciudad("Tartagal", new CentroDeLaCiudad("Tartagal", "-22.516000", "-63.800000"), salta),
                new Ciudad("General Güemes", new CentroDeLaCiudad("General Güemes", "-24.666000", "-65.050000"), salta),
                new Ciudad("Cafayate", new CentroDeLaCiudad("Cafayate", "-26.083000", "-65.966000"), salta),
                new Ciudad("Metán", new CentroDeLaCiudad("Metán", "-25.500000", "-64.983000"), salta),
                new Ciudad("Rosario de la Frontera", new CentroDeLaCiudad("Rosario de la Frontera", "-25.783000", "-64.966000"), salta),
                new Ciudad("San José de Metán", new CentroDeLaCiudad("San José de Metán", "-25.500000", "-64.983000"), salta),
                new Ciudad("San Antonio de los Cobres", new CentroDeLaCiudad("San Antonio de los Cobres", "-24.216000", "-66.316000"), salta),
                new Ciudad("Cachi", new CentroDeLaCiudad("Cachi", "-25.116000", "-66.166000"), salta)
        );

        for (Ciudad ciudad : ciudadesSalta) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesLaPampa(){
        Provincia laPampa = repositorio.buscarPorNombre("La Pampa", "Provincia", Provincia.class);
        List<Ciudad> ciudadesLaPampa = Arrays.asList(
                new Ciudad("Santa Rosa", new CentroDeLaCiudad("Santa Rosa", "-36.620000", "-64.290000"), laPampa),
                new Ciudad("General Pico", new CentroDeLaCiudad("General Pico", "-35.660000", "-63.750000"), laPampa),
                new Ciudad("Toay", new CentroDeLaCiudad("Toay", "-36.670000", "-64.380000"), laPampa),
                new Ciudad("Realicó", new CentroDeLaCiudad("Realicó", "-35.040000", "-64.240000"), laPampa),
                new Ciudad("General Acha", new CentroDeLaCiudad("General Acha", "-37.370000", "-64.600000"), laPampa),
                new Ciudad("Victorica", new CentroDeLaCiudad("Victorica", "-36.220000", "-65.430000"), laPampa),
                new Ciudad("Eduardo Castex", new CentroDeLaCiudad("Eduardo Castex", "-35.920000", "-64.290000"), laPampa),
                new Ciudad("Macachín", new CentroDeLaCiudad("Macachín", "-37.130000", "-63.680000"), laPampa),
                new Ciudad("25 de Mayo", new CentroDeLaCiudad("25 de Mayo", "-37.790000", "-67.680000"), laPampa),
                new Ciudad("Intendente Alvear", new CentroDeLaCiudad("Intendente Alvear", "-35.230000", "-63.580000"), laPampa)
        );

        for (Ciudad ciudad : ciudadesLaPampa) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesFormosa(){
        Provincia formosa = repositorio.buscarPorNombre("Formosa", "Provincia", Provincia.class);
        List<Ciudad> ciudadesFormosa = Arrays.asList(
                new Ciudad("Formosa", new CentroDeLaCiudad("Formosa", "-26.184900", "-58.173100"), formosa),
                new Ciudad("Clorinda", new CentroDeLaCiudad("Clorinda", "-25.284600", "-57.718600"), formosa),
                new Ciudad("Pirané", new CentroDeLaCiudad("Pirané", "-25.732800", "-59.108300"), formosa),
                new Ciudad("El Colorado", new CentroDeLaCiudad("El Colorado", "-26.308300", "-59.372200"), formosa),
                new Ciudad("Laguna Blanca", new CentroDeLaCiudad("Laguna Blanca", "-25.123300", "-58.260000"), formosa),
                new Ciudad("Ingeniero Juárez", new CentroDeLaCiudad("Ingeniero Juárez", "-23.897800", "-61.849400"), formosa),
                new Ciudad("Las Lomitas", new CentroDeLaCiudad("Las Lomitas", "-24.709200", "-60.593300"), formosa),
                new Ciudad("Ibarreta", new CentroDeLaCiudad("Ibarreta", "-25.214200", "-59.858300"), formosa),
                new Ciudad("Comandante Fontana", new CentroDeLaCiudad("Comandante Fontana", "-25.333300", "-59.683300"), formosa),
                new Ciudad("General Belgrano", new CentroDeLaCiudad("General Belgrano", "-25.066700", "-58.416700"), formosa)
        );

        for (Ciudad ciudad : ciudadesFormosa) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesChaco(){
        Provincia chaco = repositorio.buscarPorNombre("Chaco", "Provincia", Provincia.class);
        List<Ciudad> ciudadesChaco = Arrays.asList(
                new Ciudad("Resistencia", new CentroDeLaCiudad("Resistencia", "-27.451600", "-58.986700"), chaco),
                new Ciudad("Presidencia Roque Sáenz Peña", new CentroDeLaCiudad("Presidencia Roque Sáenz Peña", "-26.785200", "-60.438800"), chaco),
                new Ciudad("Villa Ángela", new CentroDeLaCiudad("Villa Ángela", "-27.573300", "-60.715000"), chaco),
                new Ciudad("Charata", new CentroDeLaCiudad("Charata", "-27.214200", "-61.187500"), chaco),
                new Ciudad("General José de San Martín", new CentroDeLaCiudad("General José de San Martín", "-26.537800", "-59.341700"), chaco),
                new Ciudad("Juan José Castelli", new CentroDeLaCiudad("Juan José Castelli", "-25.946700", "-60.617800"), chaco),
                new Ciudad("Las Breñas", new CentroDeLaCiudad("Las Breñas", "-27.089200", "-61.083300"), chaco),
                new Ciudad("Machagai", new CentroDeLaCiudad("Machagai", "-26.925000", "-60.050000"), chaco),
                new Ciudad("Quitilipi", new CentroDeLaCiudad("Quitilipi", "-26.869200", "-60.216700"), chaco),
                new Ciudad("Barranqueras", new CentroDeLaCiudad("Barranqueras", "-27.481700", "-58.939200"), chaco)
        );

        for (Ciudad ciudad : ciudadesChaco) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesSantaCruz(){
        Provincia santaCruz = repositorio.buscarPorNombre("Santa Cruz", "Provincia", Provincia.class);
        List<Ciudad> ciudadesSantaCruz = Arrays.asList(
                new Ciudad("Río Gallegos", new CentroDeLaCiudad("Río Gallegos", "-51.623000", "-69.216000"), santaCruz),
                new Ciudad("Caleta Olivia", new CentroDeLaCiudad("Caleta Olivia", "-46.439000", "-67.528000"), santaCruz),
                new Ciudad("El Calafate", new CentroDeLaCiudad("El Calafate", "-50.337000", "-72.264000"), santaCruz),
                new Ciudad("Pico Truncado", new CentroDeLaCiudad("Pico Truncado", "-46.793000", "-67.959000"), santaCruz),
                new Ciudad("Puerto Deseado", new CentroDeLaCiudad("Puerto Deseado", "-47.750000", "-65.900000"), santaCruz),
                new Ciudad("Las Heras", new CentroDeLaCiudad("Las Heras", "-46.542000", "-68.935000"), santaCruz),
                new Ciudad("Perito Moreno", new CentroDeLaCiudad("Perito Moreno", "-46.590000", "-70.930000"), santaCruz),
                new Ciudad("Puerto San Julián", new CentroDeLaCiudad("Puerto San Julián", "-49.306000", "-67.726000"), santaCruz),
                new Ciudad("Gobernador Gregores", new CentroDeLaCiudad("Gobernador Gregores", "-48.750000", "-70.250000"), santaCruz),
                new Ciudad("Los Antiguos", new CentroDeLaCiudad("Los Antiguos", "-46.550000", "-71.616000"), santaCruz)
        );

        for (Ciudad ciudad : ciudadesSantaCruz) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesCatamarca(){
        Provincia catamarca = repositorio.buscarPorNombre("Catamarca", "Provincia", Provincia.class);
        List<Ciudad> ciudadesCatamarca = Arrays.asList(
                new Ciudad("San Fernando del Valle de Catamarca", new CentroDeLaCiudad("San Fernando del Valle de Catamarca", "-28.469600", "-65.785200"), catamarca),
                new Ciudad("Valle Viejo", new CentroDeLaCiudad("Valle Viejo", "-28.469600", "-65.785200"), catamarca),
                new Ciudad("Belén", new CentroDeLaCiudad("Belén", "-27.649600", "-67.031000"), catamarca),
                new Ciudad("Tinogasta", new CentroDeLaCiudad("Tinogasta", "-28.063600", "-67.564200"), catamarca),
                new Ciudad("Santa María", new CentroDeLaCiudad("Santa María", "-26.698300", "-66.046000"), catamarca),
                new Ciudad("Andalgalá", new CentroDeLaCiudad("Andalgalá", "-27.574200", "-66.316700"), catamarca),
                new Ciudad("Recreo", new CentroDeLaCiudad("Recreo", "-29.280000", "-65.060000"), catamarca),
                new Ciudad("San Isidro", new CentroDeLaCiudad("San Isidro", "-28.469600", "-65.785200"), catamarca),
                new Ciudad("Fiambalá", new CentroDeLaCiudad("Fiambalá", "-27.689600", "-67.616700"), catamarca),
                new Ciudad("Saujil", new CentroDeLaCiudad("Saujil", "-27.700000", "-66.800000"), catamarca)
        );

        for (Ciudad ciudad : ciudadesCatamarca) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesSanLuis(){
        Provincia sanLuis = repositorio.buscarPorNombre("San Luis", "Provincia", Provincia.class);
        List<Ciudad> ciudadesSanLuis = Arrays.asList(
                new Ciudad("San Luis", new CentroDeLaCiudad("San Luis", "-33.295000", "-66.335600"), sanLuis),
                new Ciudad("Villa Mercedes", new CentroDeLaCiudad("Villa Mercedes", "-33.675000", "-65.457800"), sanLuis),
                new Ciudad("Merlo", new CentroDeLaCiudad("Merlo", "-32.344200", "-65.013300"), sanLuis),
                new Ciudad("La Punta", new CentroDeLaCiudad("La Punta", "-33.200000", "-66.300000"), sanLuis),
                new Ciudad("Juana Koslay", new CentroDeLaCiudad("Juana Koslay", "-33.300000", "-66.250000"), sanLuis),
                new Ciudad("Santa Rosa del Conlara", new CentroDeLaCiudad("Santa Rosa del Conlara", "-32.350000", "-65.200000"), sanLuis),
                new Ciudad("La Toma", new CentroDeLaCiudad("La Toma", "-33.050000", "-65.616700"), sanLuis),
                new Ciudad("Justo Daract", new CentroDeLaCiudad("Justo Daract", "-33.850000", "-65.183300"), sanLuis),
                new Ciudad("Concarán", new CentroDeLaCiudad("Concarán", "-32.550000", "-65.250000"), sanLuis),
                new Ciudad("Candelaria", new CentroDeLaCiudad("Candelaria", "-32.016700", "-65.566700"), sanLuis)
        );

        for (Ciudad ciudad : ciudadesSanLuis) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesMendoza(){
        Provincia mendoza = repositorio.buscarPorNombre("Mendoza", "Provincia", Provincia.class);
        List<Ciudad> ciudadesMendoza = Arrays.asList(
                new Ciudad("Mendoza", new CentroDeLaCiudad("Mendoza", "-32.890000", "-68.840000"), mendoza),
                new Ciudad("San Rafael", new CentroDeLaCiudad("San Rafael", "-34.617000", "-68.330000"), mendoza),
                new Ciudad("Godoy Cruz", new CentroDeLaCiudad("Godoy Cruz", "-32.920000", "-68.830000"), mendoza),
                new Ciudad("Luján de Cuyo", new CentroDeLaCiudad("Luján de Cuyo", "-33.050000", "-68.880000"), mendoza),
                new Ciudad("Maipú", new CentroDeLaCiudad("Maipú", "-32.980000", "-68.780000"), mendoza),
                new Ciudad("Malargüe", new CentroDeLaCiudad("Malargüe", "-35.470000", "-69.580000"), mendoza),
                new Ciudad("Tunuyán", new CentroDeLaCiudad("Tunuyán", "-33.580000", "-69.020000"), mendoza),
                new Ciudad("Rivadavia", new CentroDeLaCiudad("Rivadavia", "-33.180000", "-68.540000"), mendoza),
                new Ciudad("San Martín", new CentroDeLaCiudad("San Martín", "-33.080000", "-68.470000"), mendoza),
                new Ciudad("Las Heras", new CentroDeLaCiudad("Las Heras", "-32.830000", "-68.800000"), mendoza)
        );

        for (Ciudad ciudad : ciudadesMendoza) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesTucuman(){
        Provincia tucuman = repositorio.buscarPorNombre("Tucumán", "Provincia", Provincia.class);
        List<Ciudad> ciudadesTucuman = Arrays.asList(
                new Ciudad("San Miguel de Tucumán", new CentroDeLaCiudad("San Miguel de Tucumán", "-26.824100", "-65.222600"), tucuman),
                new Ciudad("Tafí Viejo", new CentroDeLaCiudad("Tafí Viejo", "-26.732500", "-65.258800"), tucuman),
                new Ciudad("Yerba Buena", new CentroDeLaCiudad("Yerba Buena", "-26.816700", "-65.316700"), tucuman),
                new Ciudad("Concepción", new CentroDeLaCiudad("Concepción", "-27.343300", "-65.596100"), tucuman),
                new Ciudad("Banda del Río Salí", new CentroDeLaCiudad("Banda del Río Salí", "-26.866700", "-65.166700"), tucuman),
                new Ciudad("Aguilares", new CentroDeLaCiudad("Aguilares", "-27.433300", "-65.616700"), tucuman),
                new Ciudad("Monteros", new CentroDeLaCiudad("Monteros", "-27.167800", "-65.500000"), tucuman),
                new Ciudad("Famaillá", new CentroDeLaCiudad("Famaillá", "-27.050000", "-65.400000"), tucuman),
                new Ciudad("Lules", new CentroDeLaCiudad("Lules", "-26.933300", "-65.333300"), tucuman),
                new Ciudad("Bella Vista", new CentroDeLaCiudad("Bella Vista", "-27.033300", "-65.300000"), tucuman)
        );

        for (Ciudad ciudad : ciudadesTucuman) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesEntreRios(){
        Provincia entreRios = repositorio.buscarPorNombre("Entre Ríos", "Provincia", Provincia.class);
        List<Ciudad> ciudadesEntreRios = Arrays.asList(
                new Ciudad("Paraná", new CentroDeLaCiudad("Paraná", "-31.733300", "-60.533300"), entreRios),
                new Ciudad("Concordia", new CentroDeLaCiudad("Concordia", "-31.400000", "-58.016700"), entreRios),
                new Ciudad("Gualeguaychú", new CentroDeLaCiudad("Gualeguaychú", "-33.000000", "-58.516700"), entreRios),
                new Ciudad("Gualeguay", new CentroDeLaCiudad("Gualeguay", "-33.141700", "-59.309200"), entreRios),
                new Ciudad("Villaguay", new CentroDeLaCiudad("Villaguay", "-31.866700", "-59.016700"), entreRios),
                new Ciudad("Victoria", new CentroDeLaCiudad("Victoria", "-32.618900", "-60.154700"), entreRios),
                new Ciudad("Colón", new CentroDeLaCiudad("Colón", "-32.223900", "-58.143100"), entreRios),
                new Ciudad("San José", new CentroDeLaCiudad("San José", "-32.210000", "-58.120000"), entreRios),
                new Ciudad("La Paz", new CentroDeLaCiudad("La Paz", "-30.744200", "-59.645800"), entreRios),
                new Ciudad("Crespo", new CentroDeLaCiudad("Crespo", "-32.030000", "-60.306700"), entreRios)
        );

        for (Ciudad ciudad : ciudadesEntreRios) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesRioNegro(){
        Provincia rioNegro = repositorio.buscarPorNombre("Río Negro", "Provincia", Provincia.class);
        List<Ciudad> ciudadesRioNegro = Arrays.asList(
                new Ciudad("Viedma", new CentroDeLaCiudad("Viedma", "-40.813450", "-62.996680"), rioNegro),
                new Ciudad("San Carlos de Bariloche", new CentroDeLaCiudad("San Carlos de Bariloche", "-41.145570", "-71.308220"), rioNegro),
                new Ciudad("General Roca", new CentroDeLaCiudad("General Roca", "-39.033330", "-67.583330"), rioNegro),
                new Ciudad("Cipolletti", new CentroDeLaCiudad("Cipolletti", "-38.933920", "-67.990000"), rioNegro),
                new Ciudad("Villa Regina", new CentroDeLaCiudad("Villa Regina", "-39.100000", "-67.066670"), rioNegro),
                new Ciudad("Río Colorado", new CentroDeLaCiudad("Río Colorado", "-39.016670", "-64.083330"), rioNegro),
                new Ciudad("Allen", new CentroDeLaCiudad("Allen", "-38.977220", "-67.827780"), rioNegro),
                new Ciudad("Catriel", new CentroDeLaCiudad("Catriel", "-37.879170", "-67.795830"), rioNegro),
                new Ciudad("Choele Choel", new CentroDeLaCiudad("Choele Choel", "-39.266670", "-65.666670"), rioNegro),
                new Ciudad("El Bolsón", new CentroDeLaCiudad("El Bolsón", "-41.966670", "-71.533330"), rioNegro)
        );

        for (Ciudad ciudad : ciudadesRioNegro) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }

    private void guardarCiudadesSantiagoDelEstero(){
        Provincia santiagoDelEstero = repositorio.buscarPorNombre("Santiago del Estero", "Provincia", Provincia.class);
        List<Ciudad> ciudadesSantiagoDelEstero = Arrays.asList(
                new Ciudad("Santiago del Estero", new CentroDeLaCiudad("Santiago del Estero", "-27.783357", "-64.266942"), santiagoDelEstero),
                new Ciudad("La Banda", new CentroDeLaCiudad("La Banda", "-27.733330", "-64.250000"), santiagoDelEstero),
                new Ciudad("Termas de Río Hondo", new CentroDeLaCiudad("Termas de Río Hondo", "-27.496000", "-64.859000"), santiagoDelEstero),
                new Ciudad("Añatuya", new CentroDeLaCiudad("Añatuya", "-28.460000", "-62.833300"), santiagoDelEstero),
                new Ciudad("Quimilí", new CentroDeLaCiudad("Quimilí", "-27.633300", "-62.416700"), santiagoDelEstero),
                new Ciudad("Frías", new CentroDeLaCiudad("Frías", "-28.633300", "-65.150000"), santiagoDelEstero),
                new Ciudad("Fernández", new CentroDeLaCiudad("Fernández", "-27.916700", "-63.883300"), santiagoDelEstero),
                new Ciudad("Loreto", new CentroDeLaCiudad("Loreto", "-28.300000", "-64.183300"), santiagoDelEstero),
                new Ciudad("Monte Quemado", new CentroDeLaCiudad("Monte Quemado", "-25.800000", "-62.833300"), santiagoDelEstero),
                new Ciudad("Clodomira", new CentroDeLaCiudad("Clodomira", "-27.566700", "-64.116700"), santiagoDelEstero)
        );

        for (Ciudad ciudad : ciudadesSantiagoDelEstero) {
            repositorio.guardar(ciudad.getCentroDeLaCiudad());
            repositorio.guardar(ciudad);
        }
    }
}
