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
 * This represents a flag which may be associated with a suite.
 *
 * @since 2016/06/24
 */
@Api
public enum SuiteStateFlag
{
	/** Available. */
	@Api
	AVAILABLE,
	
	/** Enabled, the application or library may be used. */
	@Api
	ENABLED,
	
	/** The application or library is pre-installed with the system. */
	@Api
	PREINSTALLED,
	
	/** Remove is not supported. */
	@Api
	REMOVE_DENIED,
	
	/** A suite provided by the system. */
	@Api
	SYSTEM,
	
	/** The suite cannot be updated. */
	@Api
	UPDATE_DENIED,
	
	/** End. */
	;
}

