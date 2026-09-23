// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.system;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown whenever there are file management errors in Media Stores
 * such as {@code TorucaStore}, {@code MovieStore}, and {@link ImageStore}.
 *
 * Errors include, but are not limited to: An element not being found, or said
 * store being full and not accepting any more elements.
 *
 * @since 2026/08/28
 */
@Api
public class StoreException
	extends Exception
{
	/** Store is full, cannot accept new elements. */
	@Api
	public static final int STORE_FULL = 1;

	/** The specified element was not found within the store. */
	@Api
	public static final int NOT_FOUND = 2;

	/** Undefined status, generic store error. */
	@Api
	public static final int UNDEFINED = 0;

	/** Represents the status of the exception. */
	private final int _status;

	/**
	 * Creates a new StoreException with an empty information message.
	 *
	 * @since 2026/08/28
	 */
	@Api
	public StoreException()
	{
		this(StoreException.UNDEFINED);
	}

	/**
	 * Creates a new StoreException with a specific status.
	 *
	 * @param __status This StoreException's status, may be either
	 * {@link #UNDEFINED}, {@link #STORE_FULL}, or {@link #NOT_FOUND}.
	 * @since 2026/08/28
	 */
	@Api
	public StoreException(int __status)
	{
		this(__status, null);
	}

	/**
	 * Creates a new StoreException with a specific status and informational
	 * message.
	 *
	 * @param __status This StoreException's status, may be either
	 * {@link #UNDEFINED}, {@link #STORE_FULL}, or {@link #NOT_FOUND}.
	 * @param __message The informational message for the new StoreException.
	 * @since 2026/08/28
	 */
	@Api
	public StoreException(int __status, String __message)
	{
		super(__message);

		this._status = __status;
	}

	/**
	 * Retrieves the status value of this StoreException.
	 *
	 * @return The StoreException's status value.
	 * @since 2026/08/28
	 */
	@Api
	public int getStatus()
	{
		return this._status;
	}
}
