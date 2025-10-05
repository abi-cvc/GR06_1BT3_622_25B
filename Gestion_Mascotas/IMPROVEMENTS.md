# 🎯 Mejoras Recomendadas para el Proyecto

## 🔴 Prioridad Alta (Seguridad)

### 1. Implementar Hashing de Contraseñas
**Estado Actual**: Las contraseñas se almacenan en texto plano ⚠️

**Riesgo**: Comprometimiento de cuentas de usuario

**Solución Recomendada**:

#### Agregar dependencia BCrypt
```xml
<!-- pom.xml -->
<dependency>
    <groupId>at.favre.lib</groupId>
    <artifactId>bcrypt</artifactId>
    <version>0.10.2</version>
</dependency>
```

#### Actualizar UsuarioDAO
```java
// Al crear usuario
public boolean crearUsuario(Usuario usuario) {
    // Hash de la contraseña antes de guardar
    String hashedPassword = BCrypt.withDefaults()
        .hashToString(12, usuario.getContrasena().toCharArray());
    usuario.setContrasena(hashedPassword);
    
    // ... resto del código
}

// Al validar login
public Usuario validarLogin(String nombreUsuario, String contrasena) {
    Usuario usuario = buscarPorNombreUsuario(nombreUsuario);
    
    if (usuario != null) {
        BCrypt.Result result = BCrypt.verifyer()
            .verify(contrasena.toCharArray(), usuario.getContrasena());
        
        if (result.verified) {
            return usuario;
        }
    }
    return null;
}
```

### 2. Implementar Filtro de Autenticación
**Problema**: Cada servlet verifica manualmente la sesión

**Solución**: Crear un filtro de autenticación

```java
package com.gestion.mascotas.filtro;

@WebFilter(urlPatterns = {"/dashboard", "/mascota", "/vacuna", "/visita"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("usuario") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        chain.doFilter(request, response);
    }
}
```

### 3. Protección contra SQL Injection
**Estado Actual**: Uso de consultas parametrizadas ✅

**Recomendación**: Mantener este patrón, nunca concatenar SQL directamente

### 4. Validación de Entrada
**Problema**: Validación mínima en algunos formularios

**Solución**: Implementar validación robusta

```java
public class ValidationUtil {
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(regex);
    }
    
    public static boolean isValidPassword(String password) {
        // Mínimo 8 caracteres, al menos una letra y un número
        return password != null && 
               password.length() >= 8 && 
               password.matches(".*[A-Za-z].*") && 
               password.matches(".*\\d.*");
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim()
                   .replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;");
    }
}
```

## 🟡 Prioridad Media (Funcionalidad)

### 5. Implementar Estadísticas Reales en Dashboard
**Estado Actual**: Valores hardcodeados (0, 0, 0, 0)

**Solución**:

```java
// DashboardServlet.java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    Long usuarioId = usuario.getId();
    
    // Obtener estadísticas reales
    MascotaDAO mascotaDAO = new MascotaDAO();
    VacunaDAO vacunaDAO = new VacunaDAO();
    VisitaVeterinariaDAO visitaDAO = new VisitaVeterinariaDAO();
    
    int totalMascotas = mascotaDAO.contarPorUsuario(usuarioId);
    int totalVacunas = vacunaDAO.contarPorUsuario(usuarioId);
    int totalVisitas = visitaDAO.contarPorUsuario(usuarioId);
    int proximasVacunas = vacunaDAO.contarProximasVacunas(usuarioId, 30);
    
    request.setAttribute("totalMascotas", totalMascotas);
    request.setAttribute("totalVisitas", totalVisitas);
    request.setAttribute("totalVacunas", totalVacunas);
    request.setAttribute("proximasVacunas", proximasVacunas);
    
    request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
}
```

### 6. Agregar Métodos de Consulta en DAOs

```java
// MascotaDAO.java
public int contarPorUsuario(Long usuarioId) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(m) FROM Mascota m WHERE m.usuario.id = :usuarioId", 
            Long.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getSingleResult().intValue();
    } finally {
        em.close();
    }
}

public List<Mascota> obtenerPorUsuario(Long usuarioId) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m.usuario.id = :usuarioId", 
            Mascota.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    } finally {
        em.close();
    }
}
```

