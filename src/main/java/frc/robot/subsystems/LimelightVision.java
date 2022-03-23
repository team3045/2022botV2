/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.lang.Math;
import java.lang.Double;
import frc.robot.enums.*;

public class LimelightVision extends SubsystemBase {
  private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private final NetworkTableEntry tx = table.getEntry("tx");
  private final NetworkTableEntry ty = table.getEntry("ty");
  private final NetworkTableEntry ta = table.getEntry("ta");
  private final NetworkTableEntry tv = table.getEntry("tv");

  private final NetworkTableEntry pipeline = table.getEntry("pipeline");

  private double x;
  private double y;
  private double area;

  private boolean v;
  private boolean aiming;

  public boolean getAiming(){
    return aiming;
  }
  public void setAiming(boolean value){
    aiming = value;
    if(value){
      if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_DRIVE)
        RobotContainer.DRIVE_MODE = DRIVE_MODE.TELEOP_AIM;
      if(RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_DRIVE)
        RobotContainer.DRIVE_MODE = DRIVE_MODE.AUTON_AIM;

      pipeline.setDouble(0);
    } else {
      if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_AIM)
        RobotContainer.DRIVE_MODE = DRIVE_MODE.TELEOP_DRIVE;
      if(RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_AIM)
        RobotContainer.DRIVE_MODE = DRIVE_MODE.AUTON_DRIVE;

      pipeline.setDouble(Constants.colorPipeline);
    }
  }
  public double x1Auto(){
    return Math.sin(x) / (Constants.ballSizeMult * area);
  }
  public double y1Auto(){
    return Math.cos(x);
  }
  public LimelightVision() {
    setAiming(true);
  }

  @Override
  public void periodic() {
    if(RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_AIM || RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_DRIVE || RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_DRIVE){
      findVals();
    }
  }
  void findVals(){
    //read values
    double vDouble = tv.getDouble(0.0);
    v = vDouble == 1.0;

    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    area = ta.getDouble(0.0);

    //post to smart dashboard
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
  }
  public Double getGoalHorizontalDistance(){
    if(!aiming){
      return null;
    }
    if(v)
      return Constants.goalRadius + (Constants.limeLightGoalVerticalOffset / Math.tan(Math.toRadians(y + Constants.limeLightAngleUp)));
    else
      return null;
  }
  public boolean getV(){
    return v;
  }
  public double getRotSpeed(){
    if(Math.abs(x) > Constants.aimTolerance)
      return -MathUtil.clamp(x * Constants.aimSpeed, -Constants.maxAimRotSpeed, Constants.maxAimRotSpeed);
    else
      return 0.0;
  }
  public boolean autonShouldShoot(){
    return Math.abs(x) < Constants.aimTolerance;
  }
}
