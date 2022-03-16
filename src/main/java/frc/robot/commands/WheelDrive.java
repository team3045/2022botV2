/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import javax.print.attribute.SetOfIntegerSyntax;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.robot.Constants;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.time.StopWatch;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.SerialPort.StopBits;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class WheelDrive {
  private final TalonFX angleMotor;
  private final TalonFX speedMotor;
  private final CANCoder encoder;
  private final StopWatch watch;
  public  final WheelDrivePID pid;
  public  final int id;
  private double setpoint;

  public WheelDrive (int angleMotor, int speedMotor, int encoder, int id) {
    this.id = id;
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

    System.out.println(id + ":" + getEncoderOut() + '|' + getError() + '|' + setpoint);
    

    angleMotor.set(ControlMode.PercentOutput, MathUtil.clamp(/*getMagScaler() * */MathUtil.clamp(getError() * SmartDashboard.getNumber("kP", 0.0),-0.5, 0.5),-1, 1));
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
    if((getEncoderOut() % 180) < 0 || (getEncoderOut() % 180) > 180)
      System.out.println(getEncoderOut() % 180);
    return getEncoderOut() % 180;
  }
  public double getSetpoint(){
    return setpoint;
  }
  public double getError(){
    double loopDownError = -((180 - setpoint) + PIDEncOut());
    double loopUpError = (180 - PIDEncOut()) + setpoint;

    double loopError = Math.abs(loopUpError) > Math.abs(loopDownError) ? loopDownError : loopUpError;

    double nonLoopError = (setpoint%180) - PIDEncOut();
    
    double error = (Math.abs(nonLoopError) > Math.abs(loopError)) ? loopError : nonLoopError;

    //System.out.print("\nID:" + id + "\nLDE:" + loopDownError + "\nLUE:" + loopUpError + "\nLP" + loopError + "\nNLE:" + nonLoopError + "\nError:" + error + "\nSP:" + setpoint + "\nENC:" + PIDEncOut());
    //System.out.println("Enc: " + PIDEncOut() + "\nSet: " + (setpoint%180) /*+ "\nLue: "+loopDownError+"\nLde: "+loopUpError*/+"\nLe: "+loopError+"\nNle: "+nonLoopError+"\nE: "+error);
    
    return (error);
  }
  public double PIDSetpoint(){
    return (getError());
  } 
  public double getMagScaler(){
    return (1 + (Constants.RotResConst * Math.abs(speedMotor.getSelectedSensorVelocity())));
  }
  public void runAngleMotor(double rate){ 
    rate = -SmartDashboard.getNumber("kP", 0.0) * getError();
    rate = MathUtil.clamp(rate, -0.5, 0.5);  
    angleMotor.set(ControlMode.PercentOutput, MathUtil.clamp(rate * getMagScaler(), -1, 1));
  }
}