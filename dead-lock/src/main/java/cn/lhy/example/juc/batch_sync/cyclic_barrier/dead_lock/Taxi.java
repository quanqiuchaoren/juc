package cn.lhy.example.juc.batch_sync.cyclic_barrier.dead_lock;

class Taxi {
    private Point location, destination;
    private final Dispatcher dispatcher;

    public Taxi(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public synchronized Point getLocation() {
        return location;
    }

    // setLocation 需要Taxi内置锁
    public synchronized void setLocation(Point location) {
        this.location = location;
        if (location.equals(destination))
            // 调用notifyAvailable()需要Dispatcher内置锁
            dispatcher.notifyAvailable(this);
    }

    public synchronized Point getDestination() {
        return destination;
    }

    public synchronized void setDestination(Point destination) {
        this.destination = destination;
    }
}