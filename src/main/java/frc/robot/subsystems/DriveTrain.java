/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.Console;

import javax.naming.PartialResultException;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.*;
import edu.wpi.first.util.sendable.SendableBuilder.BackendKind;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveTrain extends SubsystemBase {
  private final Joystick joystickL;
  private final Joystick joystickR;

  private final double W = 27;
  private final double L = 32;

  private int x = 0;
  /**
   * Creates a new ExampleSubsystem.
   */
  public DriveTrain(int joystickL, int joystickR) {
    //System.out.println("Drive Train Constructor");
    this.joystickL = new Joystick(joystickL);
    this.joystickR = new Joystick(joystickR);
  }
  //Registers the PIDCommands for the wheel drive as the default DriveTrain command
  public void regDefCommand(){
    ParallelCommandGroupNoEnd parallelCommandGroup = new ParallelCommandGroupNoEnd(RobotContainer.getInstance().backRight.pid, RobotContainer.getInstance().backLeft.pid, RobotContainer.getInstance().frontRight.pid, RobotContainer.getInstance().frontLeft.pid);
    parallelCommandGroup.addRequirements(this);
    setDefaultCommand(parallelCommandGroup);
  }
  

  @Override
  public void periodic() {    
    drive(joystickL.getRawAxis(0),joystickL.getRawAxis(1),joystickR.getRawAxis(0));
    //System.out.println(joystickR.getRawAxis(0));
    //LOG encoder output
    
    /*
    x++;
    if(x%50 == 0){
      System.out.println("Back Right Swerve encoder out: " + RobotContainer.getInstance().backRight.getEncoderOut() + 
                       "\nBack Left Swerve encoder out: " + RobotContainer.getInstance().backLeft.getEncoderOut() +
                       "\nFront Right Swerve encoder out: " + RobotContainer.getInstance().frontRight.getEncoderOut() +
                       "\nFront Left Swerve encoder out: " + RobotContainer.getInstance().frontLeft.getEncoderOut());               
    }
    */
    if(Math.abs(RobotContainer.getInstance().backLeft.getSetpoint() - RobotContainer.getInstance().backLeft.getEncoderOut()) > 15){
      //System.out.println(RobotContainer.getInstance().backLeft.getSetpoint() - RobotContainer.getInstance().backLeft.getEncoderOut());
    }
  }

  public void drive (double x1, double y1, double x2) {
    double r = Math.sqrt ((L * L) + (W * W));
    y1 = -y1;
    x1 = -x1;
    x2 = -x2;

    double a = x1 - x2 * (L / r);
    double b = x1 + x2 * (L / r);
    double c = y1 - x2 * (W / r);
    double d = y1 + x2 * (W / r);

    double backRightSpeed = Math.sqrt ((a * a) + (d * d));
    double backLeftSpeed = Math.sqrt ((a * a) + (c * c));
    double frontRightSpeed = Math.sqrt ((b * b) + (d * d));
    double frontLeftSpeed = Math.sqrt ((b * b) + (c * c));

    double backRightAngle = (Math.atan2 (a, d) / Math.PI * 180) + 180;
    double backLeftAngle = (Math.atan2 (a, c) / Math.PI * 180) + 180;
    double frontRightAngle = (Math.atan2 (b, d) / Math.PI * 180) + 180;
    double frontLeftAngle = (Math.atan2 (b, c) / Math.PI * 180) + 180;

    //System.out.println("0 1 8: "+ backRightAngle + "4 5 10: " + frontRightAngle + "6 7 11: " + frontLeftAngle);
    
    RobotContainer.getInstance().backRight.drive (backRightSpeed, backRightAngle);
    RobotContainer.getInstance().backLeft.drive (backLeftSpeed, backLeftAngle);
    RobotContainer.getInstance().frontRight.drive (frontRightSpeed, frontRightAngle);
    RobotContainer.getInstance().frontLeft.drive (frontLeftSpeed, frontLeftAngle);
  }
  double lerp (double a, double b, double c){
    return (b * c) + (a* (1-c));
  }
}
