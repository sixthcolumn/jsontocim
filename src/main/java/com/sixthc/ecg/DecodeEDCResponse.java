package com.sixthc.ecg;

import java.io.StringWriter;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;


import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import java.net.URL;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.w3c.dom.Node;
import java.io.StringReader;
import java.io.StringWriter;


import ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsResponseMessageType;
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

public class DecodeEDCResponse implements Processor {

	final static Logger logger = Logger.getLogger(DecodeEDCResponse.class);

public void process(Exchange exchange) throws Exception {

	logger.info("process 10");
	String test = exchange.getIn().getBody(String.class);
	logger.info("process 20: " + test);

	try {
	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document dDoc = builder.parse(new InputSource(new StringReader(test)));
        XPath xPath = XPathFactory.newInstance().newXPath();

        Node node = (Node) xPath.evaluate("//*[local-name()='EndDeviceControlsResponseMessage']", dDoc, XPathConstants.NODE);

	logger.info("node  " + node);

	logger.info("node val: " + node.getNodeValue());

	StringWriter writer = new StringWriter();

	InputStream xsltr = DecodeEDCResponse.class.getResourceAsStream("/xslt/dropnamespace.xsl");

	TransformerFactory factory = TransformerFactory.newInstance();	
        Transformer transformer = factory.newTransformer(new StreamSource(xsltr));

	transformer.setOutputProperty("omit-xml-declaration", "yes");
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        String xml = writer.toString();
	logger.info("reduced xml  " + xml);

	ConvertReply(xml);

        exchange.getIn().setBody(xml);

	} catch (Exception e) {
		e.printStackTrace();
	}

	
	 
}

void ConvertReply(String replyMess) {

	try {
    		JAXBContext context = JAXBContext.newInstance(ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsResponseMessageType.class);
    		Unmarshaller m = context.createUnmarshaller();
    		//m.setProperty(Marshaller.JAXB_FRAGMENT, true);
    		//m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    		StringWriter sw = new StringWriter();
    		sw.write(replyMess);

    		InputStream is = new ByteArrayInputStream(sw.toString().getBytes());
    		
    		JAXBElement<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsResponseMessageType> root = 
   			(JAXBElement<ch.iec.tc57._2017.enddevicecontrolsmessage.EndDeviceControlsResponseMessageType>) m.unmarshal(new StreamSource(is), EndDeviceControlsResponseMessageType.class);

		logger.info("root: " + root);
		EndDeviceControlsResponseMessageType edc_type = root.getValue();
		logger.info("edc_type: " + edc_type);
		logger.info("edc_types: " + edc_type.toString());

		ReplyType reply = edc_type.getReply();

		logger.info("replytype: " + reply);
		logger.info("header: " + edc_type.getHeader());

	} catch (JAXBException e) {
		e.printStackTrace();
        }

}


}
