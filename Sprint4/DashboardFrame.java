import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {

    // ==================================================
    //                      VARIABLES
    // ==================================================
    private Connection conexion;
    private ImageIcon background;
    private String usuarioActualCorreo;           // correo del usuario logueado
    private JTextField campoBusqueda;             // campo para buscar materias
    private JTextArea areaResultados;             // area para mostrar resultados
    private List<Materia> materias;               // lista de materias
    private JPanel panelContenido;                // panel central dinamico

    // ==================================================
    //                    CONSTRUCTORES
    // ==================================================
    public DashboardFrame(String correoUsuario) {
        this.usuarioActualCorreo = correoUsuario;
        inicializarFrame();
        conexion = ConexionDB.conectar();
        inicializarLayout();
        inicializarMaterias();
        mostrarPanelBienvenida();
    }

    // Constructor sin parametros (compatibilidad)
    public DashboardFrame() {
        this(null);
    }

    // ==================================================
    //               INICIALIZACION DEL FRAME
    // ==================================================
    private void inicializarFrame() {
        setTitle("Panel Principal - SOVIS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // cargar imagen de fondo desde resources
        background = new ImageIcon(getClass().getResource("/resources/fondo_2.png"));
    }

    private void inicializarLayout() {
        // panel con fondo pintado
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelFondo.setLayout(new BorderLayout());
        setContentPane(panelFondo);

        // barra de titulo
        JPanel barraTitulo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        barraTitulo.setBackground(new Color(50, 50, 50, 180));
        panelFondo.add(barraTitulo, BorderLayout.NORTH);

        // panel lateral
        JPanel panelLateral = crearPanelLateral();
        panelFondo.add(panelLateral, BorderLayout.WEST);

        // panel central dinamico
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);
        panelFondo.add(panelContenido, BorderLayout.CENTER);

        setVisible(true);
    }

    // crea los botones del menu lateral
    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setBackground(new Color(20, 20, 20, 200));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // titulo
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setMaximumSize(new Dimension(200, 80));
        JLabel lblTitulo = new JLabel("SOVIS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 50));
        lblTitulo.setForeground(Color.WHITE);
        titlePanel.add(lblTitulo);
        panel.add(titlePanel);
        panel.add(Box.createVerticalStrut(20));

        // botones estilizados
        panel.add(crearBotonEstilizado("Mi Perfil", e -> mostrarPerfilUsuario()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearBotonEstilizado("Buscar Usuario", e -> buscarUsuario()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearBotonEstilizado("Modificar Usuario", e -> modificarUsuario()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearBotonEstilizado("Eliminar Usuario", e -> eliminarUsuario()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearBotonEstilizado("Buscar Materias", e -> mostrarBusquedaMaterias()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearBotonEstilizado("Cerrar Sesion", e -> cerrarSesion()));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ==================================================
    //          PANEL DE BIENVENIDA / MATERIAS
    // ==================================================
    private void mostrarPanelBienvenida() {
        panelContenido.removeAll();
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        gbc.gridy = 0;
        panelCentral.add(crearCajaInfo("Bienvenido a SOVIS - Sistema de Gestion", 18, true), gbc);
        gbc.gridy++;
        panelCentral.add(crearCajaInfo("Seleccione una opcion del menu lateral", 16, false), gbc);

        panelContenido.add(panelCentral, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private JPanel crearCajaInfo(String texto, int fontSize, boolean bold) {
        JPanel caja = new JPanel();
        caja.setPreferredSize(new Dimension(600,70));
        caja.setBackground(new Color(80,80,80,180));
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", bold? Font.BOLD: Font.PLAIN, fontSize));
        lbl.setForeground(Color.WHITE);
        caja.add(lbl);
        return caja;
    }

    private void inicializarMaterias() {
        materias = new ArrayList<>();
        materias.add(new Materia("Programacion I", "Introduccion a la programacion, variables, ciclos, estructuras de control."));
        materias.add(new Materia("Matematicas Discretas", "Logica, conjuntos, relaciones, funciones, grafos, combinatoria."));
        materias.add(new Materia("Fundamentos de Computacion", "Historia de la computacion, hardware, software, algoritmos."));
        materias.add(new Materia("Estructura de Datos", "Listas, pilas, colas, arboles, grafos, algoritmos de ordenamiento y busqueda."));
        materias.add(new Materia("Bases de Datos", "Modelo ER, normalizacion, SQL, transacciones, stored procedures."));
        materias.add(new Materia("Programacion Orientada a Objetos", "Clases, objetos, herencia, polimorfismo, abstraccion, encapsulamiento."));
    }

    private void mostrarBusquedaMaterias() {
        panelContenido.removeAll();
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel titulo = new JLabel("Busqueda de Materias", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo, BorderLayout.NORTH);

        // campo + boton
        JPanel pb = new JPanel(new BorderLayout(5,0));
        pb.setOpaque(false);
        campoBusqueda = new JTextField();
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 14));
        campoBusqueda.setPreferredSize(new Dimension(400,30));
        JButton botonBuscar = crearBotonRedondo("Buscar", 10);
        pb.add(campoBusqueda, BorderLayout.CENTER);
        pb.add(botonBuscar, BorderLayout.EAST);
        panel.add(pb, BorderLayout.CENTER);

        // resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Arial", Font.PLAIN, 14));
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        areaResultados.setBackground(new Color(240,240,240));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setPreferredSize(new Dimension(600,300));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(41,128,185),1));
        panel.add(scroll, BorderLayout.SOUTH);

        panelContenido.add(panel, BorderLayout.CENTER);

        ActionListener accion = e -> buscarMaterias();
        botonBuscar.addActionListener(accion);
        campoBusqueda.addActionListener(accion);

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    // logica de busqueda
    private void buscarMaterias() {
        String consulta = campoBusqueda.getText().trim().toLowerCase();
        StringBuilder res = new StringBuilder();
        if (consulta.isEmpty()) {
            res.append("Por favor ingrese un termino de busqueda.");
            areaResultados.setText(res.toString());
            return;
        }
        boolean encontrado = false;
        for (Materia m : materias) {
            if (m.getNombre().toLowerCase().contains(consulta)) {
                if (!encontrado) {
                    res.append("=== RESULTADOS DE BUSQUEDA ===\n\n");
                    encontrado = true;
                }
                res.append("▶ Materia: ").append(m.getNombre()).append("\n")
                        .append("  Temario: ").append(m.getTemario()).append("\n\n");
            }
        }
        if (!encontrado) {
            res.append("No se encontraron coincidencias exactas.\n\n");
            res.append("=== SUGERENCIAS ===\n\n");
            boolean sug = false;
            for (Materia m : materias) {
                if (esSimilar(m.getNombre(), consulta)) {
                    sug = true;
                    res.append("- ").append(m.getNombre()).append("\n");
                }
            }
            if (!sug) {
                res.append("No hay sugerencias disponibles. Intente con otra palabra clave.");
            }
        }
        areaResultados.setText(res.toString());
        areaResultados.setCaretPosition(0);
    }

    private boolean esSimilar(String materia, String consulta) {
        materia = materia.toLowerCase();
        consulta = consulta.toLowerCase();
        return materia.contains(consulta)
                || (consulta.length()>2 && materia.startsWith(consulta.substring(0,2)));
    }

    // ==================================================
    //             METODOS DE USUARIO CON AVATAR
    // ==================================================
    private void mostrarPerfilUsuario() {
        // si no tenemos correo, pedimos al usuario
        if (usuarioActualCorreo == null || usuarioActualCorreo.trim().isEmpty()) {
            usuarioActualCorreo = JOptionPane.showInputDialog(this, "Ingrese su correo para ver su perfil:");
            if (usuarioActualCorreo == null || usuarioActualCorreo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Debe ingresar un correo para ver su perfil.");
                return;
            }
        }
        try {
            // consulta a BD
            String sql = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, usuarioActualCorreo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // PANEL PRINCIPAL con GridBagLayout
                JPanel panelInfo = new JPanel(new GridBagLayout());
                panelInfo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                panelInfo.setOpaque(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5,5,5,5);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.CENTER;

                // 1) CARGAR AVATAR DESDE RESOURCES ----------------------
                String nombreArchivo = rs.getString("avatar"); // nombre en BD
                ImageIcon icono = new ImageIcon(getClass().getResource("/resources/" + nombreArchivo));
                Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel lblAvatar = new JLabel(new ImageIcon(img));
                panelInfo.add(lblAvatar, gbc);

                // 2) TITULO
                gbc.gridy++;
                JLabel lblTitulo = new JLabel("Informacion del Usuario");
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
                lblTitulo.setForeground(new Color(41, 128, 185));
                panelInfo.add(lblTitulo, gbc);

                // 3) CAMPOS (GridBag, etiqueta + valor)
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridx = 0;
                gbc.gridy++;
                addInfoField(panelInfo, gbc, "ID:", String.valueOf(rs.getInt("id")));
                gbc.gridy++;
                addInfoField(panelInfo, gbc, "Nombre:", rs.getString("nombre"));
                gbc.gridy++;
                addInfoField(panelInfo, gbc, "Correo:", rs.getString("correo"));
                // puedes agregar mas campos segun tu tabla...

                // mostrar en dialogo
                JOptionPane op = new JOptionPane(panelInfo, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = op.createDialog(this, "Mi Perfil - SOVIS");
                dialog.setSize(320, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠️ No se encontró informacion para: " + usuarioActualCorreo);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error al obtener info del perfil: " + e.getMessage());
        }
    }

    // UTILITY: añade par etiqueta+valor en GridBag
    private void addInfoField(JPanel panel, GridBagConstraints gbc, String label, String value) {
        JLabel lblCampo = new JLabel(label);
        lblCampo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        panel.add(lblCampo, gbc);

        JLabel lblValor = new JLabel(value != null ? value : "N/A");
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(lblValor, gbc);
    }

    //Buscar Materias


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //             MÉTODOS DE USUARIO
    // ==================================================


    //Buscar Usuario con Avatar
    private void buscarUsuario() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del usuario:");
        if (nombre != null && !nombre.trim().isEmpty()) {

            try {
                String sql = "SELECT * FROM usuarios WHERE nombre LIKE ?";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setString(1, "%" + nombre + "%");
                ResultSet rs = stmt.executeQuery();

                // Lista para almacenar los usuarios encontrados
                List<Usuario> usuariosEncontrados = new ArrayList<>();

                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.id = rs.getInt("id");
                    usuario.nombre = rs.getString("nombre");
                    usuario.correo = rs.getString("correo");
                    usuario.avatar = rs.getString("avatar");
                    usuariosEncontrados.add(usuario);
                }

                rs.close();
                stmt.close();

                if (usuariosEncontrados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ No se encontraron usuarios con ese nombre.");
                    return;
                }

                // Crear panel para mostrar resultados con avatares
                mostrarResultadosUsuarios(usuariosEncontrados, "Usuarios encontrados:");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error al buscar usuario: " + e.getMessage());
            }
        }
    }

    // Método auxiliar para mostrar usuarios con avatares
    private void mostrarResultadosUsuarios(List<Usuario> usuarios, String titulo) {
        // Panel principal con scroll
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(Color.WHITE);

        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createVerticalStrut(15));

        // Crear un panel para cada usuario
        for (Usuario usuario : usuarios) {
            JPanel panelUsuario = crearPanelUsuario(usuario);
            panelPrincipal.add(panelUsuario);
            panelPrincipal.add(Box.createVerticalStrut(10));
        }

        // Crear scroll pane
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setPreferredSize(new Dimension(450, Math.min(400, usuarios.size() * 100 + 100)));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));

        // Mostrar en diálogo
        JOptionPane.showMessageDialog(this, scrollPane, "Resultados de Búsqueda - SOVIS", JOptionPane.PLAIN_MESSAGE);
    }

    // Crear panel individual para cada usuario
    private JPanel crearPanelUsuario(Usuario usuario) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        panel.setBackground(new Color(250, 250, 250));
        panel.setPreferredSize(new Dimension(400, 80));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Avatar (lado izquierdo)
        JLabel lblAvatar = new JLabel();
        try {
            String nombreArchivo = usuario.avatar != null ? usuario.avatar : "default_avatar.png";
            ImageIcon icono = new ImageIcon(getClass().getResource("/resources/" + nombreArchivo));
            Image img = icono.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            lblAvatar.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // Si no se puede cargar el avatar, usar un placeholder
            lblAvatar.setText("👤");
            lblAvatar.setFont(new Font("Arial", Font.PLAIN, 30));
            lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        }
        lblAvatar.setPreferredSize(new Dimension(60, 60));
        panel.add(lblAvatar, BorderLayout.WEST);

        // Información del usuario (centro)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        JLabel lblNombre = new JLabel("👤 " + usuario.nombre);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setForeground(new Color(51, 51, 51));

        JLabel lblCorreo = new JLabel("📧 " + usuario.correo);
        lblCorreo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCorreo.setForeground(new Color(102, 102, 102));

        JLabel lblId = new JLabel("🆔 ID: " + usuario.id);
        lblId.setFont(new Font("Arial", Font.PLAIN, 11));
        lblId.setForeground(new Color(128, 128, 128));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(lblCorreo);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(lblId);

        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

    // Clase auxiliar para almacenar datos del usuario
    private static class Usuario {
        int id;
        String nombre;
        String correo;
        String avatar;
    }

    //Modificar Usuario
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

    // Eliminar Usuario
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

    //Cerrar Sesión
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que quiere cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Cerrando sesión...");
            dispose();
            new LoginFrame();
        }
    }

    // ==================================================
    //           UTILS DE BOTONES (sin cambios)
    // ==================================================
    private JPanel crearBotonEstilizado(String texto, ActionListener actionListener) {
        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        bp.setOpaque(false);
        bp.setMaximumSize(new Dimension(180,40));
        bp.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(41,128,185));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
                g2.setColor(new Color(25,79,115));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,15,15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD,12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160,36));
        btn.addActionListener(actionListener);

        bp.add(Box.createHorizontalGlue());
        bp.add(btn);
        bp.add(Box.createHorizontalGlue());
        return bp;
    }

    private JButton crearBotonRedondo(String texto, int arc) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(41,128,185));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),arc,arc);
                g2.setColor(new Color(25,79,115));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,arc,arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setFont(new Font("Arial", Font.BOLD,12));
        boton.setForeground(Color.WHITE);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(100,30));
        return boton;
    }

    // ==================================================
    //                      MAIN
    // ==================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }

    // ==================================================
    //               CLASE INTERNA Materia
    // ==================================================
    private static class Materia {
        private String nombre;
        private String temario;
        public Materia(String nombre, String temario) {
            this.nombre = nombre;
            this.temario = temario;
        }
        public String getNombre()  { return nombre; }
        public String getTemario() { return temario; }
    }
}

