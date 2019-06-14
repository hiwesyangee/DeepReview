package utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HBaseServiceImpl {

    private static Runnable getThread(final int i) {
        return new Runnable() {
            @Override
            public void run() {
                long start2 = System.currentTimeMillis();
                for (int i = 0; i < 1000000; i++) {
                    HBaseUtils.getInstance().bulkput("t1", i + "", "f1", "id", String.valueOf(100321 + i));
                }
                System.out.println(System.currentTimeMillis() - start2);

            }
        };
    }

    public static void main(String args[]) {
        int ncpus = Runtime.getRuntime().availableProcessors();
        double ucpus = 0.50d;  // 期望对CPU的使用率 0 ≤ UCPU ≤ 1
        // W/C 等待时间与计算时间的比率------------线程池大小 = NCPU *UCPU(1+W/C)
        int nThreads = (int) (ncpus * ucpus * (1 + 0.25));
        long start2 = System.currentTimeMillis();
        ExecutorService fixPool = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            fixPool.execute(getThread(i));
        }
        fixPool.shutdown();
        System.out.println(System.currentTimeMillis() - start2);
    }

}
