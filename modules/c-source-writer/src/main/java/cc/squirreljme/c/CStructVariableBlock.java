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
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Defines a struct variable block.
 *
 * @since 2023/05/29
 */
public class CStructVariableBlock
	extends CBlock
{
	/** The struct index we are on. */
	protected final CStructType struct;
	
	/** Members written. */
	private final Set<CIdentifier> _written =
		new HashSet<>();
	
	/** Which member index is this? */
	private volatile int _index;
	
	/**
	 * Initializes the struct variable writer.
	 *
	 * @param __writer The writer used.
	 * @param __struct The struct being accessed.
	 * @param __close The closing type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CStructVariableBlock(CSourceWriter __writer, CStructType __struct,
		String __close)
		throws NullPointerException
	{
		super(__writer, __close);
		
		if (__struct == null)
			throw new NullPointerException("NARG");
		
		this.struct = __struct;
	}
	
	/**
	 * Writing of an array block.
	 * 
	 * @param __memberName The member name.
	 * @return The block for the array.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public CArrayBlock memberArraySet(String __memberName)
		throws IOException, NullPointerException
	{
		return this.memberArraySet(CIdentifier.of(__memberName));
	}
	
	/**
	 * Writing of an array block.
	 * 
	 * @param __memberName The member name.
	 * @return The block for the array.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public CArrayBlock memberArraySet(CIdentifier __memberName)
		throws IOException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW39 Not an array member.} */
		CVariable member = this.__checkMember(__memberName);
		CType memberType = member.type;
		CType elementType;
		if (memberType instanceof CArrayType)
			elementType = ((CArrayType)memberType).elementType;
		else if (memberType instanceof CPointerType)
			elementType = ((CPointerType)memberType).pointedType;
		else
			throw new IllegalArgumentException("CW39");
		
		// Write opening
		this.__startMember(__memberName);
		this.tokens("{");
		
		// Setup block
		CArrayBlock rv = new CArrayBlock(this, elementType);
		return this._file.__pushBlock(rv, false);
	}
	
	/**
	 * Writes a member of a struct.
	 * 
	 * @param __memberName The member name.
	 * @param __value The value to write.
	 * @return {@code this}.
	 * @throws IOException on read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public CStructVariableBlock memberSet(String __memberName,
		CExpression __value)
		throws IOException, NullPointerException
	{
		return this.memberSet(CIdentifier.of(__memberName), __value);
	}
	
	/**
	 * Writes a member of a struct.
	 * 
	 * @param __memberName The member name.
	 * @param __value The value to write.
	 * @return {@code this}.
	 * @throws IOException on read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CStructVariableBlock memberSet(CIdentifier __memberName,
		CExpression __value)
		throws IOException, NullPointerException
	{
		if (__memberName == null || __value == null)
			throw new NullPointerException("NARG");
		
		this.__checkMember(__memberName);
		this.__startMember(__memberName);
		this.tokens(__value);
		
		// Self
		return this;
	}
	
	/**
	 * Initializes a new member struct that is also a struct.
	 * 
	 * @param __memberName The member name. 
	 * @return The struct variable block
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public CStructVariableBlock memberStructSet(String __memberName)
		throws IOException, NullPointerException
	{
		return this.memberStructSet(CIdentifier.of(__memberName));
	}
	
	/**
	 * Initializes a new member struct that is also a struct.
	 * 
	 * @param __memberName The member name. 
	 * @return The struct variable block
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public CStructVariableBlock memberStructSet(CIdentifier __memberName)
		throws IOException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		// Start writing member
		CVariable member = this.__checkMember(__memberName);
		this.__startMember(__memberName);
		this.token("{");
		
		// Remove modified types
		CType type = member.type;
		for (;;)
		{
			if (type instanceof CModifiedType)
				type = ((CModifiedType)type).type;
			else if (type instanceof CPointerType)
				type = ((CPointerType)type).pointedType;
			else
				break;
		}
		
		// Open block
		return this._file.__pushBlock(
			new CStructVariableBlock(this,
				(CStructType)type,
				"}"),
			true);
	}
	
	/**
	 * Checks and returns the variable for the member.
	 * 
	 * @param __memberName The name of the member.
	 * @return The variable for the member.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	private CVariable __checkMember(CIdentifier __memberName)
		throws NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW34 Struct has no such member.} */
		CVariable rv = this.struct.member(__memberName);
		if (rv == null)
			throw new NoSuchElementException("CW34");
		
		/* {@squirreljme.error CW33 Member already written.} */ 
		Set<CIdentifier> written = this._written;
		if (written.contains(__memberName))
			throw new IllegalStateException("CW33");
		
		written.add(__memberName);
		return rv;
	}
	
	/**
	 * Starts the writing of a member.
	 * 
	 * @param __memberName The member name.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	private void __startMember(CIdentifier __memberName)
		throws IOException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		// Do we need to prefix with a comma?
		int index = this._index;
		if (index > 0)
			this.token(",");
		
		// Write out member setting
		this.tokens(".", __memberName.identifier, "=");
		
		// For later
		this._index = index + 1;
	}
}
