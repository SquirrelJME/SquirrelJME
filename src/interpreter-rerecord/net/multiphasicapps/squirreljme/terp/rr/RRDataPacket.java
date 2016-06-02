// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a data packet which is recorded and read from an input replay
 * stream.
 *
 * Once a data packet is closed, it may be reused for another command and as
 * such the objects must not be used for their original purpose after this
 * event.
 *
 * @since 2016/06/01
 */
public class RRDataPacket
	implements AutoCloseable
{
	/** The maximum command length. */
	public static final int MAX_FIELD_LENGTH =
		Short.MAX_VALUE;
	
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The owning data stream. */
	protected final RRDataStream stream;
	
	/** The objects for input and output. */
	private final List<Object> _data =
		new ArrayList<>();
	
	/** The command packet type. */
	private volatile RRDataCommand _command;
	
	/** The number of elements in the packet. */
	private volatile int _length;
	
	/** Is the packet closed? */
	private volatile boolean _closed =
		true;
	
	/**
	 * Initializes the data packet.
	 *
	 * @param __ds The owning data stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/01
	 */
	RRDataPacket(RRDataStream __ds)
	{
		// Check
		if (__ds == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.stream = __ds;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/01
	 */
	@Override
	public final void close()
	{
		// Lock
		synchronized (this.lock)
		{
			// If already closed, stop
			if (this._closed)
				return;
			
			// Set closed
			this._closed = true;
			
			// Mark closed in the stream
			this.stream.__close(this);
		}
	}
	
	/**
	 * Returns the data at the specified index.
	 *
	 * @param __i The index to get data for.
	 * @return The data at the specified index.
	 * @throws IllegalStateException If the packet is closed.
	 * @throws IndexOutOfBoundsException If the index it outside of bounds.
	 * @since 2016/06/01
	 */
	public final Object get(int __i)
		throws IllegalStateException, IndexOutOfBoundsException
	{
		synchronized (this.lock)
		{
			// {@squirreljme.error BC03 Cannot obtain data from the packet
			// because it is closed.}
			if (this._closed)
				throw new IllegalStateException("BC03");
			
			// {@squirreljme.error BC06 Index of field is not within the packet
			// bounds.}
			int c = this._length;
			if (__i < 0 || __i >= c)
				throw new IndexOutOfBoundsException("BC06");
		
			// Get size
			List<Object> data = this._data;
			int n = data.size();
		
			// If in range, use it
			if (__i < n)
				return data.get(__i);
		
			// Otherwise, treat as not set.
			return null;
		}
	}
	
	/**
	 * Returns the command that this is currrently using.
	 *
	 * @return The command this packet is using.
	 * @since 2016/06/01
	 */
	public final RRDataCommand getCommand()
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BC04 Cannot obtain the command from the
			// packet because it is closed.}
			if (this._closed)
				throw new IllegalStateException("BC04");
			
			return this._command;
		}
	}
	
	/**
	 * Returns the amount of data elements in the packet.
	 *
	 * @return The number of elements in the packet.
	 * @since 2016/06/01
	 */
	public final int length()
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BC08 Cannot obtain the length from the
			// packet because it is closed.}
			if (this._closed)
				throw new IllegalStateException("BC08");
			
			return this._length;
		}
	}
	
	/**
	 * Sets the value of a field in the command.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set it as.
	 * @return The old value.
	 * @throws ClassCastException If the value to set is not correct.
	 * @throws IndexOutOfBoundsException If the index to set is not within
	 * bounds.
	 * @since 2016/06/01
	 */
	public final Object set(int __i, Object __v)
		throws ClassCastException, IndexOutOfBoundsException
	{
		// {@squirreljme.error BC07 Cannot set the field of a data packet to
		// the specified class type. (The class of the argument)}
		if (__v != null &&
			!(__v instanceof String) && !(__v instanceof Boolean) &&
			!(__v instanceof Byte) && !(__v instanceof Short) &&
			!(__v instanceof Character) && !(__v instanceof Integer) &&
			!(__v instanceof Long) && !(__v instanceof Float) &&
			!(__v instanceof Double) && !(__v instanceof String[]) &&
			!(__v instanceof boolean[]) && !(__v instanceof byte[]) &&
			!(__v instanceof short[]) && !(__v instanceof char[]) &&
			!(__v instanceof int[]) && !(__v instanceof long[]) &&
			!(__v instanceof float[]) && !(__v instanceof double[]))
			throw new ClassCastException(String.format("BC07 %s",
				__v.getClass()));
		
		// Lock
		synchronized (this.lock)
		{
			// Get old
			Object rv = get(__i);
		
			// Get size
			List<Object> data = this._data;
			int n;
		
			// Too short? Pad with nulls
			while ((n = data.size()) <= __i)
				data.add(null);
		
			// In range? Set it
			if (__i < n)
				data.set(__i, __v);
		
			// Return old
			return rv;
		}
	}
	
	/**
	 * Clears the data packet and associates it with a new command and length.
	 *
	 * The packet is opened following this command.
	 *
	 * @param __cmd The command to use.
	 * @param __len The length of the command.
	 * @throws NullPointerException If no command was specified.
	 * @since 2016/06/01
	 */
	final void __clear(RRDataCommand __cmd, int __len)
		throws NullPointerException
	{
		// Check
		if (__cmd == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Could fail
			try
			{
				// Set as open
				this._closed = false;
			
				// Change it
				this._command = __cmd;
		
				// Set length to the given length
				__setLength(__len);
		
				// Clear all data fields
				List<Object> data = this._data;
				int n = data.size();
				for (int i = 0; i < n; i++)
					data.set(i, null);
			}
			
			// Could not set some detail (perhaps out of memory) so mark
			// as closed and rethrow the exception
			catch (RuntimeException|Error e)
			{
				this._closed = true;
				throw e;
			}
		}
	}
	
	/**
	 * Sets the length of the data packet.
	 *
	 * @param __i The length to use.
	 * @throws IllegalArgumentException If the length is negative or exceeds
	 * the maximum packet size.
	 * @since 2016/06/01
	 */
	final void __setLength(int __i)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BC05 The length of a command cannot be
		// negative or greater than the maximum command length limit.}
		if (__i < 0 || __i >= MAX_FIELD_LENGTH)
			throw new IllegalArgumentException("BC05");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BC09 Cannot set the length of the
			// packet because it is closed.}
			if (this._closed)
				throw new IllegalStateException("BC09");
			
			// Set
			this._length = __i;
		
			// Clear all data after the length
			List<Object> data = this._data;
			int n = data.size();
			for (int i = 0; i < n; i++)
				data.set(i, null);
		}
	}
}

