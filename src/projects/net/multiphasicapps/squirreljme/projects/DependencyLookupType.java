// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

/**
 * This represents the dependency family to use.
 *
 * @since 2016/10/11
 */
public enum DependencyLookupType
{
	/** Use internal ones only (runs on SquirrelJME). */
	INTERNAL,
	
	/** Use external ones only (runs on Java SE/bootstrap). */
	EXTERNAL,
	
	/** End. */
	;
	
	/**
	 * Returns the dependency types used for optional dependencies.
	 *
	 * @return The optional dependency types.
	 * @since 2016/10/11
	 */
	public final DependencyType[] optional()
	{
		// Depends
		switch (this)
		{
				// Internal?
			case INTERNAL:
				return new DependencyType[]
					{
						DependencyType.OPTIONAL
					};
				
				// External?
			case EXTERNAL:
				return new DependencyType[]
					{
						DependencyType.OPTIONAL,
						DependencyType.EXTERNAL
					};
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the dependency types used for required dependencies.
	 *
	 * @return The required dependency types.
	 * @since 2016/10/11
	 */
	public final DependencyType[] required()
	{
		// Depends
		switch (this)
		{
				// Internal?
			case INTERNAL:
				return new DependencyType[]
					{
						DependencyType.REQUIRED,
						DependencyType.INTERNAL
					};
				
				// External?
			case EXTERNAL:
				return new DependencyType[]
					{
						DependencyType.REQUIRED
					};
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

