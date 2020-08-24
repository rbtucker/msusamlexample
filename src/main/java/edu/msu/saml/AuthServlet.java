/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.msu.saml;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import com.coveo.saml.SamlResponse;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rbtucker
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("in AuthServlet.doPost()");
        SamlClient samlClient = (SamlClient) request.getServletContext().getAttribute("samlclient");
         try {
            SamlResponse samlResponse = samlClient.decodeAndValidateSamlResponse(request.getParameter("SAMLResponse"), "POST");
            System.out.println("samlResponse - " + samlResponse);
            
            // pull attributes from response and store them in current session
            Map<String, String> attributes = SamlClient.getAttributes(samlResponse);
            System.out.println("saml attributes - " + attributes.toString());
            request.getSession().setAttribute(AppContextListener.AUTHENTICATED_SESSION_DATA_KEY, attributes);
            
            // forward to desired page
            request.getRequestDispatcher("index.html").forward(request, response);
        } catch (SamlException ex) {
            throw new ServletException(ex.toString(), ex);
        }
    }
}