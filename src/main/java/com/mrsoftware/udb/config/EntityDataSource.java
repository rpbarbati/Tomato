package com.mrsoftware.udb.config;

import com.mrsoftware.udb.Entity;
import com.mrsoftware.udb.util.LowerCaseMap;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityDataSource {

    static private EntityDataSource INSTANCE = new EntityDataSource();

    static public EntityDataSource getInstance() {
        return INSTANCE;
    }

    private MysqlDataSource ds = null;

    private Connection connection = null;

    protected EntityDataSource() {
        ds = new MysqlDataSource();

        ds.setServerName("localhost");
        ds.setPortNumber(3306);
        ds.setDatabaseName("sakila");
        ds.setUser("apps");
        ds.setPassword("apps");
    }

    public DataSource getDataSource() {
        return ds;
    }

    public Connection getConnection() throws SQLException {
        // Only get a new connection when absolutely needed
        if (connection == null || connection.isClosed()) {
            connection = ds.getConnection();
        }

        return connection;
    }

    public ArrayList<LowerCaseMap> executeQuery(String sql, Entity entity) {
        ArrayList<LowerCaseMap> rows = new ArrayList<>();

        PreparedStatement s = null;
        ResultSet rs = null;

        try {
            s = getConnection().prepareStatement(sql);

            entity.setParameterValues(s);

            rs = s.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                LowerCaseMap row = new LowerCaseMap();

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }

                rows.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntityDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                s.close();
            } catch (Exception e) {
            }
        }

        return rows;
    }

    public ArrayList<LowerCaseMap> executeQuery(String sql) {
        ArrayList<LowerCaseMap> rows = new ArrayList<>();

        Statement s = null;
        ResultSet rs = null;

        try {
            s = getConnection().createStatement();

            rs = s.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                LowerCaseMap row = new LowerCaseMap();

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }

                rows.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntityDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                s.close();
                rs.close();
            } catch (Exception e) {
            }
        }

        return rows;
    }

    public int executeUpdate(String sql, Entity entity) {
        PreparedStatement s = null;
        ResultSet rs = null;

        int retval = 0;

        try {
            s = getConnection().prepareStatement(sql);

            entity.setParameterValues(s);

            retval = s.executeUpdate();

        } catch (Exception e) {
            Logger.getLogger(EntityDataSource.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                s.close();
                rs.close();
            } catch (Exception e) {
            }
        }

        return retval;
    }

    public String getMetaSchema() {
        return "sakila";
    }

    static public void main(String[] args) {
        try {
            EntityDataSource eds = new EntityDataSource();

            @SuppressWarnings("unused")
            Connection c = eds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
