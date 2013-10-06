package org.investovator.dataPlayBackEngine;

import org.investovator.dataPlayBackEngine.scheduler.EventTask;

import java.util.Observer;
import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayer {

    Timer timer;
    EventTask task;

    public DataPlayer(String stock, Observer observer) {
        this.timer = new Timer();
        task = new EventTask(stock, "2011-12-13-15-55-32");
        task.setObserver(observer);

    }

    public void runPlayback(int resolution) {

        timer.schedule(task, 0, resolution * 1000);
    }

    public void stopPlayback() {
        timer.cancel();
    }

    public void getOHLCPrice(String stock, String date){

    }
}
