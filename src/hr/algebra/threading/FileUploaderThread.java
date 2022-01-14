/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threading;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lcabraja
 */
public class FileUploaderThread implements Runnable {

    private final File destination;

    public FileUploaderThread(File destination) {
        this.destination = destination;
    }

    @Override
    public void run() {
        while (ThreadHelper.hasFiles()) {
            Upload();
        }
        if (ThreadHelper.filesCopied <= 0) {
            System.out.println("Uploader done");
        }
    }

    private synchronized void Upload() {
        while (!ThreadHelper.fileEnqueued && ThreadHelper.fileCopied) {
            try {
                System.out.println("Uploader waiting " + ThreadHelper.fileEnqueued);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ThreadHelper.fileEnqueued  = true;
        ThreadHelper.fileCopied = false;
        System.out.println("Uploader in");
        try {
            Files.copy(Paths.get(ThreadHelper.getImageToSend().getAbsolutePath()), Paths.get(destination.getAbsolutePath(), "/" + ThreadHelper.getImageToSend().getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(FileUploaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Uploader copied");
        ThreadHelper.fileEnqueued = false;
        ThreadHelper.fileCopied = true;
        notifyAll();
    }

}
