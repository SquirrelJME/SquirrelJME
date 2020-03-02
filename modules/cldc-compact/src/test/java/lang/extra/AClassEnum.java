// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.extra;

/**
 * This is a class enumeration.
 *
 * @since 2018/12/08
 */
public enum AClassEnum
{
	/** A. */
	A
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/12/08
		 */
		@Override
		public String boop()
		{
			return "Love you!";
		}
	},
	
	/** B. */
	B
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/12/08
		 */
		@Override
		public String boop()
		{
			return "You are beautiful!";
		}
	},
	
	/** C. */
	C
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/12/08
		 */
		@Override
		public String boop()
		{
			return "You are wonderful!";
		}
	},
	
	/** End. */
	;
	
	/**
	 * Returns a message.
	 *
	 * @return A message.
	 * @since 2018/12/08
	 */
	public abstract String boop();
}

