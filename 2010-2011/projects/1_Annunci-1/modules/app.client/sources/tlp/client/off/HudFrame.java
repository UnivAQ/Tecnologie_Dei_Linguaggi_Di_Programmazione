package tlp.client.off;


import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.explodingpixels.widgets.WindowUtils;

public class HudFrame
{
    private final JDialog fDialog;
    private JComponent fContentPane;
    private final HudFrame.HudPanel fHudPanel = new HudFrame.HudPanel();

    private static final int ROUNDED_RECT_DIAMETER = 16;

    public HudFrame(Frame owner) {
        fDialog = new JDialog(owner);
        init();
    }

    private void init() {
        fDialog.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
        fDialog.setUndecorated(true);
        fDialog.getRootPane().setOpaque(false);

        WindowUtils.makeWindowNonOpaque(fDialog);

        fDialog.getRootPane().setBackground(Color.BLACK);

        fDialog.setContentPane(fHudPanel);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        setContentPane(panel);

        WindowUtils.createAndInstallRepaintWindowFocusListener(fDialog);
    }

    public JDialog getJDialog() {
        return fDialog;
    }

    public JComponent getContentPane() {
        return fContentPane;
    }

    public void setContentPane(JComponent contentPane) {
        if (fContentPane != null) {
            fHudPanel.remove(fContentPane);
        }
        fContentPane = contentPane;
        fHudPanel.add(fContentPane, BorderLayout.CENTER);
    }

    private static class HudPanel extends JPanel {

        private static final Color HIGHLIGHT = new Color(255, 255, 255, 59);
        private static final Color HIGHLIGHT_BOTTOM = new Color(255, 255, 255, 25);
        private static final Color BACKGROUND = new Color(30, 30, 30, 216);

        private HudPanel() {
            setLayout(new BorderLayout());
            setOpaque(false);
        }

        @Override
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

        @Override
        protected void paintComponent(Graphics g) {
            // create a copy of the graphics object and turn on anti-aliasing.
            Graphics2D graphics2d = (Graphics2D) g.create();
            graphics2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2d.setComposite(AlphaComposite.Src);

            // draw the rounded rectangle background of the window.
            graphics2d.setColor(BACKGROUND);
            graphics2d.fillRoundRect(0, 0, getWidth(), getHeight(),
                    ROUNDED_RECT_DIAMETER, ROUNDED_RECT_DIAMETER);
            // tell the shadow to revalidate.
            getRootPane().putClientProperty("apple.awt.windowShadow.revalidateNow", new Object());

            graphics2d.dispose();
        }

    }

}
