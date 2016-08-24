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
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITFieldFlag;
import net.multiphasicapps.squirreljme.jit.base.JITFieldFlags;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlag;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;

/**
 * This performs the decoding of the class file format.
 *
 * @since 2016/06/28
 */
final class __ClassDecoder__
{
	/** The object class. */
	private static final ClassNameSymbol _OBJECT_CLASS =
		ClassNameSymbol.of("java/lang/Object");
	
	/** The magic number of the class file. */
	public static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** The owning JIT. */
	protected final JIT jit;
	
	/** The namespace to write into. */
	protected final JITNamespaceWriter namespace;
	
	/** The input data source. */
	protected final DataInputStream input;
	
	/** The build configuration. */
	protected final JITOutputConfig.Immutable config;
	
	/** Rewrites of class names. */
	private final JITClassNameRewrite[] _rewrites;
	
	/** The version of this class file. */
	private volatile __ClassVersion__ _version;
	
	/** The constant pool of the class. */
	private volatile JITConstantPool _pool;
	
	/** Class flags. */
	private volatile JITClassFlags _flags;
	
	/** The name of this class. */
	private volatile ClassNameSymbol _classname;
	
	/** Field constant value. */
	private volatile Object _fieldcv;
	
	/** Was method code parsed? */
	private volatile boolean _hitmcode;
	
	/** Method flags. */
	private volatile JITMethodFlags _mflags;
	
	/** Method type. */
	private volatile MethodSymbol _mtype;
	
	/** JIT access. */
	final JIT _jit;
	
