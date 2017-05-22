package es.schooleando.ut3ejercicio2;

import android.graphics.Bitmap;

/**
 * Created by matah on 07/05/2017.
 */
//creamos una interfaz y declaramos los métodos que implementaremos en el main
public interface InterfazDownloadURLTask {

    void finalizarDescarga(Bitmap bitmap);

    void actualizabar(int progreso);

    void cancelarDescarga(String mensaje);
}
