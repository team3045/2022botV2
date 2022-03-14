/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import javax.print.attribute.SetOfIntegerSyntax;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.robot.Constants;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.time.StopWatch;

import edu.wpi.first.wpilibj.SerialPort.StopBits;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class WheelDrive {
  private final TalonFX angleMotor;
  private final TalonFX speedMotor;
  private final CANCoder encoder;
  private final StopWatch watch;
  public  final WheelDrivePID pid;
  private double setpoint;

  public WheelDrive (int angleMotor, int speedMotor, int encoder) {
    watch = new StopWatch();
    watch.start();
    this.angleMotor = new TalonFX (angleMotor);
    this.speedMotor = new TalonFX (speedMotor);
    this.encoder = new CANCoder(encoder);

    pid = new WheelDrivePID(this);
  }
  public void drive (double speed, double angle) {
    speedMotor.set (ControlMode.PercentOutput, speed * angleFactor());
    this.setpoint = angle;
  }
  double angleFactor(){
    double delta = getEncoderOut()-setpoint;
    delta *= Math.PI/180;

    return -Math.cos(delta);
  }
  public double getEncoderOut(){
    return ((encoder.getPosition()%360)+360)%360;
  }
  public double PIDEncOut(){
    return getEncoderOut() % 180;
  }
  public double getSetpoint(){
    return setpoint;
  }
  double get40ErrorClampedSetpoint(){
    double loopError = ((setpoint + 90) % 180) - ((PIDEncOut() + 90) %180);
    double nonLoopError = (setpoint%180) - PIDEncOut();
    
    double error = (Math.abs(nonLoopError) > Math.abs(loopError)) ? loopError : nonLoopError;

    System.out.println("Enc: " + PIDEncOut() + "\nSet: " + (setpoint%180) /*+ "\nLue: "+loopDownError+"\nLde: "+loopUpError*/+"\nLe: "+loopError+"\nNle: "+nonLoopError+"\nE: "+error);

    if(error > 40)
      error = 40;
    if(error < -40)
      error = -40;
    
    return (PIDEncOut() + error + 180) % 180;
  }
  public double PIDSetpoint(){
    return (get40ErrorClampedSetpoint() + 90) % 180;
  } 
  public double getMagScaler(){
    return (1 + (Constants.RotResConst * Math.abs(speedMotor.getSelectedSensorVelocity())));
  }
  public void runAngleMotor(double rate){
    angleMotor.set(ControlMode.PercentOutput, rate /* getMagScaler()*/);
  }
}