<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Lignes de commande</title>
</head>
<body>

<h1>Lignes de la commande n°
    <c:out value="${commande.id}"/>
</h1>

<p>Client : <c:out value="${commande.client.nom}"/></p>
<p>Statut : <c:out value="${commande.statut}"/></p>

<p>
    <a href="${pageContext.request.contextPath}/lignes?action=ajouter&commandeId=${commande.id}">
        Ajouter un produit à cette commande
    </a>
</p>

<c:if test="${empty lignes}">
    <p>Cette commande ne contient encore aucun produit.</p>
</c:if>

<c:if test="${not empty lignes}">
    <table border="1">
        <thead>
        <tr>
            <th>Produit</th>
            <th>Quantité</th>
            <th>Prix unitaire au moment de la commande</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="ligne" items="${lignes}">
            <tr>
                <td><c:out value="${ligne.produit.nom}"/></td>
                <td><c:out value="${ligne.quantite}"/></td>
                <td><c:out value="${ligne.prixUnitaire}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/lignes?action=modifier&id=${ligne.id}">
                        Modifier
                    </a>

                    <form action="${pageContext.request.contextPath}/lignes"
                          method="post"
                          style="display:inline">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="id" value="${ligne.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/commandes">
        Retour aux commandes
    </a>
</p>

</body>
</html>