import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {
    private Connection conexion;
    private ImageIcon background;
    private String usuarioActualCorreo; // Para almacenar el correo del usuario actual

    // Componentes para la búsqueda de materias
    private JTextField campoBusqueda;
    private JTextArea areaResultados;
    private List<Materia> materias;
    private JPanel panelContenido;

    // Constructor modificado para recibir el correo del usuario
    public DashboardFrame(String correoUsuario) {
        // Guardar el correo del usuario que inició sesión
        this.usuarioActualCorreo = correoUsuario;

        setTitle("Panel Principal - SOVIS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cargar imagen de fondo
        background = new ImageIcon(getClass().getResource("/resources/fondo_2.png"));

        // Crear panel de fondo personalizado
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setLayout(new BorderLayout());
        setContentPane(panelFondo);

        // Conectar con la base de datos
        conexion = ConexionDB.conectar();

        // Barra de título personalizada
        JPanel barraTitulo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        barraTitulo.setBackground(new Color(50, 50, 50, 180));
        panelFondo.add(barraTitulo, BorderLayout.NORTH);

        // Panel lateral izquierdo
        JPanel panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(200, getHeight()));
        panelLateral.setBackground(new Color(20, 20, 20, 200));

        // Cambio: usar BoxLayout para mejor control del espacio vertical
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelFondo.add(panelLateral, BorderLayout.WEST);

        // Título "SOVIS" con padding
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setMaximumSize(new Dimension(200, 80));
        titlePanel.setPreferredSize(new Dimension(200, 80));

        JLabel lblTitulo = new JLabel("SOVIS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 50));
        lblTitulo.setForeground(Color.WHITE);
        titlePanel.add(lblTitulo);
        panelLateral.add(titlePanel);

        // Agregar espacio
        panelLateral.add(Box.createVerticalStrut(20));

        // Agregar nuevo botón "Mi Perfil" para mostrar información del usuario actual
        panelLateral.add(crearBotonEstilizado("Mi Perfil", e -> mostrarPerfilUsuario()));
        panelLateral.add(Box.createVerticalStrut(15));

        // Agregar botones estilizados
        panelLateral.add(crearBotonEstilizado("Buscar Usuario", e -> buscarUsuario()));
        panelLateral.add(Box.createVerticalStrut(15));
        panelLateral.add(crearBotonEstilizado("Modificar Usuario", e -> modificarUsuario()));
        panelLateral.add(Box.createVerticalStrut(15));
        panelLateral.add(crearBotonEstilizado("Eliminar Usuario", e -> eliminarUsuario()));
        panelLateral.add(Box.createVerticalStrut(15));

        // Agregar botón para buscar materias
        panelLateral.add(crearBotonEstilizado("Buscar Materias", e -> mostrarBusquedaMaterias()));
        panelLateral.add(Box.createVerticalStrut(15));

        panelLateral.add(crearBotonEstilizado("Cerrar Sesión", e -> cerrarSesion()));

        // Agregar espacio flexible al final
        panelLateral.add(Box.createVerticalGlue());

        // Panel central para contenido dinámico
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);
        panelFondo.add(panelContenido, BorderLayout.CENTER);

        // Inicializar el panel central con información por defecto
        mostrarPanelBienvenida();

        // Inicializar lista de materias
        inicializarMaterias();

        revalidate();
        repaint();
        setVisible(true);
    }

    // Método para mostrar panel de bienvenida por defecto
    private void mostrarPanelBienvenida() {
        // Limpiar el panel de contenido
        panelContenido.removeAll();

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Caja de información 1
        JPanel caja1 = new JPanel();
        caja1.setPreferredSize(new Dimension(600, 70));
        caja1.setBackground(new Color(80, 80, 80, 180));

        JLabel lblBienvenida = new JLabel("Bienvenido a SOVIS - Sistema de Gestión");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(Color.WHITE);
        caja1.add(lblBienvenida);

        panelCentral.add(caja1, gbc);

        // Caja de información 2
        gbc.gridy++;
        JPanel caja2 = new JPanel();
        caja2.setPreferredSize(new Dimension(600, 70));
        caja2.setBackground(new Color(80, 80, 80, 180));

        JLabel lblInstruccion = new JLabel("Seleccione una opción del menú lateral");
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblInstruccion.setForeground(Color.WHITE);
        caja2.add(lblInstruccion);

        panelCentral.add(caja2, gbc);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    // Inicializar la lista de materias
    private void inicializarMaterias() {
        materias = new ArrayList<>();
        materias.add(new Materia("Programación I", "Introducción a la programación, variables, ciclos, estructuras de control."));
        materias.add(new Materia("Matemáticas Discretas", "Lógica, conjuntos, relaciones, funciones, grafos, combinatoria."));
        materias.add(new Materia("Fundamentos de Computación", "Historia de la computación, hardware, software, algoritmos."));
        materias.add(new Materia("Estructura de Datos", "Listas, pilas, colas, árboles, grafos, algoritmos de ordenamiento y búsqueda."));
        materias.add(new Materia("Bases de Datos", "Modelo ER, normalización, SQL, transacciones, stored procedures."));
        materias.add(new Materia("Programación Orientada a Objetos", "Clases, objetos, herencia, polimorfismo, abstracción, encapsulamiento."));
    }

    // Constructor sin parámetros para mantener compatibilidad con el código existente
    public DashboardFrame() {
        this(null);
    }

    private JPanel crearBotonEstilizado(String texto, ActionListener actionListener) {
        // Panel contenedor para centrar el botón horizontalmente
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(180, 40));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crear el botón azul con bordes redondeados
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Color azul para el botón
                g2.setColor(new Color(41, 128, 185));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Efecto de brillo en la parte superior
                g2.setColor(new Color(52, 152, 219));
                g2.fillRoundRect(0, 0, getWidth(), getHeight()/2, 15, 15);

                // Borde del botón
                g2.setColor(new Color(25, 79, 115));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        // Configuración del botón
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(160, 36));
        boton.setMaximumSize(new Dimension(160, 36));
        boton.setMinimumSize(new Dimension(160, 36));
        boton.setOpaque(false);

        // Agregar efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setFont(new Font("Arial", Font.BOLD, 13));
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setFont(new Font("Arial", Font.BOLD, 12));
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        boton.addActionListener(actionListener);

        // Centrar el botón en el panel
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(boton);
        buttonPanel.add(Box.createHorizontalGlue());

        return buttonPanel;
    }

    // Método para mostrar la interfaz de búsqueda de materias
    private void mostrarBusquedaMaterias() {
        // Limpiar el panel de contenido
        panelContenido.removeAll();

        // Crear el panel principal para la búsqueda de materias
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 10));
        panelBusqueda.setOpaque(false);
        panelBusqueda.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título de la sección
        JLabel lblTituloBusqueda = new JLabel("Búsqueda de Materias", SwingConstants.CENTER);
        lblTituloBusqueda.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloBusqueda.setForeground(Color.WHITE);
        panelBusqueda.add(lblTituloBusqueda, BorderLayout.NORTH);

        // Panel para el campo de búsqueda
        JPanel panelCampoBusqueda = new JPanel(new BorderLayout(5, 0));
        panelCampoBusqueda.setOpaque(false);
        panelCampoBusqueda.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        campoBusqueda = new JTextField();
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusqueda.setPreferredSize(new Dimension(400, 30));

        JButton botonBuscar = new JButton("Buscar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(41, 128, 185));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(new Color(25, 79, 115));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);

                g2.dispose();

                super.paintComponent(g);
            }
        };
        botonBuscar.setFont(new Font("Arial", Font.BOLD, 12));
        botonBuscar.setForeground(Color.WHITE);
        botonBuscar.setContentAreaFilled(false);
        botonBuscar.setBorderPainted(false);
        botonBuscar.setFocusPainted(false);
        botonBuscar.setPreferredSize(new Dimension(100, 30));

        panelCampoBusqueda.add(campoBusqueda, BorderLayout.CENTER);
        panelCampoBusqueda.add(botonBuscar, BorderLayout.EAST);

        panelBusqueda.add(panelCampoBusqueda, BorderLayout.CENTER);

        // Panel para los resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setOpaque(false);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Arial", Font.PLAIN, 14));
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        areaResultados.setBackground(new Color(240, 240, 240));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setPreferredSize(new Dimension(600, 300));
        scrollResultados.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));

        panelResultados.add(scrollResultados, BorderLayout.CENTER);

        // Panel contenedor para todo
        JPanel contenedorPrincipal = new JPanel(new BorderLayout(0, 20));
        contenedorPrincipal.setOpaque(false);
        contenedorPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        contenedorPrincipal.add(panelResultados, BorderLayout.CENTER);

        // Agregar el panel al contenedor principal
        panelContenido.add(contenedorPrincipal, BorderLayout.CENTER);

        // Agregar acción al botón
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMaterias();
            }
        });

        // Permitir búsqueda al presionar Enter
        campoBusqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMaterias();
            }
        });

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    // Método para realizar la búsqueda de materias
    private void buscarMaterias() {
        String consulta = campoBusqueda.getText().trim().toLowerCase();
        StringBuilder resultados = new StringBuilder();

        if (consulta.isEmpty()) {
            resultados.append("Por favor ingrese un término de búsqueda.");
            areaResultados.setText(resultados.toString());
            return;
        }

        boolean encontrado = false;
        for (Materia materia : materias) {
            if (materia.getNombre().toLowerCase().contains(consulta)) {
                if (!encontrado) {
                    resultados.append("=== RESULTADOS DE BÚSQUEDA ===\n\n");
                    encontrado = true;
                }
                resultados.append("▶ Materia: ").append(materia.getNombre()).append("\n");
                resultados.append("  Temario: ").append(materia.getTemario()).append("\n\n");
            }
        }

        if (!encontrado) {
            resultados.append("No se encontraron coincidencias exactas.\n\n");
            resultados.append("=== SUGERENCIAS ===\n\n");
            boolean haySugerencias = false;

            for (Materia materia : materias) {
                if (esSimilar(materia.getNombre(), consulta)) {
                    haySugerencias = true;
                    resultados.append("- ").append(materia.getNombre()).append("\n");
                }
            }

            if (!haySugerencias) {
                resultados.append("No hay sugerencias disponibles para este término.\n");
                resultados.append("Intente con otra palabra clave.");
            }
        }

        areaResultados.setText(resultados.toString());
        areaResultados.setCaretPosition(0); // Volver al inicio del texto
    }

    private boolean esSimilar(String materia, String consulta) {
        materia = materia.toLowerCase();
        consulta = consulta.toLowerCase();
        return materia.contains(consulta) ||
                (consulta.length() > 2 && materia.startsWith(consulta.substring(0, 2)));
    }

    // Nueva función para mostrar información del usuario actualmente logueado
    private void mostrarPerfilUsuario() {
        if (usuarioActualCorreo == null || usuarioActualCorreo.trim().isEmpty()) {
            // Si no tenemos el correo almacenado, pedirlo al usuario
            usuarioActualCorreo = JOptionPane.showInputDialog(this, "Ingrese su correo para ver su perfil:");
            if (usuarioActualCorreo == null || usuarioActualCorreo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Debe ingresar un correo para ver su perfil.");
                return;
            }
        }

        try {
            String sql = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, usuarioActualCorreo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Crear un panel personalizado para mostrar la información del usuario
                JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
                panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Título con estilo
                JLabel lblTitulo = new JLabel("Información del Usuario", SwingConstants.CENTER);
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
                lblTitulo.setForeground(new Color(41, 128, 185));
                panelInfo.add(lblTitulo);

                // Agregar campos con formato
                addInfoField(panelInfo, "ID:", String.valueOf(rs.getInt("id")));
                addInfoField(panelInfo, "Nombre:", rs.getString("nombre"));
                addInfoField(panelInfo, "Correo:", rs.getString("correo"));

                // Verificar si existen otros campos en la tabla y mostrarlos
                try {
                    addInfoField(panelInfo, "Rol:", rs.getString("rol"));
                } catch (SQLException e) {
                    // El campo no existe, continuar sin error
                }

                try {
                    addInfoField(panelInfo, "Fecha de Registro:", rs.getString("fecha_registro"));
                } catch (SQLException e) {
                    // El campo no existe, continuar sin error
                }

                // Mostrar el panel en un diálogo personalizado
                JOptionPane optionPane = new JOptionPane(panelInfo, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "Mi Perfil - SOVIS");
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ No se encontró información para el usuario con correo: " + usuarioActualCorreo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al obtener información del perfil: " + e.getMessage());
        }
    }

    // Método auxiliar para añadir campos de información con formato consistente
    private void addInfoField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 0));
        fieldPanel.setOpaque(false);

        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(new Font("Arial", Font.PLAIN, 14));

        fieldPanel.add(lblField, BorderLayout.WEST);
        fieldPanel.add(lblValue, BorderLayout.CENTER);

        panel.add(fieldPanel);
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
            new LoginFrame();
        }
    }

    // Clase interna para representar una materia
    private static class Materia {
        private String nombre;
        private String temario;

        public Materia(String nombre, String temario) {
            this.nombre = nombre;
            this.temario = temario;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTemario() {
            return temario;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}


