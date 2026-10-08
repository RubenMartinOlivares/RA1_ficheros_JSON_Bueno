import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.modelo.Alumno;
import es.ciudadescolar.util.JsonManager;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws Exception {
      
        File ficheroJson = new File("alumnos.json");
        File ficherosJsonSalida = new File("alumnos2.json");

        LOG.info("comenzando el parseo del fichero Json["+ficheroJson.getAbsolutePath()+"]");
        
       JsonManager.parsearJsonAlumnos(ficheroJson);
        List<Alumno>alumnosEntrada = JsonManager.parsearJsonAlumnos(ficheroJson);

        LOG.info("Finalizando el parseo del fichero Json["+ficheroJson.getAbsolutePath()+"]");

        if(alumnosEntrada != null && !alumnosEntrada.isEmpty()){
            LOG.info("comenzando la generacion del fichero Json["+ficherosJsonSalida.getName()+"]");
            JsonManager.generarJsonAlumnosMayores20Anios(alumnosEntrada,ficherosJsonSalida);
            LOG.info("finzalizando la generacion del fichero Json["+ficherosJsonSalida.getName()+"]");

        }else{
            LOG.warn("no se ha recuperado NINGUN alumno del Json de entrada");
        }
    }
}
