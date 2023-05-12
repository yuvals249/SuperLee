package Stock.Service;

import Stock.Business.ReportsManager;

public class ReportsService {
    private final static ReportsManager reportsManager = ReportsManager.getInstance();
    private static ReportsService instance = null;

    private ReportsService(){
        // private constructor
    }
    public static ReportsService getInstance(){
        if(instance == null){
            instance = new ReportsService();
        }
        return instance;
    }

    public boolean createStockReport(){
        return reportsManager.createStockReport();
    }

    public boolean createOrderReport(){
        return reportsManager.createOrderReport();
    }

    public boolean createDamagedReport(){
        return reportsManager.createDamagedReport();
    }

    public boolean createShortageReport(){
        return reportsManager.createShortageReport();
    }

}
