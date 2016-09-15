// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITCodeWriter;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocator;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterFloatType;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterIntegerType;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterType;

/**
 * This is used to parse stack cached Java byte code to produce native machine
 * code from it.
 *
 * @since 2016/09/14
 */
public class BasicCodeWriter
	implements JITCodeWriter
{
	/** The owning namespace. */
	protected final BasicNamespaceWriter namespace;
	
	/** Where native machine code is to be placed. */
	protected final ExtendedDataOutputStream output;
	
	/** The constant pool to be utilized. */
	protected final BasicConstantPool pool;
	
	/** Native code generator. */
	protected final NativeCodeWriter writer;
	
	/** Native writer options. */
	protected final NativeCodeWriterOptions options;
	
	/** Native register and stack allocator. */
	protected final NativeAllocator allocator;
	
	/** The JIT configuration .*/
	protected final JITConfig config;
	
	/** The current piece of code being written. */
	final __Code__ _code;
	
	/**
	 * Initializes the code writer.
	 *
	 * @param __ns The writer for namespaces.
	 * @param __o Where native machine code is to be placed.
	 * @param __c Method code table holder.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	BasicCodeWriter(BasicNamespaceWriter __ns, ExtendedDataOutputStream __o,
		__Code__ __c)
		throws NullPointerException
	{
		// Check
		if (__ns == null || __o == null || __c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __ns;
		this.output = __o;
		this._code = __c;
		this.config = __ns._config;
		this.pool = __ns._pool;
		
		// Create code writer instance
		NativeCodeWriter writer;
		this.writer = (writer = __ns._jitoutput.createCodeWriter());
		NativeCodeWriterOptions options;
		this.options = (options = writer.options());
		
		// Create allocator
		this.allocator = options.createAllocator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void jumpTargets(int[] __t)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void primeArguments(StackMapType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Convert input arguments to types
		List<NativeRegisterType> args = new ArrayList<>();
		for (StackMapType t : __t)
			args.add(stackMapToRegisterType(t));
			
		// Debug
		System.err.printf("DEBUG -- Primed args in: %s -> %s%n",
			Arrays.<StackMapType>asList(__t), args);
		
		// Allocate the register
		NativeAllocation[] allocs = this.allocator.argumentAllocate(
			args.<NativeRegisterType>toArray(
				new NativeRegisterType[args.size()]));
		
		// Debug
		System.err.printf("DEBUG -- Primed args out: %s%n",
			Arrays.<NativeAllocation>asList(allocs));
		
		throw new Error("TODO");
	}
	
	/**
	 * Converts a Java stack map type to a native register type.
	 *
	 * @param __t The type to convert.
	 * @return The register type for the given register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public NativeRegisterType stackMapToRegisterType(StackMapType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		JITConfig config = this.config;
		JITTriplet triplet = config.triplet();
		NativeFloatType floating = triplet.floatingPoint();
		switch (__t)
		{
				// As-is
			case INTEGER: return NativeRegisterIntegerType.INTEGER;
			case LONG: return NativeRegisterIntegerType.LONG;
			
				// Depends on the pointer size
			case OBJECT:
				switch (triplet.bits())
				{
					case 8: return NativeRegisterIntegerType.BYTE;
					case 16: return NativeRegisterIntegerType.SHORT;
					case 32: return NativeRegisterIntegerType.INTEGER;
					case 64: return NativeRegisterIntegerType.LONG;
					
						// Unknown
					default:
						throw new RuntimeException("OOPS");
				}
				
				// Could be in software
			case FLOAT:
				if (floating.isHardwareFloat())
					return NativeRegisterFloatType.FLOAT;
				return NativeRegisterIntegerType.INTEGER;
			
				// Could also be in software
			case DOUBLE:
				if (floating.isHardwareDouble())
					return NativeRegisterFloatType.DOUBLE;
				return NativeRegisterIntegerType.LONG;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void variableCounts(int __ms, int __ml)
	{
		// Ignore this
	}
}

