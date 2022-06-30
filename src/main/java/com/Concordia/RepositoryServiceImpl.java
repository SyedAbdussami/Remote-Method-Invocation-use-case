package com.Concordia;

import com.Concordia.Core.Aggregate;
import com.Concordia.Core.DistributedRepository;
import com.Concordia.Core.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.RemoteException;

public class RepositoryServiceImpl implements DistributedRepository {

    Socket socket;
    PrintWriter out ;
    BufferedReader in ;
    @Override
    public String SET(String Key, int number) throws IOException {
        String message= "SET "+Key+" "+number;
        if(socket==null){
            socket = new Socket(InetAddress.getLocalHost(), 8000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Server connected to Distributed Repo");
            System.out.println("Sever Response: "+in.readLine());
        }
        out.println(message);
        String resp=in.readLine();
        System.out.println("Set response: "+resp);
        return resp;
    }

    @Override
    public String ADD(String Key, int number) throws IOException {
        String message= "ADD "+Key+" "+number;
        out.println(message);
        String resp=in.readLine();
        System.out.println("ADD response: "+resp);
        return resp;
    }

    @Override
    public String SUM(String Key) throws IOException {
        String message= "SUM "+Key;
        out.println(message);
        String resp=in.readLine();
        System.out.println("SUM response: "+resp);
        return resp;
    }

    @Override
    public String DELETE(String Key,int number) throws IOException {
        String message= "DELETE "+Key+" "+number;
        out.println(message);
        String resp=in.readLine();
        System.out.println("Delete response: "+resp);
        return resp;
    }

    @Override
    public String DELETE_KEY(String Key) throws RemoteException, IOException {
        String message= "DELETE_KEY "+Key;
        out.println(message);
        String resp=in.readLine();
        System.out.println("Delete response: "+resp);
        return resp;
    }

    @Override
    public String aggregateSum(String[] Repos, String key) throws RemoteException,IOException {
        StringBuilder repoList= new StringBuilder();
        int lastRepo=Repos.length-1;
        for(int i=0;i<Repos.length;i++){
            if(i==lastRepo){
                repoList.append(Repos[i]);
                continue;
            }
           repoList.append(Repos[i]).append(" ");
        }
        String message="DSUM "+key+" INCLUDING "+repoList;
        out.println(message);
        String resp=in.readLine();
        System.out.println("Aggregate SUM response :"+resp);
        return resp;
    }
}
