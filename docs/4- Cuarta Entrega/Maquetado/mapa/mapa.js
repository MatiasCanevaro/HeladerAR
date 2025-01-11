const ubicacionDefault = [-34.61315, -58.37723];
const map = L.map('map').setView(ubicacionDefault, 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

// ICONOS
const icono_heladera = L.icon({
    iconUrl: "imagenes/nevera.png",
    iconSize: [50, 50], // tamaño del icono
    popupAnchor: [-1, -35] // punto donde aparece el popup en relacion al icono
});

const icono_ubicacion = L.icon({
    iconUrl: "imagenes/marcador.png",
    iconSize: [30, 30],
});

// mostrar ubicacion del user
function ubicacionUsuario(e) {
    var radio = e.accuracy / 2;

    // Crea un marcador en la ubicación del usuario
    L.marker(e.latlng, {icon: icono_ubicacion}).addTo(map);

    // Crea un círculo que muestra la precisión de la ubicación
    L.circle(e.latlng, radio).addTo(map);
}
function  agregarUnaHeladera(puntoEstrategico, mensaje){
    const heladera = L.marker(puntoEstrategico, {icon: icono_heladera}).addTo(map);
    heladera.bindPopup(mensaje).openPopup();
}

function onMapClick(e) {
    alert("Hiciste click en el mapa, ubicacion: " + e.latlng);
}map.on('click', onMapClick);


let puntosEstrategico = [[-34.61315, -58.37723], [-34.615974, -58.396626], [-34.635327, -58.367271]];

agregarUnaHeladera(puntosEstrategico[0], "<b>Hola!</b><br>Soy una heladera :)");
agregarUnaHeladera(puntosEstrategico[1], "<b>Hola!</b><br>Soy otra heladera");
agregarUnaHeladera(puntosEstrategico[2], "<b>Hola!</b><br>Soy la mejor heladera");

// configura el mapa para centrarse en la ubicación del usuario si es posible
map.locate({ setView: true, maxZoom: 16 });

// escucha eventos de localización
map.on('locationfound', ubicacionUsuario);


function actualizarHTML(viandas, ubicacion) {

    // Crear un nuevo elemento para el punto
    const puntoElemento = document.createElement('h1');
    puntoElemento.textContent  = viandas;

    const ubicacionElemento = document.createElement('h3');
    puntoElemento.textContent = ubicacion;
    // Añadir el nuevo elemento al contenedor
    document.getElementById("punto-cercano").appendChild(puntoElemento);
    document.getElementById("ubicacion-cercana").appendChild(ubicacionElemento);
}

actualizarHTML(22, "Piedras, 400");


