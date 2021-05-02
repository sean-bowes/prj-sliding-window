package com.koisoftware.glissade;

import java.net.*;
import java.io.*;
import java.util.*;

public class GoBackClient {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            Socket s = new Socket("localhost", 8080);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int ws = dis.readInt();
            System.out.println("Current window size: " + ws);
            int size = dis.readInt();
            char a[] = new char[size];
            int fa;
            int fno;
            int flag = 0;
            int cnt = 0;
            int i = 0;
            while (i < a.length) {
                if (cnt == ws || flag == 1) {
                    System.out.print("Enter 1 (for yes) if all frames have been received: ");
                    fa = sc.nextInt();
                    dos.writeInt(fa);
                    cnt = 0;
                    flag = 0;
                    if (fa != 1) {
                        do {
                            System.out.print("Enter missing frame: (" + (i - ws + 1) + "," + i + ")");
                            fno = sc.nextInt();
                        } while (fno < (i - ws + 1) || fno > i);

                        dos.writeInt(fno);
                        for (int j = fno - 1; j < fno + ws - 1; j++) {
                            a[j] = dis.readChar();
                            System.out.println("Received frame is: " + a[j]);
                        }
                        i = fno + ws - 1;
                        flag = 1;
                    } else {
                        System.out.println("Ack sent for frame " + i);
                        int ack = dis.readInt();
                        if (ack == 1)
                            System.out.println("Ack received");
                        else {
                            System.out.println("Ack not received");
                            int a1 = dis.readInt();
                            for (int j = a1 - 1; j < a1 + ws - 1; j++) {
                                a[j] = dis.readChar();
                                System.out.println("Received frame is: " + a[j]);
                            }
                            i = a1 + ws - 1;
                            cnt = ws;
                        }
                    }
                } else {
                    cnt++;
                    a[i] = dis.readChar();
                    System.out.println("Received frame is: " + a[i]);
                    i++;
                }
            }
            System.out.println("The whole data received is: ");
            for (i = 0; i < a.length; i++) {
                System.out.print(a[i] + " ");
            }
            dis.close();
            dos.close();
            sc.close();
            s.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sw.toString();
            System.out.println(sw.toString());
        }
    }
}

