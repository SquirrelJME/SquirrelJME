// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This interface should be used by any class which is intended and capable
 * of being run in its own thread (via {@link Thread#Thread(Runnable)}.
 *
 * When used with {@link Thread}, the method {@link #run()} will be executed
 * when the thread is started.
 *
 * @since 2018/09/19
 */
@Api
public interface Runnable
{
	/**
	 * Performs any action that is required as needed.
	 *
	 * @since 2018/09/19
	 */
	@Api
	void run();
}

