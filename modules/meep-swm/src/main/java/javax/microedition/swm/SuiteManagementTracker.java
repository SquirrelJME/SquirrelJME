// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is used to track the the progress of a suite that is currently
 * being installed.
 *
 * @since 2016/06/24
 */
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
@Api
public abstract class SuiteManagementTracker
{
	/**
	 * Prevents implicit instantiation of trackers.
	 *
	 * @since 2016/06/24
	 */
	@Api
	protected SuiteManagementTracker()
	{
	}
	
	/**
	 * Returns the suite that this tracker is assigned to or {@code null} if it
	 * is not install yet.
	 *
	 * @return The suite this tracker is assigned to or {@code null} if it
	 * has not yet been installed.
	 * @since 2016/06/24
	 */
	@Api
	public abstract Suite getSuite();
}

