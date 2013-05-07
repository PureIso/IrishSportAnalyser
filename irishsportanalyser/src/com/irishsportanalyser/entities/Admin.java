package com.irishsportanalyser.entities;

import com.google.gwt.user.client.rpc.IsSerializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */

@PersistenceCapable
public class Admin implements IsSerializable
{
    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String username;

    @Persistent
    private String password;

    public Admin()
    {
    }

    public Admin(String username, String password)
    {
        setUsername(username);
        setPassword(password);
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }
    void setPassword(String password) {
        this.password = password;
    }
}