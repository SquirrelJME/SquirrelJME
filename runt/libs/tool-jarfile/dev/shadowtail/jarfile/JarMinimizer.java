// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.ClassInfo;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.vm.VMClassLibrary;
import dev.shadowtail.classfile.mini.DualPoolEncoder;
import dev.shadowtail.classfile.mini.DualPoolEncodeResult;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.UsedString;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.io.TableSectionOutputStream;

/**
 * This class is responsible for creating minimized Jar files which will then
 * be built into a ROM and used by SummerCoat and RatufaCoat.
 *
 * @since 2019/04/27
 */
public final class JarMinimizer
{
	/**
	 * {@squirreljme.property dev.shadowtail.jarfile.debug=boolean
	 * Should debugging text be printed for the JAR minimizer?}
	 */
	static final boolean _ENABLE_DEBUG =
		Boolean.getBoolean("dev.shadowtail.jarfile.debug");
	
	/** The state of the bootstrap. */
	protected final BootstrapState bootstrap;
	
	/** Is this a boot JAR? */
	protected final boolean boot;
	
	/** The input JAR. */
	protected final VMClassLibrary input;
	
	/** The dual-combined constant pool. */
	protected final DualClassRuntimePoolBuilder dualpool;
	
	/** Are we using our own dual pool? */
	protected final boolean owndualpool;
	
	/** The resulting JAR header. */
	private MinimizedJarHeader _jheader;
	
