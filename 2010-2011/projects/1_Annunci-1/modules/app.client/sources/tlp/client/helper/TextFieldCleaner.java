package tlp.client.helper;

import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextFieldCleaner implements MouseListener, FocusListener
{
        private Logger _logger = LoggerFactory.getLogger(TextFieldCleaner.class);

        private String _origText;

        public static JTextField attachTo(JTextField field)
        {
                new TextFieldCleaner(field);

                return field;
        }

        public TextFieldCleaner(JTextField field)
        {
                this._origText = field.getText();

                field.addMouseListener(this);
                field.addFocusListener(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
                JTextField f =(JTextField) e.getComponent();

                // Boolean isCleaned =(Boolean) f.getDocument().getProperty("cleaned");
                // if (isCleaned == null) {
                //         f.setText("");
                //         f.getDocument().putProperty("cleaned", true);
                // }
                if (f.getText().equals(this._origText)) {
                        f.setText("");
                }

                f.getParent().repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void focusGained(FocusEvent e) {}

        @Override
        public void focusLost(FocusEvent e)
        {
                JTextField f =(JTextField) e.getComponent();

                if (f.getText().isEmpty()) {
                        f.setText(this._origText);
                }
        }
}
