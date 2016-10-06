package com.mrsoftware.udb.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DataDrivenPOCServlet
 */
public class DynamicEntityServlet extends ServletBase {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DynamicEntityServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        handleEntityGets(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleEntityPosts(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* 
	 * ENTITY Services
     */
    protected void handleEntityGets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            super.doGet(request, response);
        } else {
            String name = request.getParameter("name");
            Boolean deep = new Boolean(request.getParameter("deep"));
            Boolean collection = new Boolean(request.getParameter("collection"));
//			String filter = request.getParameter("filter");

            String entity = null;

            /// Get the new instance
            if (action.equalsIgnoreCase("new")) {
                entity = DynamicEntityServices.getEntityInstance(name, deep, collection, null);
            }

            writeResponse(response, entity);
        }
    }

    protected void handleEntityPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {

        // Get JSON from request body
        String json = getRequestBody(request);

        if (json != null && !json.equals("") && json.length() > 3) {
            String action = request.getParameter("action");

            String result = null;

            if (action.equalsIgnoreCase("load")) {
                result = DynamicEntityServices.loadEntity(json);
            } else if (action.equalsIgnoreCase("save")) {
                result = DynamicEntityServices.saveEntity(json);
            }

            writeResponse(response, result);
        }
    }
}
