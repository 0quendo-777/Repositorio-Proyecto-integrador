import CamposDeTextoCustom.RoundButton;
import CamposDeTextoCustom.RoundedPasswordField;
import CamposDeTextoCustom.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.URL;

public class RegistroFrame extends JFrame {
    private JTextField nombreCampo, correoCampo;
    private JPasswordField contrasenaCampo;

    // DASHBOARD PRINCIPAL + AJUSTE PANTALLA + SWING + INTERFAZ NUEVA
    // DANIEL SANTIAGO RODRÍGUEZ GERENA & JUAN JOSÉ OQUENDO JARAMILLO*

    public RegistroFrame() {
        setTitle("Nuevo Usuario");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con imagen de fondo
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Usamos getClass().getResource() para obtener la ruta relativa de la imagen
                URL rutaFondo = getClass().getResource("/resources/fondo_1.png");
                if (rutaFondo != null) {
                    ImageIcon fondo = new ImageIcon(rutaFondo);
                    g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    System.out.println("No se encontró la imagen.");
                }
            }
        };
        add(panel);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Panel para el formulario (se elimina el recuadro blanco poniéndolo transparente)
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(450, 75, 300, 300);
        formPanel.setBackground(new Color(255, 255, 255, 0)); // Fondo totalmente transparente
        panel.add(formPanel);

        // Título (texto en blanco)
        JLabel titulo = new JLabel("¡Bienvenido a SOVIS!", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(50, 20, 200, 20);
        formPanel.add(titulo);

        // Campo de texto para "NOMBRE" (etiqueta en blanco)
        JLabel nombreLabel = new JLabel("NOMBRE");
        nombreLabel.setBounds(30, 60, 100, 20);
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nombreLabel.setForeground(Color.WHITE);
        formPanel.add(nombreLabel);

        // Campo de texto redondeado para nombre
        nombreCampo = new RoundedTextField(25);  // Usamos un radio de 25 para los bordes redondeados
        nombreCampo.setBounds(30, 80, 240, 25);
        formPanel.add(nombreCampo);

        // Campo de texto para "CORREO" (etiqueta en blanco)
        JLabel correoLabel = new JLabel("CORREO");
        correoLabel.setBounds(30, 110, 100, 20);
        correoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        correoLabel.setForeground(Color.WHITE);
        formPanel.add(correoLabel);

        // Campo de texto redondeado para correo
        correoCampo = new RoundedTextField(25);
        correoCampo.setBounds(30, 130, 240, 25);
        formPanel.add(correoCampo);

        // Campo de texto para "CONTRASEÑA" (etiqueta en blanco)
        JLabel contrasenaLabel = new JLabel("CONTRASEÑA");
        contrasenaLabel.setBounds(30, 160, 100, 20);
        contrasenaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contrasenaLabel.setForeground(Color.WHITE);
        formPanel.add(contrasenaLabel);

        // Campo de contraseña redondeado
        contrasenaCampo = new RoundedPasswordField(20, 20); // 20 columnas, 20 de radio
        contrasenaCampo.setBounds(30, 180, 240, 25);
        formPanel.add(contrasenaCampo);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Botón de registrar (usando CamposDeTextoCustom.RoundButton para lograr el estilo redondeado sin recuadro blanco)
        RoundButton btnRegistrar = new RoundButton("REGISTRARSE", 30);
        btnRegistrar.setBackground(new Color(2, 34, 49));  // Color #022231
        btnRegistrar.setForeground(Color.WHITE);  // Texto en blanco
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setBounds(30, 230, 240, 30);
        formPanel.add(btnRegistrar);

        // Evento del botón registrar (manteniendo la funcionalidad original)
        btnRegistrar.addActionListener(e -> registrarUsuario());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Añadir logo SOVIS en la franja oscura
        URL rutaLogo = getClass().getResource("/resources/SOVIS_logo.png");
        if (rutaLogo != null) {
            ImageIcon originalIcon = new ImageIcon(rutaLogo);
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            // Calcular el tamaño manteniendo la relación de aspecto
            int targetWidth = 100; // Ancho deseado
            int targetHeight = (targetWidth * originalHeight) / originalWidth;

            // Crear la imagen redimensionada preservando la relación de aspecto
            Image scaledImg = originalIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);

            // Crear el JLabel con dimensiones exactas que coincidan con la imagen
            JLabel logoLabel = new JLabel(scaledIcon);
            logoLabel.setBounds(150, 160, targetWidth, targetHeight); // Usar las mismas dimensiones de la imagen
            panel.add(logoLabel);
        } else {
            System.out.println("No se encontró el logo.");
        }

        // Texto "SOVIS" centrado en la franja oscura
        JLabel sovisLabel = new JLabel("SOVIS", SwingConstants.CENTER);
        sovisLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        sovisLabel.setForeground(Color.WHITE);
        sovisLabel.setBounds(0, 250, 400, 100);
        panel.add(sovisLabel);

        setVisible(true);


    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void registrarUsuario() {
        String nombre = nombreCampo.getText();
        String correo = correoCampo.getText();
        String contrasena = new String(contrasenaCampo.getPassword());

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            PreparedStatement stmt = Main.conexion.prepareStatement("INSERT INTO usuarios (nombre, correo, contrasena) VALUES (?, ?, ?)");
            stmt.setString(1, nombre);
            stmt.setString(2, correo);
            stmt.setString(3, contrasena);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro exitoso");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
