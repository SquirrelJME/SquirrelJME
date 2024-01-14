// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;

/**
 * Array data block.
 *
 * @since 2023/05/31
 */
public class CArrayBlock
	extends CBlock
{
	/** The element type. */
	protected final CType elementType;
	
	/** The current array index. */
	private volatile int _index;
	
	/**
	 * Initializes the C array Block.
	 *
	 * @param __ref The reference to use.
	 * @param __elementType The element type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CArrayBlock(CSourceWriter __ref, CType __elementType)
		throws NullPointerException
	{
		super(__ref, "}");
		
		if (__elementType == null)
			throw new NullPointerException("NARG");
		
		this.elementType = __elementType;
	}
	
	/**
	 * Writes a struct to the array.
	 * 
	 * @return The struct block.
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public CStructVariableBlock struct()
		throws IOException
	{
		/* {@squirreljme.error CW3f No an array of structs.} */
		CType elementType = this.elementType;
		if (!(elementType instanceof CStructType))
			throw new IllegalStateException("CW3f");
		
		// Prefix element with comma?
		int index = this._index;
		if (index > 0)
			this.token(",");
		this._index = index + 1;
		
		// Open block
		CStructVariableBlock rv = new CStructVariableBlock(
			this, (CStructType)elementType, "}");
		this.token("{");
		
		// Push it to the writer
		return this._file.__pushBlock(rv, false);
	}
	
	/**
	 * Writes an array value. 
	 *
	 * @param __expression The expression to store.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	public CArrayBlock value(CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		// Prefix element with comma?
		int index = this._index;
		if (index > 0)
			this.token(",");
		this._index = index + 1;
		
		this.token(__expression);
		
		return this;
	}
}
