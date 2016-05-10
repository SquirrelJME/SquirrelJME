// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCIClassReference;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This describes an operation.
 *
 * @since 2016/05/10
 */
public final class NCPOp
{
	/** The current instruction identifier. */
	protected final int instid;
	
	/** Operation arguments. */
	protected final List<Object> args;
	
	/** The string representation of this operation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the operation data.
	 *
	 * @param __cp The owning code parser.
	 * @param __code The code data.
	 * @param __pa The physical address in the code buffer.
	 * @throws NCPException If the operation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/10
	 */
	NCPOp(NCPCodeParser __cp, NCIByteBuffer __code, int __pa)
		throws NCPException, NullPointerException
	{
		// Check
		if (__cp == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Read the instid
		int instid = __code.readUnsignedByte(__pa);
		if (instid == NCPOpCode.WIDE)
			instid = (NCPOpCode.WIDE << 8) | __code.readUnsignedByte(__pa, 1);
		this.instid = instid;
		
		// Get the constant pool
		NCIPool pool = __cp.constantPool();
		
		// Allocate new object
		if (instid == NCPOpCode.NEW)
			args = __args(pool.<NCIClassReference>requiredAs(
				__code.readUnsignedShort(__pa, 1), NCIClassReference.class));
		
		// {@squirreljme.error AR08 Unknown instid. (The instid)}
		else
			throw new NCPException(NCPException.Issue.ILLEGAL_OPCODE,
				String.format("AR08 %d", instid));
	}
	
	/**
	 * Returns the operation arguments.
	 *
	 * @return The operation arguments.
	 * @since 2016/05/10
	 */
	public List<Object> arguments()
	{
		return args;
	}
	
	/**
	 * Returns the ID of the given instruction.
	 *
	 * @return The instruction identifier.
	 * @since 2016/05/10
	 */
	public int instructionId()
	{
		return instid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/10
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = "(" + instid + "=" +
				args.toString() + ")"));
		
		// Return
		return rv;
	}
	
	/**
	 * Wraps and returns an unmodifiable variant of the input arguments.
	 *
	 * @param __o The arguments to input.
	 * @return An unmodifiable list.
	 * @since 2016/05/10
	 */
	private static List<Object> __args(Object... __o)
	{
		return UnmodifiableList.<Object>of(Arrays.<Object>asList(__o));
	}
}

