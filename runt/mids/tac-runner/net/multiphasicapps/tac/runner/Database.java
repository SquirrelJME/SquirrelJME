// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteManager;
import javax.microedition.swm.SuiteType;

/**
 * This class loads and stores the database for the test system.
 *
 * @since 2018/10/17
 */
public final class Database
	implements Iterable<SingleUnit>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/17
	 */
	@Override
	public final Iterator<SingleUnit> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Builds the test database.
	 *
	 * @return The built test database.
	 * @since 2018/10/17
	 */
	public static final Database build()
	{
		Map<String, SingleUnit> units = new HashMap<>();
		
		// Need this to go through all the available suites for testing
		SuiteManager sm = ManagerFactory.getSuiteManager();
		for (Suite s : sm.getSuites(SuiteTypes.APPLICATION))
		{
			
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}

