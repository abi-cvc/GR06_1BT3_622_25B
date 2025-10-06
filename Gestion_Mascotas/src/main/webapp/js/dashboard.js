// ===== GESTIÓN DE MODALES =====

// Modal Editar Perfil
function abrirModalEditar() {
    document.getElementById('modalEditarPerfil').style.display = 'flex';
}

function cerrarModalEditar() {
    document.getElementById('modalEditarPerfil').style.display = 'none';
    document.getElementById('formEditarPerfil').reset();
    document.getElementById('cambiarContrasena').checked = false;
    document.getElementById('camposNuevaContrasena').style.display = 'none';
}

// Modal Eliminar Perfil
function confirmarEliminarPerfil() {
    document.getElementById('modalEliminar').style.display = 'flex';
}

function cerrarModalEliminar() {
    document.getElementById('modalEliminar').style.display = 'none';
    document.getElementById('formEliminarPerfil').reset();
    document.getElementById('confirmarEliminacion').checked = false;
}

// Modal Logout
function confirmarLogout() {
    document.getElementById('modalLogout').style.display = 'flex';
}

function cerrarModalLogout() {
    document.getElementById('modalLogout').style.display = 'none';
}

// Modal Registrar Mascota
function abrirModalRegistrarMascota() {
    document.getElementById('modalRegistrarMascota').style.display = 'flex';
}

function cerrarModalRegistrarMascota() {
    document.getElementById('modalRegistrarMascota').style.display = 'none';
    document.getElementById('formRegistrarMascota').reset();
}

// ===== CERRAR MODALES AL HACER CLIC FUERA =====
window.onclick = function(event) {
    const modales = ['modalEditarPerfil', 'modalEliminar', 'modalLogout', 'modalRegistrarMascota'];
    modales.forEach(modalId => {
        const modal = document.getElementById(modalId);
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}

// ===== GESTIÓN DE CONTRASEÑAS =====
function toggleCambiarContrasena() {
    const checkbox = document.getElementById('cambiarContrasena');
    const campos = document.getElementById('camposNuevaContrasena');
    const nuevaContrasena = document.getElementById('nuevaContrasena');
    const confirmarContrasena = document.getElementById('confirmarNuevaContrasena');

    if (checkbox.checked) {
        campos.style.display = 'block';
        nuevaContrasena.required = true;
        confirmarContrasena.required = true;
    } else {
        campos.style.display = 'none';
        nuevaContrasena.required = false;
        confirmarContrasena.required = false;
        nuevaContrasena.value = '';
        confirmarContrasena.value = '';
    }
}

function togglePassword(fieldId) {
    const field = document.getElementById(fieldId);
    const icon = document.getElementById('toggle-icon-' + fieldId);

    if (field.type === 'password') {
        field.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        field.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// ===== VALIDACIÓN DE FORMULARIOS =====

// Validar formulario de editar perfil
document.addEventListener('DOMContentLoaded', function() {
    const formEditar = document.getElementById('formEditarPerfil');
    if (formEditar) {
        formEditar.addEventListener('submit', function(e) {
            const cambiarContrasena = document.getElementById('cambiarContrasena').checked;

            if (cambiarContrasena) {
                const nuevaContrasena = document.getElementById('nuevaContrasena').value;
                const confirmarContrasena = document.getElementById('confirmarNuevaContrasena').value;

                if (nuevaContrasena !== confirmarContrasena) {
                    e.preventDefault();
                    alert('Las contraseñas nuevas no coinciden');
                    return false;
                }

                if (nuevaContrasena.length < 6) {
                    e.preventDefault();
                    alert('La nueva contraseña debe tener al menos 6 caracteres');
                    return false;
                }
            }

            const contrasenaActual = document.getElementById('contrasenaActual').value;
            if (!contrasenaActual) {
                e.preventDefault();
                alert('Debes ingresar tu contraseña actual para confirmar los cambios');
                return false;
            }
        });
    }

    // Validar formulario de registrar mascota
    const formRegistrarMascota = document.getElementById('formRegistrarMascota');
    if (formRegistrarMascota) {
        formRegistrarMascota.addEventListener('submit', function(e) {
            const nombre = document.getElementById('nombreMascota').value.trim();
            const tipo = document.getElementById('tipoMascota').value;
            const raza = document.getElementById('razaMascota').value.trim();
            const edad = document.getElementById('edadMascota').value;
            const peso = document.getElementById('pesoMascota').value;

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

    // Validar formulario de eliminar perfil
    const formEliminar = document.getElementById('formEliminarPerfil');
    if (formEliminar) {
        formEliminar.addEventListener('submit', function(e) {
            const confirmar = document.getElementById('confirmarEliminacion').checked;
            const contrasena = document.getElementById('contrasenaEliminar').value;

            if (!confirmar) {
                e.preventDefault();
                alert('Debes confirmar que entiendes que esta acción no se puede deshacer');
                return false;
            }

            if (!contrasena) {
                e.preventDefault();
                alert('Debes ingresar tu contraseña para eliminar tu perfil');
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

// ===== VALIDACIÓN DE TELÉFONO EN TIEMPO REAL =====
document.addEventListener('DOMContentLoaded', function() {
    const telefonoInput = document.getElementById('telefono');
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function(e) {
            this.value = this.value.replace(/[^0-9]/g, '').substring(0, 10);
        });
    }
});

// ===== MANEJO DE ERRORES EN URL =====
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const success = urlParams.get('success');

    if (error) {
        const mensajesError = {
            'mascota_no_encontrada': '❌ Mascota no encontrada',
            'acceso_denegado': '⛔ No tienes permiso para realizar esta acción',
            'error_eliminando': '❌ Error al eliminar la mascota',
            'accion_desconocida': '❌ Acción desconocida'
        };

        const mensaje = mensajesError[error] || '❌ Ha ocurrido un error';
        mostrarNotificacion(mensaje, 'error');
    }

    if (success) {
        const mensajesSuccess = {
            'mascota_eliminada': '✅ Mascota eliminada exitosamente',
            'registrado': '✅ Mascota registrada exitosamente',
            'actualizado': '✅ Mascota actualizada exitosamente'
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