package mx.edu.ittepic.ladm_u1_practica3_oscarramos

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    val vector : Array<Int> = Array(10,{0})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){

            //SI ENTRA ENTONCES AUN NO SE OTORGAN LOS PERMISOS, EL SIGUIENTE CODIGO LOS SOLICITA

            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

        }else{
            Toast.makeText(this,"PERMISOS YA OTORGADOS",Toast.LENGTH_LONG).show()
        }

        asignar.setOnClickListener {
            if(valor.text.isEmpty()||posicion.text.isEmpty()){
                Toast.makeText(this,"ERROR, TODOS LOS CAMPOS DEBEN TENER DATOS",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var v = valor.text.toString().toInt()
            var p = posicion.text.toString().toInt()

            vector[p] = v
            Toast.makeText(this,"SE GUARDÓ EL VALOR",Toast.LENGTH_LONG).show()
            valor.setText("")
            posicion.setText("")

        }

        mostrarT.setOnClickListener {
            var data = ""
            (0..9).forEach { p->
                data+= "[ ${vector[p]} ], "
            }

            resultado.setText(data)

        }

        guardarsd.setOnClickListener {
            guardarSD()
        }

        leerSD.setOnClickListener {
            leerSD()
        }

    }

    fun  noSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }//NOSD


    fun guardarSD(){
        if(noSD()){
            Toast.makeText(this,"NO HAY MEMORIA EXTERNA",Toast.LENGTH_LONG).show()
            return
        }

        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombre1.text.toString()+".txt")

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = ""

            (0..9).forEach {
                data += vector[it].toString()+"&"
            }
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            Toast.makeText(this,"SE GUARDO CORRECTAMENTE",Toast.LENGTH_LONG).show()

        }catch (error : IOException){

        }

    }//GUARDAR SD

    fun leerSD(){
        if(noSD()){
            Toast.makeText(this,"NO HAY MEMORIA EXTERNA",Toast.LENGTH_LONG).show()
            return
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombre2.text.toString()+".txt")
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()
            var vector = data.split("&")
            var impresion = ""
            (0..9).forEach {
                impresion+= "[ ${vector[it]} ], "
            }

            resultado.setText(impresion)
            ///Toast.makeText(this,impresion,Toast.LENGTH_LONG).show()

            flujoEntrada.close()

        }catch (error : IOException){

        }

    }
}
