// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.interpreter;

import net.multiphasicapps.squirreljme.emulator.EmulatorComponent;
import net.multiphasicapps.squirreljme.emulator.EmulatorComponentFactory;
import net.multiphasicapps.squirreljme.emulator.EmulatorSystem;

/**
 * This factory is used to .
 *
 * @since 2016/07/26
 */
public class InterpreterCPUFactory
	implements EmulatorComponentFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	public boolean isComponentHandled(
		Class<? extends EmulatorComponent> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return (__cl.equals(InterpreterCPU.class));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/26
	 */
	@Override
	public <C extends EmulatorComponent> C createComponent(
		Class<C> __cl, EmulatorSystem __es, String __id, String... __args)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __es == null || __id == null || __args == null)
			throw new NullPointerException("NARG");
		
		// CPU?
		if (__cl.equals(InterpreterCPU.class))
			return __cl.cast(new InterpreterCPU(__es, __id, __args));
		
		// {@squirreljme.error BK01 Unknown component class type.}
		else
			throw new IllegalArgumentException("BK01");
	}
}

