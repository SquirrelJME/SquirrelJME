// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Prints the SquirrelJME version information.
 * 
 * https://www.oracle.com/java/technologies/javase/versioning-naming.html
 *
 * @since 2025/04/07
 */
@SquirrelJMEVendorApi
public final class PrintVersion
{
	/**
	 * Describe this.
	 * @since 2025/04/07
	 */
	private PrintVersion()
	{
	}
	
	/**
	 * Main program arguments.
	 *
	 * @param __args Main arguments.
	 * @since 2025/04/07
	 */
	@SquirrelJMEVendorApi
	public static void main(String... __args)
		throws IOException
	{
		// Where is the version string printed?
		PrintStream out;
		if (__args == null || __args.length == 0 || __args[0] == null ||
			"-version".equals(__args[0]))
			out = System.out;
		else
			out = System.err;
		
		// Print the version string out, using the standard output format
		PrintVersion.__print(out,
			(__args != null && __args.length >= 2 ? __args[1] : "tarball"));
		
		// Success!
		System.exit(0);
	}
	
	/**
	 * Prints the version string.
	 *
	 * @param __out The output to print to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/14
	 */
	@SquirrelJMEVendorApi
	public static void print(Appendable __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Read in version resource, if it exists
		String libSubVer = "tarball";
		try (InputStream in = PrintVersion.class.getResourceAsStream(
			"version"))
		{
			if (in != null)
				libSubVer = new String(StreamUtils.readAll(in),
					"utf-8").trim();
		}
		
		// Handle print
		PrintVersion.__print(__out, libSubVer);
	}
	
	/**
	 * Prints the version string.
	 *
	 * @param __out The output to print to.
	 * @param __libSubVer The library sub-version.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/14
	 */
	private static void __print(Appendable __out, String __libSubVer)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Get end of line
		String eol = System.getProperty("line.separator");
		
		// Print the version string out, using the standard output format
		__out.append("java version \"1.8.0\"");
		__out.append(eol);
		
		// Library version
		__out.append("SquirrelJME Class Library, Micro Edition (build ");
		__out.append(SquirrelJME.RUNTIME_VERSION);
		if (__libSubVer != null)
		{
			__out.append("-");
			__out.append(__libSubVer);
		}
		__out.append(")");
		__out.append(eol);
		
		// Runtime version
		__out.append(RuntimeShelf.vmDescription(VMDescriptionType.VM_NAME));
		__out.append(" (build ");
		__out.append(RuntimeShelf.vmDescription(VMDescriptionType.VM_VERSION));
		__out.append(", ");
		__out.append(RuntimeShelf.vmDescription(VMDescriptionType.VM_INFO));
		__out.append(")");
		__out.append(eol);
	}
}
