package top.meethigher.logmonitor;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

public class TailFile extends Thread {

    private boolean debug = false;

    private int sleepTime;
    private long lastFilePosition = 0;
    private boolean shouldIRun = true;
    private File crunchifyFile = null;
    private static int crunchifyCounter = 0;

    Consumer<String> onMessage;

    public TailFile(File myFile, int myInterval, Consumer<String> onMessage) {
        crunchifyFile = myFile;
        this.sleepTime = myInterval;
        this.onMessage = onMessage;
    }

    private void printLine(String message) {
        if (onMessage != null) {
            try {
                message = new String(message.getBytes("ISO-8859-1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            onMessage.accept(message);
        }
    }

    public void stopRunning() {
        shouldIRun = false;
    }

    public void run() {
        try {
            while (shouldIRun) {
                Thread.sleep(sleepTime);
                long fileLength = crunchifyFile.length();
                if (fileLength > lastFilePosition) {

                    // Reading and writing file
                    RandomAccessFile accessFile = new RandomAccessFile(crunchifyFile, "r");
                    accessFile.seek(lastFilePosition);
                    String crunchifyLine = null;
                    while ((crunchifyLine = accessFile.readLine()) != null) {
                        this.printLine(crunchifyLine);
                        crunchifyCounter++;
                    }
                    lastFilePosition = accessFile.getFilePointer();
                    accessFile.close();
                } else {
                    if (debug)
                        this.printLine("Hmm.. Couldn't found new line after line # " + crunchifyCounter);
                }
            }
        } catch (Exception e) {
            stopRunning();
        }
        if (debug)
            this.printLine("Exit the program...");
    }




}
