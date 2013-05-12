package com.irishsportanalyser.forms;
import com.irishsportanalyser.shared.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;


/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:34
 * To change this template use File | Settings | File Templates.
 */
public class StandardLayoutForm {
    private static MenuItem addAthleteMenuItem;
    private final MenuItem homeMenuItem;
    private static MenuItem addClubMenuItem;
    private static MenuItem addCompetitionMenuItem;
    private static MenuItem enterResultsMenuItem;
    private static MenuItem addAdminMenuItem;
    private static MenuBar mainMenuBar;
    private static boolean login;
    private RootPanel mainPanel;

    public StandardLayoutForm(RootPanel mainRootPanel, String weight)
    {
        this.mainPanel = mainRootPanel;
        this.mainPanel = RootPanel.get();
        Image googleCodeImage = new Image("img/P3.png");
        this.mainPanel.add(googleCodeImage, 0, 0);
        googleCodeImage.setSize("526px", "115px");

        mainMenuBar = new MenuBar(false);
        this.mainPanel.add(mainMenuBar, 0, 115);
        mainMenuBar.setSize(weight, "30px");
        MenuBar menuBar = new MenuBar(true);

        MenuItem loginSubMEnuItem = new MenuItem("New menu", false, menuBar);
        loginSubMEnuItem.setHTML("Login");

        MenuItem adminMenuItem = new MenuItem("Administrator", false, new Command() {
            public void execute() {
                new Login(StandardLayoutForm.this.mainPanel);
            }
        });
        menuBar.addItem(adminMenuItem);

        addAdminMenuItem = new MenuItem("Add Administrator", false, new Command() {
            public void execute() {
                new AddAdministrator();
            }
        });
        menuBar.addItem(addAdminMenuItem);
        mainMenuBar.addItem(loginSubMEnuItem);

        this.homeMenuItem = new MenuItem("Home", false,(Command)null);
        mainMenuBar.addItem(this.homeMenuItem);
        MenuBar editSubMenuBar = new MenuBar(true);

        MenuItem editSubMenuItem = new MenuItem("Edit", false, editSubMenuBar);

        addAthleteMenuItem = new MenuItem("Add Athlete", false, new Command() {
            public void execute() {
                new AddAthlete();
            }
        });
        editSubMenuBar.addItem(addAthleteMenuItem);

        addClubMenuItem = new MenuItem("Add Club", false, new Command() {
            public void execute() {
                new AddClub();
            }
        });
        editSubMenuBar.addItem(addClubMenuItem);

        addCompetitionMenuItem = new MenuItem("Add Competition", false, new Command() {
            public void execute() {
                new AddCompetition();
            }
        });
        editSubMenuBar.addItem(addCompetitionMenuItem);

        MenuItemSeparator separator = new MenuItemSeparator();
        editSubMenuBar.addSeparator(separator);

        enterResultsMenuItem = new MenuItem("Enter Results", false, (Command)null);
        editSubMenuBar.addItem(enterResultsMenuItem);
        mainMenuBar.addItem(editSubMenuItem);

        if (login) return;
        addAthleteMenuItem.setEnabled(false);
        addClubMenuItem.setEnabled(false);
        addCompetitionMenuItem.setEnabled(false);
        enterResultsMenuItem.setEnabled(false);
        addAdminMenuItem.setEnabled(false);
    }

    public MenuItem enterResultsMenuItem()
    {
        return enterResultsMenuItem;
    }

    public MenuItem homeMenuItem()
    {
        return this.homeMenuItem;
    }

    public static void setLogin()
    {
        login = true;
    }

    public boolean isLogin()
    {
        return login;
    }

    public static void doLogin()
    {
        if (!login) return;
        addAthleteMenuItem.setEnabled(true);
        addClubMenuItem.setEnabled(true);
        addCompetitionMenuItem.setEnabled(true);
        enterResultsMenuItem.setEnabled(true);
        addAdminMenuItem.setEnabled(true);
    }
}
