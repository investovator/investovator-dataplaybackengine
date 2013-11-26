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
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.data.api.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.configuration.GameConfiguration;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.market.TradingSystem;
import org.investovator.dataplaybackengine.scheduler.RealTimeEventTask;

import java.util.*;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class RealTimeDataPlayer extends DataPlayer {



    Timer timer;
    RealTimeEventTask task;
//    CompanyStockTransactionsData transactionDataAPI;
//    CompanyData companyDataAPI;
//    HashMap<String,Portfolio> userPortfolios;
//    TradingSystem tradingSystem;

//    private boolean isMultiplayer;

    private RealTimeDataPlayer(String[] stocks,ArrayList<TradingDataAttribute> attributes,
                               TradingDataAttribute attributeToMatch,
                               boolean isMultiplayer) {
        this.timer = new Timer();
//        userPortfolios=new HashMap<String, Portfolio>();
        tradingSystem=new TradingSystem(attributes,attributeToMatch);
        this.isMultiplayer=isMultiplayer;
        //for testing
        this.transactionDataAPI =new CompanyStockTransactionsDataImpl();
        try {
            this.companyDataAPI=new CompanyDataImpl();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        //testing end

    }

    public RealTimeDataPlayer(String[] stocks,String startDate,
                              String dateFormat,ArrayList<TradingDataAttribute> attributes,
                              TradingDataAttribute attributeToMatch,boolean isMultiplayer) {
        this(stocks,attributes,attributeToMatch,isMultiplayer);

        task = new RealTimeEventTask(stocks, startDate,dateFormat, transactionDataAPI,attributes);

        //set the trading system as an observer
        task.setObserver(this.tradingSystem);
    }

    public RealTimeDataPlayer(String[] stocks,Date startDate,ArrayList<TradingDataAttribute> attributes,
                              TradingDataAttribute attributeToMatch,boolean isMultiplayer) {
        this(stocks,attributes,attributeToMatch,isMultiplayer);

        task = new RealTimeEventTask(stocks, startDate, transactionDataAPI,attributes);

        //set the trading system as an observer
        task.setObserver(this.tradingSystem);
    }

    public RealTimeDataPlayer(GameConfiguration config){
        this(config.getPlayingSymbols(),config.getGameStartTime(),config.getInterestedAttributes(),
                config.getAttributeToMatch(),config.isMultiplayer());
        gameSpeed =config.getPlayerSpeed();
    }

    /**
     * constructor for testing the player with custom implementations of userData, companyDataAPI, transactionDataAPI
     * @param stocks
     * @param startDate
     * @param attributes
     * @param attributeToMatch
     * @param isMultiplayer
     * @param userData
     * @param companyDataAPI
     * @param transactionDataAPI
     */
    public RealTimeDataPlayer(String[] stocks,Date startDate,ArrayList<TradingDataAttribute> attributes,
                              TradingDataAttribute attributeToMatch,boolean isMultiplayer,
                              UserData userData, CompanyData companyDataAPI,
                              CompanyStockTransactionsData transactionDataAPI){

        super(userData,companyDataAPI,transactionDataAPI);

        this.timer = new Timer();
//        userPortfolios=new HashMap<String, Portfolio>();
        tradingSystem=new TradingSystem(attributes,attributeToMatch);
        this.isMultiplayer=isMultiplayer;
        //for testing
        this.transactionDataAPI =transactionDataAPI;
        this.companyDataAPI=companyDataAPI;
        //testing end

        task = new RealTimeEventTask(stocks, startDate, transactionDataAPI,attributes);

        //set the trading system as an observer
        task.setObserver(this.tradingSystem);

    }


    /**
     * To set observers
     *
     * @param observer
     */
    public void setObserver(GameEventListener observer){
        task.setObserver((PlaybackEventListener)observer);
    }

    @Override
    public void removeObserver(GameEventListener observer) {
        task.removeObserver((PlaybackEventListener)observer);
    }

    /**
     * Start playing the data
     * @param resolution the time gaps between pushing events
     */
    private void startGame(int resolution) throws GameAlreadyStartedException {

        timer.schedule(task, 0, resolution * 1000);
        //TODO- change the RealTimeEventTask to check for the resolution when incrementing its time
    }

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

//    /**
//     *
//     * @return Company StockId and Name pairs
//     * @throws org.investovator.core.data.exeptions.DataAccessException
//     */
//    public HashMap<String,String> getStocksList() throws DataAccessException {
//
//        return companyDataAPI.getCompanyIDsNames();
//
//
//    }

    /**
     * Allows a user to join the running game
     *
     * @return
     */
    public boolean joinGame(PlaybackEventListener observer,String userName) throws UserAlreadyJoinedException {

        //if a non-admin user tries to connect to a single player game
        //todo - implement using authenticator
//        if(!isMultiplayer && user!=admin){
//            throw new UserJoinException("You don't have sufficient privileges to enter this game");
//        }

        boolean joined=false;

//        //check whether the user has already joined the game
//        if(!userPortfolios.containsKey(userName)){
//            userPortfolios.put(userName,new PortfolioImpl(userName,RealTimeDataPlayer.initialCredit,0));
//            joined=true;
//            setObserver(observer);
//        }
//        else{
////            throw new UserAlreadyJoinedException(userName);
//            //todo - remove the fpllowing line
//            setObserver(observer);
//        }

        try {
            userData.updateUserPortfolio(userName,new PortfolioImpl(userName, DailySummaryDataPLayer.initialCredit,0));
            joined=true;
            setObserver(observer);
            usersList.add(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }



        return joined;

    }


    public boolean executeOrder(String stockId, int quantity, OrderType side,String userName) throws InvalidOrderException,
            UserJoinException {

        //order validity checks
        if(quantity>RealTimeDataPlayer.maxOrderSize){
            throw new InvalidOrderException("Cannot place more than "+RealTimeDataPlayer.maxOrderSize+" orders.");
        }
        if(!task.getStocks().contains(stockId)){
            throw new InvalidOrderException("Invalid stock ID : "+stockId);
        }

//        //if the user has not joined the game
//        if(!userPortfolios.containsKey(userName)){
//            throw new UserJoinException("User "+userName+ " has not joined the game");
//
//        }
//
//
//        float executedPrice= tradingSystem.executeOrder(stockId,
//                quantity,userPortfolios.get(userName).getCashBalance(),side);
//
//        Portfolio portfolio=userPortfolios.get(userName);
//        //update the cash balance
//        if(side==OrderType.BUY){
//            portfolio.boughtShares(stockId,quantity,executedPrice);
//            //clean the blocked cash
//            portfolio.setCashBalance(portfolio.getCashBalance()+
//                    portfolio.getBlockedCash());
//            portfolio.setBlockedCash(0);
//        }
//        else if(side==OrderType.SELL){
//            portfolio.soldShares(stockId,quantity,executedPrice);
//        }

        Portfolio portfolio= null;
        try {
            portfolio = userData.getUserPortfolio(userName);

            float executedPrice= tradingSystem.executeOrder(stockId,quantity,portfolio.getCashBalance(),
                    side);

            //update the cash balance
            if(side==OrderType.BUY){
                portfolio.boughtShares(stockId,quantity,executedPrice);
                //clean the blocked cash
                portfolio.setCashBalance(portfolio.getCashBalance()+
                        portfolio.getBlockedCash());
                portfolio.setBlockedCash(0);
            }
            else if(side==OrderType.SELL){
                portfolio.soldShares(stockId,quantity,executedPrice);
            }
            userData.updateUserPortfolio(userName,portfolio);
        } catch (DataAccessException e) {
            e.printStackTrace();

            //if the user has not joined the game
            if(!e.getMessage().equalsIgnoreCase("Requested data not found")){
                throw new UserJoinException("User "+userName+ " has not joined the game");

            }

            return false;
        }

        return true;


    }




//    public void setTransactionDataAPI(CompanyStockTransactionsData api ) {
//        task.setDataApi(api);
//    }



    /**
     * Returns the current time in the playback
     * @return
     */
    public Date getCurrentTime(){
        return task.getCurrentTime();
    }

    public CompanyStockTransactionsData getTransactionsDataAPI() {
        return transactionDataAPI;
    }


//    /**
//     * Informs whether the given user has already joined the current game
//     * @param name
//     * @return
//     */
//    public boolean hasUserJoined(String name){
//        return userPortfolios.containsKey(name);
//    }

    /**
     * returns the maximum size (in money) of the order that can be placed
     */
    public int getMaxOrderSize(){
        return maxOrderSize;
    }

    /**
     * returns the initial account balance
     */
    public int getInitialCredit(){
        return initialCredit;
    }

    @Override
    public String getName() {
        return "Real Time Data Player";
    }
}
