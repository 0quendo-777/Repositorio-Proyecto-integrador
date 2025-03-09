import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardFrame extends JFrame {
    private Connection conexion;

    // DASHBOARD PRINCIPAL + AJUSTE PANTALLA + SWING
    // DANIEL SANTIAGO RODRÍGUEZ GERENA & JUAN JOSÉ OQUENDO JARAMILLO*

    public DashboardFrame() {
        setTitle("Panel Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ocupa toda la pantalla
        setUndecorated(true); // (Opcional) Elimina los bordes de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Conectar con la base de datos
        conexion = ConexionDB.conectar();

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(52, 73, 94));
        add(panel);

        // Botón de "Cuenta" en la parte superior derecha
        JButton btnCuenta = new JButton("Cuenta ⚙️");
        btnCuenta.setBounds(1300, 20, 100, 30);
        btnCuenta.setBackground(new Color(41, 128, 185));
        btnCuenta.setForeground(Color.WHITE);
        panel.add(btnCuenta);

        // Menú desplegable con opciones
        JPopupMenu menuCuenta = new JPopupMenu();
        JMenuItem buscarUsuario = new JMenuItem("🔍 Buscar Usuario");
        JMenuItem modificarUsuario = new JMenuItem("✏️ Modificar Usuario");
        JMenuItem eliminarUsuario = new JMenuItem("❌ Eliminar Usuario");
        JMenuItem cerrarSesion = new JMenuItem("🚪 Cerrar Sesión");

        menuCuenta.add(buscarUsuario);
        menuCuenta.add(modificarUsuario);
        menuCuenta.add(eliminarUsuario);
        menuCuenta.addSeparator();
        menuCuenta.add(cerrarSesion);

        btnCuenta.addActionListener(e -> menuCuenta.show(btnCuenta, 0, btnCuenta.getHeight()));

        // Funcionalidades
        buscarUsuario.addActionListener(e -> buscarUsuario());
        modificarUsuario.addActionListener(e -> modificarUsuario());
        eliminarUsuario.addActionListener(e -> eliminarUsuario());
        cerrarSesion.addActionListener(e -> cerrarSesion());

        setVisible(true);
    }


    // H3 - DANIEL SANTIAGO RODRÍGUEZ GERENA
    //  Buscar usuario por nombre (muestra resultados similares)
    private void buscarUsuario() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del usuario:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            try {
                String sql = "SELECT * FROM usuarios WHERE nombre LIKE ?";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, "%" + nombre + "%");
                ResultSet rs = stmt.executeQuery();

                StringBuilder resultado = new StringBuilder("Usuarios encontrados:\n");

                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    int id = rs.getInt("id");
                    String nombreUsuario = rs.getString("nombre");
                    String correo = rs.getString("correo");
                    resultado.append("ID: ").append(id)
                            .append(" | Nombre: ").append(nombreUsuario)
                            .append(" | Correo: ").append(correo).append("\n");
                }

                JOptionPane.showMessageDialog(this, hayResultados ? resultado.toString() : "⚠️ No se encontraron usuarios con ese nombre.");
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error al buscar usuario: " + e.getMessage());
            }
        }
    }


    // H4 - DANIEL SANTIAGO RODRÍGUEZ GERENA
    // Modificar usuario (requiere correo y contraseña)
    private void modificarUsuario() {
        String correo = JOptionPane.showInputDialog(this, "Ingrese su correo:");
        String contrasenaActual = JOptionPane.showInputDialog(this, "Ingrese su contraseña actual:");

        if (correo == null || contrasenaActual == null || correo.trim().isEmpty() || contrasenaActual.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Los campos no pueden estar vacíos.");
            return;
        }

        try {
            String sqlVerificar = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
            PreparedStatement stmtVerificar = conexion.prepareStatement(sqlVerificar);
            stmtVerificar.setString(1, correo);
            stmtVerificar.setString(2, contrasenaActual);
            ResultSet rs = stmtVerificar.executeQuery();

            if (rs.next()) {
                String nuevaContrasena = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña:");
                if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                    String sqlActualizar = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";
                    PreparedStatement stmtActualizar = conexion.prepareStatement(sqlActualizar);
                    stmtActualizar.setString(1, nuevaContrasena);
                    stmtActualizar.setString(2, correo);
                    stmtActualizar.executeUpdate();
                    JOptionPane.showMessageDialog(this, "✅ Contraseña actualizada correctamente.");
                    stmtActualizar.close();
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Correo o contraseña incorrectos.");
            }
            rs.close();
            stmtVerificar.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar usuario: " + e.getMessage());
        }
    }

    // H2 - JUAN JOSÉ OQUENDO JARAMILLO
    //  Eliminar usuario (requiere correo y contraseña)
    private void eliminarUsuario() {
        String correo = JOptionPane.showInputDialog(this, "Ingrese su correo:");
        String contrasena = JOptionPane.showInputDialog(this, "Ingrese su contraseña:");

        if (correo == null || contrasena == null || correo.trim().isEmpty() || contrasena.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Los campos no pueden estar vacíos.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar su cuenta?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                String sqlVerificar = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
                PreparedStatement stmtVerificar = conexion.prepareStatement(sqlVerificar);
                stmtVerificar.setString(1, correo);
                stmtVerificar.setString(2, contrasena);
                ResultSet rs = stmtVerificar.executeQuery();

                if (rs.next()) {
                    String sqlEliminar = "DELETE FROM usuarios WHERE correo = ?";
                    PreparedStatement stmtEliminar = conexion.prepareStatement(sqlEliminar);
                    stmtEliminar.setString(1, correo);
                    stmtEliminar.executeUpdate();
                    JOptionPane.showMessageDialog(this, "✅ Cuenta eliminada correctamente.");
                    stmtEliminar.close();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Correo o contraseña incorrectos.");
                }
                rs.close();
                stmtVerificar.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    // H6 - ADBIEL ESTEBAN AMOROCHO DE SOUSA
    //  Cerrar sesión
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quiere cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Cerrando sesión...");
            dispose();
            new LoginFrame(); // Regresa a la pantalla de inicio de sesión
        }
    }
}




