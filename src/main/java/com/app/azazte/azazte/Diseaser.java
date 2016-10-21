package com.app.azazte.azazte;

import com.app.azazte.azazte.Beans.Disease;
import com.app.azazte.azazte.Database.Connector;

import java.util.ArrayList;

/**
 * Created by home on 21/10/16.
 */
public class Diseaser {
    public Disease getDiseaseFromKeyword(String keyword){
        ArrayList<Disease> diseases = Connector.getInstance().getAllDiseases();
        for (Disease disease : diseases) {
            if (disease.getSymptom().contains(keyword)){
                return disease;
            }
        }
        return null;
    }

    public void saveDisease(Disease disease){
        Connector.getInstance().insertDisease(disease);
    }
}
