package com.example.demoXML.service;

import com.example.demoXML.controllers.MyRestController;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DOMParserService {

    Document doc;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Logger logger = LoggerFactory.getLogger(MyRestController.class);

    public String getDomParser() throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try {
            doc = builder.parse(new File("C:\\Projects for Java\\demo111\\demo11\\people.xml"));
//            System.out.println(doc.getDocumentElement().getChildNodes().getLength());
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            if (doc.hasChildNodes()) {
                print(doc.getChildNodes());
            }

            saveXML("C:\\addDateDOM.xml");

        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "C:\\addDateDOM.xml";
    }

    private void saveXML(String path) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            logger.info(DOMParser.class.getName());
        } catch (TransformerException e) {
            logger.info(DOMParser.class.getName());
        }
    }

    private void print(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);


            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("Node \"" + tempNode.getNodeName() + "\"");
                System.out.println("Node value = " + tempNode.getTextContent());

                if (tempNode.getNodeName().equals("person")) {
                    if (tempNode.hasAttributes()) {
                        NamedNodeMap nodeMap = tempNode.getAttributes();
                        for (int j = 0; j < nodeMap.getLength(); j++) {
                            Node node = nodeMap.item(j);
                            System.out.println("attribute \"" + node.getNodeName() + "\"=" + node.getNodeValue());
                        }
                    }
                    addDate(tempNode);
                }
                if (tempNode.getNodeName().equals("city")) {
                    tempNode.getParentNode().removeChild(tempNode);
                }

                if (tempNode.hasChildNodes()) {
                    print(tempNode.getChildNodes());
                }

                System.out.println();
            }
        }
    }

    private void addDate(Node node) {
        Node dateNode = doc.createElement("date");
        dateNode.appendChild(doc.createTextNode(dateFormat.format(new Date())));
        node.appendChild(dateNode);
    }

}
