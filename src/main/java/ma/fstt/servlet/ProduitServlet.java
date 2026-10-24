package ma.fstt.servlet;

import ma.fstt.dao.ProduitDAO;
import ma.fstt.entities.Produit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/produits")
public class ProduitServlet extends HttpServlet {

    private final ProduitDAO produitDAO = new ProduitDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                afficherFormulaire(request, response);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Produit produit = produitDAO.findById(id);

                if (produit == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Produit introuvable");
                    return;
                }

                request.setAttribute("produit", produit);
                afficherFormulaire(request, response);

            } else {
                List<Produit> produits = produitDAO.findAll();
                request.setAttribute("produits", produits);

                request.getRequestDispatcher(
                        "/WEB-INF/views/produits/liste.jsp"
                ).forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Erreur lors de l'accès aux produits", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                produitDAO.save(lireProduitDuFormulaire(request));

            } else if ("modifier".equals(action)) {
                Produit produit = lireProduitDuFormulaire(request);
                produit.setId(Integer.parseInt(request.getParameter("id")));
                produitDAO.update(produit);

            } else if ("supprimer".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                produitDAO.delete(id);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Action inconnue");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/produits");

        } catch (SQLException e) {
            throw new ServletException(
                    "Erreur lors de l'accès aux produits", e);
        }
    }

    private Produit lireProduitDuFormulaire(HttpServletRequest request) {
        Produit produit = new Produit();
        produit.setNom(request.getParameter("nom"));
        produit.setDescription(request.getParameter("description"));
        produit.setPrix(new BigDecimal(request.getParameter("prix")));
        produit.setStock(Integer.parseInt(request.getParameter("stock")));
        return produit;
    }

    private void afficherFormulaire(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/produits/formulaire.jsp"
        ).forward(request, response);
    }
}