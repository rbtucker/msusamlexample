/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.msu.saml;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author rbtucker
 */
public class AppContextListener implements ServletContextListener {
    public static final String AUTHENTICATED_SESSION_DATA_KEY = "authenticated.session.data";
    
    @Override
    public void contextInitialized(ServletContextEvent e) {
        // create SamlClient and store for later use
        System.out.println("in AppContextListener.contextInitialized()");
        SamlClient samlClient;
        try {
            samlClient = SamlClient.fromMetadata(
                    e.getServletContext().getInitParameter("relaying.party.identifier"),
                    e.getServletContext().getInitParameter("saml.assertion.handler.url"),
                    getIDPMetaData(e.getServletContext()));
            e.getServletContext().setAttribute("samlclient", samlClient);
            System.out.println("SamlClient created");
        } catch (SamlException ex) {
            throw new RuntimeException(ex.toString(), ex);
        }
        
    }


    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }

    private Reader getIDPMetaData(ServletContext servletContext) {
        // load the IDP metadata file from classpath
        return new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/classes/idpmetadata.xml"));
    }
}
