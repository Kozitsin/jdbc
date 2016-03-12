import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Александр on 28.02.2016.
 */

public class Connectivity implements DataBase {

    private String databaseAddress = "jdbc:sqlserver://localhost";
    private String port = ":1433;";
    private String dbName = "";

    private boolean operationStatus = false;

    private Connection conn = null;

    public boolean isOperationStatus() {
        return operationStatus;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    private String getConnectionURL() {
        StringBuilder sb = new StringBuilder(databaseAddress);
        sb.append(port);
        sb.append(";databaseName=");
        sb.append(dbName);

        return sb.toString();
    }

    private void addStoredProcedures() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            Statement st = conn.createStatement();
            // create table
            st.execute("CREATE PROCEDURE CREATE_TABLE " +
                       "AS " +
                       "BEGIN " +
                       "CREATE TABLE PERSON " +
                       "(id INTEGER, " +
                       "name NVARCHAR(255), " +
                       "surname NVARCHAR(255), " +
                       "age INTEGER, " +
                       "PRIMARY KEY (id)) " +
                       "END"
            );

            // delete table
            st.execute("CREATE PROCEDURE DROP_TABLE " +
                       "AS " +
                       "BEGIN " +
                       "DROP TABLE PERSON " +
                       "END"
            );

            // insert in table
            st.execute("CREATE PROCEDURE INSERT_IN_TABLE " +
                       "@id INTEGER, " +
                       "@name NVARCHAR(255), " +
                       "@surname NVARCHAR(255), " +
                       "@age INTEGER " +
                       "AS " +
                       "BEGIN " +
                       "INSERT INTO PERSON " +
                       "VALUES (@id, @name, @surname, @age) " +
                       "END"
            );

            // search
            st.execute("CREATE PROCEDURE SEARCH " +
                       "@surname NVARCHAR(255) " +
                       "AS " +
                       "BEGIN " +
                       "SELECT * FROM PERSON " +
                       "WHERE SURNAME = @surname " +
                       "END"
            );

            // update table
            st.execute("CREATE PROCEDURE UPDATE_TABLE " +
                       "@id INTEGER, " +
                       "@name NVARCHAR(255), " +
                       "@surname NVARCHAR(255), " +
                       "@age INTEGER " +
                       "AS " +
                       "BEGIN " +
                       "UPDATE PERSON " +
                       "SET name = @name, surname = @surname, age = @age " +
                       "WHERE id = @id " +
                       "END"
            );

            //delete from table
            st.execute("CREATE PROCEDURE DELETE_FROM_TABLE " +
                       "@surname NVARCHAR(255) " +
                       "AS " +
                       "BEGIN " +
                       "DELETE FROM PERSON " +
                       "WHERE surname = @surname " +
                       "END"
            );

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public void createNewDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433", "test", "test");

            Statement st = conn.createStatement();
            String sql = "CREATE DATABASE " + dbName;
            st.executeUpdate(sql);

            addStoredProcedures();

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public void deleteDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433", "test", "test");

            Statement st = conn.createStatement();
            String sql = "DROP DATABASE " + dbName;
            st.executeUpdate(sql);

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public void createTable() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            CallableStatement call = conn.prepareCall("EXECUTE CREATE_TABLE");
            call.execute();

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public void deleteTable() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            CallableStatement call = conn.prepareCall("EXECUTE DROP_TABLE");
            call.execute();

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public void insertInTable(int id, String name, String surname, int age) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            CallableStatement call = conn.prepareCall("EXECUTE INSERT_IN_TABLE ?, ?, ?, ?");
            call.clearParameters();

            call.setInt(1, id);
            call.setString(2, name);
            call.setString(3, surname);
            call.setInt(4, age);

            call.executeUpdate();

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public ArrayList<Object[]> search(String surname) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            CallableStatement call = conn.prepareCall("EXECUTE SEARCH ?");

            call.clearParameters();
            call.setString(1, surname);

            ResultSet rs = call.executeQuery();

            int rowCount = 4;
            ArrayList<Object[]> list = new ArrayList<Object[]>();
            while (rs.next()) {
                Object[] temp = new Object[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    temp[i] = rs.getObject(i + 1);
                }
                list.add(temp);
            }
            operationStatus = true;
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
        return null;
    }

    public void updateTable(int id, String name, String surname, int age) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            CallableStatement call = conn.prepareCall("EXECUTE UPDATE_TABLE ?, ?, ?, ?");

            call.clearParameters();
            call.setInt(1, id);
            call.setString(2, name);
            call.setString(3, surname);
            call.setInt(4, age);

            call.executeUpdate();

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }

    public void deleteFromTable(String surname) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(getConnectionURL(), "test", "test");

            CallableStatement call = conn.prepareCall("EXECUTE DELETE_FROM_TABLE ?");

            call.clearParameters();
            call.setString(1, surname);
            call.executeUpdate();

            operationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
            operationStatus = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            operationStatus = false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                operationStatus = false;
            }
        }
    }
}
