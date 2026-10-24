<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
    <title>Produits</title>
</head>
<body>
<h1>Liste des produits</h1>

<p>
    <a href="${pageContext.request.contextPath}/produits?action=ajouter">
        Ajouter un produit
    </a>
</p>

<c:if test="${empty produits}">
    <p>Aucun produit enregistré.</p>
</c:if>

<c:if test="${not empty produits}">
    <table border="1">
        <thead>
        <tr>
            <th>Nom</th>
            <th>Description</th>
            <th>Prix</th>
            <th>Stock</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="produit" items="${produits}">
            <tr>
                <td><c:out value="${produit.nom}"/></td>
                <td><c:out value="${produit.description}"/></td>
                <td><c:out value="${produit.prix}"/></td>
                <td><c:out value="${produit.stock}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/produits?action=modifier&id=${produit.id}">
                        Modifier
                    </a>

                    <form action="${pageContext.request.contextPath}/produits"
                          method="post"
                          style="display:inline">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="id" value="${produit.id}">
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