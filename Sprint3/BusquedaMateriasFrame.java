import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BusquedaMateriasFrame extends JFrame {

    // ==================================================
    //                      VARIABLES
    // ==================================================
    private JTextField campoBusqueda;
    private JTextArea areaResultados;
    private List<Materia> materias;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //                    CONSTRUCTOR
    // ==================================================
    public BusquedaMateriasFrame() {
        inicializarFrame();
        inicializarDatos();
        inicializarComponentes();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //          INICIALIZACIÓN DEL FRAME
    // ==================================================
    private void inicializarFrame() {
        setTitle("Buscar Materias");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //           INICIALIZACIÓN DE DATOS DE MATERIAS
    // ==================================================
    private void inicializarDatos() {
        materias = new ArrayList<>();
        materias.add(new Materia("Programación I", "Introducción a la programación, variables, ciclos, estructuras de control."));
        materias.add(new Materia("Matemáticas Discretas", "Lógica, conjuntos, relaciones, funciones, grafos, combinatoria."));
        materias.add(new Materia("Fundamentos de Computación", "Historia de la computación, hardware, software, algoritmos."));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //         CREACIÓN DE COMPONENTES UI
    // ==================================================
    private void inicializarComponentes() {
        // Campo de búsqueda y botón
        campoBusqueda = new JTextField();
        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMaterias();
            }
        });

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(campoBusqueda, BorderLayout.CENTER);
        panelSuperior.add(botonBuscar, BorderLayout.EAST);

        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);

        // Añadir al frame
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //             METODO DE BÚSQUEDA
    // ==================================================
    private void buscarMaterias() {
        String consulta = campoBusqueda.getText().trim().toLowerCase();
        StringBuilder resultados = new StringBuilder();

        for (Materia materia : materias) {
            if (materia.getNombre().toLowerCase().contains(consulta)) {
                resultados.append("Materia: ").append(materia.getNombre()).append("\n");
                resultados.append("Temario: ").append(materia.getTemario()).append("\n\n");
            }
        }

        if (resultados.length() == 0) {
            resultados.append("No se encontraron coincidencias exactas.\n\nSugerencias:\n");
            for (Materia materia : materias) {
                if (esSimilar(materia.getNombre(), consulta)) {
                    resultados.append("- ").append(materia.getNombre()).append("\n");
                }
            }
        }

        areaResultados.setText(resultados.toString());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //               METODO AUXILIAR
    // ==================================================
    private boolean esSimilar(String materia, String consulta) {
        String matLower = materia.toLowerCase();
        return matLower.contains(consulta)
                || (consulta.length() > 2 && matLower.startsWith(consulta.substring(0, 2)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //                CLASE INTERNA Materia
    // ==================================================
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ==================================================
    //                      MAIN
    // ==================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BusquedaMateriasFrame().setVisible(true));
    }
}
