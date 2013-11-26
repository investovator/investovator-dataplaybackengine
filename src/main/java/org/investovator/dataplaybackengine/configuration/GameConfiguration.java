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


package org.investovator.dataplaybackengine.configuration;

import java.util.Date;

/**
 * Used to pass game configurations
 *
 * @author: ishan
 * @version: ${Revision}
 */
public interface GameConfiguration extends GameTypes {

    /**
     * Returns the start time for the game
     *
     * @return
     */
    public Date getGameStartTime();

    /**
     * Returns the array of symbols that the game is played on
     *
     * @return
     */
    public String[] getPlayingSymbols();


    /**
     * Whether this is a multiplayer game or not
     *
     * @return
     */
    public boolean isMultiplayer();

}
