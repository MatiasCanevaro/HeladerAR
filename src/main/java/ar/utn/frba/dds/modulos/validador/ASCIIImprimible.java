package ar.utn.frba.dds.modulos.validador;

public class ASCIIImprimible implements FormasDeValidacion {

    public void validar(String contrasenia, ResultadoValidacion resultadoValidacion) {
        for(int i = 0; i < contrasenia.length(); i++){
            char caracter = contrasenia.charAt(i);
            if(caracter > 127 || caracter < 32){
                resultadoValidacion.agregarError("La contraseña posee caracteres no ASCII imprimibles");
                resultadoValidacion.setLaContraseniaEsValida(false);
            }
        }
    }
}
