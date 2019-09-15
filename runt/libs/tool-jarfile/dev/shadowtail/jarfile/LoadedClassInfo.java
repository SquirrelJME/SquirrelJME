// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import java.lang.ref.Reference;
import java.util.Deque;
import java.util.LinkedList;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Boot information for a class.
 *
 * @since 2019/04/30
 */
public final class LoadedClassInfo
{
	/** The bootstrap reference. */
	private final Reference<BootstrapState> _bootstrap;
	
	/** The minimized class. */
	private final MinimizedClassFile _class;
	
	/** The offset to the class. */
	private final int _classoffset;
	
	/** The offset to the constant pool allocation. */
	private int _pooloffset;
	
	/** Static memory offset. */
	private int _smemoff =
		-1;
	
	/** The class data V2 offset. */
	private int _classdata;
	
	/** The size of instances for this class. */
	private int _allocsize;
	
	/** The base pointer position for fields. */
	private int _baseoff =
		-1;
	
	/** The VTable for this class. */
	private int _vtable =
		-1;
	
	/** The constant pool references for this class vtable. */
	private int _vtablepool =
		-1;
	
	/**
	 * Initializes the boot info.
	 *
	 * @param __cl The class.
	 * @param __co The class offset.
	 * @param __bs The reference to the owning bootstrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/30
	 */
	LoadedClassInfo(MinimizedClassFile __cl, int __co,
		Reference<BootstrapState> __bs)
		throws NullPointerException
	{
		if (__cl == null || __bs == null)
			throw new NullPointerException("NARG");
		
		this._class = __cl;
		this._classoffset = __co;
		this._bootstrap = __bs;
	}
	
	/**
	 * The allocated instance size.
	 *
	 * @return The allocated instance size.
	 * @since 2019/09/14
	 */
	public final int allocationSize()
	{
		// Pre-cached already?
		int rv = this._allocsize;
		if (rv > 0)
			return rv;
		
		// Allocation size is the super-class size plus our size
		this._allocsize = (rv = this.baseOffset() +
			this._class.header.ifbytes);
		return rv;
	}
	
	/**
	 * Return the base offset of this class.
	 *
	 * @return The base off of this class.
	 * @since 2019/09/14
	 */
	public final int baseOffset()
	{
		// Was already cached?
		int rv = this._baseoff;
		if (rv >= 0)
			return rv;
		
		// Find the bootstrap
		BootstrapState bootstrap = this.__bootstrap();
		
		// The base offset is the allocation size of the super-class
		ClassName supercl = this._class.superName();
		this._baseoff = (rv = (supercl == null ? 0 :
			bootstrap.findClass(supercl).allocationSize()));
		
		return rv;
	}
	
