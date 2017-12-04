// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.id;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.runtime.midlet.depends.ProvidedInfo;
import net.multiphasicapps.squirreljme.runtime.midlet.depends.DependencyInfo;
import net.multiphasicapps.squirreljme.runtime.midlet.InvalidSuiteException;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains all of the information which is provided by a suite.
 *
 * @since 2017/11/30
 */
public final class SuiteInfo
{
	/** The type of suite this is. */
	protected final SuiteType type;
	
	/** Required dependency information. */
	private volatile Reference<DependencyInfo> _dependencies;
	
	/** Provided dependency information. */
	private volatile Reference<ProvidedInfo> _provided;
	
	/**
	 * Initializes the suite information.
	 *
	 * @param __man The manifest making up the suite information.
	 * @throws InvalidSuiteException If the suite information is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/04
	 */
	public SuiteInfo(JavaManifest __man)
		throws InvalidSuiteException, NullPointerException
	{
		if (__man == null)
			throw new NullPointerException("NARG");
		
		// First determine the type
		SuiteType type = SuiteType.ofManifest(__man);
		this.type = type;
		
		throw new todo.TODO();
	}
	
	/**
	 * Return the dependencies which are required by this suite.
	 *
	 * @return The dependencies required by this suite.
	 * @since 2017/12/04
	 */
	public final DependencyInfo dependencies()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the dependencies provided by this suite.
	 *
	 * @return The provided dependencies for this suite.
	 * @since 2017/12/04
	 */
	public final ProvidedInfo provided()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type of suite this is.
	 *
	 * @return The type of suite.
	 * @since 2017/12/04
	 */
	public final SuiteType type()
	{
		return this.type;
	}
}

