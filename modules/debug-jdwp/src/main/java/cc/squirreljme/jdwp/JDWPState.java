// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This contains the complete state for the debugging instance.
 *
 * @since 2021/03/13
 */
public final class JDWPState
{
	/** Thread groups which are available. */
	public final JDWPLinker<JDWPThreadGroup> threadGroups =
		new JDWPLinker<>(JDWPThreadGroup.class);
	
	/** Threads that are available. */
	public final JDWPLinker<JDWPThread> threads =
		new JDWPLinker<>(JDWPThread.class);
	
	/** Frames that may exist from time to time. */
	public final JDWPLinker<JDWPThreadFrame> frames =
		new JDWPLinker<>(JDWPThreadFrame.class);
	
	/** Classes that are known. */
	public final JDWPLinker<JDWPClass> classes =
		new JDWPLinker<>(JDWPClass.class);
	
	/** Methods that are known. */
	public final JDWPLinker<JDWPMethod> methods =
		new JDWPLinker<>(JDWPMethod.class);
	
	/** Fields that are known. */
	public final JDWPLinker<JDWPField> fields =
		new JDWPLinker<>(JDWPField.class);
	
	/** Objects that are known. */
	public final JDWPLinker<JDWPObject> objects =
		new JDWPLinker<>(JDWPObject.class);
	
	/**
	 * Finds any identified object.
	 * 
	 * @param __id The ID to find.
	 * @return The ID object if found or {@code null} if not.
	 * @since 2021/03/14
	 */
	public JDWPId any(int __id)
	{
		JDWPId rv;
		
		rv = this.threadGroups.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.threads.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.frames.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.classes.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.methods.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.fields.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.objects.get(__id);
		return rv;
	}
	
	/**
	 * The ID to get.
	 * 
	 * @param __id The id to get.
	 * @return Any class compatible.
	 * @since 2021/03/14
	 */
	public final JDWPClass getAnyClass(int __id)
	{
		// Is a well defined class?
		JDWPClass rv = this.classes.get(__id);
		if (rv != null)
			return rv;
		
		// Is a fake object?
		if (__id == __Synthetics__.FAKE_OBJECT.debuggerId())
			return __Synthetics__.FAKE_OBJECT;
		
		// Is a fake class?
		if (__id == __Synthetics__.FAKE_CLASS.debuggerId())
			return __Synthetics__.FAKE_CLASS;
		
		// Is a fake thread?
		if (__id == __Synthetics__.FAKE_THREAD.debuggerId())
			return __Synthetics__.FAKE_THREAD;
		
		// Is a fake thread group?
		if (__id == __Synthetics__.FAKE_THREAD_GROUP.debuggerId())
			return __Synthetics__.FAKE_THREAD_GROUP;
		
		return null;
	}
	
	/**
	 * Returns the object like for this ID.
	 * 
	 * @param __id The identifier.
	 * @return The object like for the ID, may be {@code null} if not found.
	 * @since 2021/03/14
	 */
	public final JDWPObjectLike getObjectLike(int __id)
	{
		// Reference type?
		JDWPObjectLike rv = this.getReferenceType(__id);
		if (rv != null)
			return rv;
		
		// Is a thread?
		rv = this.threads.get(__id);
		if (rv != null)
			return rv;
		
		// Is a thread group?
		return this.threadGroups.get(__id);
	}
	
	/**
	 * Gets the class for the given object type.
	 * 
	 * @param __object The object to get.
	 * @return The class for the given object like.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	public final JDWPClass getObjectLikeClass(JDWPObjectLike __object)
		throws NullPointerException
	{
		if (__object == null)
			throw new NullPointerException("NARG");
		
		// Known well defined reference type
		if (__object instanceof JDWPReferenceType)
		{
			JDWPClass classy = ((JDWPReferenceType)__object).debuggerClass();
			return (classy == null ? __Synthetics__.FAKE_OBJECT : classy);
		}
		
		// Virtual Thread
		if (__object instanceof JDWPThread)
			return __Synthetics__.FAKE_THREAD;
		
		// Virtual Thread group
		if (__object instanceof JDWPThreadGroup)
			return __Synthetics__.FAKE_THREAD_GROUP;
		
		// {@squirreljme.error AG0h The object is not typed. (The type)}
		throw new JDWPException("AG0h " + __object.getClass());
	}
	
	/**
	 * Returns a reference type.
	 * 
	 * @param __id The identifier.
	 * @return The reference type.
	 * @since 2021/03/14
	 */
	public final JDWPReferenceType getReferenceType(int __id)
	{
		JDWPClass classy = this.getAnyClass(__id);
		return (classy != null ? classy : this.objects.get(__id));
	}
}
