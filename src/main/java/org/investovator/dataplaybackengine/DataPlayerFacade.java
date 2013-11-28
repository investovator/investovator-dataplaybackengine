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

import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.core.data.exeptions.DataNotFoundException;
import org.investovator.dataplaybackengine.configuration.GameConfiguration;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.player.DailySummaryDataPLayer;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.player.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.investovator.dataplaybackengine.utils.StockUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayerFacade {

    private PlayerTypes playerType;
    private DataPlayer player;

    private int DATA_ITEMS_TO_QUERY = 1000;
    private int NUM_OF_DAYS_TO_QUERY = 1000;

    /**
     * Creates a data player up on the given configuration
     *
     * @param config
     * @return
     */
    public DataPlayer createPlayer(GameConfiguration config) {
        //if a daily summary player is needed
        if (PlayerTypes.DAILY_SUMMARY_PLAYER == config.getPlayerType()) {
            this.player = new DailySummaryDataPLayer(config);
            this.playerType = PlayerTypes.DAILY_SUMMARY_PLAYER;
        } else if (PlayerTypes.REAL_TIME_DATA_PLAYER == config.getPlayerType()) {
            this.player = new RealTimeDataPlayer(config);
            this.playerType = PlayerTypes.REAL_TIME_DATA_PLAYER;
        }

        return player;
    }

    /**
     * Returns the game ID for the game
     * @return
     */
    public String getGameId(){
        return this.player.getGameInstance();
    }


    /**
     * Returns the type of the player that is run
     *
     * @return
     */
    public PlayerTypes getCurrentPlayerType() {
        return this.playerType;
    }

    /**
     * Returns the current player
     *
     * @return
     */
    public DataPlayer getCurrentPlayer() {

        return this.player;
    }

    /**
     * Returns data up to the present date of the currently running player
     *
     * @return
     */
    public StockTradingData getDataUpToToday(String symbol, Date startingDate,
                                             ArrayList<TradingDataAttribute> attributes)
            throws DataAccessException, DataNotFoundException {

        if (this.playerType == PlayerTypes.DAILY_SUMMARY_PLAYER) {
            Date startDate = DateUtils.decrementTimeByDays(NUM_OF_DAYS_TO_QUERY, startingDate);
            return StockUtils.getDataBetweenDates(CompanyStockTransactionsData.DataType.OHLC,
                    symbol, startDate, this.player.getCurrentTime(), DATA_ITEMS_TO_QUERY, attributes);
        }
        if (this.playerType == PlayerTypes.REAL_TIME_DATA_PLAYER) {
            Date startDate = DateUtils.decrementTimeByDays(NUM_OF_DAYS_TO_QUERY, startingDate);
            return StockUtils.getDataBetweenDates(CompanyStockTransactionsData.DataType.TICKER,
                    symbol, startDate, this.player.getCurrentTime(), DATA_ITEMS_TO_QUERY, attributes);
        }

        return null;

    }

    /**
     * Starts the game
     *
     * @return whether the game was started or not
     */
    public boolean startGame() {
        try {
            this.player.startGame();
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Stops the game
     */
    public void stopGame() {
        this.player.stopGame();
    }


}
