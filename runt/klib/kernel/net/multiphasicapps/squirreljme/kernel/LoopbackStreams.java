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
		
		/** Input stream. */
		private Reference<InputStream> _instream;
		
		/** Output stream. */
		private Reference<OutputStream> _outstream;
		
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
			Reference<InputStream> ref = this._instream;
			InputStream rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._instream = new WeakReference<>(
					(rv = new __Input__(this.in)));
			
			return rv;
		}
		
		/**
		 * Returns the output for this side.
		 *
		 * @return The side output.
		 * @since 2018/01/04
		 */
		public final OutputStream output()
		{
			Reference<OutputStream> ref = this._outstream;
			OutputStream rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._outstream = new WeakReference<>(
					(rv = new __Output__(this.out)));
			
			return rv;
		}
	}
	
	/**
	 * Input stream from the queue.
	 *
	 * @since 2018/01/04
	 */
	private static final class __Input__
		extends InputStream
	{
		/** The input source. */
		protected final ByteDeque in;
		
		/**
		 * Initializes the input.
		 *
		 * @param __in The input source.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/04
		 */
		private __Input__(ByteDeque __in)
			throws NullPointerException
		{
			if (__in == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public void close()
		{
			// Close has no effect
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public int available()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public int read()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public int read(byte[] __b, int __o, int __l)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Output stream to the queue.
	 *
	 * @since 2018/01/04
	 */
	private static final class __Output__
		extends OutputStream
	{
		/** The output source. */
		protected final ByteDeque out;
		
		/**
		 * Initializes the output.
		 *
		 * @param __out The output source.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/04
		 */
		private __Output__(ByteDeque __out)
			throws NullPointerException
		{
			if (__out == null)
				throw new NullPointerException("NARG");
			
			this.out = __out;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public void close()
		{
			// Close has no effect
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public void flush()
		{
			// Flushing just notifies the other thread that data might be
			// available
			ByteDeque out = this.out;
			synchronized (out)
			{
				out.notify();
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public void write(int __b)
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
		{
			throw new todo.TODO();
		}
	}
}

