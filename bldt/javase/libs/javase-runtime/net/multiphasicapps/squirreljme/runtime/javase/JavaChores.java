// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chore;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chores;

/**
 * This is used to manage and launch chores within the Java run-time.
 *
 * This class is thread safe and utilizes a number of threads since multiple
 * things may be happening at once.
 *
 * @since 2017/12/07
 */
public class JavaChores
	extends Chores
{
	/** The current chore. */
	protected final JavaChore current;
	
	/** The chores which are currenly existing. */
	private final List<JavaChore> _chores =
		new ArrayList<>();
	
	/**
	 * Initializes the chore manager.
	 *
	 * @param __current The current chore.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public JavaChores(JavaChore __current)
		throws NullPointerException
	{
		if (__current == null)
			throw new NullPointerException("NARG");
		
		this.current = __current;
		
		// Always add the current chore to the chore list
		this._chores.add(__current);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public Chore current()
	{
		return this.current;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	protected Chore[] internalList(boolean __sys)
	{
		List<Chore> rv = new ArrayList<>();
		
		List<JavaChore> chores = this._chores;
		synchronized (this.lock)
		{
			for (JavaChore c : chores)
			{
				if (!__sys && c.isSystem())
					continue;
				
				rv.add(c);
			}
		}
		
		return rv.<Chore>toArray(new Chore[rv.size()]);
	}
}

