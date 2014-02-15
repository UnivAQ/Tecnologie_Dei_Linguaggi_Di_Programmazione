package tlp.client.off;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class NewAdView
{
        private static class Panel extends JPanel {

                private static final int ROUNDED_RECT_DIAMETER = 16;
                private static final Color HIGHLIGHT = new Color(255, 255, 255, 59);
                private static final Color HIGHLIGHT_BOTTOM = new Color(255, 255, 255, 25);
                private static final Color BACKGROUND = new Color(30, 30, 30, 255);

                private Panel() {
                        setPreferredSize(new Dimension(60, 60));
                        setLayout(new BorderLayout());
                        setOpaque(false);
                        //setBackground(new Color(30, 30, 30, 95));
                        setSize(new Dimension(120, 120));
                }

        /*        @Override
                protected void paintBorder(Graphics g) {
                // create a copy of the graphics object and turn on anti-aliasing.
                Graphics2D graphics2d = (Graphics2D) g.create();
                graphics2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // paint a border around the window that fades slightly to give a more pleasnt highlight
                // to the window edges.
                GradientPaint paint = new GradientPaint(0, 0, HIGHLIGHT, 0, getHeight(), HIGHLIGHT_BOTTOM);
                graphics2d.setPaint(paint);
                graphics2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ROUNDED_RECT_DIAMETER,
                        ROUNDED_RECT_DIAMETER);

                graphics2d.dispose();
                }
        */
                @Override
                protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        // create a copy of the graphics object and turn on anti-aliasing.
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        g2d.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, 1.0f
                        ));

                        // draw the rounded rectangle background of the window.
                        g2d.setColor(new Color (0, 0, 0, 155));
                        g2d.setStroke(new BasicStroke(3f));
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(),
                                ROUNDED_RECT_DIAMETER, ROUNDED_RECT_DIAMETER);
                        // tell the shadow to revalidate.
                        getRootPane().putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());

                        g2d.dispose();
                }
        }
}
