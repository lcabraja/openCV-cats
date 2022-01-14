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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lcabraja
 */
public class ThreadHelper {

    public static boolean fileEnqueued;
    private boolean launched;
    public static int filesCopied;
    private boolean[] threads;

    private File imageToSend = null;
    private final List<File> filesToSend;

    public ThreadHelper(List<File> filesToSend, File destination) {
        this.filesToSend = filesToSend;
        this.destination = destination;
    }

    public void launchThreads(int count) {
        count = count > filesToSend.size() ? filesToSend.size() : count;
        System.out.println("count " + count);
        ExecutorService executor = Executors.newFixedThreadPool(count);
        threads = new boolean[count];
        int lastIndex = 0;
        for (int i = 0; i < count; i++) {
            final int nextIndexFinal = i == count - 1 ? filesToSend.size() : lastIndex + filesToSend.size() / count;
            final int lastIndexFinal = lastIndex;
            final int threadIndex = i;
            executor.execute(new Thread(() -> {
                final List<File> subList = filesToSend.subList(lastIndexFinal, nextIndexFinal);
                int lastFileIndex = subList.size() - 1;
                while (lastFileIndex >= 0) {
                    lastFileIndex = Pack(subList, lastFileIndex, threadIndex);
                }
                System.out.println("Queuer (" + hashCode() + ") done ");
                threads[threadIndex] = true;
            }));
            lastIndex = nextIndexFinal;
        }
        executor.execute(new Thread(() -> {
            while (!areThreadsDone(threads)) {
                Upload();
            }
            launched = false;
            System.out.println("Uploader (" + hashCode() + ") done ");
        }));
        launched = true;
    }

    public File getImageToSend() {
        return imageToSend;
    }

    public void setImageToSend(File fileToSend) {
        this.imageToSend = fileToSend;
    }

    public File getDestination() {
        return destination;
    }

    public boolean isLaunched() {
        return launched;
    }

    private synchronized int Pack(List<File> filesToSend, int lastFileIndex, int threadIndex) {
        while (ThreadHelper.fileEnqueued) {
            try {
                System.out.println("Queuer [" + threadIndex + "] waiting " + ThreadHelper.fileEnqueued);
                wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Queuer [" + threadIndex + "] in, lastIndex = " + lastFileIndex);
        File imageToupload = filesToSend.get(lastFileIndex--);
        setImageToSend(imageToupload);
        ThreadHelper.filesCopied--;
        ThreadHelper.fileEnqueued = true;
        notifyAll();
        return lastFileIndex;
    }

    private final File destination;

    private synchronized void Upload() {
        while (!ThreadHelper.fileEnqueued) {
            try {
                System.out.println("Uploader waiting " + ThreadHelper.fileEnqueued);
                wait();
            } catch (InterruptedException e) {
            }
        }
        ThreadHelper.fileEnqueued = true;
        System.out.println("Uploader in");
        try {
            Files.copy(Paths.get(getImageToSend().getAbsolutePath()), Paths.get(destination.getAbsolutePath(), "/" + getImageToSend().getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Uploader copied");
        ThreadHelper.fileEnqueued = false;
        imageToSend = null;
        notifyAll();
    }

    private boolean areThreadsDone(boolean[] threads) {
//        for (boolean thread : threads) {
//            if (!thread) {
//                return false;
//            }
//        }
        for (int i = 0; i < threads.length; i++) {
            System.out.println("Queuer [" + i + "] " + (threads[i] ? "done" : "working"));
            if (!threads[i]) {
                return false;
            }
        }
        return true;
    }
}
