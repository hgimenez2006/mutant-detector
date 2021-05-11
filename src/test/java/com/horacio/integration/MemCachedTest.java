package com.horacio.integration;

import org.junit.Test;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MemCachedTest {
    //@Test
    public void doit() throws IOException {
        //String configEndpoint = "mycluster.fnjyzo.cfg.use1.cache.amazonaws.com";
        String configEndpoint = "mutant.jqrskw.cfg.use1.cache.amazonaws.com";
        String endpoint = "mutant.jqrskw.0001.use1.cache.amazonaws.com";
        Integer clusterPort = 11211;

        MemcachedClient client = new MemcachedClient(
                new InetSocketAddress(
                        //configEndpoint,
                        endpoint,
                        clusterPort));
        // The client will connect to the other cache nodes automatically.

        // Store a data item for an hour.
        // The client will decide which cache host will store this item.
        //client.set("theKey", 3600, "This is the data value");
        String sos = (String)client.get("theKey");
        System.out.println(sos);
    }
}
