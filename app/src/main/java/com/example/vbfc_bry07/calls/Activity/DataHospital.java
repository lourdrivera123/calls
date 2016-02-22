package com.example.vbfc_bry07.calls.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataHospital {

    public static HashMap<String, List<String>> getInfo() {
        HashMap<String, List<String>> HospitalDetails = new HashMap<>();
        List<String> Hospital1 = new ArrayList<>();
        Hospital1.add("Manuel Villanueva");
        Hospital1.add("John Kurt Duterte");
        List<String> Hospital2 = new ArrayList<>();
        Hospital2.add("Mary Joy Villareal");
        Hospital2.add("Angela Curtis");
        List<String> Hospital3 = new ArrayList<>();
        Hospital3.add("Marcus Pepito");
        Hospital3.add("Madison Balili");

        HospitalDetails.put("Davao Doctors Hospital", Hospital1);
        HospitalDetails.put("Southern Philippines Medical Center", Hospital2);
        HospitalDetails.put("San Pedro Hospital", Hospital3);

        return HospitalDetails;
    }
}
