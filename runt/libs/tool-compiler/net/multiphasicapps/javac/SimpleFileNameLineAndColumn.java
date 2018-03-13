// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This contains a simple representation of the file, line, and column
 * information.
 *
 * This class is immutable.
 *
 * @since 2018/03/12
 */
public final class SimpleFileNameLineAndColumn
	implements FileNameLineAndColumn
{
	/** The file name. */
	protected final String filename;
	
	/** The line. */
	protected final int line;
	
	/** The column. */
	protected final int column;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes this class with unknown information.
	 *
	 * @since 2018/03/12
	 */
	public SimpleFileNameLineAndColumn()
	{
		this.filename = null;
		this.line = -1;
		this.column = -1;
	}
	
	/**
	 * Initializes with all of the given information.
	 *
	 * @param __fn The file name.
	 * @param __lc The line and column information.
	 * @since 2018/03/12
	 */
	public SimpleFileNameLineAndColumn(FileName __fn, LineAndColumn __lc)
	{
		this.filename = (__fn == null ? null : __fn.fileName());
		this.line = (__lc == null ? -1 : __lc.line());
		this.column = (__lc == null ? -1 : __lc.column());
	}
	
	/**
	 * Initializes with all of the given information.
	 *
	 * @param __fn The file name.
	 * @param __lin The line.
	 * @param __col The column.
	 * @since 2018/03/12
	 */
	public SimpleFileNameLineAndColumn(FileName __fn, int __lin, int __col)
	{
		this.filename = (__fn == null ? null : __fn.fileName());
		this.line = __lin;
		this.column = __col;
	}
	
	/**
	 * Initializes with all of the given information.
	 *
	 * @param __flc The file name, line, and column information.
	 * @since 2018/03/12
	 */
	public SimpleFileNameLineAndColumn(FileNameLineAndColumn __flc)
	{
		if (__flc == null)
		{
			this.filename = null;
			this.line = -1;
			this.column = -1;
		}
		else
		{
			this.filename = __fn;
			this.line = __lin;
			this.column = __col;
		}
	}
	
	/**
	 * Initializes this class with invalid information.
	 *
	 * @since 2018/03/12
	 */
	public SimpleFileNameLineAndColumn()
	{
		this(null, -1, -1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int column()
	{
		return this.column;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof SimpleFileNameLineAndColumn))
			return false;
		
		SimpleFileNameLineAndColumn o = (SimpleFileNameLineAndColumn)__o;
		return Objects.equals(this.filename, o.filename) &&
			this.line == o.line &&
			this.column == o.column;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		return this.filename;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int hashCode()
	{
		return Objects.hashCode(this.filename) ^
			this.line ^
			(this.column << 16);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		return this.line;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				String.format("%s:%d,%d", this.filename, this.line,
					this.column)));
		
		return rv;
	}
}

