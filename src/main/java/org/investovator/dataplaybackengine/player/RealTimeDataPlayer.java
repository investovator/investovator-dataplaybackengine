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


package org.investovator.dataplaybackengine.player;

import org.investovator.core.commons.events.GameEventListener;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.data.api.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.configuration.GameConfiguration;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.market.TradingSystem;
import org.investovator.dataplaybackengine.scheduler.RealTimeEventTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeDataPlayer extends DataPlayer {

    Timer timer;
    RealTimeEventTask task;

    private RealTimeDataPlayer(ArrayList<TradingDataAttribute> attributes,
                               TradingDataAttribute attributeToMatch,
                               boolean isMultiplayer, String instanceId) {
        this.timer = new Timer();
        tradingSystem = new TradingSystem(attributes, attributeToMatch);
        this.isMultiplayer = isMultiplayer;
        this.transactionDataAPI = new CompanyStockTransactionsDataImpl();
        this.gameInstance=instanceId;
        try {
            this.companyDataAPI = new CompanyDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }

    public RealTimeDataPlayer(String[] stocks, String startDate,
                              String dateFormat, ArrayList<TradingDataAttribute> attributes,
                              TradingDataAttribute attributeToMatch, boolean isMultiplayer,
                              String instanceId) {
        this(attributes, attributeToMatch, isMultiplayer,instanceId);

        task = new RealTimeEventTask(stocks, startDate, dateFormat, transactionDataAPI, attributes);

        //set the trading system as an observer
        task.setObserver(this.tradingSystem);
    }

    public RealTimeDataPlayer(String[] stocks, Date startDate, ArrayList<TradingDataAttribute> attributes,
                              TradingDataAttribute attributeToMatch, boolean isMultiplayer,
                              String instanceId) {
        this(attributes, attributeToMatch, isMultiplayer,instanceId);

        task = new RealTimeEventTask(stocks, startDate, transactionDataAPI, attributes);

        //set the trading system as an observer
        task.setObserver(this.tradingSystem);
    }

    public RealTimeDataPlayer(GameConfiguration config) {
        this(config.getPlayingSymbols(), config.getGameStartTime(), config.getInterestedAttributes(),
                config.getAttributeToMatch(), config.isMultiplayer(), config.getGameId());
        gameSpeed = config.getPlayerSpeed();
    }

    /**
     * constructor for testing the player with custom implementations of userData, companyDataAPI, transactionDataAPI
     *
     * @param stocks
     * @param startDate
     * @param attributes
     * @param attributeToMatch
     * @param isMultiplayer
     * @param userData
     * @param companyDataAPI
     * @param transactionDataAPI
     */
    public RealTimeDataPlayer(String[] stocks, Date startDate, ArrayList<TradingDataAttribute> attributes,
                              TradingDataAttribute attributeToMatch, boolean isMultiplayer,
                              UserData userData, CompanyData companyDataAPI,
                              CompanyStockTransactionsData transactionDataAPI,
                              String instanceId) {

        super(userData, companyDataAPI, transactionDataAPI);

        this.timer = new Timer();
        tradingSystem = new TradingSystem(attributes, attributeToMatch);
        this.isMultiplayer = isMultiplayer;
        this.transactionDataAPI = transactionDataAPI;
        this.companyDataAPI = companyDataAPI;
        this.gameInstance=instanceId;

        task = new RealTimeEventTask(stocks, startDate, transactionDataAPI, attributes);

        //set the trading system as an observer
        task.setObserver(this.tradingSystem);

    }


    /**
     * To set observers
     *
     * @param observer
     */
    public void setObserver(GameEventListener observer) {
        task.setObserver((PlaybackEventListener) observer);
    }

    /**
     * To remove observers
     *
     * @param observer
     */
    @Override
    public void removeObserver(GameEventListener observer) {
        task.removeObserver((PlaybackEventListener) observer);
    }

    /**
     * Start playing the data
     *
     * @param resolution the time gaps between pushing events
     */
    private void startGame(int resolution) throws GameAlreadyStartedException {

        timer.schedule(task, 0, resolution * 1000);
    }

    /**
     * Starts a game
     *
     * @throws GameAlreadyStartedException
     */
    @Override
    public void startGame() throws GameAlreadyStartedException {
        this.startGame(gameSpeed);
    }

    /**
     * Stop the data playback
     */
    public void stopGame() {
        task.cancel();
        timer.cancel();
    }

    /**
     * Executes a given order
     *
     * @param stockId  Security ID
     * @param quantity Number of stocks to buy/sell
     * @param side     Buy/Sell
     * @param userName Users username
     * @return whether the order was carried out or not
     * @throws InvalidOrderException
     * @throws UserJoinException
     */
    public boolean executeOrder(String stockId, int quantity, OrderType side, String userName) throws InvalidOrderException,
            UserJoinException {

        //order validity checks
        if (quantity > RealTimeDataPlayer.maxOrderSize) {
            throw new InvalidOrderException("Cannot place more than " + RealTimeDataPlayer.maxOrderSize + " orders.");
        }
        if (!task.getStocks().contains(stockId)) {
            throw new InvalidOrderException("Invalid stock ID : " + stockId);
        }

        Portfolio portfolio = null;
        try {
            portfolio = userData.getUserPortfolio(gameInstance,userName);

            float executedPrice = tradingSystem.executeOrder(stockId, quantity, portfolio.getCashBalance(),
                    side);

            //update the cash balance
            if (side == OrderType.BUY) {
                portfolio.boughtShares(stockId, quantity, executedPrice);
                //clean the blocked cash
                portfolio.setCashBalance(portfolio.getCashBalance() +
                        portfolio.getBlockedCash());
                portfolio.setBlockedCash(0);
            } else if (side == OrderType.SELL) {
                portfolio.soldShares(stockId, quantity, executedPrice);
            }
            userData.updateUserPortfolio(gameInstance,userName, portfolio);
        } catch (DataAccessException e) {
            e.printStackTrace();

            //if the user has not joined the game
            if (!e.getMessage().equalsIgnoreCase("Requested data not found")) {
                throw new UserJoinException("User " + userName + " has not joined the game");

            }

            return false;
        }

        return true;


    }


    /**
     * Returns the current time in the playback
     *
     * @return
     */
    public Date getCurrentTime() {
        return task.getCurrentTime();
    }

    public CompanyStockTransactionsData getTransactionsDataAPI() {
        return transactionDataAPI;
    }

    @Override
    public String getName() {
        return "Real Time Data Player";
    }
}
