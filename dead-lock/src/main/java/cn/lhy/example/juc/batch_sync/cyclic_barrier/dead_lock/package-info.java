/**
 * 协作对象之间可能发生死锁。
 * Dispatcher.getImage()和Taxi.setLocation(Point location)都需要持有自己的对象锁，和获取对方的锁。
 * 并且在操作途中是没有释放锁的
 * 这就是隐式获取两个锁(对象之间协作)..
 * 这种方式也很容易就造成死锁.....
 */
package cn.lhy.example.juc.batch_sync.cyclic_barrier.dead_lock;