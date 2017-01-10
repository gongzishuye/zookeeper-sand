package com.apchee.zookeeper.server.persistence;


import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * @author chen lu
 * @date 2016/7/24
 * @time 14:45
 */
public class FileSnap {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileSnap.class);
    private File snapDir;
    public FileSnap(File snapDir) {
        this.snapDir = snapDir;
    }
    public static final int MAGIC = ByteBuffer.wrap("ZKSN".getBytes()).getInt();
}
