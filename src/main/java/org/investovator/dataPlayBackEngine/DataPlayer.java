package org.investovator.dataPlayBackEngine;

import org.investovator.dataPlayBackEngine.scheduler.EventTask;

import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayer {

    public void runPlayback(int resolution){
        Timer timer =new Timer();
        EventTask task=new EventTask();
        timer.schedule(task,0,resolution*1000);
    }

}