### 7. Implementar Edición de Mascotas/Vacunas/Visitas
**Estado Actual**: Solo crear, listar y eliminar

**Solución**: Agregar funcionalidad de edición

```java
// MascotaServlet.java
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    if (action == null) action = "listar";
    
    switch (action) {
        case "registrar":
            mostrarFormularioRegistro(request, response);
            break;
        case "editar":  // NUEVO
            mostrarFormularioEdicion(request, response);
            break;
        case "eliminar":
            eliminarMascota(request, response);
            break;
        default:
            listarMascotas(request, response);
            break;
    }
}

private void mostrarFormularioEdicion(HttpServletRequest request, 
                                     HttpServletResponse response)
        throws ServletException, IOException {
    Long id = Long.parseLong(request.getParameter("id"));
    Mascota mascota = mascotaDAO.obtenerPorId(id);
    request.setAttribute("mascota", mascota);
    request.getRequestDispatcher("jsp/mascota.jsp").forward(request, response);
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    
    if ("registrar".equals(action)) {
        registrarMascota(request, response);
    } else if ("editar".equals(action)) {  // NUEVO
        editarMascota(request, response);
    }
}

private void editarMascota(HttpServletRequest request, 
                          HttpServletResponse response)
        throws IOException, ServletException {
    Long id = Long.parseLong(request.getParameter("id"));
    String nombre = request.getParameter("nombre");
    String tipo = request.getParameter("tipo");
    
    Mascota mascota = mascotaDAO.obtenerPorId(id);
    if (mascota != null) {
        mascota.setNombre(nombre);
        mascota.setTipo(TipoMascota.valueOf(tipo));
        mascotaDAO.actualizar(mascota);
    }
    
    response.sendRedirect("mascota?action=listar");
}
```

### 8. Agregar Paginación a Listas
**Problema**: Todas las listas cargan todos los registros

**Solución**: Implementar paginación

```java
public List<Mascota> obtenerPaginado(int page, int size, Long usuarioId) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m.usuario.id = :usuarioId ORDER BY m.id DESC", 
            Mascota.class);
        query.setParameter("usuarioId", usuarioId);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    } finally {
        em.close();
    }
}
```

## 🟢 Prioridad Baja (Mejoras de Código)

### 9. Implementar Logging Profesional
**Problema**: Uso de System.out.println()

**Solución**: Usar SLF4J + Logback

```xml
<!-- pom.xml -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.14</version>
</dependency>
```

```java
// En lugar de System.out.println()
private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

logger.info("Usuario creado: {}", usuario.getNombreUsuario());
logger.error("Error al crear usuario", e);
```

### 10. Manejo Centralizado de Excepciones
**Problema**: Try-catch en cada método

**Solución**: Crear una clase de utilidad

```java
public class DAOException extends RuntimeException {
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    
    public static void handleDAOException(Exception e, String operation) {
        logger.error("Error en operación: {}", operation, e);
        throw new DAOException("Error al ejecutar: " + operation, e);
    }
}
```

### 11. Implementar Patrón DAO Factory
**Problema**: Creación manual de DAOs en cada servlet

**Solución**: Factory pattern

```java
public class DAOFactory {
    private static final DAOFactory instance = new DAOFactory();
    
    private DAOFactory() {}
    
    public static DAOFactory getInstance() {
        return instance;
    }
    
    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }
    
    public MascotaDAO getMascotaDAO() {
        return new MascotaDAO();
    }
    
    public VacunaDAO getVacunaDAO() {
        return new VacunaDAO();
    }
    
    public VisitaVeterinariaDAO getVisitaVeterinariaDAO() {
        return new VisitaVeterinariaDAO();
    }
}
```

### 12. Agregar Validación Bean Validation
**Solución**: Usar anotaciones Jakarta Validation

```xml
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>
```

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;
    
    @Email(message = "Email no válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;
    
    @Pattern(regexp = "\\d{10}", message = "Teléfono debe tener 10 dígitos")
    private String telefono;
    
    // ...
}
```

### 13. Implementar DTOs (Data Transfer Objects)
**Problema**: Se exponen entidades JPA directamente en la vista

**Solución**: Crear DTOs

```java
public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private String nombre;
    private String email;
    private String telefono;
    // NO incluir contraseña
    
    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        return dto;
    }
}
```

### 14. Agregar Pruebas Unitarias
**Estado Actual**: Sin pruebas

**Solución**: Implementar JUnit 5

```java
@ExtendWith(MockitoExtension.class)
class UsuarioDAOTest {
    
