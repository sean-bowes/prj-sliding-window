package com.koisoftware.glissade;

import java.io.*;
import java.net.*;
import java.util.*;

public class GoBackServer {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            ServerSocket ss = new ServerSocket(8080);
            Socket s = ss.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            System.out.print("ENTER the value of m: ");
            int m = sc.nextInt();
            int ws = (int) Math.pow(2, m) - 1;
            System.out.println("Window size is: " + ws);
            dos.writeInt(ws);
            String st = "GREETINGSFROMHOUSTON";
            dos.writeInt(st.length());
            char a[] = st.toCharArray();
            int ack, fa, fno;
            int flag = 0;
            //(i%ws == 0 && i>0)
            int cnt = 0;
            //for (int i=0;i<a.length;i++)
            int i = 0;
            while (i < a.length) {
                if (cnt == ws || flag == 1) {
                    fa = dis.readInt();
                    flag = 0;
                    cnt = 0;
                    if (fa != 1) {
                        fno = dis.readInt();
                        System.out.println("Resending the frames...");
                        for (int j = fno - 1; j < fno + ws - 1; j++) {
                            dos.writeChar(a[j]);
                            System.out.println("Sent frame " + j + "is: " + a[j]);
                        }
                        i = fno + ws - 1;
                        flag = 1;
                    } else {
                        System.out.println("Acknowledge received for frame  " + i);
                        System.out.print("Enter 1 (for yes) if the ack is received or 0 (for no) if not for frame " + i + ": ");
                        ack = sc.nextInt();
                        dos.writeInt(ack);


                        if (ack != 1) {
                            int a1;
                            do {
                                System.out.println("Enter no of lost ack :(" + (i - ws + 1) + "," + i + ")");
                                a1 = sc.nextInt();
                            } while (a1 < (i - ws + 1) || a1 > i);
                            dos.writeInt(a1);
                            for (int j = a1 - 1; j < a1 + ws - 1; j++) {
                                dos.writeChar(a[j]);
                                System.out.println("SENT FRAME  " + j + "IS: " + a[j]);
                            }
                            i = a1 + ws - 1;
                            cnt = ws;
                        }
                    }
                } else {
                    cnt++;
                    dos.writeChar(a[i]);
                    System.out.println("SENT FRAME " + i + "IS: " + a[i]);
                    i++;
                }
            }
            System.out.println("The whole data sent is: ");
            for (i = 0; i < a.length; i++) {
                System.out.print(a[i] + " ");
            }
            dis.close();
            dos.close();
            s.close();
            ss.close();
            sc.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