	/**
	 * This initializes the decoder for classes.
	 *
	 * @param __jit The owning JIT.
	 * @param __ns The class namespace.
	 * @param __dis The source for data input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	__ClassDecoder__(JIT __jit, JITNamespaceWriter __ns, DataInputStream __dis)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __ns == null || __dis == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this._jit = __jit;
		this.namespace = __ns;
		this.input = __dis;
		
		// Set rewrites
		JITOutputConfig.Immutable config = __jit.config();
		this.config = config;
		this._rewrites = config.classNameRewrites();
	}
	
	/**
	 * This performs the actual decoding of the class file.
	 *
	 * @param __jo The JIT output to use, when the name of the class is known
	 * then will be opened and written to during the decoding process.
	 * @throws IOException On read errors.
	 * @throws JITException If the class file format is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	final void __decode(JITOutput __jo)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__jo == null)
			throw new NullPointerException("NARG");
		
		DataInputStream input = this.input;
		
		// {@squirreljme.error ED12 The magic number of the input data stream
		// does not match that of the Java class file. (The magic number which
		// was read)}
		int fail;
		if ((fail = input.readInt()) != MAGIC_NUMBER)
			throw new JITException(String.format("ED12 %08x", fail));
		
		// {@squirreljme.error ED13 The version number of the input class file
		// is not valid. (The version number)}
		int cver = input.readShort() | (input.readShort() << 16);
		__ClassVersion__ version = __ClassVersion__.findVersion(cver);
		this._version = version;
		if (version == null)
			throw new JITException(String.format("ED13 %d.%d", cver >>> 16,
				(cver & 0xFFFF)));
		
		// Decode the constant pool
		JITConstantPool pool = new JITConstantPool(input, this);
		this._pool = pool;
		
		// Read the flags for this class
		JITClassFlags cf = __FlagDecoder__.__class(input.readUnsignedShort());
		this._flags = cf;
		
		// Read class name
		int clnamedx = input.readUnsignedShort();
		ClassNameSymbol clname = pool.get(clnamedx).<ClassNameSymbol>get(
			true, ClassNameSymbol.class);
		boolean isobject = (clname.equals(_OBJECT_CLASS));
		
		// There is enough "known" information (just the name) to start
		// outputting a class
		this._classname = clname;
		try (JITClassWriter cw = this.namespace.beginClass(clname))
		{
			// Rewrite classes in the constant pool
			pool.__rewrite();
			
			// Send pool
			cw.constantPool(pool, clnamedx);
			
			// Send class flags
			cw.classFlags(cf);
			
			// Read super class
			int sudx = input.readUnsignedShort();
			ClassNameSymbol suname = pool.get(sudx).<ClassNameSymbol>optional(
				true, ClassNameSymbol.class);
			
			// {@squirreljme.error ED0m The Object class must have no super
			// class and any non-Object class must have a super class.
			// (The class name; The super-class name)}
			if ((suname != null) == isobject)
				throw new JITException(String.format("ED0m %s %s", clname,
					suname));
			
			// {@squirreljme.error ED0n Interfaces must extend the Object
			// class. (Class flags; The super-class name)}
			if (cf.isInterface() && !suname.equals(_OBJECT_CLASS))
				throw new JITException(String.format("ED0n %s %s", cf,
					suname));
			
			// Send
			cw.superClass(suname, sudx);
			
			// Read in interfaces
			int icount = input.readUnsignedShort();
			ClassNameSymbol[] ifaces = new ClassNameSymbol[icount];
			int[] ifdxs = new int[icount];
			for (int i = 0; i < icount; i++)
			{
				int idx = input.readUnsignedShort();
				ifaces[i] = pool.get(idx).<ClassNameSymbol>get(true,
					ClassNameSymbol.class);
				ifdxs[i] = idx;
			}
			
			// Send
			cw.interfaceClasses(ifaces, ifdxs);
			
			// Handle fields
			int fcount = input.readUnsignedShort();
			cw.fieldCount(fcount);
			for (int i = 0; i < fcount; i++)
				__singleField(cw, input);
			
			// Handle methods
			int mcount = input.readUnsignedShort();
			cw.methodCount(mcount);
			for (int i = 0; i < mcount; i++)
				__singleMethod(cw, input);
			
			// Handle class attributes
			int na = input.readUnsignedShort();
			for (int i = 0; i < na; i++)
				__singleAttribute(cw, input, __AttributeFor__.CLASS);
			
			// End class
			cw.endClass();
		}
	}
	
	/**
	 * This checks the class is to be rewritten.
	 *
	 * @param __cn The input class.
	 * @return The rewritten class if it is to be done so or {@code __cn} if
	 * it is not rewritten.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/13
	 */
	final ClassNameSymbol __rewriteClass(ClassNameSymbol __cn)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// If no current class name was set, do not modify it because rewrites
		// would not exist.
		ClassNameSymbol classname = this._classname;
		if (classname == null)
			return __cn;
		
		// Check rewrites
		for (JITClassNameRewrite rewrite : this._rewrites)
		{
			ClassNameSymbol from = rewrite.from();
			if (from.equals(__cn))
			{
				// Never rewrite the current class
				if (classname.equals(from))
					continue;
			
				// Otherwise rewrite it
				return rewrite.to();
			}
		}
		
