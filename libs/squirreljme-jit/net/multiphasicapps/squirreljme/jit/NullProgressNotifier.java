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
 * This is a progress notifier which has no output anywhere.
 *
 * @since 2017/08/29
 */
public class NullProgressNotifier
	implements JITProgressNotifier
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void beginJar(String __n)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void endJar(String __n, int __lr, int __lc)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void jitEndHighLevelProgram(LinkingPoint __lp, long __ns)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void jitStartHighLevelProgram(LinkingPoint __lp)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void processClass(String __n, String __cl, int __num)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void processResource(String __n, String __rc, int __num)
	{
	}
}

