
package frc.robot.Test;

public class DefultMotor {

    public final int i;
    public final int x;


    public DefultMotor(int i, int x) {
        this.i = i;
        this.x = x;
    }

    public double clacSpeed() {
        return i*x/2*x*i/4;
    }

}
