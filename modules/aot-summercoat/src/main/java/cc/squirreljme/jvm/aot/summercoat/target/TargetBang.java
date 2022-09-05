// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.target;

import cc.squirreljme.runtime.cldc.util.StringUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class represents the format of a target bang which is similar to
 * GCC's triplets except it is not ambiguous with dashes and such.
 * 
 * The format is {@code arch-family!arch-variant!os!os-variant}, all fields
 * must be specified and additionally if a variant is unknown then {@code none}
 * is used.
 *
 * @see TargetArchitecture
 * @see TargetOperatingSystem
 * @since 2022/09/04
 */
public final class TargetBang
	implements Comparable<TargetBang>
{
	/** The architecture family. */
	public final TargetArchitecture arch;
	
	/** The variant of the architecture. */
	public final TargetArchitectureVariant archVariant;
	
	/** The operating system family. */
	public final TargetOperatingSystem os;
	
	/** The variant of the operating system. */
	public final TargetOperatingSystemVariant osVariant;
	
	/** Cached string. */
	private Reference<String> _string;
	
	/**
	 * Initializes the target bang.
	 * 
	 * @param __arch The architecture family used.
	 * @param __archVariant The variant of the architecture.
	 * @param __os The operating system used.
	 * @param __osVariant The operating system variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public TargetBang(TargetArchitecture __arch,
		TargetArchitectureVariant __archVariant, TargetOperatingSystem __os,
		TargetOperatingSystemVariant __osVariant)
		throws NullPointerException
	{
		if (__arch == null || __archVariant == null ||
			__os == null || __osVariant == null)
			throw new NullPointerException("NARG");
		
		this.arch = __arch;
		this.archVariant = __archVariant;
		this.os = __os;
		this.osVariant = __osVariant;
	}
	
	/**
	 * Gets the target bang for the given bang representation.
	 * 
	 * @param __bang The bang to use.
	 * @throws IllegalArgumentException If the bang is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public TargetBang(String __bang)
		throws IllegalArgumentException, NullPointerException
	{
		if (__bang == null)
			throw new NullPointerException("NARG");
		
		// Split by bang
		// {@squirreljme.error RA01 Bang does not have the correct number
		// of groups. (The bang)}
		String[] split = StringUtils.basicSplit('!', __bang);
		if (split.length != 4)
			throw new IllegalArgumentException("RA01 " + __bang);
		
		// Normalize all forms
		for (int i = 0, n = split.length; i < n; i++)
			split[i] = TargetBang.__nameForm(split[i]);
		
		// Find architecture and OS, since these are variant free
		TargetArchitecture arch = TargetArchitecture.valueOf(split[0]);
		TargetOperatingSystem os = TargetOperatingSystem.valueOf(split[2]);
		
		// Find arch variant
		TargetArchitectureVariant archVariant = null;
		for (TargetArchitectureVariant variant : arch.variants())
			if (split[1].equals(variant.name()))
			{
				archVariant = variant;
				break;
			}
		
		// {@squirreljme.error RA02 Invalid architecture variant.
		// (The variant)}
		if (archVariant == null)
			throw new IllegalArgumentException("RA02 " + split[1]);
		
		// Find os variant
		TargetOperatingSystemVariant osVariant = null;
		for (TargetOperatingSystemVariant variant : os.variants())
			if (split[3].equals(variant.name()))
			{
				osVariant = variant;
				break;
			}
		
		// {@squirreljme.error RA02 Invalid operating system variant.
		// (The variant)}
		if (osVariant == null)
			throw new IllegalArgumentException("RA03 " + split[3]);
		
		// Set
		this.arch = arch;
		this.archVariant = archVariant;
		this.os = os;
		this.osVariant = osVariant;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/04
	 */
	@Override
	public int compareTo(TargetBang __b)
	{
		int rv = this.arch.compareTo(__b.arch);
		if (rv != 0)
			return rv;
		
		rv = this.archVariant.name().compareTo(__b.archVariant.name());
		if (rv != 0)
			return rv;
		
		rv = this.os.compareTo(__b.os);
		if (rv != 0)
			return rv;
		
		return this.osVariant.name().compareTo(__b.osVariant.name());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public int hashCode()
	{
		return this.arch.hashCode() ^ this.archVariant.hashCode() ^
			this.os.hashCode() ^ this.osVariant.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public boolean equals(Object __other)
	{
		// Quick checks
		if (this == __other)
			return true;
		if (!(__other instanceof TargetBang))
			return false;
		
		return 0 == this.compareTo((TargetBang)__other);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public String toString()
	{
		// Already cached?
		Reference<String> ref = this._string;
		String rv;
		if (ref != null && (rv = ref.get()) != null)
			return rv;
		
		// Set it up
		rv = String.format("%s!%s!%s!%s",
			TargetBang.__friendlyForm(this.arch.name()),
			TargetBang.__friendlyForm(this.archVariant.name()),
			TargetBang.__friendlyForm(this.os.name()),
			TargetBang.__friendlyForm(this.osVariant.name()));
		this._string = new WeakReference<>(rv);
		return rv;
	}
	
	/**
	 * Returns the friendly form of the target bang.
	 * 
	 * @param __name The name of the enum.
	 * @return The friendly form of the bang.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	private static String __friendlyForm(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		return __name.toLowerCase().replace('_', '-');
	}
	
	/**
	 * Translates the friendly form to the enumeration form string.
	 * 
	 * @param __friendly The friendly form.
	 * @return The enum name form.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	private static String __nameForm(String __friendly)
		throws NullPointerException
	{
		if (__friendly == null)
			throw new NullPointerException("NARG");
		
		return __friendly.toUpperCase().replace('-', '_');
	}
}
