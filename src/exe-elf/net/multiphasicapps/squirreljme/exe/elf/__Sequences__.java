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
	
		// The target list
		List<__Sequence__> rv = this.seq;
	
		// Add identification
		rv.add(new __Identification__());
		
		throw new Error("TODO");
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
			// Always at the start
			this._at = 0;
		
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
			// Get the ELF
			ELFOutput eo = __Sequences__.this.owner;
			int bits = eo._wordsize;
			
			// Magic number
			__dos.writeByte(0x7F);
			__dos.writeByte('E');
			__dos.writeByte('L');
			__dos.writeByte('F');
			
			throw new Error("TODO");
		}
	}
}

