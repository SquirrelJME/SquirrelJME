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
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This performs the decoding of the class file format.
 *
 * @since 2016/06/28
 */
final class ClassDecoder
	extends __HasAttributes__
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
	protected final JITOutputConfig.Immutable _config;
	
	/** Rewrites of class names. */
	private final JITClassNameRewrite[] _rewrites;
	
	/** The version of this class file. */
	private volatile __ClassVersion__ _version;
	
	/** The constant pool of the class. */
	private volatile ClassConstantPool _pool;
	
	/** Class flags. */
	private volatile ClassClassFlags _flags;
	
	/** The name of this class. */
	private volatile ClassNameSymbol _classname;
	
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
	ClassDecoder(JIT __jit, JITNamespaceWriter __ns, DataInputStream __dis)
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
		this._config = config;
		this._rewrites = config.classNameRewrites();
	}
	
	/**
	 * This performs the actual decoding of the class file.
	 *
	 * @param __jo The JIT output to use, when the name of the class is known
	 * then will be opened and written to during the decoding process.
	 * @throws IOException On read errors.
	 * @throws ClassFormatException If the class file format is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	final void __decode(JITOutput __jo)
		throws IOException, ClassFormatException, NullPointerException
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
			throw new ClassFormatException(String.format("ED12 %08x", fail));
		
		// {@squirreljme.error ED13 The version number of the input class file
		// is not valid. (The version number)}
		int cver = input.readShort() | (input.readShort() << 16);
		__ClassVersion__ version = __ClassVersion__.findVersion(cver);
		this._version = version;
		if (version == null)
			throw new ClassFormatException(String.format("ED13 %d.%d", cver >>> 16,
				(cver & 0xFFFF)));
		
		// Decode the constant pool
		ClassConstantPool pool = new ClassConstantPool(input, this);
		this._pool = pool;
		
		// Read the flags for this class
		ClassClassFlags cf = __FlagDecoder__.__class(input.readUnsignedShort());
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
				throw new ClassFormatException(String.format("ED0m %s %s", clname,
					suname));
			
			// {@squirreljme.error ED0n Interfaces must extend the Object
			// class. (Class flags; The super-class name)}
			if (cf.isInterface() && !suname.equals(_OBJECT_CLASS))
				throw new ClassFormatException(String.format("ED0n %s %s", cf,
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
				new __FieldDecoder__(cw, input, pool, cf).__decode();
			
			// Handle methods
			int mcount = input.readUnsignedShort();
			cw.methodCount(mcount);
			for (int i = 0; i < mcount; i++)
				new __MethodDecoder__(cw, input, pool, cf, this).__decode();
			
			// Handle class attributes
			int na = input.readUnsignedShort();
			for (int i = 0; i < na; i++)
				__readAttribute(pool, input);
			
			// End class
			cw.endClass();
		}
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
		
		// No class attributes are handled at all, so ignore them all
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
}

