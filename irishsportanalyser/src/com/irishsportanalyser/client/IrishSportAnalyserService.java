package com.irishsportanalyser.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.irishsportanalyser.entities.*;
import com.irishsportanalyser.enums.Gender;
import com.irishsportanalyser.enums.Region;
import com.irishsportanalyser.shared.PerformanceHistory;

import java.util.List;

@RemoteServiceRelativePath("IrishSportAnalyserService")
public interface IrishSportAnalyserService extends RemoteService {
    public abstract String addAthlete(Athlete paramAthlete, String paramString)
            throws Exception;

    public abstract List<Athlete> getAthlete(Long paramLong)
            throws Exception;

    public abstract List<Athlete> getAthlete(Gender paramGender)
            throws Exception;

    public abstract List<Athlete> getAthlete(String paramString1, String paramString2, Gender paramGender)
            throws Exception;

    public abstract List<String> getAthlete(Result paramResult, Region paramRegion, String paramString)
            throws Exception;

    public abstract String updateAthlete(Athlete paramAthlete, String paramString)
            throws Exception;

    public abstract String addAdmin(Admin paramAdmin)
            throws Exception;

    public abstract boolean searchAdmin(Admin paramAdmin)
            throws Exception;

    public abstract String addClub(Club paramClub)
            throws Exception;

    public abstract List<Club> getClub(Region paramRegion)
            throws Exception;

    public abstract String getClubName(Long paramLong)
            throws Exception;

    public abstract String addCompetition(Competition paramCompetition)
            throws Exception;

    public abstract List<Result> getCompetitionID(Long paramLong, String paramString1, int paramInt, String paramString2)
            throws Exception;

    public abstract List<Result> getSeasonsBest(String paramString1, Gender paramGender, String paramString2, int paramInt)
            throws Exception;

    public abstract List<String> getRankCompetitionDetails(List<String> paramList)
            throws Exception;

    public abstract List<String> getSeasons(String paramString)
            throws Exception;

    public abstract List<Region> getRegions(String paramString1, String paramString2)
            throws Exception;

    public abstract List<String> getVenue(String paramString1, String paramString2, Region paramRegion)
            throws Exception;

    public abstract List<String> getCompetitionName(String paramString1, String paramString2, Region paramRegion, String paramString3)
            throws Exception;

    public abstract List<String> getDate(String paramString1, String paramString2, Region paramRegion, String paramString3, String paramString4)
            throws Exception;

    public abstract List<Competition> getCompetitionID(String paramString1, String paramString2, Region paramRegion, String paramString3, String paramString4, String paramString5)
            throws Exception;

    public abstract void addCompetitionResult(Result paramResult)
            throws Exception;

    public abstract List<PerformanceHistory> getPerformanceHistory(Result paramResult)
            throws Exception;
}
