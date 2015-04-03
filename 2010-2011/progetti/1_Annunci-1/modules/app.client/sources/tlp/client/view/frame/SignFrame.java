package tlp.client.view.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tlp.client.helper.FrameDragger;
import tlp.client.helper.SpringUtils;
import tlp.client.view.View;

@Service
public class SignFrame extends JFrame implements View
{
        private Logger _logger = LoggerFactory.getLogger(SignFrame.class);

        private JRootPane    _rPane;
        private Component    _gPane;
        private JLayeredPane _lPane;
        private Container    _cPane;

        private Dimension _windowSize = new Dimension(800, 500);

        public SignFrame()
        {
                this("iAds Client");
        }

        public SignFrame(String title)
        {
                super(title);

                this._rPane = this.getRootPane();
                this._lPane = this.getLayeredPane();
                this._cPane = this.getContentPane();
                this._gPane = this.getGlassPane();

                this._init();
        }

        protected SignFrame _init()
        {
                this._defFrame()
                    ._defLookAndFeel()
                    ._defContentPane();

                return this;
        }

        protected SignFrame _defFrame()
        {
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setUndecorated(true);
                this.setResizable(false);

                this.setPreferredSize(this._windowSize);
                SpringUtils.center(this);

                FrameDragger.attachTo(this._cPane);

                return this;
        }

        protected SignFrame _defLookAndFeel()
        {
                SpringUtils.defineLookAndFeel(this._rPane);

                return this;
        }

        protected SignFrame _defContentPane()
        {
                this._cPane.setLayout(new BorderLayout());

                return this;
        }

        @Override
        public SignFrame view()
        {
                this.pack();
                this.setVisible(true);

                return this;
        }
}
