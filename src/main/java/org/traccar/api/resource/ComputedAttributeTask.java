package org.traccar.api.resource;

import org.traccar.handler.BasePositionHandler;
import org.traccar.handler.ComputedAttributesHandler;
import org.traccar.model.Position;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ComputedAttributeTask implements Runnable {
    private final BlockingQueue<Position> queue;
    private final ComputedAttributesHandler handler;

    public ComputedAttributeTask(ComputedAttributesHandler handler) {
        this.queue = new LinkedBlockingQueue<>();
        this.handler = handler;
    }

    public void addPosition(Position position) {
        try {
            queue.put(position);
        } catch (InterruptedException e) {
            // Handle interruption gracefully
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        System.out.println("I'm running in a slave thread!");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Position position = queue.take();
                handler.handlePosition2(position, result -> {
                });
            } catch (InterruptedException e) {
                // Handle interruption gracefully
                Thread.currentThread().interrupt();
            }
        }
    }
}
