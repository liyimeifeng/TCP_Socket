package com.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPSocketServer {
	public static void main(String[] args) {
		DatagramSocket serverSocket = null;
		try {
			 serverSocket = new DatagramSocket(8888);
			 System.out.println("服务端已开启准备接收.....");
			 byte[] data = new byte[1024];
			 DatagramPacket packet = new DatagramPacket(data, data.length);
			 serverSocket.receive(packet);
			 String text = new String(packet.getData(),packet.getOffset(),packet.getLength());
			 System.out.println("客户端发来的数据：  " + text);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
