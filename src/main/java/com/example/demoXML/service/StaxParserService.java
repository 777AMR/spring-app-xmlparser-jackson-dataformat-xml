package com.example.demoXML.service;

import com.example.demoXML.controllers.MyRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.xml.stream.XMLStreamConstants.*;

@Service
public class StaxParserService {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Logger logger = LoggerFactory.getLogger(MyRestController.class);

    public String getStaxParser() {

        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = inputFactory.
                    createXMLEventReader(new FileInputStream("C:\\Projects for Java\\demo111\\demo11\\people.xml"));
            XMLOutputFactory factory = XMLOutputFactory.newInstance();

            XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter("C:\\testStax.xml"));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            boolean cityTag = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                System.out.println(event);

                switch (event.getEventType()) {
                    case START_ELEMENT: {

                        if (event.asStartElement().getName().toString().equalsIgnoreCase("city")) {
                            cityTag = true;
                            continue;
                        }

                        break;

                    }

                    case CHARACTERS: {
                        if (cityTag) {
                            continue;
                        }
                        break;
                    }

                    case END_ELEMENT: {
                        if (event.asEndElement().getName().toString().equalsIgnoreCase("city")) {
                            cityTag = true;
                            continue;
                        }

                        if (event.asEndElement().getName().toString().equalsIgnoreCase("person")) {
                            writer.add(eventFactory.createStartElement("", null, "date"));
                            writer.add(eventFactory.createCharacters(dateFormat.format(new Date())));
                            writer.add(eventFactory.createEndElement("", null, "date"));
                        }

                        break;
                    }
                }
                writer.add(event);
            }

            writer.flush();
            writer.close();

        } catch (XMLStreamException e) {
            logger.info("XMLStreamException: " + e);
        } catch (FileNotFoundException e) {
            logger.info("FileNotFoundException: " + e);
        } catch (IOException e) {
            logger.info("IOException: " + e);
        }


        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream("C:\\testStax.xml"));
            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case START_ELEMENT: {
                        if ("person".equals(reader.getLocalName()))

                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                System.out.println(reader.getAttributeName(i) + "=" + reader.getAttributeValue(i));
                            }
                        System.out.println();
                    }
                    break;

                    case CHARACTERS:
                        System.out.println(reader.getText().trim());
                        System.out.println();
                        break;
                }
            }
        } catch (XMLStreamException e) {
            logger.info("XMLStreamException: " + e);
        } catch (FileNotFoundException e) {
            logger.info("FileNotFoundException: " + e);
        }

        return "C:\\testStax.xml";
    }
}
