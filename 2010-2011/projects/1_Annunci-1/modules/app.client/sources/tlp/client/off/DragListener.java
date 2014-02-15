package tlp.client.off;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import tlp.client.view.frame.MainFrame;

public class DragListener implements MouseListener, MouseMotionListener
{
        private JComponent _target;
        private Point _startDrag;
        private Point _startLoc;

        public DragListener(JComponent target)
        {
                this._target = target;
        }

        public static MainFrame getFrame(Container target)
        {
                if (target instanceof MainFrame) {
                        return (MainFrame) target;
                }

                return DragListener.getFrame(target.getParent());
        }

        public Point getScreenLocation(MouseEvent e)
        {
                Point cursor = e.getPoint();
                Point target_location = this._target.getLocationOnScreen();

                return new Point(
                        (int) (target_location.getX() + cursor.getX()),
                        (int) (target_location.getY() + cursor.getY())
                );
        }

        @Override public void mouseClicked(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseMoved(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e)
        {
                this._startDrag = this.getScreenLocation(e);
                this._startLoc = this.getFrame(this._target).getLocation();
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
                Point current = this.getScreenLocation(e);
                Point offset = new Point(
                        (int) current.getX() - (int) _startDrag.getX(),
                        (int) current.getY() - (int) _startDrag.getY()
                );

                MainFrame frame = this.getFrame(_target);

                Point new_location = new Point(
                        (int) (this._startLoc.getX() + offset.getX()),
                        (int) (this._startLoc.getY() + offset.getY())
                );

                frame.setLocation(new_location);
        }
}
