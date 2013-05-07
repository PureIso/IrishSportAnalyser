package com.irishsportanalyser.client;

import com.google.gwt.core.client.EntryPoint;
import com.irishsportanalyser.forms.HomeForm;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class IrishSportAnalyser implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        new HomeForm();
    }
}
