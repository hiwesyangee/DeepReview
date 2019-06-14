package com.hiwes.cores.thread.thread3.Thread0218;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * 管道字符流:
 * PipedReader和PipedWriter。
 */
public class MyThread20 extends Thread {
    private WriteData2 write;
    private PipedWriter out;

    public MyThread20(WriteData2 write, PipedWriter out) {
        super();
        this.write = write;
        this.out = out;
    }

    @Override
    public void run() {
        write.writeMethod(out);
    }

}


class MyThread20_2 extends Thread {
    private ReadData2 read;
    private PipedReader input;

    public MyThread20_2(ReadData2 read, PipedReader input) {
        super();
        this.read = read;
        this.input = input;
    }

    @Override
    public void run() {
        read.readMethod(input);
    }

}


class WriteData2 {
    public void writeMethod(PipedWriter out) {
        try {
            System.out.println("Write:");
            for (int i = 0; i < 300; i++) {
                String outData = "" + (i + 1);
                out.write(outData);
                System.out.print(outData);
            }
            System.out.println();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ReadData2 {
    public void readMethod(PipedReader input) {
        try {
            System.out.println("Read:");
            char[] byteArray = new char[20];
            int readLength = input.read(byteArray);
            while (readLength != -1) {
                String newData = new String(byteArray, 0, readLength);
                System.out.print(newData);
                readLength = input.read(byteArray);
            }
            System.out.println();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Run20 {
    public static void main(String[] args) {
        try {
            WriteData2 write = new WriteData2();
            ReadData2 read = new ReadData2();

            PipedReader inputStream = new PipedReader();
            PipedWriter outputStream = new PipedWriter();

            // inputStream.connect(outputStream);
            outputStream.connect(inputStream);

            MyThread20_2 threadRead = new MyThread20_2(read,inputStream);
            threadRead.start();

            Thread.sleep(5000);

            MyThread20 threadWrite = new MyThread20(write,outputStream);
            threadWrite.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}