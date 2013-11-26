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

import org.investovator.core.commons.events.GameEvent;
import org.investovator.core.commons.events.GameEventListener;
import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.commons.utils.PortfolioImpl;
import org.investovator.core.data.api.*;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.core.data.exeptions.DataAccessException;
import org.investovator.dataplaybackengine.data.BogusCompnayDataGenerator;
import org.investovator.dataplaybackengine.data.BogusHistoryDataGenerator;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.exceptions.GameAlreadyStartedException;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;
import org.investovator.dataplaybackengine.exceptions.UserAlreadyJoinedException;
import org.investovator.dataplaybackengine.exceptions.UserJoinException;
import org.investovator.dataplaybackengine.market.OrderType;
import org.investovator.dataplaybackengine.market.TradingSystem;

import java.util.*;

/**
 * This is the parent class of all the data players. Any data player should extend this.
 * @author: ishan
 * @version: ${Revision}
 */
public abstract class DataPlayer {

    protected CompanyStockTransactionsData transactionDataAPI;
    protected CompanyData companyDataAPI;

    //to store the game players
    protected ArrayList<String> usersList;

    protected TradingSystem tradingSystem;

    protected boolean isMultiplayer;

    protected UserData userData;


    //amount of money a person get at the begining
    protected static int initialCredit=10000;

    //max amount of stocks that a person can buy/sell
    protected static int maxOrderSize=5000;

    //the default speed of the game
    protected int gameSpeed =1;

    //set the game start time
    long startTime;

    public DataPlayer() {
        this.transactionDataAPI =new CompanyStockTransactionsDataImpl();
        try {
            this.companyDataAPI=new CompanyDataImpl();
            this.userData=new UserDataImpl();

        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        //set the game start time
        this.startTime=System.currentTimeMillis();

        //initialize the users list
        this.usersList=new ArrayList<>();



    }

    protected DataPlayer(UserData userData, CompanyData companyDataAPI,
                         CompanyStockTransactionsData transactionDataAPI) {
        this.userData = userData;
        this.companyDataAPI = companyDataAPI;
        this.transactionDataAPI = transactionDataAPI;

        //set the game start time
        this.startTime=System.currentTimeMillis();

        //initialize the users list
        this.usersList=new ArrayList<>();
    }

    /**
     * To set observers
     *
     * @param observer
     */
    abstract public void setObserver(GameEventListener observer);

    /**
     * Removes the given game event listener
     * @param observer
     */
    abstract public void removeObserver(GameEventListener observer);

    /**
     * Starts the game. The default play speed of the player will be used
     */
    abstract public void startGame() throws GameAlreadyStartedException;

    /**
     * to stop the game
     */
    abstract public void stopGame();

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
    abstract public boolean executeOrder(String stockId, int quantity, OrderType side,String userName) throws
            InvalidOrderException,
            UserJoinException;

    /**
     * Returns the portfolio of the requested user
     *
     * @param userName
     * @return
     * @throws UserJoinException
     */
    public Portfolio getMyPortfolio(String userName) throws UserJoinException {

        //check if the user has joined the game
        if(!usersList.contains(userName)){
            throw new UserJoinException("User "+userName+ " has not joined the game");
        }


        Portfolio portfolio=null;
        try {
             portfolio = userData.getUserPortfolio(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new UserJoinException("User "+userName+ " has not joined the game");

        }

        return portfolio;
    }

    /**
     * Returns the current time in the playback
     * @return
     */
    abstract public Date getCurrentTime();


    /**
     * returns whether this is a multplayer game or not
     * @return
     */
    public boolean isMultiplayer() {
        return this.isMultiplayer;
    }


    /**
     * Informs whether the given user has already joined the current game
     * @param name
     * @return
     */
    public boolean hasUserJoined(String name){

        if(usersList.contains(name)){
            return true;
        }
        else{
            return false;
        }

    }

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


    /**
     * Returns a name for the player
     *
     * @return
     */
    abstract public String getName();

    /**
     * returns the total market turnover
     * @return
     */
    public float getMarketTurnover(){
        return this.tradingSystem.getMarketTurnover();
    }

    /**
     *
     * Returns the total number of trades done
     * @return
     */
    public int getTotalTrades(){
        return tradingSystem.getTotalTrades();
    }

    /**
     * Returns the run time of the game
     * @return
     */
    public long getGameRuntime(){
        return (System.currentTimeMillis()-this.startTime);
    }

    /**
     * returns portfolios of all the users
     * @return
     */
    public synchronized ArrayList<Portfolio> getAllPortfolios(){
        ArrayList<Portfolio> portfolios=new ArrayList<>();
        for(String username:usersList){
            try {
                portfolios.add(userData.getUserPortfolio(username));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return portfolios;
    }

    /**
     *returns the current price of the requested stock
     * @param stock ID of the stock
     * @return
     */
    public float getStockPrice(String stock){
        return tradingSystem.getStockPrice(stock);
    }

    /**
     * Allows a user to join the running game
     *
     * @return
     */
    public boolean joinGame(PlaybackEventListener observer,String userName) throws UserAlreadyJoinedException {

        boolean joined=false;

        try {
            userData.updateUserPortfolio(userName,new PortfolioImpl(userName, DataPlayer.initialCredit,0));
            joined=true;
            setObserver(observer);
            usersList.add(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }



        return joined;

    }

}

