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
		
		System.err.println("TODO -- Initialize more ELF sequences.");
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
			throw new Error("TODO");
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
}

