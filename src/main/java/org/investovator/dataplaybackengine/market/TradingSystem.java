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


package org.investovator.dataplaybackengine.market;

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.events.PlaybackEvent;
import org.investovator.dataplaybackengine.events.PlaybackEventListener;
import org.investovator.dataplaybackengine.events.StockUpdateEvent;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class TradingSystem implements PlaybackEventListener {

    //stock name-attribute - value
    HashMap<String,HashMap<TradingDataAttribute,Float>> priceList;
    ArrayList<TradingDataAttribute> attributes;
    //attribute used for matching stocks
    TradingDataAttribute attributeToMatch;

    //keeps track of the number of trades
    private int totalTrades;

    //total market turnover
    private float marketTurnover;

    public TradingSystem(ArrayList<TradingDataAttribute> attributes, TradingDataAttribute attributeToMatch) {
        this.priceList = new HashMap<String, HashMap<TradingDataAttribute, Float>>();
        this.attributes = attributes;
        this.attributeToMatch = attributeToMatch;
        this.marketTurnover=0;
        this.totalTrades=0;
    }

    /**
     * Executes an order on the given info
     *
     * @param stockId Name of the stock
     * @param quantity Quantity to be sold/bought
     * @param accountBalance Available cash in the users account
     * @return   the cost of a single stock
     * @throws InvalidOrderException
     */
    public float executeOrder(String stockId, int quantity, double accountBalance, OrderType type)
            throws InvalidOrderException {
        if(!priceList.containsKey(stockId)){
            throw new InvalidOrderException("No events have arrived for the stock "+stockId);

        }

        float price=priceList.get(stockId).get(attributeToMatch);
        float neededMoney=price*quantity;
        //if this is a buy order and if the user does not have enough money
        if(type==OrderType.BUY && accountBalance<neededMoney){
            throw new InvalidOrderException("Not enough money. Need "+neededMoney);
        }

        //add to turnover
        this.marketTurnover+=neededMoney;
        //increase the total number of trades
        this.totalTrades=+1;

        return price;
    }

    /**
     * Returns the current price of the stock
     * @param stockId  name of the stock
     * @return
     */
    public float getStockPrice(String stockId){
        return priceList.get(stockId).get(attributeToMatch);
    }

    /**
     * Returns the total market turnover
     * @return
     */
    public float getMarketTurnover() {
        return marketTurnover;
    }

    public void updateStockPrice(String stockId, HashMap<TradingDataAttribute, String> prices){
        HashMap<TradingDataAttribute, Float> details=new HashMap<TradingDataAttribute, Float>();
        //convert the details
        for(TradingDataAttribute attr:prices.keySet()){
            details.put(attr,Float.parseFloat(prices.get(attributeToMatch)));

        }
        priceList.put(stockId,details);

        }

    //used to listen to events by the RealTimeEventTask
    @Override
    public void eventOccurred(PlaybackEvent arg) {
        //if this is a stock event
        if(arg instanceof StockUpdateEvent){
            StockUpdateEvent event=(StockUpdateEvent)arg;

            HashMap<TradingDataAttribute,Float> values=new HashMap<TradingDataAttribute, Float>();
            //iterate all the attributes
            for(TradingDataAttribute attr:event.getData().keySet()){
                values.put(attr,event.getData().get(attr));
            }

            //update the price of the stock
            priceList.put(event.getStockId(),values);


        }
    }

    /**
     *
     * Returns the total number of trades done
     * @return
     */
    public int getTotalTrades() {
        return totalTrades;
    }
}
