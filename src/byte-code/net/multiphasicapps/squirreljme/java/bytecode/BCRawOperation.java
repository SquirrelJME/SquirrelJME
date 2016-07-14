// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.ci.CIByteBuffer;

/**
 * This represents a raw byte code operation, it only contains integral field
 * values which indicate the operation that is performed along with its
 * arguments.
 *
 * @since 2016/06/22
 */
public final class BCRawOperation
{
	/** The owning byte code. */
	protected final BCByteCode owner;
	
	/** The logical address of this instruction. */
	protected final int logicaladdress;
	
	/** The values for the operations. */
	private final long[] _values;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the raw operation.
	 *
	 * @param __bc The owning byte code.
	 * @param __bb The code buffer.
	 * @param __lp The logical address of this instruction.
	 * @throws BCException If the operation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/22
	 */
	BCRawOperation(BCByteCode __bc, CIByteBuffer __bb, int __lp)
		throws BCException, NullPointerException
	{
		if (__bc == null || __bb == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __bc;
		this.logicaladdress = __lp;
		
		// Determine the physical address
		int phy = __bc.logicalToPhysical(__lp);
		
		// Read the opcode ID
		int opid = __bb.readUnsignedByte(phy, 0);
		if (opid == BCInstructionID.WIDE)
		{
			opid <<= 8;
			opid |= __bb.readUnsignedByte(phy, 0);
		}
		
		// Depends on the operation
		long[] vals;
		switch (opid)
		{
				// Single signed byte at address 1
			case BCInstructionID.BIPUSH:
				vals = new long[]
					{
						opid,
						__bb.readByte(phy, 1)
					};
				break;
			
				// Single unsigned byte at address 1
			case BCInstructionID.ALOAD:
			case BCInstructionID.ASTORE:
			case BCInstructionID.DLOAD:
			case BCInstructionID.DSTORE:
			case BCInstructionID.FLOAD:
			case BCInstructionID.FSTORE:
			case BCInstructionID.ILOAD:
			case BCInstructionID.ISTORE:
			case BCInstructionID.LLOAD:
			case BCInstructionID.LSTORE:
			case BCInstructionID.LDC:
			case BCInstructionID.NEWARRAY:
			case BCInstructionID.RET:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedByte(phy, 1)
					};
				break;
				
				// Single unsigned short at address 1
			case BCInstructionID.ANEWARRAY:
			case BCInstructionID.CHECKCAST:
			case BCInstructionID.GETFIELD:
			case BCInstructionID.GETSTATIC:
			case BCInstructionID.INSTANCEOF:
			case BCInstructionID.INVOKESPECIAL:
			case BCInstructionID.INVOKESTATIC:
			case BCInstructionID.INVOKEVIRTUAL:
			case BCInstructionID.LDC_W:
			case BCInstructionID.LDC2_W:
			case BCInstructionID.NEW:
			case BCInstructionID.PUTFIELD:
			case BCInstructionID.PUTSTATIC:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedShort(phy, 1)
					};
				break;
				
				// Single unsigned short at address 2
			case BCInstructionID.WIDE_ALOAD:
			case BCInstructionID.WIDE_ASTORE:
			case BCInstructionID.WIDE_DLOAD:
			case BCInstructionID.WIDE_DSTORE:
			case BCInstructionID.WIDE_FLOAD:
			case BCInstructionID.WIDE_FSTORE:
			case BCInstructionID.WIDE_ILOAD:
			case BCInstructionID.WIDE_ISTORE:
			case BCInstructionID.WIDE_LLOAD:
			case BCInstructionID.WIDE_LSTORE:
			case BCInstructionID.WIDE_RET:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedShort(phy, 2)
					};
				break;
				
				// Single signed short at address 1
			case BCInstructionID.GOTO:
			case BCInstructionID.IF_ACMPEQ:
			case BCInstructionID.IF_ACMPNE:
			case BCInstructionID.IFEQ:
			case BCInstructionID.IFGE:
			case BCInstructionID.IFGT:
			case BCInstructionID.IF_ICMPEQ:
			case BCInstructionID.IF_ICMPGE:
			case BCInstructionID.IF_ICMPGT:
			case BCInstructionID.IF_ICMPLE:
			case BCInstructionID.IF_ICMPLT:
			case BCInstructionID.IF_ICMPNE:
			case BCInstructionID.IFLE:
			case BCInstructionID.IFLT:
			case BCInstructionID.IFNE:
			case BCInstructionID.IFNONNULL:
			case BCInstructionID.IFNULL:
			case BCInstructionID.JSR:
			case BCInstructionID.SIPUSH:
				vals = new long[]
					{
						opid,
						__bb.readShort(phy, 1)
					};
				break;
				
				// Single signed int at address 1
			case BCInstructionID.GOTO_W:
			case BCInstructionID.JSR_W:
				vals = new long[]
					{
						opid,
						__bb.readInt(phy, 1)
					};
				break;
				
				// Unsigned byte and signed byte
			case BCInstructionID.IINC:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedByte(phy, 1),
						__bb.readByte(phy, 2)
					};
				break;
				
				// Unsigned short and signed short
			case BCInstructionID.WIDE_IINC:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedShort(phy, 2),
						__bb.readShort(phy, 4)
					};
				break;
				
				// Invoke dynamic
			case BCInstructionID.INVOKEDYNAMIC:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedShort(phy, 1),
						0
					};
				break;
				
				// Invoke interface
			case BCInstructionID.INVOKEINTERFACE:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedShort(phy, 1),
						__bb.readUnsignedByte(phy, 3),
						0
					};
				break;
				
				// Multi-new array
			case BCInstructionID.MULTIANEWARRAY:
				vals = new long[]
					{
						opid,
						__bb.readUnsignedShort(phy, 1),
						__bb.readUnsignedByte(phy, 3)
					};
				break;
				
				// Lookup switch
			case BCInstructionID.LOOKUPSWITCH:
				throw new Error("TODO");
				
				// Table switch
			case BCInstructionID.TABLESWITCH:
				throw new Error("TODO");
			
				// Illegal instruction where fields are unknown or instructions
				// with only the opcode.
			default:
				vals = new long[]{opid};
				break;
		}
		
		// Set
		this._values = vals;
	}
	
	/**
	 * Obtains the value for the given field.
	 *
	 * @param __i The field to get the value for.
	 * @return The value of the given field.
	 * @throws IndexOutOfBoundsException If the index it out of bounds.
	 * @since 2016/06/22
	 */
	public final long get(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get
		long[] values = this._values;
		if (__i >= values.length)
			throw new IndexOutOfBoundsException("IOOB");
		return values[__i];
	}
	
	/**
	 * Returns the logical address where this instruction is located.
	 *
	 * @return The instruction's logical address.
	 * @since 2016/06/22
	 */
	public final int logicalAddress()
	{
		return this.logicaladdress;
	}
	
	/**
	 * Returns the number of fields the instruction uses.
	 *
	 * @return The number of fields.
	 * @since 2016/06/22
	 */
	public final int size()
	{
		return this._values.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/22
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("[");
			
			// Add values
			boolean com = false;
			for (long v : this._values)
			{
				// Add comma
				if (com)
					sb.append(", ");
				com = true;
				
				// Add it
				sb.append(v);
			}
			
			// Finish
			sb.append(']');
			_string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return
		return rv;
	}
}

