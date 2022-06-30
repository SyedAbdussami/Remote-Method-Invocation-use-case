package com.Concordia.Core;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Repository extends Remote {

    //    HashMap<String, ArrayList<Integer>> SET(String Key, int number) throws RemoteException, IOException;
    String SET(String Key, int number) throws RemoteException, IOException;

   String ADD(String Key, int number) throws RemoteException,IOException;

    String SUM(String Key) throws RemoteException,IOException;

    String DELETE(String Key,int number) throws RemoteException,IOException;

    String DELETE_KEY(String Key) throws RemoteException,IOException;



}
