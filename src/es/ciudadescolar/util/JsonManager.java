package es.ciudadescolar.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.ciudadescolar.modelo.Alumno;

public class JsonManager {
     
    private static final Logger LOG = LoggerFactory.getLogger(JsonManager.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public static List<Alumno> parsearJsonAlumnos(File jsonFich){
        List<Alumno> alumnos = null;

      

        JsonNode raizNodo = null;
        JsonNode alumnosNodo = null;
        Alumno alumno = null;
        

        try {
            // esto es análogo a lo que haciamos con dom en xml usando  el método parse()
            // mientras que en xml obligatoriamente tengo que tener un elemnto raiz
            // en Json no es obligatorio tener un objeto raiz, sino que puede ser un array o un valor(null,cadena,número)
            raizNodo = mapper.readTree(jsonFich);

            if(raizNodo.isObject()){
                LOG.info("El curso es: "+raizNodo.get("curso").asText());
                LOG.info("El codigo es: "+raizNodo.get("codigo").asInt());
                LOG.info("El centro es: "+raizNodo.get("centro").asText());
               
                alumnosNodo = raizNodo.get("alumnos");
                if(alumnosNodo.isArray()){
                    
                    alumnos = new ArrayList<Alumno>();
                    
                    for(JsonNode alumnoNodo:alumnosNodo){
                        alumno = new Alumno();
                        alumno.setEdad(Integer.valueOf(alumnoNodo.get("edad").asInt()));
                        alumno.setNombre(alumnoNodo.get("nombre").asText());
                        alumno.setExpediente(String.valueOf(alumnoNodo.get("expediente").asInt()));

                        LOG.info("Nuevo alumno: "+alumno);
                        alumnos.add(alumno);
                        LOG.trace("Aniadido alumno a la coleccion");
                    }
                    
                }
            }

        } catch (IOException e) {
            LOG.error("Error durante el parseo del Json["+jsonFich.getName()+e.getMessage()+"]");
        }
        
        
        return alumnos;


    }

    public static void generarJsonAlumnosMayores20Anios(List<Alumno> alumnosEntrada, File ficherosJsonSalida) {
        ArrayNode arrayAlumnos = mapper.createArrayNode();

        ObjectNode nodoAlumno = null;
        for(Alumno al:alumnosEntrada){
            if(al.getEdad() > 20){
                nodoAlumno = mapper.createObjectNode();
                nodoAlumno.put("exp", al.getExpediente());
                nodoAlumno.put("edad", al.getEdad());
                nodoAlumno.put("nom", al.getNombre());

                arrayAlumnos.add(nodoAlumno);
            }


        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(ficherosJsonSalida, arrayAlumnos);
        } catch (IOException e) {
           LOG.error("Errror durante la escritura del fichero Json"+ e.getMessage());
       
        }
    }

    //La libreria jackson ya tiene desarrolada la funcionalidad de serializacion y por tanto
    //no necesitamos crear la estructura en memoria previamente al volcado
    //usariamos la propia serializacion
    public static void generarJsonAlumnos2(List<Alumno> alumnosEntrada, File ficherosJsonSalida){
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(ficherosJsonSalida, alumnosEntrada);
        } catch (IOException e) {
            LOG.error("Error en la serializacion de los alumnos en un fichero Json");
        }
    }


}
