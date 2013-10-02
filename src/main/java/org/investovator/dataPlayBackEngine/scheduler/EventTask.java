package org.investovator.dataPlayBackEngine.scheduler;

import java.util.TimerTask;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventTask extends TimerTask {
    int i=0;
    @Override
    public void run() {

        System.out.println("Task run :"+i++);
    }
}
