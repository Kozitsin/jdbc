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

    public void createNewDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433", "test", "test");

            Statement st = conn.createStatement();
            String sql = "CREATE DATABASE " + dbName;
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

            PreparedStatement ps = conn.prepareStatement("CREATE TABLE PERSON" +
                                                         "(id INTEGER," +
                                                         " name NVARCHAR(255)," +
                                                         " surname NVARCHAR(255)," +
                                                         " age INTEGER," +
                                                         " PRIMARY KEY (id))");

            ps.executeUpdate();
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

            PreparedStatement ps = conn.prepareStatement("DROP TABLE PERSON");
            ps.executeUpdate();
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

            PreparedStatement ps = conn.prepareStatement("INSERT INTO PERSON " +
                                                         "VALUES (?, ?, ?, ?)");
            ps.clearParameters();
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setInt(4, age);

            ps.executeUpdate();
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

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM PERSON WHERE surname = ?");

            ps.clearParameters();
            ps.setString(1, surname);

            ResultSet rs = ps.executeQuery();

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

            PreparedStatement ps = conn.prepareStatement("UPDATE PERSON " +
                                                         "SET name = ?, surname = ?, age = ? " +
                                                         "WHERE id = ?");
            ps.clearParameters();
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setInt(3, age);
            ps.setInt(4, id);

            ps.executeUpdate();
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

            PreparedStatement ps = conn.prepareStatement("DELETE FROM PERSON " +
                                                         "WHERE surname = ?");
            ps.clearParameters();
            ps.setString(1, surname);

            ps.executeUpdate();
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
