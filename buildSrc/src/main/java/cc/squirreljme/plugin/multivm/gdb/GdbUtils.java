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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gradle.internal.os.OperatingSystem;

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
	 * Returns the URIs where debuggers are available.
	 *
	 * @return The URI for any debugger or {@code null} if there is none.
	 * @since 2024/07/30
	 */
	public static URI[] debuggerUri()
	{
		List<URI> result = new ArrayList<>();
		
		// WinDbg?
		if (OperatingSystem.current().isWindows())
		{
			Path winDbgServerPath = GdbUtils.winDbgServerExePath();
			if (winDbgServerPath != null)
				result.add(GdbUtils.setScheme(winDbgServerPath.toUri(),
					"windbg"));
		}
		
		// GDB?
		Path gdbServerPath = GdbUtils.gdbServerExePath();
		if (gdbServerPath != null)
			result.add(GdbUtils.setScheme(gdbServerPath.toUri(), "gdb"));
		
		// LLDB?
		Path lldbServerPath = GdbUtils.lldbServerExePath();
		if (lldbServerPath != null)
			result.add(GdbUtils.setScheme(lldbServerPath.toUri(), "lldb"));
		
		// Not found?
		if (result.isEmpty())
			return null;
		return result.toArray(new URI[result.size()]);
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
	
	/**
	 * Returns the location of WinDbg.
	 *
	 * @return The location of WinDbg.
	 * @since 2024/07/31
	 */
	public static Path winDbgServerExePath()
	{
		for (String kit : Arrays.asList("10", "8", "7"))
			for (String arch : Arrays.asList("x64", "x86"))
			{
				Path maybe = PathUtils.findPathInstalled("windbg",
					Paths.get("Windows Kits", kit,
						"Debuggers", arch));
				if (maybe != null)
					if (Files.exists(maybe) && Files.isExecutable(maybe))
						return maybe;
			}
		
		// Not found
		return null;
	}
}
