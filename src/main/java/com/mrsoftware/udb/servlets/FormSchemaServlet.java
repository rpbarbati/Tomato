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
public class FormSchemaServlet extends ServletBase {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormSchemaServlet() {
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

        handleFormSchemaGets(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    /* 
	 * FORM SCHEMA Services
     */
    protected void handleFormSchemaGets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Can pass schema and name 
        String name = request.getParameter("name");

        String force = request.getParameter("force");

        boolean forceCreate = Boolean.valueOf(((force == null || force.equals("false")) ? "false" : "true"));

        String result = null;

        // return a default form schema for the given schema and name
        result = FormSchemaServices.getFormSchema(name, forceCreate);

        writeResponse(response, result);
    }

}
