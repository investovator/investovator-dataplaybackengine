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
import org.investovator.core.data.api.CompanyData;
import org.investovator.core.data.api.CompanyStockTransactionsData;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.api.utils.StockTradingData;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.core.data.exeptions.DataNotFoundException;
import org.investovator.dataplaybackengine.configuration.GameConfiguration;
import org.investovator.dataplaybackengine.events.EventManager;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.PlaybackFinishedEvent;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.market.TradingSystem;
import org.investovator.dataplaybackengine.scheduler.DailySummaryEventTask;
import org.investovator.dataplaybackengine.utils.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DailySummaryDataPLayer extends DataPlayer {

    //used to determine the cache size
    private static int CACHE_SIZE = 100;

    //to keep track of the date
    Date today;
    //to keep the game state
    boolean gameStarted;
    //to keep track of the attributes needed
    ArrayList<TradingDataAttribute> attributes;
    //event broadcaster
    private EventManager eventManager;

    //multiplayer related event tasks
    private DailySummaryEventTask task;
    private Timer timer;

    //to cache the stock trading data items
    ConcurrentHashMap<String, HashMap<Date, HashMap<TradingDataAttribute, String>>> ohlcDataCache;

    public DailySummaryDataPLayer(String[] stocks, ArrayList<TradingDataAttribute> attributes,
                                  TradingDataAttribute attributeToMatch, boolean isMultiplayer) {

        this.ohlcDataCache = new ConcurrentHashMap<String, HashMap<Date, HashMap<TradingDataAttribute, String>>>();
        this.attributes = attributes;
        this.tradingSystem = new TradingSystem(attributes, attributeToMatch);

        this.isMultiplayer = isMultiplayer;

        eventManager = new EventManager();


        if (isMultiplayer) {
            task = new DailySummaryEventTask(this);
            this.timer = new Timer();

        }

        //initialize the stocks
        for (String stock : stocks) {
            ohlcDataCache.put(stock, new HashMap<Date, HashMap<TradingDataAttribute, String>>());
        }

        gameStarted = false;

    }


    public DailySummaryDataPLayer(GameConfiguration config) {
        this(config.getPlayingSymbols(), config.getInterestedAttributes(), config.getAttributeToMatch(),
                config.isMultiplayer());
        gameSpeed = config.getPlayerSpeed();
        today = config.getGameStartTime();
    }

    /**
     * constructor for testing the player configured with a GameConfiguration and
     * with custom implementations of userData, companyDataAPI, transactionDataAPI
     *
     * @param configuration
     * @param userData
     * @param companyDataAPI
     * @param transactionDataAPI
     */
    public DailySummaryDataPLayer(GameConfiguration configuration, UserData userData, CompanyData companyDataAPI,
                                  CompanyStockTransactionsData transactionDataAPI) {
        super(userData, companyDataAPI, transactionDataAPI);


        gameSpeed = configuration.getPlayerSpeed();
        today = configuration.getGameStartTime();
        this.ohlcDataCache = new ConcurrentHashMap<String, HashMap<Date, HashMap<TradingDataAttribute, String>>>();
        this.attributes = configuration.getInterestedAttributes();
        this.tradingSystem = new TradingSystem(attributes, configuration.getAttributeToMatch());

        this.isMultiplayer = configuration.isMultiplayer();

        eventManager = new EventManager();


        if (isMultiplayer) {
            task = new DailySummaryEventTask(this);
            this.timer = new Timer();

        }

        //initialize the stocks
        for (String stock : configuration.getPlayingSymbols()) {
            ohlcDataCache.put(stock, new HashMap<Date, HashMap<TradingDataAttribute, String>>());
        }

        gameStarted = false;


    }

    /**
     * constructor for testing the player with custom implementations of userData, companyDataAPI, transactionDataAPI
     *
     * @param stocks
     * @param attributes
     * @param attributeToMatch
     * @param isMultiplayer
     * @param userData
     * @param companyDataAPI
     * @param transactionDataAPI
     */
    public DailySummaryDataPLayer(String[] stocks, ArrayList<TradingDataAttribute> attributes,
                                  TradingDataAttribute attributeToMatch, boolean isMultiplayer,
                                  UserData userData, CompanyData companyDataAPI,
                                  CompanyStockTransactionsData transactionDataAPI) {
        super(userData, companyDataAPI, transactionDataAPI);

        this.ohlcDataCache = new ConcurrentHashMap<String, HashMap<Date, HashMap<TradingDataAttribute, String>>>();
        this.attributes = attributes;
        this.tradingSystem = new TradingSystem(attributes, attributeToMatch);

        this.isMultiplayer = isMultiplayer;

        eventManager = new EventManager();

        if (isMultiplayer) {
            task = new DailySummaryEventTask(this);
            this.timer = new Timer();

        }

        //initialize the stocks
        for (String stock : stocks) {
            ohlcDataCache.put(stock, new HashMap<Date, HashMap<TradingDataAttribute, String>>());
        }

        gameStarted = false;


    }

    /**
     * <p/>
     * Starts the game.
     * <p/>
     *
     * @return list of events needed to start the game
     * @throws GameAlreadyStartedException
     */
    public void startGame() throws GameAlreadyStartedException {

//        ArrayList<StockUpdateEvent> events = new ArrayList<StockUpdateEvent>();

        if (isMultiplayer() && !task.isRunning()) {
            timer.schedule(task, 0, gameSpeed * 1000);
        } else {
            //if the game has not started yet
            if (!gameStarted) {
                //search all the stocks
                for (String stock : ohlcDataCache.keySet()) {

                    try {
                        StockTradingData data = transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC,
                                stock, today, new Date(), DailySummaryDataPLayer.CACHE_SIZE, attributes);

                        //if any data was returned
                        if (data != null) {

                            //fire the event
                            eventManager.notifyListeners(new StockUpdateEvent(stock, data.getTradingDataEntry(today), today));

                            //add the data to the Trading system as well
                            //only add if price information exists
                            if (data.getTradingDataEntry(today) != null) {
                                tradingSystem.updateStockPrice(stock, data.getTradingDataEntry(today));
                            }
                            //remove that entry from map
                            data.getTradingData().remove(today);

                            //add the rest of the data to the cache
                            ohlcDataCache.put(stock, data.getTradingData());

                        }

                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }

                //indicate that the game started
                gameStarted = true;


            } else {
                throw new GameAlreadyStartedException(this);
            }

            today = DateUtils.incrementTimeByDays(1, today);
        }

    }

    @Override
    public void stopGame() {
        if (isMultiplayer()) {
            timer.cancel();
            task.cancel();
        }
    }

    /**
     * Used to set the game starting date at the start of the game.
     *
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.today = startDate;

    }

    /**
     * Used to set the game starting date at the start of the game.
     *
     * @param startDate
     * @param dateFormat
     * @throws ParseException
     */
    public void setStartDate(String startDate, String dateFormat) throws ParseException {
        today = DateUtils.dateStringToDateObject(startDate, dateFormat);

    }

    /**
     * Fires the  stock events of the next day. If a stock does not contain the price information
     * for the requested day, its <b>data</> map will be null.
     *
     * @return An array of StockUpdateEvent's if the data is present for at least a single stock. If data is not present
     *         for any stock, returns a null.
     */
    public void playNextDay() {

        //to track data availability
        boolean dataExists = false;

        //iterate all the stocks
        for (String stock : ohlcDataCache.keySet()) {
            //if the date is in the cache

            //to check the data avalability ofr this stock
            boolean hasDAtaForStock = false;
            if (ohlcDataCache.get(stock).containsKey(today)) {
//                events.add(new StockUpdateEvent(stock,ohlcDataCache.get(stock).get(today),today));

                //fire the event
                eventManager.notifyListeners(new StockUpdateEvent(stock, ohlcDataCache.get(stock).get(today), today));

                //add the data to the Trading system as well
                tradingSystem.updateStockPrice(stock, ohlcDataCache.get(stock).get(today));

                //remove the used items from the cache
                ohlcDataCache.get(stock).remove(today);

                hasDAtaForStock = true;


            } else {

                //fire the event
                eventManager.notifyListeners(new StockUpdateEvent(stock, null, today));

            }

            //if at least a single stock has data
            if (hasDAtaForStock) {
                dataExists = true;
            }

        }

        //if no data has been found in the cache
        if (!dataExists) {

            //to track whether at least a single stock has data in the future
            boolean hasData = false;

            //search all the stocks
            for (String stock : ohlcDataCache.keySet()) {

                try {

                    StockTradingData data = transactionDataAPI.getTradingData(CompanyStockTransactionsData.DataType.OHLC,
                            stock, today, new Date(), DailySummaryDataPLayer.CACHE_SIZE, attributes);

                    //if any data was returned
                    if (data != null && data.getTradingData() != null) {
                        HashMap<TradingDataAttribute, String> dataMap = data.getTradingDataEntry(today);
                        //get the relevant data

                        //fire the event
                        eventManager.notifyListeners(new StockUpdateEvent(stock, dataMap, today));

                        //add the data to the Trading system as well
                        if (dataMap != null) {
                            tradingSystem.updateStockPrice(stock, dataMap);
                        }

                        //remove that entry from map
                        data.getTradingData().remove(today);

                        //clear the cache first
                        ohlcDataCache.remove(stock);
                        //add the rest of the data to the cache
                        ohlcDataCache.put(stock, data.getTradingData());

                        hasData = true;

                    }

                } catch (DataNotFoundException e) {
                    e.printStackTrace();
                } catch (DataAccessException e) {
                    hasData = false;
                }
            }

            //if none of the stocks has future data
            if (!hasData) {
                //notify the player
                eventManager.notifyListeners(new PlaybackFinishedEvent());
                if (isMultiplayer()) {
                    this.task.cancel();
                    this.timer.cancel();
                }
            }

        }


        today = DateUtils.incrementTimeByDays(1, today);

    }


    /**
     * Be cautious enough to call this method before playNextDay() has been called.
     * Else it will show the next day
     *
     * @return next day in the game
     */
    public Date getNextDay() {
        return today;
    }

    /**
     * @return current day in the game
     */
    public Date getCurrentTime() {
        return DateUtils.decrementTimeByDays(1, today);
    }

    @Override
    public String getName() {
        return "Daily Summary Data Player";
    }

    /**
     * Executes a given order
     *
     * @param stockId  Security ID
     * @param quantity Number of stocks to trade
     * @param side     Buy/Sell
     * @param userName user who is placing the order
     * @return whether the order or executed or not
     * @throws InvalidOrderException
     * @throws UserJoinException
     */
    public boolean executeOrder(String stockId, int quantity, OrderType side, String userName)
            throws InvalidOrderException,
            UserJoinException {

        //order validity checks
        if (quantity > DailySummaryDataPLayer.maxOrderSize) {
            throw new InvalidOrderException("Cannot place more than " + DailySummaryDataPLayer.maxOrderSize + " orders.");
        }
        if (!ohlcDataCache.containsKey(stockId)) {
            throw new InvalidOrderException("Invalid stock ID : " + stockId);
        }


        Portfolio portfolio = null;
        try {
            portfolio = userData.getUserPortfolio(userName);

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
            userData.updateUserPortfolio(userName, portfolio);

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
     * To set observers
     *
     * @param observer
     */
    public void setObserver(GameEventListener observer) {

        eventManager.addObserver((PlaybackEventListener) observer);
    }

    /**
     * To remove observer
     *
     * @param observer
     */
    @Override
    public void removeObserver(GameEventListener observer) {
        eventManager.removeObserver((PlaybackEventListener) observer);
    }

    /**
     * Indicates whether the game is started or not
     */
    public boolean isGameStarted() {
        return gameStarted;
    }


    public CompanyStockTransactionsData getTransactionsDataAPI() {
        return this.transactionDataAPI;
    }


}
