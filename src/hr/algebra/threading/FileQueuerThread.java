/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threading;

import java.io.File;
import java.util.List;

/**
 *
 * @author lcabraja
 */
public class FileQueuerThread implements Runnable {

    private final File[] filesToUpload;
    private int lastIndex;

    public FileQueuerThread(final List<File> filesToUpload) {
        this.filesToUpload = new File[filesToUpload.size()];
        for (int i = 0; i < filesToUpload.size(); i++) {
            this.filesToUpload[i] = filesToUpload.get(i);
        }
        this.lastIndex = filesToUpload.size() - 1;
    }

    @Override
    public void run() {
        while (ThreadHelper.hasFiles() && lastIndex >= 0) {
            Pack();
        }
        if (lastIndex < 0) {
            System.out.println("Queuer (" + hashCode() + ") done");
        }
    }

    private synchronized void Pack() {
        while (ThreadHelper.fileEnqueued && !ThreadHelper.fileCopied) {
            try {
                System.out.println("Queuer (" + hashCode() + ") waiting " + ThreadHelper.fileEnqueued);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Queuer (" + hashCode() + ") in, lastIndex = " + lastIndex);
        File imageToupload = filesToUpload[lastIndex--];
        ThreadHelper.setImageToSend(imageToupload);
        ThreadHelper.filesCopied--;
        ThreadHelper.fileCopied = false;
        ThreadHelper.fileEnqueued = true;
        notifyAll();

    }
}
