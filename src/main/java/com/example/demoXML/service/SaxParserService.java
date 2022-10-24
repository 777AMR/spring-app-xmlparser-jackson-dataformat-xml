package com.example.demoXML.service;

import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

@Service
public class SaxParserService {
    public String getSaxParser() {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

            saxParser.parse("C:\\Projects for Java\\demo111\\demo11\\people.xml", new SaxHandler());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    private class SaxHandler extends DefaultHandler {
        String content = null;

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (qName) {

                case "person": {
                    System.out.println("id=" + attributes.getValue("id"));
                    System.out.println("type=" + attributes.getValue("type"));
                    break;
                }
            }
        }

        public void getElement(String uri, String localName, String qName) {
            switch (qName) {
                case "city": {
                    System.out.println(qName + ": " + content);
                    break;
                }
                case "name": {
                    System.out.println(qName + ": " + content);
                    break;
                }
            }
        }

        public void characters(char ch[], int start, int length) {
            content = String.copyValueOf(ch, start, length).trim();
        }
    }
}
