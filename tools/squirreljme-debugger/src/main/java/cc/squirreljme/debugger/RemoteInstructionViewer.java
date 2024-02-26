// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import net.multiphasicapps.classfile.ByteCodeUtils;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionMnemonics;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.Pool;

/**
 * Views remote instructions.
 *
 * @since 2024/01/22
 */
public class RemoteInstructionViewer
	implements InstructionViewer
{
	/** The address of this instruction. */
	protected final int address;
	
	/** The byte code. */
	protected final byte[] byteCode;
	
	/** The constant pool, may be {@code null}. */
	protected final KnownValue<Pool> pool;
	
	/** The debugger state. */
	protected final DebuggerState state;
	
	/** Arguments to the instruction. */
	private volatile Object[] _args;
	
	/**
	 * Initializes the instruction viewer.
	 *
	 * @param __state The state of the debugger.
	 * @param __pool The constant pool.
	 * @param __byteCode The method byte code.
	 * @param __address The address of this instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	public RemoteInstructionViewer(DebuggerState __state,
		KnownValue<Pool> __pool, byte[] __byteCode, int __address)
		throws NullPointerException
	{
		if (__state == null || __byteCode == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.pool = __pool;
		this.byteCode = __byteCode;
		this.address = __address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public int address()
	{
		return this.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public Object[] arguments()
	{
		// Was this already cached?
		Object[] result = this._args;
		if (result != null)
			return result.clone();
		
		// Process with fallback
		boolean cacheOkay = false;
		try
		{
			// Read in raw arguments
			int[] rawArgs = ByteCodeUtils.readRawArguments(this.byteCode,
				0, this.address);
			
			// If there is no pool, we cannot continue
			Pool pool = this.pool.getOrUpdateSync(this.state);
			if (pool == null)
				result = new IntegerArrayList(rawArgs).toArray(
					new Integer[rawArgs.length]);
				
			// Process them to get the real ones
			else
				try
				{
					result = ByteCodeUtils.processArguments(pool,
						this.mnemonicId(), this.address, rawArgs);
					
					// We can safely cache this
					cacheOkay = true;
				}
				catch (InvalidClassFormatException __e)
				{
					__e.printStackTrace();
					
					result = new IntegerArrayList(rawArgs).toArray(
						new Integer[rawArgs.length]);
				}
		}
		
		// Failed, fallback
		catch (InvalidClassFormatException __e)
		{
			__e.printStackTrace();
			
			result = new Object[0];
		}
		
		// Use whatever result was determined
		if (cacheOkay)
			this._args = result;
		return result.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public int length()
	{
		try
		{
			return ByteCodeUtils.instructionLength(
				this.byteCode, 0, this.address, null);
		}
		catch (InvalidClassFormatException ignored)
		{
			return 1;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public String mnemonic()
	{
		return InstructionMnemonics.toString(this.mnemonicId());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public int mnemonicId()
	{
		int base = this.byteCode[this.address] & 0xFF;
		if (base == InstructionIndex.WIDE)
			return (base << 8) | (this.byteCode[this.address + 1] & 0xFF);
		return base;
	}
}
