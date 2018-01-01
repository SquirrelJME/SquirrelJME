// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel.syscall;

import java.io.InputStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;

/**
 * This represents a wrapped program within the kernel.
 *
 * @since 2017/12/31
 */
public final class DirectProgram
	implements SystemProgram
{
	/** The task of the current process. */
	protected final KernelTask current;
	
	/** The wrapped program to access. */
	protected final KernelProgram wrapped;
	
	/**
	 * Initializes the wrapped program.
	 *
	 * @param __current The current execution task.
	 * @param __wrapped The program to be wrapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public DirectProgram(KernelTask __current, KernelProgram __wrapped)
		throws NullPointerException
	{
		if (__current == null || __wrapped == null)
			throw new NullPointerException("NARG");
		
		this.current = __current;
		this.wrapped = __wrapped;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public String controlGet(String __k)
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return this.wrapped.controlGet(this.current, __k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public void controlSet(String __k, String __v)
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		this.wrapped.controlSet(this.current, __k, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public int index()
	{
		return this.wrapped.index();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public InputStream loadResource(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this.wrapped.loadResource(this.current, __n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public int type()
	{
		return this.wrapped.type(this.current);
	}
	
	/**
	 * Returns the wrapped program.
	 *
	 * @return The wrapped program.
	 * @since 2017/12/31
	 */
	final KernelProgram __program()
	{
		return this.wrapped;
	}
}

