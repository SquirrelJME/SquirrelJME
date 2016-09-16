// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This decodes methods.
 *
 * @since 2016/08/27
 */
class __MethodDecoder__
	extends __MemberDecoder__
{
	/** The constant pool. */
	final ConstantPool _pool;
	
	/** The class decoder owning this. */
	final ClassDecoder _classdecoder;
	
	/** Was method code parsed? */
	private volatile boolean _hitmcode;
	
	/** Method flags. */
	volatile MethodFlags _mflags;
	
	/** The method name. */
	volatile IdentifierSymbol _mname;
	
	/** Method type. */
	volatile MethodSymbol _mtype;
	
	/** The output description stream. */
	private volatile MethodDescriptionStream _desc;
	
	/**
	 * Initializes the method decoder.
	 *
	 * @param __cw The class writer to write to.
	 * @param __di The data input stream for the class file.
	 * @param __pool The constant pool.
	 * @param __cf The owning class flags.
	 * @param __cx The class decoder.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__MethodDecoder__(ClassDescriptionStream __cw, DataInputStream __di,
		ConstantPool __pool, ClassFlags __cf, ClassDecoder __cx)
		throws NullPointerException
	{
		super(__cw, __di, __pool, __cf);
		
		// Check
		if (__cx == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._classdecoder = __cx;
		this._pool = __pool;
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
		ClassDescriptionStream cw = this.classwriter;
		
		// Read the flags for this method
		MethodFlags mf = __FlagDecoder__.__method(this._classflags,
			input.readUnsignedShort());
		
		// Read the method name
		ConstantPool pool = this.pool;
		int ni;
		ConstantEntry ename = pool.get((ni = input.readUnsignedShort()));
		IdentifierSymbol name = IdentifierSymbol.of(
			ename.<String>get(true, String.class));
		
		// And the type
		int ti;
		ConstantEntry etype = pool.get((ti = input.readUnsignedShort()));
		MethodSymbol type = MethodSymbol.of(
			etype.<String>get(true, String.class));
		
		// Clear before being used
		this._hitmcode = false;
		
		// Register method since code needs to be generated following this
		MethodDescriptionStream ss = cw.method(mf, name, type);
		this._desc = ss;
		
		// Needed for code
		this._mflags = mf;
		this._mname = name;
		this._mtype = type;
		
		// Handle attributes
		int na = input.readUnsignedShort();
		for (int i = 0; i < na; i++)
			__readAttribute(pool, input);
		
		// {@squirreljme.error AY0h Abstract methods cannot have code.}
		boolean hascode = this._hitmcode;
		if (hascode == mf.isAbstract())
			throw new ClassFormatException("AY0h");
		
		// If there is no code then indicate as such
		if (!hascode)
			ss.noCode();
		
		// End method
		ss.endMember();
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
				// The code attribute
			case "Code":
				// {@squirreljme.error AY0a Multiple code attributes
				// in a single method.}
				if (this._hitmcode)
					throw new ClassFormatException("AY0a");
				
				// Mark as hit, there may only be one
				this._hitmcode = true;
				
				// Setup description
				MethodDescriptionStream desc = this._desc;
				
				// Need to read and completley skip code when done
				CodeDescriptionStream mlw = desc.code();
				
				// Setup decoder and give the writer the
				// program
				new __CodeDecoder__(this, __is, mlw).__decode();
				
				// Done
				return;
			
				// Unknown
			default:
				break;
		}
	}
}

