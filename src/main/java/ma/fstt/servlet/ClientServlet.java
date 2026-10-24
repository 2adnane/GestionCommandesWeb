package ma.fstt.servlet;

import ma.fstt.dao.ClientDAO;
import ma.fstt.entities.Client;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

    private final ClientDAO clientDAO = new ClientDAO();

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
                Client client = clientDAO.findById(id);

                if (client == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "Client introuvable");
                    return;
                }

                request.setAttribute("client", client);
                afficherFormulaire(request, response);
            } else {
                List<Client> clients = clientDAO.findAll();
                request.setAttribute("clients", clients);
                request.getRequestDispatcher(
                        "/WEB-INF/views/clients/liste.jsp"
                ).forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur avec la base de données", e);
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
                Client client = lireClientDuFormulaire(request);
                clientDAO.save(client);

            } else if ("modifier".equals(action)) {
                Client client = lireClientDuFormulaire(request);
                client.setId(Integer.parseInt(request.getParameter("id")));
                clientDAO.update(client);

            } else if ("supprimer".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                clientDAO.delete(id);

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Action inconnue");
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/clients"
            );

        } catch (SQLException e) {
            throw new ServletException("Erreur avec la base de données", e);
        }
    }

    private Client lireClientDuFormulaire(HttpServletRequest request) {
        Client client = new Client();
        client.setNom(request.getParameter("nom"));
        client.setEmail(request.getParameter("email"));
        client.setAdresse(request.getParameter("adresse"));
        return client;
    }

    private void afficherFormulaire(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/clients/formulaire.jsp"
        ).forward(request, response);
    }
}