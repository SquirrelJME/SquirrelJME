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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	 * Initializes the database of units.
	 *
	 * @param __it The units to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/29
	 */
	public Database(Iterable<SingleUnit> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
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
		List<SingleUnit> units = new LinkedList<>();
		
		// Need this to go through all the available suites for testing
		SuiteManager sm = ManagerFactory.getSuiteManager();
		for (Suite s : sm.getSuites(SuiteType.APPLICATION))
		{todo.DEBUG.note("Check suite: %s", s);
			// This is not a test program for SquirrelJME, ignore
			if (!Boolean.valueOf(s.getAttributeValue("x-squirreljme-tests")))
				continue;
			
			// Load single units for tests
			for (Iterator<String> it = s.getMIDlets(); it.hasNext();)
				units.add(new SingleUnit(s, it.next()));
		}
		
		// Debug
		todo.DEBUG.note("Loaded units: %s", units);
		
		// Build the final database
		return new Database(units);
	}
}

