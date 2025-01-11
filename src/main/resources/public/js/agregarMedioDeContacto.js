
    document.getElementById('add-contacto-btn').addEventListener('click', function() {
    var contactosContainer = document.getElementById('contactos-container');

    // Crear un nuevo contenedor para el medio de contacto
    var contactoDiv = document.createElement('div');
    contactoDiv.classList.add('input-group', 'mt-2');

    // Crear el selector de medio de contacto
    var select = document.createElement('select');
    select.name = 'mediosDeContacto[]';
    select.classList.add('form-select', 'me-2');
    select.innerHTML = `
                <option value="">Seleccione un medio de contacto</option>
                <option value="email">Email</option>
                <option value="telefono">Teléfono</option>
                <option value="whatsapp">WhatsApp</option>
            `;

    // Crear el campo de entrada para el contacto
    var input = document.createElement('input');
    input.type = 'text';
    input.name = 'contacto[]';
    input.classList.add('form-control');
    input.placeholder = 'Ingrese contacto';

    // Cambiar el tipo de input y el placeholder según la selección
    select.addEventListener('change', function() {
    if (this.value === 'telefono' || this.value === 'whatsapp') {
    input.type = 'tel';
    input.placeholder = "Ingrese número de " + this.options[this.selectedIndex].text;
} else if (this.value === 'email') {
    input.type = 'email';
    input.placeholder = "Ingrese su correo electrónico";
}
});

    // Botón para eliminar el medio de contacto
    var removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.classList.add('btn', 'btn-danger', 'ms-2');
    removeBtn.innerText = 'X';
    removeBtn.addEventListener('click', function() {
    contactosContainer.removeChild(contactoDiv);
});

    // Agregar select, input y botón al contenedor
    contactoDiv.appendChild(select);
    contactoDiv.appendChild(input);
    contactoDiv.appendChild(removeBtn);

    // Agregar el contenedor al DOM
    contactosContainer.appendChild(contactoDiv);
});
