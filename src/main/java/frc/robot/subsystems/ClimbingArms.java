package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.enums.*;

public class ClimbingArms extends SubsystemBase{
    private final CANSparkMax armMotor1;
    private final CANSparkMax armMotor2;

    public ClimbingArms(int armMotor1, int armMotor2) {
        this.armMotor1 = new CANSparkMax(armMotor1, MotorType.kBrushless);
        this.armMotor2 = new CANSparkMax(armMotor2, MotorType.kBrushless);

        
    }

    @Override 
    public void periodic() {    
        armMotor2.set(-(RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmUpButton) ? Constants.climbingArmSpeed : 0) -
                    (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmDownButton) ? -Constants.climbingArmSpeed : 0));
        armMotor1.set((RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmUpButton) ? Constants.climbingArmSpeed : 0) +
                      (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmDownButton) ? -Constants.climbingArmSpeed : 0));
    }
}