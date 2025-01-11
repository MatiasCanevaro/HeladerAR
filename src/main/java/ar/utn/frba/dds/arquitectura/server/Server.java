package ar.utn.frba.dds.arquitectura.server;

import ar.utn.frba.dds.arquitectura.config.ServiceLocator;
import ar.utn.frba.dds.arquitectura.controllers.*;
import ar.utn.frba.dds.arquitectura.middlewares.AuthMiddleware;
import ar.utn.frba.dds.arquitectura.utils.Initializer;
import ar.utn.frba.dds.arquitectura.utils.JavalinRenderer;
import ar.utn.frba.dds.arquitectura.utils.PrettyProperties;
import ar.utn.frba.dds.arquitectura.server.handlers.AppHandlers;
import ar.utn.frba.dds.arquitectura.helpers.HandlebarsHelpers;
import ar.utn.frba.dds.modelos.entidades.mqttclient.BrokerMain;
import ar.utn.frba.dds.modulos.datadog.DDMetricsUtils;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import io.javalin.micrometer.MicrometerPlugin;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import org.eclipse.jetty.server.session.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Server {
    private static Javalin app = null;

    public static Javalin app() {
        if (app == null)
            throw new RuntimeException("App no inicializada");
        return app;
    }

    public static void init() {
        if (app == null) {
            Integer port = Integer.parseInt(PrettyProperties.getInstance().propertyFromName("server_port"));

            final var registry = ServiceLocator.instanceOf(StepMeterRegistry.class);
            final var micrometerPlugin = new MicrometerPlugin(config ->
                    config.registry = registry
            );

            app = Javalin.create(config(micrometerPlugin)).start(port);

            ServiceLocator.instanceOf(CronDonacionDeDineroController.class).start();
            ServiceLocator.instanceOf(CronHeladerasActivasController.class).start();
            ServiceLocator.instanceOf(CronFallaConexionController.class).comenzarBarrido();
            ServiceLocator.instanceOf(CronReportesController.class).generarReportes();
            ServiceLocator.instanceOf(CronCSVController.class).start();

            AuthMiddleware.apply(app);
            AppHandlers.applyHandlers(app);
            Router router = new Router();
            router.init(app);

            if (Boolean.parseBoolean(PrettyProperties.getInstance().propertyFromName("dev_mode"))) {
                //Initializer.init();
            }
            // Iniciar el broker automáticamente
            //BrokerMain.iniciarBroker();
        }
    }

    private static Consumer<JavalinConfig> config(MicrometerPlugin micrometerPlugin) {
        return config -> {
            config.registerPlugin(micrometerPlugin);

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
            });

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/ofertas";
                staticFiles.directory = "ofertas";
                staticFiles.location = Location.EXTERNAL;
            });

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/visitas_tecnicas";
                staticFiles.directory = "visitas_tecnicas";
                staticFiles.location = Location.EXTERNAL;
            });
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/fallaTecnica"; // Ruta donde se encuentran las imágenes
                staticFileConfig.directory = "fallaTecnica"; // Directorio donde se encuentran las imágenes
                staticFileConfig.location = Location.EXTERNAL; // Ubicación de las imágenes
            });


            config.fileRenderer(new JavalinRenderer().register("hbs", (path, model, context) -> {
                Handlebars handlebars = new Handlebars();
                HandlebarsHelpers.configure(handlebars);

                Template template = null;
                try {
                    template = handlebars.compile("templates/" + path.replace(".hbs", ""));
                    return template.apply(model);
                } catch (IOException e) {
                    e.printStackTrace();
                    context.status(HttpStatus.NOT_FOUND);
                    return "No se encuentra la página indicada...";
                }
            }));
        };
    }

    public static SessionHandler fileSessionHandler() {
        SessionHandler sessionHandler = new SessionHandler();
        //sessionHandler.setMaxInactiveInterval(-1);
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(fileSessionDataStore());
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);
        return sessionHandler;
    }

    private static FileSessionDataStore fileSessionDataStore() {
        FileSessionDataStore fileSessionDataStore = new FileSessionDataStore();

        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        File storeDir = new File(baseDir, "javalin-session-store");

        if (!storeDir.exists() && !storeDir.mkdir()) {
            throw new RuntimeException("Failed to create session store directory: " + storeDir.getAbsolutePath());
        }

        fileSessionDataStore.setStoreDir(storeDir);
        return fileSessionDataStore;
    }
}