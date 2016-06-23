// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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
	public abstract void generateBreak(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getBaudRate()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getDataBits()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getFlowControlMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getParity()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getReceiveTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getReceiveTriggerLevel()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getStopBits()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setBaudRate(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setDataBits(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setEventListener(int __a, UARTEventListener __b)
		throws IOException, ClosedDeviceException;
	
	public abstract void setFlowControlMode(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setParity(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setReceiveTimeout(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setReceiveTriggerLevel(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setStopBits(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startReading(ByteBuffer __a, InputRoundListener<UART
		, ByteBuffer> __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startReading(ByteBuffer __a, ByteBuffer __b, 
		InputRoundListener<UART, ByteBuffer> __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startWriting(ByteBuffer __a, OutputRoundListener<
		UART, ByteBuffer> __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startWriting(ByteBuffer __a, ByteBuffer __b, 
		OutputRoundListener<UART, ByteBuffer> __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void stopReading()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void stopWriting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


