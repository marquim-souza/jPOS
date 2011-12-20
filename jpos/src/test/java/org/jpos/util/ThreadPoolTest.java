package org.jpos.util;

import org.jpos.iso.ISOUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.internal.matchers.Matches;

public class ThreadPoolTest {

    class TestTask implements Runnable {
      public void run() {
        ISOUtil.sleep(500);
      }
    }

    @Test
    public void testConstructor() throws Throwable {
        ThreadPool threadPool = new ThreadPool(-1, 1);
        assertEquals("threadPool.getJobCount()", 0, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 1, threadPool.getMaxPoolSize());
    }

    @Test
    public void testConstructor1() throws Throwable {
        ThreadPool threadPool = new ThreadPool(-2, -1);
        assertEquals("threadPool.getJobCount()", 0, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 100, threadPool.getMaxPoolSize());
    }

    @Test
    public void testConstructor2() throws Throwable {
        ThreadPool threadPool = new ThreadPool(0, 100);
        assertEquals("threadPool.getJobCount()", 0, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 100, threadPool.getMaxPoolSize());
    }

    @Test
    public void testConstructor3() throws Throwable {
        ThreadPool threadPool = new ThreadPool(1, 0);
        assertEquals("threadPool.getJobCount()", 0, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 100, threadPool.getMaxPoolSize());
    }

    @Test
    public void testConstructor4() throws Throwable {
        ThreadPool threadPool = new ThreadPool(2, 100);
        assertEquals("threadPool.getJobCount()", 0, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 2, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 100, threadPool.getMaxPoolSize());
    }

    @Test
    public void testRun1() throws Throwable {
        ThreadPool threadPool = new ThreadPool(1, 100);
        threadPool.execute(new TestTask());
        ISOUtil.sleep(50);
        assertEquals("threadPool.getJobCount()", 1, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 100, threadPool.getMaxPoolSize());
        ISOUtil.sleep(500);
        assertEquals("threadPool.getJobCount()", 1, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
    }

    @Test
    public void testRun2() throws Throwable {
        ThreadPool threadPool = new ThreadPool(1, 1);
        threadPool.execute(new TestTask());
        threadPool.execute(new TestTask());
        ISOUtil.sleep(50);
        assertEquals("threadPool.getJobCount()", 2, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 1, threadPool.getMaxPoolSize());
        assertEquals("threadPool.getPendingCount()", 1, threadPool.getPendingCount());
        ISOUtil.sleep(500);
        assertEquals("threadPool.getJobCount()", 2, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getPendingCount()", 0, threadPool.getPendingCount());
        assertEquals("threadPool.getMaxPoolSize()", 1, threadPool.getMaxPoolSize());
        ISOUtil.sleep(550);
        assertEquals("threadPool.getJobCount()", 2, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 1, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 1, threadPool.getMaxPoolSize());
    }

    @Test
    public void testRun3() throws Throwable {
        ThreadPool threadPool = new ThreadPool(1, 2);
        threadPool.execute(new TestTask());
        ISOUtil.sleep(20);
        threadPool.execute(new TestTask());
        ISOUtil.sleep(20);
        assertEquals("threadPool.getJobCount()", 2, threadPool.getJobCount());
        assertEquals("threadPool.getPoolSize()", 2, threadPool.getPoolSize());
        assertEquals("threadPool.getMaxPoolSize()", 2, threadPool.getMaxPoolSize());
    }

    @Test
    public void testRunNames() throws Throwable {
        ThreadPool threadPool = new ThreadPool(1, 2);
        threadPool.execute(new TestTask());
        ISOUtil.sleep(20);
        threadPool.execute(new TestTask());
        ISOUtil.sleep(20);
        Thread[] tl = new Thread[threadPool.activeCount()];
        threadPool.enumerate(tl);
        for (Thread t :tl )
          assertThat(t.getName(), new Matches("PooledThread-\\d+-(running|idle)"));
    }

}