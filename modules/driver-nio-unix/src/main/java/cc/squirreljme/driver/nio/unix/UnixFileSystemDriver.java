// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.unix;

import cc.squirreljme.jvm.DriverFactory;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Driver factory for creating instances.
 *
 * @since 2023/08/20
 */
public class UnixFileSystemDriver
	implements DriverFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public String name()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Object newInstance()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public int priority()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Class<?> providesClass()
	{
		throw Debugging.todo();
	}
}
