cim4
-----------------

# setup

## install jboss devstudio and jboss 7.1

download jboss devstudio and install
install server jboss 7.x (select 7.1)
run $HOME/jboss-eap-7.2/*/bin/add-user.sh (add mgmt user <yourname>
start 7.1 server
http://localhost:9990 (management console)


## install soapui

download soapui
install soapui


## get repository, build code

$sh> git clone https://github.com/r0ckflite/cim4.git
$sh> cim4
$sh> mvn package


## deploy war file

http://localhsot:9990
deployments / add
choose file : git/cim4/target/cim4.war

## soapui

start soapui
import project : git/cim4/cim4-soapui.xml
start mock service

## POST JSON

	curl -v -X POST -H "Content-Type: application/json" -d '{ "@schemaLocation": "http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd", "SoapServerURL": "http://localhost:8088/mockEndDeviceControls", "Header": { "Verb": "create", "Noun": "EndDeviceControls", "Timestamp": "2017-06-20T11:24:04-06:00", "MessageID": "aab72e66-d8b4-406c-8e18-4df2864cf947", "CorrelationID": "3a913c02-bc54-4a63-b82a-b255ffa0b7da" }, "Payload": {   "EndDeviceControls": {  "EndDeviceControl": {  "mRID": "2b928834-b538-4244-9daf-6db54f700a76", "EndDeviceControlType": {           "@ref": "2.31.0.18" }, "EndDeviceGroups": { "Names": { "name": "DG2"  }  }  }  }  } }' http://localhost:8080/cim4/rest/endDeviceControls/create


# MORE


Example Messages

<!-- message to send -->
<?xml version="1.0" encoding="UTF-8"?>
<tns:CreateEndDeviceControls xmlns="http://iec.ch/TC57/2011/EndDeviceControls#" xmlns:msg="http://iec.ch/TC57/2011/schema/message" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:n1="http://www.altova.com/samplexml/other-namespace" xmlns:tns="http://iec.ch/TC57/2017/EndDeviceControlsMessage" xsi:schemaLocation="http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd">
	<tns:Header>
		<msg:Verb>create</msg:Verb>
		<msg:Noun>EndDeviceControls</msg:Noun>
		<msg:Timestamp>2017-02-16T20:22:28+00:00</msg:Timestamp>
		<msg:MessageID>aab72e66-d8b4-406c-8e18-4df2864cf947</msg:MessageID>
		<msg:CorrelationID>3a913c02-bc54-4a63-b82a-b255ffa0b7da</msg:CorrelationID>
	</tns:Header>
	<tns:Payload>
		<EndDeviceControls>
			<EndDeviceControl>
				<mRID>2b928834-b538-4244-9daf-6db54f700a76</mRID>
				<EndDeviceControlType ref="2.31.0.18"/>
				<EndDeviceGroups>
					<Names>
						<name>DG2</name>
					</Names>
				</EndDeviceGroups>
			</EndDeviceControl>
		</EndDeviceControls>
	</tns:Payload>
</tns:CreateEndDeviceControls>

<!-- reply message -->
<?xml version="1.0" encoding="UTF-8"?>
<tns:EndDeviceControlsResponseMessage xmlns="http://iec.ch/TC57/2011/EndDeviceControls#" xmlns:xml="http://www.w3.org/XML/1998/namespace" xmlns:msg="http://iec.ch/TC57/2011/schema/message" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:n1="http://www.altova.com/samplexml/other-namespace" xmlns:tns="http://iec.ch/TC57/2017/EndDeviceControlsMessage" xsi:schemaLocation="http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd">
	<tns:Header>
		<msg:Verb>reply</msg:Verb>
		<msg:Noun>EndDeviceControls</msg:Noun>
		<msg:Timestamp>2017-06-20T11:24:12-06:00</msg:Timestamp>
		<msg:MessageID>e7bdb90c-17cb-42fe-a559-19022b72a130</msg:MessageID>
		<msg:CorrelationID>3a913c02-bc54-4a63-b82a-b255ffa0b7da</msg:CorrelationID>
	</tns:Header>
	<tns:Reply>
		<msg:Result>OK</msg:Result>
		<msg:Error>
			<msg:code>0.0</msg:code>
		</msg:Error>
	</tns:Reply>
</tns:EndDeviceControlsResponseMessage>


# testing

{
  "@schemaLocation": "http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd",
  "Header": {
    "Verb": "create",
    "Noun": "EndDeviceControls",
    "Timestamp": "2017-06-20T11:24:04-06:00",
    "MessageID": "aab72e66-d8b4-406c-8e18-4df2864cf947",
    "CorrelationID": "3a913c02-bc54-4a63-b82a-b255ffa0b7da"
  },
  "Payload": {
    "EndDeviceControls": {
      "EndDeviceControl": {
        "mRID": "2b928834-b538-4244-9daf-6db54f700a76",
        "EndDeviceControlType": {
          "@ref": "2.31.0.18"
        },
        "EndDeviceGroups": {
          "Names": {
            "name": "DG2"
          }
        }
      }
    }
  }
}

Note on create process
Currently it :
	1. gets json specified in src/main/resources/json/createEndDeviceControls (I generated this using web resource, could be customized/changed)
	2. service bean EndDeviceControlsServiceImpl.java translates JSON pojo to SOAP message (wsdl2java generated classes)
	3. sends soap message to remote server
	4. (currently) skips transforming the SOAP reply to JSON, and just returns the XML, which is auto-converted to JSON by service

## POST create 
    curl -v -X POST -H "Content-Type: application/json" -d '{ "@schemaLocation": "http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd", "SoapServerURL": "http://localhost:8088/mockEndDeviceControls", "Header": { "Verb": "create", "Noun": "EndDeviceControls", "Timestamp": "2017-06-20T11:24:04-06:00", "MessageID": "aab72e66-d8b4-406c-8e18-4df2864cf947", "CorrelationID": "3a913c02-bc54-4a63-b82a-b255ffa0b7da" }, "Payload": {   "EndDeviceControls": {  "EndDeviceControl": {  "mRID": "2b928834-b538-4244-9daf-6db54f700a76", "EndDeviceControlType": {           "@ref": "2.31.0.18" }, "EndDeviceGroups": { "Names": { "name": "DG2"  }  }  }  }  } }' http://localhost:8080/cim4/rest/endDeviceControls/create
  
## POST and force error, 400  
    curl -v -X POST -H "Content-Type: application/json" -d '{ "@schemaLocation": "http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd", "SoapServerURL": "http://localhost:8088/mockEndDeviceControls", "ForceError": "true", "Header": { "Verb": "create", "Noun": "EndDeviceControls", "Timestamp": "2017-06-20T11:24:04-06:00", "MessageID": "aab72e66-d8b4-406c-8e18-4df2864cf947", "CorrelationID": "3a913c02-bc54-4a63-b82a-b255ffa0b7da" }, "Payload": {   "EndDeviceControls": {  "EndDeviceControl": {  "mRID": "2b928834-b538-4244-9daf-6db54f700a76", "EndDeviceControlType": {           "@ref": "2.31.0.18" }, "EndDeviceGroups": { "Names": { "name": "DG2"  }  }  }  }  } }' http://localhost:8080/cim4/rest/endDeviceControls/create

## GET (example, not properly part of the wsdl)
	curl -v -GET http://localhost:8080/cim4/rest/endDeviceControls/1
	
## GET (example, not properly part of the wsdl)	returns not found
	curl -v -GET http://localhost:8080/cim4/rest/endDeviceControls/86
