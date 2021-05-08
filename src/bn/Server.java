package bn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int count=0;
    static ServerAgent player1;
    static ServerAgent player2;


    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket(1999);
        System.out.println("正在监听1999端口");
        while (true){
            Socket sc=ss.accept();
            DataInputStream din=new DataInputStream(sc.getInputStream());
            DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
            switch(count){
                case 0:
                    count++;
                    System.out.println("<#ACCEPT#>1");
                    dout.writeUTF("<#ACCEPT#>1");
                    player1=new ServerAgent(sc,din,dout);
                    break;
                case 1:
                    count++;
                    System.out.println("<#ACCEPT#>2");
                    dout.writeUTF("<#ACCEPT#>2");
                    player2=new ServerAgent(sc,din,dout);

                    player1.start();
                    player2.start();
                    player1.dout.writeUTF("<#START#>");
                    player2.dout.writeUTF("<#START#>");

                    int len=WordsStore.wordssotre.length;
                    String s=WordsStore.wordssotre[(int)(Math.random()*len)];
                    player1.dout.writeUTF("<#DATA#>0|"+s+"|true");
                    player2.dout.writeUTF("<#DATA#>0|"+s+"|true");
                    System.out.println("p1-<#DATA#>0|"+s+"|true");
                    System.out.println("p2-<#DATA#>0|"+s+"|true");
                    try {
                        Thread.sleep(3000);
                        s=WordsStore.wordssotre[(int)(Math.random()*len)];
                        player1.dout.writeUTF("<#DATA#>1|"+s+"|true");
                        player2.dout.writeUTF("<#DATA#>1|"+s+"|true");
                        System.out.println("p1-<#DATA#>1|"+s+"|true");
                        System.out.println("p2-<#DATA#>1|"+s+"|true");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    System.out.println("<#FULL#>");
                    dout.writeUTF("<#FULL#>");
                    dout.close();
                    din.close();
                    sc.close();
            }

        }
    }

}
