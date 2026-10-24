<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Formulaire commande</title>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${empty commande}">Ajouter une commande</c:when>
        <c:otherwise>Modifier une commande</c:otherwise>
    </c:choose>
</h1>

<form action="${pageContext.request.contextPath}/commandes" method="post">

    <input type="hidden" name="action"
           value="${empty commande ? 'ajouter' : 'modifier'}">

    <c:if test="${not empty commande}">
        <input type="hidden" name="id" value="${commande.id}">
    </c:if>

    <p>
        <label for="clientId">Client :</label>
        <select id="clientId" name="clientId" required>
            <option value="">Choisir un client</option>

            <c:forEach var="client" items="${clients}">
                <option value="${client.id}"
                    <c:if test="${not empty commande and commande.client.id == client.id}">
                        selected
                    </c:if>>
                    <c:out value="${client.nom}"/>
                </option>
            </c:forEach>
        </select>
    </p>

    <p>
        <label for="statut">Statut :</label>
        <select id="statut" name="statut" required>
            <option value="EN_ATTENTE">En attente</option>
            <option value="VALIDEE">Validée</option>
            <option value="EXPEDIEE">Expédiée</option>
            <option value="ANNULEE">Annulée</option>
        </select>
    </p>

    <button type="submit">Enregistrer</button>
</form>

<p><a href="${pageContext.request.contextPath}/commandes">Retour à la liste</a></p>

</body>
</html>