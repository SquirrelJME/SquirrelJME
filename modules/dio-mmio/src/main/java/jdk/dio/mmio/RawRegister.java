// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.mmio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface RawRegister<T extends Number>
	extends RawMemory
{
	@Api
	void and(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void clearBit(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void clearBits(T __mask)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	T get()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	Class<T> getType();
	
	@Api
	boolean isBitSet(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void or(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void set(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void setBit(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void setBits(T __mask)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void xor(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
}

