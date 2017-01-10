package com.apchee.zookeeper.server.persistence;

import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author chen lu
 * @date 2016/7/24
 * @time 14:45
 */
public class FileTxnLog {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileTxnLog.class);
    private File txnLogDir;
    public FileTxnLog(File txnLogDir) {
        this.txnLogDir = txnLogDir;
    }
}
