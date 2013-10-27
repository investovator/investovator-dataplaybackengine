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

import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.exceptions.player.PlayerStateException;
import org.investovator.dataplaybackengine.player.OHLCDataPLayer;
import org.investovator.dataplaybackengine.player.RealTimeDataPlayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayerFacade {
    private static DataPlayerFacade facade;

    private OHLCDataPLayer ohlcDataPLayer;
    private RealTimeDataPlayer realTimeDataPlayer;
    private PlayerTypes playerType;

    private DataPlayerFacade() {

    }

    public static synchronized DataPlayerFacade getInstance(){
        if(facade==null){
            facade=new DataPlayerFacade();
        }

        return facade;
    }

    public void createPlayer(PlayerTypes playerType,String[] stocks,Date startDate,
                                 ArrayList<TradingDataAttribute> attributes,
                                 TradingDataAttribute attributeToMatch, boolean isMultiplayer) {
        //if a daily summary player is needed
        if(playerType==PlayerTypes.DAILY_SUMMARY_PLAYER){
            ohlcDataPLayer=new OHLCDataPLayer(stocks, attributes, attributeToMatch,isMultiplayer );
            this.ohlcDataPLayer.setStartDate(startDate);
        }
        //if a real time data player is needed
        else if(playerType==PlayerTypes.REAL_TIME_DATA_PLAYER){
            realTimeDataPlayer=new RealTimeDataPlayer(stocks,startDate,attributes,attributeToMatch);
        }
    }

    public OHLCDataPLayer getDailySummaryDataPLayer() throws PlayerStateException {
        if(ohlcDataPLayer!=null){
            return ohlcDataPLayer;
        }
        else{
            throw new PlayerStateException("Daily Summary player is not initialized yet.");
        }
    }

    public RealTimeDataPlayer getRealTimeDataPlayer() throws PlayerStateException {
        if (realTimeDataPlayer!=null){
            return realTimeDataPlayer;
        }
        else{
            throw new PlayerStateException("Real time data player is not initialized yet.");
        }
    }


}
