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
 * This represents the state that a suite is in.
 *
 * @since 2016/06/24
 */
@Api
public enum SuiteState
{
	/** Installation failed. */
	@Api
	INSTALLATION_FAILED,
	
	/** Installed. */
	@Api
	INSTALLED,
	
	/** Currently being installed. */
	@Api
	INSTALLING,
	
	/** Removed. */
	@Api
	REMOVED,
	
	/** End. */
	;
}

