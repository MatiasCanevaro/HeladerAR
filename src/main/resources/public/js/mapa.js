const ubicacionDefault = [-34.61315, -58.37723];
const map = L.map('map').setView(ubicacionDefault, 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

// ICONOS
const icono_heladera = L.icon({
    iconUrl: "img/mapas/mapa_nevera.png",
    iconSize: [50, 50], // tamaño del icono
    popupAnchor: [-1, -35] // punto donde aparece el popup en relacion al icono
});

const icono_heladera_inactiva = L.icon({
    iconUrl: "img/mapas/mapa_nevera_inactiva.png", // Asegúrate de tener este icono
    iconSize: [50, 50],
    popupAnchor: [-1, -35]
});

const icono_ubicacion = L.icon({
    iconUrl: "img/mapas/mapa_marcador.png",
    iconSize: [30, 30],
});

// mostrar ubicacion del user
function ubicacionUsuario(e) {
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    // Definimos límites mínimos y máximos para el radio
    const radioMin = 2;
    const radioMax = 500;
    const radio = Math.max(radioMin, Math.min(radioMax, e.accuracy / 2));

    // Agregar un marcador en la ubicación del usuario
    L.marker([lat, lng], {icon: icono_ubicacion}).addTo(map);

    // Crea un círculo que muestra la precisión de la ubicación
    L.circle([lat, lng], {radius: radio}).addTo(map);
    enviarUbicacion(lat, lng);
}


function agregarUnaHeladera(puntoEstrategico, mensaje, estado) {
    const latitud = parseFloat(puntoEstrategico[0]);
    const longitud = parseFloat(puntoEstrategico[1]);

    // Verifica que ambos elementos sean números
    if (typeof latitud !== 'number' || typeof longitud !== 'number') {
        throw new Error("Ambos elementos de puntoEstrategico deben ser numberos!!!.");
    }
    // Selecciona el icono según el estado
    const icono = estado === 'activa' ? icono_heladera : icono_heladera_inactiva;
    // Crea el marcador en el mapa
    const heladera = L.marker([latitud, longitud], {icon: icono}).addTo(map);

    heladera.bindPopup(mensaje).openPopup();
}

/*
 function agregarUnaHeladera(puntoEstrategico, mensaje) {
    const latitud = parseFloat(puntoEstrategico[0]);
    const longitud = parseFloat(puntoEstrategico[1]);

    // Verifica que ambos elementos sean números
    if (typeof latitud !== 'number' || typeof longitud !== 'number') {
        console.log("Estoy en el error : " + latitud + " soy longitud: " + longitud);
        console.log("SOY " + typeof latitud);
        throw new Error("Ambos elementos de puntoEstrategico deben ser numberos!!!.");
    }

    // Crea el marcador en el mapa
    const heladera = L.marker([latitud, longitud], {icon: icono_heladera}).addTo(map);

    console.log("Estoy en la funcion: " + latitud + " soy longitud: " + longitud);
    heladera.bindPopup(mensaje).openPopup();
}
 */

function onMapClick(e) {
    // Obtener las coordenadas del clic
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    // Mostrar un mensaje de alerta
    alert("Hiciste click en el mapa, ubicación: " + e.latlng);

    // Llamar a la función para obtener los datos de la dirección
    obtenerDatosDireccion(lat, lng);
}

function obtenerDatosDireccion(lat, lng) {
    const url = `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json&addressdetails=1`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta de la API');
            }
            return response.json();
        })
        .then(data => {
            // Extraer la información de dirección
            const provincia = data.address.state || 'No disponible';
            const ciudad = data.address.city || data.address.town || 'No disponible';
            const calle = data.address.road || 'No disponible';
            const altura = data.address.house_number || 'No disponible';

            // Mostrar los datos en un popup o en la consola
            alert(`Calle: ${calle}, Altura: ${altura}, Ciudad: ${ciudad}, Provincia: ${provincia}`);

            // Enviar los datos al servidor
            enviarDatosAlServidor(calle, altura, ciudad, provincia, lat, lng);
        })
        .catch(error => {
            console.error('Error al obtener datos de dirección:', error);
        });
}

function enviarDatosAlServidor(calle, altura, ciudad, provincia, lat, lng) {
    const datos = {
        calle: calle,
        altura: altura,
        ciudad: ciudad,
        provincia: provincia,
        latitud: lat,
        longitud: lng
    };

    // Enviar datos al servidor usando fetch
    fetch('/guardarDireccion', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos enviados exitosamente:', data);
        })
        .catch(error => {
            console.error('Error al enviar datos al servidor:', error);
        });
}

// configura el mapa para centrarse en la ubicación del usuario si es posible
map.locate({setView: true, maxZoom: 16});

// escucha eventos de localización
map.on('click', onMapClick)
map.on('locationfound', function (e) {
    ubicacionUsuario(e); // Aquí puedes definir cómo mostrar la ubicación en el mapa
    cargarHeladeras();
    //agregarUnaHeladera([-34.62746, -58.4555171], "Estoy harcodeada en la linea 165 y encima rota", 'inactiva');
});



// Función para enviar la ubicación del usuario
function enviarUbicacion(lat, lng) {
    fetch('/mapa', {
        credentials: 'include',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({latitud: lat, longitud: lng})
    })
        .then(response => response.json())
        .then(data => {
            console.log('Ubicación enviada y respuesta recibida:', data);
        })
        .catch(error => {
            console.error('Error al enviar la ubicación:', error);
        });
}


// Función para obtener las heladeras del backend y mostrarlas en el mapa
function cargarHeladeras() {
    fetch('/heladeras/carga', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            data.forEach(heladera => {
                const estado = heladera.estaActiva ? 'activa' : 'inactiva';
                const puntoEstrategico = heladera.puntoEstrategico;
                const latitud = puntoEstrategico.latitud;
                const longitud = puntoEstrategico.longitud;
                // Extraemos los datos de la dirección
                const direccion = puntoEstrategico.direccion;
                const calle = direccion.calle || 'Calle no disponible';
                const altura = direccion.altura || '';
                // Formateamos la dirección en un string
                const direccionFormateada = `${calle} ${altura}`;

                // Crear el mensaje para el popup
                const mensaje = `${direccionFormateada}`;
                agregarUnaHeladera([latitud, longitud], mensaje, estado);
            });
        })
        .catch((error) => {
            console.error('Error al cargar las heladeras:', error);
        });
}



