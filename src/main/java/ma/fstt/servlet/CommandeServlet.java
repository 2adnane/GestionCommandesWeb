package ma.fstt.servlet;

import ma.fstt.dao.ClientDAO;
import ma.fstt.dao.CommandeDAO;
import ma.fstt.entities.Client;
import ma.fstt.entities.Commande;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/commandes")
public class CommandeServlet extends HttpServlet {

    private final CommandeDAO commandeDAO = new CommandeDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("ajouter".equals(action)) {
                chargerClients(request);
                afficherFormulaire(request, response);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Commande commande = commandeDAO.findById(id);

                if (commande == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Commande introuvable");
                    return;
                }

                request.setAttribute("commande", commande);
                chargerClients(request);
                afficherFormulaire(request, response);

            } else {
                List<Commande> commandes = commandeDAO.findAll();
                request.setAttribute("commandes", commandes);

                request.getRequestDispatcher(
                        "/WEB-INF/views/commandes/liste.jsp"
                ).forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Erreur lors de l'accès aux commandes", e);
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
                Commande commande = lireCommandeDuFormulaire(request);
                commandeDAO.save(commande);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Commande commande = commandeDAO.findById(id);

                if (commande == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Commande introuvable");
                    return;
                }

                Commande donneesFormulaire =
                        lireCommandeDuFormulaire(request);

                commande.setStatut(donneesFormulaire.getStatut());
                commande.setClient(donneesFormulaire.getClient());

                // On conserve la date existante de la commande.
                commandeDAO.update(commande);

            } else if ("supprimer".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                commandeDAO.delete(id);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Action inconnue");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/commandes");

        } catch (SQLException e) {
            throw new ServletException(
                    "Erreur lors de l'accès aux commandes", e);
        }
    }

    private Commande lireCommandeDuFormulaire(
            HttpServletRequest request) {

        Client client = new Client();
        client.setId(Integer.parseInt(request.getParameter("clientId")));

        Commande commande = new Commande();
        commande.setClient(client);
        commande.setStatut(request.getParameter("statut"));

        return commande;
    }

    private void chargerClients(HttpServletRequest request)
            throws SQLException {
        request.setAttribute("clients", clientDAO.findAll());
    }

    private void afficherFormulaire(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/commandes/formulaire.jsp"
        ).forward(request, response);
    }
}