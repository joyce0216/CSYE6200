package edu.neu.csye6200.simulation;

public class Boat {
    private Position position;
    private int speed;

    public Boat(Position position, int speed) {
        this.position = position;
        this.speed = speed;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "position=" + position +
                ", speed=" + speed +
                '}';
    }
}
