// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.summercoat.constants.ClassProperty;
import cc.squirreljme.jvm.summercoat.constants.MemHandleKind;
import cc.squirreljme.jvm.summercoat.constants.StaticClassProperty;
import cc.squirreljme.jvm.summercoat.constants.StaticVmAttribute;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedClassHeader;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import dev.shadowtail.classfile.mini.Minimizer;
import dev.shadowtail.classfile.pool.AccessedField;
import dev.shadowtail.classfile.pool.BasicPool;
import dev.shadowtail.classfile.pool.BasicPoolEntry;
import dev.shadowtail.classfile.pool.ClassInfoPointer;
import dev.shadowtail.classfile.pool.ClassPool;
import dev.shadowtail.classfile.pool.DualClassRuntimePool;
import dev.shadowtail.classfile.pool.InvokeType;
import dev.shadowtail.classfile.pool.InvokedMethod;
import dev.shadowtail.classfile.xlate.DataType;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ClassNames;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.io.ChunkFuture;
import net.multiphasicapps.io.ChunkSection;

/**
 * The boot state of the system.
 *
 * @since 2020/12/13
 */
public final class BootState
{
	/** The length of an array. */
	private static final FieldNameAndType _ARRAY_LENGTH =
		new FieldNameAndType("length", "I");
	
	/** The default constructor. */
	private static final MethodNameAndType _DEFAULT_CONSTRUCTOR =
		new MethodNameAndType("<init>", "()V");
	
	/** The object class. */
	private static final ClassName _OBJECT_CLASS =
		new ClassName("java/lang/Object");
	
	/** The class for {@link Class}. */
	private static final ClassName _CLASS_CLASS =
		new ClassName("java/lang/Class");
	
	/** Object class info. */
	private static final FieldNameAndType _OBJECT_CLASS_INFO =
		new FieldNameAndType("_classInfo",
		Minimizer.CLASS_INFO_FIELD_DESC);
	
	/** The name of the string class. */
	private static final ClassName _STRING_CLASS =
		new ClassName("java/lang/String");
	
	/** The class data used. */
	private final Map<ClassName, ChunkSection> _rawChunks =
		new HashMap<>();
	
	/** Classes which have been read. */
	private final Map<ClassName, MinimizedClassFile> _readClasses =
		new HashMap<>();
	
	/** The state of all classes. */
	private final Map<ClassName, ClassState> _classStates =
		new HashMap<>();
	
	/** Actions performed on memory. */
	private final MemActions _memActions =
		new MemActions();
	
	/** Memory handles for the boot state, to be written accordingly. */
	private final MemHandles _memHandles =
		new MemHandles(this._memActions, this);
	
	/** Internal strings. */
	private final Map<String, MemHandle> _internStrings =
		new LinkedHashMap<>();
		
	/** The name of the boot class. */
	private ClassName _bootClass;
	
	/** The pool references to use for booting. */
	private DualClassRuntimePool _pool;
	
	/** Base array size. */
	private int _baseArraySize =
		-1;
	
	/** Base object size. */
	private int _baseObjectSize =
		-1;
	
	/** The pool chunk. */
	private ChunkSection _poolChunk;
	
