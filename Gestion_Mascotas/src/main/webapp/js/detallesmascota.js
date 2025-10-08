// detallesmascota.js

// Cerrar modal si se hace clic fuera de él
window.onclick = function(event) {
    const modalEditarPerfil = document.getElementById('modalEditarPerfil');
    const modalRecordatorioAlimentacion = document.getElementById('recordatorioAlimentacionModal');
    const modalRecordatorioPaseo = document.getElementById('recordatorioPaseoModal');
    const modalEliminarAlimentacion = document.getElementById('modalEliminarRecordatorioAlimentacion');
    const modalEliminarPaseo = document.getElementById('modalEliminarRecordatorioPaseo');

    if (modalEditarPerfil && event.target === modalEditarPerfil) {
        modalEditarPerfil.style.display = 'none';
    }
    if (modalRecordatorioAlimentacion && event.target === modalRecordatorioAlimentacion) {
        modalRecordatorioAlimentacion.style.display = 'none';
    }
    if (modalRecordatorioPaseo && event.target === modalRecordatorioPaseo) {
        modalRecordatorioPaseo.style.display = 'none';
    }
    if (modalEliminarAlimentacion && event.target === modalEliminarAlimentacion) {
        modalEliminarAlimentacion.style.display = 'none';
    }
    if (modalEliminarPaseo && event.target === modalEliminarPaseo) {
        modalEliminarPaseo.style.display = 'none';
    }
}

// --- Funciones para el Modal de Recordatorio de Alimentación ---

function abrirModalRecordatorioAlimentacion() {
    const modal = document.getElementById('recordatorioAlimentacionModal');
    if (modal) {
        modal.style.display = 'block';
        document.getElementById('formRecordatorioAlimentacion').reset();
        document.getElementById('horariosContainer').innerHTML = '';
    }
}

function cerrarModalRecordatorioAlimentacion() {
    const modal = document.getElementById('recordatorioAlimentacionModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

function generarCamposHorario() {
    const frecuencia = document.getElementById('frecuencia').value;
    const horariosContainer = document.getElementById('horariosContainer');
    horariosContainer.innerHTML = '';

    if (frecuencia) {
        for (let i = 1; i <= parseInt(frecuencia); i++) {
            const div = document.createElement('div');
            div.classList.add('form-group');
            div.innerHTML = `
                <label for="horario${i}"><i class="fas fa-bell"></i> Horario ${i}:</label>
                <input type="time" id="horario${i}" name="horario${i}" required class="form-control">
            `;
            horariosContainer.appendChild(div);
        }
    }
}

// --- Funciones para Modales de Eliminación ---

function confirmarEliminarRecordatorioAlimentacion(id, tipoAlimento) {
    document.getElementById('eliminarIdRecordatorioAlimentacion').value = id;
    document.getElementById('eliminarTipoAlimento').textContent = tipoAlimento;
    document.getElementById('modalEliminarRecordatorioAlimentacion').style.display = 'block';
}

function cerrarModalEliminarRecordatorioAlimentacion() {
    document.getElementById('modalEliminarRecordatorioAlimentacion').style.display = 'none';
}

function confirmarEliminarRecordatorioPaseo(id) {
    document.getElementById('eliminarIdRecordatorioPaseo').value = id;
    document.getElementById('modalEliminarRecordatorioPaseo').style.display = 'block';
}

function cerrarModalEliminarRecordatorioPaseo() {
    document.getElementById('modalEliminarRecordatorioPaseo').style.display = 'none';
}

// Manejo de mensajes de alerta
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success');
    const error = urlParams.get('error');

    if (success) {
        let message = '';
        if (success === 'recordatorio_alimentacion_registrado') {
            message = '✅ Recordatorio de alimentación registrado exitosamente.';
        } else if (success === 'recordatorio_alimentacion_eliminado') {
            message = '✅ Recordatorio de alimentación eliminado exitosamente.';
        } else if (success === 'recordatorio_paseo_registrado') {
            message = '✅ Recordatorio de paseo registrado exitosamente.';
        } else if (success === 'recordatorio_paseo_eliminado') {
            message = '✅ Recordatorio de paseo eliminado exitosamente.';
        }

        if (message) {
            console.log(message);
            // Las alertas ya se muestran en el JSP, esto es solo para debugging
        }
    }

    if (error) {
        let message = '';
        if (error === 'mascota_no_encontrada') {
            message = '❌ Error: Mascota no encontrada.';
        } else if (error === 'horarios_vacios') {
            message = '❌ Error: Debes especificar al menos un horario.';
        } else if (error === 'formato_invalido') {
            message = '❌ Error: Formato de datos inválido.';
        } else if (error === 'error_registro_recordatorio_alimentacion') {
            message = '❌ Error al registrar el recordatorio de alimentación.';
        } else if (error === 'error_eliminando_recordatorio_alimentacion') {
            message = '❌ Error al eliminar el recordatorio de alimentación.';
        } else if (error === 'error_eliminando_recordatorio_paseo') {
            message = '❌ Error al eliminar el recordatorio de paseo.';
        }

        if (message) {
            console.error(message);
        }
    }
});