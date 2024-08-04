// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.RefLinkHolder;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * A proxy virtualized object that wraps one on the host.
 *
 * @since 2024/08/04
 */
public class SpringVisObject
	extends SpringProxyObject
	implements SpringObject
{
	/** The real object to access. */
	private final Object real;
	
	/** The vis class for this object. */
	private volatile SpringVisClass _visClass;
	
	/**
	 * Initializes the vis object.
	 *
	 * @param __machine The machine to use.
	 * @param __real The real object to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public SpringVisObject(SpringMachine __machine, Object __real)
		throws NullPointerException
	{
		super(new ClassName(__real.getClass().getName()
			.replace('.', '/')), __machine);
		
		if (__machine == null || __real == null)
			throw new NullPointerException("NARG");
		
		this.real = __real;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	protected Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClass type()
	{
		SpringVisClass result;
		synchronized (this)
		{
			// Has this been cached already?
			result = this._visClass;
			if (result != null)
				return result;
			
			// Virtualize it
			result = this.machine.virtualizeClass(this.real.getClass());
			this._visClass = result;
			return result;
		}
	}
}
