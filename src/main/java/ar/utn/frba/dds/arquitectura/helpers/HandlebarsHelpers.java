package ar.utn.frba.dds.arquitectura.helpers;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public class HandlebarsHelpers {
    public static Helper<Object> isTipoPregunta = (context, options) -> {
        String tipoPregunta = context.toString();
        String expectedTipo = options.param(0);
        return tipoPregunta.equals(expectedTipo) ? options.fn() : options.inverse();
    };

    public static void registerHelpers(Handlebars handlebars) {
        handlebars.registerHelper("eq", (context, options) -> {
            if (context == null) {
                return options.inverse();
            }
            Object param = options.param(0, null);
            if (context.equals(param)) {
                return options.fn();
            } else {
                return options.inverse();
            }
        });
    }

    public static void configure(Handlebars handlebars) {
        handlebars.registerHelper("isTipoPregunta", HandlebarsHelpers.isTipoPregunta);
        HandlebarsHelpers.registerHelpers(handlebars);
    }
}