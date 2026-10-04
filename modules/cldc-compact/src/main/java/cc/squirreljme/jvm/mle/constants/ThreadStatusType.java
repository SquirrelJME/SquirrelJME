// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * The status on what a {@link Thread} is doing.
 *
 * @since 2021/03/15
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ThreadStatusType
{
	/** Running. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte RUNNING =
		0;
	
	/** Sleeping. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SLEEPING =
		1;
	
	/** Waiting on a monitor. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MONITOR_WAIT =
		2;
}
