import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField correoCampo;
    private JPasswordField contrasenaCampo;

    // DASHBOARD PRINCIPAL + AJUSTE PANTALLA + SWING
    // DANIEL SANTIAGO RODRÍGUEZ GERENA & JUAN JOSÉ OQUENDO JARAMILLO*
    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Crear panel con GridBagLayout para mejor organización
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(44, 62, 80)); // Fondo oscuro
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de bienvenida
        JLabel titulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        // Etiqueta y campo de correo
        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(correoLabel, gbc);

        correoCampo = new JTextField(15);
        gbc.gridx = 1;
        panel.add(correoCampo, gbc);

        // Etiqueta y campo de contraseña
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(contrasenaLabel, gbc);

        contrasenaCampo = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(contrasenaCampo, gbc);

        // Botón de inicio de sesión
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(btnLogin, gbc);

        // Botón de registro
        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.setBackground(new Color(46, 204, 113));
        btnRegistro.setForeground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(btnRegistro, gbc);

        // Eventos de botones
        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> new RegistroFrame()); // Abre ventana de registro

        setVisible(true);
    }


    // H5 - ABDIEL ESTEBAN AMOROCHO DE SOUSA
    // Metodo para iniciar sesión
    private void iniciarSesion() {
        String correo = correoCampo.getText();
        String contrasena = new String(contrasenaCampo.getPassword());

        try {
            // Consulta a la base de datos para verificar usuario
            PreparedStatement stmt = Main.conexion.prepareStatement("SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?");
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                dispose(); // Cierra la ventana actual
                new DashboardFrame(); // Abre el panel principal
            } else {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}

