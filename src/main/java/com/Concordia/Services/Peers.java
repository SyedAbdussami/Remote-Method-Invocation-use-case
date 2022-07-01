package com.Concordia.Services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Peers {
    ServerSocket tcpSocket;
    DatagramSocket udpSocket;
    MulticastSocket multicastSocket;
    int peerPort;
    private Map<String, String> lookup;
    byte[] buffer = new byte[4096];
    PrintWriter out;
    BufferedReader in;
    HashMap<String, ArrayList<Integer>> map = new HashMap<>();

    Peers(int port) throws IOException {
        this.peerPort = port;
        tcpSocket = new ServerSocket(port);
        udpSocket = new DatagramSocket(port);
        multicastSocket = new MulticastSocket(5555);
        InetAddress groupAddress = InetAddress.getByName("225.0.0.1");
        multicastSocket.joinGroup(groupAddress);
        multicastSocket.setReuseAddress(true);

    }

    private void start() throws IOException {
        buffer = "PEER_DISC".getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 6661);
        udpSocket.setBroadcast(true);
        udpSocket.send(packet);
        System.out.println("Peer at port " + peerPort + " Sent a message");
        while (true) {
            byte[] newBuffer = new byte[4096];
            DatagramPacket packet1 = new DatagramPacket(newBuffer, newBuffer.length);
            multicastSocket.receive(packet1);
            String resp = new String(packet1.getData(), packet1.getOffset(), packet1.getLength());
            if (resp.equals("DISC_END")) {
                System.out.println("All Peers registered. No more can be accommodated");
                break;
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                lookup = mapper.readValue(resp, Map.class);
            } catch (IOException ex) {
                System.out.println("Exception occurred in peer");
                ex.printStackTrace();
            }
            System.out.println("lookup table");
            lookup.forEach((key, value) -> System.out.println(key + " = " + value));
            System.out.println("------------------------------------------------------------");
        }
    }

    void compute() throws IOException {
        Socket socket;

        while (true) {
            try {
                socket = tcpSocket.accept();
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Request Received from " + socket.getInetAddress() + " " + socket.getPort());
                String inputLine = in.readLine();
                int sum;
                if (!inputLine.contains(" ")&& !inputLine.equals("RESET")&&!inputLine.equals("LIST_KEYS")) {
                    out.println("Server : Please Enter the correct command");
                    continue;
                }
                String[] vectors = inputLine.split(" ");
                ArrayList<Integer> arrayList=new ArrayList<>();
                if(!Objects.equals(vectors[0], "RESET")&&!inputLine.equals("LIST_KEYS")){
                    arrayList = map.get(vectors[1]);
                }
                switch (vectors[0]) {
                    case "SET":
                        ArrayList<Integer> newArrayList = new ArrayList<>();
                        newArrayList.add(Integer.parseInt(vectors[2]));
                        map.put(vectors[1], newArrayList);
                        out.println("Server : OK");
                        System.out.println("Data set for key : " + vectors[1]);
                        break;
                    case "ADD":
                        arrayList.add(Integer.parseInt(vectors[2]));
                        map.put(vectors[1], arrayList);
                        out.println("Server : OK");
                        System.out.println("Data added for key : " + vectors[1]);
                        break;
                    case "SUM":
                        sum = arrayList.stream().mapToInt(Integer::intValue).sum();
                        out.println("Server : OK " + sum);
                        System.out.println("Data summed for key : " + vectors[1]);
                        break;
                    case "DELETE":
                        arrayList.remove(new Integer(vectors[2]));
                        out.println("Server : OK " + arrayList);
                        System.out.println("Data deleted for key : " + vectors[1]);
                        break;
                    case "DELETE_KEY":
                        map.remove(vectors[1]);
                        out.println("Data for Key "+vectors[1]+" Deleted: "+arrayList);
                        System.out.println("Key "+vectors[1]+" and corresponding data deleted "+arrayList);
                        break;
                    case "RESET":
                        map.clear();
                        out.println("Repo reset");
                        System.out.println("Request to reset repo executed");
                        break;
                    case "LIST_KEYS":
                        ArrayList<String> keyList=new ArrayList<>(map.keySet());
                        out.println("Key List : "+keyList);
                        break;
                    case"LIST_VALUES":
                        ArrayList<Integer> valueList=new ArrayList<>(map.get(vectors[1]));
                        out.println("Values associated with Key "+vectors[1]+ " : "+valueList);
                        break;
                    case "GET_VALUE":
                        int randIndex=(int)(Math.random() * map.get(vectors[1]).size());
                        out.println("A value associated with the Key "+vectors[1]+ " : "+map.get(vectors[1]).get(randIndex));
                        break;
                    default:
                        out.println("Error. Please Enter the correct command");
                }
                socket.close();
                out.close();
            } catch (IOException ex) {
                System.out.println("Exception Occurred in the peer");
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        int random = ThreadLocalRandom.current().nextInt(10, 25);
        int port = 7500 + random;
        Peers peer = new Peers(port);
        peer.start();
        peer.compute();
    }

}

