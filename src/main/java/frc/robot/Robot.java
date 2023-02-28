// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.cameraserver.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  //Motor Declaration
  private Talon Motor3BackRight;
  private Talon Motor2FrontRight;
  private Talon Motor1BackLeft;
  private Talon Motor0FrontLeft;
  private Talon Motor4Pully;


  //variables for drive motors
  double leftMotors;
  double rightMotors;

  // joystick for control
  private Joystick joystick;

  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    //init of motors ([MotorName] = new Talon([Port]);)
    Motor0FrontLeft = new Talon(0); //left drive motor
    Motor1BackLeft = new Talon(1); //right drive motor
    Motor2FrontRight = new Talon(2); //left drive motor (back left)
    Motor3BackRight = new Talon(3); //right drive motor
    Motor4Pully = new Talon(4); // pully system drive motor.

    //init of controllers/joysticks ([Name] = new [Type]([USBPort]))
    joystick = new Joystick(0);

    // CameraServer.startAutomaticCapture(0);
    //CameraServer.startAutomaticCapture(1);
  }

  double joystickXAxisPos;
  double joystickYAxisPos;
  double joystickZAxisPos;
  double joystickSpeedAxisPos;

  // drive motor control.
  double deadzoneLimit = 0.4; // sets the value that the joystick position must exceed for movement of the robot to occur.

  double speed = 0;

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();

    joystickXAxisPos = joystick.getRawAxis(0); //x axis on joystick
    joystickYAxisPos = joystick.getRawAxis(1); //y axis on joystick
    joystickZAxisPos = joystick.getRawAxis(2); //z axis on joystick
    joystickSpeedAxisPos = joystick.getRawAxis(3); //y axis on joystick

    // prints the current position of 
    System.out.println("x-pos: " + joystickXAxisPos);
    System.out.println("y-pos: " + joystickYAxisPos);

    // deadzones.
    if (Math.abs(joystickXAxisPos) < deadzoneLimit || Math.abs(joystickXAxisPos) < deadzoneLimit*-1 ) {
      System.out.println(joystickXAxisPos);
      joystickXAxisPos = 0; // sets value of joystickXAxisPos to 0 if joystick value is under the deadzoneLimit.
    } // end if.

    if (Math.abs(joystickYAxisPos) < deadzoneLimit || Math.abs(joystickYAxisPos) < deadzoneLimit*-1) {
      System.out.println(joystickYAxisPos);
      joystickYAxisPos = 0; // sets value of joystickYAxisPos to 0 if joystick value is under the deadzoneLimit.
    } // end if.

    speed = ((((joystickSpeedAxisPos-1))*-7.5)-15)*-1; // sets the speed of the robot by dividing the motor speed by the speed value. The math done here is awful. Enjoy.

    System.out.println("Joystick pos: " + joystickSpeedAxisPos + "\n Speed: " + speed);

    // setting speed of left and right motors.
    leftMotors = (joystickYAxisPos+joystickXAxisPos+joystickZAxisPos+joystickSpeedAxisPos)/200; // left motors are postive values. The value is then divided to set speed.
    rightMotors = (joystickYAxisPos-joystickXAxisPos-joystickZAxisPos-joystickSpeedAxisPos)/200; // right motors are negative values. The value is then divided to set speed.

    // motors are inverted depending on the side. To move forward, left motors must be positive values, while right side motors must be set to negative values, or vice-versa.
    Motor1BackLeft.set(leftMotors); // setting speed of back left drive motor.
    Motor3BackRight.set(rightMotors*-1); // setting speed of back right motor.
    Motor0FrontLeft.set(leftMotors); // setting speed of front left drive motor.
    Motor2FrontRight.set(rightMotors*-1); // setting speed of front right motor.

    Motor4Pully.set(0.02);
                    
    // no clue what this does.
    SmartDashboard.putNumber("left", rightMotors);
    SmartDashboard.putNumber("right", rightMotors);
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
