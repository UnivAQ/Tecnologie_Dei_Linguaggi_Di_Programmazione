package tlp.client.view;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.explodingpixels.macwidgets.MacWidgetFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tlp.client.config.ConfigManager;
import tlp.client.helper.PaintedPanel;
import tlp.client.helper.SpringLayoutStrings;
import tlp.client.model.AdModel;
import tlp.client.service.AdService;
import tlp.client.service.Backend;
import tlp.client.service.BackendType;
import tlp.client.view.frame.MainFrame;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditAdView extends PaintedPanel
{
        private Logger _logger = LoggerFactory.getLogger(EditAdView.class);

        private MainFrame _frame;
        private MainView  _view;

        private AdService     _adService;
        private ConfigManager _config;

        static
        private String _editImage  = "Resources/edit-image.png";
        private String _backIcon   = "Resources/back-icon.png";
        private String _removeIcon = "Resources/remove-icon.png";

        private int _textFieldColumns = 8;

        // This attribute will be defined on this.view() method call.
        // Any method can use it safely.
        private AdModel _adModel;

        private JButton _backButton;
        private JTable  _matchesTable;

        private HashMap<Class<?>, Object> _components = new HashMap<Class<?>, Object>();

        @Autowired
        public EditAdView(
                MainFrame frame,
                MainView view,
                @Backend(BackendType.MongoDB) AdService adService,
                ConfigManager config
        ) {
                super(new ImageIcon(EditAdView.class.getResource(EditAdView._editImage)).getImage());

                this._frame  = frame;
                this._view   = view;

                this._adService = adService;
                this._config    = config;

                this._init();
        }

        protected EditAdView _init()
        {
                this.setLayout(new BorderLayout());
                this.setBounds(0, 0, this._view.getWorkBoxWidth(), this._view.getWorkBoxHeight());

                this._buildBackButton()
                    ._buildEditPanel()
                    ._buildTablePanel();

                this.setVisible(false);
                this._getParentPane().add(this, JLayeredPane.DEFAULT_LAYER);

                return this;
        }

        protected JButton _newBackButton()
        {
                ImageIcon icon = new ImageIcon(SignView.class.getResource(this._backIcon));
                icon = new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));

                JButton button = new JButton(icon);
                button.setBorderPainted(false);
                button.addActionListener(this._newBackButtonActionListener());

                return button;
        }

        protected ActionListener _newBackButtonActionListener()
        {
                return new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                EditAdView.this._comeBack();
                        }
                };
        }

        protected JButton _newDeleteButton()
        {
                ImageIcon icon = new ImageIcon(SignView.class.getResource(this._removeIcon));
                icon = new ImageIcon(icon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH));

                JButton button = new JButton(icon);
                button.setBorderPainted(false);
                button.addActionListener(this._newDeleteButtonActionListener());

                return button;
        }

        protected ActionListener _newDeleteButtonActionListener()
        {
                return new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                EditAdView self = EditAdView.this;

                                self._adService.remove(new AdModel.Id(self._adModel.get(new AdModel.Id())));
                                self._comeBack();
                                self._view.refreshList();
                        }
                };
        }

        protected JButton _newSaveButton()
        {
                JButton button = new JButton("Save");
                button.addActionListener(this._newSaveButtonActionListener());

                return button;
        }

        protected ActionListener _newSaveButtonActionListener()
        {
                return new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                EditAdView self = EditAdView.this;

                                HashMap<Class<?>, Object> c = self._components;

                                self._adModel.set(new AdModel.Title( (JTextField) c.get(AdModel.Title.class)))
                                             .set(new AdModel.Price( (JTextField) c.get(AdModel.Price.class)))
                                             .set(new AdModel.Expiry((JTextField) c.get(AdModel.Expiry.class)))
                                             .set(new AdModel.Tags(  (JTextField) c.get(AdModel.Tags.class)))
                                ;

                                self._adService.save(self._adModel);
                                self._comeBack();
                                self._view.refreshList();
                        }
                };
        }

        protected JPanel _newEditPanel()
        {
                SpringLayout layout = new SpringLayout();
                SpringLayoutStrings l = new SpringLayoutStrings();

                JPanel panel = new JPanel();
                panel.setLayout(layout);
                panel.setPreferredSize(new Dimension(this._view.getWorkBoxWidth(), 80));
                panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK,Color.GRAY));

                final JTextField titleField = new JTextField(this._textFieldColumns);
                final JTextField priceField = new JTextField(this._textFieldColumns);
                final JTextField expiryField = new JTextField(this._textFieldColumns);
                final JTextField tagsField = new JTextField(this._textFieldColumns);
                titleField.setActionCommand(new AdModel.Title().key());
                priceField.setActionCommand(new AdModel.Price().key());
                expiryField.setActionCommand(new AdModel.Expiry().key());
                tagsField.setActionCommand(new AdModel.Tags().key());
                this._components.put(AdModel.Title.class, titleField);
                this._components.put(AdModel.Price.class, priceField);
                this._components.put(AdModel.Expiry.class, expiryField);
                this._components.put(AdModel.Tags.class, tagsField);

                JButton deleteButton = this._newDeleteButton();
                JButton saveButton = this._newSaveButton();

                layout.putConstraint(l.SOUTH, saveButton, 0, l.SOUTH, panel);
                layout.putConstraint(l.HCENTER, saveButton, 0, l.HCENTER, panel);
                layout.putConstraint(l.SOUTH, deleteButton, 5, l.SOUTH, panel);
                layout.putConstraint(l.EAST, deleteButton, 0, l.EAST, panel);

                layout.putConstraint(l.NORTH, titleField, 10, l.NORTH, panel);
                layout.putConstraint(l.WEST, titleField, 20, l.WEST, panel);
                layout.putConstraint(l.WEST, priceField, 10, l.EAST, titleField);
                layout.putConstraint(l.VCENTER, priceField, 0, l.VCENTER, titleField);
                layout.putConstraint(l.WEST, expiryField, 10, l.EAST, priceField);
                layout.putConstraint(l.VCENTER, expiryField, 0, l.VCENTER, priceField);
                layout.putConstraint(l.WEST, tagsField, 10, l.EAST, expiryField);
                layout.putConstraint(l.VCENTER, tagsField, 0, l.VCENTER, expiryField);

                panel.add(saveButton);
                panel.add(deleteButton);

                panel.add(titleField);
                panel.add(priceField);
                panel.add(expiryField);
                panel.add(tagsField);

                return panel;
        }

        protected JTable _newTable()
        {
                String[][] data = {};

                JTable table = MacWidgetFactory.createITunesTable(this._newTableModel(data));
                //table.setRowHeight(40);

                return table;
        }

        protected String[] _newTableColumns()
        {
                return new String[] { "Title", "Price", "Expiry", "Tags" };
        }

        protected TableModel _newTableModel(String [][] data)
        {
                return new DefaultTableModel(data, this._newTableColumns()) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
        }

        protected JScrollPane _wrapTable(JTable table)
        {
                JScrollPane scrollPane = IAppWidgetFactory.createScrollPaneWithButtonsTogether(table);
                scrollPane.setPreferredSize(new Dimension(
                        this._view.getWorkBoxWidth(),
                        this._view.getWorkBoxHeight()
                ));

                return scrollPane;
        }

        protected EditAdView _buildBackButton()
        {
                JComponent box = this._frame.barBox();
                JButton button = this._newBackButton();
                button.setVisible(false);

                SpringLayoutStrings l = new SpringLayoutStrings();
                SpringLayout topBoxLayout =(SpringLayout) box.getLayout();
                topBoxLayout.putConstraint(l.VCENTER, button, 0, l.VCENTER, box);
                topBoxLayout.putConstraint(l.WEST, button, 10, l.WEST, box);

                box.add(button);

                this._backButton = button;

                return this;
        }

        protected EditAdView _buildEditPanel()
        {
                this.add(this._newEditPanel(), BorderLayout.PAGE_START);

                return this;
        }

        protected EditAdView _buildTablePanel()
        {
                JTable table = this._newTable();
                JScrollPane scrollPane = this._wrapTable(table);

                this.setTransparentAdd(false);
                this.add(scrollPane, BorderLayout.CENTER);
                this.setTransparentAdd(true);

                this._matchesTable = table;

                return this;
        }

        protected EditAdView _fillView()
        {
                this._fillEditPanel()
                    ._fillEditTable();

                return this;
        }

        protected EditAdView _fillEditPanel()
        {
                ((JTextField) this._components.get(AdModel.Title.class)).setText(
                        this._adModel.get(new AdModel.Title())
                );

                ((JTextField) this._components.get(AdModel.Price.class)).setText(
                        this._adModel.get(new AdModel.Price())
                );

                ((JTextField) this._components.get(AdModel.Expiry.class)).setText(
                        new AdModel.Expiry(this._adModel.get(new AdModel.Expiry())).valStr()
                );

                ((JTextField) this._components.get(AdModel.Tags.class)).setText(
                        new AdModel.Tags(this._adModel.get(new AdModel.Tags())).valStr()
                );

                return this;
        }

        protected EditAdView _fillEditTable()
        {
                List<AdModel> matches = this._adService.match(
                        new AdModel.Type(this._adModel.get(new AdModel.Type())),
                        new AdModel.Tags(this._adModel.get(new AdModel.Tags()))
                );

                String[][] data = new String[matches.size()][4];

                for (int i = 0, s = matches.size(); i < s; ++i) {
                        AdModel m = matches.get(i);

                        data[i][0] = m.get(new AdModel.Title());
                        data[i][1] = m.get(new AdModel.Price());
                        data[i][2] = new AdModel.Expiry(m.get(new AdModel.Expiry())).valStr();
                        data[i][3] = new AdModel.Tags(m.get(new AdModel.Tags())).valStr();
                }

                this._matchesTable.setModel(this._newTableModel(data));
                // For an unknown reason, the column wide
                // must be set after the injection of the model,
                // otherwise it has no effect.
                this._matchesTable.getColumn("Title").setMinWidth(200);
                this._matchesTable.getColumn("Title").setMaxWidth(200);
                this._matchesTable.getColumn("Price").setMinWidth(100);
                this._matchesTable.getColumn("Price").setMaxWidth(100);
                this._matchesTable.getColumn("Expiry").setMinWidth(100);
                this._matchesTable.getColumn("Expiry").setMaxWidth(100);
                this._matchesTable.getColumn("Tags").setMinWidth(150);
                this._matchesTable.getColumn("Tags").setMaxWidth(150);

                return this;
        }

        protected EditAdView _comeBack()
        {
                this._getParentPane().moveToFront(this._getPreviousPanel());
                this._toggleBackButton();
                this._getParentPane().remove(this);

                return this;
        }

        protected EditAdView _toggleBackButton()
        {
                if (this._backButton.isVisible()) {
                        this._frame.barBox().remove(this._backButton);
                        this._view.toggleAddButton();

                } else {
                        this._view.toggleAddButton();
                        this._backButton.setVisible(true);
                }

                return this;
        }

        protected JLayeredPane _getParentPane()
        {
                return this._view.getWorkBox();
        }

        protected JComponent _getPreviousPanel()
        {
                return this._view.getAdsBox();
        }

        public EditAdView view(AdModel.Id id)
        {
                this._adModel = this._adService.one(id);

                this._toggleBackButton();
                this._fillView();
                this.setVisible(true);
                this._getParentPane().moveToFront(this);

                return this;
        }
}
