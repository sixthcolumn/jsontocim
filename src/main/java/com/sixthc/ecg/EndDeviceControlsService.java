package com.sixthc.ecg;


import com.sixthc.ecg.jsonpojo.CreateEndDeviceControls;

import ch.iec.tc57._2011.schema.message.ReplyType;
import ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType;



public interface EndDeviceControlsService {

	EndDeviceControlsPayloadType get(int mrid) throws DeviceNotFoundException;

    void update(CreateEndDeviceControls group) throws DeviceInvalidException;

    ReplyType create(CreateEndDeviceControls group) throws DeviceInvalidException,InvalidJSON;

    void cancel(int mrid);
    
    ReplyType getDERGroup(int mrid) throws DeviceNotFoundException;


}
