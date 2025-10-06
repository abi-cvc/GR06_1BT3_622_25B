// visitas.js - Funciones para el módulo de Visitas Veterinarias

function abrirModalRegistrarVisita() {
    document.getElementById('modalRegistrarVisita').style.display = 'flex';
    document.getElementById('formRegistrarVisita').reset();
}

function cerrarModalRegistrarVisita() {
    document.getElementById('modalRegistrarVisita').style.display = 'none';
}

function confirmarEliminarVisita(id, mascota, fecha) {
    document.getElementById('eliminarIdVisita').value = id;
    document.getElementById('eliminarMascotaVisita').textContent = mascota;
    document.getElementById('eliminarFechaVisita').textContent = fecha;
    document.getElementById('modalEliminarVisita').style.display = 'flex';
}

function cerrarModalEliminarVisita() {
    document.getElementById('modalEliminarVisita').style.display = 'none';
}

window.onclick = function(event) {
    const modalRegistrar = document.getElementById('modalRegistrarVisita');
    const modalEliminar = document.getElementById('modalEliminarVisita');

    if (event.target === modalRegistrar) {
        cerrarModalRegistrarVisita();
    }
    if (event.target === modalEliminar) {
        cerrarModalEliminarVisita();
    }
};

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formRegistrarVisita');
    if (form) {
        form.addEventListener('submit', function(e) {
            const mascotaId = document.getElementById('mascotaId').value;
            const fecha = document.getElementById('fecha').value;
            const motivo = document.getElementById('motivo').value.trim();

            if (!mascotaId) {
                e.preventDefault();
                alert('Por favor selecciona una mascota');
                return false;
            }

            if (!fecha) {
                e.preventDefault();
                alert('Por favor selecciona la fecha de la visita');
                return false;
            }

            if (!motivo) {
                e.preventDefault();
                alert('Por favor ingresa el motivo de la visita');
                return false;
            }

            const fechaSeleccionada = new Date(fecha);
            const hoy = new Date();
            hoy.setHours(0, 0, 0, 0);

            if (fechaSeleccionada > hoy) {
                e.preventDefault();
                alert('La fecha de la visita no puede ser futura');
                return false;
            }
        });
    }

    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.style.display = 'none';
            }, 300);
        }, 5000);
    });
});