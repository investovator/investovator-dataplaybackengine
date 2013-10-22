package org.investovator.dataplaybackengine.events;

import java.util.Observable;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventManager extends Observable {

    public enum RealTimePlayerStates {
        GAME_OVER
    }

    public void notifyListeners(Object obj) {
        setChanged();
        notifyObservers(obj);
    }
}
