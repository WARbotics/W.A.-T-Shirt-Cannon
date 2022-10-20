/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.components.Drivetrain;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.components.Cannon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Joystick controller;
  private Drivetrain drive;
  private Cannon cannon;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    // Drivetrain
    this.drive = new Drivetrain(
        new PWMSparkMax(0),
        new PWMSparkMax(1),
        new PWMSparkMax(2),
        new PWMSparkMax(3));

    // Input
    this.controller = new Joystick(0);

    List<Solenoid> solenoids = new ArrayList<Solenoid>();
    solenoids.add(new Solenoid(PneumaticsModuleType.CTREPCM, 0));
    solenoids.add(new Solenoid(PneumaticsModuleType.CTREPCM, 1));
    solenoids.add(new Solenoid(PneumaticsModuleType.CTREPCM, 2));

    this.cannon = new Cannon(solenoids);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
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
    double x = controller.getRawAxis(4);
    double z = controller.getRawAxis(3);

    drive.drive.arcadeDrive(x, z);

    double axisOne = controller.getRawAxis(1);
    double axisTwo = controller.getRawAxis(2);

    // Gets the direction that the joypad is being pushed
    // It returns by axis instead of by button so we check directions via a
    // threshold
    if (axisOne < -0.2) {
      this.cannon.fireSolenoid(1);
    } else if (axisTwo > 0.2) {
      this.cannon.fireSolenoid(0);
    } else if (axisTwo < -0.2) {
      this.cannon.fireSolenoid(2);
    } else if (axisOne > 0.2) {
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
