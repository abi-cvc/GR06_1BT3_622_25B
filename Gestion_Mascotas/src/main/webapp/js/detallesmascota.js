// detallesmascota.js

// Cerrar modal si se hace clic fuera de él
window.onclick = function(event) {
    const modalEditarPerfil = document.getElementById('modalEditarPerfil');
    const modalRecordatorioAlimentacion = document.getElementById('modalRecordatorioAlimentacion');

    if (event.target === modalEditarPerfil) {
        modalEditarPerfil.style.display = 'none';
    }
    if (event.target === modalRecordatorioAlimentacion) {
        modalRecordatorioAlimentacion.style.display = 'none';
    }
}

// --- Funciones para el Nuevo Modal de Recordatorio de Alimentación ---

function abrirModalRecordatorioAlimentacion() {
    document.getElementById('modalRecordatorioAlimentacion').style.display = 'block';
    // Resetear el formulario al abrir
    document.getElementById('formRecordatorioAlimentacion').reset();
    document.getElementById('horariosContainer').innerHTML = ''; // Limpiar campos de horario
}

function cerrarModalRecordatorioAlimentacion() {
    document.getElementById('modalRecordatorioAlimentacion').style.display = 'none';
}

function generarCamposHorario() {
    const frecuencia = document.getElementById('frecuencia').value;
    const horariosContainer = document.getElementById('horariosContainer');
    horariosContainer.innerHTML = ''; // Limpiar campos anteriores

    if (frecuencia) {
        for (let i = 1; i <= parseInt(frecuencia); i++) {
            const div = document.createElement('div');
            div.classList.add('form-group');
            div.innerHTML = `
                <label for="horario${i}">Horario ${i}:</label>
                <input type="time" id="horario${i}" name="horario${i}" required>
            `;
            horariosContainer.appendChild(div);
        }
    }
}

// Manejo de mensajes de alerta (ya está en el JSP, pero se puede centralizar aquí si se desea)
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
        }
        // Puedes añadir más mensajes de éxito aquí
        if (message) {
            mostrarAlerta(message, 'success');
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
        }
        // Puedes añadir más mensajes de error aquí
        if (message) {
            mostrarAlerta(message, 'error');
        }
    }

    function mostrarAlerta(message, type) {
        const container = document.querySelector('.container'); // O donde quieras mostrar la alerta
        let alertDiv = document.createElement('div');
        alertDiv.classList.add('alert', type);
        alertDiv.innerHTML = `<i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i> ${message}`;
        container.prepend(alertDiv); // Añadir al principio del contenedor

        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    }
});