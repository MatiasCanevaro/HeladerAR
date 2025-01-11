document.addEventListener('DOMContentLoaded', function () {
    // Encuentra todos los submenús
    var dropdownSubmenus = document.querySelectorAll('.dropdown-submenu');

    dropdownSubmenus.forEach(function (submenu) {
        submenu.addEventListener('mouseover', function (e) {
            e.stopPropagation(); // Evita que el dropdown principal se cierre
            this.querySelector('.dropdown-menu').classList.toggle('show');
        });

        // Permitir que el submenú se mantenga abierto con hover (opcional)
        submenu.addEventListener('mouseover', function () {
            this.querySelector('.dropdown-menu').classList.add('show');
        });

        submenu.addEventListener('mouseout', function () {
            this.querySelector('.dropdown-menu').classList.remove('show');
        });
    });
});