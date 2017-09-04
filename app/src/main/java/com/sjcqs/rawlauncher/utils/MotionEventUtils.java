package com.sjcqs.rawlauncher.utils;

import android.graphics.Point;
import android.view.MotionEvent;


/**
 * Created by satyan on 9/3/17.
 */

public class MotionEventUtils {

    public final static int DISTANCE_THRESHOLD = 50;
    private static final String TAG = MotionEventUtils.class.getName();

    public static Direction getDirection(Point from, Point to) {
        Point d = new Point(from.x - to.x, from.y - to.y);
        boolean swipeX = false, swipeY = false;

        if (Math.abs(d.x) > DISTANCE_THRESHOLD) {
            swipeX = true;
        }
        if (Math.abs(d.y) > DISTANCE_THRESHOLD) {
            swipeY = true;
        }

        if (swipeX && !swipeY) {
            if (d.x > 0) {
                return Direction.WEST.setDistance(d.x);
            } else return Direction.EST.setDistance(-d.x);
        } else if (!swipeX) {
            if (d.y > 0) {
                return Direction.NORTH.setDistance(d.y);
            } else {
                return Direction.SOUTH.setDistance(-d.y);
            }
        }

        return Direction.UNKNOWN;
    }

    public static Direction getDirection(MotionEvent fromEvent, MotionEvent toEvent) {
        Point from = new Point((int) fromEvent.getRawX(), (int) fromEvent.getRawY());
        Point to = new Point((int) toEvent.getRawX(), (int) toEvent.getRawY());
        return getDirection(from, to);
    }

    public enum Direction {
        NORTH,
        SOUTH,
        EST,
        WEST,
        UNKNOWN;

        private int distance = 0;

        public int getDistance() {
            return distance;
        }

        public Direction setDistance(int distance) {
            this.distance = distance;
            return this;
        }
    }
}
