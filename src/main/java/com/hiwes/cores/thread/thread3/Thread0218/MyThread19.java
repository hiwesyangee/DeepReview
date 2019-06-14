package com.hiwes.cores.thread.thread3.Thread0218;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 管道字节流:
 * PipedInputStream和PipedOutputStream。
 */
public class MyThread19 extends Thread {
    private WriteData write;
    private PipedOutputStream out;

    public MyThread19(WriteData write, PipedOutputStream out) {
        super();
        this.write = write;
        this.out = out;
    }

    @Override
    public void run() {
        write.writeMethod(out);
    }
}


class MyThread19_2 extends Thread {
    private ReadData read;
    private PipedInputStream input;

    public MyThread19_2(ReadData read, PipedInputStream input) {
        super();
        this.read = read;
        this.input = input;
    }

    @Override
    public void run() {
        read.readMethod(input);
    }
}

class WriteData {
    public void writeMethod(PipedOutputStream out) {
        try {
            System.out.println("Write:");
            for (int i = 0; i < 300; i++) {
                String outData = "" + (i + 1);
                out.write(outData.getBytes());
                System.out.print(outData);
            }
            System.out.println("-----");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReadData {
    public void readMethod(PipedInputStream input) {
        try {
            System.out.println("Read:");
            byte[] byteArray = new byte[20];
            int readLength = input.read(byteArray);
            while (readLength != -1) {
                String newData = new String(byteArray, 0, readLength);
                System.out.print(newData);
                readLength = input.read(byteArray);
            }
            System.out.println("-----");
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Run19 {
    public static void main(String[] args) {
        try {
            WriteData writeData = new WriteData();
            ReadData readData = new ReadData();

            PipedInputStream inputStream = new PipedInputStream();
            PipedOutputStream outputStream = new PipedOutputStream();

            // inputStream.connect(outputStream);
            outputStream.connect(inputStream); // 产生线程间的数据链接

            MyThread19_2 threadRead = new MyThread19_2(readData, inputStream);
            threadRead.start();

            Thread.sleep(5000);

            MyThread19 threadWrite = new MyThread19(writeData, outputStream);
            threadWrite.start();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}