package ar.utn.frba.dds.arquitectura.exceptions;

import java.io.IOException;

public class ImageNotSavedException extends RuntimeException{
    public ImageNotSavedException(String errorAlGuardarLaImagen, IOException e) {
    }
}
