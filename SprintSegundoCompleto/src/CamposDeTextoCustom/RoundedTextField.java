package CamposDeTextoCustom;

import javax.swing.*;
import java.awt.*;

//clase CUSTOM campos de texto redondeados (OQUENDO)
public class RoundedTextField extends JTextField {
    private int radius;

    public RoundedTextField(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);  // Hace el campo completamente transparente
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));  // Espaciado interno
        setBackground(new Color(0, 0, 0, 0));  // Fondo totalmente transparente
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el fondo blanco con bordes redondeados
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el borde negro redondeado
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
    }
}
