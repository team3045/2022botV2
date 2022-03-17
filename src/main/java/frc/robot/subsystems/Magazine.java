/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Magazine extends SubsystemBase {
  private final TalonSRX uptakeMotor;
  private final TalonSRX magazineMotor;

  public Magazine(int uptake, int magazine) {
    uptakeMotor = new TalonSRX(uptake);
    magazineMotor = new TalonSRX(magazine);
  }

  @Override
  public void periodic() {
    if(RobotContainer.getInstance().buttonBoard.getRawButton(Constants.magButton))
      magazineMotor.set(ControlMode.PercentOutput, Constants.magSpeed + 
                                                  (RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.reverseButton) ? -Constants.magSpeed : 0));
    else
      magazineMotor.set(ControlMode.PercentOutput, (RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.reverseButton) ? -Constants.magSpeed : 0));
    if(RobotContainer.getInstance().buttonBoard.getRawButton(Constants.uptakeButton))
      uptakeMotor.set(ControlMode.PercentOutput, Constants.uptakeSpeed + 
                                                (RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.reverseButton) || 
                                                 RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.uptakeReverseButton)
                                                 ? -Constants.uptakeSpeed : 0));
    else
      uptakeMotor.set(ControlMode.PercentOutput, (RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.reverseButton) || 
                                                  RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.uptakeReverseButton) 
                                                  ? -Constants.uptakeSpeed : 0));
  }
}
