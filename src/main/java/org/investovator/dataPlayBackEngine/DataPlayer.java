package org.investovator.dataPlayBackEngine;

import org.investovator.dataPlayBackEngine.scheduler.EventTask;

import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayer {

    Timer timer;

    public DataPlayer() {
        this.timer = new Timer();
    }

    public void runPlayback(int resolution, String stock) {

        EventTask task = new EventTask(stock, "2011-12-13-15-55-32");
        timer.schedule(task, 0, resolution * 1000);
        try {
            Thread.sleep(5000);
            stopPlayback();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void stopPlayback() {
        timer.cancel();
    }

    public void getOHLCPrice(String stock, String date){

    }
}
