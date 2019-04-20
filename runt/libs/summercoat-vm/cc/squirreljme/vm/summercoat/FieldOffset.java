// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.nncc.NativeCode;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains an offset to a field, either static or otherwise.
 *
 * @since 2019/04/18
 */
public final class FieldOffset
{
	/** The memory access offset. */
	public final int offset;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the field offset.
	 *
	 * @param __vol Is this volatile?
	 * @param __off The target offset.
	 * @since 2019/04/18
	 */
	public FieldOffset(boolean __vol, int __off)
	{
		this.offset = __off ^ (__vol ? NativeCode.MEMORY_OFF_VOLATILE_BIT : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/20
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = "FieldOff@" + this.offset));
		
		return rv;
	}
}

