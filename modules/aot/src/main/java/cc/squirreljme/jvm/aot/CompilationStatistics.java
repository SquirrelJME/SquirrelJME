// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableSet;

/**
 * Statistics for compilation.
 *
 * @since 2022/09/06
 */
public final class CompilationStatistics
	extends AbstractMap<CompilationStatistic, Statistic>
{
	/** Internal storage. */
	private final Map<CompilationStatistic, Statistic> _storage =
		new EnumTypeMap<CompilationStatistic, Statistic>(
			CompilationStatistic.class, CompilationStatistic.values());
	
	/**
	 * Initializes the statistics container.
	 * 
	 * @since 2022/09/06
	 */
	public CompilationStatistics()
	{
		// Initialize all the statistics
		Map<CompilationStatistic, Statistic> storage = this._storage;
		for (CompilationStatistic statistic : CompilationStatistic.values())
			storage.put(statistic, new Statistic());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/06
	 */
	@Override
	public Statistic get(Object __key)
	{
		return this._storage.get(__key);
	}
	
	/**
	 * Gets the value of the given statistic.
	 * 
	 * @param __key The key to get.
	 * @return The value of the statistic.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/06
	 */
	public int getValue(CompilationStatistic __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		return this._storage.get(__key).get();
	}
	
	/**
	 * Increments the given statistic.
	 * 
	 * @param __key The key to get.
	 * @param __by The amount to increase by.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/06
	 */
	public void increment(CompilationStatistic __key, int __by)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this._storage.get(__key).increment(__by);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/06
	 */
	@Override
	public Set<Entry<CompilationStatistic, Statistic>> entrySet()
	{
		return UnmodifiableSet.of(this._storage.entrySet());
	}
}
