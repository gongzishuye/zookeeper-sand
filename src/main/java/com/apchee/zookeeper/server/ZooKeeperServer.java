package com.apchee.zookeeper.server;

import com.apchee.zookeeper.server.persistence.FileTxnSnapLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chen lu
 * @date 2017/1/10
 * @time 18:01
 */
public class ZooKeeperServer {
    protected static final Logger logger = LoggerFactory.getLogger(ZooKeeperServer.class);

    private ZKDatabase zkDb;
    private FileTxnSnapLog txnSnapLog;
    public static final int DEFAULT_TICK_TIME = 3000;
    protected int tickTime = DEFAULT_TICK_TIME;

    /** value of -1 indicates unset, use default */
    protected int minSessionTimeout = -1;
    /** value of -1 indicates unset, use default */
    protected int maxSessionTimeout = -1;

    public ZooKeeperServer(FileTxnSnapLog txnSnapLog, int tickTime,
                           int minSessionTimeout, int maxSessionTimeout, ZKDatabase zkDb) {
//        serverStats = new ServerStats(this); //当serverStats是final的时候前面不用this
        this.txnSnapLog = txnSnapLog;
        this.zkDb = zkDb;
        this.tickTime = tickTime;
        setMinSessionTimeout(minSessionTimeout);
        setMaxSessionTimeout(maxSessionTimeout);
    }

    public void setMinSessionTimeout(int min) {
        this.minSessionTimeout = min == -1 ? tickTime * 2 : min;
        logger.info("minSessionTimeout set to {}", this.minSessionTimeout);
    }

    public void setMaxSessionTimeout(int max) {
        this.maxSessionTimeout = max == -1 ? tickTime * 20 : max;
        logger.info("maxSessionTimeout set to {}", this.maxSessionTimeout);
    }
}
