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
 * The kind of event that is generated.
 *
 * @since 2021/03/13
 */
public enum EventKind
	implements HasId
{
	/** Single Step. */
	SINGLE_STEP(1),
	
	/* End. */
	;
	
	/** Quick table. */
	private static final __QuickTable__<EventKind> _QUICK =
		new __QuickTable__<>(EventKind.values());
	
	/** The event ID. */
	public final int id;
	
	/**
	 * Initializes the event kind.
	 * 
	 * @param __id The identifier.
	 * @since 2021/03/13
	 */
	EventKind(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int id()
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
	public static EventKind of(int __id)
	{
		return EventKind._QUICK.get(__id);
	}
}
