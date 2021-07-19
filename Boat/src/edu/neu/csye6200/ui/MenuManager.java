package edu.neu.csye6200.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuManager implements ActionListener {
    private JMenuBar menuBar;
    private BaseUI application;
    private HashMap<String, AbstractButton> buttonMap = new HashMap<String, AbstractButton>();

    /**
     * Constructor
     * The menu manager ID is auto generated
     * @param application the owning parent UI Application
     */
    public MenuManager(BaseUI application) {
        this.application = application;
        init();
    }

    /*
     * Initialize the menu manager
     */
    private void init() {
        getMenuBar();
    }

    /*
     * Default menu items routines
     */
    public void createDefaultActions() {
        JMenu fileMenu = addMenu(menuBar,"core.file", "File", 'F', "File Menu Actions");
        fileMenu.addSeparator();
        createMenuItem(fileMenu, "Exit", 'x', "Exit the application", new ExitAction(application));

        JMenu helpMenu = addMenu(menuBar, "core.help", "Help", 'H', "Help Menu Actions");
        createMenuItem(helpMenu, "About...", '*', "About...", new AboutAction(application));
    }

    /**
     * Get the top menu bar
     * @return the JMenuBar instance
     */
    public JMenuBar getMenuBar() {
        if (menuBar == null)
            menuBar = new JMenuBar();

        return menuBar;
    }

    /**
     * Creates a named menu and set text values based on resource values
     * @return the generated JMenu instance
     */
    private JMenu addMenu(JMenuBar mBar, String menuPath, String menuName, char menuMnemonic, String desc) {
        return( createMenu(mBar, menuPath, menuName, menuMnemonic, desc, -1));
    }

    /**
     * Creates a named menu and set text values based on resource values
     * @return the generated JMenu instance
     */
    public JMenu createMenu(JMenuBar mBar, String menuPath, String menuName, char menuMnemonic, String desc, int pos) {
        //System.out.println("MenuManager:: Adding menu bar - " + menuPath + "/" + menuName);

        String fullMenuPath = menuPath;
        if (menuPath.length() == 0)
            fullMenuPath = "core." + menuName.toLowerCase();

        AbstractButton bmenu = buttonMap.get(fullMenuPath);
        if (bmenu != null)
            return ((JMenu) bmenu);

        JMenu menu = new JMenu(menuName);

        if (menuMnemonic != ' ')
            menu.setMnemonic(menuMnemonic);

        menu.getAccessibleContext().setAccessibleDescription(desc);

        // Add this menu to a Hashtable
        buttonMap.put(fullMenuPath, menu);

        if (pos < 0)
            menu = menuBar.add(menu);
        else
            menu = (JMenu) menuBar.add(menu,pos);

        return menu;
    }

    private JMenuItem mi = null;

    /**
     * Creates a generic menu item
     * @return the generated JMenuItem instance
     */
    public JMenuItem createMenuItem(final JMenu menu, final String label, final int mnemonic,
                                    final String accessibleDescription,final ActionListener action) {
        //System.out.println("MenuManager:: Adding menu item - " + label);

        String buttonPath = menu.getName() + "." + label;
        buttonMap.put(buttonPath, menu);

        mi = menu.add(new JMenuItem(label));

        if (mnemonic != ' ') {
            mi.setMnemonic(mnemonic);
        }

        mi.getAccessibleContext().setAccessibleDescription(
                accessibleDescription);
        mi.setToolTipText(accessibleDescription);
        mi.addActionListener(action);
        if (action == null) {
            mi.setEnabled(false);
        }

        return mi;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        System.out.println("MenuManager:: Received an Action: " + arg0.getActionCommand() +
                " param " + arg0.paramString());
        application.actionPerformed(arg0);
    }

    /**
     * A default Exit action for the MenuManager
     */
    class ExitAction extends AbstractAction {

        private static final long serialVersionUID = -9197382694558803756L;
        private BaseUI application;

        protected ExitAction(BaseUI application) {
            super("ExitAction");
            this.application = application;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            application.exit();
        }
    }

    /**
     * A Default About action for the MenuManager
     * @author MMUNSON
     *
     */
    class AboutAction extends AbstractAction {

        private static final long serialVersionUID = -9197382694558803756L;
        private BaseUI application;

        protected AboutAction(BaseUI application) {
            super("AboutAction");
            this.application = application;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            application.showHelp();
        }
    }
}
