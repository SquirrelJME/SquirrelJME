// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.util.FileLocation;
import java.nio.file.Path;

/**
 * The output for a task.
 *
 * @since 2020/02/28
 */
final class __Output__
{
	/** The input file. */
	public final FileLocation input;
	
	/** The output path. */
	public final Path output;
	
	/**
	 * Initializes the output path.
	 *
	 * @param __input The input path.
	 * @param __output The output path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	public __Output__(FileLocation __input, Path __output)
		throws NullPointerException
	{
		if (__input == null || __output == null)
			throw new NullPointerException();
		
		this.input = __input;
		this.output = __output;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/28
	 */
	@Override
	public final String toString()
	{
		return String.format("{input=%s, output=%s}",
			this.input, this.output);
	}
}
