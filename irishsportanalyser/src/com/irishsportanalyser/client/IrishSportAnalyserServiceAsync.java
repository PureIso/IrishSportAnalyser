package com.irishsportanalyser.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.irishsportanalyser.entities.Admin;
import com.irishsportanalyser.entities.Athlete;
import com.irishsportanalyser.entities.Club;
import com.irishsportanalyser.entities.Competition;
import com.irishsportanalyser.entities.Result;
import com.irishsportanalyser.enums.Gender;
import com.irishsportanalyser.enums.Region;
import com.irishsportanalyser.shared.PerformanceHistory;
import java.util.List;

public interface IrishSportAnalyserServiceAsync {
        public abstract void addAthlete(Athlete paramAthlete, String paramString, AsyncCallback<String> paramAsyncCallback);

        public abstract void getAthlete(Long paramLong, AsyncCallback<List<Athlete>> paramAsyncCallback);

        public abstract void getAthlete(Gender paramGender, AsyncCallback<List<Athlete>> paramAsyncCallback);

        public abstract void getAthlete(String paramString1, String paramString2, Gender paramGender, AsyncCallback<List<Athlete>> paramAsyncCallback);

        public abstract void updateAthlete(Athlete paramAthlete, String paramString, AsyncCallback<String> paramAsyncCallback);

        public abstract void addAdmin(Admin paramAdmin, AsyncCallback<String> paramAsyncCallback);

        public abstract void searchAdmin(Admin paramAdmin, AsyncCallback<Boolean> paramAsyncCallback);

        public abstract void addClub(Club paramClub, AsyncCallback<String> paramAsyncCallback);

        public abstract void getClub(Region paramRegion, AsyncCallback<List<Club>> paramAsyncCallback);

        public abstract void getClubName(Long paramLong, AsyncCallback<String> paramAsyncCallback);

        public abstract void addCompetition(Competition paramCompetition, AsyncCallback<String> paramAsyncCallback);

        public abstract void getSeasons(String paramString, AsyncCallback<List<String>> paramAsyncCallback);

        public abstract void getRegions(String paramString1, String paramString2, AsyncCallback<List<Region>> paramAsyncCallback);

        public abstract void getVenue(String paramString1, String paramString2, Region paramRegion, AsyncCallback<List<String>> paramAsyncCallback);

        public abstract void getCompetitionName(String paramString1, String paramString2, Region paramRegion, String paramString3, AsyncCallback<List<String>> paramAsyncCallback);

        public abstract void getDate(String paramString1, String paramString2, Region paramRegion, String paramString3, String paramString4, AsyncCallback<List<String>> paramAsyncCallback);

        public abstract void getCompetitionID(String paramString1, String paramString2, Region paramRegion, String paramString3, String paramString4, String paramString5, AsyncCallback<List<Competition>> paramAsyncCallback);

        public abstract void addCompetitionResult(Result paramResult, AsyncCallback<Void> paramAsyncCallback);

        public abstract void getPerformanceHistory(Result paramResult, AsyncCallback<List<PerformanceHistory>> paramAsyncCallback);

        public abstract void getCompetitionID(Long paramLong, String paramString1, int paramInt, String paramString2, AsyncCallback<List<Result>> paramAsyncCallback);

        public abstract void getSeasonsBest(String paramString1, Gender paramGender, String paramString2, int paramInt, AsyncCallback<List<Result>> paramAsyncCallback);

        public abstract void getAthlete(Result paramResult, Region paramRegion, String paramString, AsyncCallback<List<String>> paramAsyncCallback);

        public abstract void getRankCompetitionDetails(List<String> paramList, AsyncCallback<List<String>> paramAsyncCallback);
    }