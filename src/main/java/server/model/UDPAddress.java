package server.model;

import java.net.InetAddress;
import java.util.Objects;

public class UDPAddress {
    public final InetAddress ip;
    public final int port;

    public UDPAddress(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UDPAddress that = (UDPAddress) o;
        return port == that.port && Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
