package com.sixthc.ecg.impl;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import com.sixthc.ecg.DeviceInvalidException;
import com.sixthc.ecg.DeviceNotFoundException;
import com.sixthc.ecg.EndDeviceControlsService;
import com.sixthc.ecg.InvalidJSON;
import com.sixthc.ecg.jsonpojo.CreateEndDeviceControls;

import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceControl;
import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceControl.EndDeviceControlType;
import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceControls;
import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceGroup;
import ch.iec.tc57._2011.enddevicecontrols_.Name;
import ch.iec.tc57._2011.schema.message.HeaderType;
import ch.iec.tc57._2011.schema.message.ReplyType;
import ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType;
import ch.iec.tc57._2017.executeenddevicecontrols.EndDeviceControlsPort;
import ch.iec.tc57._2017.executeenddevicecontrols.ExecuteEndDeviceControls;
import ch.iec.tc57._2017.executeenddevicecontrols.FaultMessage;

public class EndDeviceControlsServiceImpl implements EndDeviceControlsService {

	final static Logger logger = Logger.getLogger(EndDeviceControlsServiceImpl.class);

	@Override
	public EndDeviceControlsPayloadType get(int mrid)
			throws DeviceNotFoundException {

		// throw NOT FOUND for id = 86
		if (mrid == 86) {
			throw new DeviceNotFoundException("device not found");
		}

		EndDeviceControlsPayloadType payload = new EndDeviceControlsPayloadType();

		EndDeviceControls edcs = new EndDeviceControls();
		payload.setEndDeviceControls(edcs);
		EndDeviceControl edc = new EndDeviceControl();
		edcs.getEndDeviceControl().add(edc);
		edc.setMRID(String.valueOf(mrid));
		EndDeviceControlType edct = new EndDeviceControlType();
		edct.setRef("2.31.0.18");
		edc.setEndDeviceControlType(edct);
		EndDeviceGroup edg = new EndDeviceGroup();
		edc.getEndDeviceGroups().add(edg);
		Name name = new Name();
		name.setName("DG2");
		edg.getNames().add(name);

		return payload;
	}

	@Override
	public void update(CreateEndDeviceControls group) throws DeviceInvalidException {
		// TODO Auto-generated method stub

	}

	public XMLGregorianCalendar stringToDate(String dt) throws ParseException, DatatypeConfigurationException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = format.parse(dt);

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		return xmlGregCal;

	}

	@Override
	public ReplyType create(CreateEndDeviceControls group) throws DeviceInvalidException, InvalidJSON {
		logger.debug("create : "
				+ String.valueOf(group.getPayload().getEndDeviceControls().getEndDeviceControl().getMRID()));

		logger.debug("forceError = " + group.getForceError());
		if (group.getForceError() != null & "true".equals(group.getForceError())) {
			throw new InvalidJSON("forced error");
		}

		final QName SERVICE_NAME = new QName("http://iec.ch/TC57/2017/ExecuteEndDeviceControls",
				"ExecuteEndDeviceControls");
		URL wsdlURL = ExecuteEndDeviceControls.WSDL_LOCATION;

		ExecuteEndDeviceControls ss = new ExecuteEndDeviceControls(wsdlURL, SERVICE_NAME);
		EndDeviceControlsPort port = ss.getEndDeviceControlsPort();

		// Set the SOAP server address dynamically based on SOAPServerURL
		if (group.getSOAPServerURL() != null) {
			logger.debug("setting SOAP endpoint to " + group.getSOAPServerURL());
			BindingProvider provider = (BindingProvider) port;
			provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, group.getSOAPServerURL());
		}

		{
			logger.debug("Invoking createEndDeviceControls...");
			ch.iec.tc57._2011.schema.message.HeaderType _createEndDeviceControls_headerVal = null;
			javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.HeaderType> header = new javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.HeaderType>(
					_createEndDeviceControls_headerVal);
			ch.iec.tc57._2011.schema.message.RequestType _createEndDeviceControls_request = null;
			ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType payload = new EndDeviceControlsPayloadType();
			javax.xml.ws.Holder<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType> _createEndDeviceControls_payload = new javax.xml.ws.Holder<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType>(
					payload);
			javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.ReplyType> _createEndDeviceControls_reply = new javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.ReplyType>();

			// Populate header data from POJO to SOAP message
			if (group.getHeader() != null) {
				header.value = new HeaderType();
				logger.debug("populating soap header");
				header.value.setVerb(group.getHeader().getVerb());
				header.value.setNoun(group.getHeader().getNoun());
				try {
					header.value.setTimestamp(stringToDate(group.getHeader().getTimestamp()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatatypeConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				header.value.setMessageID(group.getHeader().getMessageID());
				header.value.setCorrelationID(group.getHeader().getCorrelationID());
			}

			// Populate payload from POJO to SOAP message
			if (group.getPayload() != null) {

				EndDeviceControls edcs = new EndDeviceControls();
				payload.setEndDeviceControls(edcs);
				EndDeviceControl edc = new EndDeviceControl();
				edcs.getEndDeviceControl().add(edc);
				edc.setMRID(group.getPayload().getEndDeviceControls().getEndDeviceControl().getMRID());
				EndDeviceControlType edct = new EndDeviceControlType();
				edct.setRef(group.getPayload().getEndDeviceControls().getEndDeviceControl().getEndDeviceControlType()
						.getRef());
				edc.setEndDeviceControlType(edct);
				EndDeviceGroup edg = new EndDeviceGroup();
				edc.getEndDeviceGroups().add(edg);
				Name name = new Name();
				name.setName(group.getPayload().getEndDeviceControls().getEndDeviceControl().getEndDeviceGroups()
						.getNames().getName());
				edg.getNames().add(name);
			}

			try {
				port.createEndDeviceControls(header, _createEndDeviceControls_request, _createEndDeviceControls_payload,
						_createEndDeviceControls_reply);

				logger.debug("createEndDeviceControls._createEndDeviceControls_header=" + header.value);
				logger.debug("createEndDeviceControls._createEndDeviceControls_payload="
						+ _createEndDeviceControls_payload.value);
				logger.debug("createEndDeviceControls._createEndDeviceControls_reply="
						+ _createEndDeviceControls_reply.value);
				return _createEndDeviceControls_reply.value;
			} catch (FaultMessage e) {
				logger.debug("Expected exception: FaultMessage has occurred.");
				logger.debug(e.toString());
			}
		}

		throw new DeviceInvalidException("error occurred");

	}

	@Override
	public void cancel(int mrid) {
		// TODO Auto-generated method stub

	}

	@Override
	public ReplyType getDERGroup(int mrid) throws DeviceNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
