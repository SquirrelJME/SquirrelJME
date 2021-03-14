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
	
	/** Objects that are known. */
	public final JDWPLinker<JDWPObject> objects =
		new JDWPLinker<>(JDWPObject.class);
	
	/**
	 * Returns a reference type.
	 * 
	 * @param __id The identifier.
	 * @return The reference type.
	 * @since 2021/03/14
	 */
	public final JDWPReferenceType getReferenceType(int __id)
	{
		JDWPClass classy = this.classes.get(__id);
		return (classy != null ? classy : this.objects.get(__id));
	}
}
