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
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.MinimizedPoolEntryType;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.BasicPool;
import dev.shadowtail.classfile.pool.BasicPoolEntry;
import dev.shadowtail.classfile.pool.ClassInfoPointer;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.MethodIndex;
import dev.shadowtail.classfile.pool.UsedString;
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
	private int _pooloffset =
		-1;
	
	/** Static memory offset. */
	private int _smemoff =
		-1;
	
	/** The class data V2 offset. */
	private int _classdata =
		-1;
	
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
	
	/** The depth of this class. */
	private int _classdepth =
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
		
		// Find the bootstrap
		BootstrapState bootstrap = this.__bootstrap();
		
		// This class is the size of the super class and our size
		ClassName supercl = this._class.superName();
		this._allocsize = (rv = (supercl == null ? 0 :
			bootstrap.findClass(supercl).allocationSize()) +
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
	 * Returns the depth of this class.
	 *
	 * @return The depth of this class.
	 * @since 2019/12/01
	 */
	public final int classDepth()
	{
		// Was already cached?
		int rv = this._classdepth;
		if (rv >= 0)
			return rv;
		
		// Find the bootstrap
		BootstrapState bootstrap = this.__bootstrap();
		
		// The class depth is just one added from the super-class
		ClassName supercl = this._class.superName();
		this._classdepth = (rv = (supercl == null ? 0 :
			bootstrap.findClass(supercl).classDepth() + 1));
		
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
		
		// Need the static field offset
		int smemoff = this.staticFieldOffset();
		
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
		if (rv >= 0)
			return rv;
		
		// Get bootstrap and initializer
		BootstrapState bootstrap = this.__bootstrap();
		Initializer initializer = bootstrap.initializer;
		
		// Get the class info for class info
		LoadedClassInfo classinfoci =
			bootstrap.findClass("cc/squirreljme/jvm/ClassInfo");
		
		// Allocate pointer to the class data, then get the base pointer
		this._classdata = (rv = initializer.allocate(
			classinfoci.allocationSize()));
		
		// Load all fields into a queue for useful processing
		Deque<ClassNameAndMinimizedField> fieldq = new LinkedList<>();
		for (LoadedClassInfo atcl = classinfoci; atcl != null;
			atcl = atcl.superClass())
		{
			// Push all fields to queue
			for (MinimizedField mf : atcl._class.fields(false))
				fieldq.push(new ClassNameAndMinimizedField(
					atcl.thisName(), mf));
		}
		
		// This class information
		MinimizedClassFile mcf = this._class;
		ClassName thisname = mcf.thisName();
		
		// Fill in field data
		while (!fieldq.isEmpty())
		{
			// Get entry
			ClassNameAndMinimizedField cnmf = fieldq.pop();
			ClassName cn = cnmf.classname;
			MinimizedField mf = cnmf.field;
			
			// The write pointer of this data
			int wp = rv + bootstrap.findClass(cn).baseOffset() + mf.offset;
			
			// Depends on the key (specified where and its type)
			String key = mf.name + ":" + mf.type;
			switch (key)
			{
					// Base offset for the class
				case "base:I":
					initializer.memWriteInt(wp, this.baseOffset());
					break;
					
					// Cell size
				case "cellsize:I":
					{
						// Determine the cell size
						int cellsize;
						switch (thisname.toString())
						{
							case "byte":
							case "boolean":
							case "[Z":
							case "[B":
								cellsize = 1;
								break;
							
							case "short":
							case "char":
							case "[S":
							case "[C":
								cellsize = 2;
								break;
								
							case "long":
							case "double":
							case "[J":
							case "[D":
								cellsize = 8;
								break;
								
							default:
								cellsize = 4;
								break;
						}
						
						// Write
						initializer.memWriteInt(
							wp, cellsize);
					}
					break;
				
					// Class info reference
				case "_class:I":
					initializer.memWriteInt(Modifier.RAM_OFFSET,
						wp, bootstrap.findClass(
							"cc/squirreljme/jvm/ClassInfo").infoPointer());
					break;
					
					// The depth of this class
				case "classdepth:I":
					initializer.memWriteInt(wp, this.classDepth());
					break;
					
					// Class<?> pointer, starts always at zero since it
					// is generated at run-time
				case "classobjptr:Ljava/lang/Class;":
					initializer.memWriteInt(
						wp, 0);
					break;
					
					// Component class
				case "componentclass:Lcc/squirreljme/jvm/ClassInfo;":
					// Write class ID of component type
					if (thisname.isArray())
						initializer.memWriteInt(Modifier.RAM_OFFSET,
							wp, bootstrap.findClass(
								thisname.componentType()).infoPointer());
					
					// Write null pointer
					else
						initializer.memWriteInt(wp, 0);
					break;
					
					// Default new constructor
				case "defaultnew:I":
					try
					{
						initializer.memWriteInt(Modifier.JAR_OFFSET,
							wp, this.methodCodeAddress("<init>", "()V"));
					}
					catch (InvalidClassFormatException e)
					{
					}
					break;
					
					// Dimensions
				case "dimensions:I":
					initializer.memWriteInt(
						wp, thisname.dimensions());
					break;
					
					// Class info flags
				case "flags:I":
					{
						int flags = 0;
						
						// Is this array?
						if (thisname.isArray())
						{
							// Flag it
							flags |= Constants.CIF_IS_ARRAY;
							
							// Is its component an object as well?
							if (!thisname.componentType().isPrimitive())
								flags |= Constants.CIF_IS_ARRAY_OF_OBJECTS;
						}
						
						// Is this primitive?
						if (thisname.isPrimitive())
							flags |= Constants.CIF_IS_PRIMITIVE;
						
						// Write flags
						initializer.memWriteInt(wp, flags);
					}
					break;
						
						// Interface class information
					case "interfaceclasses:[Lcc/squirreljme/jvm/ClassInfo;":
						{
							// Get interfaces pointer list
							int xp = bootstrap.classNamesInfoPointer(
								mcf.interfaceNames());
								
							// Write pointer here
							initializer.memWriteInt(Modifier.RAM_OFFSET,
								wp, xp);
							
							// The interface names in the bootstrap is the
							// pointer to the ClassInfo, but as an int array
							// type instead of an object array type. But we
							// can just override it.
							initializer.memWriteInt(Modifier.RAM_OFFSET,
								xp + Constants.OBJECT_CLASS_OFFSET,
								bootstrap.findClass(
								"[Lcc/squirreljme/jvm/ClassInfo;").
								infoPointer());
						}
						break;
					
					// The JAR index, always zero for the bootstrap
				case "jardx:I":
					initializer.memWriteInt(
						wp, 0);
					break;
					
					// Magic number
				case "magic:I":
					initializer.memWriteInt(
						wp, ClassInfo.MAGIC_NUMBER);
					break;
					
					// Pointer to the class data in ROM
				case "miniptr:I":
					initializer.memWriteInt(Modifier.JAR_OFFSET,
						wp, this.romOffset());
					break;
					
					// Monitor count
				case "_moncount:I":
					initializer.memWriteInt(
						wp, 0);
					break;
					
					// Thread owning the monitor (which there is none)
				case "_monitor:I":
					initializer.memWriteInt(
						wp, 0);
					break;
					
					// Pointer to the class name
				case "namep:I":
					initializer.memWriteInt(Modifier.JAR_OFFSET,
						wp, this.romOffset() + this.poolEntryOffset(
						this._class.thisName().toString()) + 4);
					break;
					
					// The number of available methods
				case "nummethods:I":
					initializer.memWriteInt(wp, this.methodSize());
					break;
					
					// The number of objects in this class
				case "numobjects:I":
					initializer.memWriteInt(
						wp, this._class.header.ifobjs);
					break;
					
				case "pool:I":
					initializer.memWriteInt(Modifier.RAM_OFFSET,
						wp, this.poolPointer());
					break;
					
					// Reference count for this class data, should never
					// be freed
				case "_refcount:I":
					initializer.memWriteInt(
						wp, 999999);
					break;
					
					// Pointer to our own class info
				case "selfptr:I":
					initializer.memWriteInt(Modifier.RAM_OFFSET,
						wp, rv);
					break;
					
					// Static field offset
				case "sfoffset:I":
					initializer.memWriteInt(
						wp, this.staticFieldOffset());
					break;
				
					// Allocation size of this class
				case "size:I":
					initializer.memWriteInt(wp, this.allocationSize());
					break;
					
					// Super class info
				case "superclass:Lcc/squirreljme/jvm/ClassInfo;":
					ClassName sn = mcf.superName();
					if (sn == null)
						initializer.memWriteInt(wp, 0);
					else
						initializer.memWriteInt(Modifier.RAM_OFFSET,
							wp, bootstrap.findClass(sn).infoPointer());
					break;
						
					// VTable for virtual calls
				case "vtablevirtual:[I":
					initializer.memWriteInt(Modifier.RAM_OFFSET,
						wp, this.vTables()[0]);
					break;
					
					// VTable for pool setting
				case "vtablepool:[I":
					initializer.memWriteInt(Modifier.RAM_OFFSET,
						wp, this.vTables()[1]);
					break;
					
					// Not handled yet!
				default:
					throw new todo.OOPS(key);
			}
		}
		
		// Return pointer to the data now
		return rv;
	}
	
	/**
	 * Returns the method base for the class.
	 *
	 * @return The method base.
	 * @since 2019/05/26
	 */
	public final int methodBase()
	{
		// Need the bootstrap
		BootstrapState bootstrap = this.__bootstrap();
		
		// If there is no super class then we just start at zero
		ClassName supername = this._class.superName();
		if (supername == null)
			return 0;
		
		// Otherwise it is the size of the super-class where we start from
		return bootstrap.findClass(supername).methodSize();
	}
	
	/**
	 * Returns the address of the given method.
	 *
	 * @param __mn The method name.
	 * @param __mt The method type, if {@code null} then the type is
	 * disregarded.
	 * @return The address of the given method.
	 * @since 2019/09/20
	 */
	public final int methodCodeAddress(String __mn, String __mt)
		throws NullPointerException
	{
		if (__mn == null)
			throw new NullPointerException("NARG");
		
		return this.methodCodeAddress(new MethodName(__mn),
			(__mt == null ? null : new MethodDescriptor(__mt)));
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
		if (__mn == null)
			throw new NullPointerException("NARG");
		
		// Get class information
		MinimizedClassFile mcf = this._class;
		
		// Lookup static first
		MinimizedMethod mm = mcf.method(true, __mn, __mt);
		if (mm != null)
			return this._classoffset + mcf.header.smoff + mm.codeoffset;
		
		// Otherwise fallback to instance methods
		// {@squirreljme.error BC07 Could not locate the given method.
		// (The class; The name; The type)}
		mm = mcf.method(false, __mn, __mt);
		if (mm == null)
			throw new InvalidClassFormatException(
				String.format("BC07 %s %s %s", mcf.thisName(), __mn, __mt));
		return this._classoffset + mcf.header.imoff + mm.codeoffset;
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
		if (__mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		// Try to find the method
		MinimizedClassFile mcf = this._class;
		MinimizedMethod mm = mcf.method(false, __mn, __mt);
		if (mm == null)
		{
			// {@squirreljme.error BC08 Could not find the specified method.
			// (The method name; The method type)}
			ClassName scn = mcf.superName();
			if (scn == null)
				throw new InvalidClassFormatException(
					"BC08 " + __mn + " " + __mt);
			
			// See if the super class has it
			return this.__bootstrap().findClass(scn).methodIndex(__mn, __mt);
		}
		
		// Is the index offset from the method base of this class
		return this.methodBase() + mm.index;
	}
	
	/**
	 * Returns the number of methods to use in the method table.
	 *
	 * @return The method size.
	 * @since 2019/05/26
	 */
	public final int methodSize()
	{
		// Need the bootstrap
		BootstrapState bootstrap = this.__bootstrap();
		
		// We are working on this class
		MinimizedClassFile cf = this._class;
		
		// The base method count
		int self = cf.header.imcount;
		
		// If there is no super class it is just the count
		ClassName supername = cf.superName();
		if (supername == null)
			return self;
		
		// Otherwise include the super class count as well
		return bootstrap.findClass(supername).methodSize() + self;
	}
	
	/**
	 * Returns the pointer to the offset of the given pool value.
	 *
	 * @param __v The value to get.
	 * @return The offset of the given value.
	 * @since 2019/09/14
	 */
	public final int poolEntryOffset(Object __v)
	{
		// Need pool for this!
		DualClassRuntimePool pool = this._class.pool;
		
		// Flagged if this is in the runtime pool?
		boolean inruntime;
		
		// Try to find the value from some pool
		BasicPoolEntry entry = pool.getByValue(false, __v);
		if ((inruntime = (entry == null)))
			entry = pool.getByValue(true, __v);
		
		// {@squirreljme.error BC0f Value not found in any pool. (The value)}
		if (entry == null)
			throw new InvalidClassFormatException("BC0f " + __v);
		
		// The offset of this entry is based on where it was found!
		return (inruntime ? this._class.header.runtimepooloff :
			this._class.header.staticpooloff) + entry.offset;
	}
	
	/**
	 * Returns a pointer to the class's constant pool.
	 *
	 * @return The pointer to the class constant pool.
	 * @since 2019/09/14
	 */
	public final int poolPointer()
	{
		// Need the bootstrap!
		BootstrapState bootstrap = this.__bootstrap();
		
		// Name of this class
		ClassName thisname = this.thisName();
		
		// If this is a primitive type or an array then use the pool pointer
		// for Object since they are virtually treated as Object!
		if (thisname.isPrimitive() || thisname.isArray())
			return bootstrap.findClass("java/lang/Object").poolPointer();
		
		// Has this already been cached?
		int rv = this._pooloffset;
		if (rv >= 0)
			return rv;
		
		// Can use the initializer now
		Initializer initializer = bootstrap.initializer;
		
		// Get the constant pool for this class
		MinimizedClassFile miniclass = this._class;
		DualClassRuntimePool pool = miniclass.pool;
		
		// Extract both parts of the pool
		BasicPool clpool = pool.classPool(),
			rtpool = pool.runtimePool();
		
		// Absolute offsets for each pool (in the ROM)
		int classoffset = this._classoffset;
		int clpadd = classoffset + miniclass.header.staticpooloff,
			rtpadd = classoffset + miniclass.header.runtimepooloff;
		
		// We only need space to fit the run-time pool
		int n = rtpool.size();
		this._pooloffset = (rv = initializer.allocate(n * 4));
		
		// Process entries, zero is always null!
		for (int i = 1; i < n; i++)
		{
			// Get the entry here
			BasicPoolEntry entry = rtpool.byIndex(i);
			
			// The value to write in the slot
			Modifier mx;
			int vx;
			
			// Depends on the type of entry we are using
			MinimizedPoolEntryType type = entry.type();
			switch (type)
			{
					// Field which has been accessed
				case ACCESSED_FIELD:
					AccessedField af = entry.<AccessedField>value(
						AccessedField.class);
					
					// Static fields are based on the pointer
					if (af.type().isStatic())
					{
						mx = null;
						vx = bootstrap.findClass(af.field.className()).
							fieldStaticOffset(af.field.memberName(),
								af.field.memberType());
					}
					
					// Instance fields are offset from a class
					else
					{
						mx = null;
						vx = bootstrap.findClass(af.field.className()).
							fieldInstanceOffset(af.field.memberName(),
								af.field.memberType());
					}
					break;
				
					// Pointer to class information
				case CLASS_INFO_POINTER:
					mx = Modifier.RAM_OFFSET;
					vx = bootstrap.findClass(entry.<ClassInfoPointer>value(
						ClassInfoPointer.class).name).infoPointer();
					break;
					
					// Pointer to class constant pool
				case CLASS_POOL:
					ClassPool pl = entry.<ClassPool>value(ClassPool.class);
					
					mx = Modifier.RAM_OFFSET;
					vx = bootstrap.findClass(pl.name).poolPointer();
					break;
					
					// A method to be invoked, these are always direct pointer
					// references to methods
				case INVOKED_METHOD:
					InvokedMethod im = entry.<InvokedMethod>value(
						InvokedMethod.class);
					
					mx = Modifier.JAR_OFFSET;
					vx = bootstrap.findClass(im.handle.outerClass()).
						methodCodeAddress(im.handle.name(),
							im.handle.descriptor());
					break;
					
					// Index of method
				case METHOD_INDEX:
					MethodIndex mi = entry.<MethodIndex>value(
						MethodIndex.class);
					
					mx = null;
					vx = bootstrap.findClass(mi.inclass).methodIndex(
						mi.name, mi.type);
					break;
					
					// A pointer to a string in memory
				case NOTED_STRING:
					mx = Modifier.JAR_OFFSET;
					vx = clpadd + clpool.byIndex(entry.part(0)).offset + 4;
					break;
					
				default:
					throw new todo.OOPS(type.name());
			}
			
			// Write value to the in-memory slot
			initializer.memWriteInt(mx, rv + (4 * i), vx);
			
			// Debug
			if (JarMinimizer._ENABLE_DEBUG)
				todo.DEBUG.note("Pool %s -> %08x = %08x", entry.value,
					rv + (4 * i), vx);
		}
		
		// Return the pointer where the pool was allocated
		return rv;
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
	 * Returns the offset of the static field area.
	 *
	 * @return The pointer of this class static field area.
	 * @since 2019/12/14
	 */
	public final int staticFieldOffset()
	{
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
		
		return smemoff;
	}
	
	/**
	 * Returns the super class of this class.
	 *
	 * @return The class super-class.
	 * @since 2019/09/21
	 */
	public final LoadedClassInfo superClass()
	{
		ClassName sn = this._class.superName();
		return (sn == null ? null : this.__bootstrap().findClass(sn));
	}
	
	/**
	 * Returns the name of the super class.
	 *
	 * @return The name of the super class.
	 * @since 2019/09/21
	 */
	public final ClassName superName()
	{
		return this._class.superName();
	}
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2019/09/14
	 */
	public final ClassName thisName()
	{
		return this._class.thisName();
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
		// Need the bootstrap
		BootstrapState bootstrap = this.__bootstrap();
		
		// The current class information
		ClassName thisname = this._class.thisName();
		
		// Primitive types and array types do not exist so they just use the
		// same vtables as Object
		if (thisname.isPrimitive() || thisname.isArray())
			return bootstrap.findClass("java/lang/Object").vTables();
		
		// Did we already make the VTable for this? This will happen in the
		// event arrays or primitives are virtualized
		int rv = this._vtable;
		if (rv >= 0)
			return new int[]{rv, this._vtablepool};
		
		// Total methods to put together
		int count = this.methodSize();
		
		// Reserve and cache
		int pmptr = bootstrap.reserveIntArray(count),
			ppool = bootstrap.reserveIntArray(count);
		this._vtable = pmptr;
		this._vtablepool = ppool;
		
		// Resultant arrays
		int[] mptr = new int[count];
		int[] pool = new int[count];
		
		// Initially seed the arrays with pure virtual calls to make them
		// illegal to be called virtually (they could only be called statically
		// or otherwise).
		for (int i = 1,
			jpvc = bootstrap.findClass("cc/squirreljme/jvm/JVMFunction").
				methodCodeAddress("jvmPureVirtualCall", "()V"),
			jpvp = bootstrap.findClass("cc/squirreljme/jvm/JVMFunction").
				poolPointer(); i < count; i++)
		{
			mptr[i] = jpvc;
			pool[i] = jpvp;
		}
		
		// Put every class and method into a queue for processing
		Deque<ClassNameAndMinimizedMethod> process = new LinkedList<>();
		for (LoadedClassInfo at = this; at != null; at = at.superClass())
		{
			// Current class name is needed for object push
			ClassName tn = at.thisName();
			
			// Add all methods in there
			for (MinimizedMethod mm : at._class.methods(false))
				process.push(new ClassNameAndMinimizedMethod(tn, mm));
		}
		
		// Process every method
		while (!process.isEmpty())
		{
			// Take off class and extract pieces
			ClassNameAndMinimizedMethod atcnmm = process.pop();
			ClassName atcn = atcnmm.classname;
			MinimizedMethod atmm = atcnmm.method;
			
			// Is this a private method?
			boolean atmmisprivate = atmm.flags().isPrivate();
			
			// Find the loaded class this refers to
			LoadedClassInfo atci = bootstrap.findClass(atcn);
			
			// Find the index of this method in the method table;
			int mdx = atci.methodIndex(atmm.name, atmm.type);
			
			// For the appropriate method from the top
			for (LoadedClassInfo sc = this; sc != null; sc = sc.superClass())
			{
				// Location method here, if missing skip because it will be
				// in a super class
				MinimizedMethod scmm = sc._class.method(false,
					atmm.name, atmm.type);
				if (scmm == null)
					continue;
				
				// If we hit a private method in another class, stop
				if (scmm.flags().isPrivate() && atci != sc)
					break;
				
				// Link to this method and the pool it is in as well
				mptr[mdx] = sc.methodCodeAddress(atmm.name, atmm.type);
				pool[mdx] = sc.poolPointer();
				
				// Stop
				break;
			}
		}
		
		// Finalize and cache
		bootstrap.finalizeIntArray(pmptr, Modifier.JAR_OFFSET, mptr);
		bootstrap.finalizeIntArray(ppool, Modifier.RAM_OFFSET, pool);
		
		// Return them
		return new int[]{pmptr, ppool};
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

