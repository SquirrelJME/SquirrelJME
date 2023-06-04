// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Represents a basic type within NanoCoat.
 *
 * @since 2023/05/29
 */
public enum CBasicType
	implements CType
{
	/** Void. */
	VOID("void"),
	
	/** Boolean. */
	JBOOLEAN("jboolean"),
	
	/** Signed Byte. */
	JBYTE("jbyte"),
	
	/** Signed Short. */
	JSHORT("jshort"),
	
	/** Character. */
	JCHAR("jchar"),
	
	/** Integer. */
	JINT("jint"),
	
	/** Long. */
	JLONG("sjme_jlong"),
	
	/** Float. */
	JFLOAT("sjme_jfloat"),
	
	/** Double. */
	JDOUBLE("sjme_jdouble"),
	
	/** Class. */
	JCLASS("sjme_jclass"),
	
	/** Object. */
	JOBJECT("sjme_jobject"),
	
	/** String. */
	JSTRING("sjme_jstring"),
	
	/** Field. */
	JFIELD("sjme_jfield"),
	
	/** Method. */
	JMETHOD("sjme_jmethod"),
	
	/** The NanoCoat state. */
	SJME_NANOSTATE("sjme_nanostate"),
	
	/** A NanoCoat thread. */
	SJME_NANOTHREAD("sjme_nanothread"),
	
	/** A NanoCoat resource. */
	SJME_NANORESOURCE("sjme_nanoresource"),
	
	/** Field information. */
	SJME_NANOFIELDS("sjme_nanofields"),
	
	/** Method information. */
	SJME_NANOMETHODS("sjme_nanomethods"),
	
	/** Stack frame. */
	SJME_NANOFRAME("sjme_nanoframe"),
	
	/* End. */
	;
	
	/** The token used. */
	protected final String token;
	
	/**
	 * Initializes the basic type.
	 * 
	 * @param __token The token for this type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CBasicType(String __token)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		this.token = __token;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW03 Cannot dereference a basic type.}
		throw new IllegalArgumentException("CW03");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public int pointerLevel()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this, 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType rootType()
	{
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public String token()
	{
		return this.token;
	}
}
