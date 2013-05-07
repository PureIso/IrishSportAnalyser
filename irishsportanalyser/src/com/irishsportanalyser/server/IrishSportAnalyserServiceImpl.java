package com.irishsportanalyser.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.irishsportanalyser.client.IrishSportAnalyserService;
import com.irishsportanalyser.entities.*;
import com.irishsportanalyser.enums.Gender;
import com.irishsportanalyser.enums.Region;
import com.irishsportanalyser.shared.PerformanceHistory;
import com.irishsportanalyser.shared.VariousFunctions;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

public class IrishSportAnalyserServiceImpl extends RemoteServiceServlet implements IrishSportAnalyserService {
    // Implementation of sample interface method
    public String addAthlete(Athlete athlete, String clubName)
            throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try
        {
            athlete.setName(athlete.getName().toLowerCase());
            athlete.setSurname(athlete.getSurname().toLowerCase());

            athlete.setClub(getClubId(athlete.getRegion(), clubName));

            boolean duplicate = searchDublicateAthlete(athlete);

            if (!duplicate) {
                pm.makePersistent(athlete);
                return "Add Athlete Successful!";
            }
            return "Duplicate";
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public List<Athlete> getAthlete(Long id) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<Athlete> returnResult = null;
        try
        {
            Query query = pm.newQuery(Athlete.class, "id == " + id + " ");

            List result = (List)query.execute();
            if (result.size() == 0) return null;

            returnResult = new ArrayList<Athlete>(result.size());
            for (Athlete athlete : (List<Athlete>) result)
                returnResult.add(athlete);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<Athlete> getAthlete(Gender gender) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<Athlete> returnResult = null;
        try
        {
            Query query = pm.newQuery(Athlete.class, "gender == '" + gender + "'");
            query.setOrdering("name asc");

            List result = (List)query.execute();
            if (result.size() == 0) return null;

            returnResult = new ArrayList<Athlete>(result.size());
            for (Athlete athlete : (List<Athlete>) result)
            {
                returnResult.add(athlete);
            }
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<Athlete> getAthlete(String name, String surname, Gender gender) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        name = name.toLowerCase();
        surname = surname.toLowerCase();

        List<Athlete> returnResult = null;
        try
        {
            Query query;
            if (name.isEmpty()) {
                query = pm.newQuery(Athlete.class, "surname == '" + surname + "' && gender == '" + gender + "'");
            }
            else
            {
                if (surname.isEmpty())
                    query = pm.newQuery(Athlete.class, "name == '" + name + "' && gender == '" + gender + "'");
                else {
                    query = pm.newQuery(Athlete.class, "name == '" + name + "' && surname == '" + surname + "' && gender == '" + gender + "'");
                }
            }

            List result = (List)query.execute();
            if (result.size() == 0) return null;

            returnResult = new ArrayList<Athlete>(result.size());
            for (Athlete athlete : (List<Athlete>) result) {
                returnResult.add(athlete);
            }
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public String updateAthlete(Athlete athlete, String clubName) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try
        {
            athlete.setName(athlete.getName().toLowerCase());
            athlete.setSurname(athlete.getSurname().toLowerCase());

            Query query = pm.newQuery(Athlete.class, "id == " + athlete.getId());

            List oldDetails = (List)query.execute();

            if (oldDetails.size() == 0) return "Update Athlete Failure!";

            ((Athlete)oldDetails.get(0)).setClub(getClubId(athlete.getRegion(), clubName));
            ((Athlete)oldDetails.get(0)).setName(athlete.getName());
            ((Athlete)oldDetails.get(0)).setSurname(athlete.getSurname());
            ((Athlete)oldDetails.get(0)).setRegion(athlete.getRegion());
            ((Athlete)oldDetails.get(0)).setYearOfBirth(athlete.getYearOfBirth());

            pm.makePersistent((Athlete)oldDetails.get(0));
            return "Update Athlete Successful!";
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    private Long getClubId(Region region, String clubName) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try
        {
            Query query = pm.newQuery(Club.class, "name == '" + clubName + "' && region == '" + region + "'");

            List club = (List)query.execute();

            if (club.size() == 0) return null;

            return ((Club)club.get(0)).getId();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    private boolean searchDublicateAthlete(Athlete athlete) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Athlete.class,
                    "gender == '" + athlete.getGender() + "' && name == '" + athlete.getName() +
                            "' && surname == '" + athlete.getSurname() + "' && yearOfBirth == " + athlete.getYearOfBirth() + " " +
                            " && clubId == " + athlete.getClub());

            List result = (List)query.execute();
            return result.size() != 0;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public String addAdmin(Admin admin) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try
        {
            admin.setUsername(admin.getUsername().toLowerCase());
            boolean duplicate = searchDublicateAdmin(admin);

            if (!duplicate) {
                pm.makePersistent(admin);
                return "Add Administrator Successful!";
            }
            return "Duplicate";
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    private boolean searchDublicateAdmin(Admin admin) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Admin.class, "username == '" + admin.getUsername() + "'");

            List result = (List)query.execute();
            return result.size() != 0;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public boolean searchAdmin(Admin admin) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            admin.setUsername(admin.getUsername().toLowerCase());
            Query query = pm.newQuery(Admin.class, "username == '" + admin.getUsername() + "' && password == '" + admin.getPassword() + "'");

            List result = (List)query.execute();
            return result.size() != 0;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public String addClub(Club club) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try
        {
            boolean duplicate = searchDublicateClub(club);

            if (!duplicate) {
                pm.makePersistent(club);
                return "Add Club Successful!";
            }
            return "Duplicate";
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    private boolean searchDublicateClub(Club club) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Club.class, "name == '" + VariousFunctions.escapeHtml(club.getName()) + "' && region =='" + club.getRegion() + "'");

            List result = (List)query.execute();
            return result.size() != 0;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public List<Club> getClub(Region region) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<Club> returnResult = null;
        try {
            Query query = pm.newQuery(Club.class, "region == '" + region + "' ");
            query.setOrdering("name asc");

            List result = (List)query.execute();
            if (result.size() == 0) return null;

            returnResult = new ArrayList<Club>(result.size());
            for (Club c : (List<Club>) result)
                returnResult.add(c);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public String getClubName(Long id) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        String returnResult = null;
        try {
            Query query = pm.newQuery(Club.class, "id == " + id);

            List result = (List)query.execute();
            if (result.size() == 0) return null;
            returnResult = ((Club)result.get(0)).getName();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public String addCompetition(Competition competition) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            boolean duplicate = searchDuplicateCompetition(competition);

            if (!duplicate) {
                pm.makePersistent(competition);
                return "Add Competition Successful!";
            }
            return "Duplicate";
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    private boolean searchDuplicateCompetition(Competition competition) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competition.getCompetitionType() + "' && name == '" + competition.getName() +
                    "' && region =='" + competition.getRegion() + "' && date == '" + competition.getDate() +
                    "' && venue == '" + competition.getVenue() + "'");

            List result = (List)query.execute();
            return result.size() != 0;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public List<String> getSeasons(String competitionType) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        System.out.print("Here");
        List<String> returnResult = null;
        try {
            System.out.print("Here 2");
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competitionType + "'");

            List result = (List)query.execute();
            if (result.size() == 0) return null;

            returnResult = new ArrayList<String>(result.size());
            for (Competition c : (List<Competition>) result)
                if (!returnResult.contains(c.getSeason()))
                    returnResult.add(c.getSeason());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<Region> getRegions(String competitionType, String season) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<Region> returnResult = null;
        try {
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competitionType + "' && season == '" + season + "'");

            List result = (List)query.execute();
            if (result.size() == 0) return null;

            returnResult = new ArrayList<Region>(result.size());
            for (Competition c : (List<Competition>) result)
                if (!returnResult.contains(c.getRegion()))
                    returnResult.add(c.getRegion());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<String> getVenue(String competitionType, String season, Region region) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<String> returnResult = null;
        try {
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competitionType + "' && season == '" + season + "' && region == '" + region + "'");

            List result = (List)query.execute();

            if (result.size() == 0) return null;
            returnResult = new ArrayList<String>(result.size());
            for (Competition c :(List<Competition>) result)
                if (!returnResult.contains(c.getVenue()))
                    returnResult.add(c.getVenue());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<String> getCompetitionName(String competitionType, String season, Region region, String venue) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<String> returnResult = null;
        try {
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competitionType + "' &&  season == '" + season + "' && region == '" + region + "'" +
                    " && venue == '" + venue + "'");

            List result = (List)query.execute();
            if (result.size() == 0) return null;
            returnResult = new ArrayList<String>(result.size());
            for (Competition c : (List<Competition>) result)
                if (!returnResult.contains(c.getName()))
                    returnResult.add(c.getName());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<String> getDate(String competitionType, String season, Region region, String venue, String competitionName) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<String> returnResult = null;
        try {
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competitionType + "' &&  season == '" + season + "' && region == '" + region + "'" +
                    " && venue == '" + venue + "' && name == '" + competitionName + "'");

            List result = (List)query.execute();
            if (result.size() == 0) return null;
            returnResult = new ArrayList<String>(result.size());

            for (Competition c : (List<Competition>) result)
                if (!returnResult.contains(c.getDate()))
                    returnResult.add(c.getDate());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close(); } pm.close();

        return returnResult;
    }

    public List<Competition> getCompetitionID(String competitionType, String season, Region region, String venue, String competitionName, String date) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<Competition> returnResult;
        try {
            Query query = pm.newQuery(Competition.class, "competitionType == '" + competitionType + "' && season == '" + season + "' && region == '" + region + "'" +
                    " && venue == '" + venue + "' && name == '" + competitionName + "' && date == '" + date + "'");

            List result = (List)query.execute();
            if (result.size() == 0) return null;
            returnResult = new ArrayList<Competition>(result.size());

            for (Competition c : (List<Competition>) result) {
                returnResult.add(c);
            }
            return returnResult;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public void addCompetitionResult(Result result) throws Exception {
        try {
            boolean duplicate = searchDublicateCompetitionResult(result);
            if (!duplicate)
                updateAthleteSeasonBest(result);
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    private boolean searchDublicateCompetitionResult(Result result) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query query = pm.newQuery(Result.class, "competitionType == '" + result.getCompetitionType() + "' && competitionId == " + result.getCompetitionId() +
                    " && athleteId ==" + result.getAthleteId() + " && performance == '" + result.getPerformance() +
                    "' && seasonsBest == " + result.isSeasonsBest() + " && event == '" + result.getEvent() + "'");

            List collection = (List)query.execute();
            return collection.size() != 0;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    private void updateAthleteSeasonBest(Result result) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try
        {
            Query query = pm.newQuery(Result.class, "competitionType == '" + result.getCompetitionType() + "' && athleteId == " + result.getAthleteId() + " " +
                    "&& event == '" + result.getEvent() + "' && seasonsBest == true");

            List previousBest = (List)query.execute();
            if (previousBest.size() == 0) {
                pm.makePersistent(result);
                return;
            }

            if ((result.getEvent().compareToIgnoreCase("High jump") == 0) || (result.getEvent().compareToIgnoreCase("Pole vault") == 0) ||
                    (result.getEvent().compareToIgnoreCase("Long jump") == 0) || (result.getEvent().compareToIgnoreCase("Triple jump") == 0) ||
                    (result.getEvent().compareToIgnoreCase("Shot") == 0) || (result.getEvent().compareToIgnoreCase("Heptathlon") == 0) ||
                    (result.getEvent().compareToIgnoreCase("Pentathlon") == 0) || (result.getEvent().compareToIgnoreCase("Discus") == 0) ||
                    (result.getEvent().compareToIgnoreCase("Hammer") == 0) || (result.getEvent().compareToIgnoreCase("Javelin") == 0) ||
                    (result.getEvent().compareToIgnoreCase("Decathlon") == 0))
            {
                if (((Result)previousBest.get(0)).getPerformance().compareToIgnoreCase(result.getPerformance()) < 0) {
                    ((Result)previousBest.get(0)).setSeasonsBest(false);
                    result.setSeasonsBest(true);

                    pm.makePersistent(result);
                    pm.makePersistent((Result)previousBest.get(0));
                } else {
                    result.setSeasonsBest(false);
                    pm.makePersistent(result);
                }

            }
            else if (((Result)previousBest.get(0)).getPerformance().compareToIgnoreCase(result.getPerformance()) > 0) {
                ((Result)previousBest.get(0)).setSeasonsBest(false);
                result.setSeasonsBest(true);

                pm.makePersistent(result);
                pm.makePersistent((Result)previousBest.get(0));
            } else {
                result.setSeasonsBest(false);
                pm.makePersistent(result);
            }
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            pm.close(); } pm.close();
    }

    public List<PerformanceHistory> getPerformanceHistory(Result results) throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<PerformanceHistory> returnResult;
        try {
            PerformanceHistory ph = new PerformanceHistory();
            ph.setPerformance(results.getPerformance());

            Query query = pm.newQuery(Competition.class, "id == " + results.getCompetitionId());

            List competitionResult = (List)query.execute();

            ph.setCompetition(((Competition)competitionResult.get(0)).getName());
            ph.setVenue(((Competition)competitionResult.get(0)).getVenue());
            ph.setDate(((Competition)competitionResult.get(0)).getDate());

            returnResult = new ArrayList<PerformanceHistory>(1);
            returnResult.add(ph);

            return returnResult;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public List<Result> getCompetitionID(Long athleteId, String competitionType, int season, String event) throws Exception {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<Result> returnResult = null;
        try
        {
            Query query = pm.newQuery(Result.class, "athleteId == " + athleteId + " && competitionType == '" + competitionType + "' && event == '" + event + "' && season == " + season);

            if ((event.compareToIgnoreCase("High jump") == 0) || (event.compareToIgnoreCase("Pole vault") == 0) ||
                    (event.compareToIgnoreCase("Long jump") == 0) || (event.compareToIgnoreCase("Triple jump") == 0) ||
                    (event.compareToIgnoreCase("Shot") == 0) || (event.compareToIgnoreCase("Heptathlon") == 0) ||
                    (event.compareToIgnoreCase("Pentathlon") == 0) || (event.compareToIgnoreCase("Discus") == 0) ||
                    (event.compareToIgnoreCase("Hammer") == 0) || (event.compareToIgnoreCase("Javelin") == 0) ||
                    (event.compareToIgnoreCase("Decathlon") == 0)) {
                query.setOrdering("performance desc");
            }
            else
            {
                query.setOrdering("performance asc");
            }

            List results = (List)query.execute();

            returnResult = new ArrayList<Result>(results.size());
            for (Result result : (List<Result>) results) {
                returnResult.add(result);
            }
            return returnResult;
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            pm.close();
        }
    }

    public List<Result> getSeasonsBest(String competitionType, Gender gender, String event, int season)
            throws Exception
    {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<Result> returnSeasonBest = null;
        try {
            Query query = pm.newQuery(Result.class, "competitionType == '" + competitionType + "' && " +
                    "gender == '" + gender + "' && event == '" + event + "' && season == " + season + " && seasonsBest == true");

            List allSeasonsBest = (List)query.execute();
            if (allSeasonsBest.size() == 0) return null;

            returnSeasonBest = new ArrayList<Result>(allSeasonsBest.size());
            for (Result result : (List<Result>)allSeasonsBest)
                returnSeasonBest.add(result);
        }
        catch (Exception localException) {
        }
        finally {
            pm.close();
        }
        return returnSeasonBest;
    }

    public List<String> getAthlete(Result result, Region region, String ageGroupQuery) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<String> returnResult = null;
        try
        {
            Query query;
            if (region.equals(Region.All_Ireland))
                query = pm.newQuery(Athlete.class, "id == " + result.getAthleteId() + " " + ageGroupQuery);
            else {
                query = pm.newQuery(Athlete.class, "id == " + result.getAthleteId() + " && region == '" + region + "' " + ageGroupQuery);
            }
            List athlete = (List)query.execute();

            if (athlete.size() == 0) return null;

            returnResult = new ArrayList<String>(5);
            returnResult.add("0");
            returnResult.add(result.getPerformance());
            returnResult.add(((Athlete)athlete.get(0)).getName());
            returnResult.add(((Athlete)athlete.get(0)).getSurname());
            returnResult.add(result.getCompetitionId().toString());
        } catch (Exception localException) {
        }
        finally {
            pm.close();
        }
        return returnResult;
    }

    public List<String> getRankCompetitionDetails(List<String> result) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<String> returnResult = null;
        try {
            Query query = pm.newQuery(Competition.class, "id == " + Long.parseLong((String)result.get(4)));

            List competition = (List)query.execute();

            if (competition.size() == 0) return null;
            returnResult = new ArrayList<String>(7);
            returnResult.add("0");
            returnResult.add((String)result.get(1));
            returnResult.add((String)result.get(2));
            returnResult.add((String)result.get(3));
            returnResult.add(((Competition)competition.get(0)).getDate());
            returnResult.add(((Competition)competition.get(0)).getVenue());
            returnResult.add(((Competition)competition.get(0)).getName());
        } catch (Exception localException) {
        }
        finally {
            pm.close();
        }
        return returnResult;
    }
}