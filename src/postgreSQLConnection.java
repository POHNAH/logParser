import java.sql.*;

public class postgreSQLConnection {
    String host = "magnolia-dev-magnolia-dev-cms-dev-control-2-b.yc.mvideo.ru";
    String port = "5432";
    String user = "cms_stat";
    String password = "funky21";
    String dataBaseName = "cms_stat";
    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public postgreSQLConnection() {
    }

    public postgreSQLConnection(String host, String port, String user, String password, String dataBaseName) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.dataBaseName = dataBaseName;
    }

    public ResultSet executeSelect(String sql){
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + dataBaseName,
                            user, password);
            conn.setAutoCommit(false);
//            System.out.println("Opened database successfully");

            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
//            statement.close();
//            conn.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
//        System.out.println("Operation done successfully");
        return resultSet;
    }

    public void executeInsert(String sql){
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + dataBaseName,
                    user, password);
            conn.setAutoCommit(false);
//            System.out.println("Opened database successfully");

            statement = conn.createStatement();
            statement.executeUpdate(sql);

            statement.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
//        System.out.println("Records created successfully");
    }
}
