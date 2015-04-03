package tlp.client.helper;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrameDragger
{
        private Logger _logger = LoggerFactory.getLogger(FrameDragger.class);

        private Component _window;
        private Component _draggable;

        private int _inWindowX;
        private int _inWindowY;

        public static <T extends Component> T attachTo(T draggable)
        {
                new FrameDragger(draggable);

                return draggable;
        }

        public static <T extends Component> T attachTo(Window window, T draggable)
        {
                new FrameDragger(window, draggable);

                return draggable;
        }

        // This constructor is not always appropriate,
        // because a component may have not been attached yet
        // to a parent component.
        public FrameDragger(Component draggable)
        {
                this(SwingUtilities.getRoot(draggable), draggable);
        }

        public FrameDragger(Component window, Component draggable)
        {
                this._window    = window;
                this._draggable = draggable;

                this._draggable.addMouseListener(this._newMouseListener());
                this._draggable.addMouseMotionListener(this._newMouseMotionListener());
        }

        protected MouseListener _newMouseListener()
        {
                return new MouseAdapter() {
                        @Override public void mousePressed(MouseEvent e) {
                                Point clickPoint = new Point(e.getPoint());

                                SwingUtilities.convertPointToScreen(clickPoint, _draggable);
                                _inWindowX = clickPoint.x - _window.getX();
                                _inWindowY = clickPoint.y - _window.getY();
                        }
                };
        }

        protected MouseMotionAdapter _newMouseMotionListener()
        {
                return new MouseMotionAdapter() {
                        @Override public void mouseDragged(MouseEvent e) {
                                // Algorithm 1.
                                // Point dragPoint = new Point(e.getPoint());
                                // SwingUtilities.convertPointToScreen(dragPoint, _component);

                                // Algorithm 2.
                                //Point dragPoint = e.getLocationOnScreen();

                                // Algorithm 3.
                                Point dragPoint = MouseInfo.getPointerInfo().getLocation();

                                _window.setLocation(
                                        dragPoint.x - _inWindowX,
                                        dragPoint.y - _inWindowY
                                );
                        }
                };
        }
}
