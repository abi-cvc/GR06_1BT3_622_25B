<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Redirigir a la página de login
    response.sendRedirect(request.getContextPath() + "/login");
%>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>