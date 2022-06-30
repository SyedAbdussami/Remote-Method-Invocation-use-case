package com.Concordia;

import com.Concordia.Core.DistributedRepository;
import com.Concordia.Core.Repository;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try{
            Registry registry= LocateRegistry.getRegistry();
            DistributedRepository server=(DistributedRepository) registry.lookup("DistributedRepositoryService");
            //Invoke server methods
            System.out.println(server.SET("A",5));
            System.out.println(server.ADD("A",25));
            System.out.println(server.SUM("A"));
            System.out.println(server.DELETE("A",5));
            System.out.println(server.SET("r1.A",25));
            System.out.println(server.ADD("r1.A",45));
            System.out.println(server.SET("r2.A",100));
            System.out.println(server.ADD("r2.A",200));
//            System.out.println(server.SET("r1.A",25));
            System.out.println(server.ADD("r1.A",45));
            String [] repos={"r1","r2"};
            System.out.println(server.aggregateSum(repos,"A"));

            System.out.println(server.SET("B",89));
            System.out.println(server.ADD("B",156));
            System.out.println(server.SUM("B"));

            System.out.println(server.DELETE_KEY("B"));



        }catch (Exception ex){
            System.err.println("Something Happened on the client side");
            ex.printStackTrace();
        }
    }
}
