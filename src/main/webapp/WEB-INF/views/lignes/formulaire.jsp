<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Ligne de commande</title>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${empty ligne}">
            Ajouter un produit à la commande
        </c:when>
        <c:otherwise>
            Modifier une ligne de commande
        </c:otherwise>
    </c:choose>
</h1>

<form action="${pageContext.request.contextPath}/lignes" method="post">

    <input type="hidden" name="action"
           value="${empty ligne ? 'ajouter' : 'modifier'}">

    <input type="hidden" name="commandeId" value="${commande.id}">

    <c:if test="${not empty ligne}">
        <input type="hidden" name="id" value="${ligne.id}">
    </c:if>

    <p>
        <label for="produitId">Produit :</label>
        <select id="produitId" name="produitId" required>
            <option value="">Choisir un produit</option>

            <c:forEach var="produit" items="${produits}">
                <option value="${produit.id}"
                    <c:if test="${not empty ligne and ligne.produit.id == produit.id}">
                        selected
                    </c:if>>
                    <c:out value="${produit.nom}"/>
                    — <c:out value="${produit.prix}"/>
                </option>
            </c:forEach>
        </select>
    </p>

    <p>
        <label for="quantite">Quantité :</label>
        <input id="quantite" name="quantite" type="number"
               min="1" required
               value="<c:out value='${ligne.quantite}'/>">
    </p>

    <button type="submit">Enregistrer</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/lignes?commandeId=${commande.id}">
        Retour aux lignes de la commande
    </a>
</p>

</body>
</html>