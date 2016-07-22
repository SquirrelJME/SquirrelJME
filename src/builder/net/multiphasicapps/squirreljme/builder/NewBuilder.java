// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.IOException;
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This builds the given target configuration and generates a binary.
 *
 * @since 2016/07/22
 */
public class NewBuilder
{
	/** The configuration. */
	protected final BuildConfig config;
	
	/** The target builder. */
	protected final TargetBuilder targetbuilder;
	
	/** The package list. */
	protected final PackageList packagelist;
	
	/**
	 * Initializes the new builder code.
	 *
	 * @param __conf The build configuration.
	 * @param __tb The target builder.
	 * @param __pl The package list.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public NewBuilder(BuildConfig __conf, TargetBuilder __tb, PackageList __pl)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __tb == null || __pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.targetbuilder = __tb;
		this.packagelist = __pl;
	}
	
	/**
	 * Performs the actual build.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/07/22
	 */
	public void build()
		throws IOException
	{
		throw new Error("TODO");
	}
}

