// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * A callback that can run natively.
 *
 * @since 2024/12/16
 */
@KeepWhenCompacting
final class __NativeCallback__
	implements Runnable
{
	/** The anything pointer. */
	@SquirrelJMEVendorApi
	protected final long anythingP;
	
	/** The function pointer. */
	@SquirrelJMEVendorApi
	protected final long funcP;
	
	/** The state pointer. */
	@SquirrelJMEVendorApi
	protected final long stateP;
	
	/**
	 * Initializes the native callback wrapper.
	 *
	 * @param __stateP The state pointer.
	 * @param __funcP The function pointer.
	 * @param __anythingP The anything pointer.
	 * @since 2024/12/16
	 */
	@KeepWhenCompacting
	__NativeCallback__(long __stateP, long __funcP, long __anythingP)
	{
		this.stateP = __stateP;
		this.funcP = __funcP;
		this.anythingP = __anythingP;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/12/16
	 */
	@Override
	public void run()
	{
		int error = __NativeCallback__.__invoke(this.stateP, this.funcP,
			this.anythingP);
		
		/* Did it fail? */
		if (error != 1)
			throw new RuntimeException(String.format(
				"Native error %d", error));
	}
	
	/**
	 * Invokes the given callback.
	 *
	 * @param __stateP The state pointer.
	 * @param __funcP The function pointer.
	 * @param __anythingP Any passed data.
	 * @return Any resultant error code.
	 * @since 2024/12/16
	 */
	@SquirrelJMEVendorApi
	private static native int __invoke(long __stateP, long __funcP,
		long __anythingP); 
}
