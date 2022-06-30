package com.Concordia.Core;

import java.io.IOException;
import java.rmi.Remote;

public interface Directory extends Remote {
    void start(int udpPort) throws IOException;
    void clientHandler(int tcpPort) throws IOException;
}
