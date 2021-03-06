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


package org.investovator.dataplaybackengine.datagenerators;

import org.investovator.core.commons.utils.Portfolio;
import org.investovator.core.data.api.UserData;
import org.investovator.core.data.exeptions.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class UserDataCustomImpl implements UserData {
    HashMap<String,Portfolio> users;

    public UserDataCustomImpl() {
        this.users = new HashMap<>() ;
    }

    @Override
    public Portfolio getUserPortfolio(String instance,String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void updateUserPortfolio(String instance,String username, Portfolio portfolio)
            throws DataAccessException {
        users.put(username,portfolio);
    }

    @Override
    public ArrayList<String> getWatchList(String instance,String username)
            throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addToWatchList(String instance,String username, String symbol)
            throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deleteFromWatchList(String instance,String username, String symbol)
            throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateWatchList(String instance,String username, ArrayList<String> watchList)
            throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addUserToGameInstance(String s, String s2) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ArrayList<String> getUserJoinedGameInstances(String username) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ArrayList<String> getGameInstanceUsers(String gameInstanceName) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearUserDataOnGameInstance(String gameInstanceName) throws DataAccessException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
