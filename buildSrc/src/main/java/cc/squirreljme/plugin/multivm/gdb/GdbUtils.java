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
import java.net.URI;
import java.net.URISyntaxException;
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
	 * Returns the URI where the debugger is available.
	 *
	 * @return The URI for the debugger or {@code null} if there is none.
	 * @since 2024/07/30
	 */
	public static URI debuggerUri()
	{
		// GDB?
		Path gdbServerPath = GdbUtils.gdbServerExePath();
		if (gdbServerPath != null)
			return GdbUtils.setScheme(gdbServerPath.toUri(), "gdb");
		
		// LLDB?
		Path lldbServerPath = GdbUtils.lldbServerExePath();
		if (lldbServerPath != null)
			return GdbUtils.setScheme(lldbServerPath.toUri(), "lldb");
		
		// Not found
		return null;
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
	
	/**
	 * Returns the path where LLDB Server is available.
	 *
	 * @return The LLDB Server path.
	 * @since 2024/07/30
	 */
	public static Path lldbServerExePath()
	{
		return PathUtils.findPathInstalled("lldb-server",
			"LLVM");
	}
	
	/**
	 * Switches the URI protocol.
	 *
	 * @param __in The input URI.
	 * @param __to The protocol to use.
	 * @return The new URI with the given protocol.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/30
	 */
	public static URI setScheme(URI __in, String __to)
		throws NullPointerException
	{
		if (__in == null || __to == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new URI(__to, __in.getHost(), __in.getPath(),
				__in.getFragment());
		}
		catch (URISyntaxException __e)
		{
			throw new RuntimeException("Bad URI.", __e);
		}
	}
}
