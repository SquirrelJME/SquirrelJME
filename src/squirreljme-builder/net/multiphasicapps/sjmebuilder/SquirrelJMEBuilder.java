// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.nio.file.Path;

/**
 * This class performs the actual building of SquirrelJME depending on the
 * supported targets and such.
 *
 * @since 2016/06/15
 */
public class SquirrelJMEBuilder
{
	/** The output JAR directory (and where the SquirrelJME binary goes). */
	protected final Path outdir;
	
	/** The directory which contains source code. */
	protected final Path srcdir;
	
	/** The target operating system. */
	protected final String targos;
	
	/** The target CPU. */
	protected final String targcpu;
	
	/**
	 * Initializes the builder.
	 *
	 * @param __jd The directory containing the output JAR files.
	 * @param __sd The source code root.
	 * @param __os The operating system to target.
	 * @parma __cpu The CPU to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/15
	 */
	public SquirrelJMEBuilder(Path __jd, Path __sd, String __os, String __cpu)
		throws NullPointerException
	{
		// Check
		if (__jd == null || __sd == null || __os == null || __cpu == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.outdir = __jd;
		this.srcdir = __sd;
		this.targos = __os;
		this.targcpu = __cpu;
	}
	
	/**
	 * Builds SquirrelJME.
	 *
	 * @since 2016/06/15
	 */
	public void build()
	{
		throw new Error("TODO");
	}
}

