// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

/**
 * This implements the interface for ZIP structure data and provides a class
 * for initialization.
 *
 * @param <L> The class extending this.
 * @since 2016/03/08
 */
@Deprecated
public abstract class AbstractZIPStructureElement
	<L extends AbstractZIPStructureElement>
	implements ZIPStructureElement
{
	/** The element before this one. */
	protected final L before;
	
	/** The offset to this element. */
	protected final long offset;
	
	/** The type of value here. */
	protected final Type type;
	
	/** The variable field length. */
	protected final L variablefield;
	
	/**
	 * Initializes the structured ZIP element, the offset is automatically
	 * determined.
	 *
	 * @param __bef The element before this one.
	 * @param __t The type of value stored at this position.
	 * @param __var The variable field length specifier.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/03/08
	 */
	public AbstractZIPStructureElement(L __bef, Type __t, L __var)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		before = __bef;
		type = __t;
		variablefield = __var;
		
		// If not variable then the offset is simple
		if (variablefield == null)
		{
			// No entry before this one
			if (before == null)
				offset = 0;
			
			// Otherwise right after the sized type
			else
				offset = before.offset() + before.type().size();
		}
		
		// If variable
		else
		{
			// If the element before this is variable then use the same offset
			if (before.variableField() != null)
				offset = before.offset();
			
			// Otherwise use an offset after its size
			else
				offset = before.offset() + before.type().size();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @sine 2016/03/08
	 */
	public L before()
	{
		return before;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/08
	 */
	public long offset()
	{
		return offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/08
	 */
	public Type type()
	{
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/08
	 */
	public L variableField()
	{
		return variablefield;
	}
	
}

