package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;
import com.irishsportanalyser.entities.Admin;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class AddAdministrator
{
    private DialogBox dialog;
    private Label statusLabel;
    private Button enterButton;
    private Button closeButton;
    private TextBox userNameTextBox;
    private PasswordTextBox passwordTextBox;

    public AddAdministrator()
    {
        addAdminDialogBox();

        this.closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                AddAdministrator.this.dialog.hide();
            }
        });
        this.enterButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event) {
                AddAdministrator.this.enterButton.setEnabled(false);
                try
                {
                    if (VariousFunctions.isNull(AddAdministrator.this.userNameTextBox.getText()))
                        throw new Exception("Name is null or Invalid!");
                    if (AddAdministrator.this.userNameTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    if (VariousFunctions.isNull(AddAdministrator.this.passwordTextBox.getText()))
                        throw new Exception("Password is null or Invalid!");
                    if (!AddAdministrator.this.passwordTextBox.getText().equals(AddAdministrator.this.userNameTextBox.getText())) {
                        throw new Exception("The two passwords are not identical!");
                    }

                    boolean windowsBox = Window.confirm("Are you sure?");

                    if (windowsBox) {
                        Admin admin = new Admin(VariousFunctions.escapeHtml(AddAdministrator.this.userNameTextBox.getText()),
                                VariousFunctions.escapeHtml(AddAdministrator.this.passwordTextBox.getText()));

                        AddAdministrator.this.statusLabel.setText("Please Wait. Initialising Query........");
                        AddAdministrator.this.addAdmin(admin);
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Add Administrator", e.getMessage());
                } finally {
                    AddAdministrator.this.enterButton.setEnabled(true);
                    AddAdministrator.this.statusLabel.setText("");
                }
            }
        });
    }

    private void addAdminDialogBox() {
        this.dialog = new DialogBox();
        this.dialog.setText("Add New Administrator");
        this.dialog.setAnimationEnabled(true);

        AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("297px", "214px");

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
        absolutePanel.add(this.enterButton, 204, 150);
        this.enterButton.setSize("83px", "30px");

        this.closeButton = new Button("Cancel");
        absolutePanel.add(this.closeButton, 109, 150);
        this.closeButton.setSize("83px", "30px");

        Label errorLabel = new Label("");
        errorLabel.setStyleName("errorLabel");
        absolutePanel.add(errorLabel, 10, 126);
        errorLabel.setSize("277px", "18px");

        this.statusLabel = new Label("");
        absolutePanel.add(this.statusLabel, 10, 186);
        this.statusLabel.setSize("277px", "18px");

        this.dialog.setWidget(absolutePanel);

        PasswordTextBox passwordTextBoxTwo = new PasswordTextBox();
        absolutePanel.add(passwordTextBoxTwo, 109, 80);
        passwordTextBoxTwo.setSize("159px", "18px");

        Label lblConfirm = new Label("Confirm:");
        lblConfirm.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblConfirm, 10, 92);
        lblConfirm.setSize("83px", "18px");
        this.dialog.center();
    }

    private void addAdmin(Admin admin) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                try {
                    AddAdministrator.this.enterButton.setEnabled(true);
                    AddAdministrator.this.statusLabel.setText("");
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Add Administrator", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Add Administrator", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Add Administrator", e.getMessage());
                }
            }

            public void onSuccess(String result) { AddAdministrator.this.dialog.hide();
                VariousFunctions.informationDialogBox("Add Administrator", result);
            }
        };
        IrishSportAnalyserService.addAdmin(admin,callback);
    }
}
