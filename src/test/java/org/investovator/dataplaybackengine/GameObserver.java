/*
 * investovator, Stock Market Gaming Framework
 *     Copyright (C) 2013  investovator
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.investovator.dataplaybackengine;

import org.investovator.dataplaybackengine.events.PlaybackEvent;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.PlaybackFinishedEvent;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;

import java.util.ArrayList;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class GameObserver implements PlaybackEventListener {

    private static boolean gameFinished;
    private static ArrayList<PlaybackEvent> events;

    public GameObserver() {
        gameFinished=false;
        events=new ArrayList<PlaybackEvent>();
    }

    @Override
    public void eventOccurred(PlaybackEvent event) {
        System.out.println("Event received....");
        if(event instanceof PlaybackFinishedEvent){
            gameFinished=true;
            events.add((PlaybackEvent)event);
        }
        else if (event instanceof StockUpdateEvent){
            events.add((StockUpdateEvent)event);
        }
    }


    public boolean isGameFinished() {
        return gameFinished;
    }

    public ArrayList<PlaybackEvent> getEvents() {
        return events;
    }
}
