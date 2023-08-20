// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.java;

import cc.squirreljme.jvm.DriverFactory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.FileSystem;

/**
 * Driver factory for creating instances.
 *
 * @since 2023/08/20
 */
public class JavaFileSystemDriver
	implements DriverFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public String name()
	{
		return "java";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Object newInstance()
	{
		return new JavaFileSystem();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public int priority()
	{
		return 1000;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public Class<?> providesClass()
	{
		return FileSystem.class;
	}
}
