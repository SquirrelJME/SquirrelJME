// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.InputRoundListener;
import jdk.dio.OutputRoundListener;
import jdk.dio.UnavailableDeviceException;

public interface UART
	extends Device<UART>, ByteChannel, BufferAccess<ByteBuffer>
{
	void generateBreak(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getBaudRate()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getDataBits()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getFlowControlMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getParity()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getReceiveTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getReceiveTriggerLevel()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getStopBits()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setBaudRate(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setDataBits(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setEventListener(int __a, UARTEventListener __b)
		throws IOException, ClosedDeviceException;
	
	void setFlowControlMode(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setParity(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setReceiveTimeout(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setReceiveTriggerLevel(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setStopBits(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startReading(ByteBuffer __a,
	 InputRoundListener<UART, ByteBuffer> __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startReading(ByteBuffer __a, ByteBuffer __b,
		InputRoundListener<UART, ByteBuffer> __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startWriting(ByteBuffer __a,
		OutputRoundListener<UART, ByteBuffer> __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startWriting(ByteBuffer __a, ByteBuffer __b,
		OutputRoundListener<UART, ByteBuffer> __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void stopReading()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void stopWriting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


