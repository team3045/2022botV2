/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.time.StopWatch;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.enums.*;

public class Magazine extends SubsystemBase {
  private final StopWatch shootTime;

  private final CANSparkMax uptakeMotor;
  private final CANSparkMax magazineMotor;

  public Magazine(int uptake, int magazine) {
    shootTime=new StopWatch();

    uptakeMotor = new CANSparkMax(uptake, MotorType.kBrushless);
    magazineMotor = new CANSparkMax(magazine, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_AIM || RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_DRIVE){
     if(RobotContainer.getInstance().buttonBoard.getRawButton(Constants.magButton))
        magazineMotor.set(-(Constants.magSpeed + 
                           (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.reverseButton) ? -Constants.magSpeed : 0)));
      else
        magazineMotor.set(-((RobotContainer.getInstance().buttonBoard.getRawButton(Constants.reverseButton) ? -Constants.magSpeed : 0)));
      if(RobotContainer.getInstance().buttonBoard.getRawButton(Constants.uptakeButton)){
        uptakeMotor.set(-(Constants.uptakeSpeed + 
                         (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.reverseButton) || 
                          RobotContainer.getInstance().buttonBoard.getRawButton(Constants.uptakeReverseButton)
                          ? -Constants.uptakeSpeed : 0)));
        magazineMotor.set(-(Constants.magSpeed + 
                           (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.reverseButton) ? -Constants.magSpeed : 0)));
     }
     else
        uptakeMotor.set(-((RobotContainer.getInstance().buttonBoard.getRawButton(Constants.reverseButton) || 
                           RobotContainer.getInstance().buttonBoard.getRawButton(Constants.uptakeReverseButton) 
                           ? -Constants.uptakeSpeed : 0)));
    } else if (RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_AIM){
      if(shootTime.getDuration() > Constants.revTime && shootTime.getDuration() < Constants.revTime + Constants.uptakeTime){
        uptakeMotor.set(-Constants.uptakeSpeed);
        magazineMotor.set(-Constants.magSpeed);
      } else if (shootTime.getDuration() > Constants.revTime + Constants.uptakeButton){
        RobotContainer.DRIVE_MODE = DRIVE_MODE.AUTON_DRIVE;
      }
    }
  }
}
