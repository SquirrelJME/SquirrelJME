// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPView;
import cc.squirreljme.jdwp.views.JDWPViewKind;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains the complete state for the debugging instance.
 *
 * @since 2021/03/13
 */
public final class JDWPState
{
	/** References to everything the debugger knows about. */
	public final JDWPLinker<Object> items =
		new JDWPLinker<>(Object.class);
	
	/** Thread groups which are available. */
	public final JDWPLinker<JDWPThreadGroup> oldThreadGroups =
		new JDWPLinker<>(JDWPThreadGroup.class);
	
	/** Threads that are available. */
	public final JDWPLinker<JDWPThread> oldThreads =
		new JDWPLinker<>(JDWPThread.class);
	
	/** Frames that may exist from time to time. */
	public final JDWPLinker<JDWPThreadFrame> oldFrames =
		new JDWPLinker<>(JDWPThreadFrame.class);
	
	/** Classes that are known. */
	public final JDWPLinker<JDWPClass> oldClasses =
		new JDWPLinker<>(JDWPClass.class);
	
	/** Objects that are known. */
	public final JDWPLinker<JDWPObject> oldObjects =
		new JDWPLinker<>(JDWPObject.class);
	
	/** Methods that are known. */
	@Deprecated
	public final JDWPLinker<JDWPMethod> oldMethods =
		new JDWPLinker<>(JDWPMethod.class);
	
	/** Fields that are known. */
	@Deprecated
	public final JDWPLinker<JDWPField> oldFields =
		new JDWPLinker<>(JDWPField.class);
	
	/** The binding used. */
	private final Reference<JDWPBinding> _binding;
	
	/** The views that are available. */
	private final JDWPView[] _views =
		new JDWPView[JDWPViewKind.values().length];
	
	/**
	 * Initializes the state.
	 * 
	 * @param __binding The binding used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	JDWPState(Reference<JDWPBinding> __binding)
		throws NullPointerException
	{
		if (__binding == null)
			throw new NullPointerException("NARG");
		
		this._binding = __binding;
	}
	
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
		
		rv = this.oldThreadGroups.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.oldThreads.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.oldFrames.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.oldClasses.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.oldMethods.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.oldFields.get(__id);
		if (rv != null)
			return rv;
		
		rv = this.oldObjects.get(__id);
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
		JDWPClass rv = this.oldClasses.get(__id);
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
		rv = this.oldThreads.get(__id);
		if (rv != null)
			return rv;
		
		// Is a thread group?
		return this.oldThreadGroups.get(__id);
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
		return (classy != null ? classy : this.oldObjects.get(__id));
	}
	
	/**
	 * Returns the view of the given type.
	 * 
	 * @param <V> The type to view.
	 * @param __type The type to view.
	 * @param __kind The kind of viewer to use.
	 * @return The view for the given type.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public final <V extends JDWPView> V view(Class<V> __type,
		JDWPViewKind __kind)
		throws NullPointerException
	{
		if (__type == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Use a pre-cached view if available
		JDWPView[] views = this._views;
		JDWPView view = views[__kind.ordinal()];
		if (view != null)
			return __type.cast(view);
		
		// Obtain the view for this kind of object
		view = this.__binding().debuggerView(__type, __kind,
			new WeakReference<>(this));
		
		// {@squirreljme.error AG0m The binding does not know about this kind
		// of view? (The kind)}
		if (view == null)
			throw new IllegalStateException("AG0m " + __kind);
		
		// Cache for later and use it now
		views[__kind.ordinal()] = view;
		return __type.cast(view);
	}
	
	/**
	 * Returns the used binding.
	 * 
	 * @return The binding used.
	 * @throws IllegalStateException If the binding was GCed.
	 * @since 2021/04/10
	 */
	final JDWPBinding __binding()
		throws IllegalStateException
	{
		// {@squirreljme.error AG0l The Binding was GCed.}
		JDWPBinding rv = this._binding.get();
		if (rv == null)
			throw new IllegalStateException("AG0l");
		
		return rv;
	}
}
