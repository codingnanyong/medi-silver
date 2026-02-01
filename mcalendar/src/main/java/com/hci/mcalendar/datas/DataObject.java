package com.hci.mcalendar.datas;

import com.hci.mcalendar.interfaces.IData;
import com.hci.mcalendar.interfaces.IDataView;

/**
 * Created by pdi on 2017-07-23.
 */

public abstract class DataObject implements IData, IDataView{
    private Object mContent;

    public DataObject() {

    }
    public DataObject(Object content) {
        mContent = content;
    }


}
