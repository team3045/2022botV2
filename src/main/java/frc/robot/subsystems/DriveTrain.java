/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.Console;

import javax.lang.model.util.ElementScanner6;
import javax.naming.PartialResultException;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.time.StopWatch;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.*;
import edu.wpi.first.util.sendable.SendableBuilder.BackendKind;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.enums.*;

public class DriveTrain extends SubsystemBase {
  private final Joystick joystickL;
  private final Joystick joystickR;

  private final double InputAdjustmentAngle = Math.PI/4;

  private final double XXCompMult;
  private final double XYCompMult;
  private final double YXCompMult;
  private final double YYCompMult;

  private final double W = 27;
  private final double L = 32;

  private int x = 0;
  /**
   * Creates a new ExampleSubsystem.
   */
  public DriveTrain(int joystickL, int joystickR) {
    this.joystickL = new Joystick(joystickL);
    this.joystickR = new Joystick(joystickR);

    XXCompMult = Math.cos(InputAdjustmentAngle);
    XYCompMult = Math.sin(InputAdjustmentAngle);

    YXCompMult = -Math.sin(InputAdjustmentAngle);
    YYCompMult = Math.cos(InputAdjustmentAngle);
  }
  //RegDefCommand was here
  

  @Override
  public void periodic() {    
    switch (RobotContainer.DRIVE_MODE){
      case PRE_ENABLE:
        drive(0, 0, 0);
        break;
      case AUTON_START:
        drive(0, -0.5, 0);
        break;
      case TELEOP_DRIVE:
        drive(joystickL.getRawAxis(0),joystickL.getRawAxis(1),joystickR.getRawAxis(0));
        break;
      case AUTON_DRIVE:
        drive(0,0,0);
        break;
      case TELEOP_AIM:
        drive(0, 0, RobotContainer.getInstance().LimelightVision.getRotSpeed());
        break;
      case AUTON_AIM:
        drive(0, 0, RobotContainer.getInstance().LimelightVision.getRotSpeed());
        break;
    }
  }

  public void drive (double x1, double y1, double x2) {
    double r = Math.sqrt ((L * L) + (W * W));

    y1 = -y1;
    x2 = -x2;

    //double tempX = x1 * XXCompMult + y1 * YXCompMult;
    //double tempY = x1 * XYCompMult + y1 * YYCompMult;

    //x1 = tempX;
    //y1 = tempY;

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
    if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_DRIVE && Math.abs(x1) + Math.abs(y1) + Math.abs(x2) < 0.1){
      //Default angle 0 instead of 100
      backRightAngle = 0;
      backLeftAngle = 0;
      frontRightAngle = 0;
      frontLeftAngle = 0;
    }
    //System.out.println(RobotContainer.getInstance().backRight.getEncoderOut());
    //System.out.println(RobotContainer.getInstance().backRight.get40ErrorClampedSetpoint());

    RobotContainer.getInstance().backRight.drive (backRightSpeed, backRightAngle);
    RobotContainer.getInstance().backLeft.drive (backLeftSpeed, backLeftAngle);
    RobotContainer.getInstance().frontRight.drive (frontRightSpeed, frontRightAngle);
    RobotContainer.getInstance().frontLeft.drive (frontLeftSpeed, frontLeftAngle);
  }
}
