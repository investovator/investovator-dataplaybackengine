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


package org.investovator.dataplaybackengine.scheduler;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.PlaybackFinishedEvent;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.GameFinishedException;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;

import java.util.TimerTask;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryEventTask extends TimerTask {

    private EventManager eventManager;
    private DailySummaryDataPLayer player;

    public DailySummaryEventTask(DailySummaryDataPLayer player) {
        eventManager = new EventManager();
        this.player = player;

    }

    @Override
    public void run() {
        StockUpdateEvent[] events=null;

        //if the game has not started yet
        if (!player.isGameStarted()) {
            try {
                events = player.startGame();

            } catch (GameAlreadyStartedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } else {
            try {
                events = player.playNextDay();

            } catch (GameFinishedException e) {
                //notify the player
                eventManager.notifyListeners(new PlaybackFinishedEvent());
                //stop the timer
                this.cancel();
            }
        }
        if(events!=null){

            for (StockUpdateEvent event : events) {
                eventManager.notifyListeners(event);
            }
        }
    }

    public void setObserver(PlaybackEventListener observer) {
        eventManager.addObserver(observer);
    }
}
