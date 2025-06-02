import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// CONEXIÓN CON BASE DE DATOS - OQUENNDO
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/usuarios_db?serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }
}
