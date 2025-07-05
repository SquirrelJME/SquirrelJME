// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ClassName;

/**
 * Not Described.
 *
 * @since 2025/07/05
 */
public abstract class SpringBaseClass
	implements SpringClass
{
	/** The monitor for this class object instance. */
	protected final SpringMonitor monitor =
		new SpringMonitor();
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/05
	 */
	@Override
	public final SpringMonitor monitor()
	{
		return this.monitor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/05
	 */
	@Override
	public final SpringClass type()
	{
		return this.classLoader().loadClass(
			new ClassName("java/lang/Class"));
	}
}
