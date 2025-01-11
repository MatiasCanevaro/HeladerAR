 const ubicacionDefault = [-34.61315, -58.37723];
    const map = L.map('map').setView(ubicacionDefault, 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    const icono_heladera = L.icon({
        iconUrl: "/img/mapas/mapa_nevera.png",
        iconSize: [50, 50],
        popupAnchor: [-1, -35]
    });

    const icono_ubicacion = L.icon({
        iconUrl: "/img/mapas/mapa_marcador.png",
        iconSize: [30, 30],
    });

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

    map.on('locationfound', function (e) {
        ubicacionUsuario(e); // Aquí puedes definir cómo mostrar la ubicación en el mapa
        enviarUbicacion(e.latitude, e.longitude); // Envía la ubicación al backend
        agregarUnaHeladera([-34.6025102,-58.3812733], "hola", 1);
    });

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

    function distribuirContribuciones(tiposContribucion) {
        // Selecciona las columnas izquierda y derecha
        const columnaIzquierda = document.querySelector('.columna-izquierda');
        const columnaDerecha = document.querySelector('.columna-derecha');

        // Limpia las columnas por si acaso se están volviendo a generar
        columnaIzquierda.innerHTML = '';
        columnaDerecha.innerHTML = '';

        // Selecciona todos los botones de contribución
        const botones = document.querySelectorAll('.contribution-button');

        // Oculta o muestra botones y distribúyelos entre las dos columnas
        let contador = 0;
        botones.forEach(boton => {
            const tipo = boton.getAttribute('data-type');
            if (tiposContribucion.includes(tipo)) {
                boton.style.display = "block";  // Mostrar si está en la lista
                boton.style.removeProperty('display');

                // Añadir el botón a la columna correspondiente
                if (contador % 2 === 0) {
                    columnaIzquierda.appendChild(boton);
                } else {
                    columnaDerecha.appendChild(boton);
                }
                contador++;
            } else {
                boton.style.display = "none";   // Ocultar si no está en la lista
            }
        });
    }

    // Ejemplo: Llama a la función con los tipos de contribución activos
    distribuirContribuciones(["heladera", "oferta", "dinero"]);  // Solo mostrará estos botones

    function agregarUnaHeladera(puntoEstrategico, mensaje, id) {
        const heladera = L.marker(puntoEstrategico, {icon: icono_heladera}).addTo(map);
        heladera.bindPopup(mensaje).openPopup();
        heladera.on('click', function() {
            selectedHeladeraId = id;
            alert(`Heladera seleccionada: ${mensaje}`);
        });
    }

    function cargarHeladerasPropias() {
        fetch('/heladeras/carga_heladeras_propias', {
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
                    const direccion = puntoEstrategico.direccion;
                    const calle = direccion.calle || 'Calle no disponible';
                    const altura = direccion.altura || '';
                    const direccionFormateada = `${calle} ${altura}`;
                    const mensaje = `${direccionFormateada}`;
                    agregarUnaHeladera([latitud, longitud], mensaje);
                });
            })
            .catch(error => {
                console.error('Error al cargar las heladeras propias:', error);
            });
    }

    cargarHeladerasPropias();