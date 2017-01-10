package com.apchee.zookeeper.server.persistence;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author chen lu
 * @date 2016/7/1
 * @time 17:33
 */
public class FileTxnSnapLog {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(FileTxnSnapLog.class);
    private static final String VERSION_PREFIX = "version-";
    private static final int VERSION = 2;
    private final File txnLogDir;
    private final File snapDir;
    private FileSnap fileSnap;
    private FileTxnLog fileTxnLog;

    public FileTxnSnapLog(File txnLogDir, File snapDir) throws DataDirException{
        logger.debug("Opening txnLogDir:{} snapDir:{}", txnLogDir, snapDir);

        this.txnLogDir = new File(txnLogDir, VERSION_PREFIX + VERSION);
        if (!txnLogDir.exists() && txnLogDir.mkdirs()) {//创建文件可能失败
            throw new DataDirException("Creating txnLogDir exception.");
        }
        this.snapDir = new File(snapDir, VERSION_PREFIX + VERSION);
        if (!snapDir.exists() && snapDir.mkdirs()) {
            throw new DataDirException("Creating snapDir exception.");
        }

        this.fileSnap = new FileSnap(snapDir);
        this.fileTxnLog = new FileTxnLog(txnLogDir);
    }

    public static class DataDirException extends IOException {
        public DataDirException(String message) {
            super(message);
        }
    }
}
