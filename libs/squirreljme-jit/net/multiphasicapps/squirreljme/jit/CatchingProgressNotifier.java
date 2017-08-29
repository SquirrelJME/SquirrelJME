// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.bin.LinkingPoint;

/**
 * This is a progress notifier which catches exceptions.
 *
 * @since 2017/08/29
 */
public class CatchingProgressNotifier
	implements JITProgressNotifier
{
	/** The notifier to forward to. */
	protected final JITProgressNotifier forward;
	
	/**
	 * Initializes the catching progress notifier.
	 *
	 * @param __f The notifier to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/29
	 */
	public CatchingProgressNotifier(JITProgressNotifier __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.forward = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void beginJar(String __n)
	{
		try
		{
			this.forward.beginJar(__n);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void endJar(String __n, int __lr, int __lc)
	{
		try
		{
			this.forward.endJar(__n, __lr, __lc);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void jitEndHighLevelProgram(LinkingPoint __lp, long __ns)
	{
		try
		{
			this.forward.jitEndHighLevelProgram(__lp, __ns);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void jitStartHighLevelProgram(LinkingPoint __lp)
	{
		try
		{
			this.forward.jitStartHighLevelProgram(__lp);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void processClass(String __n, String __cl, int __num)
	{
		try
		{
			this.forward.processClass(__n, __cl, __num);
		}
		catch (Exception e)
		{
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void processResource(String __n, String __rc, int __num)
	{
		try
		{
			this.forward.processResource(__n, __rc, __num);
		}
		catch (Exception e)
		{
		}
	}
}

