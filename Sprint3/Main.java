import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

// ELABORACIÓN BASE DE DATOS JUAN DANIEL DÍAZ
// CONEXIÓN CON LA BASE DE DATOS OQUENDO

public class Main {
    public static Connection conexion; // Variable de conexión global

    public static void main(String[] args) {
        conectarBaseDeDatos(); // Conectar a la base de datos
        new LoginFrame(); // Iniciar la ventana de inicio de sesión
    }

    // Metodo para conectar con la base de datos
    private static void conectarBaseDeDatos()
    {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/usuarios_db?useSSL=false", "root", "");
            System.out.println("✅ Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
