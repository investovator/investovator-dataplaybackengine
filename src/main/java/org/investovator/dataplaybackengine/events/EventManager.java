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

    public boolean addObserver(PlaybackEventListener observer){
        //add only the listeners who are not already listening
        if(!listeners.contains(observer)){
            listeners.add(observer);
            return true;

        }
        else return false;

    }

    public void notifyListeners(PlaybackEvent obj) {
        for(PlaybackEventListener listener:listeners){
            listener.eventOccurred(obj);
        }
    }
}
