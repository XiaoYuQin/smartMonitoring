package com.example.smartmonitoring;

public class Data {
	private static String IP;
	private static int port;

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Data.port = port;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}
}
