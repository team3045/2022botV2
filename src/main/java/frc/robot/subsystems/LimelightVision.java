/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.lang.Math;
import java.lang.Double;

public class LimelightVision extends SubsystemBase {
  private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  private final NetworkTableEntry tx = table.getEntry("tx");
  private final NetworkTableEntry ty = table.getEntry("ty");
  private final NetworkTableEntry ta = table.getEntry("ta");
  private final NetworkTableEntry tv = table.getEntry("tv");

  private final NetworkTableEntry camMode = table.getEntry("camMode");
  private final NetworkTableEntry ledMode = table.getEntry("ledMode");

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
      camMode.setDouble(0);
      ledMode.setDouble(3);
    } else {
      camMode.setDouble(1);
      ledMode.setDouble(0);
    }
  }

  public LimelightVision() {
    setAiming(true);
  }

  @Override
  public void periodic() {
    if(aiming){
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
}
