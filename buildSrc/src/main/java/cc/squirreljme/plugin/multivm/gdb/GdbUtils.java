// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm.gdb;

import cc.squirreljme.plugin.util.PathUtils;
import java.nio.file.Path;

/**
 * Utilities for GDB.
 *
 * @since 2024/06/15
 */
public final class GdbUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2024/06/15
	 */
	private GdbUtils()
	{
	}
	
	/**
	 * Returns the path where GDB is available.
	 *
	 * @return The GDB path.
	 * @since 2024/06/15
	 */
	public static Path gdbExePath()
	{
		return PathUtils.findPathInstalled("gdb",
			"gdb");
	}
	
	/**
	 * Returns the path where GDB Server is available.
	 *
	 * @return The GDB Server path.
	 * @since 2024/06/15
	 */
	public static Path gdbServerExePath()
	{
		return PathUtils.findPathInstalled("gdbserver",
			"gdb");
	}
}
