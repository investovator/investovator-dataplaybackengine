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


package org.investovator.dataplaybackengine.events;

import org.investovator.core.commons.events.GameEvent;
import org.investovator.dataplaybackengine.util.GameObserver;
import org.junit.Test;

/**
 * @author: ishan
 * @version: ${Revision}
 */
public class EventManagerTest {
    @Test
    public void testEventManager() throws Exception {

        GameObserver gameObserver=new GameObserver();
        EventManager manager=new EventManager();

        //add as a listener
        manager.addObserver(gameObserver);

        //send an event using event manager
        manager.notifyListeners(new PlaybackFinishedEvent());
        //wait one second
        Thread.sleep(1000);
        //check for the reply
        assert(gameObserver.getEvents().size()>0);
        GameEvent event=gameObserver.getEvents().get(0);
        assert(event instanceof PlaybackFinishedEvent);

    }
}
