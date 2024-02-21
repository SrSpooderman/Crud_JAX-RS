package org.example.crud_jaxrs;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@XmlRootElement
public class Reserva {
    private String locationNumber;
    private String client;
    private String agency;
    private String price;
    private String roomType;
    private String hotel;
    private String checkIn;
    private String roomNights;

    public Reserva(Node node){
        updateNode(node);
    }

    public Reserva(){}

    public void updateNode(Node node){
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            this.locationNumber = element.getAttribute("location_number");

            Node nodeClient = element.getElementsByTagName("client").item(0);
            this.client = nodeClient.getTextContent();

            this.agency = element.getElementsByTagName("agency").item(0).getTextContent();

            Node nodePrice = element.getElementsByTagName("price").item(0);
            this.price = nodePrice.getTextContent();

            Node nodeRoom = element.getElementsByTagName("room").item(0);
            this.roomType = nodeRoom.getTextContent();

            Node nodeHotel = element.getElementsByTagName("hotel").item(0);
            this.hotel = nodeHotel.getTextContent();

            Node nodeCheck = element.getElementsByTagName("check_in").item(0);
            this.checkIn = nodeCheck.getTextContent();

            Node roomNode = element.getElementsByTagName("room_nights").item(0);
            this.roomNights = roomNode.getTextContent();
        }
    }

    public Element toXmlElement(Document document) {
        Element reservaElement = document.createElement("booking");

        // Set attributes
        reservaElement.setAttribute("location_number", this.locationNumber);

        // Create child elements
        Element clientElement = document.createElement("client");
        clientElement.appendChild(document.createTextNode(this.client));
        reservaElement.appendChild(clientElement);

        Element agencyElement = document.createElement("agency");
        agencyElement.appendChild(document.createTextNode(this.agency));
        reservaElement.appendChild(agencyElement);

        Element roomElement = document.createElement("room");
        roomElement.appendChild(document.createTextNode(this.roomType));
        reservaElement.appendChild(roomElement);

        Element hotelElement = document.createElement("hotel");
        hotelElement.appendChild(document.createTextNode(this.hotel));
        reservaElement.appendChild(hotelElement);

        Element checkInElement = document.createElement("check_in");
        checkInElement.appendChild(document.createTextNode(this.checkIn));
        reservaElement.appendChild(checkInElement);

        Element nightsElement = document.createElement("room_nights");
        nightsElement.appendChild(document.createTextNode(this.roomNights));
        reservaElement.appendChild(nightsElement);

        return reservaElement;
    }

    public void printBookingData() {
        System.out.println("-------------------------");
        System.out.println("Location Number: " + this.locationNumber);
        System.out.println("Client: " + this.client);
        System.out.println("Agency: " + this.agency);
        System.out.println("Price: " + this.price);
        System.out.println("Room Type: " + this.roomType);
        System.out.println("Hotel: " + this.hotel);
        System.out.println("Check In Date: " + this.checkIn);
        System.out.println("Room Nights: " + this.roomNights);
        System.out.println("-------------------------");
    }

    @Override
    public String toString(){
        String response = "";
        response += "Location Number: " + this.locationNumber + " ";
        response += "Client: " + this.client + " ";
        response += "Agency: " + this.agency + " ";
        response += "Price: " + this.price + " ";
        response += "Room Type: " + this.roomType + " ";
        response += "Hotel: " + this.hotel + " ";
        response += "Check In Date: " + this.checkIn + " ";
        response += "Room Nights: " + this.roomNights + " ";
        return response;
    }
}
