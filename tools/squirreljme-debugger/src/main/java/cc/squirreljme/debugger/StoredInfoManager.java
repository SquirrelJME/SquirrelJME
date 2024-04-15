// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * Managers for each specific storage type.
 *
 * @since 2024/01/20
 */
public class StoredInfoManager
{
	/** Stored info storage for each type. */
	private final StoredInfo[] _infos;
	
	/**
	 * Initializes the storage information manager.
	 *
	 * @since 2024/01/20
	 */
	public StoredInfoManager()
	{
		InfoKind[] types = InfoKind.values();
		int numTypes = types.length;
		
		// Setup storage
		StoredInfo[] infos = new StoredInfo[numTypes];
		for (int i = 0; i < numTypes; i++)
			if (types[i].isForgettable())
				infos[i] = new ForgettableStoredInfo(types[i]);
			else
				infos[i] = new PersistentStoredInfo(types[i]);
		this._infos = infos;
	}
	
	/**
	 * Returns the stored information.
	 *
	 * @param <I> The information type.
	 * @param __class The class type used.
	 * @param __type The type to get.
	 * @return The information storage for the given type.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	@SuppressWarnings("unchecked")
	public final <I extends Info> StoredInfo<I> get(Class<I> __class,
		InfoKind __type)
		throws NullPointerException
	{
		if (__class == null || __type == null)
			throw new NullPointerException("NARG");
		
		return (StoredInfo<I>)this._infos[__type.ordinal()];
	}
	
	/**
	 * Returns the class storage manager.
	 *
	 * @return The class storage.
	 * @since 2024/01/22
	 */
	public StoredInfo<InfoByteCode> getByteCodes()
	{
		return this.<InfoByteCode>get(InfoByteCode.class, InfoKind.BYTE_CODE);
	}
	
	/**
	 * Returns the class storage manager.
	 *
	 * @return The class storage.
	 * @since 2024/01/22
	 */
	public StoredInfo<InfoClass> getClasses()
	{
		return this.<InfoClass>get(InfoClass.class, InfoKind.CLASS);
	}
	
	/**
	 * Returns the thread storage.
	 *
	 * @return The thread storage.
	 * @since 2024/01/20
	 */
	public final StoredInfo<InfoThread> getThreads()
	{
		return this.<InfoThread>get(InfoThread.class, InfoKind.THREAD);
	}
}
