package tlp.client.helper;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SpringUtils
{
        public static void defineLookAndFeel(JRootPane pane)
        {
                try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (UnsupportedLookAndFeelException e) {
                }

                pane.putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
        }

        public static Dimension getScreenSize()
        {
                return Toolkit.getDefaultToolkit().getScreenSize();
        }

        public static void center(Window window)
        {
                Dimension windowSize = window.getPreferredSize();

                // Algorithm 1.
                // Dimension screenSize = SpringUtils.getScreenSize();
                // window.setLocation(
                //        (screenSize.width  / 2) - (windowSize.width  / 2),
                //        (screenSize.height / 2) - (windowSize.height / 2)
                // );


                // Algorithm 2.
                Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
                window.setLocation(
                        centerPoint.x - (windowSize.width  / 2),
                        centerPoint.y - (windowSize.height / 2)
                );
        }

        public static void center(Window anchor, Window window)
        {
                Point anchorPoint = anchor.getLocationOnScreen();
                Dimension anchorSize = anchor.getPreferredSize();
                Dimension windowSize = window.getPreferredSize();

                window.setLocation(
                        anchorPoint.x + (anchorSize.width  / 2) - (windowSize.width  / 2),
                        anchorPoint.y + (anchorSize.height / 2) - (windowSize.height / 2)
                );
        }
}