	/**
	 * Adds the specified class to be loaded and handled later.
	 * 
	 * @param __class The class name of this resource.
	 * @param __in The chunk to read from.
	 * @param __isBootClass Is this the boot class?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/13
	 */
	public void addClass(ClassName __class, ChunkSection __in,
		boolean __isBootClass)
		throws IOException, NullPointerException
	{
		if (__class == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Store the raw chunk reference
		this._rawChunks.put(__class, __in);
		
		// Is this the entry class?
		if (__isBootClass)
			this._bootClass = __class;
	}
	
	/**
	 * Returns all of the interfaces that are implemented by the given class.
	 * 
	 * @param __cl The class to get for.
	 * @return All of the interfaces that the class implements.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public ClassNames allInterfaces(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// All types that are interfaces
		Set<ClassName> result = new SortedTreeSet<>();
		
		// Seed initial queue with the current class
		Deque<ClassName> queue = new LinkedList<>();
		queue.add(__cl);
		
		// Drain the queue and visit all the various classes
		Set<ClassName> visited = new HashSet<>();
		while (!queue.isEmpty())
		{
			// Only visit classes once
			ClassName visit = queue.remove();
			if (visited.contains(visit))
				continue;
			
			// Do not visit again
			visited.add(visit);
			
			// If this is an interface, use that
			ClassState state = this.loadClass(visit);
			if (state.classFile.flags().isInterface())
				result.add(visit);
			
			// Add the interfaces to the queue for processing 
			queue.addAll(state.classFile.interfaceNames());
			
			// Add the super class to go up if there is one
			ClassName parent = state.classFile.superName();
			if (parent != null)
				queue.add(parent);
		}
		
		return new ClassNames(result);
	}
	
	/**
	 * Performs the boot process for the system.
	 * 
	 * @param __pool The pool to use for loading.
	 * @param __lpd The local dual pool section.
	 * @param __outData The output data for the bootstrap.
	 * @param __startPoolHandleId The target handle ID for the pool.
	 * @param __vmAttribHandleId The target handle for the VM attributes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public void boot(DualClassRuntimePool __pool, ChunkSection __lpd,
		ChunkSection __outData, int[] __startPoolHandleId,
		int[] __vmAttribHandleId)
		throws IOException, NullPointerException
	{
		if (__pool == null || __lpd == null || __outData == null ||
			__startPoolHandleId == null)
			throw new NullPointerException("NARG");
		
		// Set the boot pool because we need everything that is inside
		this._pool = __pool;
		this._poolChunk = __lpd;
		
		// Recursively load the boot class and any dependent class
		ClassState boot = this.loadClass(this._bootClass);
		
		// Chain link all the internal strings needed by the bootstrap so that
		// they are consistent. This also needs to handle weak references so
		// these strings can be GCed.
		Debugging.todoNote("Chainlink intern strings.");
		if (false)
			throw Debugging.todo();
		// this._internStrings
		
		// Determine the starting memory handle ID
		__startPoolHandleId[0] = boot._poolMemHandle.id;
		__vmAttribHandleId[0] = this.__buildVmAttributes().id;
		
		// Write the memory handles (and actions) into boot memory
		this._memHandles.chunkOut(__outData);
	}
	
	/**
	 * Loads a character array.
	 * 
	 * @param __v The values to store.
	 * @return The loaded character array.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public final ChunkMemHandle loadArrayChar(char... __v)
		throws IOException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// The class used
		ClassName type = PrimitiveType.CHARACTER
			.toClassName().addDimensions(1);
		
		// Prepare array to store the character data
		int n = __v.length;
		ChunkMemHandle rv = this.prepareArray(type, n);
		
		// Determine the base offset to write to
		int baseOff = this.__baseArraySize();
		
		// Write all the elements to it
		for (int i = 0, off = baseOff; i < n; i++, off += 2)
			rv.write(off, MemoryType.SHORT, (int)__v[i]);
		
		return rv;
	}
	
	/**
	 * Loads the specified class.
	 * 
	 * @param __cl The class to load.
	 * @return The class state for the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/24
	 */
	public final ClassState loadClass(String __cl)
		throws IOException, NullPointerException
	{
		return this.loadClass(new ClassName(__cl));
	}
	
	/**
	 * Loads the specified class.
	 * 
	 * @param __cl The class to load.
	 * @return The class state for the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public final ClassState loadClass(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Has this class already been loaded?
		Map<ClassName, ClassState> classStates = this._classStates;
		ClassState rv = classStates.get(__cl);
		if (rv != null)
			return rv;
		
		// Debug
		Debugging.debugNote("Loading %s...", __cl);
		
		// Read the class data as fast as possible and store into the map so
		// we can recursive and recycle classes.
		MinimizedClassFile classFile = this.readClass(__cl);
		rv = new ClassState(__cl, classFile);
		classStates.put(__cl, rv);
		
		// Header information to extract properties from
		MinimizedClassHeader header = classFile.header;
		
		// Allocate storage for the class information
		MemHandles memHandles = this._memHandles;
		ClassInfoHandle classInfo = memHandles.allocClassInfo(rv);
		rv._classInfoHandle = classInfo;
		
		// Copy all of the static properties since they would not normally
		// be copied
		for (int i = 0; i < StaticClassProperty.NUM_STATIC_PROPERTIES; i++)
			classInfo.set(i, header.get(i));
		
		// Everything is based on the run-time pool, so we need to load
		// everything inside
		BasicPool rtPool = classFile.pool.runtimePool();
		int rtPoolSz = rtPool.size();
		
		// Allocate storage for the constant pool
		PoolHandle pool = memHandles.allocPool(rtPoolSz);
		rv._poolMemHandle = pool;
		
		// The class pool is here
		classInfo.set(ClassProperty.POOLHANDLE_POOL, pool);
		
		// The class static fields have to go somewhere
		ChunkMemHandle staticFieldData = memHandles.allocFields(
			this.__baseArraySize() + header.get(
				StaticClassProperty.SIZE_STATIC_FIELD_DATA));
		classInfo.set(ClassProperty.MEMHANDLE_STATIC_FIELDS,
			staticFieldData);
		
		// Store the pointer to where the Class ROM exists in memory, but this
		// is only valid for non-special classes
		if (!__cl.isArray() && !__cl.isPrimitive())
			classInfo.set(ClassProperty.MEMPTR_ROM_CLASS,
				this._rawChunks.get(classFile.thisName()).futureAddress());
		
		// Need to determine if we are Object or our super class is Object
		// that way there can be shortcuts on resolution
		ClassName superClass = classFile.superName();
		boolean isThisObject = classFile.thisName().isObject();
		
		// This should never happen, hopefully!
		if (!__cl.isPrimitive() && !isThisObject && superClass == null)
			throw Debugging.oops(classFile.thisName(), superClass);
		
		// If this is the object class, then it is well known what the size
		// of the various classes are
		if (isThisObject)
		{
			classInfo.set(ClassProperty.OFFSETBASE_INSTANCE_FIELDS, 0);
			classInfo.set(ClassProperty.SIZE_ALLOCATION,
				header.get(StaticClassProperty.INT_INSTANCE_FIELD_BYTES));
			
			// There is no depth to this class
			classInfo.set(ClassProperty.INT_CLASSDEPTH, 0);
		}
		
		// Primitive types are special cases
		else if (__cl.isPrimitive())
		{
			classInfo.set(ClassProperty.SIZE_ALLOCATION,
				__cl.primitiveType().bytes());
			
			// These do not make sense
			classInfo.set(ClassProperty.OFFSETBASE_INSTANCE_FIELDS, 0);
			classInfo.set(ClassProperty.INT_CLASSDEPTH, 0);
		}
		
		// Determine the kind of memory handle used
		classInfo.set(ClassProperty.INT_MEMHANDLE_KIND,
			this.__handleKind(__cl));
		
		// The cell size, if an array
		if (__cl.isArray())
			classInfo.set(ClassProperty.INT_COMPONENT_CELL_SIZE,
				DataType.of(__cl.componentType().field()).size());
		
		// Is there a super class for this class?
		ClassState superClassState = (superClass == null ? null :
			this.loadClass(superClass));
		if (superClass != null)
			classInfo.set(ClassProperty.CLASSINFO_SUPER,
				superClassState._classInfoHandle);
		
		// Store for later
		rv._superClass = superClassState;
		
		// Determine the allocation size of this class, we need to know this
		// as soon as possible but we can only determine this once object
		// is handled.
		if (!isThisObject && !__cl.isPrimitive())
		{
			// This is one deeper than the super class
			try
			{
				classInfo.set(ClassProperty.INT_CLASSDEPTH,
					this.__classDepth(rv));
			}
			catch (IllegalStateException ignored)
			{
				// Has already been set
			}
			
			// Debug super class
			Debugging.debugNote("SuperClass: %s (from %s)",
				superClassState.thisName, __cl);
			
			// The base of the class is the allocation size of the super
			// class
			int base = this.__allocSize(superClassState);
			classInfo.set(ClassProperty.OFFSETBASE_INSTANCE_FIELDS, base);
			
			// Debug
			Debugging.debugNote("offsetBase %s = %d (check %d)",
				__cl, base, classInfo.getInteger(
					ClassProperty.OFFSETBASE_INSTANCE_FIELDS));
			
			// The allocation size of this class is the combined base and
			// our storage for fields
			int calcSize = base +
				header.get(StaticClassProperty.INT_INSTANCE_FIELD_BYTES);
			try
			{
				classInfo.set(ClassProperty.SIZE_ALLOCATION, calcSize);
			}
			catch (IllegalStateException ignored)
			{
				// Verify that the value is properly determined
				int val = classInfo.getInteger(ClassProperty.SIZE_ALLOCATION);
				if (calcSize != val)
					throw Debugging.oops("Wrong calc size?",
						calcSize, val);
			}
		}
		
		// Fill in any interfaces the class implements
		ClassNames interfaces = classFile.interfaceNames();
		classInfo.set(ClassProperty.CLASSINFOS_INTERFACECLASSES,
			memHandles.allocClassInfos(
				this.loadClasses(interfaces.toArray())));
		
		// The name of the current class
		classInfo.set(ClassProperty.MEMHANDLE_THISNAME_DESC,
			this.loadString(__cl.toString()));
		classInfo.set(ClassProperty.MEMHANDLE_THISNAME_CLASSGETNAME,
			this.loadString(__cl.toRuntimeString()));
		
		// Store in constant values for fields
		int staticFieldBase = this.__baseArraySize();
		for (MinimizedField staticField : classFile.fields(true))
		{
			// Ignore field if there are no constant values
			Object constVal = staticField.value;
			if (constVal == null)
				continue;
			
			// Depends on the type being written
			int offset = staticFieldBase + staticField.offset;
			switch (staticField.datatype)
			{
				case BYTE:
					staticFieldData.write(offset, MemoryType.BYTE,
						((Number)constVal).byteValue());
					break;
					
				case SHORT:
				case CHARACTER:
					staticFieldData.write(offset, MemoryType.SHORT,
						((Number)constVal).shortValue());
					break;
					
				case INTEGER:
					staticFieldData.write(offset, MemoryType.INTEGER,
						((Number)constVal).intValue());
					break;
					
				case LONG:
					staticFieldData.write(offset, MemoryType.LONG,
						((Number)constVal).longValue());
					break;
					
				case FLOAT:
					staticFieldData.write(offset, MemoryType.INTEGER,
						Float.floatToRawIntBits((Float)constVal));
					break;
					
				case DOUBLE:
					staticFieldData.write(offset, MemoryType.LONG,
						Double.doubleToRawLongBits((Double)constVal));
					break;
					
				case OBJECT:
					if (constVal instanceof String)
					{
						staticFieldData.write(offset, MemoryType.INTEGER,
							this.loadString((String)constVal));
						break;
					}
				
				default:
					throw Debugging.oops(staticField);
			}
		}
		
		// Determine the VTable for all non-interface instance methods
		classInfo.set(ClassProperty.MEMHANDLE_VTABLE,
			this.__calcVTable(rv));
		
		// Determine all of the interfaces that this class possibly implements.
		// This will be used by instance checks and eventual interface VTable
		// lookup
		ClassNames allInterfaces = this.allInterfaces(__cl);
		classInfo.set(ClassProperty.CLASSINFOS_ALL_INTERFACECLASSES, 
			memHandles.allocClassInfos(
				this.loadClasses(allInterfaces.toArray())));
		
		// Pre-build all of the interface ITables for every interface that
		// this class implements, a class gets a ITables for all interfaces
		// and inherited interfaces it implements.
		// ex: ArrayList gets ITables for Cloneable, Collection<E>, List<E>,
		// RandomAccess
		if (true)
			Debugging.todoNote("Interface ITables");
		
		// Load the information for the Class<?> instance
		classInfo.set(ClassProperty.MEMHANDLE_LANGCLASS_INSTANCE,
			this.loadLangClass(__cl));
		
		// Set and initialize all of the entries within the pool
		for (int i = 1; i < rtPoolSz; i++)
		{
			Object pv = this.__loadPool(rtPool, rtPool.byIndex(i));
			
			// Depends on the type of value returned
			if (pv instanceof Integer)
				pool.set(i, (int)pv);
			else if (pv instanceof MemHandle)
				pool.set(i, (MemHandle)pv);
			else if (pv instanceof ChunkFuture)
				pool.set(i, (ChunkFuture)pv);
			else if (pv instanceof BootJarPointer)
				pool.set(i, (BootJarPointer)pv);
			
			// Do not know what to do with this
			else
				throw Debugging.oops(pv);
		}
		
		// The component class, if this is an array
		if (__cl.isArray())
			classInfo.set(ClassProperty.CLASSINFO_COMPONENTCLASS,
				this.loadClass(__cl.componentType())._classInfoHandle);
		
		// Determine the function pointer to the default new instance, this
		// is needed for Class.newInstance(). This may be null.
		MinimizedMethod defConst = classFile.method(false,
			BootState._DEFAULT_CONSTRUCTOR);
		if (defConst != null)
			classInfo.set(ClassProperty.FUNCPTR_DEFAULT_NEW,
				this.__calcMethodCodeAddr(rv, false, defConst));
		
		// Loading the class is complete!
		return rv;
	}
	
	/**
	 * Loads multiple classes.
	 * 
	 * @param __names The names of the classes to load.
	 * @return The loaded classes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public final ClassState[] loadClasses(ClassName... __names)
		throws IOException, NullPointerException
	{
		if (__names == null)
			throw new NullPointerException("NARG");
		
		int n = __names.length;
		ClassState[] rv = new ClassState[n];
		
		for (int i = 0; i < n; i++)
			rv[i] = this.loadClass(__names[i]);
		
		return rv;
	}
	
	/**
	 * Loads an instance of {@link Class} for a given object.
	 * 
	 * @param __cl The class to load for.
	 * @return The {@link Class} instance.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public MemHandle loadLangClass(ClassName __cl)
		throws IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
			
		// Load the string class and prepare an object that we will be writing
		// into as required
		ChunkMemHandle object = this.prepareObject(BootState._CLASS_CLASS);
		
		// There just needs to be a handle to the class information, note
		// that class information is a kind of TypeBracket at least on
		// SummerCoat.
		this.objectFieldSet(object,
			new FieldNameAndType("_type",
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;"),
			this.loadClass(__cl)._classInfoHandle);
		
		return object;
	}
	
	/**
	 * Loads a string into memory and returns the mem handle of where it is
	 * allocated.
	 * 
	 * @param __s The string to load.
	 * @return The memory handle of the string.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/21
	 */
	public final MemHandle loadString(String __s)
		throws IOException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Has this string already been loaded? If so then we need to use
		// the pre-existing string here
		Map<String, MemHandle> intern = this._internStrings;
		MemHandle rv = intern.get(__s);
		if (rv != null)
			return rv;
		
		// Load the string class and prepare an object that we will be writing
		// into as required
		ChunkMemHandle object = this.prepareObject(BootState._STRING_CLASS);
		
		// The only part of string that needs to be set is the character data
		this.objectFieldSet(object,
			new FieldNameAndType("_chars", "[C"),
			this.loadArrayChar(__s.toCharArray()));
		
		return object;
	}
	
