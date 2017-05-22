package es.schooleando.ut3ejercicio2;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

//implementamos la interfaz donde hemos declarado los métodos
public class DownloadActivity extends AppCompatActivity implements InterfazDownloadURLTask {
    //declaramos las variables que usaremos
    private ProgressBar pb;
    private TextView tv;
    private DownloadURLTask duTask;
    private ImageView iv;
    private EditText et;
    private TextView tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "Inicializando...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        //recogemos las variables del layout
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        et=(EditText)findViewById(R.id.textURL);
        tv=(TextView)findViewById(R.id.barPercent);
        iv=(ImageView)findViewById(R.id.imageView);
        tv1=(TextView)findViewById(R.id.textView);
    }
//implementamos el método onClick del botón descarga
    public void btnDesc(View view){
       pb.setVisibility(View.VISIBLE);
        duTask=new DownloadURLTask(this);
        duTask.execute(et.getText().toString());
    }

    @Override
    public void finalizarDescarga(Bitmap bitmap) {
    //ocultamos la progressbar, vaciamos el textview y modificamos las imagen
        pb.setVisibility(View.INVISIBLE);
        tv.setText("");
        iv.setImageBitmap(bitmap);
    }

    @Override
    public void actualizabar(int progreso) {
        //actualizamos la descarga
      pb.setIndeterminate(progreso<0);
        if(progreso>0)tv.setText(""+progreso+"%");

        pb.setProgress(progreso);

    }

    @Override
    public void cancelarDescarga(String mensaje) {
        //cancelamos la descarga y ocultamos la progressbar
    pb.setVisibility(View.INVISIBLE);
        tv.setText("");
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}
