// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.palmos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * This is used to write to the record data.
 *
 * @since 2019/07/13
 */
final class __RecordWriter__
	extends OutputStream
{
	/** The type used. */
	protected final String type;
	
	/** The ID used. */
	protected final int id;
	
	/** The record data. */
	private final ByteArrayOutputStream _data =
		new ByteArrayOutputStream();
	
	/** The target record collection. */
	private final Collection<PalmRecord> _target;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the record writer.
	 *
	 * @param __type The record type.
	 * @param __id The record ID.
	 * @param __t The target type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/13
	 */
	__RecordWriter__(String __type, int __id, Collection<PalmRecord> __t)
		throws NullPointerException
	{
		if (__type == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.id = __id;
		this._target = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/13
	 */
	@Override
	public final void close()
	{
		// Only close once
		if (this._closed)
			return;
		
		// Set as closed
		this._closed = true;
		
		// Add record information
		byte[] buf = this._data.toByteArray();
		this._target.add(new PalmRecord(this.type, this.id,
			buf, 0, buf.length));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/13
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		// Do not write when closed
		if (this._closed)
			throw new IOException("EOFF");
		
		// Forward
		this._data.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/13
	 */
	@Override
	public final void write(byte[] __b)
		throws IOException
	{
		// Do not write when closed
		if (this._closed)
			throw new IOException("EOFF");
			
		// Forward
		this._data.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/13
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Do not write when closed
		if (this._closed)
			throw new IOException("EOFF");
			
		// Forward
		this._data.write(__b, __o, __l);
	}
}

