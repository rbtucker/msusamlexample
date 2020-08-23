/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.msu.saml;

import com.coveo.saml.SamlClient;
import com.coveo.saml.SamlException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author rbtucker
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The filter intercepts the user and start the SAML authentication if it is not
 * authenticated
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        System.out.println("in AuthFilter.doFilter() - " + httpServletRequest.getRequestURL().toString());
        
        try {
            System.out.println("authenticated session data - " + httpServletRequest.getSession().getAttribute(AppContextListener.AUTHENTICATED_SESSION_DATA_KEY));
            if ((httpServletRequest.getParameter("SAMLResponse") != null) 
                || (httpServletRequest.getSession().getAttribute(AppContextListener.AUTHENTICATED_SESSION_DATA_KEY) != null)) {
               chain.doFilter(request, response);
            } else {
                System.out.println("redirect to IDP");
                SamlClient samlClient = (SamlClient)httpServletRequest.getServletContext().getAttribute("samlclient");
                samlClient.redirectToIdentityProvider(httpServletResponse, null);
            }
        } catch (SamlException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    public void destroy() {
    }
}
