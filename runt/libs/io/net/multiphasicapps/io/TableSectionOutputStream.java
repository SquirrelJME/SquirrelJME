// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * This is an output stream which writes to section tables, essentially a
 * number of various data chunks in the stream. All sections are ordered in
 * the order that they are created.
 *
 * This class is not thread safe.
 *
 * @since 2019/08/11
 */
public final class TableSectionOutputStream
{
	/** This represents a variable sized section. */
	public static final int VARIABLE_SIZE =
		-3219836;
	
	/** The sections which are available in the output. */
	private final List<Section> _sections =
		new LinkedList<>();
	
	/**
	 * Adds a section which is of a variable size.
	 *
	 * @return The resulting section.
	 * @since 2019/08/11
	 */
	public final Section addSection()
	{
		return this.addSection(VARIABLE_SIZE, 0);
	}
	
	/**
	 * Adds a section which is either of a variable size of a fixed size.
	 *
	 * @param __size The size of the section to use, if {@link #VARIABLE_SIZE}
	 * then the section will have a variable size.
	 * @return The section which was created for writing.
	 * @throws IllegalArgumentException If the size is zero or negative and
	 * is not the variable size.
	 * @since 2019/08/11
	 */
	public final Section addSection(int __size)
		throws IllegalArgumentException
	{
		return this.addSection(__size, 0);
	}
	
	/**
	 * Adds a section which is either of a variable size of a fixed size and
	 * one which has an alignment.
	 *
	 * @param __size The size of the section to use, if {@link #VARIABLE_SIZE}
	 * then the section will have a variable size.
	 * @param __align The alignment of the section, if the value is lower than
	 * {@code 1} it will be set to {@code 1}.
	 * @return The section which was created for writing.
	 * @throws IllegalArgumentException If the size is zero or negative and
	 * is not the variable size.
	 * @since 2019/08/11
	 */
	public final Section addSection(int __size, int __align)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BD3h Zero or negative size section. (The size)}
		if (__size != VARIABLE_SIZE && __size <= 0)
			throw new IllegalArgumentException("BD3h " + __size);
		
		// It does not make sense to align under a byte
		if (__align < 1)
			__align = 1;
		
		// Create section
		Section rv = new Section(__size, __align);
		
		// Add to our section list
		this._sections.add(rv);
		
		// And return this section
		return rv;
	}
	
	/**
	 * Returns a byte array representing the table file.
	 *
	 * @return The resulting byte array.
	 * @since 2019/08/11
	 */
	public final byte[] toByteArray()
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Record everything in
			this.writeTo(baos);
			
			// Return the resulting byte array
			return baos.toByteArray();
		}
		
		// {@squirreljme.error BD3i Could not create the byte array.}
		catch (IOException e)
		{
			throw new RuntimeException("BD3i", e);
		}
	}
	
	/**
	 * Writes the table file to the given output stream.
	 *
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/11
	 */
	public final void writeTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This represents a single section within the output.
	 *
	 * @since 2019/08/11
	 */
	public static final class Section
		extends OutputStream
		implements DataOutput
	{
		/** The fixed size of this section. */
		protected final int fixedsize;
		
		/** The alignment of this section. */
		protected final int alignment;
		
		/** Is this a variable size section? */
		protected final boolean isvariable;
		
		/**
		 * Initializes the written section.
		 *
		 * @param __size The size to use.
		 * @param __align The alignment to use.
		 * @throws IllegalArgumentException If the size is zero or negative.
		 * @since 2019/08/11
		 */
		private Section(int __size, int __align)
			throws IllegalArgumentException
		{
			// {@squirreljme.error BD3l Zero or negative size. (The size)}
			if (__size != VARIABLE_SIZE && __size <= 0)
				throw new IllegalArgumentException("BD3l " + __size);
			
			// Set
			this.fixedsize = __size;
			this.alignment = (__align >= 1 ? __align : 1);
			this.isvariable = (__size == VARIABLE_SIZE);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void close()
		{
			// Do nothing
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void flush()
		{
			// Do nothing
		}
		
		/**
		 * Returns the current written size of the section.
		 *
		 * @return The current section size.
		 * @since 2019/08/11
		 */
		public final int size()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void write(int __b)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void write(byte[] __b)
			throws IOException, NullPointerException
		{
			if (__b == null)
				throw new NullPointerException("NARG");
			
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void write(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			if (__b == null)
				throw new NullPointerException("NARG");
			if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
				throw new IndexOutOfBoundsException("IOOB");
			
			throw new todo.TODO();
		}
		
		/**
		 * Writes the address of the given section as an integer.
		 *
		 * @param __s The section to write the address of.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		public final void writeAddressInt(Section __s)
			throws IOException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			throw new todo.TODO();
		}
		
		/**
		 * Writes the address of the given section as a short.
		 *
		 * @param __s The section to write the address of.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		public final void writeAddressShort(Section __s)
			throws IOException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			throw new todo.TODO();
		}
		
		/**
		 * Writes padding which aligns to a given amount from within the
		 * data stream as itself.
		 *
		 * @param __n The number of bytes to align to.
		 * @return The number of alignment bytes written.
		 * @throws IllegalArgumentException If the requested alignment is
		 * negative.
		 * @throws IOException On write errors.
		 * @since 2019/08/11
		 */
		public final int writeAlignment(int __n)
			throws IllegalArgumentException, IOException
		{
			// {@squirreljme.error BD3k Cannot align to a negative amount.
			// (The alignment)}
			if (__n < 1)
				throw new IllegalArgumentException("BD3k " + __n);
			
			// Not point aligning to a byte
			if (__n == 1)
				return 0;
			
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeBoolean(boolean __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeByte(int __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeBytes(String __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeChar(int __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeChars(String __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeDouble(double __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeFloat(float __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeInt(int __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeLong(long __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * Writes the specified number of bytes as padding, the padding
		 * value is zero.
		 *
		 * @param __n The number of bytes to pad with.
		 * @since 2019/08/11
		 */
		public final void writePadding(int __n)
			throws IOException
		{
			this.writePadding(__n, 0);
		}
		
		/**
		 * Writes the specified number of bytes as padding.
		 *
		 * @param __n The number of bytes to pad with.
		 * @param __v The padding value to write.
		 * @throws IllegalArgumentException If the padding amount is negative.
		 * @throws IOException On write errors.
		 * @since 2019/08/11
		 */
		public final void writePadding(int __n, int __v)
			throws IllegalArgumentException, IOException
		{
			// {@squirreljme.error BD3j Negative padding. (The padding)}
			if (__n < 0)
				throw new IllegalArgumentException("BD3j " + __n);
			
			// Not writing anything, so ignore
			if (__n == 0)
				return;
			
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeShort(int __v)
			throws IOException
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeUTF(String __v)
			throws IOException
		{
			throw new todo.TODO();
		}
	}
}

