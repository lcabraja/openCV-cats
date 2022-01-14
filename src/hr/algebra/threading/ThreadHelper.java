/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threading;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author lcabraja
 */
public class ThreadHelper {

    public static boolean fileEnqueued;
    public static boolean fileCopied;
    public static boolean launched;
    public static int filesCopied;

    private static File imageToSend = null;
    private static List<File> filesToSend = null;
    private static File destination = null;

    public ThreadHelper(List<File> filesToSend, File destination) {
        setFilesToSend(filesToSend);
        setDestination(destination);
    }

    public void launchThreads(int count) {
        if (!hasFiles()) {
            return;
        }
        count = count > filesToSend.size() ? filesToSend.size() : count;
        System.out.println("count " + count);
        ExecutorService executor = Executors.newFixedThreadPool(count);
        int lastIndex = 0;
        for (int i = 0; i < count; i++) {
            int nextIndex = i == count - 1 ? filesToSend.size() : lastIndex + filesToSend.size() / count;
            executor.execute(new FileQueuerThread(filesToSend.subList(lastIndex, nextIndex)));
            lastIndex = nextIndex;
        }
        ExecutorService executor2 = Executors.newFixedThreadPool(1);
        executor.execute(new FileUploaderThread(destination));
        launched = true;
    }

    public static File getImageToSend() {
        return imageToSend;
    }

    public static void setImageToSend(File fileToSend) {
        ThreadHelper.imageToSend = fileToSend;
    }

    public static File getDestination() {
        return destination;
    }

    public static void setDestination(File destination) {
        ThreadHelper.destination = destination;
    }

    public static void setFilesToSend(List<File> files) {
        filesToSend = files;
        filesCopied = files.size();
    }

    public static void removeFile(File file) {
        System.out.println("Removing file " + file.getName() + "... " + filesToSend.size() + " left...");
        filesToSend.remove(file);
    }

    public static boolean hasFiles() {
        return filesToSend != null && filesToSend.size() > 0;
    }

}
