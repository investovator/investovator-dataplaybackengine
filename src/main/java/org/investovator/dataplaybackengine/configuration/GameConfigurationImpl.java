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

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class GameConfigurationImpl implements GameConfiguration {

    private Date gameStartTime;
    private String[] playingSymbols;
    private boolean multiplayer;
    public ArrayList<TradingDataAttribute> interestedAttributes;
    public PlayerTypes playerType;
    public TradingDataAttribute attributeToMatch;
    public String description;
    public int gameSpeed;


    public GameConfigurationImpl(Date gameStartTime, String[] playingSymbols, boolean multiplayer,
                                 GameTypes gameConf) {
        this.gameStartTime = gameStartTime;
        this.playingSymbols = playingSymbols;
        this.multiplayer = multiplayer;
        this.interestedAttributes = gameConf.getInterestedAttributes();
        this.playerType = gameConf.getPlayerType();
        this.attributeToMatch = gameConf.getAttributeToMatch();
        this.description = gameConf.getDescription() + "with " + gameConf.getInterestedAttributes().toString();
        this.gameSpeed = gameConf.getPlayerSpeed();
    }

    @Override
    public Date getGameStartTime() {
        return gameStartTime;
    }

    @Override
    public String[] getPlayingSymbols() {
        return playingSymbols;
    }

    @Override
    public boolean isMultiplayer() {
        return multiplayer;
    }

    @Override
    public ArrayList<TradingDataAttribute> getInterestedAttributes() {
        return interestedAttributes;
    }

    @Override
    public PlayerTypes getPlayerType() {
        return playerType;
    }

    @Override
    public int getPlayerSpeed() {
        return gameSpeed;
    }

    @Override
    public TradingDataAttribute getAttributeToMatch() {
        return attributeToMatch;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
