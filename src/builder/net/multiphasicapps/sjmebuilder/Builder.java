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

import java.io.IOException;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This is the builder for native binaries.
 *
 * @since 2016/06/24
 */
public class Builder
{
	/** The package list to use. */
	protected final PackageList plist;
	
	/** The target architecture. */
	protected final String arch;
	
	/** The target operating system. */
	protected final String os;
	
	/** The target variant. */
	protected final String variant;
	
	/** The triplet. */
	protected final String triplet;
	
	/**
	 * Initializes the builder for a native target.
	 *
	 * @param __pl The package list to use.
	 * @param __arch The target architecture.
	 * @param __os The target operating system.
	 * @param __var The target variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public Builder(PackageList __pl, String __arch, String __os, String __var)
		throws NullPointerException
	{
		// Check
		if (__pl == null || __arch == null || __os == null || __var == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.plist = __pl;
		this.arch = __arch;
		this.os = __os;
		this.variant = __var;
		this.triplet = __arch + "." + __os + "." + __var;
		
		throw new Error("TODO");
	}
	
	/**
	 * Performs the actual build step.
	 *
	 * @since 2016/06/24
	 */
	public void build()
	{
		throw new Error("TODO");
	}
}

