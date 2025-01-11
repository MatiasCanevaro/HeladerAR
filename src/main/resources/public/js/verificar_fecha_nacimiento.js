document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('registroForm').addEventListener('submit', function(event) {
        const birthdayInput = document.getElementById('birthday');
        const birthday = new Date(birthdayInput.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (birthday > today) {
            alert('La fecha de nacimiento no puede ser mayor a la fecha actual.');
            event.preventDefault(); // Previene el envío si hay error
            return; // Sale de la función para evitar el envío
        }

        const minDate = new Date();
        minDate.setFullYear(minDate.getFullYear() - 120);

        if (birthday < minDate) {
            alert('La fecha de nacimiento no puede ser menor a 120 años.');
            event.preventDefault(); // Previene el envío si hay error
            return; // Sale de la función para evitar el envío
        }

        // Más validaciones aquí si es necesario
    });
});
