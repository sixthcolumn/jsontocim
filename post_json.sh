curl -v -X POST -H "Content-Type: application/json" -d '{ "@schemaLocation": "http://iec.ch/TC57/2017/EndDeviceControlsMessage EndDeviceControlsMessage.xsd", "SoapServerURL": "http://localhost:8088/mockEndDeviceControls", "Header": { "Verb": "create", "Noun": "EndDeviceControls", "Timestamp": "2017-06-20T11:24:04-06:00", "MessageID": "aab72e66-d8b4-406c-8e18-4df2864cf947", "CorrelationID": "3a913c02-bc54-4a63-b82a-b255ffa0b7da" }, "Payload": {   "EndDeviceControls": {  "EndDeviceControl": {  "mRID": "2b928834-b538-4244-9daf-6db54f700a76", "EndDeviceControlType": {           "@ref": "2.31.0.18" }, "EndDeviceGroups": { "Names": { "name": "DG2"  }  }  }  }  } }' http://localhost:8080/cim4/rest/endDeviceControls/create