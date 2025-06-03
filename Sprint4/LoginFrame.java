import CamposDeTextoCustom.RoundButton;
import CamposDeTextoCustom.RoundedPasswordField;
import CamposDeTextoCustom.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField correoCampo;
    private JPasswordField contrasenaCampo;

    // DASHBOARD PRINCIPAL + AJUSTE PANTALLA + SWING + INTERFAZ NUEVA
    // DANIEL SANTIAGO RODRÍGUEZ GERENA
    // DANIEL SANTIAGO RODRÍGUEZ GERENA & JUAN JOSÉ OQUENDO JARAMILLO*

    public LoginFrame() {
        setTitle("SOVIS - Inicio de Sesión");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Panel principal con imagen de fondo
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Usamos getClass().getResource() para obtener la ruta relativa de la imagen
                URL rutaFondo = getClass().getResource("/resources/fondo_1.png");
                if (rutaFondo != null) {
                    ImageIcon fondo = new ImageIcon(rutaFondo);
                    g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    System.out.println("❌ No se encontró la imagen.");
                }
            }
        };
        add(panel);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Panel izquierdo sin color de fondo, con texto en blanco
        JPanel panelIzquierdo = new JPanel(null); // Usar layout null para posiciones absolutas
        panelIzquierdo.setPreferredSize(new Dimension(350, 950)); // Aumentamos la altura del panel
        panelIzquierdo.setBackground(new Color(255, 255, 255, 0)); // Fondo transparente
        panel.add(panelIzquierdo, BorderLayout.WEST);

        // Añadir logo SOVIS
        URL rutaLogo = getClass().getResource("/resources/SOVIS_logo.png");
        if (rutaLogo != null) {
            ImageIcon logoIcon = new ImageIcon(rutaLogo);
            // Ajustar el tamaño del logo si es necesario (opcional)
            Image img = logoIcon.getImage();
            Image newImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Ajusta el tamaño según necesites
            ImageIcon logoResized = new ImageIcon(newImg);

            JLabel logoLabel = new JLabel(logoResized);
            logoLabel.setBounds(120, 100, 150, 150); // Ajusta las coordenadas para posicionar el logo
            panelIzquierdo.add(logoLabel);
        } else {
            System.out.println("No se encontró el logo.");
        }

        // Texto "SOVIS" alineado a la izquierda y hacia abajo
        JLabel nombreApp = new JLabel("SOVIS", SwingConstants.CENTER); // Alineación izquierda
        nombreApp.setFont(new Font("Segoe UI", Font.BOLD, 72));
        nombreApp.setForeground(Color.WHITE); // Texto blanco
        nombreApp.setBounds(20, 270, 350, 60); // Coordenadas ajustadas para estar abajo
        panelIzquierdo.add(nombreApp);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Panel derecho (sin color de fondo, solo textos blancos)
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setBackground(new Color(255, 255, 255, 0)); // Sin color de fondo
        panel.add(panelDerecho, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mensaje de Bienvenida
        JLabel titulo = new JLabel("BIENVENIDO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);  // Texto blanco
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelDerecho.add(titulo, gbc);

        // Correo
        JLabel correoLabel = new JLabel("CORREO");
        correoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        correoLabel.setForeground(Color.WHITE);  // Texto blanco
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelDerecho.add(correoLabel, gbc);

        // Correo
        correoCampo = new RoundedTextField(20);  // Reemplazamos JTextField
        correoCampo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2;
        panelDerecho.add(correoCampo, gbc);

        //Contraseña
        JLabel contrasenaLabel = new JLabel("CONTRASEÑA");
        contrasenaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contrasenaLabel.setForeground(Color.WHITE);  // Texto blanco
        gbc.gridy = 3;
        panelDerecho.add(contrasenaLabel, gbc);

        // Contraseña
        contrasenaCampo = new RoundedPasswordField(20, 20); // Campo de contraseña redondeado
        contrasenaCampo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4;
        panelDerecho.add(contrasenaCampo, gbc);

        gbc.gridwidth = 1;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Botón de iniciar sesión
        RoundButton btnLogin = new RoundButton("INICIAR SESIÓN", 30);
        btnLogin.setBackground(new Color(2, 34, 49));  // Color #022231
        btnLogin.setForeground(Color.WHITE);  // Texto blanco
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Fuente Segoe UI
        gbc.gridy = 5;
        gbc.gridx = 0;
        panelDerecho.add(btnLogin, gbc);

        // Acción para iniciar sesión
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Botón de registro
        RoundButton btnRegistro = new RoundButton("REGISTRARSE", 30);
        btnRegistro.setBackground(new Color(2, 34, 49));  // Color #022231
        btnRegistro.setForeground(Color.WHITE);  // Texto blanco
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 14));  // Fuente Segoe UI
        gbc.gridx = 1;
        panelDerecho.add(btnRegistro, gbc);

        // Acción para abrir el registro
        btnRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegistroFrame();
            }
        });

        setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // H5 - ABDIEL ESTEBAN AMOROCHO DE SOUSA
    // Metodo para iniciar sesión
    private void iniciarSesion() {
        String correo = correoCampo.getText();
        String contrasena = new String(contrasenaCampo.getPassword());

        try {
            PreparedStatement stmt = Main.conexion.prepareStatement(
                    "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?");
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                boolean formularioCompletado = rs.getBoolean("formulario_completado");

                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                dispose();
                if (!formularioCompletado) {
                    new FormularioReflexionFrame(userId); // Muestra el formulario
                } else {
                    new DashboardFrame();
                } // Muestra el Dashboard
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
