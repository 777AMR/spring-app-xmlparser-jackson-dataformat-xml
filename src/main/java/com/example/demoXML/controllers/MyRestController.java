package com.example.demoXML.controllers;

import com.example.demoXML.entity.SpaceShip;
import com.example.demoXML.entity.SpaceStation;
import com.example.demoXML.entity.User;
import com.example.demoXML.java2xsd.Person;
import com.example.demoXML.service.DOMParserService;
import com.example.demoXML.service.SaxParserService;
import com.example.demoXML.service.StaxParserService;
import com.example.demoXML.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {
    private final UserService userService;
    //типа сервисы
    private final DOMParserService domParserService;
    private final SaxParserService saxParserService;
    private final StaxParserService staxParserService;

    Logger logger = LoggerFactory.getLogger(MyRestController.class);

    public MyRestController(UserService userService, DOMParserService domParserService, SaxParserService saxParserService, StaxParserService staxParserService) {
        this.userService = userService;
        this.domParserService = domParserService;
        this.saxParserService = saxParserService;
        this.staxParserService = staxParserService;
    }


    @GetMapping("/spacestation")
//    @GetMapping(value = "/spacestation", produces = {"application/json"})
    public SpaceStation spaceStation() throws XMLStreamException, IOException {
        InputStream xmlResource = MyRestController.class.getClassLoader().getResourceAsStream("spacestation.xml");
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(xmlResource);

        XmlMapper xmlMapper = new XmlMapper();
        SpaceStation spaceStation = xmlMapper.readValue(xmlStreamReader, SpaceStation.class);

        for (SpaceShip spaceship : spaceStation.getSpaceships()) {
            spaceship.setName(spaceship.getName() + " Now For sale!!!");
            spaceship.getPrice().setValueOfShip(20.0d);
        }

        SpaceShip spaceShip = spaceStation.getSpaceships().get(0);
        String xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(spaceShip);
        logger.info("Here the xml: \n" + xmlString);

        return spaceStation;
    }

    @GetMapping("/user")
    public User getUser() throws JsonProcessingException {

        String str = "<data>" +
                "<name>Meiram</name>" +
                "<surname>Meiram1</surname>" +
                "</data>";

        XmlMapper xmlMapper = new XmlMapper();
        User value = xmlMapper.readValue(str, User.class);

        User user = new User();
        user.setName(value.getName());
        user.setSurname(value.getSurname());
        return user;
    }

    @GetMapping("/userxml")
    public String getUserToXml() throws JsonProcessingException {

        User user = new User();
        user.setName("Nick");
        user.setSurname("Ivanov");

        XmlMapper xmlMapper = new XmlMapper();

//        String xml = xmlMapper.writeValueAsString(user);
//        logger.info("xml: " + xml);

        String xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        logger.info("Here the xml: \n" + xmlString);

        return xmlString;
    }

    @GetMapping(value = "/all", produces = {"application/xml"})
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<User>> getAllUser() {
//        List<User> users = userService.findAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addEmployee(@RequestBody User user) {
        User newEmployee = userService.addUser(user);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateEmployee(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/person")
    public Person getPerson() throws IOException {
        String personString = "<Person>\n" +
                "    <firstName>Rohan</firstName>\n" +
                "    <lastName>Daye</lastName>\n" +
                "    <phoneNumbers>\n" +
                "        <phoneNumbers>9911034731</phoneNumbers>\n" +
                "        <phoneNumbers>9911033478</phoneNumbers>\n" +
                "    </phoneNumbers>\n" +
                "    <address>\n" +
                "        <streetName>Name1</streetName>\n" +
                "        <city>City1</city>\n" +
                "    </address>\n" +
                "    <address>\n" +
                "        <streetName>Name2</streetName>\n" +
                "        <city>City2</city>\n" +
                "    </address>\n" +
                "</Person>";

        XmlMapper xmlMapper = new XmlMapper();

        Person value = xmlMapper.readValue(personString, Person.class);

        Person person = new Person(); // test data
        person.setFirstName(value.getFirstName());
        person.setLastName(value.getLastName());
        person.setPhoneNumbers(value.getPhoneNumbers());
        person.setAddress(value.getAddress());

        return person;
    }

    @GetMapping("/peopledomxml")
    public String getPeopleDomXml() throws ParserConfigurationException {
        //Идет загрузка файла сразу в память
        return domParserService.getDomParser();
    }

    @GetMapping("/peoplesaxxml")
    public String getPeopleSaxXml() {
        //Последовательно идет по всему XML и ловим теги в методах через switch
        return saxParserService.getSaxParser();
    }

    @GetMapping("/peoplestaxxml")
    public String getPeopleStaxXml() {
        //работает в 2х форматах: курсора и итератора
        return staxParserService.getStaxParser();
    }
}
