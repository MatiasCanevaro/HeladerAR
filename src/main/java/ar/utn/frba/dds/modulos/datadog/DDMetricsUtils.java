package ar.utn.frba.dds.modulos.datadog;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.datadog.DatadogConfig;
import io.micrometer.datadog.DatadogMeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;	

@Slf4j
public class DDMetricsUtils {
	@Getter
	private final StepMeterRegistry registry;

	public DDMetricsUtils(String appTag) {
		// crea un registro para nuestras métricas basadas en DD
		var config = new DatadogConfig() {
			@Override
			public Duration step() {
				return Duration.ofSeconds(10);
			}

			@Override
			public String apiKey() {
				return leerArchivoProperties();
			}

			@Override
			public String uri() {
				return "https://api.us5.datadoghq.com";
			}

			@Override
			public String get(String k) {
				return null;
			}
		};
		registry = new DatadogMeterRegistry(config, Clock.SYSTEM);
		registry.config().commonTags("app", appTag );
		initInfraMonitoring() ;
	}

	private void initInfraMonitoring() {
		try (var jvmGcMetrics = new JvmGcMetrics(); var jvmHeapPressureMetrics = new JvmHeapPressureMetrics()) {
			jvmGcMetrics.bindTo(registry);
			jvmHeapPressureMetrics.bindTo(registry);
		}
		new JvmMemoryMetrics().bindTo(registry);
		new ProcessorMetrics().bindTo(registry);
		new FileDescriptorMetrics().bindTo(registry);
	}

	public String leerArchivoProperties(){
		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream("./src/main/resources/archivoDeConfiguracion.properties")) {
			properties.load(fis);
			return properties.getProperty("datadog_apiKey");
		} catch (IOException e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}
		return null;
	}

}
