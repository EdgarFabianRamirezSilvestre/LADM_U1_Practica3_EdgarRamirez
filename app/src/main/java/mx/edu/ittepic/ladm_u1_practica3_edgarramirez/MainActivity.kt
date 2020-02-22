package mx.edu.ittepic.ladm_u1_practica3_edgarramirez

import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    val vector : Array<Int> = Array(10,{0})
    var posicion = 0
    var contenido = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boton3.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 0
                )
            }

            guardarArchivoSD()

        }//boton3

        boton1.setOnClickListener {
            insertarVector()
        }

        boton2.setOnClickListener {
            mostrarContenido()
        }

        boton4.setOnClickListener {
            leerArchivoSD()
        }

    }//onCreate

    //FUNCIONES DE MENSAJE
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage((m))
            .setPositiveButton("OK"){d,i->}
            .show()
    }//mensaje

    fun mensajeVector(m:String){
        AlertDialog.Builder(this)
            .setTitle("VECTOR")
            .setMessage((m))
            .setPositiveButton("OK"){d,i->}
            .show()
    }//mensajeVector

    //FUNCION QUE VERIFICA LA EXISTENCIA DE UNA SD
    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if (estado!=Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }//noSD

    private fun insertarVector() {
        if(editText.text.isEmpty() || editText2.text.isEmpty()) {
            mensaje("Error todos los campos deben ser llenados")
            return
        }
        var data = editText.text.toString().toInt()
        var posicion = editText2.text.toString().toInt()
        vector[posicion]=data
        mensaje("INSERCION CORRECTA")
        vaciarCampos()
    }//insertarVector

    // FUNCION QUE MUESTRA EL CONTENIDO DEL VECTOR
    fun mostrarContenido() {
        contenido=""

        (0..9).forEach {
            contenido = contenido + vector[it]
            if(it<9){
            contenido = contenido + "-"}
        }
        mensajeVector(contenido)
    }//mostrarContenido

    //FUNCION PARA LEER ARCHIVOS DESDE LA SD
    fun leerArchivoSD(){
        var nombreArchivo = editText4.text.toString()
        if(noSD()){
            mensaje("NO EXISTE MEMORIA EXTERNA")
            return
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombreArchivo)

            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()

            mensajeVector(data)

            var vectorsito= data.split("-")

            (0..9).forEach {
                vector[it]=vectorsito[it].toInt()
            }

            flujoEntrada.close()
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//leerArchivoSD

    //FUNCION PARA GUARDAR ARCHIVOS EN LA SD
    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("NO EXISTE MEMORIA EXTERNA SD")
            return
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var data = editText3.text.toString()
            var datosArchivo = File(rutaSD.absolutePath, data)
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))

            flujoSalida.write(contenido)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("¡EXITO! Se guardo el vector correctamente en MEMORIA EXTERNA SD")

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//guardarArchivoSD

    //FUNCION QUE VACIA LOS CAMPOS
    private fun vaciarCampos() {
        editText.setText("")
        editText2.setText("")
        editText3.setText("")
        editText4.setText("")
    }//vaciarCampos

}//main
