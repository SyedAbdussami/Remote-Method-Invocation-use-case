package com.Concordia.Core;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Aggregate extends Remote {
    String aggregateSum(String[] Repos,String key) throws RemoteException, IOException;
}
