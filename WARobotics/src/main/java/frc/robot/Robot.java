/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.components.Drivetrain;
import frc.robot.components.OI;
import frc.robot.components.OI.DriveMode;

import java.util.ArrayList;
import java.util.List;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.robot.components.Cannon;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private OI input; 
  private AHRS navX;
  private Drivetrain drive;
  private Cannon cannon;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {


    //Drivetrain
    drive = new Drivetrain(new WPI_TalonSRX(1), new WPI_TalonSRX(2), new WPI_VictorSPX(3), new WPI_VictorSPX(4), navX);

    // Input
    Joystick driveStick = new Joystick(0);
    Joystick operator = new Joystick(1);
    input = new OI(driveStick, operator);

    List<Solenoid> solenoids = new ArrayList<Solenoid>();
    solenoids.add(new Solenoid(PneumaticsModuleType.CTREPCM, 0));
    solenoids.add(new Solenoid(PneumaticsModuleType.CTREPCM, 1));
    solenoids.add(new Solenoid(PneumaticsModuleType.CTREPCM, 2));
    
    cannon = new Cannon(solenoids);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    drive.resetOdomentry(new Pose2d());
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double driveY = -input.driver.getRawAxis(1);
    double zRotation = input.driver.getRawAxis(2);
    double rightDriveY = input.driver.getRawAxis(3);
    SmartDashboard.putString("Drivemode", input.getDriveMode().name()); // What is the current driving mode 
    // Driving Modes logic
    if (input.getDriveMode() == DriveMode.SPEED) {
      drive.drive.arcadeDrive(driveY, zRotation);
      // Speed
    } else if (input.getDriveMode() == DriveMode.PRECISION) {
      // Double check that they are the right controls
      // Precision
      drive.drive.tankDrive(driveY * .70, -rightDriveY * .70);
      // make turning senetive but forward about .50
    } else {
      drive.drive.arcadeDrive(driveY*.70, zRotation*.70);
    }

    // Driving modes
    if (input.driver.getRawButton(1)) {
      // Set Speed Mode
      /*
      *when we press button 1 the robot goes into speed mood
      */
      input.setDriveMode(DriveMode.SPEED);      
    } else if (input.driver.getRawButton(2)) {
      // Precision
      /*
      *when we press button 2 the robot goes into precision mode.
      */
      input.setDriveMode(DriveMode.PRECISION);
    } else if (input.driver.getRawButton(3)) {
      // Default
      /*
      *when we press button 3 the robot goes into a defulat drive mode.
      */
      input.setDriveMode(DriveMode.DEFAULT);
    }

    double axisOne = input.driver.getRawAxis(1);
    double axisTwo = input.driver.getRawAxis(2);
    
    if (axisOne == -1) {
      this.cannon.fireSolenoid(1);
    } else if (axisTwo == 1) {
      this.cannon.fireSolenoid(0);
    } else if (axisTwo == -1) {
      this.cannon.fireSolenoid(2);
    } else if (axisOne == 1) {
      this.cannon.fireAllSolenoids();
    }
    

  }


  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  /**
   * This function is called periodically when disabled.
   */
  @Override
  public void disabledPeriodic() {
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }

}
