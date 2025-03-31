package CamposDeTextoCustom;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

//Clase CUSTOM botones redondeados (OQUENDO)
public class RoundButton extends JButton {
    private int radius;

    public RoundButton(String label, int radius) {
        super(label);
        this.radius = radius;
        setOpaque(false);  // Evita que el botón tenga un fondo rectangular
        setFocusPainted(false);
        setContentAreaFilled(false); // Evita que Swing pinte el fondo predeterminado
        setBorderPainted(false); // Evita que Swing pinte el borde predeterminado
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Definir color dependiendo del estado del botón (hover o normal)
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker()); // Más oscuro si está presionado
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter()); // Más claro si el mouse está encima
        } else {
            g2.setColor(getBackground()); // Color normal
        }

        // Dibujar el fondo redondeado
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Dibujar el borde negro redondeado
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, radius, radius);

        g2.dispose();

        // Dibujar el texto centrado manualmente
        g.setFont(getFont());
        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - fm.getDescent();
        g.drawString(getText(), x, y);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No hacer nada para evitar que Swing pinte un borde cuadrado
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius).contains(x, y);
    }
}
