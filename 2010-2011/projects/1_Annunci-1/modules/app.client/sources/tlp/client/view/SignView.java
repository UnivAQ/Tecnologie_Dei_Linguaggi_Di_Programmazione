package tlp.client.view;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.client.config.ConfigManager;
import tlp.client.config.SessionManager;
import tlp.client.config.UserIdProp;
import tlp.client.config.UserUsernameProp;
import tlp.client.helper.PaintedPanel;
import tlp.client.helper.SpringLayoutStrings;
import tlp.client.helper.TextFieldCleaner;
import tlp.client.model.UserModel;
import tlp.client.service.Backend;
import tlp.client.service.BackendType;
import tlp.client.service.UserService;
import tlp.client.view.frame.MainFrame;
import tlp.client.view.frame.SignFrame;
import tlp.common.core.ContextFactory;

@Service
public class SignView implements View
{
        private Logger _logger = LoggerFactory.getLogger(SignView.class);

        private SignFrame _frame;
        private Container _box;

        private UserService    _userService;
        private ConfigManager  _config;
        private SessionManager _session;

        private String _signImage = "Resources/sign-image.png";
        private String _closeIcon = "Resources/close-icon.png";

        private int _textFieldColumns = 18;

        @Autowired
        public SignView(
                SignFrame frame,
                @Backend(BackendType.MongoDB) UserService userService,
                ConfigManager config,
                SessionManager session)
        {
                this._frame = frame;
                this._box   = frame.getContentPane();

                this._userService = userService;
                this._config      = config;
                this._session     = session;

                this._init();
        }

        protected SignView _init()
        {
                PaintedPanel panel = this._newPaintedPanel();

                JButton closeButton = this._newCloseButton();

                JTextField userField = this._newUsernameField();
                JPasswordField passField = this._newPasswordField();

                JButton signupButton = this._newSignupButton(userField, passField);
                JButton signinButton = this._newSigninButton(userField, passField);

                SpringLayout layout = new SpringLayout();
                SpringLayoutStrings l = new SpringLayoutStrings();
                layout.putConstraint(l.NORTH, closeButton, 0, l.NORTH, panel);
                layout.putConstraint(l.EAST, closeButton, 0, l.EAST, panel);
                layout.putConstraint(l.NORTH, userField, -80, l.VCENTER, panel);
                layout.putConstraint(l.WEST, userField, -(userField.getPreferredSize().width / 2), l.HCENTER, panel);
                layout.putConstraint(l.NORTH, passField, 10, l.SOUTH, userField);
                layout.putConstraint(l.WEST, passField, 0, l.WEST, userField);
                layout.putConstraint(l.NORTH, signupButton, 10, l.SOUTH, passField);
                layout.putConstraint(l.WEST, signupButton, 20, l.WEST, passField);
                layout.putConstraint(l.NORTH, signinButton, 10, l.SOUTH, passField);
                layout.putConstraint(l.WEST, signinButton, 20, l.EAST, signupButton);

                panel.setLayout(layout);
                panel.add(closeButton);
                panel.add(userField);
                panel.add(passField);
                panel.add(signupButton);
                panel.add(signinButton);

                this._box.add(panel, BorderLayout.CENTER);

                return this;
        }

        protected PaintedPanel _newPaintedPanel()
        {
                Image image = new ImageIcon(SignView.class.getResource(this._signImage)).getImage();

                PaintedPanel panel = new PaintedPanel(image);

                return panel;
        }

        protected JButton _newCloseButton()
        {
                ImageIcon icon = new ImageIcon(SignView.class.getResource(this._closeIcon));

                JButton button = new JButton(icon);
                button.setBorderPainted(false);
                button.addActionListener(this._newCloseButtonActionListener());

                return button;
        }

        protected ActionListener _newCloseButtonActionListener()
        {
                return new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                                SignView.this._frame.dispose();
                        }
                };
        }

        protected JButton _newSignupButton(JTextField userField, JPasswordField passField)
        {
                JButton button = HudWidgetFactory.createHudButton("Register");
                button.addActionListener(this._newSignupButtonActionListener(userField, passField));

                return button;
        }

        protected ActionListener _newSignupButtonActionListener(final JTextField userField, final JPasswordField passField)
        {
                return new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                                SignView self = SignView.this;

                                UserModel.Username u = new UserModel.Username(userField);
                                UserModel.Password p = new UserModel.Password(passField);

                                if (! self._userService.existsUsername(u)) {
                                        UserModel userModel = new UserModel()
                                                .set(u)
                                                .set(p)
                                        ;

                                        self._userService.newUser(userModel);

                                        self._storeIdentity(userModel);
                                        self._giveWay();
                                }
                        }
                };
        }

        protected JButton _newSigninButton(JTextField userField, JPasswordField passField)
        {
                JButton button = HudWidgetFactory.createHudButton("Sign In");
                button.addActionListener(this._newSigninButtonActionListener(userField, passField));

                return button;
        }

        protected ActionListener _newSigninButtonActionListener(final JTextField userField, final JPasswordField passField)
        {
                return new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                                UserModel.Username u = new UserModel.Username(userField);
                                UserModel.Password p = new UserModel.Password(passField);

                                if (SignView.this._userService.authenticate(u, p)) {
                                        SignView self = SignView.this;

                                        UserModel.Id id = self._userService.getUidOf(u);

                                        UserModel userModel = new UserModel()
                                                .set(id)
                                                .set(u)
                                                .set(p)
                                        ;

                                        self._storeIdentity(userModel);
                                        self._giveWay();
                                }
                        }
                };
        }

        protected JTextField _newUsernameField()
        {
                JTextField field = new JTextField("Username...", this._textFieldColumns);

                String username = this._fetchUsername();
                if (username != null) {
                        field.setText(username);
                }

                TextFieldCleaner.attachTo(field);

                return field;
        }

        protected JPasswordField _newPasswordField()
        {
                JPasswordField field = new JPasswordField("Password...", this._textFieldColumns);

                TextFieldCleaner.attachTo(field);

                return field;
        }

        protected String _fetchUsername()
        {
                return this._config.get(new UserUsernameProp());
        }

        protected SignView _storeIdentity(UserModel model)
        {
                UserIdProp id         = new UserIdProp(model.get(new UserModel.Id()));
                UserUsernameProp user = new UserUsernameProp(model.get(new UserModel.Username()));

                this._config.set(id).set(user);
                this._session.set(id).set(user);

                return this;
        }

        protected SignView _giveWay()
        {
                MainFrame mainFrame = ContextFactory.get(MainFrame.class);
                MainView mainView = ContextFactory.get(MainView.class);

                this._frame.dispose();

                mainView.view();
                mainFrame.view();

                return this;
        }

        @Override
        public SignView view()
        {
                this._frame.view();

                return this;
        }
}
