// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This is a simple Java execution specification filler which provides the
 * minimal functionality.
 *
 * @since 2020/12/26
 */
public class SimpleJavaExecSpecFiller
	implements JavaExecSpecFiller
{
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void classpath(Collection<Object> __classPath)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public Iterable<String> getCommandLine()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void setMain(String __mainClass)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void setArgs(Collection<String> __asList)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/12/26
	 */
	@Override
	public void systemProperties(Map<String, String> __sysProps)
	{
		throw new Error("TODO");
	}
}
