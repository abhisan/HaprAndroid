package com.hapr.customview;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XParser extends DefaultHandler {

	ArrayList<String> idList = new ArrayList<String>();
	ArrayList<String> controlList = new ArrayList<String>();
	ArrayList<String> locationList = new ArrayList<String>();
	ArrayList<String> stateList = new ArrayList<String>();
	ArrayList<String> iconList = new ArrayList<String>();
	private String tempList = null;

	public XParser() {
		// TODO Auto-generated constructor stub
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equalsIgnoreCase("id")) {
			tempList = "";
		} else if (localName.equalsIgnoreCase("control")) {
			tempList = "";
		} else if (localName.equalsIgnoreCase("location")) {
			tempList = "";
		} else if (localName.equalsIgnoreCase("state")) {
			tempList = "";
		} else if (localName.equalsIgnoreCase("icon")) {
			tempList = "";
		} else {
			tempList = "";
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (localName.equalsIgnoreCase("id")) {
			idList.add(tempList);
		} else if (localName.equalsIgnoreCase("control")) {
			controlList.add(tempList);
		} else if (localName.equalsIgnoreCase("location")) {
			locationList.add(tempList);
		} else if (localName.equalsIgnoreCase("state")) {
			stateList.add(tempList);
		} else if (localName.equalsIgnoreCase("icon")) {
			iconList.add(tempList);
		}
		tempList = "";
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		tempList += new String(ch, start, length);
	}

}
