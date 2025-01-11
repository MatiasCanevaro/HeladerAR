package ar.utn.frba.dds.modulos.validador;

public class Longitud implements FormasDeValidacion {

    public void validar(String contrasenia, ResultadoValidacion resultadoValidacion) {
        if(contrasenia.length() < 8){
            resultadoValidacion.agregarError("La contraseña debe ser mayor a 8 caracteres");
            resultadoValidacion.setLaContraseniaEsValida(false);
        }
        if(contrasenia.length() > 64){
            resultadoValidacion.agregarError("La contraseña debe ser menor a 64 caracteres");
            resultadoValidacion.setLaContraseniaEsValida(false);
        }
    }

}
