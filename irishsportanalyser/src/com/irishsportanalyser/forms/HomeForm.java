package com.irishsportanalyser.forms;

import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;
import com.irishsportanalyser.entities.Athlete;
import com.irishsportanalyser.entities.Club;
import com.irishsportanalyser.entities.Result;
import com.irishsportanalyser.enums.Gender;
import com.irishsportanalyser.enums.Region;
import com.irishsportanalyser.shared.*;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */

public class HomeForm {
    private final RootPanel rootPanel;
    private final StandardLayoutForm layout;
    private TabPanel mainTabPanel;
    private RadioButton leaderboardOutdoorRadioButton;
    private RadioButton leaderboardIndoorRadioButton;
    private RadioButton indoorPerformanceHistoryRadioButton;
    private RadioButton outdoorPerformanceHistoryRadioButton;
    private RadioButton leaderboardMaleRadioButton;
    private RadioButton performanceFemaleRadioButton;
    private RadioButton miscFemaleRadioButton;
    private ListBox leaderboardSeasonComboBox;
    private ListBox leaderboardRegionComboBox;
    private ListBox leaderboardAgeComboBox;
    private ListBox leaderboardEventventComboBox;
    private ListBox regionProfileComboBox;
    private ListBox clubProfileComboBox;
    private ListBox seasonProfileListBox;
    private ListBox eventPerformanceHistoryListBox;
    private TextBox searchAthleteNameTextBox;
    private TextBox searchAthleteSurnameTextBox;
    private TextBox searchAthleteIdTextBox;
    private TextBox athleteIdTextBox;
    private TextBox genderProfileTextBox;
    private TextBox nameProfileTextBox;
    private TextBox surnameProfileTextBox;
    private TextBox yobProfileTextBox;
    private Button leaderboardEnterButton;
    private Button editAthleteButton;
    private Button athleteIdSearchButton;
    private Button athleteNameSurnameSearchButton;
    private Button btnSubmit;
    private Button goButton;
    private Button miscDisplayButton;
    private Button btnCancel;
    private CellTable<List<String>> leaderboardRankTable;
    private CellTable<Athlete> searchAthleteTable;
    private CellTable<Athlete> miscAthleteTable;
    private CellTable<PerformanceHistory> athletePerformanceHistoryTable;
    private ScrollPanel scrollPanel;
    private AbsolutePanel searchAthletePanel;
    private AbsolutePanel competitionHistoryPanel;
    private AbsolutePanel miscAbsolutePanel;
    private SingleSelectionModel<Athlete> selectionModel;
    private List<PerformanceHistory> totalPerformanceHistoryData;
    private List<List<String>> rankedAthletes;
    private List<List<String>> finalRankingList;
    private int workCount;
    private int totalWork;
    private int athleteCount;
    private int seasonBestCount;
    private int seasonBestCurrentCount;

