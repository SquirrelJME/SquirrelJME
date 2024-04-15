// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.IOException;

/**
 * This interface is used to signal changes in the HTTP state such as
 * when a connection should be made.
 *
 * @since 2019/05/13
 */
public interface HTTPSignalListener
{
	/**
	 * Tells the other end that the HTTP request data is ready.
	 *
	 * @param __data The data of the request.
	 * @throws IOException If the request could not be sent.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	void requestReady(byte[] __data)
		throws IOException, NullPointerException;
}

