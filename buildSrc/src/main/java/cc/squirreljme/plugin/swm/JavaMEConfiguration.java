// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

import cc.squirreljme.plugin.util.StringUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This represents a configuration such as CLDC which specifies which base
 * classes are available. Configurations may optionally be "compact" in which
 * they are a lighter version.
 *
 * @since 2016/12/14
 */
public final class JavaMEConfiguration
	implements Comparable<JavaMEConfiguration>, MarkedDependency, MarkedProvided
{
	/** CDC Application. */
	public static final APIName CDC_NAME =
		new APIName("CDC");
	
	/** CLDC Application. */
	public static final APIName CLDC_NAME =
		new APIName("CLDC");
	
	/** Name. */
	protected final APIName name;
	
	/** Version. */
	protected final SuiteVersion version;
	
	/** Is this configuration compact? */
	protected final boolean compact;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the configuration using the given API name and version.
	 *
	 * @param __n The name to use.
	 * @param __v The version of the suite.
	 * @param __c If {@code true} then the configuration is compact.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public JavaMEConfiguration(APIName __n, SuiteVersion __v, boolean __c)
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
	 * @param __n The string to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/30
	 */
	public JavaMEConfiguration(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error DG02 Expected two or three fields for the
		configuration. (The input string)} */
		String[] fields = StringUtils.fieldSplit('-', __n);
		int fn = fields.length;
		if (fn != 2 && fn != 3)
			throw new InvalidSuiteException(String.format("AR02 %s", __n));
		
		// Potentially compact?
		this.compact = (fn > 2 &&
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
	public int compareTo(JavaMEConfiguration __o)
	{
		APIName aName = this.name;
		APIName bName = __o.name;
		
		// CDC is always better than CLDC
		if (aName.equals(JavaMEConfiguration.CDC_NAME) &&
			!bName.equals(JavaMEConfiguration.CDC_NAME))
			return 1;
		
		int rv = aName.compareTo(bName);
		if (rv != 0)
			return rv;
		
		rv = this.version.compareTo(__o.version);
		if (rv != 0)
			return rv;
		
		// Compact is before non-compact
		boolean a = this.compact,
			b = __o.compact;
		if (a != b)
			return (a ? -1 : 1);
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JavaMEConfiguration))
			return false;
		
		JavaMEConfiguration o = (JavaMEConfiguration)__o;
		return this.name.equals(o.name) &&
			this.version.equals(o.version) &&
			this.compact == o.compact;
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
	 * @since 2017/12/31
	 */
	@Override
	public boolean isOptional()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public boolean matchesProvided(MarkedProvided __mp)
		throws NullPointerException
	{
		if (__mp == null)
			throw new NullPointerException("NARG");
		
		return this.equals(__mp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/30
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "" +
				this.name + "-" + this.version +
				(this.compact ? "-compact" : "")));
		
		return rv;
	}
	
	/**
	 * Returns the version of this configuration.
	 *
	 * @return The configuration version.
	 * @since 2017/12/05
	 */
	public SuiteVersion version()
	{
		return this.version;
	}
}

