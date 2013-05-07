package com.irishsportanalyser.entities;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.user.client.rpc.IsSerializable;
import com.irishsportanalyser.enums.Gender;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class Result implements IsSerializable
{

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String competitionType;

    @Persistent
    private Long competitionId;

    @Persistent
    private Gender gender;

    @Persistent
    private Long athleteId;

    @Persistent
    private String performance;

    @Persistent
    private boolean seasonsBest;

    @Persistent
    private String event;

    @Persistent
    private int season;

    public Result()
    {
    }

    public Result(String competitionType, Long competitionId, Long athleteId, String performance, String event, Gender gender, int season)
    {
        setCompetitionType(competitionType);
        setCompetitionId(competitionId);
        setAthleteId(athleteId);
        setPerformance(performance);
        setSeasonsBest(true);
        setEvent(event);
        setGender(gender);
        setSeason(season);
    }


    public String getCompetitionType() {
        return competitionType;
    }

    void setCompetitionType(String competitionType) {
        this.competitionType = competitionType;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public Gender getGender() {
        return gender;
    }

    void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getAthleteId() {
        return athleteId;
    }

    void setAthleteId(Long athleteId) {
        this.athleteId = athleteId;
    }

    public String getPerformance() {
        return performance;
    }

    void setPerformance(String performance) {
        this.performance = performance;
    }

    public boolean isSeasonsBest() {
        return seasonsBest;
    }

    public void setSeasonsBest(boolean seasonsBest) {
        this.seasonsBest = seasonsBest;
    }

    public String getEvent() {
        return event;
    }

    void setEvent(String event) {
        this.event = event;
    }

    public int getSeason() {
        return season;
    }

    void setSeason(int season) {
        this.season = season;
    }
}
