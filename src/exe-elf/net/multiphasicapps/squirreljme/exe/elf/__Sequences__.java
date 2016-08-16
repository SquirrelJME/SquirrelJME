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
		
		// Initialize program headers (and any real code areas)
		__Programs__ programs = new __Programs__();
		this.programs = programs;
		
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
					this._size = 36;
					break;
			
					// 64-bit
				case 64:
					this._size = 48;
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
			
			// Write the size of this header
			__dos.writeShort(this._size);
			
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
	 * This is single program within the program header.
	 *
	 * @since 2016/08/16
	 */
	final class __Program__
		extends __Sequence__
	{
		/** The wrapped program. */
		protected final ELFProgram program;
		
		/**
		 * Initializes the program.
		 *
		 * @param __addr The address the program should be at.
		 * @param __last The program before this one.
		 * @param __prg The program to represent.
		 * @param __i The program index.
		 * @throws NullPointerException On null arguments, except for
		 * {@code __last}.
		 * @since 2016/08/16
		 */
		private __Program__(int __addr, __Program__ __last, ELFProgram __prg,
			int __i)
			throws NullPointerException
		{
			// Check
			if (__prg == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.program = __prg;
			
			// Add self to the end of the program list
			List<__Sequences__.__Sequence__> seqs = __Sequences__.this.seq;
			seqs.add(this);
			
			// Set address and size
			this._at = __addr;
			this._size = __prg._length;
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
	 * This represents the program headers in the ELF.
	 *
	 * @since 2016/08/16
	 */
	final class __Programs__
		extends __Sequence__
	{
		/** The entry size. */
		final int _entsize;
		
		/** Subprograms. */
		final List<__Program__> _subprogs =
			new ArrayList<>();
		
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
			ELFOutput owner = __Sequences__.this.owner;
			int entsize;
			switch (owner._wordsize)
			{
					// 32-bit
				case 32:
					this._entsize = (entsize = 32);
					break;
			
					// 64-bit
				case 64:
					this._entsize = (entsize = 56);
					break;
			
					// Default
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Add to sequence list since it has to be placed before the about
			// to follow code areas
			List<__Sequences__.__Sequence__> seqs = __Sequences__.this.seq;
			__Sequence__ justbefore = seqs.get(seqs.size() - 1);
			seqs.add(this);
			
			// Get ELF programs to generate headers for
			List<ELFProgram> programs = owner._programs;
			int n = programs.size();
			this._entcount = n;
			
			// Start program headers right where the older data ends
			int base = justbefore._at + justbefore._size;
			this._at = base;
			
			// Add locations of each area
			// Start directly following the program header table
			int now = base + (entsize * n);
			__Program__ last = null;
			List<__Program__> subprogs = this._subprogs;
			for (int i = 0; i < n; i++)
			{
				// Get program here
				ELFProgram prg = programs.get(i);
				
				// Create new target program
				__Program__ p = new __Program__(now, last, prg, i);
				last = p;
				subprogs.add(p);
				
				// Set position of the program and make the next program
				// follow this one
				p._at = now;
				now = ((now + p._size + 3) & (~3));
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
			// Write the header information
			List<__Program__> subprogs = this._subprogs;
			int n = subprogs.size();
			for (int i = 0; i < n; i++)
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
			
			// No sections
			System.err.println("TODO -- Add support for sections.");
			this._entcount = 0;
			this._stringsect = 0;
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

