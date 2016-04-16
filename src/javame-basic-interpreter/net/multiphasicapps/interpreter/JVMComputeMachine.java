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

import net.multiphasicapps.classfile.CFFieldReference;
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
		
		// Get source to copy
		JVMVariable[] vars = __frame.variables();
		JVMVariable src = vars[__src];
		
		// The destination may need to be replaced
		JVMVariable dest = vars[__dest];
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
	public void getStaticField(JVMStackFrame __frame, int __dest,
		CFFieldReference __f)
	{
		// Debug
		System.err.printf("DEBUG -- Read Static %s%n", __f);
		
		// Get and initialize the class
		JVMClass cl = engine.classes().loadClass(__f.className().symbol());
		JVMObject clo = cl.classObject(__frame.thread());
		
		throw new Error("TODO");
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
		
		// Resolve the method to be invoked
		JVMMethod res = __resolveMethod(__frame, instance, __ref, __type);
		
		// Get the target class and make sure it is initialized
		JVMClass cl = res.outerClass();
		
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
		
		throw new Error("TODO");
	}
	
	/**
	 * Resolves the given method.
	 *
	 * @param __frame The stack frame.
	 * @param __i The object instance.
	 * @param __ref The class and name of the method.
	 * @param __type How the method is to be invoked.
	 * @return The resolved method.
	 * @throws JVMClassFormatError If the method is not resolvable.
	 * @throws NullPointerException On null arguments, except for {@code __i}.
	 */
	private JVMMethod __resolveMethod(JVMStackFrame __frame, JVMObject __i,
		CFMethodReference __ref, CPInvokeType __type)
		throws JVMClassFormatError, NullPointerException
	{
		// Check
		if (__ref == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Static initialization?
		if (__type == CPInvokeType.STATIC)
		{
			// Get the class of the given method
			JVMClass cl = engine.classes().loadClass(__ref.className().
				symbol());
			JVMObject clo = cl.classObject(__frame.thread());
			
			// Find a method by the given name
			JVMMethod rv = cl.methods().get(__ref.memberName(),
				__ref.memberType());
			if (rv != null)
				return rv;
			
			// {@squirreljme.error IN0q Could not locate the specified static
			// method. (The static method)}
			throw new JVMIncompatibleClassChangeError(__frame, String.format(
				"IN0q %s", __ref));
		}
		
		// Called on an instance of a method
		else
		{
			// Go through all classes 
		
			throw new Error("TODO");
		}
	}
}

