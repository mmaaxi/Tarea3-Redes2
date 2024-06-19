
import java.net.*;
import java.io.*;
import java.util.Arrays;

/**
 *
 * @author axele
 */
public class CecoD {

    public static void main(String[] args) {
        try {
            int pto = 8000;
            String dir = "127.0.0.1";
            InetAddress dst = InetAddress.getByName(dir);
            int tam = 10, x = 0;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            DatagramSocket cl = new DatagramSocket();
            while (true) {
                x = 0;
                System.out.println("Escribe un mensaje, <Enter> para enviar, \"salir\" para terminar");
                String msj = br.readLine();
                if (msj.compareToIgnoreCase("salir") == 0) {
                    System.out.println("fin");
                    br.close();
                    cl.close();
                    System.exit(0);
                } else {
                    byte[] b = msj.getBytes();
                    if (b.length > tam) {
                        byte[] b_eco = new byte[b.length];
                        //System.out.println("b_eco: "+b_eco.length+" bytes");
                        int tp = (int) (b.length / tam);
                        //                  if(b.length%tam>0)
                        //                      tp=tp+1;
                        for (int j = 0; j < tp; j++) {
                            //byte[] tmp = new byte[tam];
                            x++;
                            byte[] tmp = Arrays.copyOfRange(b, j * tam, ((j * tam) + (tam)));
                            //System.out.println("Paquete:"+x+", bytes enviados: "+tmp.length);
                            DatagramPacket p = new DatagramPacket(tmp, tmp.length, dst, pto);
                            cl.send(p);
                            System.out.println("Enviando fragmento " + x + "\ndesde:" + (j * tam) + " hasta " + ((j * tam) + (tam - 1)));
                            DatagramPacket p1 = new DatagramPacket(new byte[tam], tam);
                            cl.receive(p1);
                            byte[] bp1 = p1.getData();
                            for (int i = 0; i < tam; i++) {
                                //System.out.println((j*tam)+i+"->"+tmp[i]);
                                b_eco[(j * tam) + i] = bp1[i];
                            }//for
                        }//for
                        if (b.length % tam > 0) { //bytes sobrantes  
                            //tp=tp+1;
                            x++;
                            int sobrantes = b.length % tam;
                            System.out.println("sobrantes:" + sobrantes);
                            //System.out.println("paquete: " + x + "  b:" + b.length + "ultimo pedazo desde " + tp * tam + " hasta " + ((tp * tam) + sobrantes - 1));
                            byte[] tmp = Arrays.copyOfRange(b, tp * tam, ((tp * tam) + sobrantes));
                            //System.out.println("tmp tam "+tmp.length);
                            DatagramPacket p = new DatagramPacket(tmp, tmp.length, dst, pto);
                            cl.send(p);
                            DatagramPacket p1 = new DatagramPacket(new byte[tam], tam);
                            cl.receive(p1);
                            byte[] bp1 = p1.getData();
                            for (int i = 0; i < sobrantes; i++) {
                                // System.out.println((tp*tam)+i+"->"+i);
                                b_eco[(tp * tam) + i] = bp1[i];
                            }//for
                        }//if

                        String eco = new String(b_eco);
                        System.out.println("Eco recibido: " + eco);
                    } else {
                        DatagramPacket p = new DatagramPacket(b, b.length, dst, pto);
                        cl.send(p);
                        DatagramPacket p1 = new DatagramPacket(new byte[65535], 65535);
                        cl.receive(p1);
                        String eco = new String(p1.getData(), 0, p1.getLength());
                        System.out.println("Eco recibido: " + eco);
                    }//else
                }//else
            }//while
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main
}