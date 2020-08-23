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
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;

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
            SamlResponse samlResponse = samlClient.decodeAndValidateSamlResponse(request.getParameter("SAMLResponse"));
            System.out.println("samlResponse - " + samlResponse);
            request.getSession().setAttribute(AppContextListener.AUTHENTICATED_SESSION_DATA_KEY, getAttributes(samlResponse.getAssertion().getAttributeStatements()));
            request.getRequestDispatcher("index.html").forward(request, response);
        } catch (SamlException ex) {
            throw new ServletException(ex.toString(), ex);
        }
    }
    
    private HashMap<String, String> getAttributes(List <AttributeStatement> attributeStatements) {
        HashMap<String, String> retval = new HashMap<>();
        
        if (!attributeStatements.isEmpty()) {
             for (Attribute att : attributeStatements.get(0).getAttributes()) {
                    retval.put(att.getFriendlyName(),att.getAttributeValues().get(0).getDOM().getTextContent());
            }
        }
        
        System.out.println("SAML attributes - " + retval.toString());
    
        return retval;
    }
}