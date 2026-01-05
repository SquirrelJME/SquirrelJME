// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * A specific group of entries.
 *
 * @since 2025/12/06
 */
public enum SpecificGroup
{
	/** Java specific information. */
	JAVA("Java")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/12/06
		 */
		@Override
		public SpecificField[] fields()
		{
			return InfoJava.values();
		}
	},
	
	/** Operating system information. */
	OPERATING_SYSTEM("Operating System")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/12/06
		 */
		@Override
		public SpecificField[] fields()
		{
			return InfoOperatingSystem.values();
		}
	},
	
	/** Operating system information. */
	LANGUAGE("Language")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/12/06
		 */
		@Override
		public SpecificField[] fields()
		{
			return InfoLanguage.values();
		}
	},
	
	/* End. */
	;
	
	/** The title for this group. */
	protected final String title;
	
	/**
	 * Initializes the enum.
	 *
	 * @param __title The title for the group.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/06
	 */
	SpecificGroup(String __title)
		throws NullPointerException
	{
		if (__title == null)
			throw new NullPointerException("NARG");
		
		this.title = __title;
	}
	
	/**
	 * Returns the fields within the group.
	 *
	 * @return The group fields.
	 * @since 2025/12/06
	 */
	public abstract SpecificField[] fields();
}
