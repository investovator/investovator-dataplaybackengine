package org.investovator.dataPlayBackEngine.scheduler;

import org.investovator.core.org.investovator.data.HistoryDataAPI;
import org.investovator.core.org.investovator.data.types.HistoryOrderData;
import org.investovator.dataPlayBackEngine.data.BogusHistoryDataGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventTask extends TimerTask {
    int i=0;

    //stock to playback
    private String stockId;
    //start time
    private Date currentTime;
    //Data interface
    HistoryDataAPI dataApi=new BogusHistoryDataGenerator();


    public EventTask(String stockId, String startT) {
        this.stockId = stockId;

        //String sourceDate=currentTime;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
        try {
             currentTime =format.parse(startT);
            System.out.println("start time: "+currentTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        ;
    }

    @Override
    public void run() {

        HistoryOrderData[] data=dataApi.getData(currentTime,incrementTimeBySeconds(1),"goog");
        //print the data
        System.out.println("===================================================");
        for(HistoryOrderData d:data){
            System.out.println(d.getDate()+"_"+d.getStockId()+"_"+d.getPrice()+"_"+d.getNumOfShares()+"_"+d.isBid());
        }

        // in order to point to the next time interval
        currentTime=incrementTimeBySeconds(1);
        //System.out.println("Current time: "+currentTime);
    }

    /**
     * Increases the current time by the given number of seconds
     *
     * @param seconds the number of seconds to increase by
     */
    private Date incrementTimeBySeconds(int seconds){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.SECOND, seconds); //minus number would decrement the days
        return cal.getTime();

    }
}
