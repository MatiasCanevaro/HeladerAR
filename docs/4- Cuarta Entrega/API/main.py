from typing import List
from fastapi import FastAPI, Depends
from pydantic import BaseModel
import math
import json
from sqlalchemy.orm import Session
import models
from database import engine, SessionLocal

class Lugar(BaseModel):
    nombre: str
    direccion: str
    latitud: float
    longitud: float

app = FastAPI(
title="Recomendadora de Lugares de Donación",
    description="Dada un punto geográfico, propone los lugares dónde podés acercar donaciones.",
    summary="¡Hola! Soy una API de recomendación de lugares de donación.",
    version="1.0",
    contact={
        "name": "Grupo 1",
        "url": "https://www.youtube.com/watch?v=1zDf7WoLEDk",
        "email": "tpdisenio99@gmail.com",
    }
)

models.Base.metadata.create_all(bind=engine)

def obtener_sesion_bd():
    bd = SessionLocal()
    try:
        yield bd
    finally:
        bd.close()

@app.get("/recomendacion/", 
    tags=["Recomendación"],
    summary="Obtener una recomendación",
    description="Este endpoint se encarga de recibir una latitud y longitud de tipo string y retornar los 3 lugares de donación más cercanos a dicha coordenada.",)
async def obtener_recomendacion(latitud: str, longitud: str, bd: Session = Depends(obtener_sesion_bd)):    
    lugares_bd = obtener_lugares_de_bd(bd)
    lugares_convertidos = convertir_lat_y_long_de_string_a_float(lugares_bd)

    referencia = Lugar(latitud=float(latitud), longitud=float(longitud),nombre="",direccion="")
    lugares_y_distancias = asociar_cada_lugar_con_su_distancia_al_lugar_de_referencia(lugares_convertidos, referencia)

    lugares_y_distancias_ordenados = ordenar_por_distancia_de_menor_a_mayor(lugares_y_distancias)
    lugares_mas_cercanos = obtener_primeros_tres_elementos(lugares_y_distancias_ordenados)

    json = crear_json(lugares_mas_cercanos)
    return json

def obtener_lugares_de_bd(bd: Session) -> List[Lugar]:
    return bd.query(models.LugarDB).all()

def convertir_lat_y_long_de_string_a_float(lugares: List[Lugar]) -> List[Lugar]:
    return [
        Lugar(
            latitud=float(lugar.latitud),
            longitud=float(lugar.longitud),
            nombre=lugar.nombre,
            direccion=lugar.direccion
        )
        for lugar in lugares
    ]

def asociar_cada_lugar_con_su_distancia_al_lugar_de_referencia(lugares_convertidos: List[Lugar], referencia: Lugar) -> List[tuple]:
    return [
        (lugar, calcular_distancia_en_metros_entre(referencia, lugar))
        for lugar in lugares_convertidos
    ]

def calcular_distancia_en_metros_entre(lugar1: Lugar, lugar2: Lugar) -> float:
    R = 6371  # Radio de la Tierra en kilómetros

    lat1, lon1 = map(math.radians, [lugar1.latitud, lugar1.longitud])
    lat2, lon2 = map(math.radians, [lugar2.latitud, lugar2.longitud])

    lat_distance = lat2 - lat1
    lon_distance = lon2 - lon1

    a = math.sin(lat_distance / 2) ** 2 + math.cos(lat1) * math.cos(lat2) * math.sin(lon_distance / 2) ** 2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance = R * c * 1000  # Convertir a metros

    return abs(distance)

def ordenar_por_distancia_de_menor_a_mayor(lugares_y_distancias: List[tuple]) -> List[tuple]:
    return sorted(lugares_y_distancias, key=lambda x: x[1])

def obtener_primeros_tres_elementos(lugares_y_distancias: List[tuple]) -> List[Lugar]:
    return [lugar for lugar, _ in lugares_y_distancias[:3]]

def crear_json(lugares: List[Lugar]) -> List[dict]:
    json_resultado = json.dumps([
        {
            "nombre": lugar.nombre,
            "direccion": lugar.direccion,
        }
        for lugar in lugares
    ], indent=2)
    json_a_retornar = json.loads(json_resultado)
    return json_a_retornar