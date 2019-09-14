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

