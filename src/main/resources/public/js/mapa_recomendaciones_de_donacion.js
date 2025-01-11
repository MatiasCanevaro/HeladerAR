const ubicacionDefault = [-34.61315, -58.37723];
const map = L.map('map').setView(ubicacionDefault, 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '© OpenStreetMap'
}).addTo(map);

// ICONOS
const icono_recomendacion = L.icon({
    iconUrl: "/img/mapas/mapa_comunidad.png",
    iconSize: [50, 50], // tamaño del icono
    popupAnchor: [-1, -35] // punto donde aparece el popup en relacion al icono
});

const icono_ubicacion = L.icon({
    iconUrl: "/img/mapas/mapa_marcador.png",
    iconSize: [30, 30],
});

let selectedLocation = null;

// mostrar ubicacion del user
function ubicacionUsuario(e) {
    var accuracy = e.accuracy;

    // Definimos límites mínimos y máximos para el radio
    var radioMin = 2; // El valor mínimo de radio para evitar que sea muy pequeño
    var radioMax = 500; // El valor máximo de radio para evitar que sea muy grande

    // Ajustamos el radio proporcionalmente dentro del rango
    var radio = Math.max(radioMin, Math.min(radioMax, accuracy / 2));

    // Crea un marcador en la ubicación del usuario
    L.marker(e.latlng, {icon: icono_ubicacion}).addTo(map);

    // Crea un círculo que muestra la precisión de la ubicación
    L.circle(e.latlng, {radius: radio}).addTo(map);
}

function agregarPuntoRecomendado(puntoEstrategico, mensaje) {
    const recomendacion = L.marker(puntoEstrategico, {icon: icono_recomendacion}).addTo(map);
    recomendacion.bindPopup(mensaje).openPopup();

    recomendacion.on('click', function() {
        const lat = puntoEstrategico[0];
        const lng = puntoEstrategico[1];
        selectedLocation = { lat, lng };

        alert(`Seleccionaste el lugar de donación: ${mensaje}`);
        document.getElementById('next-button').disabled = false;
    });
}

// configura el mapa para centrarse en la ubicación del usuario si es posible
map.locate({ setView: true, maxZoom: 16 });

// escucha eventos de localización
map.on('locationfound', function (e) {
    ubicacionUsuario(e); // Aquí puedes definir cómo mostrar la ubicación en el mapa
    enviarUbicacion(e.latitude, e.longitude); // Envía la ubicación al backend
});

// Función para enviar la ubicación del usuario
function enviarUbicacion(lat, lng) {
    const params = new URLSearchParams();
    params.append('latitud', lat);
    params.append('longitud', lng);

    fetch('/dashboard/contribuciones/hacerse_cargo_de_heladera/recomendaciones', {
        credentials: 'include',
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
        .then(response => response.json())
        .then(data => {
            console.log('Ubicación enviada y respuesta recibida:', data);
        })
        .catch((error) => {
            console.error('Error al enviar la ubicación:', error);
        });
}

// Función para obtener los puntos recomendados del backend y mostrarlos en el mapa
function cargarPuntosRecomendados() {
    fetch('/puntosRecomendados/carga', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            data.forEach(punto => {
                const latitud = punto.latitud;
                const longitud = punto.longitud;
                const descripcion = punto.nombre + "\n" + punto.distanciaEnKM + " km" || 'Descripción no disponible';

                // Crear el mensaje para el popup
                const mensaje = `${descripcion}`;
                agregarPuntoRecomendado([latitud, longitud], mensaje);
            });
        })
        .catch((error) => {
            console.error('Error al cargar los puntos recomendados:', error);
        });
}

cargarPuntosRecomendados();

document.getElementById('next-button').addEventListener('click', function() {
    if (selectedLocation) {
        enviarDatosAlServidor(null, null, null, null, selectedLocation.lat, selectedLocation.lng);
    }
});