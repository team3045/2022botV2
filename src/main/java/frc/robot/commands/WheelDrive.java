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
  public  final int id;
  private double setpoint;

  private double wheelOffset;
  private double encoderPosInPeriodic;

  public WheelDrive (int angleMotor, int speedMotor, int encoder, int id) {
    this.id = id;
    //watch was here
    this.angleMotor = new TalonFX (angleMotor);
    this.speedMotor = new TalonFX (speedMotor);
    this.encoder = new CANCoder(encoder);

    wheelOffset = this.encoder.getAbsolutePosition();
  }
  public void drive (double speed, double angle) {
    encoderPosInPeriodic = encoder.getAbsolutePosition();
    this.setpoint = angle;

    //System.out.println(id + ": " +getEncoderOut());
    speedMotor.set (ControlMode.PercentOutput, speed * angleFactor());

    double rate = -MathUtil.clamp(/*getMagScaler() * */MathUtil.clamp(getError() * 0.007,-0.5, 0.5),-1, 1);
    //System.out.println(id + ":" + getEncoderOut() + '|' + getError() + '|' + setpoint + '|' + rate);
    
    
    angleMotor.set(ControlMode.PercentOutput, rate);
  }
  double angleFactor(){
    double delta = getEncoderOut()-setpoint;
    delta *= Math.PI/180;
    return -Math.cos(delta);
  }
  public double getEncoderOut(){
    return (((encoderPosInPeriodic-wheelOffset)%360)+360)%360;
  }
  public double PIDEncOut(){
    return getEncoderOut() % 180;
  }
  public double getSetpoint(){
    return setpoint;
  }
  public double getError(){
    setpoint %= 180;

    double loopDownError = -((180 - setpoint) + PIDEncOut());
    double loopUpError = ((180 - PIDEncOut()) + setpoint);

    double loopError = Math.abs(loopUpError) > Math.abs(loopDownError) ? loopDownError : loopUpError;

    double nonLoopError = (setpoint%180) - PIDEncOut();
    
    double error = (Math.abs(nonLoopError) > Math.abs(loopError)) ? loopError : nonLoopError;

    //System.out.print("\nID:" + id + "\nLDE:" + loopDownError + "\nLUE:" + loopUpError + "\nLP" + loopError + "\nNLE:" + nonLoopError + "\nError:" + error + "\nSP:" + setpoint + "\nENC:" + PIDEncOut());
    //System.out.println("Enc: " + PIDEncOut() + "\nSet: " + (setpoint%180) /*+ "\nLue: "+loopDownError+"\nLde: "+loopUpError*/+"\nLe: "+loopError+"\nNle: "+nonLoopError+"\nE: "+error);
    
    return (error);
  }
  public double getMagScaler(){
    return (1 + (Constants.RotResConst * Math.abs(speedMotor.getSelectedSensorVelocity())));
  }
}