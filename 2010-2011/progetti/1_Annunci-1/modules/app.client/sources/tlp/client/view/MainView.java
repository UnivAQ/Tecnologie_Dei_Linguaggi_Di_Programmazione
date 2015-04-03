package tlp.client.view;

import com.explodingpixels.macwidgets.SourceList;
import com.explodingpixels.macwidgets.SourceListCategory;
import com.explodingpixels.macwidgets.SourceListClickListener;
import com.explodingpixels.macwidgets.SourceListClickListener.Button;
import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import com.explodingpixels.macwidgets.SourceListSelectionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.client.config.SessionManager;
import tlp.client.config.UserIdProp;
import tlp.client.config.UserUsernameProp;
import tlp.client.helper.SpringLayoutStrings;
import tlp.client.model.AdModel;
import tlp.client.model.UserModel;
import tlp.client.service.AdService;
import tlp.client.service.Backend;
import tlp.client.service.BackendType;
import tlp.client.util.TextUtil;
import tlp.client.view.frame.MainFrame;
import tlp.common.core.ContextFactory;

@Service
public class MainView implements View
{
        private Logger _logger = LoggerFactory.getLogger(MainView.class);

        private MainFrame    _frame;
        private JPanel       _barBox;
        private JLayeredPane _workBox;

        private AdService      _adService;
        private SessionManager _session;

        private String _addIcon  = "Resources/add-icon.png";
        private String _nextIcon = "Resources/next-icon.png";

        private Dimension _nextIconSize = new Dimension(16, 16);

        private JButton _addButton;
        private JLabel  _userLabel;
        private SourceList                      _adsList;
        private HashMap<SourceListItem, String> _itemsPool;
        private String                          _selectedAdId;

        @Autowired
        public MainView(
                MainFrame frame,
                @Backend(BackendType.MongoDB) AdService adService,
                SessionManager session)
        {
                frame.workBox(new JLayeredPane());

                this._frame   = frame;
                this._barBox  =(JPanel) frame.barBox();
                this._workBox =(JLayeredPane) frame.workBox();

                this._adService = adService;
                this._session   = session;

                this._init();
        }

        protected MainView _init()
        {
                this._buildBarBox()
                    ._buildWorkBox();

                return this;
        }

        protected JButton _newAddButton()
        {
                ImageIcon icon = new ImageIcon(MainView.class.getResource(this._addIcon));

                JButton button = new JButton(icon);
                button.setBorderPainted(false);
                button.addActionListener(this._newAddButtonActionListener(button));

                return button;
        }

