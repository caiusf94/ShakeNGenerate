package com.caiusf.shakengenerate.models;

/**
 * Created by caius.florea on 15-Jan-17.
 */

public class Language {

    private String languageName;
    private String languageCode;

    public Language(String languageName, String languageCode){
        this.languageName = languageName;
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String toString(){
        return languageName;
    }
}
