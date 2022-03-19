package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.enums.*;

public class ClimbingArms extends SubsystemBase{
    private final TalonSRX armMot1;
    private final TalonSRX armMot2;

    public ClimbingArms(int armMot1, int armMot2) {
        this.armMot1 = new TalonSRX(armMot1);
        this.armMot2 = new TalonSRX(armMot2);
    }

    @Override 
    public void periodic() {
        armMot1.set(ControlMode.PercentOutput, (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmUpButton) ? Constants.climbingArmSpeed : 0) +
                                               (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.climbingArmDownButton) ? -Constants.climbingArmSpeed : 0));
    }
}