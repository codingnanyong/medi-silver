package com.hci.mcalendar.EventHandler;

import com.hci.mcalendar.datas.DataObject;

import java.util.ArrayList;

/**
 * Created by pdi on 2017-07-24.
 */

public abstract class MCalendarEventHandler {
    public abstract void handler(long millis, ArrayList<DataObject> contents);
}
