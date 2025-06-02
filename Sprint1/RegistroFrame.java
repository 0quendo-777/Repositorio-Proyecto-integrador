import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistroFrame extends JFrame {
    private JTextField nombreCampo, correoCampo;
    private JPasswordField contrasenaCampo;

    // DASHBOARD PRINCIPAL + AJUSTE PANTALLA + SWING
    // DANIEL SANTIAGO RODRÍGUEZ GERENA & JUAN JOSÉ OQUENDO JARAMILLO*
    public RegistroFrame() {
        setTitle("Registro de Usuario");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Crear el panel con GridBagLayout para organizar los elementos
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(39, 174, 96)); // Color verde
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de la ventana
        JLabel titulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        // Etiqueta y campo de nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(nombreLabel, gbc);

        nombreCampo = new JTextField(15);
        gbc.gridx = 1;
        panel.add(nombreCampo, gbc);

        // Etiqueta y campo de correo
        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(correoLabel, gbc);

        correoCampo = new JTextField(15);
        gbc.gridx = 1;
        panel.add(correoCampo, gbc);

        // Etiqueta y campo de contraseña
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(contrasenaLabel, gbc);

        contrasenaCampo = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(contrasenaCampo, gbc);

        // Botón de registro
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBackground(new Color(255, 165, 0)); // Color naranja
        btnRegistrar.setForeground(Color.WHITE);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnRegistrar, gbc);

        // Evento del botón registrar
        btnRegistrar.addActionListener(e -> registrarUsuario());

        setVisible(true);
    }

    // H1 - JUAN JOSÉ OQUENDO JARAMILLO
    // Metodo para registrar usuario en la base de datos
    private void registrarUsuario() {
        String nombre = nombreCampo.getText();
        String correo = correoCampo.getText();
        String contrasena = new String(contrasenaCampo.getPassword());

        // Verificar que todos los campos estén llenos
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            // Insertar usuario en la base de datos
            PreparedStatement stmt = Main.conexion.prepareStatement("INSERT INTO usuarios (nombre, correo, contrasena) VALUES (?, ?, ?)");
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, contrasena);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro exitoso");
            dispose(); // Cierra la ventana después de registrar
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}

