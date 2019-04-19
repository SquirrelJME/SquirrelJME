// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import dev.shadowtail.classfile.mini.MinimizedMethod;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This represents a class which has been loaded.
 *
 * @since 2019/01/06
 */
public final class LoadedClass
{
	/** The minimized class. */
	protected final MinimizedClassFile miniclass;
	
	/** The super class. */
	protected final LoadedClass superclass;
	
	/** The component type. */
	protected final LoadedClass componentclass;
	
	/** Runtime constant pool, which is initialized when it is needed. */
	protected final RuntimeConstantPool runpool;
	
	/** The number of bytes into an object where this class starts. */
	protected final int startifbytes;
	
	/** Total instance size so far for fields. */
	protected final int totalifbytes;
	
	/** Static initializer, if any. */
	protected final StaticMethodHandle staticinit;
	
	/** Interface classes. */
	final LoadedClass[] _interfaces;
	
	/** Static methods. */
	private final Map<MethodNameAndType, StaticMethodHandle> _smethods;
	
	/** Instance methods. */
	private final Map<MethodNameAndType, StaticMethodHandle> _imethods;
	
	/** Static fields, minimized form is used for linking. */
	private final Map<FieldNameAndType, MinimizedField> _sfields;
	
	/** Instance fields, minimized form is used for linking. */
	private final Map<FieldNameAndType, MinimizedField> _ifields;
	
	/** String form. */
	private Reference<String> _string;
	
	/** The position in the static field area, determined on load. */
	volatile int _startsfbytes;
	
	/** Has this class been initialized? */
	volatile boolean _beeninit;
	
	/** Claimed static field space yet? */
	volatile boolean _claimedsfspace;
	
	/**
	 * Initializes the loaded class.
	 *
	 * @param __cf The minimized class file.
	 * @param __sn The super class.
	 * @param __in The interfaces.
	 * @param __ct The component type, for arrays.
	 * @throws NullPointerException On null arguments, except for {@code __sn}
	 * and {@code __ct}.
	 * @since 2019/04/17
	 */
	public LoadedClass(MinimizedClassFile __cf, LoadedClass __sn,
		LoadedClass[] __in, LoadedClass __ct)
		throws NullPointerException
	{
		if (__cf == null || __in == null)
			throw new NullPointerException("NARG");
		
		for (LoadedClass o : (__in = __in.clone()))
			if (o == null)
				throw new NullPointerException("NARG");
		
		this.miniclass = __cf;
		this.superclass = __sn;
		this.componentclass = __ct;
		this._interfaces = __in;
		
		// Calculate byte size for fields in this class
		int ifbytes = __cf.header.ifbytes,
			startifbytes = (__sn == null ? 0 : __sn.totalifbytes);
		this.startifbytes = startifbytes;
		this.totalifbytes = startifbytes + ifbytes;
		
		// Run-time constant pool
		RuntimeConstantPool runpool;
		this.runpool = (runpool = new RuntimeConstantPool(__cf.pool()));
		
		// Initialize static methods
		Map<MethodNameAndType, StaticMethodHandle> smethods =
			new LinkedHashMap<>();
		for (MinimizedMethod mm : __cf.methods(true))
			smethods.put(new MethodNameAndType(mm.name, mm.type),
				new StaticMethodHandle(runpool, mm));
		this._smethods = smethods;
		
		// Static initializer
		this.staticinit = smethods.get(new MethodNameAndType(
			new MethodName("<clinit>"), new MethodDescriptor("()V")));
		
		// Initialize instance methods
		// Note that these are initialized as static handles to refer to them
		// directly, just instance based lookup will use a different handle
		// type...
		Map<MethodNameAndType, StaticMethodHandle> imethods =
			new LinkedHashMap<>();
		for (MinimizedMethod mm : __cf.methods(false))
			imethods.put(new MethodNameAndType(mm.name, mm.type),
				new StaticMethodHandle(runpool, mm));
		this._imethods = imethods;
		
		// Get static field info
		Map<FieldNameAndType, MinimizedField> sfields = new LinkedHashMap<>();
		for (MinimizedField ff : __cf.fields(true))
			sfields.put(new FieldNameAndType(ff.name, ff.type), ff);
		this._sfields = sfields;
		
		// Get instance field info
		Map<FieldNameAndType, MinimizedField> ifields = new LinkedHashMap<>();
		for (MinimizedField ff : __cf.fields(false))
			ifields.put(new FieldNameAndType(ff.name, ff.type), ff);
		this._ifields = ifields;
	}
	
