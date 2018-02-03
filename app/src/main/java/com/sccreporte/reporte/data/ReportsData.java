package com.sccreporte.reporte.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpson on 2/3/2018.
 */

public class ReportsData {
    public List<Report> getData(){
        List<Report> result = new ArrayList<>();
        String[] titles={"Home","My Travels","Browse Photos"};
        Report current;
        for (int i=0;i<3;i++){
            current = new Report();
            current.data = titles[i];
            result.add(current);
        }
        for (int i =0;i<=30;i++){
            current = new Report();
            current.data = "caca" + String.valueOf(i);
            result.add(current);
        }
        return result;
    }
    public class Report{
        public String data;
    }
}
