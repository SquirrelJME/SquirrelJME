// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.vki.FixedClassIDs;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedPool;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.nncc.ClassPool;
import dev.shadowtail.classfile.nncc.WhereIsThis;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;

/**
 * This class is responsible for creating minimized Jar files which will then
 * be built into a ROM and used by SummerCoat and RatufaCoat.
 *
 * @since 2019/04/27
 */
public final class JarMinimizer
{
	/** Maximum permitted boot RAM size. */
	public static final int MAXIMUM_BOOT_RAM_SIZE =
		1048576;
	
	/** Maximum number of boot classes. */
	public static final int MAX_BOOT_CLASSES =
		128;
	
	/** Is this a boot JAR? */
	protected final boolean boot;
	
	/** The input JAR. */
	protected final VMClassLibrary input;
	
	/** Mini-classes, if this is a boot Jar. */
	private final Map<ClassName, MinimizedClassFile> _minicl;
	
	/** The offset to the mini-class data. */
	private final Map<ClassName, Integer> _minicloff;
	
	/** Classes which have been booted. */
	private final Map<ClassName, __BootClass__> _bootclasses;
	
	/** Intern string pointer table. */
	private final Map<String, Integer> _stringintern;
	
	/** Memory operations. */
	private final List<MemoryOperation> _mops;
	
	/** Allocator for boot memory. */
	private final StaticAllocator _alloc;
	
	/** Actual boot RAM. */
	private final byte[] _bram;
	
	/** Class table pointer. */
	private int _ctableptr;
	
	/** Next CTable position. */
	private int _ctablenext =
		FixedClassIDs.MAX_FIXED;
	
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
		{
			this._minicl = new HashMap<>();
			this._minicloff = new HashMap<>();
			this._stringintern = new HashMap<>();
			this._bootclasses = new HashMap<>();
			this._alloc = new StaticAllocator(0);
			this._bram = new byte[MAXIMUM_BOOT_RAM_SIZE];
			this._mops = new ArrayList<>();
		}
		
