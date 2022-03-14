/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Constants;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

public class WheelDrivePID extends PIDCommand {
  public WheelDrivePID (WheelDrive wheel) {
    super(new PIDController (Constants.WheelPIDP, Constants.WheelPIDI, Constants.WheelPIDD, 0.001), 
          wheel::PIDEncOut,
          wheel::PIDSetpoint,
          wheel::runAngleMotor); 

    getController().enableContinuousInput(0, 180);
    //addRequirements(RobotContainer.getInstance().getDRinstance());
  }
  @Override
  public boolean isFinished() {
    return false;
  }
}