package bn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import static bn.Server.*;

public class ServerAgent extends Thread{
    Socket sc; // 网络套接字
    DataInputStream din;
    DataOutputStream dout;
    boolean flag = true;

    int oneortwo;
    int currid;
    String currstr;
    static volatile int nowid_one=0;
    static volatile int nowid_two=0;
    static volatile int maxid_one=0;
    static volatile int maxid_two=0;

    public ServerAgent(Socket sc, DataInputStream din, DataOutputStream dout) {
        this.sc=sc;
        this.din=din;
        this.dout=dout;
    }
@Override
    public void run(){
        while (flag){
            try {
                synchronized (this) {
                    Thread.sleep(6000);
                    int len = WordsStore.wordssotre.length;
                    String s = WordsStore.wordssotre[(int) (Math.random() * len)];
                    maxid_one++;
                    maxid_two++;
                    player1.dout.writeUTF("<#DATA#>" + (maxid_one) + "|" + s + "|true");
                    player2.dout.writeUTF("<#DATA#>" + (maxid_two) + "|" + s + "|true");
                    System.out.println("p1-<#DATA#>" + (maxid_one) + "|" + s + "|true");
                    System.out.println("p2-<#DATA#>" + (maxid_two) + "|" + s + "|true");
                }
                String msg=din.readUTF();
                System.out.println("收到的消息："+msg);
                if(msg.startsWith("<#KILL#>")){
                    String temp=msg.substring(8);
                    String[] ts=temp.split("\\|");
                    oneortwo=Integer.parseInt(ts[0]);
                    currid=Integer.parseInt(ts[1]);
                    currstr=ts[2];
                    synchronized (this) {
                        if (oneortwo == 1) {
                            nowid_one++;
                            if (nowid_one < nowid_two) {
                                continue;
                            } else if (nowid_one >= nowid_two) {
                                maxid_two++;
                                player2.dout.writeUTF("<#DATA#>" + (maxid_two) + "|" + currstr + "|true");
                                System.out.println("(惩罚)p2-<#DATA#>" + (maxid_two) + "|" + currstr + "|true");
                            }
                        } else if (oneortwo == 2) {
                                    nowid_two++;
                                    if (nowid_two < nowid_one) {
                                        continue;
                                    } else if (nowid_two >= nowid_one) {
                                        maxid_one++;
                                        player1.dout.writeUTF("<#DATA#>" + (maxid_one) + "|" + currstr + "|true");
                                        System.out.println("(惩罚)p1-<#DATA#>" + (maxid_one) + "|" + currstr + "|true");
                            }
                        }
                    }
                }else if(msg.startsWith("<#EXIT#>")){
                    count=0;
                    maxid_one=0;
                    maxid_two=0;
                    nowid_two=0;
                    nowid_two=0;
                        player1.dout.writeUTF(msg);
                        player2.dout.writeUTF(msg);
                        player1.flag = false;
                        player2.flag = false;
                        player1.dout.close();
                        player1.din.close();
                        player1.sc.close();
                        player2.dout.close();
                        player2.din.close();
                        player2.sc.close();
                    }
                if(nowid_one>=5&&nowid_two<5){
                    count=0;
                    maxid_one=0;
                    maxid_two=0;
                    nowid_two=0;
                    nowid_two=0;
                    player1.dout.writeUTF("<#FINISH#>1");
                    player2.dout.writeUTF("<#FINISH#>1");
                    System.out.println("p1赢得游戏");
                    player1.flag = false;
                    player2.flag = false;
                    player1.dout.close();
                    player1.din.close();
                    player1.sc.close();
                    player2.dout.close();
                    player2.din.close();
                    player2.sc.close();
                }else if(nowid_two>=5&&nowid_one<5){
                    count=0;
                    maxid_one=0;
                    maxid_two=0;
                    nowid_two=0;
                    nowid_two=0;
                    player1.dout.writeUTF("<#FINISH#>2");
                    player2.dout.writeUTF("<#FINISH#>2");
                    System.out.println("p2赢得游戏");
                    player1.flag = false;
                    player2.flag = false;
                    player1.dout.close();
                    player1.din.close();
                    player1.sc.close();
                    player2.dout.close();
                    player2.din.close();
                    player2.sc.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            }
        }

    }
