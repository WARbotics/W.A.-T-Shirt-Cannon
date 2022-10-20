package frc.robot.components;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.common.PID;


/*
*The Drivetrain controls the motors on the left and on the right. 
*It also gives use values so we are able to make fully functioning autonomous.
*/

public class Drivetrain {

    private MotorControllerGroup left, right;
    public DifferentialDrive drive;

    // private AHRS navx; 
    

    private double deadBand = 0.0;
    private PID PID = new PID(0.30, 0.00, 0.01);
    private double speed = 0;
    private double rotation = 0; 

/*
*This instantiates the leftFront and other motors as CANSparkMax
*/

    public Drivetrain(PWMSparkMax leftFront,PWMSparkMax leftRear,PWMSparkMax rightFront,  PWMSparkMax rightRear) {

        // Initializes controller groups for left and right sides
        this.left = new MotorControllerGroup(leftFront, leftRear);
        this.right = new MotorControllerGroup(rightFront, rightRear);
        
        // Initializes the differential drive controller
        this.drive = new DifferentialDrive(left, right);
    }
    /**
    * Sets the deadband for the drivetrain 
    *
    * @param deadBand the amount of deadband that will be applied to the drivetrain 
    */
    public void setDeadBand(double deadBand) {
        this.deadBand = deadBand;
    }
    /**
    * Gets the speed for the drivetrain 
    *
    */
    public double getSpeed() {
        return speed;
    }
/*
*gets the rotation for the drivetrain
*/
    public double getRotation() {
        return rotation;
    }
/*
*drives the robot in a curve
*/
    public void curveDrive(double speed, double rotation, boolean isQuickTurn) {
        if (Math.abs(speed) <= this.deadBand) {
            speed = 0;
        }
        if (Math.abs(rotation) <= this.deadBand) {
            rotation = 0;
        }
        this.speed = speed;
        this.rotation = rotation;
        PID.setActual(this.speed);
        drive.curvatureDrive(this.speed, this.rotation, isQuickTurn);
    }
/*
*runs tank drive via voltage
*/
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        left.setVoltage(leftVolts);
        right.setVoltage(-rightVolts);
        drive.feed();
      }

/*
*this gets average encoder distance
*/     
    public void tankDriveCustom(double leftSpeed, double rightSpeed){
        left.set(leftSpeed);
        right.set(-rightSpeed);
        drive.feed();
    }

    public void update(){}

}