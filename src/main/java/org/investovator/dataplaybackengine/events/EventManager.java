package org.investovator.dataplaybackengine.events;

import org.investovator.core.commons.events.GameEvent;

import java.util.ArrayList;

/**
 * Event broadcaster for games
 *
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

    /**
     * Adds an observer
     *
     * @param observer
     * @return
     */
    public boolean addObserver(PlaybackEventListener observer) {
        //add only the listeners who are not already listening
        if (!listeners.contains(observer)) {
            listeners.add(observer);
            return true;

        } else return false;

    }

    /**
     * Remove a game observer
     *
     * @param observer
     */
    public void removeObserver(PlaybackEventListener observer) {
        if (listeners.contains(observer)) {
            listeners.remove(observer);
        }
    }

    /**
     * notifies the listeners
     *
     * @param obj
     */
    public void notifyListeners(GameEvent obj) {
        for (PlaybackEventListener listener : listeners) {
            listener.eventOccurred(obj);
        }
    }

    /**
     * Used to let the others know when the admin stops the game
     */
    public void notifyGameFinished(){
        for (PlaybackEventListener listener : listeners) {
            listener.eventOccurred(new PlaybackFinishedEvent());
        }
    }
}
