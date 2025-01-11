package ar.utn.frba.dds.modelos.repositorios;

import ar.utn.frba.dds.modelos.entidades.cuestionarios.*;
import ar.utn.frba.dds.modelos.entidades.heladeras.Heladera;
import ar.utn.frba.dds.modelos.entidades.ofertasYCanjes.Rubro;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PreguntaCuestionarioTest implements SimplePersistenceTest {
    @Test
    void persistir() {
        Pregunta pregunta1 = new Pregunta();
        Pregunta pregunta2 = new Pregunta();
        Pregunta pregunta3 = new Pregunta();
        pregunta1.setDescripcion("descripcion1");
        pregunta1.setDescripcion("descripcion2");
        pregunta1.setDescripcion("descripcion3");

        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(pregunta1);
        preguntas.add(pregunta2);
        preguntas.add(pregunta3);

        Repositorio repositorio = new Repositorio();
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.agregarPreguntas(preguntas);

        CuestionarioRespondido cuestionarioRespondido = new CuestionarioRespondido();
        cuestionarioRespondido.setCuestionario(cuestionario);

        Respuesta respuesta = new Respuesta();
        respuesta.setCuestionarioRespondido(cuestionarioRespondido);
        respuesta.setPregunta(pregunta1);

        repositorio.guardar(respuesta);
    }
    @Test
    void buscarTodos() {
        Repositorio repositorio = new Repositorio();
        List<Object> preguntas = repositorio.buscarTodos("Pregunta");
        for (Object o : preguntas) {
            Pregunta pregunta = (Pregunta) o;
            System.out.println(pregunta.getDescripcion());
        }
    }
}
