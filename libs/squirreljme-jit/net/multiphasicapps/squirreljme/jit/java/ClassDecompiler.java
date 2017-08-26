// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.jit.bin.TemporaryBinary;
import net.multiphasicapps.squirreljme.jit.hil.HighLevelProgram;
import net.multiphasicapps.squirreljme.jit.JITProcessor;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.symbols.ClassStructure;
import net.multiphasicapps.squirreljme.jit.symbols.Symbols;
import net.multiphasicapps.squirreljme.jit.verifier.VerificationChecks;

/**
 * This class reads the Java class file format and then passes that to the
 * JIT compiler which recompiles the given class.
 *
 * @since 2017/05/29
 */
public final class ClassDecompiler
{
	/** The magic number of the class file. */
	private static final int _MAGIC_NUMBER =
		0xCAFEBABE;
	
	/** The owning configuration. */
	protected final JITConfig config;
	
	/** The input stream containing the class data. */
	protected final DataInputStream in;
	
	/** The processor being used. */
	protected final JITProcessor processor;
	
	/**
	 * Creates an instance of the compiler for the given class file.
	 *
	 * @param __jp The JIT Processor which is processing this class.
	 * @param __is The stream containing the class data to compile.
	 * @return The compilation task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/02
	 */
	public ClassDecompiler(JITProcessor __jp, InputStream __is)
		throws NullPointerException
	{
		// Check
		if (__is == null || __jp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __jp.config();
		this.processor = __jp;
		this.in = new DataInputStream(__is);
	}
	
	/**
	 * Runs the class decompiler.
	 *
	 * @return The resulting unit.
	 * @since 2017/06/02
	 */
	public void run()
	{
		try
		{
			JITProcessor processor = this.processor;
			Symbols symbols = processor.symbols();
			VerificationChecks verifier = processor.verifier();
			
			// {@squirreljme.error JI06 Invalid magic number read from the
			// start of the class file. (The read magic number; The expected
			// magic number)}
			DataInputStream in = this.in;
			int mnum;
			if ((mnum = in.readInt()) != _MAGIC_NUMBER)
				throw new JITException(String.format("JI06 %08x %08x", mnum,
					_MAGIC_NUMBER));
			
			// {@squirreljme.error JI08 The version number of the input class
			// file is not valid. (The version number)}
			int cver = in.readShort() | (in.readShort() << 16);
			ClassVersion version = ClassVersion.findVersion(cver);
			if (version == null)
				throw new JITException(String.format("JI08 %d.%d",
					cver >>> 16, (cver & 0xFFFF)));
			
			// Decode the class
			Pool pool = new Pool(in);
			
			// Decode flags
			ClassFlags classflags = new ClassFlags(in.readUnsignedShort());
			
			// Name of the current class
			ClassName thisname = pool.<ClassName>require(ClassName.class,
				in.readUnsignedShort());
			
			// Build new unit
			ClassStructure struct = symbols.createClass(thisname);
			struct.setFlags(classflags);
			
			// Super class name
			ClassName supername = pool.<ClassName>get(ClassName.class,
				in.readUnsignedShort());
			struct.setSuperClass(supername);
			
			// Verify extend
			if (supername != null)
				verifier.canExtend(thisname, supername);
			
			// Read interfaces
			int icount = in.readUnsignedShort();
			ClassName[] interfaces = new ClassName[icount];
			for (int i = 0; i < icount; i++)
			{
				ClassName inf;
				interfaces[i] = (inf = pool.<ClassName>require(ClassName.class,
					in.readUnsignedShort()));
				
				// Verify implements
				verifier.canImplement(thisname, inf);
			}
			struct.setInterfaces(interfaces);
			
			// Read in fields
			String[] attr = new String[1];
			int nf = in.readUnsignedShort();
			for (int i = 0; i < nf; i++)
			{
				FieldFlags flags = new FieldFlags(classflags,
					in.readUnsignedShort());
				FieldName name = new FieldName(
					pool.<UTFConstantEntry>require(UTFConstantEntry.class,
					in.readUnsignedShort()).toString());
				FieldDescriptor type = new FieldDescriptor(
					pool.<UTFConstantEntry>require(UTFConstantEntry.class,
					in.readUnsignedShort()).toString());
				
				// Handle attributes
				int na = in.readUnsignedShort();
				for (int j = 0; j < na; j++)
					try (DataInputStream ai = __nextAttribute(in, pool, attr))
					{
						// Only care about the constant value
						if (!"ConstantValue".equals(attr[0]))
							continue;
						
						throw new todo.TODO();
					}
				
				throw new todo.TODO();
			}
			
			// Read in methods
			int nm = in.readUnsignedShort();
			for (int i = 0; i < nm; i++)
			{
				MethodFlags flags = new MethodFlags(classflags,
					in.readUnsignedShort());
				MethodName name = new MethodName(
					pool.<UTFConstantEntry>require(UTFConstantEntry.class,
					in.readUnsignedShort()).toString());
				MethodDescriptor type = new MethodDescriptor(
					pool.<UTFConstantEntry>require(UTFConstantEntry.class,
					in.readUnsignedShort()).toString());
				
				// Handle attributes
				int na = in.readUnsignedShort();
				HighLevelProgram cf = null;
				for (int j = 0; j < na; j++)
					try (DataInputStream ai = __nextAttribute(in, pool, attr))
					{
						// Only care about the code attribute
						if (!"Code".equals(attr[0]))
							continue;
						
						// {@squirreljme.error JI1b The specified method
						// contains more than one code attribute. (The current
						// class; The method name; The method type)}
						if (cf != null)
							throw new JITException(String.format(
								"JI1b %s %s %s", thisname, name, type));
						
						// Run decompiler
						CodeDecompiler cd = new CodeDecompiler(flags, name,
							type, ai, pool, processor, version, thisname);
						cf = cd.run();
					}
					
				// {@squirreljme.error JI1c The specified method does not have
				// the correct matching for abstract and if code exists or not.
				// (The current
				// class; The method name; The method type; The method flags)}
				if ((cf == null) != (flags.isAbstract() | flags.isNative()))
					throw new JITException(String.format(
						"JI1c %s %s %s", thisname, name, type, flags));
				
				throw new todo.TODO();
			}
			
			// Handle attributes
			if (true)
				throw new todo.TODO();
			
			// {@squirreljme.error JI12 Expected end of the class to follow the
			// attributes in the class. (The name of this class)}
			if (in.read() >= 0)
				throw new JITException(String.format("JI12 %s", thisname));
			
			throw new todo.TODO();
		}
		
		// {@squirreljme.error JI07 Read error reading the class.}
		catch (IOException e)
		{
			throw new JITException("JI07", e);
		}
	}
	
	/**
	 * Reads the next attribute from the class.
	 *
	 * @param __in The input stream where bytes come from.
	 * @param __pool The constant pool.
	 * @param __aname The output name of the attribute which was just read.
	 * @return The stream to the attribute which just has been read.
	 * @throws IOException On read errors.
	 * @throws JITException If the attribute is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/09
	 */
	static DataInputStream __nextAttribute(DataInputStream __in,
		Pool __pool, String[] __aname)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__aname == null)
			throw new NullPointerException("NARG");
		
		// The name is not parsed here
		__aname[0] = __pool.<UTFConstantEntry>require(UTFConstantEntry.class,
			__in.readUnsignedShort()).toString();
		
		// {@squirreljme.error JI1a Attribute exceeds 2GiB in length. (The
		// size of the attribute)}
		int len = __in.readInt();
		if (len < 0)
			throw new JITException(String.format("JI1a %d",
				len & 0xFFFFFFFFL));
		
		// Setup reader
		return new DataInputStream(new SizeLimitedInputStream(__in, len, true,
			false));
	}
}

