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
import java.io.DataOutputStream;
import java.lang.ref.Reference;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
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
		Integer.MIN_VALUE;
	
	/** The sections which are available in the output. */
	private final List<Section> _sections =
		new LinkedList<>();
	
	/** Are the section informations dirty? */
	private final __Dirty__ _dirty =
		new __Dirty__();
	
	/** The current file size. */
	private int _filesize;
	
	/**
	 * Adds a section which is of a variable size.
	 *
	 * @return The resulting section.
	 * @since 2019/08/11
	 */
	public final TableSectionOutputStream.Section addSection()
	{
		return this.addSection(TableSectionOutputStream.VARIABLE_SIZE, 1);
	}
	
	/**
	 * Adds a section which consists of the given byte array.
	 *
	 * @param __bytes The byte array to initialize as.
	 * @return The resulting section.
	 * @throws IOException If the section could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/11
	 */
	public final TableSectionOutputStream.Section addSection(byte[] __bytes)
		throws IOException, NullPointerException
	{
		return this.addSection(__bytes, 1);
	}
	
	/**
	 * Adds a section which consists of the given byte array using the given
	 * alignment.
	 *
	 * @param __bytes The byte array to initialize as.
	 * @param __align The alignment to use.
	 * @return The resulting section.
	 * @throws IllegalArgumentException If the size is zero or negative and
	 * is not the variable size, or the alignment is below one.
	 * @throws IOException If the section could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/11
	 */
	public final TableSectionOutputStream.Section addSection(byte[] __bytes,
		int __align)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__bytes == null)
			throw new NullPointerException("NARG");
		
		// Add new section
		Section rv = this.addSection(__bytes.length, __align);
		
		// Write all the bytes into it
		rv.write(__bytes);
		
		// Use this section
		return rv;
	}
	
	/**
	 * Adds a section which is either of a variable size of a fixed size.
	 *
	 * @param __size The size of the section to use, if {@link #VARIABLE_SIZE}
	 * then the section will have a variable size.
	 * @return The section which was created for writing.
	 * @throws IllegalArgumentException If the size is zero or negative and
	 * is not the variable size, or the alignment is below one.
	 * @since 2019/08/11
	 */
	public final TableSectionOutputStream.Section addSection(int __size)
		throws IllegalArgumentException
	{
		return this.addSection(__size, 1);
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
	 * is not the variable size, or the alignment is below one.
	 * @since 2019/08/11
	 */
	public final TableSectionOutputStream.Section addSection(int __size,
		int __align)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BD3h Zero or negative size section. (The size)}
		if (__size != TableSectionOutputStream.VARIABLE_SIZE && __size <= 0)
			throw new IllegalArgumentException("BD3h " + __size);
		
		// {@squirreljme.error BD3q Zero or negative alignment.
		// (The alignment)}
		if (__align < 1)
			throw new IllegalArgumentException("BD3q " + __align);
		
		// Create section
		Section rv = new Section(__size, __align, this._dirty);
		
		// Add to our section list
		this._sections.add(rv);
		
		// Becomes dirty because new section was added
		this._dirty._dirty = true;
		
		// And return this section
		return rv;
	}
	
	/**
	 * Returns the current size of the file.
	 *
	 * @return The file size.
	 * @since 2019/08/25
	 */
	public final int fileSize()
	{
		this.__undirty();
		return this._filesize;
	}
	
	/**
	 * Returns the address of the given section.
	 *
	 * @param __s The section.
	 * @return The address of the section.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/25
	 */
	public final int sectionAddress(Section __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.__undirty();
		return __s._writeaddress;
	}
	
	/**
	 * Returns the size of the given section.
	 *
	 * @param __s The section.
	 * @return The size of the section.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/25
	 */
	public final int sectionSize(Section __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.__undirty();
		return __s._writesize;
	}
	
	/**
	 * Returns a byte array representing the table file.
	 *
	 * @return The resulting byte array.
	 * @since 2019/08/11
	 */
	public final byte[] toByteArray()
	{
		// Setup output byte array which has a base size for the size of the
		// file
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
			this.fileSize()))
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
		
		// The current write pointer
		int writeptr = 0;
		
		// Undirty and get the file size
		this.__undirty();
		int filesize = this._filesize;
		
		// Write each individual section to the output stream
		List<Section> sections = this._sections;
		for (int i = 0, n = sections.size(); i < n; i++)
		{
			Section section = sections.get(i);
			
			// Get properties of the section
			byte[] data = section._data;
			int cursize = section._size,
				writeaddress = section._writeaddress,
				writesize = section._writesize,
				writeendaddress = writeaddress + writesize,
				actualwrite = (cursize < writesize ? cursize : writesize);
			
			// Write padding until we reach our needed address
			while (writeptr < writeaddress)
			{
				__os.write(0);
				writeptr++;
			}
			
			// Rewrites must be processed to figure out how to refer to
			// other section aliases!
			for (__Rewrite__ rewrite : section._rewrites)
			{
				// The target section if any
				Reference<TableSectionOutputStream.Section> refsection =
					rewrite._section;
				
				// Determine the value that is to be written
				int value = 0;
				switch (rewrite._value)
				{
						// Address of section
					case ADDRESS:
						if (refsection != null)
							value = refsection.get()._writeaddress;
						break;
						
						// Size of section or file.
					case SIZE:
						if (refsection == null)
							value = filesize;
						else
							value = refsection.get()._writesize;
						break;
				}
				
				// Add offset of value
				value += rewrite._offset;
				
				// Perform the actual rewrite
				int paddr = rewrite._address;
				switch (rewrite._type)
				{
					case SHORT:
						data[paddr] = (byte)(value >>> 8);
						data[paddr + 1] = (byte)(value);
						break;
					
					case INTEGER:
						data[paddr] = (byte)(value >>> 24);
						data[paddr + 1] = (byte)(value >>> 16);
						data[paddr + 2] = (byte)(value >>> 8);
						data[paddr + 3] = (byte)(value);
						break;
				}
			}
			
			// Write the section data as an entire chunk, note that this
			// could be a fixed size section with a short buffer!
			__os.write(data, 0, actualwrite);
			writeptr += actualwrite;
			
			// Write padding until we reach our ending address
			while (writeptr < writeendaddress)
			{
				__os.write(0);
				writeptr++;
			}
		}
	}
	
	/**
	 * Undirties and calculations all the section layout and information.
	 *
	 * @since 2019/08/25
	 */
	private final void __undirty()
	{
		// There is no need to calculate if this is not dirty at all
		__Dirty__ dirty = this._dirty;
		if (!dirty._dirty)
			return;
		
		// Our current file size
		int filesize = 0;
		
		// We must go through all of the sections, perform their required
		// alignment while additionally calculating their addresses within
		// the file for section references.
		List<Section> sections = this._sections;
		for (int i = 0, n = sections.size(); i < n; i++)
		{
			Section section = sections.get(i);
			
			// Perform alignment of this section
			if ((filesize % section.alignment) != 0)
				filesize += section.alignment - (filesize % section.alignment);
			
			// Section is addressed here
			section._writeaddress = filesize;
			
			// Move the current file size up by the section's size
			int writesize = (section.isvariable ?
				section._size : section.fixedsize);
			filesize += writesize;
			section._writesize = writesize;
		}
		
		// Store file size
		this._filesize = filesize;
		
		// Clear dirty flag
		dirty._dirty = false;
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
		/** The size of the bufer. */
		private static final int _BUFFER_SIZE =
			512;
		
		/** The fixed size of this section. */
		protected final int fixedsize;
		
		/** The alignment of this section. */
		protected final int alignment;
		
		/** Is this a variable size section? */
		protected final boolean isvariable;
		
		/** Data rewrites which are possible. */
		private final List<__Rewrite__> _rewrites =
			new LinkedList<>();
		
		/** The tracker for the dirtiness. */
		private final __Dirty__ _dirty;
		
		/** The byte buffer data. */
		private byte[] _data;
		
		/** The current size of the section. */
		private int _size;
		
		/** The write address of this section. */
		private int _writeaddress =
			-1;
		
		/** The write size of this section. */
		private int _writesize =
			-1;
		
		/**
		 * Initializes the written section.
		 *
		 * @param __size The size to use.
		 * @param __align The alignment to use.
		 * @param __d The dirty flag.
		 * @throws IllegalArgumentException If the size is zero or negative.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		private Section(int __size, int __align, __Dirty__ __d)
			throws IllegalArgumentException, NullPointerException
		{
			if (__d == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error BD3l Zero or negative size. (The size)}
			if (__size != TableSectionOutputStream.VARIABLE_SIZE && __size <= 0)
				throw new IllegalArgumentException("BD3l " + __size);
			
			// Set
			this.fixedsize = __size;
			this.alignment = (__align >= 1 ? __align : 1);
			this.isvariable = (__size == TableSectionOutputStream.VARIABLE_SIZE);
			
			// Dirty flag storage
			this._dirty = __d;
			
			// If this is a fixed size section, we never have to expand it
			// so we can allocate all the needed data!
			if (__size != TableSectionOutputStream.VARIABLE_SIZE)
				this._data = new byte[__size];
			else
				this._data = new byte[Section._BUFFER_SIZE];
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
			return this._size;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void write(int __b)
			throws IOException
		{
			// {@squirreljme.error BD3m Size of section exceeded. (The size
			// of the section)}
			int size = this._size;
			if (!this.isvariable && size + 1 > this.fixedsize)
				throw new IOException("BD3m " + size);
			
			// Possibly resize the data array, only when variable
			byte[] data = this._data;
			if (this.isvariable && size >= data.length)
			{
				data = Arrays.copyOf(data, size + Section._BUFFER_SIZE);
				this._data = data;
			}
			
			// Write into the data
			data[size] = (byte)__b;
			
			// Size up
			this._size = size + 1;
			
			// Becomes dirty
			this._dirty._dirty = true;
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
			
			this.write(__b, 0, __b.length);
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
			
			// {@squirreljme.error BD3p Size of section exceeded.}
			int size = this._size;
			if (!this.isvariable && size + __l > this.fixedsize)
				throw new IOException("BD3p");
			
			// Possibly resize the data array (only when variable)
			byte[] data = this._data;
			if (this.isvariable && size + __l >= data.length)
			{
				data = Arrays.copyOf(data,
					size + (__l < Section._BUFFER_SIZE ? Section._BUFFER_SIZE : __l));
				this._data = data;
			}
			
			// Write into the data
			for (int i = 0; i < __l; i++)
				data[size++] = __b[__o++];
			
			// Size up
			this._size = size;
			
			// Becomes dirty
			this._dirty._dirty = true;
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
			this.write(__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeBytes(String __v)
			throws IOException, NullPointerException
		{
			if (__v == null)
				throw new NullPointerException("NARG");
			
			for (int i = 0, n = __v.length(); i < n; i++)
				this.write(__v.charAt(i));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeChar(int __v)
			throws IOException
		{
			this.writeShort(__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeChars(String __v)
			throws IOException, NullPointerException
		{
			if (__v == null)
				throw new NullPointerException("NARG");
			
			for (int i = 0, n = __v.length(); i < n; i++)
			{
				char c = __v.charAt(i);
				
				this.write(c >> 8);
				this.write(c);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeDouble(double __v)
			throws IOException
		{
			this.writeLong(Double.doubleToLongBits(__v));
		}
		
		/**
		 * Writes the size of the file as an integer.
		 *
		 * @throws IOException On write errors.
		 * @since 2019/08/11
		 */
		public final void writeFileSizeInt()
			throws IOException
		{
			// Record rewrite
			this._rewrites.add(new __Rewrite__(this._size,
				__RewriteType__.INTEGER, __RewriteValue__.SIZE, 0, null));
				
			// Place padding
			this.writeInt(0);
		}
		
		/**
		 * Writes the size of the file as a short.
		 *
		 * @throws IOException On write errors.
		 * @since 2019/08/11
		 */
		public final void writeFileSizeShort()
			throws IOException
		{
			// Record rewrite
			this._rewrites.add(new __Rewrite__(this._size,
				__RewriteType__.SHORT, __RewriteValue__.SIZE, 0, null));
				
			// Place padding
			this.writeShort(0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeFloat(float __v)
			throws IOException
		{
			this.writeInt(Float.floatToIntBits(__v));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeInt(int __v)
			throws IOException
		{
			this.write(__v >> 24);
			this.write(__v >> 16);
			this.write(__v >> 8);
			this.write(__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeLong(long __v)
			throws IOException
		{
			this.write((int)(__v >> 56));
			this.write((int)(__v >> 48));
			this.write((int)(__v >> 40));
			this.write((int)(__v >> 32));
			this.write((int)(__v >> 24));
			this.write((int)(__v >> 16));
			this.write((int)(__v >> 8));
			this.write((int)(__v));
		}
		
		/**
		 * Writes the specified number of bytes as padding, the padding
		 * value is zero.
		 *
		 * @param __n The number of bytes to pad with.
		 * @throws IOException On write errors.
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
			
			// Write the padding
			for (int i = 0; i < __n; i++)
				this.write(__v);
		}
		
		/**
		 * Writes the address of the given section as an integer.
		 *
		 * @param __s The section to write the address of.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/24
		 */
		public final void writeSectionAddressInt(Section __s)
			throws IOException, NullPointerException
		{
			this.writeSectionAddressInt(__s, 0);
		}
		
		/**
		 * Writes the address of the given section as an integer.
		 *
		 * @param __s The section to write the address of.
		 * @param __o The offset to use.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		public final void writeSectionAddressInt(Section __s, int __o)
			throws IOException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Record rewrite
			this._rewrites.add(new __Rewrite__(this._size,
				__RewriteType__.INTEGER, __RewriteValue__.ADDRESS, __o, __s));
				
			// Place padding
			this.writeInt(0);
		}
		
		/**
		 * Writes the address of the given section as a short.
		 *
		 * @param __s The section to write the address of.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/24
		 */
		public final void writeSectionAddressShort(Section __s)
			throws IOException, NullPointerException
		{
			this.writeSectionAddressShort(__s, 0);
		}
		
		/**
		 * Writes the address of the given section as a short.
		 *
		 * @param __s The section to write the address of.
		 * @param __o The offset value.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		public final void writeSectionAddressShort(Section __s, int __o)
			throws IOException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Record rewrite
			this._rewrites.add(new __Rewrite__(this._size,
				__RewriteType__.SHORT, __RewriteValue__.ADDRESS, __o, __s));
				
			// Place padding
			this.writeShort(0);
		}
		
		/**
		 * Writes the size of the given section as an integer.
		 *
		 * @param __s The section and its size to write.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		public final void writeSectionSizeInt(Section __s)
			throws IOException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Record rewrite
			this._rewrites.add(new __Rewrite__(this._size,
				__RewriteType__.INTEGER, __RewriteValue__.SIZE, 0, __s));
				
			// Place padding
			this.writeInt(0);
		}
		
		/**
		 * Writes the size of the given section as a short.
		 *
		 * @param __s The section and its size to write.
		 * @throws IOException On write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/08/11
		 */
		public final void writeSectionSizeShort(Section __s)
			throws IOException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Record rewrite
			this._rewrites.add(new __Rewrite__(this._size,
				__RewriteType__.SHORT, __RewriteValue__.SIZE, 0, __s));
				
			// Place padding
			this.writeShort(0);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeShort(int __v)
			throws IOException
		{
			this.write(__v >> 8);
			this.write(__v);
		}
		
		/**
		 * Writes the specified short value and checks to make sure it is
		 * within the range of a short.
		 *
		 * @param __v The value to write.
		 * @throws IOException If the short could not be written or it
		 * exceeds the range of a short value.
		 * @since 2019/08/11
		 */
		public final void writeShortChecked(int __v)
			throws IOException
		{
			// {@squirreljme.error BD3o Signed short value out of range.
			// (The value)}
			if (__v < -32768 || __v > 32767)
				throw new IOException("BD3o " + __v);
			
			this.write(__v >> 8);
			this.write(__v);
		}
		
		/**
		 * Writes the specified unsigned short value and checks to make sure it
		 * is within the range of an unsigned short.
		 *
		 * @param __v The value to write.
		 * @throws IOException If the unsigned short could not be written or it
		 * exceeds the range of an unsigned short value.
		 * @since 2019/08/11
		 */
		public final void writeUnsignedShortChecked(int __v)
			throws IOException
		{
			// {@squirreljme.error BD3n Unsigned short value out of range.
			// (The value)}
			if (__v < 0 || __v > 65535)
				throw new IOException("BD3n " + __v);
			
			this.write(__v >> 8);
			this.write(__v);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/08/11
		 */
		@Override
		public final void writeUTF(String __v)
			throws IOException, NullPointerException
		{
			if (__v == null)
				throw new NullPointerException("NARG");
			
			// Write into a buffer
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
				2 + (__v.length() * 2));
				DataOutputStream dos = new DataOutputStream(baos))
			{
				// Write data
				dos.writeUTF(__v);
				
				// Dump to this internally
				baos.writeTo(this);
			}
		}
	}
	
	/**
	 * Are the addresses and sizes considered dirty?
	 *
	 * @since 2019/08/25
	 */
	static final class __Dirty__
	{
		/** Flag used to store the dirty state. */
		volatile boolean _dirty;
	}
}

