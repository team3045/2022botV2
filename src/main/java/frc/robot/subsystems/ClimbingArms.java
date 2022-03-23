package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.enums.*;

public class ClimbingArms extends SubsystemBase{
    private final TalonSRX armMotor1;
    private final TalonSRX armMotor2;

    public ClimbingArms(int armMotor1, int armMotor2) {
        this.armMotor1 = new TalonSRX(armMotor1);
        this.armMotor2 = new TalonSRX(armMotor2);
    }

    @Override 
    public void periodic() {
        armMotor2.set(ControlMode.PercentOutput, (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmUpButton) ? Constants.climbingArmSpeed : 0) +
                                               (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmDownButton) ? -Constants.climbingArmSpeed : 0));
        armMotor1.set(ControlMode.PercentOutput, (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmUpButton) ? Constants.climbingArmSpeed : 0) +
                                               (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmDownButton) ? -Constants.climbingArmSpeed : 0));
    }
}