	/**
	 * Initializes the minimizer worker.
	 *
	 * @param __dp The global dual constant pool, may be {@code null} to not
	 * use the pack-file global one.
	 * @param __boot Is this a boot JAR?
	 * @param __in The input library.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private JarMinimizer(DualClassRuntimePoolBuilder __dp, boolean __boot,
		VMClassLibrary __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
			
		this.boot = __boot;
		this.input = __in;
		
		// Use the passed pool if it was passed, but otherwise just use one
		// in the event one was not passed through (uses our own pool)
		DualClassRuntimePoolBuilder usedp;
		this.dualpool = (usedp = (__boot ? null :
			(__dp != null ? __dp : new DualClassRuntimePoolBuilder())));
		this.owndualpool = (usedp == null);
		
		// Setup bootstrap, but only if booting
		this.bootstrap = (__boot ? new BootstrapState() : null);
	}
	
	/**
	 * Returns the method base for the class.
	 *
	 * @param __cl The class to get the index base of.
	 * @return The method base.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	private final int __classMethodBase(ClassName __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Get class method might be in
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		LoadedClassInfo bi = boots.get(__cl);
		
		// If this has no super class, then the base is zero
		ClassName supername = bi._class.superName();
		if (supername == null)
			return 0;
		
		// Otherwise it is the number of available methods in the super class
		return this.__classMethodSize(supername);
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
		LoadedClassInfo bi = this._boots.get(__cl);
		MinimizedClassFile mcf = bi._class;
		
		// Lookup static first
		MinimizedMethod mm = mcf.method(true, __mn, __mt);
		if (mm != null)
			return bi._classoffset + mcf.header.smoff + mm.codeoffset;
		
		// Otherwise fallback to instance methods
		// {@squirreljme.error BC07 Could not locate the given method.
		// (The class; The name; The type)}
		mm = mcf.method(false, __mn, __mt);
		if (mm == null)
			throw new InvalidClassFormatException(
				String.format("BC07 %s %s %s", __cl, __mn, __mt));
		return bi._classoffset + mcf.header.imoff + mm.codeoffset;
	}
	
	/**
	 * Returns the method index for the given method.
	 *
	 * @param __cl The class the method is in.
	 * @param __mn The name of the method.
	 * @param __mt The descriptor of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	private final int __classMethodIndex(ClassName __cl,
		MethodName __mn, MethodDescriptor __mt)
		throws NullPointerException
	{
		if (__cl == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		// Primitives and array types are not real, so just have everything
		// about them point to object!
		if (__cl.isPrimitive() || __cl.isArray())
			return this.__classMethodIndex(new ClassName("java/lang/Object"),
				__mn, __mt);
		
		// Seeker class
		ClassName seeker = __cl;
		
		// Recursively scan for the method in all classes
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		while (seeker != null)
		{
			// Get boot information
			LoadedClassInfo bi = boots.get(seeker);
			MinimizedClassFile mcf = bi._class;
			
			// Is class found in this method?
			MinimizedMethod mm = mcf.method(false, __mn, __mt);
			if (mm != null)
				return this.__classMethodBase(seeker) + mm.index;
			
			// Go above
			seeker = mcf.superName();
		}
		
		// {@squirreljme.error BC08 Could not locate the method. (The class;
		// Method name; Method type)}
		throw new InvalidClassFormatException(
			String.format("BC08 %s %s %s", __cl, __mn, __mt));
	}
	
	/**
	 * Returns the number of methods to use in the method table.
	 *
	 * @param __cl The class to get the method size of.
	 * @return The method size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	private final int __classMethodSize(ClassName __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Get class method might be in
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		LoadedClassInfo bi = boots.get(__cl);
		
		// If there is no super class it is just the count
		ClassName supername = bi._class.superName();
		if (supername == null)
			return bi._class.header.imcount;
		
		// Otherwise include the super class count as well
		return this.__classMethodSize(supername) + bi._class.header.imcount;
	}
	
	/**
	 * This allocations and returns the vtable for this class.
	 *
	 * @param __init The initializer.
	 * @param __cl The class to build the vtable for.
	 * @return The VTable address and the pool table address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	private final int[] __classVTable(Initializer __init, ClassName __cl)
		throws NullPointerException
	{
		if (__init == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Primitive types and array types do not exist so they just use the
		// same vtable as Object
		if (__cl.isPrimitive() || __cl.isArray())
			return this.__classVTable(__init,
				new ClassName("java/lang/Object"));
		
		// We need boot information to get class information!
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		
		// Did we already make the VTable for this? This will happen in the
		// event arrays or primitives are virtualized
		LoadedClassInfo selfbi = boots.get(__cl);
		int rv = selfbi._vtable;
		if (rv >= 0)
			return new int[]{rv, selfbi._vtablepool};
		
		// Build array of all the classes that are used in the method and
		// super class chain
		List<LoadedClassInfo> classes = new ArrayList<>();
		for (ClassName at = __cl, su = null; at != null; at = su)
		{
			// Load class info for this
			LoadedClassInfo bi = boots.get(at);
			
			// Add this class to the start of the chain (so super classes
			// go in at the start)
			classes.add(0, bi);
			
			// Set super class for next run
			su = bi._class.superName();
		}
		
		// Abstract methods which are not bound to anything will instead
		// be bound to this method which indicates failure.
		int jpvc = this.__classMethodCodeAddress(
			"cc/squirreljme/jvm/JVMFunction", "jvmPureVirtualCall", "()V"),
			jpvp = this.__initPool(__init, "cc/squirreljme/jvm/JVMFunction");
		
		// Initialize method table with initial values
		int numv = this.__classMethodSize(__cl);
		List<Integer> entries = new ArrayList<>(numv);
		for (int i = 0; i < numv; i++)
			entries.add(jpvc);
		
		// Also there need to be pointers to the constant pool as well
		List<Integer> pools = new ArrayList<>(numv);
		for (int i = 0; i < numv; i++)
			pools.add(jpvp);
		
		// Go through every class and fill the table information
		// The class index here will be used as a stopping point since methods
		// at higher points will stop here
		for (int ci = 0, cn = classes.size(); ci < cn; ci++)
		{
			// Get the class to scan through
			LoadedClassInfo mbi = classes.get(ci);
			MinimizedClassFile mcf = mbi._class;
			
			// Get the VTable base for this class
			ClassName mcfname = mcf.thisName();
			int vb = this.__classMethodBase(mcfname);
			
			// Process each interface method in this class
			for (MinimizedMethod mm : mcf.methods(false))
			{
				// Determine the actual index of this entry
				int vat = vb + mm.index;
				
				// Private visibility modifies how lookup is done
				boolean ispriv = mm.flags().isPrivate(),
					ispkpriv = mm.flags().isPackagePrivate();
				
				// Start at the end of the class scan to find the highest
				// replacing class member
				// Note that for any methods which are private the lookup
				// only starts at the base class because those are not visible
				// to any other method
				MethodNameAndType mnat = mm.nameAndType();
				for (int pi = (ispriv ? ci : cn - 1); pi >= ci; pi--)
				{
					// Get the class information for this one
					LoadedClassInfo pbi = classes.get(pi);
					MinimizedClassFile pcf = pbi._class;
					ClassName pcfname = pcf.thisName();
					
					// If our method is package private and the other class
					// is not in the same package then we cannot link to that
					// method because it does not inherit.
					if (ispkpriv && !mcfname.isInSamePackage(pcfname))
						continue;
					
					// If the class has no such method then stop
					MinimizedMethod pm = pcf.method(false, mnat);
					if (pm == null)
						continue;
					
					// Debug
					if (_ENABLE_DEBUG)
						todo.DEBUG.note("Link: (%s) %s -> %s : %s",
							__cl, mcfname, pcfname, mnat);
						
					// Use this method
					entries.set(vat, pbi._classoffset + pcf.header.imoff +
						pm.codeoffset);
					pools.set(vat, this.__initPool(__init, pcfname));
					break;
				}
			}
		}
		
		// Allocate array
		rv = __init.allocate(Constants.ARRAY_BASE_SIZE + (4 * numv));
		selfbi._vtable = rv;
		
		// Write array details
		__init.memWriteInt(Modifier.RAM_OFFSET,
			rv + Constants.OBJECT_CLASS_OFFSET,
			this.__classId(__init, new ClassName("[I")));
		__init.memWriteInt(
			rv + Constants.OBJECT_COUNT_OFFSET,
			999999);
		__init.memWriteInt(
			rv + Constants.ARRAY_LENGTH_OFFSET,
			numv);
		
		// Write in entries
		int wp = rv + Constants.ARRAY_BASE_SIZE;
		for (int i = 0; i < numv; i++, wp += 4)
			__init.memWriteInt(Modifier.JAR_OFFSET,
				wp, entries.get(i));
		
		// Also write in the pool values
		int pv = __init.allocate(Constants.ARRAY_BASE_SIZE + (4 * numv));
		selfbi._vtablepool = pv;
		
		// Write array details
		__init.memWriteInt(Modifier.RAM_OFFSET,
			pv + Constants.OBJECT_CLASS_OFFSET,
			this.__classId(__init, new ClassName("[I")));
		__init.memWriteInt(
			pv + Constants.OBJECT_COUNT_OFFSET,
			999999);
		__init.memWriteInt(
			pv + Constants.ARRAY_LENGTH_OFFSET,
			numv);
		
		// Write in pools
		wp = pv + Constants.ARRAY_BASE_SIZE;
		for (int i = 0; i < numv; i++, wp += 4)
			__init.memWriteInt(Modifier.RAM_OFFSET,
				wp, pools.get(i));
		
		// Return it
		return new int[]{rv, pv};
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
		
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		ClassName kn = new ClassName(
			"cc/squirreljme/jvm/Bootstrap");
		
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
			
		// {@squirreljme.error BC09 No such class exists. (The class)}
		LoadedClassInfo bi = this._boots.get(__cl);
		if (bi == null)
			throw new InvalidClassFormatException("BC09 " + __cl);
		
		// Only calculate it once
		int rv = bi._pooloffset;
		if (rv != 0)
			return rv;
		
		// Get constant pool
		MinimizedClassFile mcl = bi._class;
		DualClassRuntimePool pool = mcl.pool;
		
		throw new todo.TODO();
		/*
		
		// The JAR offset for the actual pool area
		int jarpooloff = bi._classoffset + mcl.header.pooloff;
		
		// Allocate and store space needed for the active pool contents
		int n = pool.size();
		bi._pooloffset = (rv = __init.allocate(n * 4));
		
		// Debug
		if (_ENABLE_DEBUG)
			todo.DEBUG.note("Building %s at %d (virt %d)",
				__cl, rv, rv + 1048576);
		
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
				case LONG:
				case DOUBLE:
					break;
					
				case METHOD_INDEX:
					{
						MethodIndex mi = (MethodIndex)pv;
						__init.memWriteInt(
							ep, this.__classMethodIndex(mi.inclass,
								mi.name, mi.type));
					}
					break;
					
					// Write the pointer to the UTF data
				case STRING:
					{
						__init.memWriteInt(Modifier.JAR_OFFSET,
							ep, jarpooloff + pool.offset(i) + 4);
					}
					break;
					
					// Integer constant
				case INTEGER:
					__init.memWriteInt(ep, ((Number)pv).intValue());
					break;
				
					// Float constant
				case FLOAT:
					__init.memWriteInt(ep,
						Float.floatToRawIntBits(((Number)pv).floatValue()));
					break;
					
					// Write pointer to the string UTF data
				case USED_STRING:
					{
						// Get the string index
						int sdx = pp[0];
						
						// Write offset of that string
						__init.memWriteInt(Modifier.JAR_OFFSET,
							ep, jarpooloff + pool.offset(sdx) + 4);
					}
					break;
					
					// Field being accessed
				case ACCESSED_FIELD:
					{
						// Static field offsets are always known
						AccessedField af = (AccessedField)pv;
						LoadedClassInfo afci = bootstrap.findClass(
							af.field.className());
						
						if (af.type().isStatic())
							__init.memWriteInt(ep,
								afci.fieldStaticOffset(
									af.field.memberName(),
									af.field.memberType()));
						
						// Instance fields are not yet known
						else
							__init.memWriteInt(ep,
								afci.fieldInstanceOffset(
									af.field.memberName(),
									af.field.memberType()));
					}
					break;
				
					// Class ID
				case CLASS_NAME:
					__init.memWriteInt(Modifier.RAM_OFFSET,
						ep, this.__classId(__init, (ClassName)pv));
					break;
					
					// List of class IDs
				case CLASS_NAMES:
					__init.memWriteInt(Modifier.RAM_OFFSET,
						ep, this.__classIds(__init, (ClassNames)pv));
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
		*/
	}
	
	/**
	 * Processes the input JAR.
	 *
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	private final void __process(OutputStream __sout)
		throws IOException, NullPointerException
	{
		if (__sout == null)
			throw new NullPointerException("NARG");
		
		// The current state of the bootstrap
		BootstrapState bootstrap = this.bootstrap;
		Initializer initializer = (bootstrap == null ? null :
			bootstrap.initializer);
		
		// This is processed for all entries
		VMClassLibrary input = this.input;
		
		// Need list of resources to determine
		String[] rcnames = input.listResources();
		int numrc = rcnames.length;
		
		// Sort all the resources so that it is faster to find the entries
		Arrays.sort(rcnames);
		
		// Manifest offset and length
		int manifestoff = 0,
			manifestlen = 0;
		
		// Table of the entire JAR for writing
		TableSectionOutputStream out = new TableSectionOutputStream();
		
		// Table of contents
		ByteArrayOutputStream tbaos = new ByteArrayOutputStream(2048);
		DataOutputStream tdos = new DataOutputStream(tbaos);
		
		// JAR content data
		ByteArrayOutputStream jbaos = new ByteArrayOutputStream(1048576);
		DataOutputStream jdos = new DataOutputStream(jbaos);
		
		// The global dual-constant pool if one is available
		DualClassRuntimePoolBuilder dualpool = this.dualpool;
		
		// Relative offset from header and table of contents
		int reloff = MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC +
			(numrc * MinimizedJarHeader.TOC_ENTRY_SIZE);
		
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
					bytes = Minimizer.minimize(dualpool, ClassFile.decode(in));
				
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
			
			// Write the resource hash code, so that entry searches do not need
			// string creation in searching
			tdos.writeInt(rc.hashCode());
			
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
			
			// Is this the manifest?
			if (rc.equals("META-INF/MANIFEST.MF"))
			{
				manifestoff = clpos;
				manifestlen = bytes.length;
			}
			
			// If boot processing is going to be done, we need to
			// know about this class file if it is one
			if (isclass && bootstrap != null)
				bootstrap.loadClassFile(bytes, clpos);
			
			// Then write the actual data stream
			jdos.write(bytes);
		}
		
		// Write header
		__dos.writeInt(MinimizedJarHeader.MAGIC_NUMBER);
		
		// Fields to store the header
		int[] hfs = new int[MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC / 4];
		int hat = 0;
		
		// Number of resources
		__dos.writeInt((hfs[hat++] = numrc));
		
		// Offset to table of contents
		__dos.writeInt((hfs[hat++] =
			MinimizedJarHeader.HEADER_SIZE_WITH_MAGIC));
		
		// Manifest offset and its length, if any
		__dos.writeInt((hfs[hat++] = manifestoff));
		__dos.writeInt((hfs[hat++] = manifestlen));
		
		// Building pre-boot state
		if (bootstrap != null)
		{
			// Round data stream to 4 bytes
			while ((jdos.size() & 3) != 0)
				jdos.write(0);
			
			// Write address to the boot table
			int baseaddr,
				injaraddr;
			__dos.writeInt((hfs[hat++] = 
				(injaraddr = (reloff + (baseaddr = jdos.size())))));
			
			// Debug
			if (_ENABLE_DEBUG)
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
			__dos.writeInt((hfs[hat++] = bootmem.length));
			
			// Bootstrap pool, static field pointer offset, and the offset
			// to the bootstrap's code
			__dos.writeInt((hfs[hat++] = poolptr[0]));
			__dos.writeInt((hfs[hat++] = ksfa[0]));
			__dos.writeInt(this.__classMethodCodeAddress(
				"cc/squirreljme/jvm/Bootstrap",
				"__start",
				null));
			__dos.writeInt((hfs[hat++] = clab));
			__dos.writeInt((hfs[hat++] = claab));
		}
		
		// No boot data
		else
		{
			// Boot memory offset, size
			__dos.writeInt(0);
			__dos.writeInt(0);
			
			// Pool, sfa, code, classidba, classidbaa
			__dos.writeInt(0);
			__dos.writeInt(0);
			__dos.writeInt(0);
			__dos.writeInt(0);
			__dos.writeInt(0);
		}
		
		// No global dual pool used
		if (dualpool == null)
		{
			// Static pool offset and size
			__dos.writeInt(0);
			__dos.writeInt(0);
			
			// Runtime pool offset and size
			__dos.writeInt(0);
			__dos.writeInt(0);
		}
		
		// We are using our own dual pool, so write it out as if it were
		// in the pack file. It is only local to this JAR.
		else if (this.owndualpool)
		{
			// Round for the pools
			while (((reloff + jdos.size()) & 3) != 0)
				jdos.write(0);
			
			// Encode the pool
			int basep = reloff + jdos.size();
			DualPoolEncodeResult der = DualPoolEncoder.encode(dualpool, jdos);
			
			// Write where the pools were written
			__dos.writeInt(basep + der.staticpooloff);
			__dos.writeInt(der.staticpoolsize);
			__dos.writeInt(basep + der.runtimepooloff);
			__dos.writeInt(der.runtimepoolsize);
		}
		
		// We are using the global pack pool, so set special indicators
		// that we are doing as such! The minimized class will use special
		// a special aliased pool for the pack file.
		else
		{
			// Static pool offset and size
			__dos.writeInt(-1);
			__dos.writeInt(-1);
			
			// Runtime pool offset and size
			__dos.writeInt(-1);
			__dos.writeInt(-1);
		}
		
		// Build header
		this._jheader = new MinimizedJarHeader(hfs);
		
		// Write table of contents and JAR data
		tbaos.writeTo(__dos);
		jbaos.writeTo(__dos);
		
		// Write final class information
		out.writeTo(__sout);
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
			JarMinimizer.minimize(null, __boot, __in, baos, null);
			
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
		JarMinimizer.minimize(null, __boot, __in, __out, null);
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @param __out The stream where JAR data will be placed.
	 * @param __mjh The output JAR header.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/17
	 */
	public static final void minimize(boolean __boot, VMClassLibrary __in,
		OutputStream __out, MinimizedJarHeader[] __mjh)
		throws IOException, NullPointerException
	{
		JarMinimizer.minimize(null, __boot, __in, __out, __mjh);
	}
	
	/**
	 * Minimizes the specified Jar file.
	 *
	 * @param __dp The dual-pool.
	 * @param __boot Should pre-created boot memory be created to quickly
	 * initialize the virtual machine?
	 * @param __in The input JAR file.
	 * @param __out The stream where JAR data will be placed.
	 * @param __mjh The output JAR header.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/29
	 */
	public static final void minimize(DualClassRuntimePoolBuilder __dp,
		boolean __boot, VMClassLibrary __in, OutputStream __out,
		MinimizedJarHeader[] __mjh)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Use helper class
		JarMinimizer jm = new JarMinimizer(__dp, __boot, __in);
		jm.__process(__out);
		
		// Set header that was generated
		if (__mjh != null && __mjh.length > 0)
			__mjh[0] = jm._jheader;
	}
}
