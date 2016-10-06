package com.mrsoftware.udb.servlets;

import com.mrsoftware.udb.exceptions.TomatoException;
import com.mrsoftware.udb.exceptions.TomatoServicesException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class DataDrivenPOCServlet
 */
public class ServletBase extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBase() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Support methods
    protected void writeResponse(HttpServletResponse response, String entity) {
        try {
            response.setContentType("text/plain");

            PrintWriter writer = response.getWriter();

            writer.write(entity);

            writer.flush();

            writer.close();
        } catch (Exception e) {
            throw new TomatoServicesException(e, "Error writing HTTP response!", "Entity", entity);
        }
    }

    protected String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));

            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch (Exception ex) {
            throw new TomatoException(ex, "Exception reading HTTP request body!");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
            }
        }
        return stringBuilder.toString();
    }
}