    public HomeForm()
    {
        this.rootPanel = RootPanel.get();
        this.rootPanel.setStyleName("background");
        this.layout = new StandardLayoutForm(this.rootPanel, "972px");

        homeFormLeaderboardDesign();
        homeFormProfileDesign();
        homeFormMiscellaneousDesign();

        String competitionType = this.leaderboardIndoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
        getSeasons(competitionType);

        this.layout.homeMenuItem().setScheduledCommand(new Command() {
            public void execute() {
                HomeForm.this.rootPanel.clear();
                new HomeForm();
            }
        });
        this.layout.enterResultsMenuItem().setScheduledCommand(new Command() {
            public void execute() {
                HomeForm.this.rootPanel.clear();
                new EnterResultsForm();
            }
        });
        this.leaderboardEnterButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    HomeForm.this.createRankTable();
                    String competitionType = HomeForm.this.leaderboardIndoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    Gender gender = HomeForm.this.leaderboardMaleRadioButton.getValue().booleanValue() ? Gender.Male : Gender.Female;
                    int season = Integer.parseInt(HomeForm.this.leaderboardSeasonComboBox.getItemText(HomeForm.this.leaderboardSeasonComboBox.getSelectedIndex()));
                    String ageGroupQuery = VariousFunctions.getAgeJDOQueryString(HomeForm.this.leaderboardAgeComboBox.getValue(
                            HomeForm.this.leaderboardAgeComboBox.getSelectedIndex()));
                    String selectedEvent = HomeForm.this.leaderboardEventventComboBox.getItemText(HomeForm.this.leaderboardEventventComboBox.getSelectedIndex());
                    Region region = VariousFunctions.stringToRegion(HomeForm.this.leaderboardRegionComboBox.getItemText(HomeForm.this.leaderboardRegionComboBox.getSelectedIndex()));

                    HomeForm.this.getLeaderboardResults(competitionType, region, gender, selectedEvent, season, ageGroupQuery);
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard", e.getMessage());
                }
            }
        });
        this.leaderboardIndoorRadioButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (HomeForm.this.leaderboardIndoorRadioButton.getValue().booleanValue()) {
                    HomeForm.this.leaderboardIndoorRadioButton.setEnabled(false);
                    HomeForm.this.leaderboardEventventComboBox.clear();
                    VariousFunctions.eventItem(HomeForm.this.leaderboardEventventComboBox, "Indoor");
                    HomeForm.this.leaderboardIndoorRadioButton.setEnabled(true);
                    String competitionType = HomeForm.this.leaderboardIndoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    HomeForm.this.getSeasons(competitionType);
                }
            }
        });
        this.indoorPerformanceHistoryRadioButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (HomeForm.this.indoorPerformanceHistoryRadioButton.getValue().booleanValue()) {
                    HomeForm.this.indoorPerformanceHistoryRadioButton.setEnabled(false);
                    HomeForm.this.eventPerformanceHistoryListBox.clear();
                    VariousFunctions.eventItem(HomeForm.this.eventPerformanceHistoryListBox, "Indoor");
                    HomeForm.this.indoorPerformanceHistoryRadioButton.setEnabled(true);
                    String competitionType = HomeForm.this.indoorPerformanceHistoryRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    HomeForm.this.getSeasons(competitionType);
                }
            }
        });
        this.leaderboardOutdoorRadioButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (HomeForm.this.leaderboardOutdoorRadioButton.getValue().booleanValue()) {
                    HomeForm.this.leaderboardOutdoorRadioButton.setEnabled(false);
                    HomeForm.this.leaderboardEventventComboBox.clear();
                    VariousFunctions.eventItem(HomeForm.this.leaderboardEventventComboBox, "Outdoor");
                    HomeForm.this.leaderboardOutdoorRadioButton.setEnabled(true);
                    String competitionType = HomeForm.this.leaderboardIndoorRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    HomeForm.this.getSeasons(competitionType);
                }
            }
        });
        this.outdoorPerformanceHistoryRadioButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (HomeForm.this.outdoorPerformanceHistoryRadioButton.getValue().booleanValue()) {
                    HomeForm.this.outdoorPerformanceHistoryRadioButton.setEnabled(false);
                    HomeForm.this.eventPerformanceHistoryListBox.clear();
                    VariousFunctions.eventItem(HomeForm.this.eventPerformanceHistoryListBox, "Outdoor");
                    HomeForm.this.outdoorPerformanceHistoryRadioButton.setEnabled(true);
                    String competitionType = HomeForm.this.indoorPerformanceHistoryRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    HomeForm.this.getSeasons(competitionType);
                }
            }
        });
        this.athleteIdSearchButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    if (!VariousFunctions.isNumeric(HomeForm.this.searchAthleteIdTextBox.getText()))
                        throw new Exception("Value is not numeric!");
                    //Gender gender = HomeForm.this.performanceFemaleRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;
                    long id = Long.parseLong(HomeForm.this.searchAthleteIdTextBox.getText());
                    HomeForm.this.searchAthleteIdTextBox.setValue(null);
                    HomeForm.this.getAthlete(Long.valueOf(id));
                    HomeForm.this.clearProfileHistoryData();
                } catch (Exception e) {
                    HomeForm.this.clearProfileHistory();
                    HomeForm.this.goButton.setEnabled(false);
                    VariousFunctions.errorDialogBox("Search Athlete ID", e.getMessage());
                }
            }
        });
        this.athleteIdSearchButton.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == '\r')
                    try {
                        if (!VariousFunctions.isNumeric(HomeForm.this.searchAthleteIdTextBox.getText()))
                            throw new Exception("Value is not numeric!");
                        //Gender gender = HomeForm.this.performanceFemaleRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;
                        long id = Long.parseLong(HomeForm.this.searchAthleteIdTextBox.getText());
                        HomeForm.this.searchAthleteIdTextBox.setValue(null);
                        HomeForm.this.getAthlete(Long.valueOf(id));
                        HomeForm.this.clearProfileHistoryData();
                    } catch (Exception e) {
                        HomeForm.this.clearProfileHistory();
                        HomeForm.this.goButton.setEnabled(false);
                        VariousFunctions.errorDialogBox("Search Athlete ID", e.getMessage());
                    }
            }
        });
        this.athleteNameSurnameSearchButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    Gender gender = HomeForm.this.performanceFemaleRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;
                    if (HomeForm.this.searchAthleteNameTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    String name = HomeForm.this.searchAthleteNameTextBox.getValue();
                    HomeForm.this.searchAthleteNameTextBox.setValue(null);

                    if (HomeForm.this.searchAthleteSurnameTextBox.getValue().length() > 20)
                        throw new Exception("Surname is too long");
                    String surname = HomeForm.this.searchAthleteSurnameTextBox.getValue();
                    HomeForm.this.searchAthleteSurnameTextBox.setValue(null);

                    name = VariousFunctions.escapeHtml(name.toLowerCase());
                    surname = VariousFunctions.escapeHtml(surname.toLowerCase());

                    HomeForm.this.getAthlete(name, surname, gender);
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Search Athlete", e.getMessage());
                }
            }
        });
        this.goButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    Long athleteId = Long.valueOf(Long.parseLong(HomeForm.this.athleteIdTextBox.getValue()));
                    String competitionType = HomeForm.this.indoorPerformanceHistoryRadioButton.getValue().booleanValue() ? "Indoor" : "Outdoor";
                    int season = Integer.parseInt(HomeForm.this.seasonProfileListBox.getItemText(HomeForm.this.seasonProfileListBox.getSelectedIndex()));
                    String selectedEvent = HomeForm.this.eventPerformanceHistoryListBox.getValue(HomeForm.this.eventPerformanceHistoryListBox.getSelectedIndex());

                    HomeForm.this.getPerformanceHistoryDetails(athleteId, competitionType, season, selectedEvent);
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Performance History", e.getMessage());
                }
            }
        });
        this.searchAthleteTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Athlete>() {
            long lastClick = -1000L;

            public void onCellPreview(CellPreviewEvent<Athlete> event) { long clictAt = System.currentTimeMillis();
                GWT.log("clickAt: " + clictAt);
                GWT.log("lastClick: " + this.lastClick);
                if (event.getNativeEvent().getType().contains("click")) {
                    GWT.log( String.valueOf(clictAt - this.lastClick));
                    if (clictAt - this.lastClick < 300L) {
                        Athlete athlete = (Athlete)HomeForm.this.selectionModel.getSelectedObject();
                        if (athlete.getName() != "Empty")
                        {
                            HomeForm.this.clearProfileHistoryData();
                            HomeForm.this.getClubName(athlete.getClub());
                            HomeForm.this.athleteIdTextBox.setValue(athlete.getId().toString());
                            HomeForm.this.regionProfileComboBox.addItem(athlete.getRegion().toString());
                            HomeForm.this.genderProfileTextBox.setValue(athlete.getGender().toString());
                            HomeForm.this.nameProfileTextBox.setValue(athlete.getName());
                            HomeForm.this.surnameProfileTextBox.setValue(athlete.getSurname());
                            HomeForm.this.yobProfileTextBox.setValue(Integer.toString(athlete.getYearOfBirth()));
                            HomeForm.this.goButton.setEnabled(true);
                        }
                    }

                    this.lastClick = System.currentTimeMillis();
                }
            }
        });
        this.miscDisplayButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    HomeForm.this.miscDisplayButton.setText("Loading");
                    HomeForm.this.miscDisplayButton.setEnabled(false);
                    Gender gender = HomeForm.this.miscFemaleRadioButton.getValue().booleanValue() ? Gender.Female : Gender.Male;
                    HomeForm.this.getAthlete(gender);
                } catch (Exception e) {
                    HomeForm.this.createMiscSearchAllAthleteTable();
                    HomeForm.this.miscDisplayButton.setEnabled(false);
                    VariousFunctions.errorDialogBox("Display Athletes", e.getMessage());
                }
            }
        });
        this.editAthleteButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    String currentRegion = HomeForm.this.regionProfileComboBox.getValue(HomeForm.this.regionProfileComboBox.getSelectedIndex());
                    HomeForm.this.regionProfileComboBox.clear();
                    String currentClub = HomeForm.this.clubProfileComboBox.getValue(HomeForm.this.clubProfileComboBox.getSelectedIndex());
                    HomeForm.this.clubProfileComboBox.clear();

                    HomeForm.this.regionProfileComboBox.addItem(currentRegion);
                    if (!currentRegion.equalsIgnoreCase(Region.Leinster.toString()))
                        HomeForm.this.regionProfileComboBox.addItem(Region.Leinster.toString());
                    if (!currentRegion.equalsIgnoreCase(Region.Munster.toString()))
                        HomeForm.this.regionProfileComboBox.addItem(Region.Munster.toString());
                    if (!currentRegion.equalsIgnoreCase(Region.Connacht.toString()))
                        HomeForm.this.regionProfileComboBox.addItem(Region.Connacht.toString());
                    if (!currentRegion.equalsIgnoreCase(Region.Ulster.toString())) {
                        HomeForm.this.regionProfileComboBox.addItem(Region.Ulster.toString());
                    }
                    HomeForm.this.clubProfileComboBox.addItem(currentClub);
                    HomeForm.this.loadClub(VariousFunctions.stringToRegion(HomeForm.this.regionProfileComboBox.getItemText(HomeForm.this.regionProfileComboBox.getSelectedIndex())), currentClub);

                    HomeForm.this.nameProfileTextBox.setEnabled(true);
                    HomeForm.this.surnameProfileTextBox.setEnabled(true);
                    HomeForm.this.regionProfileComboBox.setEnabled(true);
                    HomeForm.this.clubProfileComboBox.setEnabled(true);
                    HomeForm.this.yobProfileTextBox.setEnabled(true);
                    HomeForm.this.btnSubmit.setEnabled(true);
                    HomeForm.this.editAthleteButton.setEnabled(false);
                    HomeForm.this.btnCancel.setEnabled(true);
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Edit Profile", e.getMessage());
                }
            }
        });
        this.btnSubmit.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event) {
                try {
                    if (VariousFunctions.isNull(HomeForm.this.nameProfileTextBox.getText()))
                        throw new Exception("Name is null or Invalid!");
                    if (HomeForm.this.nameProfileTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    if (VariousFunctions.isNull(HomeForm.this.surnameProfileTextBox.getText()))
                        throw new Exception("Surname is null or Invalid!");
                    if (HomeForm.this.surnameProfileTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    if (VariousFunctions.isNull(HomeForm.this.yobProfileTextBox.getText()))
                        throw new Exception("Year of Birth is null or Invalid \n Use 0 if unknown!");
                    if (Integer.parseInt(HomeForm.this.yobProfileTextBox.getText()) < 0) {
                        throw new Exception("Invalid year of birth!");
                    }

                    String clubName = HomeForm.this.clubProfileComboBox.getItemText(HomeForm.this.clubProfileComboBox.getSelectedIndex());
                    if (clubName.equals("No Clubs")) {
                        throw new Exception("Please add a club to the list via the Add Club menu item.");
                    }
                    Gender gender = HomeForm.this.genderProfileTextBox.getText() == "Male" ? Gender.Male : Gender.Female;

                    Region region = VariousFunctions.stringToRegion(HomeForm.this.regionProfileComboBox.getItemText(HomeForm.this.regionProfileComboBox.getSelectedIndex()));

                    boolean windowsBox = Window.confirm(
                            "Gender: " + gender + "\n" +
                                    "Name: " + HomeForm.this.nameProfileTextBox.getText() + "\n" +
                                    "Surname: " + HomeForm.this.surnameProfileTextBox.getText() + "\n" +
                                    "Year of Birth: " + HomeForm.this.yobProfileTextBox.getText() + "\n" +
                                    "Region: " + region.toString() + "\n" +
                                    "Club Name: " + clubName);

                    if (windowsBox) {
                        Athlete athlete = new Athlete();
                        athlete.setId(Long.valueOf(Long.parseLong(HomeForm.this.athleteIdTextBox.getText())));
                        athlete.setGender(gender);
                        athlete.setName(VariousFunctions.escapeHtml(HomeForm.this.nameProfileTextBox.getText()));
                        athlete.setSurname(VariousFunctions.escapeHtml(HomeForm.this.surnameProfileTextBox.getText()));
                        athlete.setYearOfBirth(Integer.parseInt(HomeForm.this.yobProfileTextBox.getText()));
                        athlete.setRegion(region);
                        HomeForm.this.updateAthlete(athlete, VariousFunctions.escapeHtml(clubName));
                        HomeForm.this.clearProfileHistoryData();
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Edit Profile", e.getMessage());
                }
            }
        });
        this.btnCancel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                HomeForm.this.clearProfileHistoryData();
            }
        });
    }

    private void clearProfileHistoryData() {
        this.athleteIdTextBox.setValue(null);
        this.nameProfileTextBox.setValue(null);
        this.surnameProfileTextBox.setValue(null);
        this.yobProfileTextBox.setValue(null);
        this.clubProfileComboBox.clear();
        this.genderProfileTextBox.setValue(null);
        this.regionProfileComboBox.clear();

        List<PerformanceHistory> performanceHistory = Arrays.asList(new PerformanceHistory[] { new PerformanceHistory(
                "Empty", "Empty", "Empty") });
        this.athletePerformanceHistoryTable.setRowCount(performanceHistory.size(), true);
        this.athletePerformanceHistoryTable.setRowData(0, performanceHistory);
        List<Athlete> athlete = Arrays.asList(new Athlete[] { new Athlete(Gender.Male, "Empty", "Empty", Region.Leinster, Long.valueOf(Long.parseLong("0"))) });
        this.searchAthleteTable.setRowCount(athlete.size(), true);
        this.searchAthleteTable.setRowData(0, athlete);
        this.nameProfileTextBox.setEnabled(false);
        this.surnameProfileTextBox.setEnabled(false);
        this.regionProfileComboBox.setEnabled(false);
        this.clubProfileComboBox.setEnabled(false);
        this.yobProfileTextBox.setEnabled(false);
        this.btnSubmit.setEnabled(false);
        this.btnCancel.setEnabled(false);
        if (this.layout.isLogin())
            this.editAthleteButton.setEnabled(true);
        else
            this.editAthleteButton.setEnabled(false);
    }

    private void updateAthlete(Athlete athlete, String clubName) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Update Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Update Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Update Athlete", e.getMessage());
                }
            }
            public void onSuccess(String result) { VariousFunctions.informationDialogBox("Update Athlete", result); }

        };
        IrishSportAnalyserService.updateAthlete(athlete, clubName,callback);
    }

    private void loadClub(Region region, final String currentClub) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Club>> callback = new AsyncCallback<List<Club>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Upload Club List", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Upload Club List", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Upload Club List", e.getMessage());
                }
            }

            public void onSuccess(List<Club> result)
            {
                try {
                    if (result == null)
                        HomeForm.this.clubProfileComboBox.addItem("No Clubs");
                    else
                        for (Club club : result)
                            if (!club.getName().equalsIgnoreCase(currentClub))
                                HomeForm.this.clubProfileComboBox.addItem(club.getName().replace("&amp;", "&"));
                }
                catch (Exception e)
                {
                    VariousFunctions.errorDialogBox("Upload Club List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getClub(region, callback);
    }

    private void clearProfileHistory() {
        this.clubProfileComboBox.clear();
        this.athleteIdTextBox.setValue(null);
        this.regionProfileComboBox.clear();
        this.genderProfileTextBox.setValue(null);
        this.nameProfileTextBox.setValue(null);
        this.surnameProfileTextBox.setValue(null);
        this.yobProfileTextBox.setValue(null);
        this.goButton.setEnabled(false);
    }

    private void createProfileSearchTable() {
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setStyleName("panelBorder");
        this.searchAthletePanel.add(scrollPanel, 0, 188);
        scrollPanel.setSize("314px", "159px");

        this.searchAthleteTable = new CellTable<Athlete>();
        scrollPanel.setWidget(this.searchAthleteTable);
        this.searchAthleteTable.setSize("312px", "55px");
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

        this.selectionModel = new SingleSelectionModel<Athlete>();
        this.searchAthleteTable.setSelectionModel(this.selectionModel);

        List<Athlete> athlete = Arrays.asList(new Athlete[] { new Athlete(Gender.Male, "Empty", "Empty", Region.Leinster, Long.valueOf(Long.parseLong("0"))) });
        this.searchAthleteTable.setRowCount(athlete.size(), true);
        this.searchAthleteTable.setRowData(0, athlete);
    }

    private void createPerformanceHistoryTable() {
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setStyleName("panelBorder");
        this.competitionHistoryPanel.add(scrollPanel, 10, 58);
        scrollPanel.setSize("592px", "167px");

        this.athletePerformanceHistoryTable = new CellTable<PerformanceHistory>();
        scrollPanel.setWidget(this.athletePerformanceHistoryTable);
        this.athletePerformanceHistoryTable.setSize("100%", "51px");
        this.athletePerformanceHistoryTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        TextColumn<PerformanceHistory> performanceColumn = new TextColumn<PerformanceHistory>() {
            public String getValue(PerformanceHistory ph) {
                return ph.getPerformance();
            }
        };
        performanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.athletePerformanceHistoryTable.addColumn(performanceColumn, "Performance");
        this.athletePerformanceHistoryTable.setColumnWidth(performanceColumn, "97px");

        TextColumn<PerformanceHistory> competitionColumn = new TextColumn<PerformanceHistory>() {
            public String getValue(PerformanceHistory ph) {
                return ph.getCompetition();
            }
        };
        competitionColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.athletePerformanceHistoryTable.addColumn(competitionColumn, "Competition");
        this.athletePerformanceHistoryTable.setColumnWidth(competitionColumn, "175px");

        TextColumn<PerformanceHistory> venueColumn = new TextColumn<PerformanceHistory>() {
            public String getValue(PerformanceHistory ph) {
                return ph.getVenue();
            }
        };
        venueColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.athletePerformanceHistoryTable.addColumn(venueColumn, "Venue");
        this.athletePerformanceHistoryTable.setColumnWidth(venueColumn, "110px");

        TextColumn<PerformanceHistory> dateColumn = new TextColumn<PerformanceHistory>() {
            public String getValue(PerformanceHistory ph) {
                return ph.getDate();
            }
        };
        dateColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.athletePerformanceHistoryTable.addColumn(dateColumn, "Date");
        this.athletePerformanceHistoryTable.setColumnWidth(dateColumn, "154px");

        SingleSelectionModel<PerformanceHistory> selectionModelPHistory = new SingleSelectionModel<PerformanceHistory>();
        this.athletePerformanceHistoryTable.setSelectionModel(selectionModelPHistory);

        List<PerformanceHistory> performanceHistory = Arrays.asList(new PerformanceHistory[] { new PerformanceHistory(
                "Empty", "Empty", "Empty") });
        this.athletePerformanceHistoryTable.setRowCount(performanceHistory.size(), true);
        this.athletePerformanceHistoryTable.setRowData(0, performanceHistory);
    }

    private void createMiscSearchAllAthleteTable() {
        ScrollPanel scrollPanel1 = new ScrollPanel();
        scrollPanel1.setStyleName("panelBorder");
        this.miscAbsolutePanel.add(scrollPanel1, 0, 90);
        scrollPanel1.setSize("313px", "255px");

        this.miscAthleteTable = new CellTable<Athlete>();
        this.miscAthleteTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        scrollPanel1.setWidget(this.miscAthleteTable);
        this.miscAthleteTable.setSize("313px", "50px");

        Column<Athlete,Number> column = new Column<Athlete,Number>(new NumberCell()) {
            public Number getValue(Athlete athlete) {
                return athlete.getId();
            }
        };
        this.miscAthleteTable.addColumn(column, "ID");
        this.miscAthleteTable.setColumnWidth(column, "90px");

        TextColumn<Athlete> textColumn = new TextColumn<Athlete>() {
            public String getValue(Athlete athlete) {
                return athlete.getName();
            }
        };
        this.miscAthleteTable.addColumn(textColumn, "Name");
        this.miscAthleteTable.setColumnWidth(textColumn, "95px");

        TextColumn<Athlete> textColumn_1 = new TextColumn<Athlete>() {
            public String getValue(Athlete athlete) {
                return athlete.getSurname();
            }
        };
        this.miscAthleteTable.addColumn(textColumn_1, "Surname");

        SingleSelectionModel<Athlete> miscSelectionModel = new SingleSelectionModel<Athlete>();
        this.miscAthleteTable.setSelectionModel(miscSelectionModel);

        List<Athlete> athlete = Arrays.asList(new Athlete[]{new Athlete(Gender.Male, "Empty", "Empty", Region.Leinster, Long.valueOf(Long.parseLong("0")))});
        this.miscAthleteTable.setRowCount(athlete.size(), true);
        this.miscAthleteTable.setRowData(0, athlete);
    }

    private void createRankTable() {
        this.leaderboardRankTable = new CellTable<List<String>>();
        this.scrollPanel.setWidget(this.leaderboardRankTable);
        this.leaderboardRankTable.setSize("100%", "53px");
        this.leaderboardRankTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        Column<List<String>,Number> rankColumn = new Column<List<String>,Number>(new NumberCell()) {
            public Number getValue(List<String> object) {
                return Integer.valueOf(Integer.parseInt((String)object.get(0)));
            }
        };
        rankColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(rankColumn, "Rank");
        this.leaderboardRankTable.setColumnWidth(rankColumn, "64px");

        TextColumn<List<String>> performanceColumn = new TextColumn<List<String>>() {
            public String getValue(List<String> object) {
                return (String)object.get(1);
            }
        };
        performanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(performanceColumn, "Perf.");
        this.leaderboardRankTable.setColumnWidth(performanceColumn, "80px");

        TextColumn<List<String>> nameColumn = new TextColumn<List<String>>() {
            public String getValue(List<String> object) {
                return (String)object.get(2);
            }
        };
        nameColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(nameColumn, "Name");
        this.leaderboardRankTable.setColumnWidth(nameColumn, "96px");

        TextColumn<List<String>> surnameColumn = new TextColumn<List<String>>()
        {
            public String getValue(List<String> object) {
                return (String)object.get(3);
            }
        };
        surnameColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(surnameColumn, "Surname");
        this.leaderboardRankTable.setColumnWidth(surnameColumn, "97px");

        TextColumn<List<String>> dateColumn = new TextColumn<List<String>>() {
            public String getValue(List<String> object) {
                return (String)object.get(4);
            }
        };
        dateColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(dateColumn, "Date");
        this.leaderboardRankTable.setColumnWidth(dateColumn, "147px");

        TextColumn<List<String>> venueColumn = new TextColumn<List<String>>() {
            public String getValue(List<String> object) {
                return (String)object.get(5);
            }
        };
        venueColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(venueColumn, "Venue");

        TextColumn<List<String>> competitionColumn = new TextColumn<List<String>>() {
            public String getValue(List<String> object) {
                return (String)object.get(6);
            }
        };
        competitionColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        this.leaderboardRankTable.addColumn(competitionColumn, "Competition");

        SingleSelectionModel<List<String>> selectionModel = new SingleSelectionModel<List<String>> ();
        this.leaderboardRankTable.setSelectionModel(selectionModel);

        List<String> data = new ArrayList<String>(7);
        data.add("0");
        data.add("Empty");
        data.add("Empty");
        data.add("Empty");
        data.add("Empty");
        data.add("Empty");
        data.add("Empty");

        List<List<String>> leaderboard = new ArrayList<List<String>>(1);
        leaderboard.add(data);

        this.leaderboardRankTable.setRowCount(leaderboard.size(), true);
        this.leaderboardRankTable.setRowData(0, leaderboard);
    }

    private void getAthlete(Gender gender) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Athlete>> callback = new AsyncCallback<List<Athlete>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                } finally {
                    HomeForm.this.clearProfileHistory();
                    HomeForm.this.miscDisplayButton.setText("Display");
                    HomeForm.this.miscDisplayButton.setEnabled(true);
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

                        HomeForm.this.miscAthleteTable.setRowCount(athletes.size(), true);
                        HomeForm.this.miscAthleteTable.setRowData(0, athletes);
                    }
                    else {
                        VariousFunctions.informationDialogBox("Get Athletes", "No Athlete Found!");
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                } finally {
                    HomeForm.this.miscDisplayButton.setText("Display");
                    HomeForm.this.miscDisplayButton.setEnabled(true);
                }
            }
        };
        IrishSportAnalyserService.getAthlete(gender,callback);
    }

    private void getAthlete(Long id) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Athlete>> callback = new AsyncCallback<List<Athlete>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                } finally {
                    HomeForm.this.clearProfileHistory();
                }
            }

            public void onSuccess(List<Athlete> athletes) {
                try {
                    if (athletes != null) {
                        Athlete athlete = athletes.get(0);

                        athlete.setName(athlete.getName().substring(0, 1).toUpperCase()
                                .concat(athlete.getName().substring(1).toLowerCase()));
                        athlete.setSurname(athlete.getSurname().substring(0, 1).toUpperCase()
                                .concat(athlete.getSurname().substring(1).toLowerCase()));

                        HomeForm.this.getClubName(athlete.getClub());
                        HomeForm.this.athleteIdTextBox.setValue(athlete.getId().toString());
                        HomeForm.this.regionProfileComboBox.addItem(athlete.getRegion().toString());
                        HomeForm.this.genderProfileTextBox.setValue(athlete.getGender().toString());
                        HomeForm.this.nameProfileTextBox.setValue(athlete.getName());
                        HomeForm.this.surnameProfileTextBox.setValue(athlete.getSurname());
                        HomeForm.this.yobProfileTextBox.setValue(Integer.toString(athlete.getYearOfBirth()));
                        HomeForm.this.goButton.setEnabled(true);
                    }
                    else {
                        HomeForm.this.clearProfileHistory();
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
        AsyncCallback<List<Athlete>> callback = new AsyncCallback<List<Athlete>>() {
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

                        HomeForm.this.searchAthleteTable.setRowCount(athletes.size(), true);
                        HomeForm.this.searchAthleteTable.setRowData(0, athletes);
                    } else {
                        VariousFunctions.informationDialogBox("Get Athlete", "No Athlete Found!");
                    }
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Athlete", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getAthlete(name, surname, gender,callback);
    }

    private void getPerformanceHistory(Result result) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<PerformanceHistory>> callback = new AsyncCallback<List<PerformanceHistory>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    if (HomeForm.this.workCount == HomeForm.this.totalWork)
                        VariousFunctions.errorDialogBox("Get Performance", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    if (HomeForm.this.workCount == HomeForm.this.totalWork)
                        VariousFunctions.errorDialogBox("Get Performance", "InvocationException!");
                } catch (Throwable e) {
                    if (HomeForm.this.workCount == HomeForm.this.totalWork)
                        VariousFunctions.errorDialogBox("Get Performance", e.getMessage());
                }
            }

            public void onSuccess(List<PerformanceHistory> performanceHistory) {
                try {
                    HomeForm.this.totalPerformanceHistoryData.add((PerformanceHistory)performanceHistory.get(0));
                    HomeForm.this.workCount += 1;

                    if (HomeForm.this.workCount == HomeForm.this.totalWork) {
                        HomeForm.this.athletePerformanceHistoryTable.setRowCount(HomeForm.this.totalPerformanceHistoryData.size(), true);
                        HomeForm.this.athletePerformanceHistoryTable.setRowData(0, HomeForm.this.totalPerformanceHistoryData);
                    }
                } catch (Exception e) {
                    if (HomeForm.this.workCount == HomeForm.this.totalWork)
                        VariousFunctions.errorDialogBox("Get Performance", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getPerformanceHistory(result,callback);
    }

    private void getPerformanceHistoryDetails(Long athleteId, String competitionType, int season, String event) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Result>> callback = new AsyncCallback<List<Result>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Performance History - Details", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Performance History - Details", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Performance History - Details", e.getMessage());
                }
            }

            public void onSuccess(List<Result> performanceHistory)
            {
                try {
                    HomeForm.this.totalPerformanceHistoryData = new ArrayList<PerformanceHistory>(performanceHistory.size());
                    HomeForm.this.totalWork = performanceHistory.size();
                    HomeForm.this.workCount = 0;

                    if (performanceHistory.size() != 0) {
                        for (Result result : performanceHistory) {
                            HomeForm.this.getPerformanceHistory(result);
                        }
                    }
                    else
                        VariousFunctions.informationDialogBox("Get Performance History", "No History Found!");
                }
                catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Performance History", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getCompetitionID(athleteId, competitionType, season, event,callback);
    }

    private void getClubName(Long id) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Club Name", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Club Name", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Club Name", e.getMessage());
                }
            }

            public void onSuccess(String clubName) {
                try {
                    HomeForm.this.clubProfileComboBox.addItem(clubName);
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Club Name", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getClubName(id,callback);
    }

    private void getSeasons(String competitionType) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = (IrishSportAnalyserServiceAsync)GWT.create(IrishSportAnalyserService.class);
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
                    HomeForm.this.leaderboardSeasonComboBox.clear();
                    HomeForm.this.seasonProfileListBox.clear();
                    if (result == null)
                        HomeForm.this.leaderboardSeasonComboBox.addItem("No Competitions");
                    else
                        for (String season : result) {
                            HomeForm.this.leaderboardEnterButton.setEnabled(true);
                            HomeForm.this.leaderboardSeasonComboBox.addItem("Select Season");
                            HomeForm.this.leaderboardSeasonComboBox.addItem(season);
                            HomeForm.this.seasonProfileListBox.addItem("Select Season");
                            HomeForm.this.seasonProfileListBox.addItem(season);
                        }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Season List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getSeasons(competitionType,callback);
    }

    void getLeaderboardResults(String competitionType, final Region region, Gender gender, final String event, int season, final String ageGroupQuery) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<Result>> callback = new AsyncCallback<List<Result>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Ranking", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Ranking", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Ranking", e.getMessage());
                }
            }

            public void onSuccess(List<Result> allSeasonsBest) {
                try {
                    HomeForm.this.athleteCount = 0;
                    HomeForm.this.seasonBestCount = 0;
                    HomeForm.this.seasonBestCurrentCount = 0;
                    if (allSeasonsBest != null) {
                        HomeForm.this.seasonBestCount = allSeasonsBest.size();
                        HomeForm.this.rankedAthletes = new ArrayList<List<String>>();
                        for (Result result : allSeasonsBest)
                            HomeForm.this.getAthlete(result, region, ageGroupQuery, event);
                    }
                    else {
                        VariousFunctions.informationDialogBox("Get Leaderboard Ranking", "No Results Found!");
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Ranking", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getSeasonsBest(competitionType, gender, event, season,callback);
    }

    private void getAthlete(Result result, Region region, String ageGroupQuery, final String event) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Athlete", e.getMessage());
                }
            }

            public void onSuccess(List<String> result) {
                try {
                    HomeForm.this.seasonBestCurrentCount += 1;
                    if (result != null) {
                        HomeForm.this.rankedAthletes.add(result);
                        if (HomeForm.this.seasonBestCurrentCount == HomeForm.this.seasonBestCount) {
                            HomeForm.this.finalRankingList = new ArrayList<List<String>>(HomeForm.this.rankedAthletes.size());
                            for (List<String> rankedAthlete : HomeForm.this.rankedAthletes)
                            {
                                rankedAthlete.set(2, ((String)rankedAthlete.get(2)).substring(0, 1).toUpperCase()
                                        .concat(((String)rankedAthlete.get(2)).substring(1).toLowerCase()));
                                rankedAthlete.set(3, ((String)rankedAthlete.get(3)).substring(0, 1).toUpperCase()
                                        .concat(((String)rankedAthlete.get(3)).substring(1).toLowerCase()));
                                HomeForm.this.getRankCompetitionDetails(rankedAthlete, event);
                            }
                        }
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Athlete", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getAthlete(result, region, ageGroupQuery,callback);
    }

    private void getRankCompetitionDetails(List<String> rankedAthletes, final String event) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
            public void onFailure(Throwable caught) {
                try {
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Competition Details", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Competition Details", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Competition Details", e.getMessage());
                }
            }

            public void onSuccess(List<String> rankedAthletes) {
                try {
                    HomeForm.this.athleteCount += 1;
                    HomeForm.this.finalRankingList.add(rankedAthletes);
                    if (HomeForm.this.athleteCount == HomeForm.this.finalRankingList.size()) {
                        HomeForm.this.finalRankingList = VariousFunctions.CollectionSort(HomeForm.this.finalRankingList, event);

                        HomeForm.this.leaderboardRankTable.setRowCount(HomeForm.this.finalRankingList.size(), true);
                        HomeForm.this.leaderboardRankTable.setRowData(0, HomeForm.this.finalRankingList);
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Get Leaderboard Rank - Get Competition Details", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getRankCompetitionDetails(rankedAthletes,callback);
    }

    private void homeFormLeaderboardDesign() {
        this.mainTabPanel = new TabPanel();
        this.rootPanel.add(this.mainTabPanel, 0, 148);
        this.mainTabPanel.setSize("974px", "483px");

        LayoutPanel leaderboardLayoutPanel = new LayoutPanel();
        leaderboardLayoutPanel.setStyleName("background");
        this.mainTabPanel.add(leaderboardLayoutPanel, "Leaderboard", false);
        leaderboardLayoutPanel.setSize("956px", "370px");

        VerticalPanel queryVerticalPanel = new VerticalPanel();
        queryVerticalPanel.setStyleName("panelBorder");
        queryVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        queryVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        Label lblNewLabel = new Label("Select a Competition Type:");
        queryVerticalPanel.add(lblNewLabel);
        lblNewLabel.setSize("192px", "20px");

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        queryVerticalPanel.add(horizontalPanel);

        this.leaderboardIndoorRadioButton = new RadioButton("competitionLeaderboard", "Indoor");
        this.leaderboardIndoorRadioButton.setValue(Boolean.valueOf(true));
        horizontalPanel.add(this.leaderboardIndoorRadioButton);
        this.leaderboardIndoorRadioButton.setSize("64", "18");
        this.leaderboardOutdoorRadioButton = new RadioButton("competitionLeaderboard", "Outdoor");

        horizontalPanel.add(this.leaderboardOutdoorRadioButton);
        this.leaderboardOutdoorRadioButton.setSize("64", "18");

        Label lblSelectGender = new Label("Select Gender:");
        queryVerticalPanel.add(lblSelectGender);
        lblSelectGender.setSize("192px", "20px");

        HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
        queryVerticalPanel.add(horizontalPanel_1);

        this.leaderboardMaleRadioButton = new RadioButton("gender", "Male");
        this.leaderboardMaleRadioButton.setValue(Boolean.valueOf(true));
        horizontalPanel_1.add(this.leaderboardMaleRadioButton);
        RadioButton leaderboardFemaleRadioButton = new RadioButton("gender", "Female");
        horizontalPanel_1.add(leaderboardFemaleRadioButton);

        Label lblSeason = new Label("Season:");
        queryVerticalPanel.add(lblSeason);
        lblSeason.setSize("192px", "20px");

        this.leaderboardSeasonComboBox = new ListBox();
        this.leaderboardSeasonComboBox.addItem("Loading....");
        queryVerticalPanel.add(this.leaderboardSeasonComboBox);
        this.leaderboardSeasonComboBox.setWidth("158px");

        Label lblRegion = new Label("Region:");
        queryVerticalPanel.add(lblRegion);
        lblRegion.setSize("192px", "20px");

        this.leaderboardRegionComboBox = new ListBox();
        this.leaderboardRegionComboBox.addItem(Region.All_Ireland.toString());
        this.leaderboardRegionComboBox.addItem(Region.Leinster.toString());
        this.leaderboardRegionComboBox.addItem(Region.Munster.toString());
        this.leaderboardRegionComboBox.addItem(Region.Connacht.toString());
        this.leaderboardRegionComboBox.addItem(Region.Ulster.toString());
        queryVerticalPanel.add(this.leaderboardRegionComboBox);
        this.leaderboardRegionComboBox.setWidth("100px");

        Label lblGroup = new Label("Age Group:");
        queryVerticalPanel.add(lblGroup);
        lblGroup.setSize("192px", "20px");

        this.leaderboardAgeComboBox = new ListBox();
        VariousFunctions.ageItems(this.leaderboardAgeComboBox);
        queryVerticalPanel.add(this.leaderboardAgeComboBox);
        this.leaderboardAgeComboBox.setWidth("100px");

        Label lblEvent = new Label("Event:");
        queryVerticalPanel.add(lblEvent);
        lblEvent.setSize("192px", "20px");

        this.leaderboardEventventComboBox = new ListBox();
        VariousFunctions.eventItem(this.leaderboardEventventComboBox, "Indoor");
        queryVerticalPanel.add(this.leaderboardEventventComboBox);
        this.leaderboardEventventComboBox.setWidth("100px");

        this.leaderboardEnterButton = new Button("Enter");
        this.leaderboardEnterButton.setEnabled(false);
        this.leaderboardEnterButton.setText("Enter");
        queryVerticalPanel.add(this.leaderboardEnterButton);
        this.leaderboardEnterButton.setWidth("97px");

        this.scrollPanel = new ScrollPanel();
        this.scrollPanel.setStyleName("panelBorder");

        leaderboardLayoutPanel.add(queryVerticalPanel);
        leaderboardLayoutPanel.setWidgetLeftWidth(queryVerticalPanel, 10.0D, Style.Unit.PX, 197.0D, Style.Unit.PX);
        leaderboardLayoutPanel.setWidgetTopHeight(queryVerticalPanel, 45.0D, Style.Unit.PX, 290.0D, Style.Unit.PX);
        leaderboardLayoutPanel.add(this.scrollPanel);
        leaderboardLayoutPanel.setWidgetLeftWidth(this.scrollPanel, 208.0D, Style.Unit.PX, 748.0D, Style.Unit.PX);
        leaderboardLayoutPanel.setWidgetTopHeight(this.scrollPanel, 0.0D, Style.Unit.PX, 370.0D, Style.Unit.PX);

        createRankTable();
        this.mainTabPanel.selectTab(0);
    }

    private void homeFormMiscellaneousDesign() {
        LayoutPanel miscellaneousLayoutPanel = new LayoutPanel();
        this.mainTabPanel.add(miscellaneousLayoutPanel, "Miscellaneous", false);
        miscellaneousLayoutPanel.setSize("956px", "386px");

        this.miscAbsolutePanel = new AbsolutePanel();
        this.miscAbsolutePanel.setStyleName("panelBorder");

        Label lblDisplayAllAthlete = new Label("Display All Athlete");
        lblDisplayAllAthlete.setStyleName("topLabel");
        lblDisplayAllAthlete.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.miscAbsolutePanel.add(lblDisplayAllAthlete, 0, 0);
        lblDisplayAllAthlete.setSize("315px", "20px");

        RadioButton miscMaleRadioButton = new RadioButton("miscGenderType", "Indoor");
        miscMaleRadioButton.setValue(Boolean.valueOf(true));
        miscMaleRadioButton.setHTML("Male");
        this.miscAbsolutePanel.add(miscMaleRadioButton, 84, 25);
        miscMaleRadioButton.setWidth("72px");

        this.miscFemaleRadioButton = new RadioButton("miscGenderType", "Outdoor");
        this.miscFemaleRadioButton.setHTML("Female");
        this.miscAbsolutePanel.add(this.miscFemaleRadioButton, 162, 25);

        this.miscDisplayButton = new Button("Search");
        this.miscDisplayButton.setText("Display");
        this.miscAbsolutePanel.add(this.miscDisplayButton, 126, 52);
        this.miscDisplayButton.setSize("67px", "30px");

        AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("panelBorder");

        Label lblNewLabel_1 = new Label("Irish Sport Ananlyser");
        lblNewLabel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        lblNewLabel_1.setStyleName("topLabel");
        absolutePanel.add(lblNewLabel_1, 0, 0);
        lblNewLabel_1.setSize("443px", "23px");

        HTML htmlNewHtml = new HTML("Version: 1 Build: Release (15/06/2012) - 2 <br>GWT SDK: 2.4.0  <br>App Engine SDK: 1.6.5  <br>JRE System Library: JRE6\r\n<br>\r\n<br>Irish Sport Analyser is for athletes, coaches and fans, to provide a clear indicator of Irish Athletes, Athletics Performance.  The aim is to help coaches and athletes gauge how well they are performing against other athletes.\r\n<br>\r\n<br>The ranking is based on compilations of athlete’s performances from various competitions.  It provides user with the functions to view the top ranked athletes in the Country and their performance history.\r\n<br>\r\n<br>The application is powered by the Google App Engine and designed using Google Web Toolkit.\r\n", true);
        htmlNewHtml.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        absolutePanel.add(htmlNewHtml, 0, 25);

        Image image = new Image("appengine-silver-120x30.gif");
        absolutePanel.add(image, 166, 319);
        image.setSize("120px", "30px");

        miscellaneousLayoutPanel.add(absolutePanel);
        miscellaneousLayoutPanel.setWidgetLeftWidth(absolutePanel, 439.0D, Style.Unit.PX, 445.0D, Style.Unit.PX);
        miscellaneousLayoutPanel.setWidgetTopHeight(absolutePanel, 19.0D, Style.Unit.PX, 351.0D, Style.Unit.PX);

        createMiscSearchAllAthleteTable();
        miscellaneousLayoutPanel.add(this.miscAbsolutePanel);
        this.miscAbsolutePanel.setSize("314px", "347px");
        miscellaneousLayoutPanel.setWidgetLeftWidth(this.miscAbsolutePanel, 106.0D, Style.Unit.PX, 318.0D, Style.Unit.PX);
        miscellaneousLayoutPanel.setWidgetTopHeight(this.miscAbsolutePanel, 19.0D, Style.Unit.PX, 358.0D, Style.Unit.PX);
    }

    private void homeFormProfileDesign() {
        LayoutPanel profileLayoutPanel = new LayoutPanel();
        this.mainTabPanel.add(profileLayoutPanel, "Profile", false);
        profileLayoutPanel.setSize("958px", "433px");

        AbsolutePanel fullDetailsPanel = new AbsolutePanel();
        fullDetailsPanel.setStyleName("panelBorder");

        VerticalPanel verticalPanel_1 = new VerticalPanel();
        fullDetailsPanel.add(verticalPanel_1);
        verticalPanel_1.setSize("547px", "165px");

        Label lblFullDetails = new Label("Full Details");
        lblFullDetails.setStyleName("topLabel");
        lblFullDetails.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel_1.add(lblFullDetails);

        HorizontalPanel horizontalPanel_6 = new HorizontalPanel();
        verticalPanel_1.add(horizontalPanel_6);
        horizontalPanel_6.setWidth("547px");

        Label lblAthleteId_1 = new Label("Athlete ID:");
        lblAthleteId_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_6.add(lblAthleteId_1);
        lblAthleteId_1.setSize("90px", "");

        this.athleteIdTextBox = new TextBox();
        this.athleteIdTextBox.setEnabled(false);
        horizontalPanel_6.add(this.athleteIdTextBox);
        this.athleteIdTextBox.setSize("150px", "");

        Label lblClub = new Label("Region:");
        lblClub.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_6.add(lblClub);
        lblClub.setWidth("80px");

        this.regionProfileComboBox = new ListBox();
        this.regionProfileComboBox.setEnabled(false);
        horizontalPanel_6.add(this.regionProfileComboBox);
        this.regionProfileComboBox.setWidth("140px");

        HorizontalPanel horizontalPanel_7 = new HorizontalPanel();
        verticalPanel_1.add(horizontalPanel_7);
        horizontalPanel_7.setWidth("547px");

        Label lblGender = new Label("Gender:");
        lblGender.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_7.add(lblGender);
        lblGender.setSize("90px", "");

        this.genderProfileTextBox = new TextBox();
        this.genderProfileTextBox.setEnabled(false);
        horizontalPanel_7.add(this.genderProfileTextBox);
        this.genderProfileTextBox.setSize("150px", "");

        Label label_1 = new Label("Club:");
        label_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_7.add(label_1);
        label_1.setSize("80px", "");

        this.clubProfileComboBox = new ListBox();
        this.clubProfileComboBox.setEnabled(false);
        horizontalPanel_7.add(this.clubProfileComboBox);
        this.clubProfileComboBox.setWidth("140px");

        HorizontalPanel horizontalPanel_8 = new HorizontalPanel();
        verticalPanel_1.add(horizontalPanel_8);
        horizontalPanel_8.setWidth("547px");

        Label lblName = new Label("Name:");
        lblName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_8.add(lblName);
        lblName.setWidth("90px");

        this.nameProfileTextBox = new TextBox();
        this.nameProfileTextBox.setEnabled(false);
        horizontalPanel_8.add(this.nameProfileTextBox);
        this.nameProfileTextBox.setSize("150px", "");

        Label lblYearOfBirth = new Label("Year of Birth:");
        lblYearOfBirth.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_8.add(lblYearOfBirth);
        lblYearOfBirth.setWidth("80px");

        this.yobProfileTextBox = new TextBox();
        this.yobProfileTextBox.setEnabled(false);
        horizontalPanel_8.add(this.yobProfileTextBox);
        this.yobProfileTextBox.setSize("135px", "");

        HorizontalPanel horizontalPanel_9 = new HorizontalPanel();
        verticalPanel_1.add(horizontalPanel_9);
        horizontalPanel_9.setWidth("547px");

        Label lblSurname_1 = new Label("Surname:");
        lblSurname_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        horizontalPanel_9.add(lblSurname_1);
        lblSurname_1.setWidth("90px");

        this.surnameProfileTextBox = new TextBox();
        this.surnameProfileTextBox.setEnabled(false);
        horizontalPanel_9.add(this.surnameProfileTextBox);
        this.surnameProfileTextBox.setSize("150px", "");

        this.editAthleteButton = new Button("Edit");

        this.editAthleteButton.setEnabled(false);
        horizontalPanel_9.add(this.editAthleteButton);
        this.editAthleteButton.setWidth("71px");

        this.btnCancel = new Button("Cancel");
        this.btnCancel.setEnabled(false);
        horizontalPanel_9.add(this.btnCancel);
        this.btnCancel.setWidth("60px");

        this.btnSubmit = new Button("Submit");
        this.btnSubmit.setEnabled(false);
        horizontalPanel_9.add(this.btnSubmit);
        this.btnSubmit.setWidth("86px");

        this.competitionHistoryPanel = new AbsolutePanel();
        this.competitionHistoryPanel.setStyleName("panelBorder");

        Label lblPerformanceHistory = new Label("Performance History");
        lblPerformanceHistory.setStyleName("topLabel");
        lblPerformanceHistory.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.competitionHistoryPanel.add(lblPerformanceHistory, 0, 0);
        lblPerformanceHistory.setSize("615px", "18px");

        this.indoorPerformanceHistoryRadioButton = new RadioButton("performanceHistoryLeaderboard", "Indoor");
        this.indoorPerformanceHistoryRadioButton.setValue(Boolean.valueOf(true));
        this.competitionHistoryPanel.add(this.indoorPerformanceHistoryRadioButton, 10, 32);
        this.indoorPerformanceHistoryRadioButton.setSize("64", "18");

        this.outdoorPerformanceHistoryRadioButton = new RadioButton("performanceHistoryLeaderboard", "Outdoor");
        this.competitionHistoryPanel.add(this.outdoorPerformanceHistoryRadioButton, 73, 33);
        this.outdoorPerformanceHistoryRadioButton.setSize("87px", "18px");

        Label label = new Label("Season:");
        label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.competitionHistoryPanel.add(label, 165, 36);
        label.setSize("48px", "18px");

        this.seasonProfileListBox = new ListBox();
        this.seasonProfileListBox.addItem("Select season");
        this.competitionHistoryPanel.add(this.seasonProfileListBox, 219, 32);
        this.seasonProfileListBox.setSize("125px", "22px");

        Label label_5 = new Label("Event:");
        label_5.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.competitionHistoryPanel.add(label_5, 359, 35);
        label_5.setSize("56px", "18px");

        this.eventPerformanceHistoryListBox = new ListBox();
        VariousFunctions.eventItem(this.eventPerformanceHistoryListBox, "Indoor");
        this.competitionHistoryPanel.add(this.eventPerformanceHistoryListBox, 421, 32);
        this.eventPerformanceHistoryListBox.setSize("125px", "22px");

        this.goButton = new Button("Edit");
        this.goButton.setText("Go");
        this.goButton.setEnabled(false);
        this.competitionHistoryPanel.add(this.goButton, 566, 24);
        this.goButton.setSize("36px", "30px");

        this.searchAthletePanel = new AbsolutePanel();
        this.searchAthletePanel.setStyleName("panelBorder");
        this.searchAthletePanel.setSize("314px", "347px");

        Label lblSearchAthlete = new Label("Search Athlete");
        lblSearchAthlete.setStyleName("topLabel");
        lblSearchAthlete.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        this.searchAthletePanel.add(lblSearchAthlete, 0, 0);
        lblSearchAthlete.setSize("314px", "18px");

        RadioButton performanceMaleRadioButton = new RadioButton("profileGenderType", "Indoor");
        performanceMaleRadioButton.setValue(Boolean.valueOf(true));
        performanceMaleRadioButton.setHTML("Male");
        this.searchAthletePanel.add(performanceMaleRadioButton, 83, 24);
        performanceMaleRadioButton.setWidth("72px");

        this.performanceFemaleRadioButton = new RadioButton("profileGenderType", "Outdoor");
        this.performanceFemaleRadioButton.setHTML("Female");
        this.searchAthletePanel.add(this.performanceFemaleRadioButton, 161, 24);

        Label label_2 = new Label("Athlete ID:");
        label_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.searchAthletePanel.add(label_2, 0, 58);
        label_2.setSize("77px", "18px");

        this.searchAthleteIdTextBox = new TextBox();
        this.searchAthletePanel.add(this.searchAthleteIdTextBox, 83, 58);
        this.searchAthleteIdTextBox.setSize("138px", "18px");

        this.athleteIdSearchButton = new Button("Search");
        this.athleteIdSearchButton.setText("Search");
        this.searchAthletePanel.add(this.athleteIdSearchButton, 237, 58);
        this.athleteIdSearchButton.setSize("67px", "30px");

        Label label_3 = new Label("First Name:");
        label_3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.searchAthletePanel.add(label_3, 0, 105);
        label_3.setSize("77px", "18px");

        this.searchAthleteNameTextBox = new TextBox();
        this.searchAthletePanel.add(this.searchAthleteNameTextBox, 82, 105);
        this.searchAthleteNameTextBox.setSize("139px", "18px");

        Label label_4 = new Label("Surname:");
        label_4.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        this.searchAthletePanel.add(label_4, 0, 142);
        label_4.setSize("77px", "18px");

        this.searchAthleteSurnameTextBox = new TextBox();
        this.searchAthletePanel.add(this.searchAthleteSurnameTextBox, 82, 142);
        this.searchAthleteSurnameTextBox.setSize("139px", "18px");

        this.athleteNameSurnameSearchButton = new Button("Search");
        this.athleteNameSurnameSearchButton.setText("Search");
        this.searchAthletePanel.add(this.athleteNameSurnameSearchButton, 237, 142);
        this.athleteNameSurnameSearchButton.setSize("67px", "30px");

        profileLayoutPanel.add(fullDetailsPanel);
        profileLayoutPanel.setWidgetLeftWidth(fullDetailsPanel, 361.0D, Style.Unit.PX, 549.0D, Style.Unit.PX);
        profileLayoutPanel.setWidgetTopHeight(fullDetailsPanel, 0.0D, Style.Unit.PX, 178.0D, Style.Unit.PX);

        createPerformanceHistoryTable();
        profileLayoutPanel.add(this.competitionHistoryPanel);
        profileLayoutPanel.setWidgetLeftWidth(this.competitionHistoryPanel, 328.0D, Style.Unit.PX, 615.0D, Style.Unit.PX);
        profileLayoutPanel.setWidgetTopHeight(this.competitionHistoryPanel, 194.0D, Style.Unit.PX, 239.0D, Style.Unit.PX);

        createProfileSearchTable();
        profileLayoutPanel.add(this.searchAthletePanel);
        profileLayoutPanel.setWidgetLeftWidth(this.searchAthletePanel, 6.0D, Style.Unit.PX, 318.0D, Style.Unit.PX);
        profileLayoutPanel.setWidgetTopHeight(this.searchAthletePanel, 49.0D, Style.Unit.PX, 356.0D, Style.Unit.PX);
        if (this.layout.isLogin())
            this.editAthleteButton.setEnabled(true);
    }
}
