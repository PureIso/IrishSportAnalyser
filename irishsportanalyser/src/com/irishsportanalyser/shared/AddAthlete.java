package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
import com.irishsportanalyser.entities.Athlete;
import com.irishsportanalyser.entities.Club;
import com.irishsportanalyser.enums.Gender;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.client.IrishSportAnalyserServiceAsync;
import com.irishsportanalyser.enums.Region;
import java.util.List;

public class AddAthlete
{
    private DialogBox dialog;
    private Button submitButton;
    private Button closeButton;
    private TextBox yearOfBirthTextBox;
    private TextBox nameTextBox;
    private TextBox surnameTextBox;
    private RadioButton maleRadioButton;
    private Label connectionLabel;
    private ListBox clubListBox;
    private ListBox regionListBox;

    public AddAthlete()
    {
        addAthleteDialogBox();
        loadClubs();

        this.closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                AddAthlete.this.dialog.hide();
            }
        });
        this.submitButton.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent event) {
                AddAthlete.this.submitButton.setEnabled(false);
                try
                {
                    if (VariousFunctions.isNull(AddAthlete.this.nameTextBox.getText()))
                        throw new Exception("Name is null or Invalid!");
                    if (AddAthlete.this.nameTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    if (VariousFunctions.isNull(AddAthlete.this.surnameTextBox.getText()))
                        throw new Exception("Surname is null or Invalid!");
                    if (AddAthlete.this.surnameTextBox.getValue().length() > 20)
                        throw new Exception("Name is too long");
                    if (VariousFunctions.isNull(AddAthlete.this.yearOfBirthTextBox.getText()))
                        throw new Exception("Year of Birth is null or Invalid \n Use 0 if unknown!");
                    if (Integer.parseInt(AddAthlete.this.yearOfBirthTextBox.getText()) < 0) {
                        throw new Exception("Invalid year of birth!");
                    }

                    String clubName = AddAthlete.this.clubListBox.getItemText(AddAthlete.this.clubListBox.getSelectedIndex());
                    if (clubName.equals("No Clubs")) {
                        throw new Exception("Please add a club to the list via the Add Club menu item.");
                    }
                    Gender gender = AddAthlete.this.maleRadioButton.getValue().booleanValue() ? Gender.Male : Gender.Female;

                    Region region = VariousFunctions.stringToRegion(AddAthlete.this.regionListBox.getItemText(AddAthlete.this.regionListBox.getSelectedIndex()));

                    boolean windowsBox = Window.confirm(
                            "Gender: " + gender + "\n" +
                                    "Name: " + AddAthlete.this.nameTextBox.getText() + "\n" +
                                    "Surname: " + AddAthlete.this.surnameTextBox.getText() + "\n" +
                                    "Year of Birth: " + AddAthlete.this.yearOfBirthTextBox.getText() + "\n" +
                                    "Region: " + region.toString() + "\n" +
                                    "Club Name: " + clubName);

                    if (windowsBox) {
                        Athlete athlete = new Athlete();
                        athlete.setGender(gender);
                        athlete.setName(VariousFunctions.escapeHtml(AddAthlete.this.nameTextBox.getText()));
                        athlete.setSurname(VariousFunctions.escapeHtml(AddAthlete.this.surnameTextBox.getText()));
                        athlete.setYearOfBirth(Integer.parseInt(AddAthlete.this.yearOfBirthTextBox.getText()));
                        athlete.setRegion(region);

                        AddAthlete.this.connectionLabel.setText("Initialising Query........");
                        AddAthlete.this.addAthlete(athlete, clubName);
                    }
                } catch (Exception e) {
                    VariousFunctions.errorDialogBox("Add Athlete", e.getMessage());
                } finally {
                    AddAthlete.this.connectionLabel.setText("");
                    AddAthlete.this.submitButton.setEnabled(true);
                }
            }
        });
        this.regionListBox.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                AddAthlete.this.loadClubs();
            }
        });
    }

    private void addAthlete(Athlete athlete, String clubName) {
        IrishSportAnalyserServiceAsync IrishSportAnalyserService = GWT.create(IrishSportAnalyserService.class);
        AsyncCallback<String> callback = new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                try {
                    AddAthlete.this.submitButton.setEnabled(true);
                    AddAthlete.this.connectionLabel.setText("");
                    throw caught;
                } catch (IncompatibleRemoteServiceException e) {
                    VariousFunctions.errorDialogBox("Add Athlete", "IncompatibleRemoteServiceException!");
                } catch (InvocationException e) {
                    VariousFunctions.errorDialogBox("Add Athlete", "InvocationException!");
                } catch (Throwable e) {
                    VariousFunctions.errorDialogBox("Add Athlete", e.getMessage());
                }
            }
            public void onSuccess(String result) { AddAthlete.this.submitButton.setEnabled(true);
                VariousFunctions.informationDialogBox("Add Athlete", result);
                AddAthlete.this.cleanForm();
            }
        };
        IrishSportAnalyserService.addAthlete(athlete, clubName, callback);
    }

    private void cleanForm()
    {
        this.yearOfBirthTextBox.setText("0");
        this.nameTextBox.setText("");
        this.surnameTextBox.setText("");
        this.maleRadioButton.setValue(true);
    }

    private void addAthleteDialogBox()
    {
        this.dialog = new DialogBox();
        this.dialog.setText("Add Athlete");
        this.dialog.setAnimationEnabled(true);

        AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setSize("279px", "289px");

        this.maleRadioButton = new RadioButton("gender", "Male");
        absolutePanel.add(this.maleRadioButton, 77, 0);
        this.maleRadioButton.setValue(true);

        RadioButton femaleRadioButton = new RadioButton("gender", "Female");
        absolutePanel.add(femaleRadioButton, 140, 0);

        this.closeButton = new Button("Cancel");
        absolutePanel.add(this.closeButton, 45, 228);
        this.closeButton.setWidth("96px");

        this.submitButton = new Button("Submit");
        absolutePanel.add(this.submitButton, 147, 228);
        this.submitButton.setWidth("96px");

        this.connectionLabel = new Label("");
        this.connectionLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        absolutePanel.add(this.connectionLabel, 4, 202);
        this.connectionLabel.setSize("256px", "20px");

        Label label_6 = new Label("Year of Birth:");
        label_6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(label_6, 4, 106);
        label_6.setSize("90px", "18px");

        this.yearOfBirthTextBox = new TextBox();
        absolutePanel.add(this.yearOfBirthTextBox, 100, 106);
        this.yearOfBirthTextBox.setText("0");
        this.yearOfBirthTextBox.setSize("150px", "18px");

        Label emptyLabelBlock2 = new Label("*NB: Enter 0 as year of birth if unknown.");
        emptyLabelBlock2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        absolutePanel.add(emptyLabelBlock2, 4, 264);
        emptyLabelBlock2.setStyleName("errorLabel");
        emptyLabelBlock2.setSize("256px", "18px");

        Label label_5 = new Label("Name:");
        label_5.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(label_5, 0, 36);
        label_5.setSize("90px", "18px");

        Label label_2 = new Label("Region:");
        label_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(label_2, 0, 142);
        label_2.setSize("90px", "18px");

        this.regionListBox = new ListBox();
        absolutePanel.add(this.regionListBox, 100, 142);
        this.regionListBox.addItem(Region.Leinster.toString());
        this.regionListBox.addItem(Region.Munster.toString());
        this.regionListBox.addItem(Region.Connacht.toString());
        this.regionListBox.addItem(Region.Ulster.toString());
        this.regionListBox.setWidth("160px");

        Label label_7 = new Label("Surname:");
        label_7.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(label_7, 0, 70);
        label_7.setSize("90px", "18px");

        Label label_4 = new Label("Club:");
        label_4.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        absolutePanel.add(label_4, 4, 178);
        label_4.setSize("90px", "18px");

        this.clubListBox = new ListBox();
        this.clubListBox.addItem("Loading.....");
        absolutePanel.add(this.clubListBox, 100, 174);
        this.clubListBox.setWidth("160px");

        this.nameTextBox = new TextBox();
        absolutePanel.add(this.nameTextBox, 100, 34);
        this.nameTextBox.setSize("150px", "18px");

        this.surnameTextBox = new TextBox();
        absolutePanel.add(this.surnameTextBox, 100, 70);
        this.surnameTextBox.setSize("150px", "18px");

        this.dialog.setWidget(absolutePanel);
        this.dialog.center();
    }

    private void getClub(Region region) {
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

            public void onSuccess(List<Club> result) {
                try {
                    AddAthlete.this.clubListBox.clear();

                    if (result == null)
                        AddAthlete.this.clubListBox.addItem("No Clubs");
                    else
                        for (Club club : result)
                            AddAthlete.this.clubListBox.addItem(club.getName().replace("&amp;", "&"));
                }
                catch (Exception e)
                {
                    VariousFunctions.errorDialogBox("Upload Club List", e.getMessage());
                }
            }
        };
        IrishSportAnalyserService.getClub(region,callback);
    }

    private void loadClubs() {
        this.submitButton.setEnabled(false);
        this.regionListBox.setEnabled(false);
        this.clubListBox.setEnabled(false);
        try {
            Region region = VariousFunctions.stringToRegion(this.regionListBox.getItemText(this.regionListBox.getSelectedIndex()));
            getClub(region);
        } catch (Exception e) {
            VariousFunctions.errorDialogBox("Loading Club List", e.getMessage());
        } finally {
            this.submitButton.setEnabled(true);
            this.regionListBox.setEnabled(true);
            this.clubListBox.setEnabled(true);
        }
    }
}