	/**
	 * Returns the instance offset of the field.
	 *
	 * @param __fn The field name.
	 * @param __fd The field descriptor.
	 * @return The offset of the field.
	 * @throws InvalidClassFormatException If the field was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final int fieldInstanceOffset(FieldName __fn, FieldDescriptor __fd)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__fn == null || __fd == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BC02 Could not locate instance field. (Class;
		// Field Name; Field Type)}
		MinimizedField mf = this._class.field(false, __fn, __fd);
		if (mf == null)
			throw new InvalidClassFormatException(
				String.format("BC02 %s %s %s", this._class.thisName(), __fn,
					__fd));
		
		// Determine offset to field
		return this.baseOffset() + mf.offset;
	}
	
	/**
	 * Returns the static offset of the field.
	 *
	 * @param __fn The field name.
	 * @param __fd The field descriptor.
	 * @return The offset of the field.
	 * @throws InvalidClassFormatException If the field was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/14
	 */
	public final int fieldStaticOffset(FieldName __fn, FieldDescriptor __fd)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__fn == null || __fd == null)
			throw new NullPointerException("NARG");
		
		// Do we need to allocate static field space for this class?
		int smemoff = this._smemoff;
		if (smemoff < 0)
		{
			// Need the bootstrap here
			BootstrapState bootstrap = this.__bootstrap();
			Initializer initializer = bootstrap.initializer;
			
			// Allocate memory
			smemoff = bootstrap.allocateStaticFieldSpace(
				this._class.header.sfsize);
			
			// Store
			this._smemoff = smemoff;
			
			// Initialize static field values
			int sfieldarea = bootstrap.staticFieldAreaAddress();
			for (MinimizedField mf : this._class.fields(true))
			{
				// No value here?
				Object val = mf.value;
				if (val == null)
					continue;
				
				// Write pointer for this field
				int wp = sfieldarea + smemoff + mf.offset;
				
				// Write constant value
				switch (mf.type.toString())
				{
					case "B":
					case "Z":
						initializer.memWriteByte(wp,
							((Number)val).byteValue());
						break;
					
					case "S":
					case "C":
						initializer.memWriteShort(wp,
							((Number)val).shortValue());
						break;
					
					case "I":
						initializer.memWriteInt(wp,
							((Number)val).intValue());
						break;
					
					case "J":
						initializer.memWriteLong(wp,
							((Number)val).longValue());
						break;
					
					case "F":
						initializer.memWriteInt(wp,
							Float.floatToRawIntBits(
								((Number)val).floatValue()));
						break;
					
					case "D":
						initializer.memWriteLong(wp,
							Double.doubleToRawLongBits(
								((Number)val).doubleValue()));
						break;
					
					case "Ljava/lang/String;":
						throw new todo.TODO("Write string");
					
						// Unknown
					default:
						throw new todo.OOPS(mf.type.toString());
				}
			}
		}
		
		// {@squirreljme.error BC04 Could not locate static field. (Class;
		// Field Name; Field Type)}
		MinimizedField mf = this._class.field(true, __fn, __fd);
		if (mf == null)
			throw new InvalidClassFormatException(
				String.format("BC04 %s %s %s", this._class.thisName(),
					__fn, __fd));
		
		// Return offset to it
		return smemoff + mf.offset;
	}
	
	/**
	 * Returns the pointer to the class information.
	 *
	 * @return The pointer to the class information.
	 * @since 2019/09/14
	 */
	public final int infoPointer()
	{
		// If it has already been initialized use it
		int rv = this._classdata;
		if (rv != 0)
			return rv;
		
		// Get bootstrap and initializer
		BootstrapState bootstrap = this.__bootstrap();
		Initializer initializer = bootstrap.initializer;
		
		// Allocate pointer to the class data, then get the base pointer
		this._classdata = (rv = initializer.allocate(this.allocationSize()));
		
		// Load all fields into a queue for useful processing
		Deque<ClassNameAndMinimizedField> fieldq = new LinkedList<>();
		for (ClassName at = new ClassName("cc/squirreljme/jvm/ClassInfo"),
			atsuper = null; at != null; at = atsuper)
		{
			// Get class information
			LoadedClassInfo atlci = bootstrap.findClass(at);
			
			// Get super class for next run
			atsuper = atlci._class.superName();
			
			// Push all fields to queue
			for (MinimizedField mf : atlci._class.fields(false))
				fieldq.push(new ClassNameAndMinimizedField(at, mf));
		}
		
		// Fill in field data
		while (!fieldq.isEmpty())
		{
			throw new todo.TODO();
		}
		
		// Return pointer to the data now
		return rv;
		
		/*
		// Initialize all fields within the class information
		for (ClassName at = cicn, atsuper = null; at != null; at = atsuper)
		{
			// Get info for this
			LoadedClassInfo ai = boots.get(at);
			
			// Get super class
			atsuper = ai._class.superName();
			
			// Base offset for this class
			int base = rv + ai.baseOffset();
			
			// Go through and place field values
			for (MinimizedField mf : ai._class.fields(false))
			{
				// Get pointer value to write int
				int wp = base + mf.offset;
				
				// Depends on the type
				String key = mf.name + ":" + mf.type;
				switch (key)
				{
						// Class<?> pointer, allocated when needed
					case "classobjptr:Ljava/lang/Class;":
						initializer.memWriteInt(
							wp, 0);
						break;
						
						// Magic number
					case "magic:I":
						initializer.memWriteInt(
							wp, ClassInfo.MAGIC_NUMBER);
						break;
						
						// Self pointer
					case "selfptr:I":
						initializer.memWriteInt(Modifier.RAM_OFFSET,
							wp, rv);
						break;
						
						// Class info flags
					case "flags:I":
						{
							int flags = 0;
							
							// Is this array?
							if (__cl.isArray())
							{
								// Flag it
								flags |= Constants.CIF_IS_ARRAY;
								
								// Is its component an object as well?
								if (!__cl.componentType().isPrimitive())
									flags |= Constants.CIF_IS_ARRAY_OF_OBJECTS;
							}
							
							// Is this primitive?
							if (__cl.isPrimitive())
								flags |= Constants.CIF_IS_PRIMITIVE;
							
							// Write flags
							initializer.memWriteInt(wp, flags);
						}
						break;
						
						// Pointer to the class data in ROM
					case "miniptr:I":
						initializer.memWriteInt(Modifier.JAR_OFFSET,
							wp, bi._classoffset);
						break;
						
						// Pointer to the class name
					case "namep:I":
						if (true)
							throw new todo.TODO();
						/*
						__init.memWriteInt(Modifier.JAR_OFFSET,
							wp, bi._classoffset + bi._class.header.pooloff +
								bi._class.pool.offset(bi._class.pool.part(
								bi._class.header.classname, 0)) + 4);
						* /
						break;
						
						// Super class info
					case "superclass:Lcc/squirreljme/jvm/ClassInfo;":
						{
							ClassName sn = bi._class.superName();
							if (sn == null)
								initializer.memWriteInt(wp, 0);
							else
								initializer.memWriteInt(Modifier.RAM_OFFSET,
									wp, this.__classId(initializer, sn));
						}
						break;
						
						// Interface class information
					case "interfaceclasses:[Lcc/squirreljme/jvm/ClassInfo;":
						{
							// Get interfaces
							ClassNames ints = bi._class.interfaceNames();
							int numints = ints.size();
							
							// Allocate and set field array pointer
							int cip = initializer.allocate(
								Constants.ARRAY_BASE_SIZE + (numints * 4));
							initializer.memWriteInt(Modifier.RAM_OFFSET,
								wp, cip);
							
							// Write array details
							initializer.memWriteInt(Modifier.RAM_OFFSET,
								cip + Constants.OBJECT_CLASS_OFFSET,
								this.__classId(initializer, new ClassName(
									"[Lcc/squirreljme/jvm/ClassInfo;")));
							initializer.memWriteInt(
								cip + Constants.OBJECT_COUNT_OFFSET,
								999999);
							initializer.memWriteInt(
								cip + Constants.ARRAY_LENGTH_OFFSET,
								numints);
							
							// Write interface IDs
							for (int j = 0; j < numints; j++)
								initializer.memWriteInt(Modifier.RAM_OFFSET,
									cip + Constants.ARRAY_BASE_SIZE + (j * 4),
									this.__classId(initializer, ints.get(j)));
						}
						break;
						
						// Component class
					case "componentclass:Lcc/squirreljme/jvm/ClassInfo;":
						{
							// Write class ID of component type
							if (__cl.isArray())
								initializer.memWriteInt(Modifier.RAM_OFFSET,
									wp, this.__classId(initializer,
										initializer.componentType()));
							
							// Write null pointer
							else
								__init.memWriteInt(wp, 0);
						}
						break;
						
						// VTable for virtual calls
					case "vtablevirtual:[I":
						initializer.memWriteInt(Modifier.RAM_OFFSET,
							wp, this.__classVTable(initializer, __cl)[0]);
						break;
						
						// VTable for pool setting
					case "vtablepool:[I":
						initializer.memWriteInt(Modifier.RAM_OFFSET,
							wp, this.__classVTable(initializer, __cl)[1]);
						break;
						
						// Base offset for the class
					case "base:I":
						initializer.memWriteInt(wp, bi.baseOffset());
						break;
						
						// The number of objects in this class
					case "numobjects:I":
						initializer.memWriteInt(
							wp, bi._class.header.ifobjs);
						break;
						
						// Allocation size of the class
					case "size:I":
						initializer.memWriteInt(wp, bi.allocationSize());
						break;
						
						// Dimensions
					case "dimensions:I":
						initializer.memWriteInt(
							wp, __cl.dimensions());
						break;
						
						// Cell size
					case "cellsize:I":
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
							initializer.memWriteInt(
								wp, cellsize);
						}
						break;
						
						// Is class info instance
					case "_class:I":
						initializer.memWriteInt(Modifier.RAM_OFFSET,
							wp, this.__classId(initializer, cdcln));
						break;
						
						// Reference count for this class data, should never
						// be freed
					case "_refcount:I":
						initializer.memWriteInt(
							wp, 999999);
						break;
						
						// Thread owning the monitor (which there is none)
					case "_monitor:I":
						initializer.memWriteInt(
							wp, 0);
						break;
					
						// Monitor count
					case "_moncount:I":
						initializer.memWriteInt(
							wp, 0);
						break;
					
					default:
						throw new todo.OOPS(key);
				}
			}
		}
		
		// Return the pointer to the class data
		return rv;
		*/
	}
	
	/**
	 * Returns the method base for the class.
	 *
	 * @return The method base.
	 * @since 2019/05/26
	 */
	public final int methodBase()
	{
		throw new todo.TODO();
		/*
		// Get class method might be in
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		LoadedClassInfo bi = boots.get(__cl);
		
		// If this has no super class, then the base is zero
		ClassName supername = bi._class.superName();
		if (supername == null)
			return 0;
		
		// Otherwise it is the number of available methods in the super class
		return this.__classMethodSize(supername);
		*/
	}
	
	/**
	 * Returns the address of the given method.
	 *
	 * @param __mn The method name.
	 * @param __mt The method type, if {@code null} then the type is
	 * disregarded.
	 * @return The address of the given method.
	 * @since 2019/04/30
	 */
	public final int methodCodeAddress(MethodName __mn, MethodDescriptor __mt)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
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
		*/
	}
	
	/**
	 * Returns the method index for the given method.
	 *
	 * @param __mn The name of the method.
	 * @param __mt The descriptor of the method.
	 * @return The index of the given method.
	 * @since 2019/05/26
	 */
	public final int methodIndex(MethodName __mn, MethodDescriptor __mt)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
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
		*/
	}
	
	/**
	 * Returns the number of methods to use in the method table.
	 *
	 * @return The method size.
	 * @since 2019/05/26
	 */
	public final int methodSize()
	{
		throw new todo.TODO();
		/*
		// Get class method might be in
		Map<ClassName, LoadedClassInfo> boots = this._boots;
		LoadedClassInfo bi = boots.get(__cl);
		
		// If there is no super class it is just the count
		ClassName supername = bi._class.superName();
		if (supername == null)
			return bi._class.header.imcount;
		
		// Otherwise include the super class count as well
		return this.__classMethodSize(supername) + bi._class.header.imcount;
		*/
	}
	
	/**
	 * Returns a pointer to the class's constant pool.
	 *
	 * @return The pointer to the class constant pool.
	 * @since 2019/09/14
	 */
	public final int poolPointer()
	{
		throw new todo.TODO();
		
		/*
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
		/ *
		
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
	 * Returns the ROM offset of this class.
	 *
	 * @return The ROM offset of the class.
	 * @since 2019/09/14
	 */
	public final int romOffset()
	{
		return this._classoffset;
	}
	
	/**
	 * Returns the pointers to the class method execution pointers along with
	 * the required pool pointers.
	 *
	 * @return The method table pointer and the pool pointer table.
	 * @since 2019/09/14
	 */
	public final int[] vTables()
	{
		throw new todo.TODO();
		/*
		
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
		*/
	}
	
	/**
	 * Returns the bootstrap.
	 *
	 * @return The bootstrap.
	 * @throws IllegalStateException If it was garbage collected.
	 * @since 2019/09/14
	 */
	private final BootstrapState __bootstrap()
		throws IllegalStateException
	{
		Reference<BootstrapState> ref = this._bootstrap;
		
		// {@squirreljme.error BC0d The bootstrap state was garbage collected,
		// therefor this class is no longer valid.}
		BootstrapState rv = ref.get();
		if (rv == null)
			throw new IllegalStateException("BC0d");
		
		return rv;
	}
}

