package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.irishsportanalyser.enums.Region;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class VariousFunctions
{
    public static void ageItems(ListBox listbox)
    {
        String[] items = { "All", "Under 12", "Under 13", "Under 15", "Under 17",
                "Under 19", "Under 23", "Seniors", "Masters 1", "Masters 2",
                "Masters 3", "Masters 4", "Masters 5" };
        for (String item : items)
            listbox.addItem(item);
    }

    public static String getAgeJDOQueryString(String ageGroup)
    {
        Date date = new Date();
        int year = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(date));
        if (ageGroup.equalsIgnoreCase("Under 12")) {
            return "&& yearOfBirth > " + (year - 12);
        }
        if (ageGroup.equalsIgnoreCase("Under 13")) {
            return "&& yearOfBirth <= " + (year - 12) + " && yearOfBirth > " + (year - 13);
        }
        if (ageGroup.equalsIgnoreCase("Under 15")) {
            return "&& yearOfBirth <= " + (year - 13) + " && yearOfBirth > " + (year - 15);
        }
        if (ageGroup.equalsIgnoreCase("Under 17")) {
            return "&& yearOfBirth <= " + (year - 15) + " && yearOfBirth > " + (year - 17);
        }
        if (ageGroup.equalsIgnoreCase("Under 19")) {
            return "&& yearOfBirth <= " + (year - 17) + " && yearOfBirth > " + (year - 19);
        }
        if (ageGroup.equalsIgnoreCase("Under 23")) {
            return "&& yearOfBirth <= " + (year - 19) + " && yearOfBirth > " + (year - 23);
        }
        if (ageGroup.equalsIgnoreCase("Seniors")) {
            return "&& yearOfBirth <= " + (year - 23) + " && yearOfBirth > " + (year - 35);
        }
        if (ageGroup.equalsIgnoreCase("Masters 1")) {
            return "&& yearOfBirth <= " + (year - 35) + " && yearOfBirth > " + (year - 40);
        }
        if (ageGroup.equalsIgnoreCase("Masters 2")) {
            return "&& yearOfBirth <= " + (year - 40) + " && yearOfBirth > " + (year - 45);
        }
        if (ageGroup.equalsIgnoreCase("Masters 3")) {
            return "&& yearOfBirth <= " + (year - 45) + " && yearOfBirth > " + (year - 50);
        }
        if (ageGroup.equalsIgnoreCase("Masters 4")) {
            return "&& yearOfBirth <= " + (year - 50) + " && yearOfBirth > " + (year - 55);
        }
        if (ageGroup.equalsIgnoreCase("Masters 5")) {
            return "&& yearOfBirth <= " + (year - 55) + " && yearOfBirth > " + (year - 110);
        }

        return "";
    }

    public static void eventItem(ListBox listbox, String competitionType)
    {
        if (competitionType == "Indoor") {
            String[] items = { "60 m", "60 m hurldes", "200 m", "400 m", "800 m", "1500 m",
                    "1 mile", "3000 m", "5000 m", "High jump", "Pole vault", "Long jump", "Triple jump",
                    "Shot", "Heptathlon", "3000 m walk", "5000 m walk", "Pentathlon" };

            for (String item : items)
                listbox.addItem(item);
        }
        else
        {
            String[] items = { "100 m", "100 m hurldes", "110 m hurldes", "200 m", "400 m", "400 m hurldes", "800 m", "1500 m",
                    "1 mile", "3000 m", "5000 m", "10000 m", "Half Marathon", "Marathon", "2000 m steeple",
                    "3000 m steeple", "High jump", "Pole vault", "Long jump", "Triple jump", "Shot", "Discus",
                    "Hammer", "Javelin", "Decathlon", "10000 m walk", "20000 m walk", "10 km walk", "20 km walk", "50 km walk",
                    "Heptathlon" };
            for (String item : items)
                listbox.addItem(item);
        }
    }

    public static String escapeHtml(String html)
            throws Exception
    {
        if (html == null) {
            return null;
        }
        if (html.contains("'"))
            throw new Exception("Single Quote (') is unacceptable!");
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    public static boolean isNumeric(String string)
    {
        try
        {
            Long.parseLong(string);
            return true; } catch (Exception e) {
        }
        return false;
    }

    public static boolean isNull(String string)
    {
        return string.isEmpty();
    }

    public static boolean isValidResult(String value, String event)
    {
        if ((event.compareToIgnoreCase("Heptathlon") == 0) || (event.compareToIgnoreCase("Pentathlon") == 0) ||
                (event.compareToIgnoreCase("Decathlon") == 0))
        {
            return !((value.contains(".")) || (value.contains(":")));
        }

        return (value.contains(".")) || (value.contains(":"));
    }

    public static void informationDialogBox(String title, String message)
    {
        final DialogBox dialogBox = new DialogBox();
        AbsolutePanel dialogVPanel = new AbsolutePanel();

        dialogBox.setText(title);
        dialogBox.setAnimationEnabled(true);

        Button closeButton = new Button("Close");
        closeButton.getElement().setId("closeButton");

        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(closeButton, 133, 63);

        closeButton.setSize("87px", "30px");
        dialogBox.setWidget(dialogVPanel);
        dialogVPanel.setSize("327px", "100px");
        dialogBox.center();
        closeButton.setFocus(true);

        Image image = new Image("img/information.png");
        dialogVPanel.add(image, 0, 0);
        image.setSize("65px", "57px");

        ScrollPanel scrollPanel = new ScrollPanel();
        dialogVPanel.add(scrollPanel, 71, 0);
        scrollPanel.setSize("256px", "57px");

        HTML htmlNewHtml = new HTML(message, true);
        scrollPanel.setWidget(htmlNewHtml);
        htmlNewHtml.setSize("100%", "100%");

        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
    }

    public static void errorDialogBox(String title, String message)
    {
        final DialogBox dialogBox = new DialogBox();
        AbsolutePanel dialogVPanel = new AbsolutePanel();

        dialogBox.setText(title);
        dialogBox.setAnimationEnabled(true);

        Button closeButton = new Button("Close");
        closeButton.getElement().setId("closeButton");

        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(closeButton, 133, 63);

        closeButton.setSize("87px", "30px");
        dialogBox.setWidget(dialogVPanel);
        dialogVPanel.setSize("327px", "100px");
        dialogBox.center();
        closeButton.setFocus(true);

        Image image = new Image("img/error.png");
        dialogVPanel.add(image, 0, 0);
        image.setSize("65px", "57px");

        ScrollPanel scrollPanel = new ScrollPanel();
        dialogVPanel.add(scrollPanel, 71, 0);
        scrollPanel.setSize("256px", "57px");

        HTML htmlNewHtml = new HTML(message, true);
        scrollPanel.setWidget(htmlNewHtml);
        htmlNewHtml.setSize("100%", "100%");

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                dialogBox.hide();
            }
        });
    }

    public static Region stringToRegion(String region)
            throws Exception
    {
        if (Region.Leinster.toString().equals(region))
            return Region.Leinster;
        if (Region.Connacht.toString().equals(region))
            return Region.Connacht;
        if (Region.Munster.toString().equals(region))
            return Region.Munster;
        if (Region.Ulster.toString().equals(region))
            return Region.Ulster;
        if (Region.All_Ireland.toString().equals(region))
            return Region.All_Ireland;
        if (Region.All_Ireland.toString().equals(region))
            return Region.Non_Irish;
        throw new Exception("Invalid String region!");
    }

    public static List<List<String>> CollectionSort(List<List<String>> collection, String event)
            throws Exception
    {
        int indexRemove = 0;
        List sortedList = new ArrayList(collection.size());
        int rank = 1;
        try {
            if ((event.compareToIgnoreCase("High jump") == 0) || (event.compareToIgnoreCase("Pole vault") == 0) ||
                    (event.compareToIgnoreCase("Long jump") == 0) || (event.compareToIgnoreCase("Triple jump") == 0) ||
                    (event.compareToIgnoreCase("Shot") == 0) || (event.compareToIgnoreCase("Heptathlon") == 0) ||
                    (event.compareToIgnoreCase("Pentathlon") == 0) || (event.compareToIgnoreCase("Discus") == 0) ||
                    (event.compareToIgnoreCase("Hammer") == 0) || (event.compareToIgnoreCase("Javelin") == 0) ||
                    (event.compareToIgnoreCase("Decathlon") == 0))
            {
                List currentSmallest = Arrays.asList(new String[] { new String("0.0"),
                        "0.0" });
                for (int i = 0; i < collection.size(); i++) {
                    for (int j = 0; j < collection.size(); j++) {
                        if ((collection.get(j) != null) &&
                                (((String)((List)collection.get(j)).get(1)).compareToIgnoreCase((String)currentSmallest.get(1)) > 0)) {
                            currentSmallest = (List)collection.get(j);
                            indexRemove = j;
                        }
                    }

                    collection.set(indexRemove, null);
                    currentSmallest.set(0, Integer.toString(rank));
                    rank++;
                    sortedList.add(currentSmallest);

                    currentSmallest = Arrays.asList(new String[] { new String("0.0"),
                            "0.0" });
                }
            }
            else
            {
                List currentSmallest = Arrays.asList(new String[] { new String("9999.9999"),
                        "9999.9999" });
                for (int i = 0; i < collection.size(); i++) {
                    for (int j = 0; j < collection.size(); j++) {
                        if ((collection.get(j) != null) &&
                                (((String)((List)collection.get(j)).get(1)).compareToIgnoreCase((String)currentSmallest.get(1)) < 0)) {
                            currentSmallest = (List)collection.get(j);
                            indexRemove = j;
                        }
                    }

                    collection.set(indexRemove, null);
                    currentSmallest.set(0, Integer.toString(rank));
                    rank++;
                    sortedList.add(currentSmallest);

                    currentSmallest = Arrays.asList(new String[] { new String("9999.9999"),
                            "9999.9999" });
                }
            }
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        List currentSmallest;
        return sortedList;
    }
}