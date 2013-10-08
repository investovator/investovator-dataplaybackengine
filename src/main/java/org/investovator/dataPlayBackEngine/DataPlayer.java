package org.investovator.dataPlayBackEngine;

import org.investovator.core.data.HistoryDataAPI;
import org.investovator.core.excelimporter.HistoryData;
import org.investovator.dataPlayBackEngine.data.BogusHistoryDataGenerator;
import org.investovator.dataPlayBackEngine.scheduler.EventTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observer;
import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayer {

    Timer timer;
    EventTask task;
    HistoryDataAPI dataAPI;

    public DataPlayer(String stock) {
        this.timer = new Timer();
        //for testing
        this.dataAPI=new BogusHistoryDataGenerator();
        task = new EventTask(stock, "2011-12-13-15-55-32",dataAPI);

    }

    public void setObserver(Observer observer){
        task.setObserver(observer);
    }

    public void runPlayback(int resolution) {

        timer.schedule(task, 0, resolution * 1000);
    }

    public void stopPlayback() {
        timer.cancel();
    }

    public HistoryData getOHLCPrice(String stock, String date){

        Date currentTime=null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ss"); //should be in format year-month-date-24hr-minute-second
        try {
            currentTime =format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataAPI.getOHLCPData(currentTime,stock);
    }
}
