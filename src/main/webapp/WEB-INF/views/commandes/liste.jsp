<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Liste des commandes</title>
</head>
<body>

<h1>Liste des commandes</h1>

<p>
    <a href="${pageContext.request.contextPath}/commandes?action=ajouter">
        Ajouter une commande
    </a>
</p>

<c:if test="${empty commandes}">
    <p>Aucune commande enregistrée.</p>
</c:if>

<c:if test="${not empty commandes}">
    <table border="1">
        <thead>
        <tr>
            <th>Numéro</th>
            <th>Date</th>
            <th>Client</th>
            <th>Statut</th>
            <th>Actions</th>
            <th>Lignes</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="commande" items="${commandes}">
            <tr>
                <td><c:out value="${commande.id}"/></td>
                <td><c:out value="${commande.dateCommande}"/></td>
                <td><c:out value="${commande.client.nom}"/></td>
                <td><c:out value="${commande.statut}"/></td>

                <td>
                    <a href="${pageContext.request.contextPath}/commandes?action=modifier&id=${commande.id}">
                        Modifier
                    </a>

                    <form action="${pageContext.request.contextPath}/commandes"
                          method="post"
                          style="display:inline">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="id" value="${commande.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>

                <td>
                    <a href="${pageContext.request.contextPath}/lignes?commandeId=${commande.id}">
                        Gérer les produits
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/clients">Clients</a> |
    <a href="${pageContext.request.contextPath}/produits">Produits</a>
</p>

</body>
</html>