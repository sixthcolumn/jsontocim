package com.sixthc.ecg;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsResponseMessageType;

public class DecodeEDCResponse implements Processor {

	final static Logger logger = Logger.getLogger(DecodeEDCResponse.class);

	public void process(Exchange exchange) throws Exception {

		try {
			String xml = exchange.getIn().getBody(String.class);
			EndDeviceControlsResponseMessageType edc = convertXMLtoPOJO(xml);
			String json = convertEDCPOJOtoJSON(edc);
			exchange.getIn().setBody(json);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	EndDeviceControlsResponseMessageType convertXMLtoPOJO(String xml) {
		try {
			SOAPMessage message = MessageFactory.newInstance().createMessage(null,
					new ByteArrayInputStream(xml.getBytes()));
			logger.info(message.toString());

			Unmarshaller unmarshaller = JAXBContext.newInstance(EndDeviceControlsResponseMessageType.class)
					.createUnmarshaller();
			EndDeviceControlsResponseMessageType edc = (EndDeviceControlsResponseMessageType) unmarshaller
					.unmarshal(message.getSOAPBody().extractContentAsDocument());
			return edc;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	String convertEDCPOJOtoJSON(EndDeviceControlsResponseMessageType edc) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.disableHtmlEscaping();

		Gson gson = gsonBuilder.create();
		return gson.toJson(edc, EndDeviceControlsResponseMessageType.class);
	}

}
