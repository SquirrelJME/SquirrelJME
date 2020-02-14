// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
	public abstract void and(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void clearBit(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void clearBits(T __mask)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract T get()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract Class<T> getType();
	
	public abstract boolean isBitSet(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void or(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void set(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void setBit(int __dx)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void setBits(T __mask)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void xor(T __v)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
}

