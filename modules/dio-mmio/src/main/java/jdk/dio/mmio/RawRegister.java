// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.mmio;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

public interface RawRegister<T extends Number>
	extends RawMemory
{
	void and(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void clearBit(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void clearBits(T __mask)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	T get()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	Class<T> getType();
	
	boolean isBitSet(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void or(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void set(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void setBit(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void setBits(T __mask)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	void xor(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
}

