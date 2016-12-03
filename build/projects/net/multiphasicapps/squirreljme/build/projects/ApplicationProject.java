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
import java.util.Objects;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteID;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteName;
import net.multiphasicapps.squirreljme.suiteid.MidletSuiteVendor;
import net.multiphasicapps.squirreljme.suiteid.MidletVersion;

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
	
	/** The project manifest (in its source form). */
	protected final JavaManifest sourcemanifest;
	
	/** The suite ID of the midlet. */
	protected final MidletSuiteID suiteid;
	
	/**
	 * Initializes the project information.
	 *
	 * @param __am The owning application manager.
	 * @param __p The path to the project.
	 * @param __man The manifest that is used for the source.
	 * @throws InvalidProjectException If the project is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/20
	 */
	ApplicationProject(ApplicationManager __am, Path __p, JavaManifest __man)
		throws InvalidProjectException, NullPointerException
	{
		super(__p);
		
		// Check
		if (__am == null || __man == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.appman = __am;
		this.sourcemanifest = __man;
		
		// Decode suite identifiers
		JavaManifestAttributes attr = __man.getMainAttributes();
		try
		{
			// {@squirreljme.error AT05 No application vendor was specified.}
			MidletSuiteVendor vend = new MidletSuiteVendor(
				Objects.<String>requireNonNull(
				attr.getValue("X-SquirrelJME-Vendor"), "AT05"));
			
			// {@squirreljme.error AT06 No application name was specified.}
			MidletSuiteName name = new MidletSuiteName(
				Objects.<String>requireNonNull(
				attr.getValue("X-SquirrelJME-Title"), "AT06"));
			
			// {@squirreljme.error AT07 No application version was specified.}
			MidletVersion vers = new MidletVersion(
				Objects.<String>requireNonNull(
				attr.getValue("X-SquirrelJME-Version"), "AT07"));
			
			// Finish it
			this.suiteid = new MidletSuiteID(vend, name, vers);
		}
		
		// {@squirreljme.error AT04 The given project at the specified path
		// does not have a correct suite name, vendor, or version. (The path to
		// the project)}
		catch (IllegalArgumentException|NullPointerException e)
		{
			throw new InvalidProjectException(String.format("AT04 %s", __p),
				e);
		}
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
		return this.suiteid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/24
	 */
	@Override
	public final String toString()
	{
		return this.suiteid.toString();
	}
}

