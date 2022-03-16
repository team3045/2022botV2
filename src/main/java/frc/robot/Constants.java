/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
/*
 *   
 *   The Constants class provides a convenie  p
 * ace for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * dec
 * lared globally (i.e. public static).  Do not put anything functional in thi
 *  class. 
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int TIMEOUT = 10;
    public static final double MAX_VOLTS = 4.95;

    public static final double WheelPIDP = 0.0014;
    public static final double WheelPIDI = 0.0;
    public static final double WheelPIDD = 0.00007;//00007;

    public static final double RotResConst = 0.0; //0.0005

    public static final int intakeButton = 0;
    public static final double limeLightAngleUp = 0; //Not final angle of limelight
    public static final double[] limeLightShooterOffset = new double[]{1, 1, 1}; //Not final limelight Shooter offset
    public static final double limeLightGoalVerticalOffset = 5; //also not final
    public static final double G = 9.80665; //assuming m/s^2
    public static final double goalRadius = 0.68; //assuming meters
}
