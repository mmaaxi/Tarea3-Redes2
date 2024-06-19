import java.net.*;
/**
 *
 * @author axele
 */
public class SecoD {
    public static void main(String[] args){
      try{  
          int pto=8000;
          String msj="";
          DatagramSocket s = new DatagramSocket(pto);
          s.setReuseAddress(true);
         // s.setBroadcast(true);
          System.out.println("Servidor iniciado... espedando datagramas..");
          for(int i=1;;i++){
              byte[] b = new byte[65535];
              DatagramPacket p = new DatagramPacket(b,b.length);
              s.receive(p);
              msj = new String(p.getData(),0,p.getLength());
              System.out.println("Se ha recibido datagrama desde "+p.getAddress()+":"+p.getPort()+" con los datos: num paquete:"+i+" con "+p.getLength()+" bytes y con el mensaje: "+msj);
              s.send(p);
          }//for

      }catch(Exception e){
          e.printStackTrace();
      }//catch

    }//main
}