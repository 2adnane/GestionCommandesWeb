<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Liste des clients</title>
</head>
<body>

<h1>Liste des clients</h1>

<p>
    <a href="${pageContext.request.contextPath}/clients?action=ajouter">
        Ajouter un client
    </a>
</p>

<c:if test="${empty clients}">
    <p>Aucun client enregistré.</p>
</c:if>

<c:if test="${not empty clients}">
    <table border="1">
        <thead>
        <tr>
            <th>Identifiant</th>
            <th>Nom</th>
            <th>Email</th>
            <th>Adresse</th>
            <th>Actions</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="client" items="${clients}">
            <tr>
                <td><c:out value="${client.id}"/></td>
                <td><c:out value="${client.nom}"/></td>
                <td><c:out value="${client.email}"/></td>
                <td><c:out value="${client.adresse}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/clients?action=modifier&id=${client.id}">
                        Modifier
                    </a>

                    <form action="${pageContext.request.contextPath}/clients"
                          method="post"
                          style="display:inline">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="id" value="${client.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

</body>
</html>