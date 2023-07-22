// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.InputRoundListener;
import jdk.dio.OutputRoundListener;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface UART
	extends Device<UART>, ByteChannel, BufferAccess<ByteBuffer>
{
	@Api
	void generateBreak(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getBaudRate()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getDataBits()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getFlowControlMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getParity()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getReceiveTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getReceiveTriggerLevel()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getStopBits()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setBaudRate(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setDataBits(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setEventListener(int __a, UARTEventListener __b)
		throws IOException, ClosedDeviceException;
	
	@Api
	void setFlowControlMode(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setParity(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setReceiveTimeout(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setReceiveTriggerLevel(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setStopBits(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startReading(ByteBuffer __a,
	 InputRoundListener<UART, ByteBuffer> __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startReading(ByteBuffer __a, ByteBuffer __b,
		InputRoundListener<UART, ByteBuffer> __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startWriting(ByteBuffer __a,
		OutputRoundListener<UART, ByteBuffer> __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startWriting(ByteBuffer __a, ByteBuffer __b,
		OutputRoundListener<UART, ByteBuffer> __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void stopReading()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void stopWriting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


