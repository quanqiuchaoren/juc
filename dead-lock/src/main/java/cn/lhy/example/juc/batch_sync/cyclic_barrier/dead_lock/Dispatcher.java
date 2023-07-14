package cn.lhy.example.juc.batch_sync.cyclic_barrier.dead_lock;

import java.util.HashSet;
import java.util.Set;

class Dispatcher {
    private final Set<Taxi> taxis;
    private final Set<Taxi> availableTaxis;

    public Dispatcher() {
        taxis = new HashSet<Taxi>();
        availableTaxis = new HashSet<Taxi>();
    }

    public synchronized void notifyAvailable(Taxi taxi) {
        availableTaxis.add(taxi);
    }

    // 调用getImage()需要Dispatcher内置锁
    public synchronized Image getImage() {
        Image image = new Image();
        for (Taxi t : availableTaxis)
            // 调用getLocation()需要Taxi内置锁
            image.drawMarker(t.getLocation());
        return image;
    }
}