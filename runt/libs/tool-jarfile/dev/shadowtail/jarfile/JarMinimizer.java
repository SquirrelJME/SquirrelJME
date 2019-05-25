// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.lang.ClassDataV2;
import cc.squirreljme.runtime.cldc.vki.DefaultConfiguration;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.UsedString;
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
	 * Returns the class allocation size.
	 *
	 * @param __cl The class to get the allocation size for.
	 * @return The allocation size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/25
	 */
	public final int __classAllocSize(ClassName __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Lookup pre-cached size
		__BootInfo__ bi = this._boots.get(__cl);
		int rv = bi._allocsize,
			basep;
		if (rv != 0)
			return rv;
		
		// Calculate and cache size
		ClassName supercl = bi._class.superName();
		bi._baseoff = (basep = (supercl == null ? 0 :
			this.__classAllocSize(supercl)));
		bi._allocsize = (rv = basep + bi._class.header.ifbytes);
		return rv;
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
	 * @since 2019/05/25
	 */
	private final int __classFieldInstanceOffset(Initializer __init,
		ClassName __cl, FieldName __fn, FieldDescriptor __ft)
		throws NullPointerException
	{
		if (__cl == null || __fn == null || __ft == null)
			throw new NullPointerException("NARG");
		
		// Get the boot information
		__BootInfo__ bi = this._boots.get(__cl);
		
		// Determine offset to field
		this.__classAllocSize(__cl);
		return bi._baseoff + bi._class.field(false, __fn, __ft).offset;
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
		if (smemoff < 0)
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
			
			// Initialize static field values
			for (MinimizedField mf : bi._class.fields(true))
			{
				// No value here?
				Object val = mf.value;
				if (val == null)
					continue;
				
				// Write pointer for this field
				int wp = smemoff + mf.offset;
				
				// Write constant value
				switch (mf.type.toString())
				{
					case "B":
					case "Z":
						__init.memWriteByte(wp, ((Number)val).byteValue());
						break;
					
					case "S":
					case "C":
						__init.memWriteShort(wp, ((Number)val).shortValue());
						break;
					
					case "I":
						__init.memWriteInt(wp, ((Number)val).intValue());
						break;
					
					case "J":
						__init.memWriteLong(wp, ((Number)val).longValue());
						break;
					
					case "F":
						__init.memWriteInt(wp, Float.floatToRawIntBits(
							((Number)val).floatValue()));
						break;
					
					case "D":
						__init.memWriteLong(wp, Double.doubleToRawLongBits(
							((Number)val).doubleValue()));
						break;
					
					default:
						todo.TODO.note("Write constant value");
						break;
				}
			}
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
	 * @param __init The initializer.
	 * @param __cl The class to get the ID of.
	 * @return The ID of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	private final int __classId(Initializer __init, ClassName __cl)
		throws NullPointerException
	{
		if (__init == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Find boot info
		Map<ClassName, __BootInfo__> boots = this._boots;
		__BootInfo__ bi = boots.get(__cl);
		
		// If it is missing, this likely refers to an array or similar
		if (bi == null)
			boots.put(__cl, (bi = new __BootInfo__(
				Minimizer.minimizeAndDecode(
					ClassFile.special(__cl.field())), 0)));
		
		// If it has already been initialized use it
		int rv = bi._classdata;
		if (rv != 0)
			return rv;
		
		// Need the class data object to work with
		ClassName cdcln = new ClassName(
			"cc/squirreljme/runtime/cldc/lang/ClassDataV2");
		
		// Get information on the class data structure
		__BootInfo__ cdi = boots.get(cdcln);
		
		// Allocate pointer to the class data, then get the base pointer
		bi._classdata = (rv = __init.allocate(this.__classAllocSize(cdcln)));
		
		// Debug
		todo.DEBUG.note("Writing CDV2 for %s (%d, virt %d)", __cl, rv,
			rv + 1048576);
		
		// Initialize all fields for all super classes!
		for (ClassName at = cdcln, atsuper = null; at != null; at = atsuper)
		{
			// Get info for this
			__BootInfo__ ai = boots.get(at);
			
			// Get super class
			atsuper = ai._class.superName();
			
			// Base offset for this class
			this.__classAllocSize(at);
			int base = rv + ai._baseoff;
			
			// Go through and place field values
			for (MinimizedField mf : ai._class.fields(false))
			{
				// Get pointer value to write int
				int wp = base + mf.offset;
				
				// Debug
				todo.DEBUG.note("%s:%s @ %d", mf.name, mf.type, wp);
				
				// Depends on the type
				String key = mf.name + ":" + mf.type;
				switch (key)
				{
						// Class<?> pointer, allocated when needed
					case "classobjptr:I":
						__init.memWriteInt(
							wp, 0);
						break;
						
						// Magic number
					case "magic:I":
						__init.memWriteInt(
							wp, ClassDataV2.MAGIC_NUMBER);
						break;
						
						// Pointer to the class data in ROM
					case "miniptr:I":
						__init.memWriteInt(Modifier.JAR_OFFSET,
							wp, bi._classoffset);
						break;
						
						// Super class' ClassDataV2
					case "superclass:I":
						__init.memWriteInt(Modifier.RAM_OFFSET,
							wp, this.__classId(__init, atsuper));
						break;
						
						// VTable for special calls
					case "vtablespecial:I":
						todo.TODO.note("Write special VTable!");
						__init.memWriteInt(Modifier.RAM_OFFSET,
							wp, 0);
						break;
						
						// VTable for virtual calls
					case "vtablevirtual:I":
						todo.TODO.note("Write virtual VTable!");
						__init.memWriteInt(Modifier.RAM_OFFSET,
							wp, 0);
						break;
						
						// Base offset for the class
					case "base:S":
						this.__classAllocSize(__cl);
						__init.memWriteShort(
							wp, bi._baseoff);
						break;
						
						// Allocation size of the class
					case "size:S":
						__init.memWriteShort(
							wp, this.__classAllocSize(__cl));
						break;
						
						// Dimensions
					case "dimensions:B":
						__init.memWriteByte(
							wp, __cl.dimensions());
						break;
						
						// Cell size
					case "cellsize:S":
						{
							// Determine the cell size
							int cellsize;
							switch (__cl.toString())
							{
								case "[Z":
								case "[B":	cellsize = 1; break;
								case "[S":
								case "[C":	cellsize = 2; break;
								case "[J":
								case "[D":	cellsize = 8; break;
								default:	cellsize = 4; break;
							}
							
							// Write
							__init.memWriteShort(
								wp, cellsize);
						}
						break;
						
						// ClassData version
					case "version:B":
						__init.memWriteByte(
							wp, 2);
						break;
						
						// Is ClassDataV2 instance
					case "_class:I":
						__init.memWriteInt(Modifier.RAM_OFFSET,
							wp, this.__classId(__init, cdcln));
						break;
						
						// Reference count for this class data, should never
						// be freed
					case "_refcount:I":
						__init.memWriteInt(
							wp, 99999);
						break;
						
						// Thread owning the monitor (which there is none)
					case "_monitor:I":
						__init.memWriteInt(
							wp, 0);
						break;
					
					default:
						throw new todo.OOPS(key);
				}
			}
		}
		
		// Return the pointer to the class data
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
	 * @throws NullPointer-Exception On null arguments.
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
		
		// Get constant pool
		MinimizedClassFile mcl = bi._class;
		MinimizedPool pool = mcl.pool;
		
		// The JAR offset for the actual pool area
		int jarpooloff = bi._classoffset + mcl.header.pooloff;
		
		// Allocate and store space needed for the active pool contents
		int n = pool.size();
		bi._pooloffset = (rv = __init.allocate(n * 4));
		
		// Debug
		todo.DEBUG.note("Building %s at %d (virt %d)", __cl, rv, rv + 1048576);
		
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
							__init.memWriteInt(ep,
								this.__classFieldInstanceOffset(
									__init,
									af.field.className(),
									af.field.memberName(),
									af.field.memberType()));
					}
					break;
				
					// Class ID
				case CLASS_NAME:
					__init.memWriteInt(Modifier.RAM_OFFSET,
						ep, this.__classId(__init, (ClassName)pv));
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
			
			// Load class IDs for the byte array
			int clab = this.__classId(init, new ClassName("[B")),
				claab = this.__classId(init, new ClassName("[[B"));
			
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
			__dos.writeInt(clab);
			__dos.writeInt(claab);
		}
		
		// No boot data
		else
		{
			// Boot memory offset and size
			__dos.writeInt(0);
			__dos.writeInt(0);
			
			// Boot offset, pool, sfa, code, classidba, classidbaa
			__dos.writeInt(0);
			__dos.writeInt(0);
			__dos.writeInt(0);
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
