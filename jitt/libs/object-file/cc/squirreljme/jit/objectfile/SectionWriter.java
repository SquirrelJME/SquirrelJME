// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This class is used to write data to sections.
 *
 * @since 2018/02/24
 */
public final class SectionWriter
{
	/** The section to write to. */
	protected final Section section;
	
	/** Properties of the data being written. */
	protected final DataProperties dataproperties;
	
	/**
	 * Initializes the section writer.
	 *
	 * @param __s The section to write to.
	 * @param __dp The properties of the data to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	SectionWriter(Section __s, DataProperties __dp)
		throws NullPointerException
	{
		if (__s == null || __dp == null)
			throw new NullPointerException("NARG");
		
		this.section = __s;
		this.dataproperties = __dp;
	}
	
	/**
	 * Exports the given symbol by the given name.
	 *
	 * @param __name The name of the symbol.
	 * @param __scope The scope of the symbol.
	 * @return The exported symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/25
	 */
	public final ExportedSymbol exportSymbol(SymbolName __name,
		ExportedSymbol.Scope __scope)
		throws NullPointerException
	{
		if (__name == null || __scope == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes the specified byte value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writeByte(int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes the specified double value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writeDouble(double __v)
	{
		this.writeLong(Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * Writes the specified float value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writeFloat(float __v)
	{
		this.writeInteger(Float.floatToRawIntBits(__v));
	}
	
	/**
	 * Writes the specified integer value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writeInteger(int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes the specified long value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writeLong(long __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes the specified pointer value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writePointer(long __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes the specified short value.
	 *
	 * @param __v The value to write.
	 * @since 2018/02/25
	 */
	public final void writeShort(int __v)
	{
		throw new todo.TODO();
	}
}

