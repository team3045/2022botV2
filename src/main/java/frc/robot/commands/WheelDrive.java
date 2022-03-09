/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.robot.Constants;
import com.ctre.phoenix.sensors.CANCoder;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class WheelDrive {
  private final TalonFX angleMotor;
  private final TalonFX speedMotor;
  private final CANCoder encoder;
  public  final WheelDrivePID pid;

  private double setpoint;

  public WheelDrive (int angleMotor, int speedMotor, int encoder) {
    this.angleMotor = new TalonFX (angleMotor);
    this.speedMotor = new TalonFX (speedMotor);
    this.encoder = new CANCoder(encoder);

    pid = new WheelDrivePID(this);
  }
  public void drive (double speed, double angle) {
    speedMotor.set (ControlMode.PercentOutput, speed);
    this.setpoint = angle;
  }
  public double getEncoderOut(){
    return ((encoder.getPosition()%360)+360)%360;
  }
  public double getSetpoint(){
    System.out.println(setpoint);
    return setpoint;
  }
  public double getScaledP(){
    return (1 + Math.abs(speedMotor.getSelectedSensorVelocity())) * Constants.WheelPIDP;
  }
  public void runAngleMotor(double rate){
    System.out.println("rate: " + rate); 
    angleMotor.set(ControlMode.PercentOutput, rate);
  }
}