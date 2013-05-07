package com.irishsportanalyser.server;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF
{
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public static PersistenceManagerFactory get()
    {
        return pmfInstance;
    }
}
