// ============================================
// FUNCIONES DE MODALES
// ============================================

function abrirModalEditar() {
    document.getElementById('modalEditarPerfil').style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function cerrarModalEditar() {
    document.getElementById('modalEditarPerfil').style.display = 'none';
    document.body.style.overflow = 'auto';
    document.getElementById('formEditarPerfil').reset();
    document.getElementById('camposNuevaContrasena').style.display = 'none';
    document.getElementById('cambiarContrasena').checked = false;
}

function confirmarEliminarPerfil() {
    document.getElementById('modalEliminar').style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function cerrarModalEliminar() {
    document.getElementById('modalEliminar').style.display = 'none';
    document.body.style.overflow = 'auto';
    document.getElementById('formEliminarPerfil').reset();
}

function confirmarLogout() {
    document.getElementById('modalLogout').style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function cerrarModalLogout() {
    document.getElementById('modalLogout').style.display = 'none';
    document.body.style.overflow = 'auto';
}

// ============================================
// TOGGLE PASSWORD
// ============================================

function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById('toggle-icon-' + inputId);
    
    if (input && icon) {
        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    }
}

// ============================================
// TOGGLE CAMBIAR CONTRASEÑA
// ============================================

function toggleCambiarContrasena() {
    const checkbox = document.getElementById('cambiarContrasena');
    const campos = document.getElementById('camposNuevaContrasena');
    const nuevaContrasena = document.getElementById('nuevaContrasena');
    const confirmarNuevaContrasena = document.getElementById('confirmarNuevaContrasena');
    
    if (checkbox.checked) {
        campos.style.display = 'block';
        nuevaContrasena.required = true;
        confirmarNuevaContrasena.required = true;
    } else {
        campos.style.display = 'none';
        nuevaContrasena.required = false;
        confirmarNuevaContrasena.required = false;
        nuevaContrasena.value = '';
        confirmarNuevaContrasena.value = '';
    }
}

// ============================================
// VALIDACIÓN DE FORMULARIO DE EDICIÓN
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    const formEditar = document.getElementById('formEditarPerfil');
    
    if (formEditar) {
        formEditar.addEventListener('submit', function(e) {
            const contrasenaActual = document.getElementById('contrasenaActual').value;
            
            if (!contrasenaActual) {
                e.preventDefault();
                alert('⚠️ Debes ingresar tu contraseña actual para guardar los cambios.');
                document.getElementById('contrasenaActual').focus();
                return false;
            }
            
            // Validar contraseñas nuevas si se está cambiando
            const cambiarContrasena = document.getElementById('cambiarContrasena').checked;
            if (cambiarContrasena) {
                const nuevaContrasena = document.getElementById('nuevaContrasena').value;
                const confirmarNuevaContrasena = document.getElementById('confirmarNuevaContrasena').value;
                
                if (nuevaContrasena.length < 6) {
                    e.preventDefault();
                    alert('⚠️ La nueva contraseña debe tener al menos 6 caracteres.');
                    document.getElementById('nuevaContrasena').focus();
                    return false;
                }
                
                if (nuevaContrasena !== confirmarNuevaContrasena) {
                    e.preventDefault();
                    alert('⚠️ Las contraseñas nuevas no coinciden.');
                    document.getElementById('confirmarNuevaContrasena').focus();
                    return false;
                }
            }
            
            return true;
        });
    }
    
    // Validación de teléfono (solo números)
    const telefonoInput = document.getElementById('telefono');
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function() {
            this.value = this.value.replace(/\D/g, '');
            if (this.value.length > 10) {
                this.value = this.value.slice(0, 10);
            }
        });
    }
    
    // Auto-hide alerts
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.animation = 'slideOut 0.3s ease-out';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
});

// ============================================
// CERRAR MODAL AL HACER CLICK FUERA
// ============================================

window.onclick = function(event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        }
    });
}

// ============================================
// CERRAR MODAL CON TECLA ESC
// ============================================

document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
            if (modal.style.display === 'flex') {
                modal.style.display = 'none';
                document.body.style.overflow = 'auto';
            }
        });
    }
});
