package org.investovator.dataPlayBackEngine.scheduler;

import java.util.TimerTask;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventTask extends TimerTask {
    int i=0;

    //stock to playback
    private String stockId;

    public EventTask(String stockId) {
        this.stockId = stockId;
    }

    @Override
    public void run() {

        System.out.println("Querying for "+stockId+" :"+i++);
    }
}
