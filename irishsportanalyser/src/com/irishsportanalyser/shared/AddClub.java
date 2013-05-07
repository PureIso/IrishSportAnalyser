package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
import com.irishsportanalyser.entities.Club;
import com.irishsportanalyser.enums.Region;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;

public class AddClub
{
    private DialogBox dialog;
    private Label statusLabel;
    private Button enterButton;
    private ListBox regionListBox;
    private Button closeButton;
    private TextBox clubNameTextBox;

    public AddClub()
    {
        addClubDialogBox();

        this.enterButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event) {
                AddClub.this.enterButton.setEnabled(false);
                try {
                    if (VariousFunctions.isNull(AddClub.this.clubNameTextBox.getText()))
                        throw new Exception("Name is null or Invalid!");
                    if (AddClub.this.clubNameTextBox.getValue().length() > 35) {
                        throw new Exception("Name is too long");
                    }
                    Region region = VariousFunctions.stringToRegion(AddClub.this.regionListBox.getItemText(AddClub.this.regionListBox.getSelectedIndex()));
                    String name = AddClub.this.clubNameTextBox.getText();

                    boolean windowsBox = Window.confirm(
                            "Region: " + region + "\n" +
                                    "Club Name: " + name);
                    if (windowsBox) {
                        Club club = new Club(region, VariousFunctions.escapeHtml(name));
                        AddClub.this.statusLabel.setText("Please Wait. Initialising Query........");
                        AddClub.this.addClub(club);
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Add Club", e.getMessage());
                } finally {
                    AddClub.this.statusLabel.setText("");
                    AddClub.this.enterButton.setEnabled(true);
                }
            }
        });
        this.closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                AddClub.this.dialog.hide();
            }
        });
    }

    private void addClubDialogBox() {
        this.dialog = new DialogBox();
        this.dialog.setText("Add New Club");
        this.dialog.setAnimationEnabled(true);

        AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("297px", "170px");

        Label lblRegion_1 = new Label("Region:");
        lblRegion_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblRegion_1, 20, 14);
        lblRegion_1.setSize("83px", "18px");

        Label lblUserName = new Label("Club Name: ");
        lblUserName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblUserName, 20, 56);
        lblUserName.setSize("83px", "18px");

        this.clubNameTextBox = new TextBox();
        absolutePanel.add(this.clubNameTextBox, 109, 44);
        this.clubNameTextBox.setSize("159px", "18px");

        this.regionListBox = new ListBox();
        this.regionListBox.addItem(Region.Leinster.toString());
        this.regionListBox.addItem(Region.Munster.toString());
        this.regionListBox.addItem(Region.Connacht.toString());
        this.regionListBox.addItem(Region.Ulster.toString());
        absolutePanel.add(this.regionListBox, 109, 10);
        this.regionListBox.setSize("106px", "22px");

        this.enterButton = new Button("Enter");
        absolutePanel.add(this.enterButton, 197, 104);
        this.enterButton.setSize("81px", "30px");

        this.closeButton = new Button("Cancel");
        absolutePanel.add(this.closeButton, 110, 104);
        this.closeButton.setSize("81px", "30px");

        Label errorLabel = new Label("");
        errorLabel.setStyleName("errorLabel");
        absolutePanel.add(errorLabel, 10, 80);
        errorLabel.setSize("268px", "18px");

        this.statusLabel = new Label("");
        absolutePanel.add(this.statusLabel, 19, 140);
        this.statusLabel.setSize("268px", "18px");

        this.dialog.setWidget(absolutePanel);
        this.dialog.center();
    }

    private void addClub(Club club) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                try {
                    AddClub.this.enterButton.setEnabled(true);
                    AddClub.this.statusLabel.setText("");
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Add Club", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Add Club", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Add Club", e.getMessage());
                }
            }

            public void onSuccess(String result) { if (result == null) {
                AddClub.this.statusLabel.setText("");
                AddClub.this.enterButton.setEnabled(true);
                VariousFunctions.errorDialogBox("Add Club", "Unknown Error");
            }
            else {
                VariousFunctions.informationDialogBox("Add Club", result);
            }
            }
        };
        IrishSportAnalyserService.addClub(club,callback);
    }
}