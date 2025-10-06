// ===== GESTIÓN DE MODALES - VACUNAS =====

// Modal Registrar Vacuna
function abrirModalRegistrarVacuna() {
    document.getElementById('modalRegistrarVacuna').style.display = 'flex';
}

function cerrarModalRegistrarVacuna() {
    document.getElementById('modalRegistrarVacuna').style.display = 'none';
    document.getElementById('formRegistrarVacuna').reset();
}

// Modal Eliminar Vacuna
function confirmarEliminarVacuna(id, nombre) {
    document.getElementById('eliminarIdVacuna').value = id;
    document.getElementById('eliminarNombreVacuna').textContent = nombre;
    document.getElementById('modalEliminarVacuna').style.display = 'flex';
}

function cerrarModalEliminarVacuna() {
    document.getElementById('modalEliminarVacuna').style.display = 'none';
}

// Modal Logout
function confirmarLogout() {
    document.getElementById('modalLogout').style.display = 'flex';
}

function cerrarModalLogout() {
    document.getElementById('modalLogout').style.display = 'none';
}

// ===== CERRAR MODALES AL HACER CLIC FUERA =====
window.addEventListener('click', function(event) {
    const modales = [
        'modalRegistrarVacuna',
        'modalEliminarVacuna',
        'modalLogout'
    ];

    modales.forEach(modalId => {
        const modal = document.getElementById(modalId);
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

// ===== VALIDACIÓN DEL FORMULARIO =====
document.addEventListener('DOMContentLoaded', function() {
    const formRegistrar = document.getElementById('formRegistrarVacuna');

    if (formRegistrar) {
        formRegistrar.addEventListener('submit', function(e) {
            const mascotaId = document.getElementById('mascotaId').value;
            const nombreVacuna = document.getElementById('nombreVacuna').value;
            const fechaVacuna = document.getElementById('fechaVacuna').value;

            // Validar que se haya seleccionado una mascota
            if (!mascotaId) {
                e.preventDefault();
                alert('Por favor selecciona una mascota');
                return false;
            }

            // Validar que se haya seleccionado un tipo de vacuna
            if (!nombreVacuna) {
                e.preventDefault();
                alert('Por favor selecciona el tipo de vacuna');
                return false;
            }

            // Validar que se haya ingresado una fecha
            if (!fechaVacuna) {
                e.preventDefault();
                alert('Por favor ingresa la fecha de aplicación o programación');
                return false;
            }

            // Validar que la fecha no sea muy antigua (más de 10 años)
            const hoy = new Date();
            hoy.setHours(0, 0, 0, 0);
            const fechaSeleccionada = new Date(fechaVacuna);
            const fechaMinima = new Date();
            fechaMinima.setFullYear(fechaMinima.getFullYear() - 10);

            if (fechaSeleccionada < fechaMinima) {
                const confirmacion = confirm('La fecha seleccionada es de hace más de 10 años. ¿Estás seguro de continuar?');
                if (!confirmacion) {
                    e.preventDefault();
                    return false;
                }
            }
        });
    }

    // NO establecer fecha máxima - permitir fechas futuras para programar vacunas
    // El campo de fecha puede ser pasado, presente o futuro
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

// ===== MANEJO DE ERRORES EN URL =====
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const success = urlParams.get('success');

    if (error && !document.querySelector('.alert-error')) {
        const mensajesError = {
            'mascota_no_encontrada': '❌ Mascota no encontrada',
            'error_registro': '❌ Error al registrar la vacuna',
            'error_eliminando': '❌ Error al eliminar la vacuna',
            'fecha_invalida': '❌ Fecha inválida'
        };

        const mensaje = mensajesError[error] || '❌ Ha ocurrido un error';
        mostrarNotificacion(mensaje, 'error');
    }

    if (success && !document.querySelector('.alert-success')) {
        const mensajesSuccess = {
            'registrado': '✅ Vacuna registrada exitosamente',
            'eliminado': '✅ Vacuna eliminada exitosamente'
        };

        const mensaje = mensajesSuccess[success] || '✅ Operación exitosa';
        mostrarNotificacion(mensaje, 'success');
    }
});

function mostrarNotificacion(mensaje, tipo) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo}`;
    alertDiv.innerHTML = `
        <i class="fas ${tipo === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'}"></i>
        <span>${mensaje}</span>
    `;

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alertDiv, container.firstChild);

        setTimeout(() => {
            alertDiv.style.opacity = '0';
            setTimeout(() => {
                alertDiv.remove();
            }, 500);
        }, 5000);
    }
}

// ===== FUNCIONALIDADES ADICIONALES =====

// Filtrar vacunas por mascota (función opcional para futuras mejoras)
function filtrarPorMascota(mascotaId) {
    const filas = document.querySelectorAll('tbody tr');

    filas.forEach(fila => {
        if (mascotaId === 'todas') {
            fila.style.display = '';
        } else {
            const mascotaFila = fila.dataset.mascotaId;
            if (mascotaFila === mascotaId) {
                fila.style.display = '';
            } else {
                fila.style.display = 'none';
            }
        }
    });
}

// Buscar vacunas (función opcional para futuras mejoras)
function buscarVacunas(termino) {
    const filas = document.querySelectorAll('tbody tr');
    const terminoLower = termino.toLowerCase();

    filas.forEach(fila => {
        const texto = fila.textContent.toLowerCase();
        if (texto.includes(terminoLower)) {
            fila.style.display = '';
        } else {
            fila.style.display = 'none';
        }
    });
}