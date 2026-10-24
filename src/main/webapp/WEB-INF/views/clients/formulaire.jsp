<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Formulaire client</title>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${empty client}">Ajouter un client</c:when>
        <c:otherwise>Modifier un client</c:otherwise>
    </c:choose>
</h1>

<form action="${pageContext.request.contextPath}/clients" method="post">

    <input type="hidden" name="action"
           value="${empty client ? 'ajouter' : 'modifier'}">

    <c:if test="${not empty client}">
        <input type="hidden" name="id" value="${client.id}">
    </c:if>

    <p>
        <label for="nom">Nom :</label>
        <input id="nom" name="nom" type="text"
               value="<c:out value='${client.nom}'/>" required>
    </p>

    <p>
        <label for="email">Email :</label>
        <input id="email" name="email" type="email"
               value="<c:out value='${client.email}'/>" required>
    </p>

    <p>
        <label for="adresse">Adresse :</label>
        <input id="adresse" name="adresse" type="text"
               value="<c:out value='${client.adresse}'/>">
    </p>

    <button type="submit">Enregistrer</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/clients">
        Retour à la liste
    </a>
</p>

</body>
</html>