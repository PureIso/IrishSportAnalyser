package com.irishsportanalyser.entities;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.user.client.rpc.IsSerializable;
import com.irishsportanalyser.enums.Gender;
import com.irishsportanalyser.enums.Region;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Athlete implements IsSerializable
{

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Gender gender;

    @Persistent
    private String name;

    @Persistent
    private String surname;

    @Persistent
    private int yearOfBirth;

    @Persistent
    private Region region;

    @Persistent
    private Long clubId;

    public Athlete()
    {
    }

    public Athlete(Gender gender, String name, String surname, Region region, Long club)
    {
        setGender(Gender.Male);
        setName("Empty");
        setSurname("Empty");
        setRegion(Region.Leinster);
        setYearOfBirth(0);
        setClub(club);
    }

    public Long getId() {
        if (this.id == null) {
            return 0L;
        }
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public int getYearOfBirth() {
        return this.yearOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public Long getClub() {
        return this.clubId;
    }

    public void setClub(Long clubId) {
        this.clubId = clubId;
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
