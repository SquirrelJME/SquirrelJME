// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Suspension policy.
 *
 * @since 2021/03/13
 */
public enum SuspendPolicy
	implements __IdNumbered__
{
	/** None. */
	NONE(0),
	
	/** Stop the thread generating the event. */
	EVENT_THREAD(1),
	
	/** Stop absolutely everything. */
	ALL(2),
	
	/* End. */
	;
	
	/** Quick lookup. */
	private static final __QuickTable__<SuspendPolicy> _QUICK =
		new __QuickTable__<>(SuspendPolicy.values());
	
	/** The event ID. */
	public final int id;
	
	/**
	 * Initializes the constant.
	 * 
	 * @param __id The identifier.
	 * @since 2021/03/13
	 */
	SuspendPolicy(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
	
	/**
	 * Looks up the constant by the given Id.
	 * 
	 * @param __id The Id.
	 * @return The found constant.
	 * @since 2021/03/13
	 */
	public static SuspendPolicy of(int __id)
	{
		return SuspendPolicy._QUICK.get(__id);
	}
}
