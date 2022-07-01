package com.Concordia;

import com.Concordia.Core.DistributedRepository;
import com.Concordia.Core.RepException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws RepException {
        try {
            Registry registry = LocateRegistry.getRegistry();
            DistributedRepository server = (DistributedRepository) registry.lookup("DistributedRepositoryService");
            //Invoking remote methods
            System.out.println(server.SET("A", 5));
            System.out.println(server.ADD("A", 25));
            System.out.println(server.SUM("A"));
            System.out.println(server.DELETE("A", 5));
            System.out.println(server.SET("r1.A", 25));
            System.out.println(server.ADD("r1.A", 45));
            System.out.println(server.SET("r2.A", 100));
            System.out.println(server.ADD("r2.A", 200));
            System.out.println(server.ADD("r1.A", 45));
            String[] repos = {"r1", "r2"};
            //Aggregate sum of r1 and r2
            System.out.println(server.aggregateSum(repos, "A"));
            System.out.println(server.SET("B", 89));
            System.out.println(server.ADD("B", 156));
            System.out.println(server.SUM("B"));
            //deleting key b from repo0
            System.out.println(server.DELETE_KEY("B"));

            System.out.println(server.SET("V", 123));
            System.out.println(server.SET("Z", 789));

            System.out.println(server.SET("r1.P", 123));
            System.out.println(server.SET("r1.U", 789));

            System.out.println("Repos key list: "+ server.KEY_LIST("r1"));

            System.out.println("Repos key list: "+ server.LIST_VALUES("A"));
            System.out.println("Repos key list: "+ server.GET_VALUE("r2.A"));

            //Reset all keys in repo r1 and r2
            System.out.println("Repos reset: "+server.RESET(repos));

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RepException("Error occurred while invoking remote methods",ex);
        } catch (NotBoundException e) {
            System.err.println("Remote Object not located in the remote machine");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
