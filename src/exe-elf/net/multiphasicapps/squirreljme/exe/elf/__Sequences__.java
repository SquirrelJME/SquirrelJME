// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;

/**
 * This contains the sequences which are used to output the specified ELF.
 *
 * @since 2016/08/16
 */
class __Sequences__
	implements Iterable<__Sequences__.__Sequence__>
{
	/** The owning ELF. */
	protected final ELFOutput owner;

	/** The sequence list. */
	protected final List<__Sequence__> seq =
		new ArrayList<>();
	
	/** The elf header. */
	protected final __Header__ header;
	
	/** The program headers. */
	protected final __Programs__ programs;
	
	/** The section headers. */
	protected final __Sections__ sections;
	
	/**
	 * Makes the write sequence list.
	 *
	 * @param __eo The output ELF to sequence.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	__Sequences__(ELFOutput __eo)
		throws NullPointerException
	{
		// Check
		if (__eo == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __eo;
		
		// The target list
		List<__Sequence__> rv = this.seq;
	
		// Identification
		rv.add(new __Identification__());
		
		// The header
		__Header__ h = new __Header__();
		this.header = h;
		rv.add(h);
		
		if (true)
			throw new Error("TODO");
		
		// Initialize program headers
		__Programs__ programs = new __Programs__();
		this.programs = programs;
		rv.add(programs);
		
		// Initialize sections
		__Sections__ sections = new __Sections__();
		this.sections = sections;
		rv.add(sections);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/16
	 */
	@Override
	public Iterator<__Sequences__.__Sequence__> iterator()
	{
		return this.seq.iterator();
	}
	
	/**
	 * This represents a single sequence which is used to determine how an ELF
	 * is to be written.
	 *
	 * @since 2016/08/16
	 */
	abstract class __Sequence__
	{
		/** The position where this sequence is located. */
		volatile int _at =
			-1;
	
		/** The size of the sequence. */
		volatile int _size =
			-1;
	
		/**
		 * Internal only.
		 *
		 * @since 2016/08/16
		 */
		private __Sequence__()
		{
		}
		
		/**
		 * Writes the given sequence.
		 *
		 * @param __dos The sequence to write.
		 * @throws IOException On write errors.
		 * @since 2016/08/16
		 */
		abstract void __write(ExtendedDataOutputStream __dos)
			throws IOException;
	}
	
	/**
	 * The header contains more details about the ELF.
	 *
	 * @since 2016/08/16
	 */
	final class __Header__
		extends __Sequence__
	{
		/** The entry point of the ELF. */
		volatile long _entrypoint;
		
		/** The position of the program header. */
		volatile int _pheadoff;
		
		/** The position of the section header. */
		
		/**
		 * Initializes the header.
		 *
		 * @since 2016/08/16
		 */
		private __Header__()
		{
			// Starts after the identification
			this._at = 16;
			
			// The size depends on the bit count
			switch (__Sequences__.this.owner._wordsize)
			{
					// 32-bit
				case 32:
					this._size = 52;
					break;
			
					// 64-bit
				case 64:
					this._size = 64;
					break;
			
					// Default
				default:
					throw new RuntimeException("OOPS");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/16
		 */
		@Override
		void __write(ExtendedDataOutputStream __dos)
			throws IOException
		{
			// Get
			ELFOutput eo = __Sequences__.this.owner;
			int bits = eo._wordsize;
			__Programs__ programs = __Sequences__.this.programs;
			__Sections__ sections = __Sequences__.this.sections;
			
			// Write the elf type
			__dos.writeShort(eo._type.identifier());
			
			// And the machine
			__dos.writeShort(eo._machine);
			
			// Only version 1 of ELF is written
			__dos.writeInt(1);
			
			// Write the entry point, program header offset, and program header
			// size
			long entrypoint = this._entrypoint;
			int phoff = programs._at, shoff = sections._at;
			switch (bits)
			{
					// 32-bit
				case 32:
					__dos.writeInt((int)entrypoint);
					__dos.writeInt(phoff);
					__dos.writeInt(shoff);
					break;
					
					// 64-bit
				case 64:
					__dos.writeLong(entrypoint);
					__dos.writeLong(phoff & 0xFFFF_FFFFL);
					__dos.writeLong(shoff & 0xFFFF_FFFFL);
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Write flags
			__dos.writeInt(eo._flags);
			
			// Write program header size and count
			__dos.writeShort(programs._entsize);
			__dos.writeShort(programs._entcount);
			
			// And for the section headers also
			__dos.writeShort(sections._entsize);
			__dos.writeShort(sections._entcount);
			
			// The section which contains the strings
			__dos.writeShort(sections._stringsect);
		}
	}
	
	/**
	 * The ELF identification header.
	 *
	 * @since 2016/08/16
	 */
	final class __Identification__
		extends __Sequence__
	{
		/**
		 * Internal only.
		 *
		 * @since 2016/08/16
		 */
		private __Identification__()
		{
			// Always at the start and the same size
			this._at = 0;
			this._size = 16;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/16
		 */
		@Override
		void __write(ExtendedDataOutputStream __dos)
			throws IOException
		{
			// Get the ELF
			ELFOutput eo = __Sequences__.this.owner;
			int bits = eo._wordsize;
			
			// Magic number
			__dos.writeByte(0x7F);
			__dos.writeByte('E');
			__dos.writeByte('L');
			__dos.writeByte('F');
			
			// Write the ELF class
			switch (bits)
			{
					// 32-bit
				case 32:
					__dos.writeByte(1);
					break;
					
					// 64-bit
				case 64:
					__dos.writeByte(1);
					break;
				
					// Unknown
				default:
					throw new RuntimeException("NARG");
			}
			
			// Write the endianess
			JITCPUEndian end = owner._endianess;
			switch (end)
			{
					// Big
				case BIG:
					__dos.writeByte(2);
					break;
					
					// Little
				case LITTLE:
					__dos.writeByte(1);
					break;
				
					// Unknown
				default:
					throw new RuntimeException("NARG");
			}
			
			// Always version 1
			__dos.writeByte(1);
			
			// Write the OS ABI
			__dos.writeByte(eo._osabi);
			
			// Write the OS ABI specific part (padding)
			__dos.write(eo._padding, 0, 8);
		}
	}
	
	/**
	 * This represents the program headers in the ELF.
	 *
	 * @since 2016/08/16
	 */
	final class __Programs__
		extends __Sequence__
	{
		/** The entry size. */
		final int _entsize;
		
		/** The entry count. */
		volatile int _entcount =
			-1;
		
		/**
		 * Initializes the program headers.
		 *
		 * @since 2016/08/16
		 */
		private __Programs__()
		{
			// The size depends on the bit count
			switch (__Sequences__.this.owner._wordsize)
			{
					// 32-bit
				case 32:
					this._entsize = 32;
					break;
			
					// 64-bit
				case 64:
					this._entsize = 56;
					break;
			
					// Default
				default:
					throw new RuntimeException("OOPS");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/16
		 */
		@Override
		void __write(ExtendedDataOutputStream __dos)
			throws IOException
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This represents the sections in the ELF.
	 *
	 * @since 2016/08/16
	 */
	final class __Sections__
		extends __Sequence__
	{
		/** The entry size. */
		final int _entsize;
		
		/** The entry count. */
		volatile int _entcount =
			-1;
		
		/** The index containing the string table. */
		volatile int _stringsect =
			-1;
		
		/**
		 * Initializes the sections.
		 *
		 * @since 2016/08/16
		 */
		private __Sections__()
		{
			// The size depends on the bit count
			switch (__Sequences__.this.owner._wordsize)
			{
					// 32-bit
				case 32:
					this._entsize = 40;
					break;
			
					// 64-bit
				case 64:
					this._entsize = 64;
					break;
			
					// Default
				default:
					throw new RuntimeException("OOPS");
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/16
		 */
		@Override
		void __write(ExtendedDataOutputStream __dos)
			throws IOException
		{
			throw new Error("TODO");
		}
	}
}