		// Not used
		else
		{
			this._minicl = null;
			this._minicloff = null;
			this._stringintern = null;
			this._bootclasses = null;
			this._alloc = null;
			this._bram = null;
			this._mops = null;
		}
	}
	
	/**
	 * Boots the given class and returns the offset to the class constant
	 * pool.
	 *
	 * @param __cn The class to initialize.
	 * @return The boot class information.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final __BootClass__ __bootClass(String __cn)
		throws IOException, NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		return this.__bootClass(new ClassName(__cn));
	}
	
	/**
	 * Boots the given class and returns the offset to the class constant
	 * pool.
	 *
	 * @param __cn The class to initialize.
	 * @return The boot class information.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final __BootClass__ __bootClass(ClassName __cn)
		throws IOException, NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Allocator for static things
		StaticAllocator alloc = this._alloc;
		
		// If the class has already been booted, then use it!
		Map<ClassName, __BootClass__> bootclasses = this._bootclasses;
		__BootClass__ rv = bootclasses.get(__cn);
		if (rv != null)
			return rv;
		
		// Debug
		todo.DEBUG.note("Pre-Loading %s...", __cn);
		
		// Load minimized class information
		MinimizedClassFile minicf = (__cn.isPrimitive() || __cn.isArray() ?
			Minimizer.minimizeAndDecode(ClassFile.special(__cn.field())) :
			this._minicl.get(__cn));
		
		// Quickly setup boot information for recursive purposes
		Integer xmoff = this._minicloff.get(__cn);
		int moff;
		rv = new __BootClass__(minicf, (moff = (xmoff == null ? 0 : xmoff)));
		bootclasses.put(__cn, rv);
		
		// Allocate class table?
		int ctableptr = this._ctableptr;
		if (ctableptr == 0)
		{
			ctableptr = alloc.allocate(4 * MAX_BOOT_CLASSES);
			this._ctableptr = ctableptr;
		}
		
		// Find spot where this class belongs in the class table
		int ctablespot = FixedClassIDs.of(__cn.toString());
		if (ctablespot <= 0)
		{
			ctablespot = this._ctablenext++;
			
			// {@squirreljme.error BC02 Out of class table index.}
			if (ctablespot >= MAX_BOOT_CLASSES)
				throw new RuntimeException("BC02");
		}
		
		// Set spot
		rv.ctablespot = ctablespot;
		
		// Allocate memory for the constant pool
		int numpool = minicf.header.poolcount,
			poolptr = alloc.allocate(4 * numpool);
		
		// Set pool pointer
		rv.poolptr = poolptr;
		
		// Load super class
		ClassName supername = minicf.superName();
		if (supername != null)
		{
			// Find the Class
			__BootClass__ superclass;
			rv.superclass = (superclass = this.__bootClass(supername));
			
			// Field base is based on super's size
			rv.ifieldbase = superclass.ifieldsize;
			rv.ifieldsize = rv.ifieldbase + minicf.header.ifbytes;
		}
		
		// Use base size for Object class
		else
			rv.ifieldsize = 12;
		
		// Load interfaces
		ClassNames interfacenames = minicf.interfaceNames();
		int numinterfaces = interfacenames.size();
		__BootClass__[] interfaces = new __BootClass__[numinterfaces];
		rv.interfaces = interfaces;
		for (int i = 0; i < numinterfaces; i++)
			interfaces[i] = this.__bootClass(interfacenames.get(i));
		
		// Offset to the code area in this minimized method
		int scodebase = moff + minicf.header.smoff,
			icodebase = moff + minicf.header.imoff;
		
		// Initialize all the various pool entries
		MinimizedPool pool = minicf.pool;
		for (int i = 1; i < numpool; i++)
		{
			// Type and original value
			MinimizedPoolEntryType type = pool.type(i);
			Object pval = pool.get(i);
			int numpart = pool.partCount(i);
			
			// Debug
			todo.DEBUG.note("Init pool %d %s: %s", i, type, pval);
			
			// The address where this will write to
			int pvaddr = poolptr + (4 * i);
			
			// Depends on the type
			switch (type)
			{
					// Do nothing
				case NULL:
				case STRING:
				case METHOD_DESCRIPTOR:
					break;
					
					// Name of class, refer to class index
				case CLASS_NAME:
					this.__memWriteInt(null, pvaddr, this.__bootClass(
						(ClassName)pval).ctablespot);
					break;
					
					// The pool of another classe
				case CLASS_POOL:
					this.__memWriteInt(MemoryOperationType.OFFSET_RAM, pvaddr,
						this.__bootClass(((ClassPool)pval).name).poolptr);
					break;
					
					// Names of classes
				case CLASS_NAMES:
					{
						// Allocate pointer where class IDs are
						int cnsp = alloc.allocate(2 * (numpart + 1));
						
						// Write class IDs
						for (int j = 0; j < numpart; j++)
							this.__memWriteShort(null, cnsp + (2 * j),
								this.__bootClass(((ClassNames)pval).get(j)).
								ctablespot);
						
						// Write address of table
						this.__memWriteInt(MemoryOperationType.OFFSET_RAM,
							pvaddr, cnsp);
					}
					break;
					
					// String that is used in code
				case USED_STRING:
					if (true)
						throw new todo.TODO();
					this.__memWriteInt(MemoryOperationType.OFFSET_RAM, pvaddr,
						this.__stringIntern((String)pval));
					break;
					
					// Where is this method?
				case WHERE_IS_THIS:
					{
						// Get method index part
						int id = pool.part(i, 0);
						
						// Instance method? And get the index
						boolean isinstance = ((id &
							WhereIsThis.INSTANCE_BIT) != 0);
						int mdx = (id & (~WhereIsThis.INSTANCE_BIT));
						
						// Set this to the where offset
						this.__memWriteInt(MemoryOperationType.OFFSET_JAR,
							pvaddr, (isinstance ? icodebase : scodebase) +
							minicf.methods(!isinstance)[mdx].whereoffset);
					}
					break;
				
				default:
					throw new todo.OOPS(type.name());
			}
		}
		
		// Initialized okay, return the used info
		return rv;
	}
	
	/**
	 * Writes to memory.
	 *
	 * @param __t The operation type.
	 * @param __a The address to write to.
	 * @param __v The value to write
	  *@since 2019/04/27
	 */
	private final void __memWriteInt(MemoryOperationType __t, int __a, int __v)
	{
		// Force normal write
		if (__t == null)
			__t = MemoryOperationType.NORMAL;
		
		// Store init operation needed
		if (__t != MemoryOperationType.NORMAL)
			this._mops.add(new MemoryOperation(__t, 4, __a));
		
		// Write value at address
		byte[] bram = this._bram;
		bram[__a++] = (byte)(__v >>> 24);
		bram[__a++] = (byte)(__v >>> 16);
		bram[__a++] = (byte)(__v >>> 8);
		bram[__a++] = (byte)(__v);
	}
	
	/**
	 * Writes to memory.
	 *
	 * @param __t The operation type.
	 * @param __a The address to write to.
	 * @param __v The value to write
	  *@since 2019/04/27
	 */
	private final void __memWriteShort(MemoryOperationType __t, int __a, int __v)
	{
		// Force normal write
		if (__t == null)
			__t = MemoryOperationType.NORMAL;
		
		// Store init operation needed
		if (__t != MemoryOperationType.NORMAL)
			this._mops.add(new MemoryOperation(__t, 2, __a));
		
		// Write value at address
		byte[] bram = this._bram;
		bram[__a++] = (byte)(__v >>> 8);
		bram[__a++] = (byte)(__v);
	}
	
	/**
	 * Interns the given string.
	 *
	 * @param __s The string to intern.
	 * @return The pointer to the string intern.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final int __stringIntern(String __s)
		throws IOException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// If the string has already been interned then use that
		Map<String, Integer> stringintern = this._stringintern;
		Integer rv = stringintern.get(__s);
		if (rv != null)
			return rv;
		
		// Determine if we need to store this as characters?
		boolean ischar = false;
		int strlen = __s.length();
		for (int i = 0; i < strlen; i++)
			if (__s.charAt(i) > 255)
			{
				ischar = true;
				break;
			}
		
		// Need the sequence class to know where to put this string
		__BootClass__ aascl = this.__bootClass((ischar ?
			"cc/squirreljme/runtime/cldc/string/CharArraySequence" :
			"cc/squirreljme/runtime/cldc/string/ByteArraySequence")),
			strcl = this.__bootClass("java/lang/String");
		
		throw new todo.TODO();
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
		Map<ClassName, MinimizedClassFile> minicl = this._minicl;
		Map<ClassName, Integer> minicloff = this._minicloff;
		
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
			(4 * 12);
		
		// Go through and minimize/concat all resources
		for (int i = 0; i < numrc; i++)
		{
			String rc = rcnames[i];
			ClassName ofclass = null;
			
			// The resulting byte array containing data
			byte[] bytes;
			
			// Open resource
			try (InputStream in = input.resourceAsStream(rc))
			{
				// Minimizing class
				if (rc.endsWith(".class"))
				{
					// Minimize file data
					bytes = Minimizer.minimize(ClassFile.decode(in));
					
					// If boot processing is going to be done, we need to
					// know about this class file
					if (minicl != null)
					{
						// Decode it
						MinimizedClassFile cf =
							MinimizedClassFile.decode(bytes);
						
						// And cache it for later
						minicl.put((ofclass = cf.thisName()), cf);
					}
				}
				
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
			
			// Round data stream to 4 bytes
			while ((jdos.size() & 1) != 0)
				jdos.write(0);
			
			// Write offset to data stream and size
			int clpos;
			tdos.writeInt((clpos = (reloff + jdos.size())));
			tdos.writeInt(bytes.length);
			
			// Store offset to mini class data in the Jar
			if (minicloff != null)
				minicloff.put(ofclass, clpos);
			
			// Then write the actual data stream
			jdos.write(bytes);
		}
		
		// Write header
		__dos.writeInt(MinimizedJarHeader.MAGIC_NUMBER);
		
		// Number of resources
		__dos.writeInt(numrc);
		
		// Offset to table of contents
		__dos.writeInt(MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC);
		
		// Boot RAM and Init RAM
		byte[] bram = this._bram;
		
		// Building pre-boot state
		if (minicl != null)
		{
			// The most important class is the kernel so that will get booted
			__BootClass__ kboot = this.__bootClass(
				"cc/squirreljme/runtime/cldc/vki/Kernel");
			
			throw new todo.TODO();
		}
		
		// No boot data
		else
		{
			// Boot pool offset
			__dos.writeInt(0);
			
			// Boot RAM off+size
			__dos.writeInt(0);
			__dos.writeInt(0);
			
			// Init RAM off+size
			__dos.writeInt(0);
			__dos.writeInt(0);
		}
		
		// Write table of contents and JAR data
		__dos.write(tbaos.toByteArray());
		__dos.write(jbaos.toByteArray());
		
		// Write boot RAM and init RMA?
		if (bram != null)
		{
			// Boot RAM
			__dos.write(bram);
			
			// Init RAM
			throw new todo.TODO();
		}
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
