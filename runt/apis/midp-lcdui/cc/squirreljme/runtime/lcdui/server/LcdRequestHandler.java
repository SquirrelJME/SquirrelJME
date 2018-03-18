// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

/**
 * This is the handler for any requests that are made to the LCD server, this
 * enables client threads to call into the server from any thread while the
 * GUI remains in a single thread at all time.
 *
 * It is up to the implementation to properly thread in these events as they
 * are used.
 *
 * @since 2018/03/17
 */
public interface LcdRequestHandler
{
	/**
	 * Invokes the specified request at a future time within the thread which
	 * is not specifically known.
	 *
	 * @param __r The request to handle.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public abstract void invokeLater(LcdRequest __r)
		throws NullPointerException;
	
	/**
	 * Invokes the specified request as soon as possible and blocks until
	 * execution has finished.
	 *
	 * @param <R> The class type to return.
	 * @param __cl The class type to return.
	 * @param __r The request to handle.
	 * @return The result of the request;
	 * @throws InterruptedException If the thread was interrupted while it
	 * was waiting.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public abstract <R> R invokeNow(Class<R> __cl, LcdRequest __r)
		throws InterruptedException, NullPointerException;
}

