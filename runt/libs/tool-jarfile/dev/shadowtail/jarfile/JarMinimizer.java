// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.vki.DefaultConfiguration;
import cc.squirreljme.runtime.cldc.vki.FixedClassIDs;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.nncc.AccessedField;
import dev.shadowtail.classfile.nncc.ClassPool;
import dev.shadowtail.classfile.nncc.InvokedMethod;
import dev.shadowtail.classfile.nncc.MethodIndex;
import dev.shadowtail.classfile.nncc.UsedString;
import dev.shadowtail.classfile.xlate.InvokeType;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;

/**
 * This class is responsible for creating minimized Jar files which will then
 * be built into a ROM and used by SummerCoat and RatufaCoat.
 *
 * @since 2019/04/27
 */
public final class JarMinimizer
{
	/** Is this a boot JAR? */
	protected final boolean boot;
	
	/** The input JAR. */
	protected final VMClassLibrary input;
	
	/** Boot information for classes. */
	private final Map<ClassName, __BootInfo__> _boots;
	
	/** Static field pointer area. */
	private int _sfieldarea;
	
	/** Static field area next pointer. */
	private int _sfieldnext;
	
	/**
	 * Initializes the minimizer worker.
	 *
	 * @param __boot Is this a boot JAR?
	 * @param __in The input library.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private JarMinimizer(boolean __boot, VMClassLibrary __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.boot = __boot;
		this.input = __in;
		
		// Only used if this is a boot JAR
		if (__boot)
			this._boots = new HashMap<>();
		else
			this._boots = null;
	}
	
	/**
	 * Returns the address of the given class.
	 *
	 * @param __cl The class to look for.
	 * @return The address of the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/28
	 */
	public final int __classAddress(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return this._boots.get(new ClassName(__cl))._classoffset;
	}
	
	/**
	 * Returns the offset of the field.
	 *
	 * @param __init The initializer used.
	 * @param __cl The class name.
	 * @param __fn The field name.
	 * @param __ft The field type.
	 * @return The offset of the field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	private final int __classFieldStaticOffset(Initializer __init,
		ClassName __cl, FieldName __fn, FieldDescriptor __ft)
		throws NullPointerException
	{
		if (__cl == null || __fn == null || __ft == null)
			throw new NullPointerException("NARG");
		
		// Allocate area for static fields
		int sfieldarea = this._sfieldarea;
		if (sfieldarea == 0)
			this._sfieldarea = (sfieldarea = __init.allocate(
				DefaultConfiguration.MINIMUM_STATIC_FIELD_SIZE));
		
		// Get the class for the field
		__BootInfo__ bi = this._boots.get(__cl);
		
		// Need to allocate static field area?
		int smemoff = bi._smemoff;
		if (smemoff == 0)
		{
			// Use next pointer area
			int sfieldnext = this._sfieldnext;
			bi._smemoff = (smemoff = sfieldnext);
			
			// {@squirreljme.error BC03 Ran out of static field space.}
			int snext = sfieldnext + bi._class.header.sfsize;
			if (snext >= DefaultConfiguration.MINIMUM_STATIC_FIELD_SIZE)
				throw new RuntimeException("BC03");
			
			// Set next pointer area
			this._sfieldnext = snext;
		}
		
		// Try to get static field
		MinimizedField mf = bi._class.field(true, __fn, __ft);
		if (mf == null)
			return -1;
		
		// Return offset to it
		return smemoff + mf.offset;
	}
	
	/**
	 * Returns the ID of the class.
	 *
	 * @param __cl The class to get the ID of.
	 * @return The ID of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	private final int __classId(ClassName __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
			
		// Use a fixed class ID, if there is none then this is normalized to
		// -1 which means it must be handled later
		int rv = FixedClassIDs.of(__cl.toString());
		if (rv <= 0)
			return -1;
		return rv;
	}
	
	/**
	 * Returns the address of the given method.
	 *
	 * @param __cl The class to look in.
	 * @param __mn The method name.
	 * @param __mt The method type, if {@code null} then the type is
	 * disregarded.
	 * @return The address of the given method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/28
	 */
	private final int __classMethodCodeAddress(String __cl, String __mn,
		String __mt)
		throws NullPointerException
	{
		if (__cl == null || __mn == null)
			throw new NullPointerException("NARG");
		
		return this.__classMethodCodeAddress(new ClassName(__cl),
			new MethodName(__mn),
			(__mt == null ? null : new MethodDescriptor(__mt)));
	}
	
