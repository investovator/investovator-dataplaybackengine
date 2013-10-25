package org.investovator.dataplaybackengine.events;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventManager {

    private ArrayList<PlaybackEventListener> listeners;

    public enum RealTimePlayerStates {
        GAME_OVER
    }

    public EventManager() {
        this.listeners = new ArrayList<PlaybackEventListener>();
    }

    public void addObserver(PlaybackEventListener observer){
        listeners.add(observer);
        System.out.println(observer.hashCode());
    }

    public void notifyListeners(PlaybackEvent obj) {
        for(PlaybackEventListener listener:listeners){
            listener.eventOccurred(obj);
        }
    }
}
