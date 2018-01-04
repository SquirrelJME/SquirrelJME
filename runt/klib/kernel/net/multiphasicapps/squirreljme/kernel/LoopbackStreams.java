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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.io.InputStream;
import java.io.OutputStream;
import net.multiphasicapps.util.bytedeque.ByteDeque;

/**
 * This is a set of streams which loop into each other, this allows for
 * communication where one source of streams writes into the stream of the
 * other side.
 *
 * @since 2018/01/04
 */
public final class LoopbackStreams
{
	/** A double deque is used from one side to another. */
	private final ByteDeque[] _queues =
		new ByteDeque[]{new ByteDeque(), new ByteDeque()};
	
	/** Side A reference. */
	private volatile Reference<Side> _a;
	
	/** Side B reference. */
	private volatile Reference<Side> _b;
	
	/**
	 * Initializes the loop back streams.
	 *
	 * @since 2018/01/04
	 */
	public LoopbackStreams()
	{
	}
	
	/**
	 * Returns side A.
	 *
	 * @return Side A.
	 * @since 2018/01/04
	 */
	public final Side sideA()
	{
		Reference<Side> ref = this._a;
		Side rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			ByteDeque[] queues = this._queues;
			this._a = new WeakReference<>(
				(rv = new Side(queues[0], queues[1])));
		}
		
		return rv;
	}
	
	/**
	 * Returns side B.
	 *
	 * @return Side B.
	 * @since 2018/01/04
	 */
	public final Side sideB()
	{
		Reference<Side> ref = this._b;
		Side rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			ByteDeque[] queues = this._queues;
			this._b = new WeakReference<>(
				(rv = new Side(queues[1], queues[0])));
		}
		
		return rv;
	}
	
	/**
	 * This represents the side of the loopback stream.
	 *
	 * @since 2018/01/04
	 */
	public static final class Side
	{
		/** The input queue. */
		protected final ByteDeque in;
		
		/** The output queue. */
		protected final ByteDeque out;
		
		/**
		 * Initializes the side.
		 *
		 * @param __in The input queue.
		 * @param __out The output queue.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/04
		 */
		private Side(ByteDeque __in, ByteDeque __out)
			throws NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
			this.out = __out;
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

