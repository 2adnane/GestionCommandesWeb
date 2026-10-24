<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Formulaire produit</title>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${empty produit}">Ajouter un produit</c:when>
        <c:otherwise>Modifier un produit</c:otherwise>
    </c:choose>
</h1>

<form action="${pageContext.request.contextPath}/produits" method="post">

    <input type="hidden" name="action"
           value="${empty produit ? 'ajouter' : 'modifier'}">

    <c:if test="${not empty produit}">
        <input type="hidden" name="id" value="${produit.id}">
    </c:if>

    <p>
        <label for="nom">Nom :</label>
        <input id="nom" name="nom" type="text"
               value="<c:out value='${produit.nom}'/>" required>
    </p>

    <p>
        <label for="description">Description :</label>
        <textarea id="description" name="description"><c:out value="${produit.description}"/></textarea>
    </p>

    <p>
        <label for="prix">Prix :</label>
        <input id="prix" name="prix" type="number"
               min="0" step="0.01"
               value="<c:out value='${produit.prix}'/>" required>
    </p>

    <p>
        <label for="stock">Stock :</label>
        <input id="stock" name="stock" type="number"
               min="0"
               value="<c:out value='${produit.stock}'/>" required>
    </p>

    <button type="submit">Enregistrer</button>
</form>

<p><a href="${pageContext.request.contextPath}/produits">Retour à la liste</a></p>

</body>
</html>