	/**
	 * Returns the address of the given method.
	 *
	 * @param __cl The class to look in.
	 * @param __mn The method name.
	 * @param __mt The method type, if {@code null} then the type is
	 * disregarded.
	 * @return The address of the given method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	private final int __classMethodCodeAddress(ClassName __cl, MethodName __mn,
		MethodDescriptor __mt)
		throws NullPointerException
	{
		if (__cl == null || __mn == null)
			throw new NullPointerException("NARG");
		
		// Get class method might be in
		__BootInfo__ bi = this._boots.get(__cl);
		MinimizedClassFile mcf = bi._class;
		
		// Lookup static first
		MinimizedMethod mm = mcf.method(true, __mn, __mt);
		if (mm != null)
			return bi._classoffset + mcf.header.smoff + mm.codeoffset;
		
		// Otherwise fallback to instance methods
		return bi._classoffset + mcf.header.imoff +
			mcf.method(false, __mn, __mt).codeoffset;
	}
	
	/**
	 * Returns the initialize sequence that is needed for execution.
	 *
	 * @param __poolp The output pointer of the initial constant pool.
	 * @param __ksfa Output static field area.
	 * @return The initialization sequence needed to start the kernel properly.
	 * @since 2019/04/30
	 */
	private final Initializer __init(int[] __poolp, int[] __ksfa)
		throws NullPointerException
	{
		if (__poolp == null || __ksfa == null)
			throw new NullPointerException("NARG");
		
		Map<ClassName, __BootInfo__> boots = this._boots;
		ClassName kn = new ClassName(
			"cc/squirreljme/runtime/cldc/vki/__Bootstrap__");
		
		// Initializer used for memory purposes
		Initializer rv = new Initializer();
		
		// Initialize the bootstrap pool, which may recursively initialize
		// more constant pools in order for other methods to be executed
		// properly
		int poolptr = this.__initPool(rv, kn);
		__poolp[0] = poolptr;
		
		// Allocate and setup kernel object pointer
		__ksfa[0] = this._sfieldarea;
		
		// Done with initialization, the ROM writer will dump the data needed
		// for the kernel to start properly
		return rv;
	}
	
