// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.nio.file.Path;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteID;

/**
 * This represents the base class for MIDlets and LIBlets.
 *
 * @since 2016/11/20
 */
public abstract class ApplicationProject
	extends BaseProject
{
	/** The owning application manager. */
	protected final ApplicationManager appman;
	
	/**
	 * Initializes the project information.
	 *
	 * @param __am The owning application manager.
	 * @param __p The path to the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/20
	 */
	ApplicationProject(ApplicationManager __am, Path __p)
		throws NullPointerException
	{
		super(__p);
		
		// Check
		if (__am == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.appman = __am;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the application manager which owns this project.
	 *
	 * @return The owning application manager.
	 * @since 2016/11/24
	 */
	public final ApplicationManager applicationManager()
	{
		return this.appman;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/24
	 */
	@Override
	public final int hashCode()
	{
		return midletSuiteId().hashCode();
	}
	
	/**
	 * Returns the midlet suite identification.
	 *
	 * @return The suite identification.
	 * @since 2016/11/24
	 */
	public final MidletSuiteID midletSuiteId()
	{
		throw new Error("TODO");
	}
}

