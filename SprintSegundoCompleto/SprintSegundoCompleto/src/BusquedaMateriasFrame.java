import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BusquedaMateriasFrame extends JFrame {
    private JTextField campoBusqueda;
    private JTextArea areaResultados;
    private List<Materia> materias;

    public BusquedaMateriasFrame() {
        setTitle("Buscar Materias");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear materias de ejemplo
        materias = new ArrayList<>();
        materias.add(new Materia("Programación I", "Introducción a la programación, variables, ciclos, estructuras de control."));
        materias.add(new Materia("Matemáticas Discretas", "Lógica, conjuntos, relaciones, funciones, grafos, combinatoria."));
        materias.add(new Materia("Fundamentos de Computación", "Historia de la computación, hardware, software, algoritmos."));

        // Campo de búsqueda
        campoBusqueda = new JTextField();
        JButton botonBuscar = new JButton("Buscar");
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.add(campoBusqueda, BorderLayout.CENTER);
        panelSuperior.add(botonBuscar, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);

        // Acción del botón
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarMaterias();
            }
        });
    }

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

    private boolean esSimilar(String materia, String consulta) {
        return materia.toLowerCase().contains(consulta) || consulta.length() > 2 && materia.toLowerCase().startsWith(consulta.substring(0, 2));
    }

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
        SwingUtilities.invokeLater(() -> new BusquedaMateriasFrame().setVisible(true));
    }
}
