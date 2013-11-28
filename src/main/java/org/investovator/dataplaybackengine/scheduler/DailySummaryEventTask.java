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

import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;

import java.util.TimerTask;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryEventTask extends TimerTask {

    private DailySummaryDataPLayer player;
    private boolean running;

    public DailySummaryEventTask(DailySummaryDataPLayer player) {
        this.player = player;

    }

    @Override
    public void run() {
        running = true;

        //if the game has not started yet
        if (!player.isGameStarted()) {
            try {
                player.startGame();

            } catch (GameAlreadyStartedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } else {
            player.playNextDay();

        }
    }


    /**
     * Returns whether the task is running
     *
     * @return
     */
    public boolean isRunning() {
        return running;
    }
}
