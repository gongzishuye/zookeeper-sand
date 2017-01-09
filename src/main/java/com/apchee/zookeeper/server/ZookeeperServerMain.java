package com.apchee.zookeeper.server;

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
        } catch (ServerConfig.ConfigException e) {
            e.printStackTrace();
        }
    }

    protected void initializeAndRun(String[] args)
            throws ServerConfig.ConfigException {
        ServerConfig cfg = new ServerConfig();
        if(args.length == 1) {
            cfg.parse(args[0]);
        }

    }
}
