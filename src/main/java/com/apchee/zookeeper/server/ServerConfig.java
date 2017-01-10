package com.apchee.zookeeper.server;

import com.apchee.zookeeper.server.util.VerifyingFileFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;

/**
 * @author chen lu
 * @date 2017/1/8
 * @time 21:11
 */
public class ServerConfig {

    private final Logger logger;

    public ServerConfig() {
        logger = LoggerFactory.getLogger(ServerConfig.class);
    }
    protected InetSocketAddress clientPortAddress;
    protected InetSocketAddress secureClientPortAddress;

    protected File dataDir;
    protected File dataLogDir;

//    protected int snapRetainCount = 3;功能暂时屏蔽，后期再加入
//    protected int purgeInterval = 0;

    protected int tickTime = ZooKeeperServer.DEFAULT_TICK_TIME;

    /** defaults to -1 if not set explicitly */
    protected int minSessionTimeout = -1;
    /** defaults to -1 if not set explicitly */
    protected int maxSessionTimeout = -1;

    private final int MIN_SNAP_RETAIN_COUNT = 3;//为什么没有加static呢，为什么是private呢

    public static class ConfigException extends Exception {
        public ConfigException(String msg) {
            super(msg);
        }

        public ConfigException(String msg, Exception exp) {
            super(msg, exp);
        }
    }

    public void parse(String path) throws ConfigException {
        try{//只在需要的地方try catch
            File cfgFile = new VerifyingFileFactory(logger)
                .setWarnForRelativePath(true).build()
                .create(path);

            Properties properties = new Properties();
            FileInputStream stream = new FileInputStream(cfgFile);
            try {
                properties.load(stream);//不需要了就关上
            } finally {//少写一个catch
                if(stream != null) stream.close();
            }
            parseCfg(properties);
        } catch (IOException e) {
            throw new ConfigException("");
        } catch (IllegalArgumentException e) {
            throw new ConfigException("");
        }
    }

    //这种方式，成员变量不能用final了，因为成员变量需要直接赋初值
    //这里的成员变量命名是final的，为什么不搞成final的呢?
    //和root的SystemConfig相比，各有优劣，这种方式不用每个都trim了，root的方式getProperties必须要用默认值
    public void parseCfg(Properties properties) {
        VerifyingFileFactory.Builder build = new VerifyingFileFactory(logger)
                .setWarnForRelativePath(true).build();

        int clientPort = 0;
        for(Map.Entry<Object, Object> entry: properties.entrySet()) {
            String key = entry.getKey().toString().trim();
            String value = entry.getValue().toString().trim();
            logger.error("properties key=" + key
                     + ", properties value=" + value);

            if(key.equals("dataDir")) {//properties文件key也是驼峰写法
                dataDir = build.create(value);
            } else if(key.equals("dataLogDir")) {
                dataLogDir = build.create(value);
            }  else if (key.equals("clientPort")) {
                clientPort = Integer.parseInt(value);
            } else if (key.equals("tickTime")) {
                tickTime = Integer.parseInt(value);
            } else if (key.equals("minSessionTimeout")) {
                minSessionTimeout = Integer.parseInt(value);
            } else if (key.equals("maxSessionTimeout")) {
                maxSessionTimeout = Integer.parseInt(value);
            }
        }

        if(dataDir == null) {
            throw new IllegalArgumentException("dataDir is not set");
        }

        if(dataLogDir == null) {
            dataLogDir = dataDir;
        }

        if(clientPort == 0) {//简化了配置，如果想启动zookeeper必须设置clientPort
            throw new IllegalArgumentException("clinetPort is not set");
        } else {
            this.clientPortAddress = new InetSocketAddress(clientPort);
            logger.info("clientPortAddress is {}", this.clientPortAddress.toString());
        }

        if(tickTime <= 0) throw new IllegalArgumentException("tickTime is not legal");//小bug

        //session默认时间，最大是20倍tickTime，最小是2倍tickTime
        minSessionTimeout = minSessionTimeout == -1 ? tickTime * 2 : minSessionTimeout;
        maxSessionTimeout = maxSessionTimeout == -1 ? tickTime * 20 : maxSessionTimeout;
    }
}
