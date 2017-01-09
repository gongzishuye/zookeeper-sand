package com.apchee.zookeeper.server;

import com.apchee.zookeeper.server.util.VerifyingFileFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    protected File dataDir;
    protected File dataLogDir;

    protected int snapRetainCount = 3;
    protected int purgeInterval = 0;

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
        for(Map.Entry<Object, Object> entry: properties.entrySet()) {
            String key = entry.getKey().toString().trim();
            String value = entry.getValue().toString().trim();
            logger.error("properties key=" + key
                     + ", properties value=" + value);

            if(key.equals("dataDir")) {//properties文件key也是驼峰写法
                dataDir = build.create(value);
            } else if(key.equals("dataLogDir")) {
                dataLogDir = build.create(value);
            } else if (key.equals("autopurge.snapRetainCount")) {
                snapRetainCount = Integer.parseInt(value);
            } else if (key.equals("autopurge.purgeInterval")) {
                purgeInterval = Integer.parseInt(value);
            }
        }

        if(dataDir == null) {
            throw new IllegalArgumentException("dataDir is not set");
        }

        if(dataLogDir == null) {
            dataLogDir = dataDir;
        }

        if(snapRetainCount < MIN_SNAP_RETAIN_COUNT) {
            logger.warn("Invalid autopurge.snapRetainCount: " + snapRetainCount
                    + ". Defaulting to " + MIN_SNAP_RETAIN_COUNT);
            snapRetainCount = MIN_SNAP_RETAIN_COUNT;
        }
    }
}
