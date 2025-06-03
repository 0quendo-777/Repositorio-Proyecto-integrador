import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormularioReflexionFrame extends JFrame {

    // ==================================================
    //                      VARIABLES
    // ==================================================
    private int usuarioId;
    private JComboBox<String>[] respuestas;
    private String[] preguntas = {
            "¿Por qué te interesa estudiar Ingeniería de Sistemas?",
            "¿Te gusta resolver problemas lógicos y matemáticos?",
            "¿Cómo te sientes aprendiendo nuevas tecnologías por tu cuenta?",
            "¿Has intentado programar alguna vez? Si es así, ¿te gustó la experiencia?",
            "¿Prefieres trabajar solo o en equipo para resolver problemas?"
    };
    private String[][] opciones = {
            {
                    "Me gusta la tecnología y quiero aprender a programar.",
                    "Es una carrera con buena salida laboral.",
                    "Me interesa la computación y cómo funcionan los sistemas.",
                    "No estoy seguro, pero quiero explorar si es para mí."
            },
            {
                    "Sí, me encanta.",
                    "A veces, dependiendo del problema.",
                    "No mucho, prefiero tareas más creativas.",
                    "No, se me dificulta bastante."
            },
            {
                    "Me gusta y lo hago con frecuencia.",
                    "Lo hago si es necesario, pero prefiero que me expliquen.",
                    "Me cuesta, pero lo intento.",
                    "No me gusta aprender solo, prefiero guías detalladas."
            },
            {
                    "Sí, y me encantó.",
                    "Sí, pero fue difícil y no estoy seguro si es para mí.",
                    "No, pero me interesa aprender.",
                    "No, y no me llama mucho la atención."
            },
            {
                    "Prefiero trabajar solo.",
                    "Me gusta trabajar en equipo.",
                    "Depende del tipo de problema."
            }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //                    CONSTRUCTOR
    // ==================================================
    public FormularioReflexionFrame(int usuarioId) {
        this.usuarioId = usuarioId;
        inicializarFrame();
        inicializarComponentes();
        setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //          INICIALIZACIÓN DEL FRAME
    // ==================================================
    private void inicializarFrame() {
        setTitle("Formulario de Reflexión");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //         CREACIÓN DE COMPONENTES UI
    // ==================================================
    @SuppressWarnings("unchecked")
    private void inicializarComponentes() {
        // Panel principal con fondo claro
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(230, 240, 255));

        // Inicializar respuestas
        respuestas = new JComboBox[preguntas.length];

        // Crear cada pregunta con su JComboBox
        for (int i = 0; i < preguntas.length; i++) {
            JPanel panelPregunta = new JPanel(new BorderLayout());
            panelPregunta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true)
            ));
            panelPregunta.setBackground(Color.WHITE);

            JLabel label = new JLabel(preguntas[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(new Color(44, 62, 80));

            respuestas[i] = new JComboBox<>(opciones[i]);
            respuestas[i].setFont(new Font("Arial", Font.PLAIN, 12));
            respuestas[i].setPreferredSize(new Dimension(500, 25));
            respuestas[i].setBackground(Color.WHITE);
            respuestas[i].setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219)));

            panelPregunta.add(label, BorderLayout.NORTH);
            panelPregunta.add(respuestas[i], BorderLayout.CENTER);
            panelPrincipal.add(panelPregunta);
        }

        // Botón de envío
        JButton btnEnviar = crearBotonEnviar();
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(230, 240, 255));
        panelBoton.add(btnEnviar);

        // Añadir al frame
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //          METODO PARA CREAR BOTÓN ENVIAR
    // ==================================================
    private JButton crearBotonEnviar() {
        JButton btnEnviar = new JButton("Enviar respuestas");
        btnEnviar.setBackground(new Color(52, 152, 219));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        btnEnviar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarRespuestas();
            }
        });
        return btnEnviar;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //             METODO PARA GUARDAR DATOS
    // ==================================================
    private void guardarRespuestas() {
        try (Connection conexion = ConexionDB.conectar()) {
            if (conexion != null) {
                String sqlInsert = "INSERT INTO respuestas_formulario (usuario_id, pregunta, respuesta) VALUES (?, ?, ?)";
                PreparedStatement psInsert = conexion.prepareStatement(sqlInsert);

                for (int i = 0; i < preguntas.length; i++) {
                    psInsert.setInt(1, usuarioId);
                    psInsert.setString(2, preguntas[i]);
                    psInsert.setString(3, (String) respuestas[i].getSelectedItem());
                    psInsert.executeUpdate();
                }

                String sqlActualizar = "UPDATE usuarios SET formulario_completado = 1 WHERE id = ?";
                PreparedStatement psActualizar = conexion.prepareStatement(sqlActualizar);
                psActualizar.setInt(1, usuarioId);
                psActualizar.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Respuestas guardadas correctamente.");
                dispose();
                new DashboardFrame();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar respuestas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
