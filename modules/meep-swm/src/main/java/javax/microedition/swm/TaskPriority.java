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
 * This represents the priority of a task.
 *
 * @since 2016/06/24
 */
@Api
public enum TaskPriority
{
	/** Maximum priority. */
	@Api
	MAX,
	
	/** Minimum priority. */
	@Api
	MIN,
	
	/** Normal priority. */
	@Api
	NORM,
	
	/** End. */
	;
}

