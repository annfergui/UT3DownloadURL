import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class DemoDownloadURL {
	

	public static void main(String[] args) {
		byte[]buffer=new byte[4096];
		int n=-1;
		String urlString;
		// 1. pedimos una URL por l铆nea de comandos
		BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Introduce la URL para poder descargar el archivo");
		try {
			urlString=stdin.readLine();
		
			// TODO Auto-generated catch block
			
		
		// 2. creamos el objeto URL y comprobamos que es una URL correcta
		
			URL url=new URL(urlString);
			
			// 3. Obtenemos un objeto HttpURLConnection. openConnection 
			HttpURLConnection myURLConnection=(HttpURLConnection) url.openConnection();
			
			// 4. configuramos la conexi贸n al m茅todo GET. setRequestMethod
			myURLConnection.setRequestMethod("GET");
			
			// 5. Nos conectamos. connect
			myURLConnection.connect();
			
			// 6. Obtenemos y imprimimos el c贸digo de respuesta. getResponseCode
			int respuestCode=myURLConnection.getResponseCode();
			System.out.println("El c骴igo de respuesta es:"+respuestCode);
			
			// 7. Obtenemos y imprimimos el tama帽o del recurso. ContentLength
			int respuestSize=myURLConnection.getContentLength();
			System.out.println("El tama駉 del recurso es:"+respuestSize);
			
			// 8. Guardamos el stream a un fichero con el nombre del recurso
			//    en caso de que el c贸digo sea correcto.
			if(respuestCode==200){
				InputStream bis= myURLConnection.getInputStream();
				//le a馻dimos un cast 
				//BufferedInputStream bis=(BufferedInputStream) myURLConnection.getInputStream();
				File fich=new File(myURLConnection.getResponseMessage()+".dat");
				//File fich=new File(url.toString());
				OutputStream output = new FileOutputStream(fich);
						
			while((n=bis.read(buffer))!=-1){
				output.write(buffer,0,n);
			
			
			}
			bis.close();
			output.close();
				
				System.out.println("Se ha exportado el fichero correctamente. ");
			}else{
				
				// 9. Damos un error en caso de que el c贸digo sea incorrecto (BufferedInputStream -> FileOutputStream)
				System.out.println("Error:El c骴igo es incorrecto");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.err.println("Error:La URL no es correcta");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida");
		}
		
		
		
		
	
	
		
	
		
		
		
		
		

	}

}