		// Not rewritten, use original
		return __cn;
	}
	
	/**
	 * Handles a single attribute.
	 *
	 * @param __cw The class writer to write to.
	 * @param __di The input stream.
	 * @param __af The type of thing this attribute is for.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	private void __singleAttribute(JITClassWriter __cw, DataInputStream __di,
		__AttributeFor__ __af)
		throws IOException, NullPointerException
	{
		// Check
		if (__cw == null || __di == null || __af == null)
			throw new NullPointerException("NARG");
		
		// Read the attribute name and length
		JITConstantPool pool = this._pool;
		JITConstantEntry eaname = pool.get(__di.readUnsignedShort());
		String aname = eaname.get(false, String.class);
		
		// {@squirreljme.error ED19 The length of the attribute exceeds
		// 2GiB.}
		int len = __di.readInt();
		if (len < 0)
			throw new JITException("ED19");
		
		// Depends on the thing containing the attributes
		switch (__af)
		{
				// Classes
			case CLASS:
				// All class attributes are ignored
				break;
			
				// Fields
			case FIELD:
				switch (aname)
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
				break;
			
				// Methods
			case METHOD:
				switch (aname)
				{
						// The code attribute
					case "Code":
						// {@squirreljme.error ED03 Multiple code attributes
						// in a single method.}
						if (this._hitmcode)
							throw new JITException("ED03");
						
						// Mark as hit
						this._hitmcode = true;
						
						// Need to read and completley skip code when done
						try (JITMethodWriter mlw = __cw.code();
							DataInputStream cis = new DataInputStream(
								new SizeLimitedInputStream(__di, len, true)))
						{
							// Setup decoder and give the writer the
							// program
							mlw.acceptProgram(new __CodeDecoder__(this, cis,
								this._mflags, this._mtype, mlw).__decode());
						}
						
						// Done
						return;
					
						// Unknown
					default:
						break;
				}
				break;
			
				// Code
			case CODE:
				switch (aname)
				{
						// The stack map table
					case "StackMap":
					case "StackMapTable":
						if (true)
							throw new Error("TODO");
						
						return;
					
						// Unknown
					default:
						break;
				}
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// {@squirreljme.error ED01 Reached EOF while skipping attribute
		// data.}
		for (int i = 0; i < len; i++)
			if (__di.read() < 0)
				throw new JITException("ED01");
	}
	
	/**
	 * Handles a single field.
	 *
	 * @param __cw The class writer to write to.
	 * @param __di The data input stream for the class file.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	private void __singleField(JITClassWriter __cw, DataInputStream __di)
		throws IOException, NullPointerException
	{
		// Check
		if (__cw == null || __di == null)
			throw new NullPointerException("NARG");
		
		// Read the flags for this field
		JITFieldFlags mf = __FlagDecoder__.__field(this._flags,
			input.readUnsignedShort());
		
		// Read the name
		JITConstantPool pool = this._pool;
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
			__singleAttribute(__cw, input, __AttributeFor__.FIELD);
		
		// Register the field
		__cw.field(mf, name, ndx, type, tdx, this._fieldcv);
		
		// End field
		__cw.endField();
	}
	
	/**
	 * Handles a single method.
	 *
	 * @param __cw The class writer to write to.
	 * @param __di The data input stream for the class file.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	private void __singleMethod(JITClassWriter __cw, DataInputStream __di)
		throws IOException, NullPointerException
	{
		// Check
		if (__cw == null || __di == null)
			throw new NullPointerException("NARG");
		
		// Read the flags for this method
		JITMethodFlags mf = __FlagDecoder__.__method(this._flags,
			input.readUnsignedShort());
		
		// Read the method name
		JITConstantPool pool = this._pool;
		int ni;
		JITConstantEntry ename = pool.get((ni = input.readUnsignedShort()));
		IdentifierSymbol name = IdentifierSymbol.of(
			ename.<String>get(true, String.class));
		
		// And the type
		int ti;
		JITConstantEntry etype = pool.get((ti = input.readUnsignedShort()));
		MethodSymbol type = MethodSymbol.of(
			etype.<String>get(true, String.class));
		
		// Clear before being used
		this._hitmcode = false;
		
		// Register method since code needs to be generated following this
		__cw.method(mf, name, ni, type, ti);
		
		// Needed for code
		this._mflags = mf;
		this._mtype = type;
		
		// Handle attributes
		int na = input.readUnsignedShort();
		for (int i = 0; i < na; i++)
			__singleAttribute(__cw, input, __AttributeFor__.METHOD);
		
		// {@squirreljme.error ED05 Abstract methods cannot have code.}
		boolean hascode = this._hitmcode;
		if (hascode == mf.isAbstract())
			throw new JITException("ED05");
		
		// If there is no code then indicate as such
		if (!hascode)
			__cw.noCode();
		
		// Clear
		this._mflags = null;
		this._mtype = null;
		
		// End method
		__cw.endMethod();
	}
}

