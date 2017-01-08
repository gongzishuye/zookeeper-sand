package com.apchee.zookeeper.server.util;

import org.slf4j.Logger;

import java.io.File;

/**
 * @author chen lu
 * @date 2017/1/8
 * @time 21:39
 */
public class VerifyingFileFactory {

    private boolean warnForRelativePath;
    private final Logger logger;

    public VerifyingFileFactory(Logger logger) {
        this.logger = logger;
    }

    public void setWarnForRelativePath(boolean warnForRelativePath) {
        this.warnForRelativePath = warnForRelativePath;
    }

    public Builder build() {
        return new Builder();
    }

    public class Builder {

        public File create(String path) {
            File file = new File(path);
            doFailForNonExistingPath(file);
            if(VerifyingFileFactory.this.warnForRelativePath) {
                doFailForRelativePath(file);
            }
            return file;
        }

        private void doFailForRelativePath(File file) {
            if(file.isAbsolute()) return;
            if(file.getPath().startsWith("." + File.separator)) return;
            logger.warn(file.getPath()+" is relative. Prepend ."
                    +File.separator+" to indicate that you're sure!");
        }


        private void doFailForNonExistingPath(File file) {
            if(!file.exists()) {
                throw new IllegalArgumentException(file.getPath()
                + " not exit");
            }
        }
    }
}
