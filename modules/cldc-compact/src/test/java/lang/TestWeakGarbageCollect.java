// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests weak garbage collection.
 *
 * @since 2023/07/09
 */
public class TestWeakGarbageCollect
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/07/09
	 */
	@Override
	public void test()
	{
		// Create new weak reference, we have it created in a sub-method
		// because we do not want any local variables to remain which have
		// a reference to the actual object...
		Reference<Object> weak = this.weak();
		
		// It is unspecified when weak references are cleared, so we can
		// force our paws a bit with forced garbage collection...
		// OpenJDK itself does not immediately GC but SquirrelJME itself does
		// however we still need to coerce so this passes in OpenJDK...
		Runtime runtime = Runtime.getRuntime();
		for (int i = 0; i < 10; i++)
		{
			// Suggest garbage collection
			runtime.gc();
			
			// Yield and sleep to let it run, in the event of cooperative
			// systems
			Thread.yield();
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException ignored)
			{
			}
		}
		
		// Try to get the reference now
		this.secondary("ref", weak.get());
	}
	
	/**
	 * Makes a weak reference to an object.
	 * 
	 * @return A weak reference to some object.
	 * @since 2023/07/09
	 */
	public Reference<Object> weak()
	{
		return new WeakReference<>(new Object());
	}
}
