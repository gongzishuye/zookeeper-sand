package com.apchee.zookeeper.server;

import com.apchee.zookeeper.server.ServerConfig.ConfigException;
import com.apchee.zookeeper.server.persistence.FileTxnSnapLog;

/**
 * @author chen lu
 * @date 2017/1/8
 * @time 21:08
 */
public class ZookeeperServerMain {

    public static void main(String[] args) {
        ZookeeperServerMain main = new ZookeeperServerMain();
        try {
            main.initializeAndRun(args);
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }

    protected void initializeAndRun(String[] args)
            throws ConfigException {
        ServerConfig cfg = new ServerConfig();
        if(args.length == 1) {
            cfg.parse(args[0]);
        }

        runFromConfig(cfg);
    }

    protected void runFromConfig(ServerConfig config) {
        FileTxnSnapLog txnSnapLog = null;
        ZooKeeperServer zkServer = new ZooKeeperServer(txnSnapLog,
                config.tickTime, config.minSessionTimeout, config.maxSessionTimeout, null);

    }
}
