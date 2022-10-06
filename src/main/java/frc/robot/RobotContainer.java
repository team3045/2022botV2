/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.StatusFrame;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.enums.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static DRIVE_MODE DRIVE_MODE;

  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final DriveTrain driveTrain;

  public static Robot robot;

  public final WheelDrive backRight;
  public final WheelDrive backLeft;
  public final WheelDrive frontRight;
  public final WheelDrive frontLeft;
  
  public final Shooter shooter;
  public final Intake intake;
  public final ClimbingArms climbingArms;
  public final Magazine magazine;
  
  public final LimelightVision LimelightVision = new LimelightVision();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  private static RobotContainer instance;
  public final Joystick buttonBoard = new Joystick(2);

  public final Compressor comp = new Compressor(PneumaticsModuleType.CTREPCM);

  public static RobotContainer getInstance(){
    return instance;
  }
  public DriveTrain getDRinstance(){
    return driveTrain;
  }
  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    CameraServer.startAutomaticCapture();
    CameraServer.startAutomaticCapture(1);

    DRIVE_MODE = DRIVE_MODE.PRE_ENABLE;
    instance=this;
    driveTrain = new DriveTrain(1, 0);

    frontLeft = new WheelDrive (0, 1, 10, 0, 7.82);
    backRight = new WheelDrive (4, 5, 11, 1, 256.99);
    frontRight = new WheelDrive (2, 3, 8, 2, 336.18);
    backLeft = new WheelDrive (6, 7, 9, 3, 307.0);

    shooter = new Shooter(8, 9);

    intake = new Intake(18, 0);

    climbingArms = new ClimbingArms(17,  19);

    magazine = new Magazine(15);
  




    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
