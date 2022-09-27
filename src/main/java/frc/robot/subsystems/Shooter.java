package frc.robot.subsystems;

import java.util.logging.LogManager;

import javax.print.attribute.SetOfIntegerSyntax;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.time.StopWatch;

import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.enums.*;


public class Shooter extends SubsystemBase {
    private final TalonFX topFW;
    private final TalonFX bottomFW;

    private final StopWatch revWatch;
    
    public Shooter(int topFWID, int bottomFWID){
        revWatch = new StopWatch();

        topFW = new TalonFX(topFWID);
        bottomFW = new TalonFX(bottomFWID);
    }

    double regression(Double distance){
        return SmartDashboard.getNumber("throttle", 0.0);
        /*
        if(distance == null){
          System.out.println("Aim off or goal not visible");
          return 0.0;
        } else {
          double speed = 0.0;
          
          speed += 0.385952381;
          speed += 0.0408044733 * distance;
          speed += -0.0129329004 * distance * distance;
          speed += 0.0014141414 * distance * distance * distance;
            
          speed += 0.02;
          return speed;
        }
        */
    }

    @Override
    public void periodic(){
        /*if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_AIM || RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_AIM){
            if(RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance() != null){
                SmartDashboard.putNumber("Distance", RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance());
                SmartDashboard.putNumber("Speed", regression(RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance()));
            }
        }*/
        if(RobotContainer.DRIVE_MODE == DRIVE_MODE.TELEOP_DRIVE){
            if(RobotContainer.getInstance().buttonBoard.getRawButtonPressed(Constants.revButton) || RobotContainer.getInstance().buttonBoard.getRawButtonReleased(Constants.revButton))
                revWatch.start();

            if (RobotContainer.getInstance().buttonBoard.getRawButton(Constants.revButton)){
                topFW.set(ControlMode.PercentOutput, regression(RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance()));
                bottomFW.set(ControlMode.PercentOutput, -regression(RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance()));
            } else {
                topFW.set(ControlMode.PercentOutput,0);
                bottomFW.set(ControlMode.PercentOutput,0);
            }
        } else if(RobotContainer.DRIVE_MODE == DRIVE_MODE.AUTON_SHOOT){
            topFW.set(ControlMode.PercentOutput, regression(RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance()));
            bottomFW.set(ControlMode.PercentOutput, -regression(RobotContainer.getInstance().LimelightVision.getGoalHorizontalDistance()));
        } else {
            topFW.set(ControlMode.PercentOutput,  0);
            bottomFW.set(ControlMode.PercentOutput, 0);
        }
    }
}