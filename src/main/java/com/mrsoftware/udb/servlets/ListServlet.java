package com.mrsoftware.udb.servlets;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.config.EntityDataSource;
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
public class ListServlet extends ServletBase {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListServlet() {
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

        String listName = request.getParameter("name");

        Entity list = Entity.createEntityOrView(EntityDataSource.getInstance().getMetaSchema() + ".topicItem");

        list.setFilter("topic_id = " + listName);

        list.isArray(true);

        list.load();

        String s = list.toJSON();

        writeResponse(response, s);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
