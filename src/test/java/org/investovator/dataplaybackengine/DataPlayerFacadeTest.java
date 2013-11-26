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

import org.apache.commons.configuration.ConfigurationException;
import org.investovator.core.commons.configuration.ConfigLoader;
import org.investovator.core.data.api.utils.TradingDataAttribute;
import org.investovator.dataplaybackengine.configuration.GameConfiguration;
import org.investovator.dataplaybackengine.configuration.GameConfigurationImpl;
import org.investovator.dataplaybackengine.configuration.GameTypesImpl;
import org.investovator.dataplaybackengine.player.DataPlayer;
import org.investovator.dataplaybackengine.player.type.PlayerTypes;
import org.investovator.dataplaybackengine.utils.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class DataPlayerFacadeTest {
    @Test
    public void testCreatePlayer() throws Exception {

        DataPlayerFacade facade=new DataPlayerFacade();
        setSystemProperties();

        String[] stocks=new String[1];
        stocks[0]="GOOG";
        //stocks[1]="APPL";
        String startDate="2011-12-13-15-55-32";
        Date startDateObject= DateUtils.dateStringToDateObject(startDate, DateUtils.DATE_FORMAT_1);

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.CLOSING_PRICE);

        GameConfiguration config=new GameConfigurationImpl(startDateObject,stocks,true,
                GameTypesImpl.DAILY_SUMMARY_CLOSING_PRICE_GAME);

        //create a multiplayer game
        DataPlayer player=facade.createPlayer(config);
        assert(player!=null);

    }

    @Test
    public void testGetCurrentPlayerType() throws Exception {
        DataPlayerFacade facade=new DataPlayerFacade();
        setSystemProperties();

        String[] stocks=new String[1];
        stocks[0]="GOOG";
        //stocks[1]="APPL";
        String startDate="2011-12-13-15-55-32";
        Date startDateObject= DateUtils.dateStringToDateObject(startDate, DateUtils.DATE_FORMAT_1);

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.CLOSING_PRICE);

        GameConfiguration config=new GameConfigurationImpl(startDateObject,stocks,true,
                GameTypesImpl.DAILY_SUMMARY_CLOSING_PRICE_GAME);

        //create a multiplayer game
        DataPlayer player=facade.createPlayer(config);
        assert(facade.getCurrentPlayerType()== PlayerTypes.DAILY_SUMMARY_PLAYER);

    }

    @Test
    public void testGetCurrentPlayer() throws Exception {
        DataPlayerFacade facade=new DataPlayerFacade();
        setSystemProperties();

        String[] stocks=new String[1];
        stocks[0]="GOOG";
        //stocks[1]="APPL";
        String startDate="2011-12-13-15-55-32";
        Date startDateObject= DateUtils.dateStringToDateObject(startDate, DateUtils.DATE_FORMAT_1);

        //define the attributes needed
        ArrayList<TradingDataAttribute> attributes=new ArrayList<TradingDataAttribute>();

        //just the closing price is enough for now
        attributes.add(TradingDataAttribute.DAY);
        attributes.add(TradingDataAttribute.CLOSING_PRICE);

        GameConfiguration config=new GameConfigurationImpl(startDateObject,stocks,true,
                GameTypesImpl.DAILY_SUMMARY_CLOSING_PRICE_GAME);

        //create a multiplayer game
        DataPlayer player=facade.createPlayer(config);
        assert(facade.getCurrentPlayer()!=null);

    }

    public void setSystemProperties(){
        try {
            String realPath = "src/test/resources/";

            ConfigLoader.loadProperties(realPath+"/database.properties");
            //SQL Config
            System.setProperty("org.investovator.core.data.mysql.ddlscriptpath", realPath+"investovator.sql" );
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }


}
