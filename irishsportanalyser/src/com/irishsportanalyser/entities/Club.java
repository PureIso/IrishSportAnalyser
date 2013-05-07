package com.irishsportanalyser.entities;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.user.client.rpc.IsSerializable;
import com.irishsportanalyser.enums.Region;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class Club implements IsSerializable
{

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Region region;

    @Persistent
    private String name;

    public Club()
    {
    }

    public Club(Region region, String name)
    {
        setName(name);
        setRegion(region);
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Region getRegion() {
        return this.region;
    }
    void setRegion(Region region) {
        this.region = region;
    }
    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }
}
