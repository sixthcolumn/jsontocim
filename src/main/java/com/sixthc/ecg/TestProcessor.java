package com.sixthc.ecg;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.sixthc.ecg.jsonpojo.CreateEndDeviceControls;

import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceControl;
import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceControl.EndDeviceControlType;
import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceControls;
import ch.iec.tc57._2011.enddevicecontrols_.EndDeviceGroup;
import ch.iec.tc57._2011.enddevicecontrols_.Name;
import ch.iec.tc57._2011.schema.message.HeaderType;
import ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType;
import ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsRequestMessageType;

public class TestProcessor implements Processor {

	final static Logger logger = Logger.getLogger(TestProcessor.class);

public XMLGregorianCalendar stringToDate(String dt) throws ParseException, DatatypeConfigurationException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = format.parse(dt);

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		return xmlGregCal;

}
	

public void process(Exchange exchange) throws Exception {

	CreateEndDeviceControls edc;
	logger.info("process 1");
	edc = exchange.getIn().getBody(CreateEndDeviceControls.class);
	logger.info("process edc: " + edc);
	logger.info("process edc verb: " + edc.getHeader().getVerb());

	String test = exchange.getIn().getBody(String.class);
	logger.info("process 2: " + test);

	logger.debug("forceError = " + edc.getForceError());
	if (edc.getForceError() != null & "true".equals(edc.getForceError())) {
		throw new InvalidJSON("forced error");
	}


	final QName EDC_REQUEST = new QName("http://iec.ch/TC57/2017/EndDeviceControlsMessage",
				"CreateEndDeviceControls");

	logger.debug("building createEndDeviceControls soap message...");
	ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsRequestMessageType edc_mtype = new EndDeviceControlsRequestMessageType();
	ch.iec.tc57._2011.schema.message.HeaderType _createEndDeviceControls_headerVal = null;
	javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.HeaderType> header = new javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.HeaderType>( _createEndDeviceControls_headerVal);

	ch.iec.tc57._2011.schema.message.RequestType _createEndDeviceControls_request = null;
	ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType payload = new EndDeviceControlsPayloadType();
	edc_mtype.setRequest(_createEndDeviceControls_request);
	edc_mtype.setPayload(payload);

	javax.xml.ws.Holder<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType> _createEndDeviceControls_payload = new javax.xml.ws.Holder<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsPayloadType>( payload);
	javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.ReplyType> _createEndDeviceControls_reply = new javax.xml.ws.Holder<ch.iec.tc57._2011.schema.message.ReplyType>();

	// Populate header data from POJO to SOAP message
	if (edc.getHeader() != null) {
		header.value = new HeaderType();
		edc_mtype.setHeader(header.value);
		logger.debug("populating soap header");
		header.value.setVerb(edc.getHeader().getVerb());
		header.value.setNoun(edc.getHeader().getNoun());
		logger.debug("populating date: " + edc.getHeader().getTimestamp());

		try {
			header.value.setTimestamp(stringToDate(edc.getHeader().getTimestamp()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		header.value.setMessageID(edc.getHeader().getMessageID());
		header.value.setCorrelationID(edc.getHeader().getCorrelationID());
	}

	// Populate payload from POJO to SOAP message
	if (edc.getPayload() != null) {

		EndDeviceControls edcs = new EndDeviceControls();
		payload.setEndDeviceControls(edcs);

		EndDeviceControl nedc = new EndDeviceControl();
		edcs.getEndDeviceControl().add(nedc);
		nedc.setMRID(edc.getPayload().getEndDeviceControls().getEndDeviceControl().getMRID());

		EndDeviceControlType edct = new EndDeviceControlType();
		edct.setRef(edc.getPayload().getEndDeviceControls().getEndDeviceControl().getEndDeviceControlType().getRef());
		nedc.setEndDeviceControlType(edct);

		EndDeviceGroup edg = new EndDeviceGroup();
		nedc.getEndDeviceGroups().add(edg);

		Name name = new Name();
		name.setName(edc.getPayload().getEndDeviceControls().getEndDeviceControl().getEndDeviceGroups().getNames().getName());
		edg.getNames().add(name);
	}

    JAXBContext context = JAXBContext.newInstance(ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsRequestMessageType.class);
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FRAGMENT, true);
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    JAXBElement<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsRequestMessageType> root = new JAXBElement<>(EDC_REQUEST, EndDeviceControlsRequestMessageType.class, edc_mtype);
    

    StringWriter sw = new StringWriter();
    m.marshal(root, sw);

    String result = sw.toString();

    logger.info("marshalled string: " + result);

    /* haxxing up the soap envelope */
    String withsoap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body>" + result + "</soapenv:Body></soapenv:Envelope>";

    logger.info("marshalled string with soap: " + withsoap);

    exchange.getIn().setBody(withsoap);
}

}
