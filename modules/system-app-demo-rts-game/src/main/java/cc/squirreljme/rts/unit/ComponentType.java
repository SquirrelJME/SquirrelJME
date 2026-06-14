// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.unit;

/**
 * Identifier for each component type.
 *
 * @since 2026/06/11
 */
public enum ComponentType
{
	/** This component does nothing. */
	NONE(NoneComponent.class),
	
	/* End. */
	;
	
	/** The subtype of the component. */
	private final Class<? extends BaseComponent> subType;
	
	/**
	 * Initializes the component type enum.
	 *
	 * @param __subType The subtype class.
	 * @since 2026/06/11
	 */
	ComponentType(Class<? extends BaseComponent> __subType)
	{
		this.subType = __subType;
	}
	
	/**
	 * Maps the component to the given type, this is checked to ensure it is
	 * proper.
	 *
	 * @param <B> The type to map to.
	 * @param __b The component to map.
	 * @return The mapped component.
	 * @since 2026/06/11
	 */
	@SuppressWarnings("unchecked")
	public <B extends BaseComponent> B map(BaseComponent __b)
	{
		return (B)this.subType.cast(__b);
	}
}
