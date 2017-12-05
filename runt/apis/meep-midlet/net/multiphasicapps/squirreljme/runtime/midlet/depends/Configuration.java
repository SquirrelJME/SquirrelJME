// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.depends;

import java.util.Arrays;
import java.util.Objects;
import net.multiphasicapps.squirreljme.runtime.midlet.id.APIName;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteVersion;
import net.multiphasicapps.squirreljme.runtime.midlet.InvalidSuiteException;
import net.multiphasicapps.strings.StringUtils;

/**
 * This represents a configuration such as CLDC which specifies which base
 * classes are available. Configurations may optionally be "compact" in which
 * they are a lighter version.
 *
 * @since 2016/12/14
 */
public final class Configuration
	implements BasicAPI, Comparable<Configuration>
{
	/** Name. */
	protected final APIName name;
	
	/** Version. */
	protected final SuiteVersion version;
	
	/** Is this configuration compact? */
	protected final boolean compact;
	
	/**
	 * Initializes the configuration using the given API name and version.
	 *
	 * @param __n The name to use.
	 * @param __v The version of the suite.
	 * @param __c If {@code true} then the configuration is compact.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public Configuration(APIName __n, SuiteVersion __v, boolean __c)
		throws NullPointerException
	{
		if (__n == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this.version = __v;
		this.compact = __c;
	}
	
	/**
	 * Initializes the configuration by parsing the given string.
	 *
	 * @param __s The string to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public Configuration(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AD0p Expected two or three fields for the
		// configuration. (The input string)}
		String[] fields = StringUtils.fieldSplit('-', __n);
		int fn = fields.length;
		if (fn != 2 && fn != 3)
			throw new InvalidSuiteException(String.format("AD0p %s", __n));
		
		// Potentially compact?
		this.compact = (fn >= 2 &&
			0 == fields[2].compareToIgnoreCase("compact"));
		
		// Parse name and version
		this.name = new APIName(fields[0]);
		this.version = new SuiteVersion(fields[1]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public int compareTo(Configuration __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^
			Objects.hashCode(this.version) ^
			(this.compact ? 0xFFFFFFFF : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/05
	 */
	@Override
	public SuiteVersion version()
	{
		return this.version;
	}
}

