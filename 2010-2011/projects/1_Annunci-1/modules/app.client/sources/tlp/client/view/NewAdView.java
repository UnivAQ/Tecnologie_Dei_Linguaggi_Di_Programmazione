package tlp.client.view;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.explodingpixels.macwidgets.HudWindow;
import com.explodingpixels.macwidgets.plaf.HudPaintingUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tlp.client.config.ConfigManager;
import tlp.client.config.UserIdProp;
import tlp.client.helper.SpringLayoutStrings;
import tlp.client.helper.SpringUtils;
import tlp.client.helper.TextFieldCleaner;
import tlp.client.model.AdModel;
import tlp.client.model.Field;
import tlp.client.service.AdService;
import tlp.client.service.Backend;
import tlp.client.service.BackendType;
import tlp.client.view.frame.MainFrame;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewAdView extends HudWindow implements View
{
        private Logger _logger = LoggerFactory.getLogger(NewAdView.class);

        private MainFrame _frame;
        private MainView  _view;
        private JDialog   _dialog;

        private AdService     _adService;
        private ConfigManager _config;

        private Dimension _windowSize = new Dimension(400, 220);

        private HashMap<Class<?>, Object> _components = new HashMap<Class<?>, Object>();

        @Autowired
        public NewAdView(
                MainFrame frame,
                MainView view,
                @Backend(BackendType.MongoDB) AdService adService,
                ConfigManager config)
        {
                super("New Ad", frame);

                this._frame  = frame;
                this._view   = view;
                this._dialog = this.getJDialog();

                this._adService = adService;
                this._config    = config;

                this._init();
        }

        protected NewAdView _init()
        {
                this.hideCloseButton();
                this._dialog.setUndecorated(true);

                this._dialog.setPreferredSize(this._windowSize);
                this._dialog.setSize(this._windowSize);

                // Horizontally centered,
                // but vertically top aligned.
                SpringUtils.center(this._frame, this._dialog);
                this._dialog.setLocation(new Point(
                        this._dialog.getLocation().x,
                        this._frame.workBox().getLocationOnScreen().y
                ));

                //this._dialog.setModalityType(Dialog.ModalityType.MODELESS);
                //this._dialog.setFocusableWindowState(false);
                //this._dialog.setFocusable(true);

                this.setContentPane(this._newPanel());

                return this;
        }

        protected JPanel _newPanel()
        {
                final JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());

                panel.add(this._newTopPanel(), BorderLayout.NORTH);
                panel.add(this._newCenterPanel(), BorderLayout.CENTER);
                panel.add(this._newBottomPanel(), BorderLayout.SOUTH);

                return panel;
        }

        protected JPanel _newTopPanel()
        {
                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());

                JRadioButton saleRadio = new JRadioButton("For Sale");
                JRadioButton wantedRadio = new JRadioButton("Wanted");
                saleRadio.setActionCommand(AdModel.TypeType.SALE.toString());
                wantedRadio.setActionCommand(AdModel.TypeType.WANTED.toString());
                HudPaintingUtils.initHudComponent(wantedRadio);
                HudPaintingUtils.initHudComponent(saleRadio);

                final ButtonGroup group = new ButtonGroup();
                group.add(saleRadio);
                group.add(wantedRadio);
                group.setSelected(saleRadio.getModel(), true);

                panel.add(saleRadio);
                panel.add(wantedRadio);

                this._components.put(AdModel.Type.class, group);

                return panel;
        }

        protected JPanel _newCenterPanel()
        {
                final JPanel panel = new JPanel();
                SpringLayout layout = new SpringLayout();
                panel.setLayout(layout);

                Font labelFont = UIManager.getFont("Button.font").deriveFont(Font.BOLD, 13.0f);
                Integer fieldColumns = new Integer(15);

                ArrayList<Triplet<String, String, Field<?>>> fields = new ArrayList<Triplet<String, String, Field<?>>>();
                fields.add(new Triplet<String, String, Field<?>>("Title:", "Title...", new AdModel.Title()));
                fields.add(new Triplet<String, String, Field<?>>("Price:", "Price...", new AdModel.Price()));
                fields.add(new Triplet<String, String, Field<?>>("Expiry:", "Expiry...", new AdModel.Expiry()));
                fields.add(new Triplet<String, String, Field<?>>("Tags:", "tag, tag, tag...", new AdModel.Tags()));

                Pair<JLabel, JTextField> labelNfield;

                SpringLayoutStrings l = new SpringLayoutStrings();

                int rows = 0;
                Component pivot1 = panel;
                Component pivot2 = panel;
                for (Triplet<String, String, Field<?>> f: fields) {
                        labelNfield = this._newTaggedTextField(
                                f.getValue0(), f.getValue1(), f.getValue2(),
                                fieldColumns, labelFont
                        );

                        JLabel label     = labelNfield.getValue0();
                        JTextField field = labelNfield.getValue1();

                        panel.add(label);
                        panel.add(field);

                        //SpringUtils.makeGrid(panel, rows, 2, 50, 20, 50, 20);
                        if (rows == 0) {
                                layout.putConstraint(l.NORTH, label, 10, l.NORTH, panel);
                                layout.putConstraint(l.WEST, label, 50, l.WEST, panel);
                                layout.putConstraint(l.WEST, field, 50, l.EAST, label);
                                layout.putConstraint(l.VCENTER, field, 0, l.VCENTER, label);
                        } else {
                                layout.putConstraint(l.NORTH, label, 10, l.SOUTH, pivot1);
                                layout.putConstraint(l.WEST, label, 0, l.WEST, pivot1);
                                layout.putConstraint(l.WEST, field, 0, l.WEST, pivot2);
                                layout.putConstraint(l.VCENTER, field, 0, l.VCENTER, label);
                        }

                        pivot1 = label;
                        pivot2 = field;

                        this._components.put(f.getValue2().getClass(), labelNfield.getValue1());

                        ++rows;
                }

                return panel;
        }

        protected JPanel _newBottomPanel()
        {
                final JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

                JButton cancelButton = HudWidgetFactory.createHudButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                                NewAdView.this._dialog.dispose();
                        }
                });

                JButton createButton = HudWidgetFactory.createHudButton("Create");
                createButton.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                                NewAdView self = NewAdView.this;

                                HashMap<Class<?>, Object> c = self._components;

                                AdModel model = new AdModel()
                                        .set(new AdModel.Uid(self._config.get(new UserIdProp())))
                                        .set(new AdModel.Type(  (ButtonGroup) c.get(AdModel.Type.class)))
                                        .set(new AdModel.Title( (JTextField) c.get(AdModel.Title.class)))
                                        .set(new AdModel.Price( (JTextField) c.get(AdModel.Price.class)))
                                        .set(new AdModel.Expiry((JTextField) c.get(AdModel.Expiry.class)))
                                        .set(new AdModel.Tags(  (JTextField) c.get(AdModel.Tags.class)))
                                ;

                                self._adService.save(model);

                                self._addAdToView(model);

                                self._dialog.dispose();
                        }
                });

                panel.add(cancelButton);
                panel.add(createButton);

                return panel;
        }

        protected Pair<JLabel, JTextField> _newTaggedTextField(
                String labelText,
                String fieldPlaceHolder,
                Field<?> fieldProp,
                Integer fieldColumns,
                Font font
        ) {
                JLabel label = HudWidgetFactory.createHudLabel(labelText);
                label.setFont(font);

                final JTextField field = new JTextField(fieldPlaceHolder, fieldColumns);
                field.setActionCommand(fieldProp.key());
                label.setLabelFor(field);
                TextFieldCleaner.attachTo(field);

                return new Pair<JLabel, JTextField>(label, field);
        }

        protected NewAdView _addAdToView(AdModel model)
        {
                this._view.refreshList();

                return this;
        }

        @Override
        public NewAdView view()
        {
                this._dialog.setVisible(true);

                return this;
        }
}
