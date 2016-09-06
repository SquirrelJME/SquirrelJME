// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITFieldFlags;

/**
 * This decodes ields.
 *
 * @since 2016/08/27
 */
class __FieldDecoder__
	extends __MemberDecoder__
{
	/** Field constant value. */
	private volatile Object _fieldcv;
	
	/**
	 * Initializes the field decoder.
	 *
	 * @param __cw The class writer to write to.
	 * @param __di The data input stream for the class file.
	 * @param __pool The constant pool.
	 * @param __cf The owning class flags.
	 * @since 2016/08/18
	 */
	__FieldDecoder__(JITClassWriter __cw, DataInputStream __di,
		JITConstantPool __pool, JITClassFlags __cf)
	{
		super(__cw, __di, __pool, __cf);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/18
	 */
	@Override
	void __decode()
		throws IOException
	{
		// Get
		DataInputStream input = this.input;
		JITClassWriter cw = this.classwriter;
		
		// Read the flags for this field
		JITFieldFlags mf = __FlagDecoder__.__field(this._classflags,
			input.readUnsignedShort());
		
		// Read the name
		JITConstantPool pool = this.pool;
		int ndx;
		JITConstantEntry ename = pool.get((ndx = input.readUnsignedShort()));
		IdentifierSymbol name = IdentifierSymbol.of(
			ename.<String>get(true, String.class));
		
		// And the type
		int tdx;
		JITConstantEntry etype = pool.get((tdx = input.readUnsignedShort()));
		FieldSymbol type = FieldSymbol.of(
			etype.<String>get(true, String.class));
		
		// Clear these before handling attributes
		this._fieldcv = null;
		
		// Need to handle attributes
		int na = input.readUnsignedShort();
		for (int i = 0; i < na; i++)
			__readAttribute(pool, input);
		
		// Register the field
		cw.field(mf, name, ndx, type, tdx, this._fieldcv);
		
		// End field
		cw.endField();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/27
	 */
	@Override
	void __handleAttribute(String __name, DataInputStream __is)
		throws IOException
	{
		// Check
		if (__name == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Which attribute?
		switch (__name)
		{
				// Constant value
			case "ConstantValue":
				// {@squirreljme.error ED04 Multiple field constant
				// values defined for a single field.}
				if (this._fieldcv != null)
					throw new JITException("ED04");
				
				if (true)
					throw new Error("TODO");
				
				return;
			
				// Unknown
			default:
				break;
		}
	}
}