	/**
	 * Initializes the constant pool for the given class.
	 *
	 * @param __init The initializer for the memory.
	 * @param __cl The class to initialize.
	 * @return The pointer of the class constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	private final int __initPool(Initializer __init, String __cl)
		throws NullPointerException
	{
		if (__init == null || __cl == null)
			throw new NullPointerException("NARG");
		
		return this.__initPool(__init, new ClassName(__cl));
	}
	
	/**
	 * Initializes the constant pool for the given class.
	 *
	 * @param __init The initializer for the memory.
	 * @param __cl The class to initialize.
	 * @return The pointer of the class constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	private final int __initPool(Initializer __init, ClassName __cl)
		throws NullPointerException
	{
		if (__init == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Treat as invalid for these
		if (__cl.isPrimitive() || __cl.isArray())
			return -1;
		
		// Only calculate it once
		__BootInfo__ bi = this._boots.get(__cl);
		int rv = bi._pooloffset;
		if (rv != 0)
			return rv;
		
		// Debug
		todo.DEBUG.note("Building %s", __cl);
		
		// Get constant pool
		MinimizedClassFile mcl = bi._class;
		MinimizedPool pool = mcl.pool;
		
		// The JAR offset for the actual pool area
		int jarpooloff = bi._classoffset + mcl.header.pooloff;
		
		// Allocate and store space needed for the active pool contents
		int n = pool.size();
		bi._pooloffset = (rv = __init.allocate(n * 4));
		
		// Process the constant pool
		for (int i = 1; i < n; i++)
		{
			// Get pool type and value
			MinimizedPoolEntryType pt = pool.type(i);
			Object pv = pool.get(i);
			int[] pp = pool.parts(i);
			
			// The pointer to this entry
			int ep = rv + (4 * i);
			
			// Depends on the part
			switch (pt)
			{
					// These have no effect on runtime
				case NULL:
				case METHOD_DESCRIPTOR:
					break;
					
					// These are initialized at the second stage bootstrap
				case CLASS_NAMES:
				case INTEGER:
				case FLOAT:
				case LONG:
				case DOUBLE:
				case METHOD_INDEX:
				case USED_STRING:
					__init.memWriteInt(ep, -1);
					break;
					
					// Write the pointer to the UTF data
				case STRING:
					{
						__init.memWriteInt(Modifier.JAR_OFFSET,
							ep, jarpooloff + pool.offset(i) + 4);
					}
					break;
					
					// Field being accessed
				case ACCESSED_FIELD:
					{
						// Static field offsets are always known
						AccessedField af = (AccessedField)pv;
						if (af.type().isStatic())
							__init.memWriteInt(ep,
								this.__classFieldStaticOffset(
									__init,
									af.field.className(),
									af.field.memberName(),
									af.field.memberType()));
						
						// Instance fields are not yet known
						else
							__init.memWriteInt(ep, -1);
					}
					break;
				
					// Class ID
				case CLASS_NAME:
					__init.memWriteInt(ep, this.__classId((ClassName)pv));
					break;
					
					// Class constant pool
				case CLASS_POOL:
					__init.memWriteInt(Modifier.RAM_OFFSET,
						ep, this.__initPool(__init, ((ClassPool)pv).name));
					break;
					
					// A method to be invoked, these are always direct pointer
					// references to methods
				case INVOKED_METHOD:
					{
						InvokedMethod im = (InvokedMethod)pv;
						__init.memWriteInt(Modifier.JAR_OFFSET,
							ep, this.__classMethodCodeAddress(im.handle.
								outerClass(), im.handle.name(),
								im.handle.descriptor()));
					}
					break;
				
				default:
					throw new todo.OOPS(pt.name());
			}
		}
		
		// Return the pointer where the pool was allocated
		return rv;
	}
	
	/**
	 * Processes the input JAR.
	 *
	 * @param __dos The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final void __process(DataOutputStream __dos)
		throws IOException, NullPointerException
	{
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// This is processed for all entries
		VMClassLibrary input = this.input;
		
		// If this is a boot JAR, this will later be used and pre-initialized
		// boot memory will be setup accordingly
		Map<ClassName, __BootInfo__> boots = this._boots;
		
		// Need list of resources to determine
		String[] rcnames = input.listResources();
		int numrc = rcnames.length;
		
		// Table of contents
		ByteArrayOutputStream tbaos = new ByteArrayOutputStream(2048);
		DataOutputStream tdos = new DataOutputStream(tbaos);
		
		// JAR content data
		ByteArrayOutputStream jbaos = new ByteArrayOutputStream(1048576);
		DataOutputStream jdos = new DataOutputStream(jbaos);
		
		// Relative offset from header and table of contents
		int reloff = MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC +
			(numrc * 12);
		
		// Go through and minimize/concat all resources
		for (int i = 0; i < numrc; i++)
		{
			String rc = rcnames[i];
			ClassName ofclass = null;
			
			// The resulting byte array containing data
			byte[] bytes;
			
			// Open resource
			boolean isclass = false;
			try (InputStream in = input.resourceAsStream(rc))
			{
				// Minimizing class
				if ((isclass = rc.endsWith(".class")))
					bytes = Minimizer.minimize(ClassFile.decode(in));
				
				// Plain resource, sent straight through
				else
				{
					// Copy bytes
					byte[] buf = new byte[512];
					try (ByteArrayOutputStream xbaos =
						new ByteArrayOutputStream(4096))
					{
						for (;;)
						{
							int ll = in.read(buf);
							
							if (ll < 0)
								break;
							
							xbaos.write(buf, 0, ll);
						}
						
						// Use this
						bytes = xbaos.toByteArray();
					}
				}
			}
			
			// Round data stream to 2 bytes (so string length is aligned)
			while ((jdos.size() & 1) != 0)
				jdos.write(0);
			
			// Record offset to resource name
			tdos.writeInt(reloff + jdos.size());
			jdos.writeUTF(rc);
			
			// Round data stream to 2 bytes
			while ((jdos.size() & 1) != 0)
				jdos.write(0);
			
			// Write offset to data stream and size
			int clpos;
			tdos.writeInt((clpos = (reloff + jdos.size())));
			tdos.writeInt(bytes.length);
			
			// If boot processing is going to be done, we need to
			// know about this class file if it is one
			if (boots != null && isclass)
			{
				// Decode it
				MinimizedClassFile cf = MinimizedClassFile.decode(bytes);
				
				// Store loaded for later boot usage
				boots.put(cf.thisName(), new __BootInfo__(cf, clpos));
			}
			
			// Then write the actual data stream
			jdos.write(bytes);
		}
		
		// Write header
		__dos.writeInt(MinimizedJarHeader.MAGIC_NUMBER);
		
		// Number of resources
		__dos.writeInt(numrc);
		
		// Offset to table of contents
		__dos.writeInt(MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC);
		
		// Building pre-boot state
		if (boots != null)
		{
			// Round data stream to 4 bytes
			while ((jdos.size() & 3) != 0)
				jdos.write(0);
			
			// Write address to the boot table
			int baseaddr,
				injaraddr;
			__dos.writeInt((injaraddr = (reloff + (baseaddr = jdos.size()))));
			
			// Debug
			todo.DEBUG.note("Boot RAM written at @%08x", injaraddr);
			
			// Initialize and write startup memory
			int[] poolptr = new int[1],
				ksfa = new int[1];
			Initializer init = this.__init(poolptr, ksfa);
			
			// Write boot memory
			byte[] bootmem = init.toByteArray();
			jdos.write(bootmem);
			
			// Write length of the boot RAM initialize area
			__dos.writeInt(bootmem.length);
			
			// Bootstrap pool, static field pointer offset, and the offset
			// to the bootstrap's code
			__dos.writeInt(poolptr[0]);
			__dos.writeInt(ksfa[0]);
			__dos.writeInt(this.__classMethodCodeAddress(
				"cc/squirreljme/runtime/cldc/vki/__Bootstrap__",
				"__start",
				null));
		}
		
		// No boot data
		else
		{
			// Boot memory offset and size
			__dos.writeInt(0);
			__dos.writeInt(0);
			
			// Boot pool, sfa, and code
			__dos.writeInt(0);
			__dos.writeInt(0);
			__dos.writeInt(0);
		}
		
		// Write table of contents and JAR data
		__dos.write(tbaos.toByteArray());
		__dos.write(jbaos.toByteArray());
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @return The resulting byte array of minimization.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public static final byte[] minimize(boolean __boot, VMClassLibrary __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Write to a temporary byte array
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1048576))
		{
			// Perform minimization
			JarMinimizer.minimize(__boot, __in, baos);
			
			// Return the generated array
			return baos.toByteArray();
		}
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @param __out The stream where JAR data will be placed.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public static final void minimize(boolean __boot, VMClassLibrary __in,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Use helper class
		new JarMinimizer(__boot, __in).__process(new DataOutputStream(__out));
	}
}
