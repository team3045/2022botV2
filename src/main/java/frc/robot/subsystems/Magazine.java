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

  private final CANSparkMax magazineMotor;

  public Magazine(int magazine) {
    shootTime=new StopWatch();

    magazineMotor = new CANSparkMax(magazine, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    
    if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_AIM || RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_DRIVE){
      magazineMotor.set((RobotContainer.getInstance().buttonBoard.getRawButton(Constants.magButton) ? Constants.magSpeed : 0) + (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.uptakeButton) ? Constants.uptakeSpeed : 0));
      if(RobotContainer.getInstance().buttonBoard.getRawButton(Constants.reverseButton)){
        magazineMotor.set(-1 * Constants.magSpeed);
      }
    } else if (RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_AIM){
      if(shootTime.getDuration() > Constants.revTime && shootTime.getDuration() < Constants.revTime + Constants.uptakeTime){
        //uptakeMotor.set(-Constants.uptakeSpeed);
        magazineMotor.set(Constants.magSpeed);
      } else if (shootTime.getDuration() > Constants.revTime + Constants.uptakeButton){
        RobotContainer.DRIVE_MODE = DRIVE_MODE.AUTON_DRIVE;
      }
    } else if (RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_SHOOT && RobotContainer.robot.watch.getDuration() > 3){
      //uptakeMotor.set(-Constants.uptakeSpeed);
      magazineMotor.set(Constants.magSpeed);
    }
  }
}
