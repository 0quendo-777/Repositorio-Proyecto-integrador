package CamposDeTextoCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

//Clase CUSTOM campo de texto para la contraseña (OQUENDO)
public class RoundedPasswordField extends JPasswordField {
    private int radius;

    public RoundedPasswordField(int columns, int radius) {
        super(columns);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo blanco con bordes redondeados
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Dibujar borde negro
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No hacer nada para evitar que el borde por defecto sobrescriba el personalizado
    }

    @Override
    public boolean contains(int x, int y) {
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius);
        return shape.contains(x, y);
    }
}
