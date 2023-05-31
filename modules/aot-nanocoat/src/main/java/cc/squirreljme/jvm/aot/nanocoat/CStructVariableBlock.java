// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Defines a struct variable block.
 *
 * @since 2023/05/29
 */
public class CStructVariableBlock
	extends CBlock
{
	/** Which member index is this? */
	private volatile int _index;
	
	/**
	 * Initializes the struct variable writer.
	 * 
	 * @param __writer The writer used.
	 * @since 2023/05/29
	 */
	CStructVariableBlock(Reference<CSourceWriter> __writer)
	{
		this(__writer, "};");
	}
	
	/**
	 * Initializes the struct variable writer.
	 * 
	 * @param __writer The writer used.
	 * @param __close The closing type.
	 * @since 2023/05/31
	 */
	CStructVariableBlock(Reference<CSourceWriter> __writer, String __close)
	{
		super(__writer, __close);
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
	public CArrayBlock memberArraySet(String __memberName)
		throws IOException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		CSourceWriter writer = this.writer();
		
		// Write opening
		this.__startMember(__memberName);
		writer.tokens("{");
		
		// Setup block
		CArrayBlock rv = new CArrayBlock(writer._selfRef);
		return writer.__pushBlock(rv);
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
	public CStructVariableBlock memberSet(String __memberName,
		Object... __value)
		throws IOException, NullPointerException
	{
		if (__memberName == null || __value == null || __value.length == 0)
			throw new NullPointerException("NARG");
		
		CSourceWriter writer = this.writer();
		
		this.__startMember(__memberName);
		writer.tokens(__value);
		
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
	 * @since 2023/05/31
	 */
	public CStructVariableBlock memberStructSet(String __memberName)
		throws IOException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		CSourceWriter writer = this.writer();
		
		// Start writing member
		this.__startMember(__memberName);
		writer.token("{");
		
		// Open block
		return writer.__pushBlock(new CStructVariableBlock(writer._selfRef,
			"}"));
	}
	
	/**
	 * Starts the writing of a member.
	 * 
	 * @param __memberName The member name.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	private void __startMember(String __memberName)
		throws IOException, NullPointerException
	{
		if (__memberName == null)
			throw new NullPointerException("NARG");
		
		CSourceWriter writer = this.writer();
		
		// Do we need to prefix with a comma?
		int index = this._index;
		if (index > 0)
			writer.token(",");
		
		// Start on fresh line for readability
		writer.freshLine();
		
		// Write out member setting
		writer.tokens("." + __memberName, "=");
		
		// For later
		this._index = index + 1;
	}
}
