package ma.fstt.servlet;

import ma.fstt.dao.CommandeDAO;
import ma.fstt.dao.LigneDeCommandeDAO;
import ma.fstt.dao.ProduitDAO;
import ma.fstt.entities.Commande;
import ma.fstt.entities.LigneDeCommande;
import ma.fstt.entities.Produit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/lignes")
public class LigneDeCommandeServlet extends HttpServlet {

    private final LigneDeCommandeDAO ligneDAO =
            new LigneDeCommandeDAO();
    private final CommandeDAO commandeDAO =
            new CommandeDAO();
    private final ProduitDAO produitDAO =
            new ProduitDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                int commandeId =
                        Integer.parseInt(request.getParameter("commandeId"));
                Commande commande = commandeDAO.findById(commandeId);

                if (commande == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Commande introuvable");
                    return;
                }

                preparerFormulaire(request, commande, null);
                afficherFormulaire(request, response);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                LigneDeCommande ligne = ligneDAO.findById(id);

                if (ligne == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Ligne de commande introuvable");
                    return;
                }

                preparerFormulaire(request, ligne.getCommande(), ligne);
                afficherFormulaire(request, response);

            } else {
                int commandeId =
                        Integer.parseInt(request.getParameter("commandeId"));
                Commande commande = commandeDAO.findById(commandeId);

                if (commande == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Commande introuvable");
                    return;
                }

                request.setAttribute("commande", commande);
                request.setAttribute(
                        "lignes",
                        ligneDAO.findByCommandeId(commandeId)
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/lignes/liste.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException(
                    "Erreur lors de l'accès aux lignes de commande", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            int commandeId;

            if ("ajouter".equals(action)) {
                commandeId =
                        Integer.parseInt(request.getParameter("commandeId"));

                Commande commande = commandeDAO.findById(commandeId);
                Produit produit = chargerProduit(request);

                if (commande == null || produit == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Commande ou produit introuvable");
                    return;
                }

                int quantite =
                        Integer.parseInt(request.getParameter("quantite"));

                if (quantite < 1) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "La quantité doit être au moins égale à 1");
                    return;
                }

                LigneDeCommande ligne = new LigneDeCommande();
                ligne.setCommande(commande);
                ligne.setProduit(produit);
                ligne.setQuantite(quantite);

                // On enregistre le prix actuel comme prix de cette commande.
                ligne.setPrixUnitaire(produit.getPrix());

                ligneDAO.save(ligne);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                LigneDeCommande ligne = ligneDAO.findById(id);

                if (ligne == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Ligne de commande introuvable");
                    return;
                }

                commandeId = ligne.getCommande().getId();

                Produit produit = chargerProduit(request);
                if (produit == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Produit introuvable");
                    return;
                }

                int quantite =
                        Integer.parseInt(request.getParameter("quantite"));

                if (quantite < 1) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "La quantité doit être au moins égale à 1");
                    return;
                }

                ligne.setProduit(produit);
                ligne.setQuantite(quantite);
                ligne.setPrixUnitaire(produit.getPrix());

                ligneDAO.update(ligne);

            } else if ("supprimer".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                LigneDeCommande ligne = ligneDAO.findById(id);

                if (ligne == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Ligne de commande introuvable");
                    return;
                }

                commandeId = ligne.getCommande().getId();
                ligneDAO.delete(id);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Action inconnue");
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/lignes?commandeId=" + commandeId
            );

        } catch (SQLException e) {
            throw new ServletException(
                    "Erreur lors de l'accès aux lignes de commande", e);
        }
    }

    private Produit chargerProduit(HttpServletRequest request)
            throws SQLException {
        int produitId =
                Integer.parseInt(request.getParameter("produitId"));
        return produitDAO.findById(produitId);
    }

    private void preparerFormulaire(HttpServletRequest request,
                                    Commande commande,
                                    LigneDeCommande ligne)
            throws SQLException {
        request.setAttribute("commande", commande);
        request.setAttribute("ligne", ligne);
        request.setAttribute("produits", produitDAO.findAll());
    }

    private void afficherFormulaire(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/lignes/formulaire.jsp"
        ).forward(request, response);
    }
}