package com.Concordia;

import com.Concordia.Core.DistributedRepository;
import com.Concordia.Core.Repository;
import com.Concordia.Services.RepositoryServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Service_Deamon {
    public static void main(String[] args) {
        try {
            Repository server = new RepositoryServiceImpl();
            DistributedRepository stub = (DistributedRepository) UnicastRemoteObject.exportObject((DistributedRepository) server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("DistributedRepositoryService", stub);
            System.out.println("Service Deamon Started");
        } catch (Exception ex) {
            System.err.println("Something Occurred in Server");
            ex.printStackTrace();
        }
    }

}
