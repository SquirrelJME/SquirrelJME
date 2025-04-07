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
import java.io.PrintStream;

/**
 * Prints the SquirrelJME version information.
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
	{
		// Where is the version string printed?
		PrintStream out;
		if (__args == null || __args.length == 0 || __args[0] == null ||
			"-version".equals(__args[0]))
			out = System.out;
		else
			out = System.err;
		
		// Print the version string out, using the standard output format
		out.printf("java version \"1.8.0\"%n");
		out.printf("SquirrelJME Class Library, Micro Edition (build %s)%n",
			SquirrelJME.RUNTIME_VERSION);
		out.printf("%s (build %s)%n",
			RuntimeShelf.vmDescription(VMDescriptionType.VM_NAME),
			RuntimeShelf.vmDescription(VMDescriptionType.VM_VERSION));
		
		// Success!
		System.exit(0);
	}
}
