package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */

import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;
import com.irishsportanalyser.entities.Competition;
import com.irishsportanalyser.enums.Region;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;

public class AddCompetition
{
    private DialogBox dialog;
    private Label statusLabel;
    private Button enterButton;
    private TextBox competitionVenueTextBox;
    private DatePicker datePicker;
    private RadioButton indoorRadioButton;
    private ListBox regionListBox;
    private TextBox competitionNameTextBox;
    private Button closeButton;

    public AddCompetition()
    {
        addCompetitionDialogBox();

        this.enterButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event) {
                AddCompetition.this.enterButton.setEnabled(false);
                try
                {
                    if (VariousFunctions.isNull(AddCompetition.this.competitionNameTextBox.getText()))
                        throw new Exception("Competition Name is null or Invalid!");
                    if (AddCompetition.this.competitionNameTextBox.getText().length() > 35)
                        throw new Exception("Competition Name is too long");
                    if (VariousFunctions.isNull(AddCompetition.this.competitionVenueTextBox.getText()))
                        throw new Exception("Competition Venue is null or Invalid!");
                    if (AddCompetition.this.competitionVenueTextBox.getText().length() > 20)
                        throw new Exception("Competition Venue is too long");
                    if (AddCompetition.this.datePicker.getValue() == null) {
                        throw new Exception("Please select a date!");
                    }
                    String competitionName = VariousFunctions.escapeHtml(AddCompetition.this.competitionNameTextBox.getText());
                    String comeptitionVenue = VariousFunctions.escapeHtml(AddCompetition.this.competitionVenueTextBox.getText());
                    String dateString = DateTimeFormat.getFormat("EEE, MMM d, yyyy").format(AddCompetition.this.datePicker.getValue());
                    String season = DateTimeFormat.getFormat("yyyy").format(AddCompetition.this.datePicker.getValue());
                    String competitionType = AddCompetition.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    Region region = VariousFunctions.stringToRegion(AddCompetition.this.regionListBox.getItemText(AddCompetition.this.regionListBox.getSelectedIndex()));

                    boolean windowsBox = Window.confirm("Competition Type: " + competitionType + "\nRegion: " + region + "\n" +
                            "Competition Name: " + competitionName + "\nCompetition Venue: " + comeptitionVenue + "\nCompetition Date: " + dateString + "\nSeason: " + season);

                    if (windowsBox) {
                        Competition competition = new Competition(competitionType, region, competitionName, comeptitionVenue, dateString, season);

                        AddCompetition.this.statusLabel.setText("Initialising Query........");
                        AddCompetition.this.addCompetition(competition);
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Add Competition", e.getMessage());
                } finally {
                    AddCompetition.this.enterButton.setEnabled(true);
                    AddCompetition.this.statusLabel.setText("");
                }
            }
        });
        this.closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                AddCompetition.this.dialog.hide();
            }
        });
    }

    private void addCompetitionDialogBox() {
        this.dialog = new DialogBox();
        this.dialog.setText("Add New Competition");
        this.dialog.setAnimationEnabled(true);

        AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("283px", "439px");

        RadioButton outdoorRadioButton = new RadioButton("competitionLeaderboard", "Outdoor");
        absolutePanel.add(outdoorRadioButton, 158, 22);
        outdoorRadioButton.setSize("81px", "18px");

        this.indoorRadioButton = new RadioButton("competitionLeaderboard", "Indoor");
        this.indoorRadioButton.setValue(Boolean.valueOf(true));
        absolutePanel.add(this.indoorRadioButton, 82, 22);
        this.indoorRadioButton.setSize("64", "18");

        Label label = new Label("Select a Competition Type:");
        absolutePanel.add(label, 72, 0);
        label.setSize("167px", "20px");

        Label lblUserName = new Label("Name:");
        lblUserName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblUserName, 10, 76);
        lblUserName.setSize("56px", "18px");

        this.competitionNameTextBox = new TextBox();
        absolutePanel.add(this.competitionNameTextBox, 72, 76);
        this.competitionNameTextBox.setSize("181px", "18px");

        this.regionListBox = new ListBox();
        this.regionListBox.addItem("All_Ireland");
        this.regionListBox.addItem("Leinster");
        this.regionListBox.addItem("Munster");
        this.regionListBox.addItem("Connacht");
        this.regionListBox.addItem("Ulster");
        this.regionListBox.addItem("Non_Irish");
        absolutePanel.add(this.regionListBox, 72, 48);
        this.regionListBox.setSize("106px", "22px");

        this.enterButton = new Button("Enter");
        absolutePanel.add(this.enterButton, 182, 375);
        this.enterButton.setSize("81px", "30px");

        this.closeButton = new Button("Cancel");
        this.closeButton.setText("Cancel");
        absolutePanel.add(this.closeButton, 72, 375);
        this.closeButton.setSize("81px", "30px");

        Label errorLabel = new Label("");
        errorLabel.setStyleName("errorLabel");
        absolutePanel.add(errorLabel, 10, 351);
        errorLabel.setSize("268px", "18px");

        this.statusLabel = new Label("");
        absolutePanel.add(this.statusLabel, 10, 411);
        this.statusLabel.setSize("268px", "18px");

        this.datePicker = new DatePicker();
        absolutePanel.add(this.datePicker, 72, 154);

        Label lblDate = new Label("Date:");
        lblDate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblDate, 10, 163);
        lblDate.setSize("56px", "18px");

        Label lblRegion = new Label("Region:");
        lblRegion.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblRegion, 10, 48);
        lblRegion.setSize("56px", "18px");

        this.competitionVenueTextBox = new TextBox();
        absolutePanel.add(this.competitionVenueTextBox, 72, 112);
        this.competitionVenueTextBox.setSize("181px", "18px");

        Label lblVenue = new Label("Venue:");
        lblVenue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(lblVenue, 10, 112);
        lblVenue.setSize("56px", "18px");

        this.dialog.setWidget(absolutePanel);
        this.dialog.center();
    }

    private void addCompetition(Competition competition) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                try {
                    AddCompetition.this.enterButton.setEnabled(true);
                    AddCompetition.this.statusLabel.setText("");
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Add Competition", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Add Competition", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Add Competition", e.getMessage());
                }
            }

            public void onSuccess(String result) { if (result == null) {
                AddCompetition.this.statusLabel.setText("");
                VariousFunctions.errorDialogBox("Add Competition", "Unknown Error");
            }
            else {
                AddCompetition.this.dialog.hide();
                VariousFunctions.informationDialogBox("Add Competition", result);
            }
            }
        };
        IrishSportAnalyserService.addCompetition(competition,callback);
    }
}