    @Mock
    private SessionFactory sessionFactory;
    
    @Mock
    private Session session;
    
    @InjectMocks
    private UsuarioDAO usuarioDAO;
    
    @Test
    void testCrearUsuario() {
        Usuario usuario = new Usuario("test", "Test User", 
                                     "test@test.com", "1234567890", "password");
        
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(mock(Transaction.class));
        
        boolean result = usuarioDAO.crearUsuario(usuario);
        
        assertTrue(result);
        verify(session).persist(usuario);
    }
}
```

## 📱 Mejoras de UI/UX

### 15. Implementar Mensajes Flash
**Problema**: Mensajes de error/éxito no persisten después de redirección

**Solución**: Usar sesión para mensajes flash

```java
public class FlashMessage {
    public static void setSuccess(HttpSession session, String message) {
        session.setAttribute("flashSuccess", message);
    }
    
    public static void setError(HttpSession session, String message) {
        session.setAttribute("flashError", message);
    }
    
    public static String getSuccess(HttpSession session) {
        String message = (String) session.getAttribute("flashSuccess");
        session.removeAttribute("flashSuccess");
        return message;
    }
    
    public static String getError(HttpSession session) {
        String message = (String) session.getAttribute("flashError");
        session.removeAttribute("flashError");
        return message;
    }
}
```

### 16. Agregar Confirmación de Eliminación
**Problema**: Eliminación directa sin confirmación

**Solución**: JavaScript para confirmación

```javascript
function confirmarEliminacion(id, nombre) {
    if (confirm('¿Estás seguro de eliminar a ' + nombre + '?')) {
        window.location.href = 'mascota?action=eliminar&id=' + id;
    }
}
```

### 17. Implementar Búsqueda y Filtros
**Solución**: Agregar funcionalidad de búsqueda

```java
public List<Mascota> buscar(String termino, Long usuarioId) {
    EntityManager em = emf.createEntityManager();
    try {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m.usuario.id = :usuarioId " +
            "AND LOWER(m.nombre) LIKE LOWER(:termino)", 
            Mascota.class);
        query.setParameter("usuarioId", usuarioId);
        query.setParameter("termino", "%" + termino + "%");
        return query.getResultList();
    } finally {
        em.close();
    }
}
```

## 🏗️ Arquitectura

### 18. Migrar a Arquitectura REST
**Futuro**: API RESTful para aplicaciones móviles

```java
@Path("/api/mascotas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MascotaResource {
    
    @GET
    public Response getMascotas(@Context SecurityContext sc) {
        // ...
    }
    
    @POST
    public Response createMascota(MascotaDTO mascota, @Context SecurityContext sc) {
        // ...
    }
}
```

### 19. Implementar Service Layer
**Problema**: Lógica de negocio mezclada en Servlets y DAOs

**Solución**: Capa de servicios

```java
@Service
public class MascotaService {
    private MascotaDAO mascotaDAO;
    private UsuarioDAO usuarioDAO;
    
    public MascotaDTO registrarMascota(MascotaDTO dto, Long usuarioId) {
        // Validaciones
        // Lógica de negocio
        // Llamadas a DAOs
        // Conversión a DTO
    }
}
```

## 📊 Resumen de Prioridades

| Mejora | Prioridad | Impacto | Esfuerzo |
|--------|-----------|---------|----------|
| Hashing de contraseñas | 🔴 Alta | Alto | Medio |
| Filtro de autenticación | 🔴 Alta | Alto | Bajo |
| Validación de entrada | 🔴 Alta | Alto | Medio |
| Estadísticas reales | 🟡 Media | Medio | Bajo |
| Edición de registros | 🟡 Media | Medio | Medio |
| Paginación | 🟡 Media | Medio | Medio |
| Logging profesional | 🟢 Baja | Bajo | Bajo |
| Pruebas unitarias | 🟢 Baja | Alto | Alto |

---

**Recomendación**: Empezar con las mejoras de prioridad alta para garantizar la seguridad de la aplicación.
