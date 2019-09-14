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