        protected ActionListener _newAddButtonActionListener(final JButton triggerButton)
        {
                return new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                NewAdView newAdView = ContextFactory.get(NewAdView.class);
                                newAdView.view();
                        }
                };
        }

        protected JLabel _newUsernameLabel()
        {
                JLabel label = new JLabel();
                label.setForeground(Color.GRAY);

                return label;
        }

        protected SourceList _newSourceList()
        {
                SourceList list = new SourceList(new SourceListModel());

                list.getComponent().setBounds(0, 0,
                        this.getWorkBoxWidth(),
                        this.getWorkBoxHeight()
                );

                list.addSourceListClickListener(this._newSourceListClickListener(this._workBox));
                list.addSourceListSelectionListener(this._newSourceListSelectionListener());

                return list;
        }

        protected SourceListClickListener _newSourceListClickListener(final JLayeredPane ownerPane) {
                return new SourceListClickListener() {
                        @Override
                        public void sourceListCategoryClicked(SourceListCategory category, Button button, int clickCount) {}

                        @Override
                        public void sourceListItemClicked(SourceListItem item, Button button, int clickCount) {
                                if (clickCount != 2) {
                                        return;
                                }

                                MainView self = MainView.this;

                                // This is a workaround to prevent the AdsList view
                                // to appear under the EditAd view.
                                self._workBox.moveToBack(self.getAdsBox());

                                EditAdView editAdView = ContextFactory.get(EditAdView.class);
                                editAdView.view(new AdModel.Id(self._itemsPool.get(item)));
                        }
                };
        }

        protected SourceListSelectionListener _newSourceListSelectionListener()
        {
                return new SourceListSelectionListener() {
                        @Override
                        public void sourceListItemSelected(SourceListItem item)
                        {
                                MainView.this._selectedAdId = MainView.this._itemsPool.get(item);
                        }
                };
        }

        protected ImageIcon _newItemIcon()
        {
                ImageIcon icon = new ImageIcon(NewAdView.class.getResource(this._nextIcon));

                icon = new ImageIcon(icon.getImage().getScaledInstance(
                        this._nextIconSize.width,
                        this._nextIconSize.height,
                        Image.SCALE_SMOOTH
                ));

                return icon;
        }

        protected MainView _buildBarBox()
        {
                JButton addButton = this._newAddButton();
                JLabel userLabel = this._newUsernameLabel();

                SpringLayout layout =(SpringLayout) this._barBox.getLayout();
                SpringLayoutStrings l = new SpringLayoutStrings();
                layout.putConstraint(l.VCENTER, addButton, 0, l.VCENTER, this._barBox);
                layout.putConstraint(l.WEST, addButton, 5, l.WEST, this._barBox);
                layout.putConstraint(l.VCENTER, userLabel, 0, l.VCENTER, this._barBox);
                layout.putConstraint(l.EAST, userLabel, -20, l.EAST, this._barBox);

                this._barBox.setLayout(layout);
                this._barBox.add(addButton);
                this._barBox.add(userLabel);

                this._addButton = addButton;
                this._userLabel = userLabel;

                return this;
        }

        protected MainView _buildWorkBox()
        {
                SourceList adsList = this._newSourceList();
                this._workBox.add(adsList.getComponent(), JLayeredPane.DEFAULT_LAYER);

                this._adsList = adsList;

                return this;
        }

        protected MainView _fillAdsList(SourceList list)
        {
                // Rational:
                // The session object returns a valid id only after
                // the the SignView is disposed and this.view() method is invoked.
                // For this reason this method should not run before MainView.view().
                List<AdModel> models = this._adService.all(new UserModel.Id(
                        this._session.get(new UserIdProp())
                ));

                HashMap<SourceListItem, String> pool = new HashMap<SourceListItem, String>();

                for (AdModel model: models) {
                        this._addToList(model, list, pool);

                }

                if (this._selectedAdId != null) {
                        for (Map.Entry<SourceListItem, String> entry: pool.entrySet()) {
                                if (entry.getValue().equals(this._selectedAdId)) {
                                        list.setSelectedItem(entry.getKey());
                                }
                        }
                }

                this._itemsPool = pool;

                return this;
        }

        protected MainView _addToList(AdModel model, SourceList list, HashMap<SourceListItem, String> pool)
        {
                SourceListCategory category = model.get(new AdModel.Type()).equals(AdModel.TypeType.SALE) ?
                                this._getSaleCategory(list)
                        :       this._getWantedCategory(list)
                ;

                List<AdModel> match = this._adService.match(
                        new AdModel.Type(model.get(new AdModel.Type())),
                        new AdModel.Tags(model.get(new AdModel.Tags()))
                );

                SourceListItem item = new SourceListItem(
                        model.get(new AdModel.Title()),
                        this._newItemIcon()
                );
                item.setCounterValue(match.size());

                // The user must be informed that an ad is expired.
                if (model.get(new AdModel.Expiry()).before(Calendar.getInstance().getTime())) {
                        item.setText("[EXPIRED] " + item.getText());
                }

                list.getModel().addItemToCategory(item, category);
                pool.put(item, model.get(new AdModel.Id()));

                return this;
        }

        protected SourceListCategory _getSaleCategory(SourceList list)
        {
                String text = AdModel.TypeType.SALE.toString().toLowerCase();

                SourceListCategory category = null;

                for (SourceListCategory c: list.getModel().getCategories()) {
                        if (c.getText().toLowerCase().equals(text)) {
                                category = c;
                        }
                }

                if (category == null) {
                        // The Sale category comes always before the Wanted one.
                        int index = 0;

                        category = new SourceListCategory(TextUtil.upperCaseFirst(text));

                        list.getModel().addCategory(category, index);
                }

                return category;
        }

        protected SourceListCategory _getWantedCategory(SourceList list)
        {
                String text = AdModel.TypeType.WANTED.toString().toLowerCase();

                SourceListCategory category = null, saleCategory = null;

                for (SourceListCategory c: list.getModel().getCategories()) {
                        if (c.getText().toLowerCase().equals(text)) {
                                category = c;
                        } else {
                                saleCategory = c;
                        }
                }

                if (category == null) {
                        // The Wanted category comes always after the Sale one,
                        // but first if the Sale category not yet exists.
                        int index = (saleCategory == null) ?
                                0 : 1
                        ;

                        category = new SourceListCategory(TextUtil.upperCaseFirst(text));

                        list.getModel().addCategory(category, index);
                }

                return category;
        }

        protected MainView _runRefreshThread()
        {
                ExecutorService executor = Executors.newSingleThreadExecutor();

                final Runnable refresher = new Runnable() {
                        @Override
                        public void run() {
                                MainView.this.refreshList();
                        }
                };

                executor.execute(new Runnable() {
                        @Override
                        public void run() {
                                while (true) {
                                        try {
                                                Thread.sleep(10000);
                                                SwingUtilities.invokeLater(refresher);
                                        } catch (InterruptedException e) {
                                        }
                                }
                        }
                });

                return this;
        }

        @Override
        public MainView view()
        {
                // Tational:
                // This object is constructed before the SignView can set the session.
                // So, we need to postpone the label customization.
                this._userLabel.setText(this._session.get(new UserUsernameProp()));

                // See this._fillAdsList() for more informations on this call.
                this._fillAdsList(this._adsList);

                this._frame.view();
                this._runRefreshThread();

                return this;
        }

        public MainView refreshList()
        {
                for (SourceListCategory c: this._adsList.getModel().getCategories()) {
                        for (SourceListItem i: c.getItems()) {
                                //this._adsList.getModel().removeItemFromCategory(i, c);
                                this._itemsPool.remove(i);
                        }

                        //this._adsList.getModel().removeCategory(c);
                }

                SourceList oldList = this._adsList;
                this._buildWorkBox();
                this._fillAdsList(this._adsList);
                this._workBox.remove(oldList.getComponent());

                return this;
        }

        public MainView toggleAddButton()
        {
                this._addButton.setEnabled(! this._addButton.isEnabled());
                this._addButton.setVisible(! this._addButton.isVisible());

                return this;
        }

        public JLayeredPane getWorkBox()
        {
                return this._workBox;
        }

        public JComponent getAdsBox()
        {
                return this._adsList.getComponent();
        }

        public int getWorkBoxWidth()
        {
                return this._frame.getPreferredSize().width;
        }

        public int getWorkBoxHeight()
        {
                return this._frame.getPreferredSize().height - this._frame.barBox().getHeight();
        }
}
