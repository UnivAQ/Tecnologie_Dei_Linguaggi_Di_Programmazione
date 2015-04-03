package tlp.client.view.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SpringLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tlp.client.helper.FrameDragger;
import tlp.client.helper.SpringUtils;

@Service
public class MainFrame extends JFrame
{
        private Logger _logger = LoggerFactory.getLogger(MainFrame.class);

        private JRootPane    _rPane;
        private Component    _gPane;
        private JLayeredPane _lPane;
        private Container    _cPane;

        private Dimension _windowSize = new Dimension(600, 800);

        private JComponent _barBox;
        private JComponent _workBox;

        public MainFrame()
        {
                this("iAds Client");
        }

        public MainFrame(String title)
        {
                super(title);

                this._rPane = this.getRootPane();
                this._lPane = this.getLayeredPane();
                this._cPane = this.getContentPane();
                this._gPane = this.getGlassPane();

                this._init();
        }

        protected MainFrame _init()
        {
                this._defFrame()
                    ._defLookAndFeel()
                    ._defContentPane();

                return this;
        }

        protected MainFrame _defFrame()
        {
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setUndecorated(false);
                this.setResizable(false);

                this.setPreferredSize(this._windowSize);
                SpringUtils.center(this);

                return this;
        }

        protected MainFrame _defLookAndFeel()
        {
                SpringUtils.defineLookAndFeel(this._rPane);

                return this;
        }

        protected MainFrame _defContentPane()
        {
                this._cPane.setLayout(new BorderLayout());

                this._barBox  = this.barBox();
                this._workBox = this.workBox();

                this._barBox.setLayout(new SpringLayout());
                this._workBox.setLayout(new BoxLayout(this._workBox, BoxLayout.Y_AXIS));

                this._barBox.setPreferredSize(new Dimension(this._cPane.getWidth(), 34));
                this._barBox.setOpaque(false);
                this._barBox.setBorder(BorderFactory.createMatteBorder(
                        0, 0, 1, 0, new Color(135, 135, 135, 255)
                ));

                this._workBox.setPreferredSize(new Dimension(this._cPane.getWidth(), 34));

                this._cPane.add(this._barBox, BorderLayout.PAGE_START);
                this._cPane.add(this._workBox, BorderLayout.CENTER);

                return this;
        }

        public MainFrame view()
        {
                this.pack();
                this.setVisible(true);

                return this;
        }

        public JComponent barBox()
        {
                if (this._barBox == null) {
                        this._barBox = FrameDragger.attachTo(this, new JPanel());
                }

                return this._barBox;
        }

        public JComponent barBox(JComponent newPanel)
        {
                JComponent oldPanel = this.barBox();

                this._barBox = newPanel;

                this._cPane.remove(oldPanel);
                this._cPane.add(newPanel, BorderLayout.PAGE_START);

                return oldPanel;
        }

        public JComponent workBox()
        {
                if (this._workBox == null) {
                        this._workBox = new JPanel();
                }

                return this._workBox;
        }

        public JComponent workBox(JComponent newPanel)
        {
                JComponent oldPanel = this.workBox();

                this._workBox = newPanel;

                this._cPane.remove(oldPanel);
                this._cPane.add(newPanel, BorderLayout.CENTER);

                return oldPanel;
        }
}
