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
 * A matcher for the given current thread.
 *
 * @since 2021/03/14
 */
public class ThreadModifierMatcher
	implements EventModifierMatcher
{
	/** The thread to match against. */
	protected final JDWPThread thread;
	
	/**
	 * Initializes the thread matcher.
	 * 
	 * @param __thread The thread to match against.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/14
	 */
	public ThreadModifierMatcher(JDWPThread __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		this.thread = __thread;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/14
	 */
	@Override
	public boolean isMatch(EventModifier __mod)
		throws NullPointerException
	{
		if (!(__mod instanceof ThreadModifier))
			return false;
		
		ThreadModifier mod = (ThreadModifier)__mod;
		return mod.thread == this.thread;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/14
	 */
	@Override
	public boolean mayMatch(EventModifier __mod)
		throws NullPointerException
	{
		return (__mod instanceof ThreadModifier);
	}
}
