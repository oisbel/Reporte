package com.sccreporte.reporte.data;

/** To pass Report Object between activities
 * Created by simpson on 2/8/2018.
 */

public class Comunicador {

    private static ReportsData.Report report = null;

    public static void setObjReport(ReportsData.Report newObjReport) {
        report = newObjReport;
    }

    public static ReportsData.Report getObjReport() {
        return report;
    }
}