	/**
	 * Is this an array?
	 *
	 * @return If this is an array or not.
	 * @since 2019/04/18
	 */
	public final boolean isArray()
	{
		return this.componentclass != null;
	}
	
	/**
	 * Checks if this class is a super class of the given class.
	 *
	 * @param __cl The class to check.
	 * @return If this class is a super class of the given class.
	 * @since 2019/04/17
	 */
	public final boolean isSuperClass(LoadedClass __cl)
	{
		// Only need to check super classes
		for (LoadedClass at = this.superclass; at != null; at = at.superclass)
			if (at == __cl)
				return true;
		
		return false;
	}
	
	/**
	 * Looks up the given field.
	 *
	 * @param __static Lookup static field?
	 * @param __n The name.
	 * @param __t The type.
	 * @return The field information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	public final MinimizedField lookupField(boolean __static,
		String __n, String __t)
		throws NullPointerException
	{
		return this.lookupField(__static,
			new FieldName(__n), new FieldDescriptor(__t));
	}
	
	/**
	 * Looks up the given field.
	 *
	 * @param __static Lookup static field?
	 * @param __n The name.
	 * @param __t The type.
	 * @return The field information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	public final MinimizedField lookupField(boolean __static,
		FieldName __n, FieldDescriptor __t)
		throws NullPointerException
	{
		return this.lookupField(__static,
			new FieldNameAndType(__n, __t));
	}
	
	/**
	 * Looks up the given field.
	 *
	 * @param __static Lookup static field?
	 * @param __nat The name and type.
	 * @return The field information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	public final MinimizedField lookupField(boolean __static,
		FieldNameAndType __nat)
		throws NullPointerException
	{
		if (__nat == null)
			throw new NullPointerException("NARG");
		
		MinimizedField rv;
		
		// Use static method handle
		if (__static)
			rv = this._sfields.get(__nat);
		else
			rv = this._ifields.get(__nat);
		
		// {@squirreljme.error AE0d No such field exists. (Is static?; The
		// name and type of the field)}
		if (rv == null)
			throw new VMIncompatibleClassChangeException(String.format(
				"AE0d %b %s", __static, __nat));
		
		return rv;
	}
	
	/**
	 * Looks up the given method.
	 *
	 * @param __lut The type of lookup to perform.
	 * @param __static Is the specified method static?
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The handle to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final MethodHandle lookupMethod(MethodLookupType __lut,
		boolean __static, String __name, String __desc)
		throws NullPointerException
	{
		return this.lookupMethod(__lut, __static, new MethodName(__name),
			new MethodDescriptor(__desc));
	}
	
	/**
	 * Looks up the given method.
	 *
	 * @param __lut The type of lookup to perform.
	 * @param __static Is the specified method static?
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The handle to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public final MethodHandle lookupMethod(MethodLookupType __lut, 
		boolean __static, MethodName __name, MethodDescriptor __desc)
		throws NullPointerException
	{
		return this.lookupMethod(__lut, __static,
			new MethodNameAndType(__name, __desc));
	}
	
	/**
	 * Looks up the given method.
	 *
	 * @param __lut The type of lookup to perform.
	 * @param __static Is the specified method static?
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The handle to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	public final MethodHandle lookupMethod(MethodLookupType __lut, 
		boolean __static, MethodNameAndType __nat)
		throws NullPointerException
	{
		// Static lookup always returns the discovered method so it is called
		// directly rather than virtually
		if (__static || __lut == MethodLookupType.STATIC)
		{
			StaticMethodHandle rv;
			
			// Use static method handle
			if (__static)
				rv = this._smethods.get(__nat);
			else
				rv = this._imethods.get(__nat);
			
			// {@squirreljme.error AE07 The target method does not exist
			// in the class. (This class; The lookup type; Is this a static
			// lookup?; The name and type)}
			if (rv == null)
				throw new VMIncompatibleClassChangeException(
					String.format("AE07 %s %s %b %s",
						this.miniclass.thisName(), __lut, __static, __nat));
			
			return rv;
		}
		
		// This depends on the instance, so only the class and method name
		// are used as it is looked up on load
		else if (__lut == MethodLookupType.INSTANCE)
			return new InstanceMethodHandle(this.miniclass.thisName(), __nat);
		
		// Similar to instance lookup but starts at the super-class instead
		else if (__lut == MethodLookupType.SUPER)
			return new SuperMethodHandle(this.miniclass.superName(), __nat);
		
		throw new todo.OOPS(__lut.name());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/17
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "Class " +
				this.miniclass.thisName()));
		
		return rv;
	}
}

