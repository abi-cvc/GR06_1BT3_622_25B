// ============================================
// VALIDACIÓN DE REGISTRO
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registroForm');
    const passwordInput = document.getElementById('contrasena');
    const confirmPasswordInput = document.getElementById('confirmarContrasena');
    const nombreUsuarioInput = document.getElementById('nombreUsuario');
    const emailInput = document.getElementById('email');
    const telefonoInput = document.getElementById('telefono');

    // Validación de nombre de usuario
    if (nombreUsuarioInput) {
        nombreUsuarioInput.addEventListener('input', function() {
            const value = this.value;
            
            // Remover espacios
            if (value.includes(' ')) {
                this.value = value.replace(/\s/g, '');
            }
            
            // Validar solo caracteres alfanuméricos y guiones
            const regex = /^[a-zA-Z0-9_-]*$/;
            if (!regex.test(this.value)) {
                this.value = this.value.replace(/[^a-zA-Z0-9_-]/g, '');
            }
        });
    }

    // Validación de teléfono
    if (telefonoInput) {
        telefonoInput.addEventListener('input', function() {
            // Solo números
            this.value = this.value.replace(/\D/g, '');
            
            // Máximo 10 dígitos
            if (this.value.length > 10) {
                this.value = this.value.slice(0, 10);
            }
        });
    }

    // Medidor de fortaleza de contraseña
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            checkPasswordStrength(this.value);
            checkPasswordMatch();
        });
    }

    // Validación de coincidencia de contraseñas
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', checkPasswordMatch);
    }

    // Validación del formulario antes de enviar
    if (form) {
        form.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
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
// FUNCIÓN: Toggle Password Visibility
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
// FUNCIÓN: Check Password Strength
// ============================================
function checkPasswordStrength(password) {
    const strengthBar = document.querySelector('.strength-bar');
    const passwordHelp = document.getElementById('password-help');
    
    if (!strengthBar || !passwordHelp) return;
    
    let strength = 0;
    let message = '';
    
    // Criterios de fortaleza
    const criteria = {
        length: password.length >= 8,
        lowercase: /[a-z]/.test(password),
        uppercase: /[A-Z]/.test(password),
        numbers: /\d/.test(password),
        special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
    };
    
    // Calcular fortaleza
    if (password.length >= 6) strength++;
    if (criteria.length) strength++;
    if (criteria.lowercase && criteria.uppercase) strength++;
    if (criteria.numbers) strength++;
    if (criteria.special) strength++;
    
    // Actualizar barra y mensaje
    strengthBar.className = 'strength-bar';
    
    if (password.length === 0) {
        strengthBar.style.width = '0';
        message = 'Mínimo 6 caracteres';
        passwordHelp.style.color = 'var(--text-secondary)';
    } else if (strength <= 2) {
        strengthBar.classList.add('weak');
        message = '⚠️ Contraseña débil - Agrega más caracteres';
        passwordHelp.style.color = 'var(--error-color)';
    } else if (strength <= 4) {
        strengthBar.classList.add('medium');
        message = '✓ Contraseña media - Puedes mejorarla';
        passwordHelp.style.color = 'var(--warning-color)';
    } else {
        strengthBar.classList.add('strong');
        message = '✓ Contraseña fuerte';
        passwordHelp.style.color = 'var(--success-color)';
    }
    
    passwordHelp.textContent = message;
}

// ============================================
// FUNCIÓN: Check Password Match
// ============================================
function checkPasswordMatch() {
    const password = document.getElementById('contrasena');
    const confirmPassword = document.getElementById('confirmarContrasena');
    const confirmHelp = document.getElementById('confirm-help');
    
    if (!password || !confirmPassword || !confirmHelp) return;
    
    if (confirmPassword.value === '') {
        confirmHelp.textContent = '';
        confirmHelp.style.color = 'var(--text-secondary)';
        return;
    }
    
    if (password.value === confirmPassword.value) {
        confirmHelp.textContent = '✓ Las contraseñas coinciden';
        confirmHelp.style.color = 'var(--success-color)';
        confirmPassword.style.borderColor = 'var(--success-color)';
    } else {
        confirmHelp.textContent = '✗ Las contraseñas no coinciden';
        confirmHelp.style.color = 'var(--error-color)';
        confirmPassword.style.borderColor = 'var(--error-color)';
    }
}

// ============================================
// FUNCIÓN: Validate Form
// ============================================
function validateForm() {
    let isValid = true;
    let errors = [];
    
    // Validar nombre de usuario
    const nombreUsuario = document.getElementById('nombreUsuario').value.trim();
    if (nombreUsuario.length < 3) {
        errors.push('El nombre de usuario debe tener al menos 3 caracteres');
        isValid = false;
    }
    
    // Validar nombre completo
    const nombre = document.getElementById('nombre').value.trim();
    if (nombre.length < 2) {
        errors.push('El nombre completo debe tener al menos 2 caracteres');
        isValid = false;
    }
    
    // Validar email
    const email = document.getElementById('email').value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        errors.push('Por favor ingresa un email válido');
        isValid = false;
    }
    
    // Validar teléfono (si está presente)
    const telefono = document.getElementById('telefono').value.trim();
    if (telefono && telefono.length !== 10) {
        errors.push('El teléfono debe tener exactamente 10 dígitos');
        isValid = false;
    }
    
    // Validar contraseña
    const password = document.getElementById('contrasena').value;
    if (password.length < 6) {
        errors.push('La contraseña debe tener al menos 6 caracteres');
        isValid = false;
    }
    
    // Validar coincidencia de contraseñas
    const confirmPassword = document.getElementById('confirmarContrasena').value;
    if (password !== confirmPassword) {
        errors.push('Las contraseñas no coinciden');
        isValid = false;
    }
    
    // Validar términos y condiciones
    const aceptarTerminos = document.querySelector('input[name="aceptarTerminos"]');
    if (aceptarTerminos && !aceptarTerminos.checked) {
        errors.push('Debes aceptar los términos y condiciones');
        isValid = false;
    }
    
    // Mostrar errores si hay
    if (!isValid) {
        showValidationErrors(errors);
    }
    
    return isValid;
}

// ============================================
// FUNCIÓN: Show Validation Errors
// ============================================
function showValidationErrors(errors) {
    // Remover alertas existentes
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());
    
    // Crear nueva alerta
    const alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-error';
    alertDiv.innerHTML = `
        <i class="fas fa-exclamation-circle"></i>
        <div>
            <strong>Por favor corrige los siguientes errores:</strong>
            <ul style="margin: 0.5rem 0 0 1.5rem; padding: 0;">
                ${errors.map(error => `<li>${error}</li>`).join('')}
            </ul>
        </div>
    `;
    
    // Insertar antes del formulario
    const form = document.getElementById('registroForm');
    form.parentNode.insertBefore(alertDiv, form);
    
    // Scroll hacia la alerta
    alertDiv.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    
    // Auto-hide después de 8 segundos
    setTimeout(() => {
        alertDiv.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => alertDiv.remove(), 300);
    }, 8000);
}

// ============================================
// FUNCIÓN: Format Phone Number
// ============================================
function formatPhoneNumber(input) {
    const value = input.value.replace(/\D/g, '');
    let formattedValue = '';
    
    if (value.length > 0) {
        formattedValue = value.substring(0, 3);
    }
    if (value.length > 3) {
        formattedValue += '-' + value.substring(3, 6);
    }
    if (value.length > 6) {
        formattedValue += '-' + value.substring(6, 10);
    }
    
    input.value = formattedValue;
}
