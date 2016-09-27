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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataInputStream;
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This class is used to decode the actual code attribute in the method
 * along with any of its attributes.
 *
 * @since 2016/08/19
 */
final class __CodeDecoder__
	extends __HasAttributes__
{
	/** The maximum number of bytes the code attribute can be. */
	private static final int _CODE_SIZE_LIMIT =
		65535;
	
	/** Constant pool. */
	protected final ConstantPool pool;
	
	/** The output code writer. */
	final CodeDescriptionStream _writer;
	
	/** The owning method decoder. */
	final __MethodDecoder__ _decoder;
	
	/** The class decoder owning this. */
	final ClassDecoder _classdecoder;
	
	/** The method flags. */
	final MethodFlags _flags;
	
	/** The method reference. */
	final MethodReference _ref;
	
	/** The input code attribute data. */
	private final DataInputStream _input;
	
	/** The maximum number of local variables. */
	volatile int _maxlocals;
	
	/** The maximum size of the stack. */
	volatile int _maxstack;
	
	/** The stack map table state. */
	volatile Map<Integer, __SMTState__> _smt;
	
	/** Are there exception handlers present? */
	volatile boolean _hasexceptions;
	
	/**
	 * Add base code decoder class.
	 *
	 * @param __cd The method decoder being used.
	 * @param __dis The input source.
	 * @param __f The method flags.
	 * @param __t The method type.
	 * @param __mlw The logic writer to write code to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__CodeDecoder__(__MethodDecoder__ __cd, DataInputStream __dis,
		CodeDescriptionStream __mlw)
		throws NullPointerException
	{
		// Check
		if (__cd == null || __dis == null || __mlw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._decoder = __cd;
		ClassDecoder classdec = __cd._classdecoder;
		this._classdecoder = classdec;
		this._input = __dis;
		this._flags = __cd._mflags;
		this._writer = __mlw;
		this.pool = __cd._pool;
		
		// Create reference
		this._ref = new MethodReference(classdec._classname, __cd._mname,
			__cd._mtype, classdec._flags.isInterface());
	}
	
	/**
	 * Decodes the code attribute and any of its contained data
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/08/23
	 */
	void __decode()
		throws IOException
	{
		DataInputStream input = this._input;
		ConstantPool pool = this.pool;
		CodeDescriptionStream writer = this._writer;
		
		// Read max stack and locals
		int maxstack = input.readUnsignedShort();
		int maxlocals = input.readUnsignedShort();
		
		// Store for SMT parsing and code usage
		this._maxstack = maxstack;
		this._maxlocals = maxlocals;
		
		// Report this to the writer
		writer.variableCounts(maxstack, maxlocals);
		
		// {@squirreljme.error AY36 The code for a given method exceeds the
		// code size limit, or the size is zero. (The current code length;
		// The code size limit)}
		int codelen = input.readInt();
		if (codelen <= 0 || codelen > _CODE_SIZE_LIMIT)
			throw new ClassFormatException(String.format("AY36 %d %d",
				codelen & 0xFFFF_FFFFL, _CODE_SIZE_LIMIT));
		
		// Set code length
		writer.codeLength(codelen);
		
		// Read code and save it for later after the exception table and
		// possibly the stack map table parse has been parsed
		byte[] code = new byte[codelen];
		input.readFully(code);
		
		// Read the exception table
		int numex = input.readUnsignedShort();
		boolean hasexceptions = (numex != 0);
		this._hasexceptions = hasexceptions;
		for (int i = 0; i < numex; i++)
			throw new Error("TODO");
		
		// Read attributes
		int na = input.readUnsignedShort();
		for (int i = 0; i < na; i++)
			__readAttribute(pool, input);
		
		// If no stack map table exists then setup an initial implicit state
		Map<Integer, __SMTState__> smt;
		MethodReference ref = this._ref;
		if ((smt = this._smt) == null)
			this._smt = (smt = __SMTParser__.__initialState(this._flags,
				ref.memberType(), maxstack, maxlocals));
		
		// Parse the byte code now
		try (ExtendedDataInputStream dis = new ExtendedDataInputStream(
			new ByteArrayInputStream(code)))
		{
			// Mark the stream and determine the jump targets, this information
			// is passed to the method writer so that it is not forced to
			// store state for any position that is not a jump target
			dis.mark(codelen);
			writer.jumpTargets(new __JumpTargetCalc__(dis, codelen).targets());
			
			// Calculate all of the variable type for all operation positions
			// if requested
			if (this._classdecoder.options().contains(
				ClassDecoderOption.CALCULATE_ALL_VARIABLE_TYPES))
			{
				dis.reset();
				writer.variableTypes(new __VarTypeCalc__(dis, codelen, smt).
					types());
			}
			
			// Reset and decode operations
			dis.reset();
			__MethodDecoder__ decoder = this._decoder;
			new __OpParser__(writer, dis, smt, decoder._classflags,
				pool, ref).__decodeAll();
		}
		
		// Done
		throw new Error("TODO");
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
		boolean old = false;
		switch (__name)
		{
				// The stack map table
			case "StackMap":
				old = true;
			case "StackMapTable":
				// {@squirreljme.error AY3t Only a single stack map table is
				// permitted in a code attribute.}
				if (this._smt != null)
					throw new ClassFormatException("AY3t");
				
				// Parse and store result
				this._smt = new __SMTParser__(!old, __is, this._flags,
					this._ref.memberType(), this._maxstack,
					this._maxlocals).result();
				return;
			
				// Unknown
			default:
				break;
		}
	}
}

