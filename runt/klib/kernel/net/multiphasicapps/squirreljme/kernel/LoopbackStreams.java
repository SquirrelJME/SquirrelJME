// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is a set of streams which loop into each other, this allows for
 * communication where one source of streams writes into the stream of the
 * other side.
 *
 * @since 2018/01/04
 */
public final class LoopbackStreams
{
	/**
	 * Returns side A.
	 *
	 * @return Side A.
	 * @since 2018/01/04
	 */
	public final Side sideA()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns side B.
	 *
	 * @return Side B.
	 * @since 2018/01/04
	 */
	public final Side sideB()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This represents the side of the loopback stream.
	 *
	 * @since 2018/01/04
	 */
	public final class Side
	{
		/**
		 * Initializes the side.
		 *
		 * @since 2018/01/04
		 */
		private Side()
		{
		}
		
		/**
		 * Returns the input for this side.
		 *
		 * @return The side input.
		 * @since 2018/01/04
		 */
		public final InputStream input()
		{
			throw new todo.TODO();
		}
		
		/**
		 * Returns the output for this side.
		 *
		 * @return The side output.
		 * @since 2018/01/04
		 */
		public final OutputStream output()
		{
			throw new todo.TODO();
		}
	}
}

