// ===== MODAL DE VER DETALLES =====
function verDetallesMascota(id, nombre, tipo, raza, edad, peso, color) {
    // Actualizar el icono según el tipo
    const iconElement = document.querySelector('#modalDetallesMascota .detalle-icon i');
    if (tipo === 'PERRO') {
        iconElement.className = 'fas fa-dog';
    } else if (tipo === 'GATO') {
        iconElement.className = 'fas fa-cat';
    } else {
        iconElement.className = 'fas fa-paw';
    }

    // Llenar los datos
    document.getElementById('detalleNombre').textContent = nombre;
    document.getElementById('detalleTipo').textContent = tipo;
    document.getElementById('detalleRaza').textContent = raza;
    document.getElementById('detalleEdad').textContent = edad + ' años';
    document.getElementById('detallePeso').textContent = peso + ' kg';
    document.getElementById('detalleColor').textContent = color || 'No especificado';

    // Mostrar el modal
    document.getElementById('modalDetallesMascota').style.display = 'flex';
}

function cerrarModalDetalles() {
    document.getElementById('modalDetallesMascota').style.display = 'none';
}

// ===== MODAL DE EDITAR =====
function editarMascota(id, nombre, tipo, raza, edad, peso, color) {
    // Llenar el formulario con los datos actuales
    document.getElementById('editarId').value = id;
    document.getElementById('editarNombre').value = nombre;
    document.getElementById('editarTipo').value = tipo;
    document.getElementById('editarRaza').value = raza;
    document.getElementById('editarEdad').value = edad;
    document.getElementById('editarPeso').value = peso;
    document.getElementById('editarColor').value = color || '';

    // Mostrar el modal
    document.getElementById('modalEditarMascota').style.display = 'flex';
}

function cerrarModalEditar() {
    document.getElementById('modalEditarMascota').style.display = 'none';
    document.getElementById('formEditarMascota').reset();
}

// ===== MODAL DE ELIMINAR =====
function confirmarEliminarMascota(id, nombre) {
    document.getElementById('eliminarId').value = id;
    document.getElementById('eliminarNombre').textContent = nombre;
    document.getElementById('modalEliminarMascota').style.display = 'flex';
}

function cerrarModalEliminar() {
    document.getElementById('modalEliminarMascota').style.display = 'none';
}

// ===== CERRAR MODALES AL HACER CLIC FUERA =====
window.addEventListener('click', function(event) {
    const modales = [
        'modalDetallesMascota',
        'modalEditarMascota',
        'modalEliminarMascota',
        'modalRegistrarMascota',
        'modalLogout'
    ];

    modales.forEach(modalId => {
        const modal = document.getElementById(modalId);
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

// ===== VALIDACIÓN DEL FORMULARIO DE EDITAR =====
document.addEventListener('DOMContentLoaded', function() {
    const formEditar = document.getElementById('formEditarMascota');
    if (formEditar) {
        formEditar.addEventListener('submit', function(e) {
            const nombre = document.getElementById('editarNombre').value.trim();
            const tipo = document.getElementById('editarTipo').value;
            const raza = document.getElementById('editarRaza').value.trim();
            const edad = document.getElementById('editarEdad').value;
            const peso = document.getElementById('editarPeso').value;

            if (!nombre || !tipo || !raza) {
                e.preventDefault();
                alert('Por favor completa todos los campos obligatorios');
                return false;
            }

            if (edad < 0) {
                e.preventDefault();
                alert('La edad debe ser un valor positivo');
                return false;
            }

            if (peso <= 0) {
                e.preventDefault();
                alert('El peso debe ser mayor a 0');
                return false;
            }
        });
    }
});

// ===== ALERTAS AUTO-DESVANECIENTES =====
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.remove();
            }, 500);
        }, 5000);
    });
});