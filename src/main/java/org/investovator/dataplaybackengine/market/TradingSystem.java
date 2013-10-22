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
import org.investovator.dataplaybackengine.events.StockEvent;
import org.investovator.dataplaybackengine.exceptions.InvalidOrderException;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class TradingSystem implements Observer {

    //stock name-attribute - value
    HashMap<String,HashMap<TradingDataAttribute,Float>> priceList;
    TradingDataAttribute[] attributes;
    //attribute used for matching stocks
    TradingDataAttribute attributeToMatch;

    public TradingSystem(TradingDataAttribute[] attributes, TradingDataAttribute attributeToMatch) {
        this.priceList = new HashMap<String, HashMap<TradingDataAttribute, Float>>();
        this.attributes = attributes;
        this.attributeToMatch = attributeToMatch;
    }

    /**
     * Executes an order on the given info
     *
     * @param stockId Name of the stock
     * @param quantity Quantity to be sold/bought
     * @param side  Sell/Buy
     * @param accountBalance Available cash in the users account
     * @return   a negative expense, a positive income, or a zero for a failed transaction
     * @throws InvalidOrderException
     */
    public float executeOrder(String stockId, int quantity, OrderType side, int accountBalance)
            throws InvalidOrderException {
        if(!priceList.containsKey(stockId)){
            throw new InvalidOrderException("No events have arrived for the stock "+stockId);

        }
        //if the user does not have enough money
        float neededMoney=priceList.get(stockId).get(attributeToMatch)*quantity;
        if(accountBalance<neededMoney){
            throw new InvalidOrderException("Not enough money. Need "+neededMoney);
        }

        //if this is a buy
        if(side==OrderType.BUY){
            return -(neededMoney);
        }
        else if (side==OrderType.SELL){
            return neededMoney;
        }
        else {
            return 0;
        }

    }

    /**
     * Returns the current price of the stock
     * @param stockId  name of the stock
     * @return
     */
    public float getStockPrice(String stockId){
        return priceList.get(stockId).get(attributeToMatch);
    }

    //used to listen to events by the EventTask
    @Override
    public void update(Observable o, Object arg) {
        //if this is a stock event
        if(arg instanceof StockEvent){
            StockEvent event=(StockEvent)arg;

            HashMap<TradingDataAttribute,Float> values=new HashMap<TradingDataAttribute, Float>();
            //iterate all the attributes
            for(TradingDataAttribute attr:event.getData().keySet()){
                values.put(attr,event.getData().get(attr));
            }

            //update the price of the stock
            priceList.put(event.getStockId(),values);


        }
    }
}