	/**
	 * Gets the value for the given instance field.
	 * 
	 * @param <V> The type of value requested.
	 * @param __cl The type of vlaue requested.
	 * @param __object The object to get.
	 * @param __nat The name and type of the field.
	 * @throws IllegalArgumentException If the class does not have that field.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/12
	 */
	public  <V> V objectFieldGet(Class<V> __cl, ChunkMemHandle __object,
		FieldNameAndType __nat)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__object == null || __nat == null)
			throw new NullPointerException("NARG");
		
		// Determine the starting point for the object scan
		ClassState start;
		if (__nat.equals(BootState._OBJECT_CLASS_INFO))
			start = this.loadClass(BootState._OBJECT_CLASS);
		else
			start = this.<ClassInfoHandle>objectFieldGet(ClassInfoHandle.class,
				__object, BootState._OBJECT_CLASS_INFO).classState();
		
		// Determine where in the object we will be reading the value
		for (ClassState at = start; at != null; at = at._superClass)
			for (MinimizedField f : at.classFile.fields(false))
				if (__nat.equals(f.nameAndType()))
				{
					// Where is this field located?
					int offset = f.offset + at._classInfoHandle.getInteger(
						ClassProperty.OFFSETBASE_INSTANCE_FIELDS);
					
					Debugging.debugNote("[%s/%s] <- offset %d = %d + %d",
						__nat, at.thisName,
						offset, f.offset, at._classInfoHandle.getInteger(
						ClassProperty.OFFSETBASE_INSTANCE_FIELDS));
					
					// Set value here
					return __object.<V>read(__cl, offset,
						MemoryType.of(__nat.type().dataType()));
				}
		
		// {@squirreljme.error BC0k Class does not contain the given field.
		// (The class; The field)}
		throw new IllegalArgumentException(String.format("BC0k %s %s",
			start.thisName, __nat));
	}
	
	/**`
	 * Sets the given field for the object instance.
	 * 
	 * @param __object The object to set.
	 * @param __nat The name and type of the field.
	 * @param __v The value to store.
	 * @throws IllegalArgumentException If the class does not have that field.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public void objectFieldSet(ChunkMemHandle __object, FieldNameAndType __nat,
		Object __v)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__object == null || __nat == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Determine the starting point for the object scan
		ClassState start;
		if (__nat.equals(BootState._OBJECT_CLASS_INFO))
			start = this.loadClass(BootState._OBJECT_CLASS);
		else
			start = this.<ClassInfoHandle>objectFieldGet(ClassInfoHandle.class,
				__object, BootState._OBJECT_CLASS_INFO).classState();
		
		// Determine where in the object we will be writing the value
		for (ClassState at = start; at != null; at = at._superClass)
			for (MinimizedField f : at.classFile.fields(false))
				if (__nat.equals(f.nameAndType()))
				{
					// Where is this field located?
					int offset = f.offset + at._classInfoHandle.getInteger(
						ClassProperty.OFFSETBASE_INSTANCE_FIELDS);
					
					Debugging.debugNote("[%s/%s] -> offset %d = %d + %d",
						__nat, at.thisName,
						offset, f.offset, at._classInfoHandle.getInteger(
						ClassProperty.OFFSETBASE_INSTANCE_FIELDS));
					
					// Set value here
					__object.write(offset,
						MemoryType.of(__nat.type().dataType()), __v);
					
					// Stop processing, since we set some value
					return;
				}
		
		// {@squirreljme.error BC0j Class does not contain the given field.
		// (The class; The field)}
		throw new IllegalArgumentException(String.format("BC0j %s %s",
			start.thisName, __nat));
	}
	
	/**
	 * Prepares an array instance.
	 * 
	 * @param __cl The class type to use for the array.
	 * @param __len The length of the array.
	 * @return The handle for the array data.
	 * @throws IllegalArgumentException If the class is not an array or the
	 * array length is negative.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public ChunkMemHandle prepareArray(ClassName __cl, int __len)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		if (__len < 0)
			throw new IllegalArgumentException("NEGV " + __len);
		
		// {@squirreljme.error BC0f Class is not an array. (The class)}
		if (!__cl.isArray())
			throw new IllegalArgumentException("BC0f" + __cl);
		
		// We need to know about the class to work with it
		ClassState state = this.loadClass(__cl);
		
		// Determine the memory handle kind used
		int handleKind = this.__handleKind(__cl);
		
		// Determine the base array size.
		int baseArraySize = this.__baseArraySize();
		
		// Allocate memory needed to store the array handle, this includes
		// room for all of the elements accordingly
		ChunkMemHandle rv = this._memHandles.alloc(
			handleKind, baseArraySize +
				(__len * __cl.componentType().field().dataType().size()));
		
		// Debug
		Debugging.debugNote("array[%d] (sz=%d)", __len, rv.byteSize);
		
		// Store the array size as a hint
		rv._arraySize = __len;
		
		// Set array field information
		this.objectFieldSet(rv, BootState._OBJECT_CLASS_INFO,
			state._classInfoHandle);
		this.objectFieldSet(rv, BootState._ARRAY_LENGTH,
			__len);
		
		return rv;
	}
	
	/**
	 * Prepares memory that contains an instance for the given class.
	 * 
	 * @param __cl The class to load.
	 * @return The handle to the class data.
	 * @throws IOException On read errors.
	 * @throws IllegalArgumentException If an object is attempted to be
	 * allocated.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public ChunkMemHandle prepareObject(ClassName __cl)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BC0i Cannot use this method to initialize a
		// primitive or array type. (The class name)}
		if (__cl.isPrimitive() || __cl.isArray())
			throw new IllegalArgumentException("BC0i " + __cl);
		
		// We need to know about the class to work with it
		ClassState state = this.loadClass(__cl);
		
		// Allocate memory needed to store the object handle
		ChunkMemHandle rv = this._memHandles.allocObject(
			state._classInfoHandle.getInteger(ClassProperty.SIZE_ALLOCATION));
		
		// Set object field information
		this.objectFieldSet(rv, BootState._OBJECT_CLASS_INFO,
			state._classInfoHandle);
		
		return rv;
	}
	
	/**
	 * Reads a minimized class file for the given class.
	 * 
	 * @param __class The class to boot.
	 * @return The read class data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/16
	 */
	public MinimizedClassFile readClass(ClassName __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Did we already read this class?
		Map<ClassName, MinimizedClassFile> readClasses = this._readClasses;
		MinimizedClassFile rv = readClasses.get(__class);
		if (rv != null)
			return rv;
		
		// Arrays are special and are dynamically created accordingly
		if (__class.isArray() || __class.isPrimitive())
			rv = MinimizedClassFile.decode(Minimizer.minimize(
				ClassFile.special(__class.field())));
		
		// Load in class data
		else
			try (InputStream in = this._rawChunks.get(__class).currentStream())
			{
				rv = MinimizedClassFile.decode(in, this._pool);
				
				// Debug
				Debugging.debugNote("Read %s...", rv.thisName());
			}
		
		// Cache it and use it
		readClasses.put(__class, rv);
		return rv;
	}
	
	/**
	 * Returns the allocation size of the class.
	 * 
	 * @param __class The class.
	 * @return The allocation size.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/24
	 */
	private int __allocSize(ClassState __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		ClassInfoHandle classInfo = __class._classInfoHandle;
		
		// See if it is available
		try
		{
			return classInfo.getInteger(ClassProperty.SIZE_ALLOCATION);
		}
		
		// Needs to be calculated
		catch (NoSuchElementException ignored)
		{
			Deque<ClassState> superChain = this.__classChain(__class);
			
			// Handle what is in the queue to determine our allocation size
			int allocSize = 0;
			while (!superChain.isEmpty())
			{
				ClassState at = superChain.removeFirst();
				
				// The allocation size is what is going up
				allocSize += at.classFile.header.get(
					StaticClassProperty.INT_INSTANCE_FIELD_BYTES);
			}
			
			// Set it accordingly
			classInfo.set(ClassProperty.SIZE_ALLOCATION, allocSize);
			
			// Use that one
			return allocSize;
		}
	}
	
	/**
	 * Determines the base array size.
	 * 
	 * @return The base array size.
	 * @since 2021/01/20
	 */
	int __baseArraySize()
	{
		// Calculate the base size for arrays
		int baseArraySize = this._baseArraySize;
		if (baseArraySize < 0)
			try
			{
				// Load array file, to get information on it
				MinimizedClassFile arrayFile = this.readClass(
					BootState._OBJECT_CLASS.addDimensions(1));
				
				// Is a base object, but also with the array bytes attached
				baseArraySize = this.__baseObjectSize() +
					arrayFile.header.get(
						StaticClassProperty.INT_INSTANCE_FIELD_BYTES);
				
				this._baseArraySize = baseArraySize;
			}
			catch (IOException e)
			{
				// {@squirreljme.error BC0o Could not read class.}
				throw new InvalidClassFormatException("BC0o", e);
			}
		
		return baseArraySize;
	}
	
	/**
	 * Determines the base array size.
	 * 
	 * @return The base array size.
	 * @since 2021/01/20
	 */
	int __baseObjectSize()
	{
		// Calculate the base size for arrays
		int baseObjectSize = this._baseObjectSize;
		if (baseObjectSize < 0)
			try
			{
				// Load object to get its size
				MinimizedClassFile objectFile = this.readClass(
					BootState._OBJECT_CLASS);
				
				// Is just the field size of the class
				baseObjectSize = objectFile.header.get(
					StaticClassProperty.INT_INSTANCE_FIELD_BYTES);
				
				this._baseObjectSize = baseObjectSize;
			}
			catch (IOException e)
			{
				// {@squirreljme.error BC0p Could not read class.}
				throw new InvalidClassFormatException("BC0p", e);
			}
		
		return baseObjectSize;
	}
	
	/**
	 * Creates the {@link StaticVmAttribute} mapping.
	 * 
	 * @return The memory handle for attributes.
	 * @throws IOException On read errors.
	 * @since 2021/01/24
	 */
	private MemHandle __buildVmAttributes()
		throws IOException
	{
		PropertyListHandle rv = this._memHandles.allocList(
			MemHandleKind.STATIC_VM_ATTRIBUTES, StaticVmAttribute.NUM_METRICS);
		
		// The base size of object
		int objectBase = this.loadClass("java/lang/Object")
			._classInfoHandle.getInteger(ClassProperty.SIZE_ALLOCATION);
		
		rv.set(StaticVmAttribute.SIZE_OBJECT,
			objectBase);
		rv.set(StaticVmAttribute.OFFSETOF_ARRAY_LENGTH_FIELD,
			objectBase + this.loadClass("[I").classFile
				.field(false, BootState._ARRAY_LENGTH).offset);
		rv.set(StaticVmAttribute.OFFSETOF_OBJECT_TYPE_FIELD,
			this.loadClass("java/lang/Object").classFile
				.field(false, BootState._OBJECT_CLASS_INFO).offset);
		
		return rv;
	}
	
	/**
	 * Returns the class chain, the uppermost class is first in the chain.
	 * 
	 * @param __class The class to get the chain of.
	 * @return The class queue chain.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/24
	 */
	private LinkedList<ClassState> __classChain(ClassState __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// Load the class chain
		LinkedList<ClassState> superChain = new LinkedList<>();
		for (ClassState at = __class; at != null; at = at._superClass)
		{
			// Process into the queue
			superChain.addFirst(at);
			
			// If this is not the object class, and our super class is not
			// yet known then fill it in
			if (!at.thisName.isObject() && at._superClass == null)
			{
				// This should be created but it might not have reached
				// the initialization point, due to cyclic super class
				// loading.
				at._superClass = this.loadClass(at.classFile.superName());
			}
		}
		
		return superChain;
	}
	
	/**
	 * Returns the class depth for a given class.
	 * 
	 * @param __class The class to get the depth of.
	 * @return The class depth.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/24
	 */
	private int __classDepth(ClassState __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		ClassInfoHandle classInfo = __class._classInfoHandle;
		
		// See if it is available
		try
		{
			return classInfo.getInteger(ClassProperty.INT_CLASSDEPTH);
		}
		
		// Needs to be calculated
		catch (NoSuchElementException ignored)
		{
			// Load the class chain
			Deque<ClassState> superChain = this.__classChain(__class);
			
			// The depth is just the size of the class chain minus one, since
			// Object is depth zero
			int depth = superChain.size() - 1;
			
			// Set it accordingly
			classInfo.set(ClassProperty.INT_CLASSDEPTH, depth);
			
			// Use that one
			return depth;
		}
	}
	
	/**
	 * Returns the kind of handle used for the class.
	 * 
	 * @param __cl The class to get.
	 * @return The handle kind of the class.
	 * @since 2021/01/24
	 */
	private int __handleKind(ClassName __cl)
	{
		// If not an array is just an object instance
		if (!__cl.isArray())
			return MemHandleKind.OBJECT_INSTANCE;
		
		// Otherwise it is an array type
		PrimitiveType pType = __cl.componentType().primitiveType();
		if (pType == null)
			return MemHandleKind.OBJECT_ARRAY;
			
		switch (pType)
		{
			case BOOLEAN:
			case BYTE:
				return MemHandleKind.BYTE_ARRAY;
			
			case CHARACTER:
				return MemHandleKind.CHARACTER_ARRAY;
				
			case DOUBLE:
				return MemHandleKind.DOUBLE_ARRAY;
				
			case FLOAT:
				return MemHandleKind.FLOAT_ARRAY;
				
			case INTEGER:
				return MemHandleKind.INTEGER_ARRAY;
				
			case LONG:
				return MemHandleKind.LONG_ARRAY;
				
			case SHORT:
				return MemHandleKind.SHORT_ARRAY;
				
			default:
				throw Debugging.oops(__cl);
		}
	}
	
	/**
	 * Loads the specified pool entry into memory and returns the handle.
	 *
	 * @param __clPool The class runtime pool;
	 * @param __entry The entry to load.
	 * @return The loaded pool value.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	private Object __loadPool(BasicPool __clPool, BasicPoolEntry __entry)
		throws IOException, NullPointerException
	{
		if (__clPool == null || __entry == null)
			throw new NullPointerException("NARG");
		
		// Needed pool information
		DualClassRuntimePool dualPool = this._pool;
		BasicPool staticDualPool = dualPool.classPool();
		ChunkSection poolChunk = this._poolChunk;
		
		// Determine where the pool is located
		int poolOff = poolChunk.futureAddress().get();
		
		// Debugging
		Debugging.debugNote("Load pool entry: %s", __entry);
		
		// TODO: Remove fail value
		int failValue = 0x3E_00_0000 |
			(__entry.index & 0xFFFF) |
			(__entry.type().ordinal() << 16);
		
		// Depends on the type used
		switch (__entry.type())
		{
				// Accessed field
			case ACCESSED_FIELD:
				{
					AccessedField access = __entry.<AccessedField>value(
						AccessedField.class);
					FieldReference ref = access.field;
					
					// Is this static?
					boolean isStatic = access.type.isStatic();
					
					// We can refer to fields that are in a super class by
					// using the subclass class.
					// ex: ArrayList.modCount -> AbstractList.modCount
					for (ClassState at = this.loadClass(ref.className());
						at != null; at = at._superClass)
					{
						// Determine the source field
						MinimizedField minField = at.classFile.field(
							isStatic, ref.memberNameAndType());
						if (minField == null)
							continue;
						
						// Static is the direct offset with the array base
						if (isStatic)
							return this.__baseArraySize() + minField.offset;
						
						// Whereas instance is based on the class base
						return at._classInfoHandle.getInteger(
							ClassProperty.OFFSETBASE_INSTANCE_FIELDS) +
							minField.offset;
					}
						
					// {@squirreljme.error BC0q Could not resolve field
					// access entry. (The entry)}
					throw new InvalidClassFormatException(
						"BC0q " + __entry);
				}
			
				// Class information
			case CLASS_INFO_POINTER:
				return this.loadClass(__entry.<ClassInfoPointer>value(
					ClassInfoPointer.class).name)._classInfoHandle;
			
				// Class constant pool reference
			case CLASS_POOL:
				return this.loadClass(__entry.<ClassPool>value(
					ClassPool.class).name)._poolMemHandle;
				
				// A static method that has been invoked
			case INVOKED_METHOD:
				InvokedMethod invokedMethod = __entry.<InvokedMethod>value(
					InvokedMethod.class);
				
				// Call of static method, this is a direct method pointer to
				// the method code
				if (invokedMethod.type() == InvokeType.STATIC)
				{
					MethodHandle handle = invokedMethod.handle;
					
					// Load the target class
					ClassState inClass = this.loadClass(handle.outerClass());
					
					// Find the target method
					// {@squirreljme.error BC0m The specified method does not
					// exist. (The method)}
					MinimizedMethod method = inClass.classFile.method(
						true, handle.nameAndType());
					if (method == null)
						throw new InvalidClassFormatException(
							"BC0m " + handle);
					
					// {@squirreljme.error BC0n Pure native method call not
					// wrapped or processed. (The method)}
					if (method.flags().isNative())
					{
						Debugging.todoNote("Fail on pure native: %s",
							handle);
						break;
						/*throw new InvalidClassFormatException(
							"BC0n " + handle);*/
					}
					
					// Where is the code located?
					return this.__calcMethodCodeAddr(inClass, true,
						method);
				}
				
				Debugging.todoNote("Handle other: %s", __entry);
				break;
				
				// A noted string for debugging purposes, this directly points
				// to the ROM for String data
			case NOTED_STRING:
				BasicPoolEntry poolStr =
					staticDualPool.byIndex(__entry.part(0));
				
				// These point to Strings that prefix with [hash$16 len$16]
				return new BootJarPointer(poolOff + poolStr.offset + 4);
			
				// String used as a constant value, should be pre-loaded and
				// interned accordingly by the bootstrap.
			case USED_STRING:
				return this.loadString(__entry.value.toString());
		}
		
		if (false)
			throw Debugging.todo();
		Debugging.todoNote("Handle pool entry: %s", __entry);
		return failValue;
	}
	
	/**
	 * Calculates the method code address.
	 * 
	 * @param __inClass The class this is in.
	 * @param __static Is this a static method?
	 * @param __method The method to get.
	 * @return The pointer to the method code.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/10/29
	 */
	private BootJarPointer __calcMethodCodeAddr(ClassState __inClass,
		boolean __static, MinimizedMethod __method)
		throws NullPointerException
	{
		if (__inClass == null || __method == null)
			throw new NullPointerException("NARG");
		
		int base = __inClass.classFile.header.get(__static ?
			StaticClassProperty.OFFSET_STATIC_METHOD_DATA :
			StaticClassProperty.OFFSET_INSTANCE_METHOD_DATA); 
		
		return new BootJarPointer(base + __method.codeoffset,
			this._rawChunks.get(__inClass.thisName).futureAddress());
	}
	
	/**
	 * Calculates the VTable for the given class, a VTable contains all of
	 * the method references and such.
	 * 
	 * @param __class The class to calculate for.
	 * @return The calculated and initialized VTable for a class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/26
	 */
	private MemHandle __calcVTable(ClassState __class)
		throws IOException, NullPointerException
	{
		if (__class == null)
			throw new NullPointerException("NARG");
		
		// State for the current class
		List<VTableMethod> methodInfo = new ArrayList<>();
		
		// We need the class chain to build the method table and to determine
		// which classes and such to lookup within 
		LinkedList<ClassState> classChain = this.__classChain(__class);
		for (ListIterator<ClassState> chainLink = classChain.listIterator();
			chainLink.hasNext();)
		{
			// Process all methods within this class
			ClassState at = chainLink.next();
			for (MinimizedMethod method : at.classFile.methods(false))
			{
				VTableMethod vMethod;
				MethodFlags flags = method.flags();
				
				// Private methods just point to themselves
				boolean isInit = method.name.isAnyInitializer();
				if (flags.isPrivate())
					vMethod = new VTableMethod(
						this.__calcMethodCodeAddr(at, false, method),
						at._poolMemHandle);
				
				// Start from the highest class and look down for a matching
				// method
				else
					throw Debugging.todo();
				
				// Register the method
				methodInfo.add(vMethod);
			}
		}
		
		// Allocate VTable information
		int numMethods = methodInfo.size();
		ListValueHandle vTable = this._memHandles.allocList(
			MemHandleKind.VIRTUAL_VTABLE, numMethods * 2);
		
		// Build VTable information into an actual VTable
		for (int i = 0, o = 0; i < numMethods; i++, o += 2)
		{
			VTableMethod method = methodInfo.get(i);
			
			vTable.set(o, method.execAddr);
			vTable.set(o + 1, method.poolHandle);
		} 
		
		// Use this handle
		return vTable;
	}
}
