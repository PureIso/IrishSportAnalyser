package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.*;
import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;
import com.irishsportanalyser.entities.Admin;
import com.irishsportanalyser.forms.HomeForm;
import com.irishsportanalyser.forms.StandardLayoutForm;

public class Login
{
    private int tries;
    private Label errorLabel;
    private DialogBox dialog;
    private Label statusLabel;
    private Button enterButton;
    private PasswordTextBox passwordTextBox;
    private Button closeButton;
    private TextBox userNameTextBox;
    private final RootPanel rp;

    public Login(RootPanel rootpanel)
    {
        this.rp = rootpanel;
        loginDialogBox();

        this.enterButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Login.this.sendAdmin();
            }
        });
        this.closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Login.this.dialog.hide();
            }
        });
        this.passwordTextBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == '\r')
                    Login.this.sendAdmin();
            }
        });
    }

    private void sendAdmin()
    {
        this.enterButton.setEnabled(false);
        try {
            if (this.tries > 3) { this.dialog.hide();
            } else {
                this.tries += 1;
                if (this.userNameTextBox.getValue().length() > 20)
                    throw new Exception("User Name is too long");
                Admin admin = new Admin(VariousFunctions.escapeHtml(this.userNameTextBox.getText()),
                        VariousFunctions.escapeHtml(this.passwordTextBox.getText()));

                this.statusLabel.setText("Please Wait. Initialising Query........");
                loginAdmin(admin);
            }
        } catch (Exception e) {
            this.enterButton.setEnabled(true);
            this.statusLabel.setText("");
            VariousFunctions.errorDialogBox("Login", e.getMessage());
        }
    }

    private void loginDialogBox() {
        this.tries = 0;
        this.dialog = new DialogBox();
        this.dialog.setText("Administrator Login");
        this.dialog.setAnimationEnabled(true);

        AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("297px", "170px");

        Label lblUserName = new Label("User Name: ");
        lblUserName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblUserName, 10, 22);
        lblUserName.setSize("83px", "18px");

        this.userNameTextBox = new TextBox();
        absolutePanel.add(this.userNameTextBox, 109, 10);
        this.userNameTextBox.setSize("159px", "18px");

        Label lblPassword = new Label("Password:");
        lblPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblPassword, 10, 56);
        lblPassword.setSize("83px", "18px");

        this.passwordTextBox = new PasswordTextBox();
        absolutePanel.add(this.passwordTextBox, 109, 44);
        this.passwordTextBox.setSize("159px", "18px");

        this.enterButton = new Button("Enter");
        absolutePanel.add(this.enterButton, 197, 104);
        this.enterButton.setSize("81px", "30px");

        this.closeButton = new Button("Cancel");
        absolutePanel.add(this.closeButton, 110, 104);
        this.closeButton.setSize("81px", "30px");

        this.errorLabel = new Label("");
        this.errorLabel.setStyleName("errorLabel");
        absolutePanel.add(this.errorLabel, 10, 80);
        this.errorLabel.setSize("268px", "18px");

        this.statusLabel = new Label("");
        absolutePanel.add(this.statusLabel, 19, 140);
        this.statusLabel.setSize("268px", "18px");

        this.dialog.setWidget(absolutePanel);
        this.dialog.center();
    }

    private void loginAdmin(Admin admin) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                try {
                    Login.this.statusLabel.setText("");
                    Login.this.enterButton.setEnabled(true);
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Administrator Login", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Administrator Login", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Administrator Login", e.getMessage());
                }
            }

            public void onSuccess(Boolean result) { if (result.booleanValue()) {
                StandardLayoutForm.setLogin();
                StandardLayoutForm.doLogin();
                Login.this.dialog.hide();
                VariousFunctions.informationDialogBox("Administrator Login", "Login Successful!");
                Login.this.rp.clear();
                new HomeForm();
            }
            else {
                Login.this.enterButton.setEnabled(true);
                Login.this.errorLabel.setText("Username or Passowrd Invalid!");
                Login.this.statusLabel.setText("");
            }
            }
        };
        IrishSportAnalyserService.searchAdmin(admin,callback);
    }
}
