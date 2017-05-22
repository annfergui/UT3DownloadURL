package es.schooleando.ut3ejercicio2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ruben on 18/11/16.
 */

public class DownloadURLTask extends AsyncTask<String,Integer,Bitmap> {
    private static final String LOGTAG="DownloadURLTask";
    private InterfazDownloadURLTask metods;
    //vamos a pasarle la referencia del contexto y la envolveremos dentro de una WeakReference
    //asi el GarbageCollector podrá liberar la memoria del Activity aunque AsyncTask siga ejecutándose
    private WeakReference<Context> contexto;
    private String error;



    public DownloadURLTask(Context contexto) {

        this.contexto=new WeakReference<>(contexto);
        //le pasamos la interfaz al context
       this.metods = (InterfazDownloadURLTask)contexto;

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bmap = null;

        //Comprobamos la conexión de red
        ConnectivityManager connMgr = (ConnectivityManager) contexto.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();

        //comprobamos si existe la conexión y si no devuelve error
        if(networkInfo!=null&&networkInfo.isConnected()){
            Log.d(LOGTAG, "Llega a buscar conectividad");
            //creamos objeto URL
            try {
                //usamos la URL para abrir la conexión y descargar la imagen
                URL url=new URL(params[0]);
                HttpURLConnection con=(HttpURLConnection) url.openConnection();
                //hacemos una petición head para obtener el progreso de la descarga
                con.setRequestMethod("HEAD");
                con.connect();
                //comprobamos que el recurso es una imagen para guardarla como binario ys si no devuelve error
                if(con.getContentType().contains("image/")){
                    //creamos un int,un InputStream y un ByteArrayOutputStream(para que nos escriba byte a byte en un array)
                    //y un byte de 1024
                    int tamanyo=con.getContentLength();
                    InputStream is=url.openStream();
                    ByteArrayOutputStream os=new ByteArrayOutputStream();
                    byte[] b=new byte[1024];
                    //vamos a descargar el archivo poco a poco
                    // si el inputStream es distinto de -1 escribimos en el ByteArrayoutputStream
                    for(int i;(i=is.read(b))!=-1;){
                        //si el contexto es null entonces cancelamos
                        if(contexto==null) {
                            cancel(true);
                            //cancela("El contexto no existe.Cerrando...");
                           //os.close();
                            //is.close();
                            //return null ;

                       }

                        //escribimos en el OutputStream(byte, off(the start offset in data),len(the number of bytes to write))
                        os.write(b,0,i);
                        //utilizamos el tamaño del buffer tamanyo para escribir el progreso
                        if(tamanyo>0){

                            publishProgress(os.size()*100/tamanyo);
                        }else{
                            publishProgress(i*-1);
                        }
                    }
                    bmap= BitmapFactory.decodeByteArray(os.toByteArray(),0,os.size());

                    //cerramos el input y el output
                    os.close();
                    is.close();


                }else{
                    cancela("No es una imagen");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            cancela("No hay una conexión");
        }

        return bmap;

    }
    //método cuando cancelamos
    public void cancela(String mensaje){
        Log.d(LOGTAG,mensaje);
        this.error=mensaje;
        cancel(true);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap b) {
        if (contexto != null) this.metods.finalizarDescarga(b);


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (contexto != null){ metods.actualizabar(values[0]);
           }
    }

    @Override
    protected void onCancelled() {
        if (contexto != null) metods.cancelarDescarga(this.error);
    }
}