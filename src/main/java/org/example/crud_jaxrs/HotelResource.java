package org.example.crud_jaxrs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter

@Path("/reservas")
public class HotelResource {
    File file = new File("C:\\Users\\pfran\\Desktop\\deberes\\Crud_JAX-RS\\src\\main\\resources\\bookings.xml");

    @GET
    @Produces("text/html")
    public String getBookings() {
       ArrayList<Reserva> reservas = new ArrayList<Reserva>();
       try{
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           Document document = dBuilder.parse(file);
           document.getDocumentElement().normalize();

           NodeList nodeList = document.getElementsByTagName("booking");

           for (int i = 0; i <  nodeList.getLength(); i++){
               Node node = nodeList.item(i);
               Reserva reserva = new Reserva(node);
               reservas.add(reserva);
           }
       } catch (ParserConfigurationException | SAXException | IOException e) {
           throw new RuntimeException(e);
       }

       String result = "";
       for (Reserva reserva:reservas){
           result += reserva.toString()+"\n";
       }

       return result;
    }

    @DELETE
    @Path("/{locationNumber}")
    public Response deleteBooking(@PathParam("locationNumber") String locationNumber) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("booking");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String bookingLocationNumber = element.getAttribute("location_number");
                    if (bookingLocationNumber.equals(locationNumber)) {
                        node.getParentNode().removeChild(node);

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource domSource = new DOMSource(document);
                        StreamResult streamResult = new StreamResult(file);
                        transformer.transform(domSource, streamResult);

                        return Response.status(Response.Status.OK).entity("Reserva eliminada correctamente").build();
                    }
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("La reserva no se encontró").build();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @POST
    @Consumes("application/xml")
    public Response createBooking(Reserva reserva) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            Element rootElement = document.getDocumentElement();
            Element newBooking = reserva.toXmlElement(document);
            rootElement.appendChild(newBooking);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);

            return Response.status(Response.Status.CREATED).entity("Reserva creada correctamente").build();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @PUT
    @Path("/{locationNumber}")
    @Consumes("application/xml")
    public Response updateBooking(@PathParam("locationNumber") String locationNumber, Reserva updatedReserva) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("booking");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String bookingLocationNumber = element.getAttribute("location_number");
                    if (bookingLocationNumber.equals(locationNumber)) {

                        Reserva reserva = new Reserva(node);

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource domSource = new DOMSource(document);
                        StreamResult streamResult = new StreamResult(file);
                        transformer.transform(domSource, streamResult);

                        return Response.status(Response.Status.OK).entity("Reserva actualizada correctamente").build();
                    }
                }
            }
            return Response.status(Response.Status.NOT_FOUND).entity("La reserva no se encontró").build();
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
