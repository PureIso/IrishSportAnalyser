package com.irishsportanalyser.forms;

import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;
import com.irishsportanalyser.entities.Athlete;
import com.irishsportanalyser.entities.Competition;
import com.irishsportanalyser.entities.Result;
import com.irishsportanalyser.enums.Gender;
import com.irishsportanalyser.enums.Region;
import com.irishsportanalyser.shared.VariousFunctions;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.Arrays;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class EnterResultsForm {
    private final RootPanel rootPanel;
    private RadioButton indoorRadioButton;
    private RadioButton outdoorRadioButton;
    private ListBox eventListBox;
    private ListBox seasonComboBox;
    private ListBox regionListBox;
    private ListBox venueListBox;
    private ListBox competitionListBox;
    private ListBox dateListBox;
    private Button enterButton;
    private LongBox athlete1LongBox;
    private LongBox athlete2LongBox;
    private LongBox athlete3LongBox;
    private LongBox athlete4LongBox;
    private LongBox athlete5LongBox;
    private LongBox athlete6LongBox;
    private LongBox athlete7LongBox;
    private LongBox athlete8LongBox;
    private TextBox athlete1PerformanceTextBox;
    private TextBox athlete2PerformanceTextBox;
    private TextBox athlete3PerformanceTextBox;
    private TextBox athlete4PerformanceTextBox;
    private TextBox athlete5PerformanceTextBox;
    private TextBox athlete6PerformanceTextBox;
    private TextBox athlete7PerformanceTextBox;
    private TextBox athlete8PerformanceTextBox;
    private TextBox searchAthleteNameTextBox;
    private TextBox searchAthleteSurnameTextBox;
    private TextBox searchAthleteIdTextBox;
    private Button athleteIdSearchButton;
    private Button athleteNameSurnameSearchButton;
    private Button clearButton;
    private Long competitionId;
    private RadioButton femaleRadioButton;
    private RadioButton femaleSearchRadioButton;
    private int workCount;
    private int totalWork;
    private AbsolutePanel searchAthletePanel;
    private CellTable<Athlete> searchAthleteTable;

    public EnterResultsForm()
    {
        this.rootPanel = RootPanel.get();
        this.rootPanel.setStyleName("background");
        StandardLayoutForm layout = new StandardLayoutForm(this.rootPanel, "770px");
        enterResultsFormDesign();

        String competitionType = this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
        this.seasonComboBox.clear();
        getSeasons(competitionType);

        layout.homeMenuItem().setScheduledCommand(new Command() {
            public void execute() {
                EnterResultsForm.this.rootPanel.clear();
                new HomeForm();
            }
        });
        layout.enterResultsMenuItem().setScheduledCommand(new Command() {
            public void execute() {
                EnterResultsForm.this.rootPanel.clear();
                new EnterResultsForm();
            }
        });
        this.athleteIdSearchButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    if (!VariousFunctions.isNumeric(EnterResultsForm.this.searchAthleteIdTextBox.getText()))
                        throw new Exception("Value is not numeric!");
                    long id = Long.parseLong(EnterResultsForm.this.searchAthleteIdTextBox.getText());
                    EnterResultsForm.this.searchAthleteIdTextBox.setValue(null);
                    EnterResultsForm.this.getAthlete(Long.valueOf(id));
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Search Athlete", e.getMessage());
                }
            }
        });
        this.athleteNameSurnameSearchButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    Gender gender = EnterResultsForm.this.femaleSearchRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;

                    if (EnterResultsForm.this.searchAthleteNameTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    String name = EnterResultsForm.this.searchAthleteNameTextBox.getValue();
                    EnterResultsForm.this.searchAthleteNameTextBox.setValue(null);

                    if (EnterResultsForm.this.searchAthleteSurnameTextBox.getValue().length() > 20)
                        throw new Exception("Surname is too long");
                    String surname = EnterResultsForm.this.searchAthleteSurnameTextBox.getValue();
                    EnterResultsForm.this.searchAthleteSurnameTextBox.setValue(null);

                    EnterResultsForm.this.getAthlete(name, surname, gender);
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Search Athlete", e.getMessage());
                }
            }
        });
        this.seasonComboBox.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event) {
                try {
                    EnterResultsForm.this.regionListBox.clear();
                    EnterResultsForm.this.regionListBox.addItem("Select Region");
                    EnterResultsForm.this.regionListBox.setEnabled(false);
                    EnterResultsForm.this.venueListBox.clear();
                    EnterResultsForm.this.venueListBox.addItem("Select Venue");
                    EnterResultsForm.this.venueListBox.setEnabled(false);
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);
                    String season = EnterResultsForm.this.seasonComboBox.getItemText(EnterResultsForm.this.seasonComboBox.getSelectedIndex());
                    if ((season.isEmpty()) || (season.equalsIgnoreCase("Select Season"))) {
                        throw new Exception();
                    }
                    String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";

                    EnterResultsForm.this.seasonComboBox.setEnabled(false);
                    EnterResultsForm.this.getRegions(competitionType, season);
                    EnterResultsForm.this.seasonComboBox.setEnabled(true);
                    EnterResultsForm.this.regionListBox.setEnabled(true);
                }
                catch (Exception e) {
                    EnterResultsForm.this.regionListBox.clear();
                    EnterResultsForm.this.regionListBox.addItem("Select Region");
                    EnterResultsForm.this.regionListBox.setEnabled(false);
                    EnterResultsForm.this.venueListBox.clear();
                    EnterResultsForm.this.venueListBox.addItem("Select Venue");
                    EnterResultsForm.this.venueListBox.setEnabled(false);
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);
                }
            }
        });
        this.indoorRadioButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (EnterResultsForm.this.indoorRadioButton.getValue().booleanValue()) {
                    EnterResultsForm.this.indoorRadioButton.setEnabled(false);
                    EnterResultsForm.this.seasonComboBox.setEnabled(false);
                    EnterResultsForm.this.eventListBox.clear();
                    EnterResultsForm.this.seasonComboBox.clear();
                    String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    EnterResultsForm.this.getSeasons(competitionType);
                    VariousFunctions.eventItem(EnterResultsForm.this.eventListBox, "Indoor");
                    EnterResultsForm.this.indoorRadioButton.setEnabled(true);
                    EnterResultsForm.this.seasonComboBox.setEnabled(true);
                }
            }
        });
        this.outdoorRadioButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (EnterResultsForm.this.outdoorRadioButton.getValue().booleanValue()) {
                    EnterResultsForm.this.outdoorRadioButton.setEnabled(false);
                    EnterResultsForm.this.seasonComboBox.setEnabled(false);
                    EnterResultsForm.this.eventListBox.clear();
                    EnterResultsForm.this.seasonComboBox.clear();
                    String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    EnterResultsForm.this.getSeasons(competitionType);
                    VariousFunctions.eventItem(EnterResultsForm.this.eventListBox, "Outdoor");
                    EnterResultsForm.this.outdoorRadioButton.setEnabled(true);
                    EnterResultsForm.this.seasonComboBox.setEnabled(true);
                }
            }
        });
        this.regionListBox.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event) {
                try {
                    EnterResultsForm.this.venueListBox.clear();
                    EnterResultsForm.this.venueListBox.addItem("Select Venue");
                    EnterResultsForm.this.venueListBox.setEnabled(false);
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);

                    String season = EnterResultsForm.this.seasonComboBox.getItemText(EnterResultsForm.this.seasonComboBox.getSelectedIndex());
                    String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    Region region = VariousFunctions.stringToRegion(EnterResultsForm.this.regionListBox.getItemText(EnterResultsForm.this.regionListBox.getSelectedIndex()));

                    EnterResultsForm.this.regionListBox.setEnabled(false);
                    EnterResultsForm.this.getVenue(competitionType, season, region);
                    EnterResultsForm.this.regionListBox.setEnabled(true);
                    EnterResultsForm.this.venueListBox.setEnabled(true);
                }
                catch (Exception e) {
                    EnterResultsForm.this.venueListBox.clear();
                    EnterResultsForm.this.venueListBox.addItem("Select Venue");
                    EnterResultsForm.this.venueListBox.setEnabled(false);
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);
                }
            }
        });
        this.venueListBox.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event) {
                try {
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);

                    String season = EnterResultsForm.this.seasonComboBox.getItemText(EnterResultsForm.this.seasonComboBox.getSelectedIndex());
                    String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    Region region = VariousFunctions.stringToRegion(EnterResultsForm.this.regionListBox.getItemText(EnterResultsForm.this.regionListBox.getSelectedIndex()));
                    String venue = EnterResultsForm.this.venueListBox.getItemText(EnterResultsForm.this.venueListBox.getSelectedIndex());
                    if ((venue.isEmpty()) || (venue.equalsIgnoreCase("Select Venue"))) {
                        throw new Exception();
                    }
                    EnterResultsForm.this.venueListBox.setEnabled(false);
                    EnterResultsForm.this.getVenue(competitionType, season, region, venue);
                    EnterResultsForm.this.venueListBox.setEnabled(true);
                    EnterResultsForm.this.competitionListBox.setEnabled(true);
                }
                catch (Exception e) {
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);
                }
            }
        });
        this.competitionListBox.addChangeHandler(new ChangeHandler()
        {
            public void onChange(ChangeEvent event) {
                try {
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);

                    String season = EnterResultsForm.this.seasonComboBox.getItemText(EnterResultsForm.this.seasonComboBox.getSelectedIndex());
                    String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    Region region = VariousFunctions.stringToRegion(EnterResultsForm.this.regionListBox.getItemText(EnterResultsForm.this.regionListBox.getSelectedIndex()));
                    String venue = EnterResultsForm.this.venueListBox.getItemText(EnterResultsForm.this.venueListBox.getSelectedIndex());
                    String competitionName = EnterResultsForm.this.competitionListBox.getItemText(EnterResultsForm.this.competitionListBox.getSelectedIndex());
                    if ((competitionName.isEmpty()) || (competitionName.equalsIgnoreCase("Select Competition"))) {
                        throw new Exception();
                    }
                    EnterResultsForm.this.competitionListBox.setEnabled(false);
                    EnterResultsForm.this.getDate(competitionType, season, region, venue, competitionName);
                    EnterResultsForm.this.competitionListBox.setEnabled(true);
                    EnterResultsForm.this.dateListBox.setEnabled(true);
                }
                catch (Exception e) {
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");
                    EnterResultsForm.this.dateListBox.setEnabled(false);
                }
            }
        });
        this.dateListBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                String date = EnterResultsForm.this.dateListBox.getItemText(EnterResultsForm.this.dateListBox.getSelectedIndex());
                if ((date.isEmpty()) || (date.equalsIgnoreCase("Select Date"))) {
                    EnterResultsForm.this.enterButton.setEnabled(false);
                } else {
                    String season = EnterResultsForm.this.seasonComboBox.getItemText(EnterResultsForm.this.seasonComboBox.getSelectedIndex());
                    try
                    {
                        Region region = VariousFunctions.stringToRegion(EnterResultsForm.this.regionListBox.getItemText(EnterResultsForm.this.regionListBox.getSelectedIndex()));
                        String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                        String venue = EnterResultsForm.this.venueListBox.getItemText(EnterResultsForm.this.venueListBox.getSelectedIndex());
                        String competitionName = EnterResultsForm.this.competitionListBox.getItemText(EnterResultsForm.this.competitionListBox.getSelectedIndex());
                        EnterResultsForm.this.getCompetitionID(competitionType, season, region, venue, competitionName, date);
                        EnterResultsForm.this.enterButton.setEnabled(true);
                    }
                    catch (Exception localException)
                    {
                    }
                }
            }
        });
        this.enterButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Gender gender = EnterResultsForm.this.femaleRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;
                String competitionType = EnterResultsForm.this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                try {
                    boolean windowsBox = Window.confirm("Are you sure? \n\nCompetition Type: " +
                            competitionType + "\n" +
                            "Gender Result: " + gender + "\n" +
                            "Event: " + EnterResultsForm.this.eventListBox.getItemText(EnterResultsForm.this.eventListBox.getSelectedIndex()));

                    if (windowsBox) EnterResultsForm.this.sendResults();
                }
                catch (Exception e) { VariousFunctions.informationDialogBox("Enter Results", e.getMessage()); }

            }
        });
        this.clearButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                boolean windowsBox = Window.confirm("Are you sure?");
                if (windowsBox) EnterResultsForm.this.clear();
            }
        });
    }

    private void enterResultsFormDesign()
    {
        AbsolutePanel absolutePanel = new AbsolutePanel();
        this.rootPanel.add(absolutePanel, 10, 148);
        absolutePanel.setSize("770px", "560px");

        AbsolutePanel selectionPanel = new AbsolutePanel();
        selectionPanel.setStyleName("panelBorder");
        absolutePanel.add(selectionPanel, 0, 0);
        selectionPanel.setSize("763px", "127px");

        Label lblSelectCompetition = new Label("Competition Details");
        lblSelectCompetition.setStyleName("topLabel");
        lblSelectCompetition.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectionPanel.add(lblSelectCompetition, 0, 0);
        lblSelectCompetition.setSize("763px", "18px");

        Label lblSeason = new Label("Season:");
        lblSeason.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        selectionPanel.add(lblSeason, 198, 28);

        this.seasonComboBox = new ListBox();
        this.seasonComboBox.addItem("Select Season");
        selectionPanel.add(this.seasonComboBox, 252, 24);
        this.seasonComboBox.setSize("125px", "22px");

        this.indoorRadioButton = new RadioButton("competitionLeaderboard", "Indoor");
        this.indoorRadioButton.setValue(Boolean.valueOf(true));
        selectionPanel.add(this.indoorRadioButton, 10, 47);
        this.indoorRadioButton.setSize("64", "18");

        this.outdoorRadioButton = new RadioButton("competitionLeaderboard", "Outdoor");
        selectionPanel.add(this.outdoorRadioButton, 87, 48);
        this.outdoorRadioButton.setSize("81px", "18px");

        Label label = new Label("Select a Competition Type:");
        label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectionPanel.add(label, 0, 25);
        label.setSize("167px", "20px");

        this.femaleRadioButton = new RadioButton("gender", "Female");
        selectionPanel.add(this.femaleRadioButton, 86, 100);
        this.femaleRadioButton.setSize("67px", "18px");

        RadioButton maleRadioButton = new RadioButton("gender", "Male");
        maleRadioButton.setValue(Boolean.valueOf(true));
        selectionPanel.add(maleRadioButton, 10, 100);
        maleRadioButton.setSize("67px", "18px");

        Label label_1 = new Label("Select Gender:");
        label_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectionPanel.add(label_1, 0, 78);
        label_1.setSize("167px", "20px");

        Label lblRegion = new Label("Region:");
        lblRegion.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        selectionPanel.add(lblRegion, 198, 63);
        lblRegion.setSize("48px", "18px");

        this.regionListBox = new ListBox();
        this.regionListBox.addItem("Select Region");
        this.regionListBox.setEnabled(false);
        selectionPanel.add(this.regionListBox, 252, 59);
        this.regionListBox.setSize("125px", "22px");

        Label lblVenue = new Label("Venue:");
        lblVenue.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        selectionPanel.add(lblVenue, 198, 101);
        lblVenue.setSize("48px", "18px");

        this.venueListBox = new ListBox();
        this.venueListBox.addItem("Select Venue");
        this.venueListBox.setEnabled(false);
        selectionPanel.add(this.venueListBox, 252, 97);
        this.venueListBox.setSize("125px", "22px");

        this.competitionListBox = new ListBox();
        this.competitionListBox.addItem("Select Competition");
        this.competitionListBox.setEnabled(false);
        selectionPanel.add(this.competitionListBox, 514, 24);
        this.competitionListBox.setSize("203px", "22px");

        Label lblCompetitionName = new Label("Competition Name:");
        lblCompetitionName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        selectionPanel.add(lblCompetitionName, 383, 28);
        lblCompetitionName.setSize("125px", "18px");

        Label lblCompetitionDate = new Label("Competition Date:");
        lblCompetitionDate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        selectionPanel.add(lblCompetitionDate, 383, 63);
        lblCompetitionDate.setSize("125px", "18px");

        this.dateListBox = new ListBox();
        this.dateListBox.addItem("Select Date");
        this.dateListBox.setEnabled(false);
        selectionPanel.add(this.dateListBox, 514, 59);
        this.dateListBox.setSize("125px", "22px");

        Label lblEvent = new Label("Event:");
        lblEvent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        selectionPanel.add(lblEvent, 452, 100);
        lblEvent.setSize("56px", "18px");

        this.eventListBox = new ListBox();
        VariousFunctions.eventItem(this.eventListBox, "Indoor");
        selectionPanel.add(this.eventListBox, 514, 97);
        this.eventListBox.setSize("125px", "22px");

        AbsolutePanel enterResultPanel = new AbsolutePanel();
        enterResultPanel.setStyleName("panelBorder");
        absolutePanel.add(enterResultPanel, 44, 181);
        enterResultPanel.setSize("372px", "367px");

        Label lblEventResults = new Label("Event Results");
        lblEventResults.setStyleName("topLabel");
        lblEventResults.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        enterResultPanel.add(lblEventResults, 0, 0);
        lblEventResults.setSize("372px", "18px");

        this.athlete1LongBox = new LongBox();
        enterResultPanel.add(this.athlete1LongBox, 117, 24);
        this.athlete1LongBox.setSize("38px", "24px");

        Label label_2 = new Label("#1");
        enterResultPanel.add(label_2, 0, 28);

        Label lblAthleteId = new Label("Athlete's ID:");
        enterResultPanel.add(lblAthleteId, 29, 28);
        lblAthleteId.setSize("82px", "18px");

        Label lblPerformance = new Label("Performance :");
        enterResultPanel.add(lblPerformance, 163, 28);
        lblPerformance.setSize("89px", "18px");

        this.athlete1PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete1PerformanceTextBox, 258, 24);
        this.athlete1PerformanceTextBox.setSize("93px", "18px");

        Label label_3 = new Label("#2");
        enterResultPanel.add(label_3, 0, 64);
        label_3.setSize("14px", "18px");

        Label label_4 = new Label("Athlete's ID:");
        enterResultPanel.add(label_4, 29, 64);
        label_4.setSize("82px", "18px");

        this.athlete2LongBox = new LongBox();
        enterResultPanel.add(this.athlete2LongBox, 117, 60);
        this.athlete2LongBox.setSize("38px", "24px");

        Label label_5 = new Label("Performance :");
        enterResultPanel.add(label_5, 163, 64);
        label_5.setSize("89px", "18px");

        this.athlete2PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete2PerformanceTextBox, 258, 60);
        this.athlete2PerformanceTextBox.setSize("93px", "18px");

        Label label_6 = new Label("#3");
        enterResultPanel.add(label_6, 0, 100);
        label_6.setSize("14px", "18px");

        Label label_7 = new Label("Athlete's ID:");
        enterResultPanel.add(label_7, 29, 100);
        label_7.setSize("82px", "18px");

        this.athlete3LongBox = new LongBox();
        enterResultPanel.add(this.athlete3LongBox, 117, 96);
        this.athlete3LongBox.setSize("38px", "24px");

        Label label_8 = new Label("Performance :");
        enterResultPanel.add(label_8, 163, 100);
        label_8.setSize("89px", "18px");

        this.athlete3PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete3PerformanceTextBox, 258, 96);
        this.athlete3PerformanceTextBox.setSize("93px", "18px");

        Label label_9 = new Label("#4");
        enterResultPanel.add(label_9, 0, 136);
        label_9.setSize("14px", "18px");

        Label label_10 = new Label("Athlete's ID:");
        enterResultPanel.add(label_10, 29, 136);
        label_10.setSize("82px", "18px");

        this.athlete4LongBox = new LongBox();
        enterResultPanel.add(this.athlete4LongBox, 117, 132);
        this.athlete4LongBox.setSize("38px", "24px");

        Label label_11 = new Label("Performance :");
        enterResultPanel.add(label_11, 163, 136);
        label_11.setSize("89px", "18px");

        this.athlete4PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete4PerformanceTextBox, 258, 132);
        this.athlete4PerformanceTextBox.setSize("93px", "18px");

        Label label_12 = new Label("#5");
        enterResultPanel.add(label_12, 0, 172);
        label_12.setSize("14px", "18px");

        Label label_13 = new Label("Athlete's ID:");
        enterResultPanel.add(label_13, 29, 172);
        label_13.setSize("82px", "18px");

        this.athlete5LongBox = new LongBox();
        enterResultPanel.add(this.athlete5LongBox, 117, 168);
        this.athlete5LongBox.setSize("38px", "24px");

        Label label_14 = new Label("Performance :");
        enterResultPanel.add(label_14, 163, 172);
        label_14.setSize("89px", "18px");

        this.athlete5PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete5PerformanceTextBox, 258, 168);
        this.athlete5PerformanceTextBox.setSize("93px", "18px");

        Label label_15 = new Label("#6");
        enterResultPanel.add(label_15, 0, 208);
        label_15.setSize("14px", "18px");

        Label label_16 = new Label("Athlete's ID:");
        enterResultPanel.add(label_16, 29, 208);
        label_16.setSize("82px", "18px");

        this.athlete6LongBox = new LongBox();
        enterResultPanel.add(this.athlete6LongBox, 117, 204);
        this.athlete6LongBox.setSize("38px", "24px");

        Label label_17 = new Label("Performance :");
        enterResultPanel.add(label_17, 163, 208);
        label_17.setSize("89px", "18px");

        this.athlete6PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete6PerformanceTextBox, 258, 204);
        this.athlete6PerformanceTextBox.setSize("93px", "18px");

        Label label_18 = new Label("#7");
        enterResultPanel.add(label_18, 0, 244);
        label_18.setSize("14px", "18px");

        Label label_19 = new Label("Athlete's ID:");
        enterResultPanel.add(label_19, 30, 244);
        label_19.setSize("84px", "18px");

        this.athlete7LongBox = new LongBox();
        enterResultPanel.add(this.athlete7LongBox, 117, 240);
        this.athlete7LongBox.setSize("39px", "24px");

        Label label_20 = new Label("Performance :");
        enterResultPanel.add(label_20, 164, 244);
        label_20.setSize("89px", "18px");

        this.athlete7PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete7PerformanceTextBox, 259, 240);
        this.athlete7PerformanceTextBox.setSize("93px", "18px");

        Label label_21 = new Label("#8");
        enterResultPanel.add(label_21, 0, 280);
        label_21.setSize("14px", "18px");

        Label label_22 = new Label("Athlete's ID:");
        enterResultPanel.add(label_22, 30, 280);
        label_22.setSize("84px", "18px");

        this.athlete8LongBox = new LongBox();
        enterResultPanel.add(this.athlete8LongBox, 117, 276);
        this.athlete8LongBox.setSize("39px", "24px");

        Label label_23 = new Label("Performance :");
        enterResultPanel.add(label_23, 164, 280);
        label_23.setSize("89px", "18px");

        this.athlete8PerformanceTextBox = new TextBox();
        enterResultPanel.add(this.athlete8PerformanceTextBox, 259, 276);
        this.athlete8PerformanceTextBox.setSize("93px", "18px");

        this.enterButton = new Button("Enter");
        this.enterButton.setEnabled(false);
        enterResultPanel.add(this.enterButton, 258, 312);
        this.enterButton.setSize("81px", "30px");

        this.clearButton = new Button("Enter");
        this.clearButton.setText("Clear");
        enterResultPanel.add(this.clearButton, 171, 312);
        this.clearButton.setSize("81px", "30px");

        Label lblpleaseSelectCompetition = new Label("*Please select competition details.");
        lblpleaseSelectCompetition.setStyleName("errorLabel");
        enterResultPanel.add(lblpleaseSelectCompetition, 80, 348);

        AbsolutePanel exampleResultPanel = new AbsolutePanel();
        exampleResultPanel.setStyleName("panelBorder");
        absolutePanel.add(exampleResultPanel, 0, 136);
        exampleResultPanel.setSize("763px", "39px");

        Label lblSamplePerformance = new Label("Sample Performance:   100m = 11.60,  800m = 1:59:00, Long Jump = 6.20, Decathlon = 5000");
        lblSamplePerformance.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblSamplePerformance.setStyleName("errorLabel");
        exampleResultPanel.add(lblSamplePerformance, 0, 0);
        lblSamplePerformance.setSize("763px", "18px");

        Label lblPleaseMakeSure = new Label("Please Make sure they're no duplicate Athlete ID. All Invalid Results will be ignored.");
        lblPleaseMakeSure.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblPleaseMakeSure.setStyleName("errorLabel");
        exampleResultPanel.add(lblPleaseMakeSure, 0, 18);
        lblPleaseMakeSure.setSize("763px", "18px");

        this.searchAthletePanel = new AbsolutePanel();
        this.searchAthletePanel.setStyleName("panelBorder");
        absolutePanel.add(this.searchAthletePanel, 424, 181);
        this.searchAthletePanel.setSize("314px", "367px");

        Label searchAthleteTitleLabel = new Label("Search Athlete");
        searchAthleteTitleLabel.setStyleName("topLabel");
        searchAthleteTitleLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.searchAthletePanel.add(searchAthleteTitleLabel, 0, 0);
        searchAthleteTitleLabel.setSize("314px", "18px");

        RadioButton maleSearchRadioButton = new RadioButton("competitionType", "Indoor");
        this.searchAthletePanel.add(maleSearchRadioButton, 83, 24);
        maleSearchRadioButton.setValue(Boolean.valueOf(true));
        maleSearchRadioButton.setHTML("Male");
        maleSearchRadioButton.setWidth("72px");

        this.femaleSearchRadioButton = new RadioButton("competitionType", "Outdoor");
        this.searchAthletePanel.add(this.femaleSearchRadioButton, 161, 24);
        this.femaleSearchRadioButton.setHTML("Female");

        Label athleteSearchIDLabel = new Label("Athlete ID:");
        this.searchAthletePanel.add(athleteSearchIDLabel, 0, 58);
        athleteSearchIDLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        athleteSearchIDLabel.setSize("77px", "18px");

        this.searchAthleteIdTextBox = new TextBox();
        this.searchAthletePanel.add(this.searchAthleteIdTextBox, 83, 58);
        this.searchAthleteIdTextBox.setSize("138px", "18px");

        this.athleteIdSearchButton = new Button("Enter");
        this.athleteIdSearchButton.setText("Search");
        this.searchAthletePanel.add(this.athleteIdSearchButton, 237, 58);
        this.athleteIdSearchButton.setSize("67px", "30px");

        Label athleteSearchNameLabel = new Label("First Name:");
        athleteSearchNameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.searchAthletePanel.add(athleteSearchNameLabel, 0, 105);
        athleteSearchNameLabel.setSize("77px", "18px");

        this.searchAthleteNameTextBox = new TextBox();
        this.searchAthletePanel.add(this.searchAthleteNameTextBox, 82, 105);
        this.searchAthleteNameTextBox.setSize("139px", "18px");

        Label athleteSearchSurnameLabel = new Label("Surname:");
        athleteSearchSurnameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.searchAthletePanel.add(athleteSearchSurnameLabel, 0, 142);
        athleteSearchSurnameLabel.setSize("77px", "18px");

        this.searchAthleteSurnameTextBox = new TextBox();
        this.searchAthletePanel.add(this.searchAthleteSurnameTextBox, 82, 142);
        this.searchAthleteSurnameTextBox.setSize("139px", "18px");

        this.athleteNameSurnameSearchButton = new Button("Enter");
        this.athleteNameSurnameSearchButton.setText("Search");
        this.searchAthletePanel.add(this.athleteNameSurnameSearchButton, 237, 142);
        this.athleteNameSurnameSearchButton.setSize("67px", "30px");

        createTable();
    }

    private void createTable() {
        ScrollPanel scrollPanel = new ScrollPanel();
        this.searchAthletePanel.add(scrollPanel, 0, 188);
        scrollPanel.setSize("314px", "179px");

        this.searchAthleteTable = new CellTable<Athlete>();
        this.searchAthleteTable.setStyleName("panelBorder");
        scrollPanel.setWidget(this.searchAthleteTable);
        this.searchAthleteTable.setSize("312px", "52px");
        this.searchAthleteTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        Column<Athlete,Number> rankColumn = new Column<Athlete,Number>(new NumberCell()) {
            public Number getValue(Athlete athlete) {
                return athlete.getId();
            }
        };
        rankColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.searchAthleteTable.addColumn(rankColumn, "ID");
        this.searchAthleteTable.setColumnWidth(rankColumn, "54px");

        TextColumn<Athlete> performanceColumn = new TextColumn<Athlete>() {
            public String getValue(Athlete athlete) {
                return athlete.getName();
            }
        };
        performanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.searchAthleteTable.addColumn(performanceColumn, "Name");

        TextColumn<Athlete> surnameColumn = new TextColumn<Athlete>() {
            public String getValue(Athlete object) {
                return object.getSurname();
            }
        };
        surnameColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.searchAthleteTable.addColumn(surnameColumn, "Surname");

        SingleSelectionModel<Athlete> selectionModel = new SingleSelectionModel<Athlete>();
        this.searchAthleteTable.setSelectionModel(selectionModel);

        List<Athlete> athlete = Arrays.asList(new Athlete[]{new Athlete(Gender.Male, "Empty", "Empty", Region.Leinster, Long.valueOf(Long.parseLong("0")))});
        this.searchAthleteTable.setRowCount(athlete.size(), true);
        this.searchAthleteTable.setRowData(0, athlete);
    }

    private void clear() {
        this.athlete1LongBox.setValue(null);
        this.athlete2LongBox.setValue(null);
        this.athlete3LongBox.setValue(null);
        this.athlete4LongBox.setValue(null);
        this.athlete5LongBox.setValue(null);
        this.athlete6LongBox.setValue(null);
        this.athlete7LongBox.setValue(null);
        this.athlete8LongBox.setValue(null);
        this.athlete1PerformanceTextBox.setText(null);
        this.athlete2PerformanceTextBox.setText(null);
        this.athlete3PerformanceTextBox.setText(null);
        this.athlete4PerformanceTextBox.setText(null);
        this.athlete5PerformanceTextBox.setText(null);
        this.athlete6PerformanceTextBox.setText(null);
        this.athlete7PerformanceTextBox.setText(null);
        this.athlete8PerformanceTextBox.setText(null);
        this.searchAthleteIdTextBox.setText(null);
        this.searchAthleteNameTextBox.setText(null);
        this.searchAthleteSurnameTextBox.setText(null);

        List<Athlete> athlete = Arrays.asList(new Athlete[] { new Athlete(Gender.Male, "Empty", "Empty", Region.Leinster, Long.valueOf(Long.parseLong("0"))) });
        this.searchAthleteTable.setRowCount(athlete.size(), true);
        this.searchAthleteTable.setRowData(0, athlete);
    }

    private void sendResults()
    {
        try {
            Gender gender = this.femaleRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;
            String competitionType = this.indoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
            String event = this.eventListBox.getItemText(this.eventListBox.getSelectedIndex());
            int season = Integer.parseInt(this.seasonComboBox.getItemText(this.seasonComboBox.getSelectedIndex()));

            this.workCount = 0;
            this.totalWork = 0;

            for (int i = 0; i < 11; i++)
                switch (i) {
                    case 0:
                        if ((!this.athlete1LongBox.getText().isEmpty()) && (!this.athlete1PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete1LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete1PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete1LongBox.getValue() != this.athlete2LongBox.getValue()) && (this.athlete1LongBox.getValue() != this.athlete3LongBox.getValue()) &&
                                    (this.athlete1LongBox.getValue() != this.athlete4LongBox.getValue()) && (this.athlete1LongBox.getValue() != this.athlete5LongBox.getValue()) &&
                                    (this.athlete1LongBox.getValue() != this.athlete6LongBox.getValue()) && (this.athlete1LongBox.getValue() != this.athlete7LongBox.getValue()) &&
                                    (this.athlete1LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete1LongBox.getText())),
                                        this.athlete1PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete1LongBox.getText())), result, i); }
                        }break;
                    case 1:
                        if ((!this.athlete2LongBox.getText().isEmpty()) && (!this.athlete2PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete2LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete2PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete2LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete2LongBox.getValue() != this.athlete3LongBox.getValue()) &&
                                    (this.athlete2LongBox.getValue() != this.athlete4LongBox.getValue()) && (this.athlete2LongBox.getValue() != this.athlete5LongBox.getValue()) &&
                                    (this.athlete2LongBox.getValue() != this.athlete6LongBox.getValue()) && (this.athlete2LongBox.getValue() != this.athlete7LongBox.getValue()) &&
                                    (this.athlete2LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete2LongBox.getText())),
                                        this.athlete2PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete2LongBox.getText())), result, i); }
                        }break;
                    case 2:
                        if ((!this.athlete3LongBox.getText().isEmpty()) && (!this.athlete3PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete3LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete3PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete3LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete3LongBox.getValue() != this.athlete2LongBox.getValue()) &&
                                    (this.athlete3LongBox.getValue() != this.athlete4LongBox.getValue()) && (this.athlete3LongBox.getValue() != this.athlete5LongBox.getValue()) &&
                                    (this.athlete3LongBox.getValue() != this.athlete6LongBox.getValue()) && (this.athlete3LongBox.getValue() != this.athlete7LongBox.getValue()) &&
                                    (this.athlete3LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete3LongBox.getText())),
                                        this.athlete3PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete3LongBox.getText())), result, i); }
                        }break;
                    case 3:
                        if ((!this.athlete4LongBox.getText().isEmpty()) && (!this.athlete4PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete4LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete4PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete4LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete4LongBox.getValue() != this.athlete2LongBox.getValue()) &&
                                    (this.athlete4LongBox.getValue() != this.athlete3LongBox.getValue()) && (this.athlete4LongBox.getValue() != this.athlete5LongBox.getValue()) &&
                                    (this.athlete4LongBox.getValue() != this.athlete6LongBox.getValue()) && (this.athlete4LongBox.getValue() != this.athlete7LongBox.getValue()) &&
                                    (this.athlete4LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete4LongBox.getText())),
                                        this.athlete4PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete4LongBox.getText())), result, i); }
                        }break;
                    case 4:
                        if ((!this.athlete5LongBox.getText().isEmpty()) && (!this.athlete5PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete5LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete5PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete5LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete5LongBox.getValue() != this.athlete2LongBox.getValue()) &&
                                    (this.athlete5LongBox.getValue() != this.athlete3LongBox.getValue()) && (this.athlete5LongBox.getValue() != this.athlete4LongBox.getValue()) &&
                                    (this.athlete5LongBox.getValue() != this.athlete6LongBox.getValue()) && (this.athlete5LongBox.getValue() != this.athlete7LongBox.getValue()) &&
                                    (this.athlete5LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete5LongBox.getText())),
                                        this.athlete5PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete5LongBox.getText())), result, i); }
                        }break;
                    case 5:
                        if ((!this.athlete6LongBox.getText().isEmpty()) && (!this.athlete6PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete6LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete6PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete6LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete6LongBox.getValue() != this.athlete2LongBox.getValue()) &&
                                    (this.athlete6LongBox.getValue() != this.athlete3LongBox.getValue()) && (this.athlete6LongBox.getValue() != this.athlete4LongBox.getValue()) &&
                                    (this.athlete6LongBox.getValue() != this.athlete5LongBox.getValue()) && (this.athlete6LongBox.getValue() != this.athlete7LongBox.getValue()) &&
                                    (this.athlete6LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete6LongBox.getText())),
                                        this.athlete6PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete6LongBox.getText())), result, i); }
                        }break;
                    case 6:
                        if ((!this.athlete7LongBox.getText().isEmpty()) && (!this.athlete7PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete7LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete7PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete7LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete7LongBox.getValue() != this.athlete2LongBox.getValue()) &&
                                    (this.athlete7LongBox.getValue() != this.athlete3LongBox.getValue()) && (this.athlete7LongBox.getValue() != this.athlete4LongBox.getValue()) &&
                                    (this.athlete7LongBox.getValue() != this.athlete5LongBox.getValue()) && (this.athlete7LongBox.getValue() != this.athlete6LongBox.getValue()) &&
                                    (this.athlete7LongBox.getValue() != this.athlete8LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete7LongBox.getText())),
                                        this.athlete7PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete7LongBox.getText())), result, i); }
                        }break;
                    case 7:
                        if ((!this.athlete8LongBox.getText().isEmpty()) && (!this.athlete8PerformanceTextBox.getText().isEmpty()) &&
                                (VariousFunctions.isNumeric(this.athlete8LongBox.getText())) &&
                                (VariousFunctions.isValidResult(this.athlete8PerformanceTextBox.getText(), event)))
                        {
                            if ((this.athlete8LongBox.getValue() != this.athlete1LongBox.getValue()) && (this.athlete8LongBox.getValue() != this.athlete2LongBox.getValue()) &&
                                    (this.athlete8LongBox.getValue() != this.athlete3LongBox.getValue()) && (this.athlete8LongBox.getValue() != this.athlete4LongBox.getValue()) &&
                                    (this.athlete8LongBox.getValue() != this.athlete5LongBox.getValue()) && (this.athlete8LongBox.getValue() != this.athlete6LongBox.getValue()) &&
                                    (this.athlete8LongBox.getValue() != this.athlete7LongBox.getValue()))
                            {
                                Result result = new Result(competitionType, this.competitionId, Long.valueOf(Long.parseLong(this.athlete8LongBox.getText())),
                                        this.athlete8PerformanceTextBox.getText(), event, gender, season);

                                validateAthlete(Long.valueOf(Long.parseLong(this.athlete8LongBox.getText())), result, i);
                            }
                        }
                        break;
                }
        } catch (Exception e) { VariousFunctions.errorDialogBox("Sending Results", e.getMessage()); }
    }

    private void getAthlete(Long id)
    {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Athlete>> callback = new AsyncCallback<List<Athlete> >() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                }
            }

            public void onSuccess(List<Athlete> athletes) {
                try {
                    if (athletes != null) {
                        for (Athlete athlete : athletes)
                        {
                            athlete.setName(athlete.getName().substring(0, 1).toUpperCase()
                                    .concat(athlete.getName().substring(1).toLowerCase()));
                            athlete.setSurname(athlete.getSurname().substring(0, 1).toUpperCase()
                                    .concat(athlete.getSurname().substring(1).toLowerCase()));
                        }

                        EnterResultsForm.this.searchAthleteTable.setRowCount(athletes.size(), true);
                        EnterResultsForm.this.searchAthleteTable.setRowData(0, athletes);
                    }
                    else {
                        VariousFunctions.informationDialogBox("Get Athlete", "No Athlete Found!");
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getAthlete(id,callback);
    }

    private void getAthlete(String name, String surname, Gender gender) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Athlete>> callback = new AsyncCallback<List<Athlete> >() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                }
            }

            public void onSuccess(List<Athlete> athletes) {
                try {
                    if (athletes != null) {
                        for (Athlete athlete : athletes)
                        {
                            athlete.setName(athlete.getName().substring(0, 1).toUpperCase()
                                    .concat(athlete.getName().substring(1).toLowerCase()));
                            athlete.setSurname(athlete.getSurname().substring(0, 1).toUpperCase()
                                    .concat(athlete.getSurname().substring(1).toLowerCase()));
                        }

                        EnterResultsForm.this.searchAthleteTable.setRowCount(athletes.size(), true);
                        EnterResultsForm.this.searchAthleteTable.setRowData(0, athletes);
                    }
                    else {
                        VariousFunctions.informationDialogBox("Get Athlete", "No Athlete Found!");
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getAthlete(name, surname, gender,callback);
    }

    private void validateAthlete(Long id, final Result result, final int index) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Athlete>> callback = new AsyncCallback<List<Athlete> >() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Validate Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Validate Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Validate Athlete", e.getMessage());
                }
            }

            public void onSuccess(List<Athlete> results) {
                try {
                    if (results != null) {
                        EnterResultsForm.this.totalWork += 1;
                        EnterResultsForm.this.enterResult(result);

                        switch (index) {
                            case 0:
                                EnterResultsForm.this.athlete1LongBox.setText(null);
                                EnterResultsForm.this.athlete1PerformanceTextBox.setText(null);
                                break;
                            case 1:
                                EnterResultsForm.this.athlete2LongBox.setText(null);
                                EnterResultsForm.this.athlete2PerformanceTextBox.setText(null);
                                break;
                            case 2:
                                EnterResultsForm.this.athlete3LongBox.setText(null);
                                EnterResultsForm.this.athlete3PerformanceTextBox.setText(null);
                                break;
                            case 3:
                                EnterResultsForm.this.athlete4LongBox.setText(null);
                                EnterResultsForm.this.athlete4PerformanceTextBox.setText(null);
                                break;
                            case 4:
                                EnterResultsForm.this.athlete5LongBox.setText(null);
                                EnterResultsForm.this.athlete5PerformanceTextBox.setText(null);
                                break;
                            case 5:
                                EnterResultsForm.this.athlete6LongBox.setText(null);
                                EnterResultsForm.this.athlete6PerformanceTextBox.setText(null);
                                break;
                            case 6:
                                EnterResultsForm.this.athlete7LongBox.setText(null);
                                EnterResultsForm.this.athlete7PerformanceTextBox.setText(null);
                                break;
                            case 7:
                                EnterResultsForm.this.athlete8LongBox.setText(null);
                                EnterResultsForm.this.athlete8PerformanceTextBox.setText(null);
                        }
                    }
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Validate Athlete", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getAthlete(id,callback);
    }

    private void getSeasons(String competitionType) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Season List", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Season List", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Season List", e.getMessage());
                }
            }

            public void onSuccess(List<String> result) {
                try {
                    EnterResultsForm.this.seasonComboBox.clear();
                    if (result.size() == 0)
                        EnterResultsForm.this.seasonComboBox.addItem("No Competitions");
                    else
                        for (String season : result) {
                            EnterResultsForm.this.seasonComboBox.addItem("Select Season");
                            EnterResultsForm.this.seasonComboBox.addItem(season);
                        }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Season List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getSeasons(competitionType,callback);
    }

    private void getRegions(String competitionType, String season) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Region>> callback = new AsyncCallback<List<Region>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Region List", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Region List", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Region List", e.getMessage());
                }
            }

            public void onSuccess(List<Region> result) {
                try {
                    EnterResultsForm.this.regionListBox.clear();
                    EnterResultsForm.this.regionListBox.addItem("Select Region");

                    for (Region region : result)
                        EnterResultsForm.this.regionListBox.addItem(region.toString());
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Region List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getRegions(competitionType, season,callback);
    }

    private void getVenue(String competitionType, String season, Region region) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Venue List", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Venue List", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Venue List", e.getMessage());
                }
            }

            public void onSuccess(List<String> result) {
                try {
                    EnterResultsForm.this.venueListBox.clear();
                    EnterResultsForm.this.venueListBox.addItem("Select Venue");

                    for (String venue : result)
                        EnterResultsForm.this.venueListBox.addItem(venue);
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Venue List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getVenue(competitionType, season, region,callback);
    }

    private void getVenue(String competitionType, String season, Region region, String venue) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Competition Name List", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Competition Name List", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Competition Name List", e.getMessage());
                }
            }

            public void onSuccess(List<String> result) {
                try {
                    EnterResultsForm.this.competitionListBox.clear();
                    EnterResultsForm.this.competitionListBox.addItem("Select Competition");

                    for (String competition : result)
                        EnterResultsForm.this.competitionListBox.addItem(competition);
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Competition Name List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getCompetitionName(competitionType, season, region, venue,callback);
    }

    private void getDate(String competitionType, String season, Region region, String venue, String competitionName) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Competition Date List", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Competition Date List", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Competition Date List", e.getMessage());
                }
            }

            public void onSuccess(List<String> result) {
                try {
                    EnterResultsForm.this.dateListBox.clear();
                    EnterResultsForm.this.dateListBox.addItem("Select Date");

                    for (String date : result)
                        EnterResultsForm.this.dateListBox.addItem(date);
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Competition Date List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getDate(competitionType, season, region, venue, competitionName,callback);
    }

    private void getCompetitionID(String competitionType, String season, Region region, String venue, String competitionName, String date) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Competition>> callback = new AsyncCallback<List<Competition>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Competition ID", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Competition ID", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Competition ID", e.getMessage());
                }
            }

            public void onSuccess(List<Competition> result) {
                try {
                    EnterResultsForm.this.competitionId = ((Competition)result.get(0)).getId();
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Competition ID", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getCompetitionID(competitionType, season, region, venue, competitionName, date,callback);
    }

    private void enterResult(Result result) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Enter Result", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Enter Result", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Enter Result", e.getMessage());
                }
            }

            public void onSuccess(Void result) { EnterResultsForm.this.workCount += 1;
                if (EnterResultsForm.this.workCount == EnterResultsForm.this.totalWork)
                    VariousFunctions.informationDialogBox("Enter Result", "All Added Successfully!");
            }
        };
        IrishSportAnalyserService.addCompetitionResult(result,callback);
    }
}
