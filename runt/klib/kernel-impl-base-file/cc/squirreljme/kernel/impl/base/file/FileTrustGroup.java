// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.runtime.cldc.SystemTrustGroup;
import cc.squirreljme.runtime.cldc.SystemTrustGroupUtils;
import java.nio.file.Path;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This represents a trust group which is backed by the file system.
 *
 * @since 2018/02/10
 */
public final class FileTrustGroup
	implements SystemTrustGroup
{
	/** Is this trust group trusted? */
	public static final JavaManifestKey IS_TRUSTED =
		new JavaManifestKey("Is-Trusted");
	
	/** The name this is associated with. */
	public static final JavaManifestKey NAME =
		new JavaManifestKey("Name");
	
	/** The vendor this is associated with. */
	public static final JavaManifestKey VENDOR =
		new JavaManifestKey("Vendor");
	
	/** Lock to prevent intertwined read/write. */
	protected final Object lock =
		new Object();
	
	/** The path to the group information. */
	protected final Path path;
	
	/** The index of this group. */
	protected final int index;
	
	/**
	 * Initializes the file trust group.
	 *
	 * @param __p The path to the group.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/10
	 */
	FileTrustGroup(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
		this.index = Integer.parseInt(__p.getFileName().toString(), 10);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof SystemTrustGroup))
			return false;
		
		return SystemTrustGroupUtils.equals(this, (SystemTrustGroup)__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/12
	 */
	@Override
	public int hashCode()
	{
		return SystemTrustGroupUtils.hashCode(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final boolean isTrusted()
	{
		return Boolean.valueOf(__Utils__.__get(this.lock, this.path,
			FileTrustGroup.IS_TRUSTED));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final String name()
	{
		return __Utils__.__get(this.lock, this.path, FileTrustGroup.NAME);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/10
	 */
	@Override
	public final String vendor()
	{
		return __Utils__.__get(this.lock, this.path, FileTrustGroup.VENDOR);
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __lock The locking object.
	 * @param __p The manifest path.
	 * @param __k The key to set.
	 * @param __v The value to set, {@code null} clears it.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/02/10
	 */
	final void __set(JavaManifestKey __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		__Utils__.__set(this.lock, this.path, __k, __v);
	}
}

