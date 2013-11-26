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
 * @author: ishan
 * @version: ${Revision}
 */
public abstract class DataPlayer {

    protected CompanyStockTransactionsData transactionDataAPI;
    protected CompanyData companyDataAPI;

//    protected     HashMap<String,Portfolio> userPortfolios;

    protected ArrayList<String> usersList;


    protected TradingSystem tradingSystem;

    protected boolean isMultiplayer;

    protected UserData userData;


    //amount of money a person get at the begining
    protected static int initialCredit=10000;

    //max amount of stocks that a person can buy/sell
    protected static int maxOrderSize=5000;

    protected int gameSpeed =1;

    //set the game start time
    long startTime;

    public DataPlayer() {
        this.transactionDataAPI =new CompanyStockTransactionsDataImpl();
        try {
            this.companyDataAPI=new CompanyDataImpl();
            this.userData=new UserDataImpl();

            //todo - testing code to clear DB manually -should be moved to controller
//            DataStorage storage = new DataStorageImpl();
//            storage.resetDataStorage();
            //todo - end of testing


        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        //set the game start time
        this.startTime=System.currentTimeMillis();

        //initialize the users list
        this.usersList=new ArrayList<>();



    }

    protected DataPlayer(UserData userData, CompanyData companyDataAPI, CompanyStockTransactionsData transactionDataAPI) {
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
    abstract public void setObserver(PlaybackEventListener observer);

    /**
     * Starts the game. The default play speed of the player will be used
     */
    abstract public void startGame() throws GameAlreadyStartedException;

    /**
     * to stop the game
     */
    abstract public void stopGame();

//    /**
//     * Starts the game.
//     * @param speed Defines the speed of the game. Usually the minimum delay between checking for  new events
//     */
//    abstract private void startGame(int speed) throws GameAlreadyStartedException;

//    /**
//     * Stop the data playback
//     */
//    abstract public void stopPlayback();

    abstract public boolean executeOrder(String stockId, int quantity, OrderType side,String userName) throws InvalidOrderException,
            UserJoinException;

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


//
//        //if the user has not joined the game
//        if(!userPortfolios.containsKey(userName)){
//            throw new UserJoinException("User "+userName+ " has not joined the game");
//
//        }

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

//        try{
//
//            Portfolio portfolio = userData.getUserPortfolio(name);
//        }
//     catch (DataAccessException e) {
//        e.printStackTrace();
//
//        //if the user has not joined the game
//        if(!e.getMessage().equalsIgnoreCase("Requested data not found")){
//            return false;
//
//        }
//
//    }

        if(usersList.contains(name)){
            return true;
        }
        else{
            return false;
        }


//        return userPortfolios.containsKey(name);
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
     * Returns the starting date/time and the ending date/time which has data for all the given set of stocks
     *
     * @param stocks
     * @return First element contains start date, second element contains the ending date,
     * a null may be returned for any of the dates if no common date is found
     */
    public Date[] getCommonStartingAndEndDates(String[] stocks, CompanyStockTransactionsData.DataType type) throws DataAccessException {
        Date startDate=null;
        Date endDate=null;

        //Date(in order) - [stocks]
        TreeMap<Date,ArrayList<String>> counter=new TreeMap<Date, ArrayList<String>>();

        //iterate all the stocks
        for(String stock:stocks){
            //get all the dates for that stock
            Date[] dates=transactionDataAPI.getDataDaysRange(type,stock);

            //add them to the map
            for(Date date:dates){
                //if the arraylist has not been initialized
                if(!counter.containsKey(date)){
                    counter.put(date,new ArrayList<String>());
                }

                ArrayList<String> stockList=counter.get(date);
                stockList.add(stock);
                counter.put(date,stockList);
            }
        }

        //iterate the map in the ascending order and determine the largest date which has all the stocks
        for(Date date:counter.keySet()){
            if(counter.get(date).size()==stocks.length){
                startDate=date;
                break;
            }
        }

        //reverse order the collection first
        Comparator cmp = Collections.reverseOrder();
        TreeMap<Date,ArrayList<String>> reverseOrderedMap=new TreeMap<Date,ArrayList<String>>(cmp);
        reverseOrderedMap.putAll(counter);

        //iterate the map in the descending order and determine the biggest date which has all the stocks
        for(Date date:reverseOrderedMap.keySet()){
            if(reverseOrderedMap.get(date).size()==stocks.length){
                endDate=date;
                break;
            }
        }

        //return the array
        return new Date[] {startDate,endDate};

    }

    /**
     *Returns the starting date/time and the ending date/time for the given set of stocks. Those dates does
     *not necessarily need to contain values for every stock
     *
     * @param stocks
     * @return First element contains start date, second element contains the ending date
     */
    public Date[] getStartingAndEndDates(String[] stocks, CompanyStockTransactionsData.DataType type) throws DataAccessException {

        //to store all the date
        List<Date> datesList=new ArrayList<Date>();

        //iterate all the stocks
        for(String stock:stocks){
            //get all the dates for that stock
            Date[] dates=transactionDataAPI.getDataDaysRange(type,stock);

            //add them to the map
            datesList.addAll(Arrays.asList(dates));
        }

        //sort in the ascending order
        Collections.sort(datesList);

        return  new Date[] {datesList.get(0),datesList.get(datesList.size()-1)};


    }

    /**
     * @return Company StockId and Name pairs
     * @throws org.investovator.core.data.exeptions.DataAccessException
     */
    public HashMap<String, String> getStocksList() throws DataAccessException {

        return companyDataAPI.getCompanyIDsNames();


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

//    /**
//     * Used to set a custom user data implementation for testing purposes
//     * @param userData
//     */
//    public void setUserData(UserData userData){
//         this.userData=userData;
//    }
}

