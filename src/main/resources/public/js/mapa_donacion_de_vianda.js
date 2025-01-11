const ubicacionDefault = [-34.61315, -58.37723];
const map = L.map('map').setView(ubicacionDefault, 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

// ICONOS
const icono_heladera = L.icon({
    iconUrl: "/img/mapas/mapa_nevera.png",
    iconSize: [50, 50], // tamaño del icono
    popupAnchor: [-1, -35] // punto donde aparece el popup en relacion al icono
});

const icono_ubicacion = L.icon({
    iconUrl: "/img/mapas/mapa_marcador.png",
    iconSize: [30, 30],
});

let selectedHeladeraId = null;

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

function agregarUnaHeladera(puntoEstrategico, mensaje, id) {
    const heladera = L.marker(puntoEstrategico, {icon: icono_heladera}).addTo(map);
    heladera.bindPopup(mensaje).openPopup();
    heladera.on('click', function() {
        selectedHeladeraId = id;
        alert(`Heladera seleccionada: ${mensaje}`);
    });
}

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
map.locate({ setView: true, maxZoom: 16 });

// escucha eventos de localización
map.on('locationfound', function (e) {
    ubicacionUsuario(e); // Aquí puedes definir cómo mostrar la ubicación en el mapa
    enviarUbicacion(e.latitude, e.longitude); // Envía la ubicación al backend
    agregarUnaHeladera([-34.6025102,-58.3812733], "hola", 1);
});

// Función para enviar la ubicación del usuario
function enviarUbicacion(lat, lng) {
    fetch('/mapa', {
        credentials: 'include',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ latitud: lat, longitud: lng })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Ubicación enviada y respuesta recibida:', data);
        })
        .catch((error) => {
            console.error('Error al enviar la ubicación:', error);
        });
}

// Función para obtener las heladeras del backend y mostrarlas en el mapa
function cargarHeladeras() {
    fetch('/heladeras/carga_heladeras_con_espacio', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            data.forEach(heladera => {
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
                agregarUnaHeladera([latitud, longitud], mensaje, heladera.id);
            });
        })
        .catch((error) => {
            console.error('Error al cargar las heladeras:', error);
        });
}

// Llamar a la función para cargar y mostrar las heladeras en el mapa
cargarHeladeras();

// Handle form submission
document.getElementById('donacionForm').addEventListener('submit', function(event) {
    if (!selectedHeladeraId) {
        event.preventDefault();
        alert('Por favor, seleccione una heladera en el mapa.');
    } else {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'idHeladera';
        input.value = selectedHeladeraId;
        this.appendChild(input);
    }
});