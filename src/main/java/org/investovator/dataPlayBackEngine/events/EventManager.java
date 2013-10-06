package org.investovator.dataPlayBackEngine.events;

import java.util.Observable;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventManager extends Observable {

    public void notifyListners(Object obj){
        setChanged();
        notifyObservers(obj);
    }
}
