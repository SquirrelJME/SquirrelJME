// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.Objects;
import net.multiphasicapps.classfile.CFFieldFlags;
import net.multiphasicapps.classfile.CFFieldReference;
import net.multiphasicapps.classfile.CFMethodFlags;
import net.multiphasicapps.classfile.CFMethodReference;
import net.multiphasicapps.classprogram.CPComputeMachine;
import net.multiphasicapps.classprogram.CPInvokeType;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This is the computational machine which performs the actual logic operations
 * which are needed for the interpreter to actually perform work.
 *
 * @since 2016/04/08
 */
public class JVMComputeMachine
	implements CPComputeMachine<JVMStackFrame>
{
	/** The owning engine. */
	protected final JVMEngine engine;	
	
	/**
	 * Initializes the computing machine.
	 *
	 * @param __e The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/08
	 */
	JVMComputeMachine(JVMEngine __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __e;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws JVMNoClassDefFoundError If the class was not found.
	 * @since 2016/04/09
	 */
	@Override
	public void allocateObject(JVMStackFrame __frame, int __dest,
		ClassNameSymbol __cl)
		throws JVMNoClassDefFoundError
	{
		// This thread
		JVMThread thread = __frame.thread();
		
		// Find the class representation
		JVMEngine engine = __frame.engine();
		JVMClass jcl = engine.classes().loadClass(__cl);
		
		// {@squirreljme.error IN0j Could not find a definition for the given
		// class. (The missing class)}
		if (jcl == null)
			throw new JVMNoClassDefFoundError(__frame, String.format("IN0j %s",
				__cl));
		
		// Check if the method can be accessed or not
		if (!thread.checkAccess(jcl))
			throw new JVMEngineException(__frame);
		
		// Debug
		System.err.printf("DEBUG -- allocateObject(%d, %s)%n", __dest, __cl);
		
		// Allocate a new object of that kind
		JVMObject obj = engine.objects().spawnObject(thread, jcl);
		
		// Store it in the given varaible
		JVMVariable[] vars = __frame.variables();
		JVMVariable var = vars[__dest];
		if (!(var instanceof JVMVariable.OfObject))
			vars[__dest] = (var = JVMVariable.OfObject.empty());
		
		// Set it
		JVMVariable.OfObject vo = (JVMVariable.OfObject)var;
		vo.set(obj);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public void copy(JVMStackFrame __frame, int __dest, int __src)
	{
		// Do nothing if the same
		if (__dest == __src)
			return;
		
		// Get variables to copy
		JVMVariable[] vars = __frame.variables();
		JVMVariable src = vars[__src];
		JVMVariable dest = vars[__dest];
		
		// Debug
		System.err.printf("DEBUG -- Copy %d (%s) -> %d (%s)%n",
			__src, Objects.toString(src),
			__dest, Objects.toString(dest));
		
		// The destination may need to be replaced
		if (dest == null ||
			!(dest.getClass().isInstance(src.getClass())))
			vars[__dest] = dest = JVMVariable.empty(src);
		
		// Set the value of the destination to the source
		dest.set((Object)src.get());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public void getStaticField(JVMStackFrame __frame, int __dest,
		CFFieldReference __f)
	{
		// Debug
		System.err.printf("DEBUG -- Read Static %s %d%n", __f, __dest);
		
		// Obtain the static field
		JVMField field = __getStaticField(__frame, __f);
		
		// Get field value
		Object fv = field.getStaticValue();
		
		// Variable needs creation?
		JVMVariable[] vars = __frame.variables();
		
		// Set it or new it?
		vars[__dest] = JVMVariable.setOrNew(vars[__dest], fv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/15
	 */
	@Override
	public void invoke(JVMStackFrame __frame, int __dest,
		CFMethodReference __ref, CPInvokeType __type, int... __args)
	{
		// Debug
		System.err.printf("DEBUG -- Invoke method %d %s %s %s%n", __dest,
			__ref, __type, MissingCollections.boxedList(__args));
			
		// Our current source class
		JVMClass frameclass = __frame.inClass();
		
		// Get the instance to perform the invocation on
		JVMVariable[] vars = __frame.variables();
		JVMObject instance;
		if (__type.isInstance())
		{
			// {@squirreljme.error IN0m Cannot invoke a null instance.}
			if (null == (instance = ((JVMVariable.OfObject)vars[__args[0]]).
				get()))
				throw new JVMNullPointerException(__frame, "IN0m");
		}
		
		// Static call	
		else
			instance = null;
		
		// The method to invoke
		JVMMethod invokethis;
		
		// Get the class of the given method
		JVMClass basecl = engine.classes().loadClass(__ref.className().
			symbol());
		JVMObject baseclo = basecl.classObject(__frame.thread());
		
		// Find a method by the given name
		JVMMethod baseit = basecl.methods().get(__ref.memberName(),
			__ref.memberType());
		
		// {@squirreljme.error IN0q Could not locate the specified method to
		// derive an invocation from. (The static method)}
		if (baseit == null)
			throw new JVMIncompatibleClassChangeError(__frame,
				String.format("IN0q %s", __ref));
		
		// Static invoke?
		if (__type == CPInvokeType.STATIC)
		{
			// {@squirreljme.error IN0t Cannot statically invoke a non-static
			// method. (The given method)}
			if (!baseit.flags().isStatic())
				throw new JVMIncompatibleClassChangeError(__frame,
					String.format("IN0t %s", __ref));
			
			// Use this method
			invokethis = baseit;
		}
		
		// Special invoke?
		else if (__type == CPInvokeType.SPECIAL)
		{
			// A new method is chosen if the following is true:
			// 1. SUPER flag is set for the calling class
			// 2. The method's class is a superclass of our class
			// 3. The class is not an initialization method
			if (frameclass.flags().isSpecialInvokeSpecial() &&
				frameclass.isSuperClass(basecl) &&
				!baseit.isInitializer())
				throw new Error("TODO");
			
			// Otherwise use the given method
			else
				invokethis = baseit;
		}
		
		// Invoke on an interface
		else if (__type == CPInvokeType.INTERFACE)
			throw new Error("TODO");
		
		// Called on an instance of a method
		else if (__type == CPInvokeType.VIRTUAL)
			throw new Error("TODO");
		
		// Unknown
		else
			throw new RuntimeException("WTFX");
		
		// {@squirreljme.error IN0u Not permitted to access the specified
		// method. (The method to invoke)}
		if (!__frame.thread().checkAccess(invokethis))
			throw new JVMIncompatibleClassChangeError(__frame,
				String.format("IN0u %s", invokethis));
		
		// {@squirreljme.error IN0v Cannot invoke an abstract or static method.
		// (The method to invoke; The access flags)}
		CFMethodFlags mf = invokethis.flags();
		if (mf.isAbstract() || mf.isNative())
			throw new JVMIncompatibleClassChangeError(__frame,
				String.format("IN0v %s %s", invokethis, mf));
		
		// Get the target class and make sure it is initialized
		JVMClass cl = invokethis.outerClass();
		
		// If an instance it must be one of the class containing the method
		if (instance != null)
		{
			// {@squirreljme.error IN0n The instance does not extend or
			// implement the given class. (The expected class type; The current
			// class type)}
			if (!cl.isInstance(instance))
				throw new JVMClassCastException(__frame,
					String.format("IN0n %s %s", cl, instance.classType()));
		}
		
		// Setup calling arguments
		int n = __args.length;
		Object[] call = new Object[n];
		for (int i = 0; i < n; i++)
			call[i] = vars[__args[i]].get();
		
		// Invoke it
		invokethis.interpret(__frame.thread(), false, call);
		
		// Handle return value
		if (__ref.memberType().returnValue() != null)
			throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/16
	 */
	@Override
	public void putStaticField(JVMStackFrame __frame, CFFieldReference __f,
		int __src)
	{
		// Debug
		System.err.printf("DEBUG -- Put Static %s %d%n", __f, __src);
		
		// Obtain the static field
		JVMField field = __getStaticField(__frame, __f);
		
		// Place the value
		field.setStaticValue((__frame.variables()[__src]).get());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/16
	 */
	@Override
	public void returnValue(JVMStackFrame __frame, int __src)
	{
		// Handle return of value
		if (__src >= 0)
			throw new Error("TODO");
		
		// Mark frame to be returned from
		__frame.markReturn();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/16 
	 */
	@Override
	public void tossException(JVMStackFrame __frame, int __object)
	{
		// Debug
		System.err.printf("DEBUG -- Throw exception %d.%n", __object);
		
		throw new Error("TODO");
	}
	
	/**
	 * Obtains the specified static field.
	 *
	 * @param __frame The execution frame.
	 * @param __f The field to obtain.
	 * @return The static field.
	 * @since 2016/04/16
	 */
	JVMField __getStaticField(JVMStackFrame __frame, CFFieldReference __f) 
	{
		// Get and initialize the class
		JVMClass cl = engine.classes().loadClass(__f.className().symbol());
		JVMObject clo = cl.classObject(__frame.thread());
		
		// Locate the field
		JVMField f = cl.fields().get(__f.memberName(), __f.memberType());
		
		// {@squirreljme.error IN0x The specified field is not static.
		// (The field to obtain; The flags of that field)}
		CFFieldFlags ff = f.flags();
		if (!ff.isStatic())
			throw new JVMIncompatibleClassChangeError(__frame, String.format(
				"IN0x %s %s", __f, ff));
		
		// {@squirreljme.error IN0y Cannot access the given static field.
		// (The static field)}
		if (!__frame.checkAccess(f))
			throw new JVMIncompatibleClassChangeError(__frame, String.format(
				"IN0y %s", __f));
		
		// Use it
		return f;
	}
}

