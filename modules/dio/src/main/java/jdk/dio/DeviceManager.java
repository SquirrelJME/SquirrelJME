// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.util.Iterator;

@Api
public class DeviceManager
{
	@Api
	public static final int EXCLUSIVE =
		1;
	
	@Api
	public static final int SHARED =
		2;
	
	@Api
	public static final int UNSPECIFIED_ID =
		-1;
	
	@Api
	private DeviceManager()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <P extends Device<? super P>> void addRegistrationListener(
		RegistrationListener<P> __a, Class<P> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <P extends Device<? super P>> Iterator<DeviceDescriptor<P>>
		list()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static <P extends Device<? super P>> Iterator<DeviceDescriptor<P>>
		list(Class<P> __a)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(Class<P> __a, 
		DeviceConfig<? super P> __b)
		throws IOException, InvalidDeviceConfigException, 
			UnsupportedDeviceTypeException, DeviceNotFoundException, 
			UnavailableDeviceException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new InvalidDeviceConfigException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(Class<P> __a, 
		DeviceConfig<? super P> __b, int __c)
		throws IOException, InvalidDeviceConfigException, 
			UnsupportedDeviceTypeException, DeviceNotFoundException, 
			UnavailableDeviceException, UnsupportedAccessModeException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new InvalidDeviceConfigException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		if (false)
			throw new UnsupportedAccessModeException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(int __a)
		throws IOException, DeviceNotFoundException, 
			UnavailableDeviceException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(int __a, Class<P> __b)
		throws IOException, UnsupportedDeviceTypeException, 
			DeviceNotFoundException, UnavailableDeviceException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(int __a, Class<P> __b,
		int __c)
		throws IOException, UnsupportedDeviceTypeException, 
			DeviceNotFoundException, UnavailableDeviceException, 
			UnsupportedAccessModeException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		if (false)
			throw new UnsupportedAccessModeException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(int __a, int __b)
		throws IOException, DeviceNotFoundException, 
			UnavailableDeviceException, UnsupportedAccessModeException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		if (false)
			throw new UnsupportedAccessModeException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(DeviceConfig<? super P
		> __a)
		throws IOException, InvalidDeviceConfigException, 
			UnsupportedDeviceTypeException, DeviceNotFoundException, 
			UnavailableDeviceException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new InvalidDeviceConfigException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(DeviceConfig<? super P
		> __a, int __b)
		throws IOException, InvalidDeviceConfigException, 
			UnsupportedDeviceTypeException, DeviceNotFoundException, 
			UnavailableDeviceException, UnsupportedAccessModeException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new InvalidDeviceConfigException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		if (false)
			throw new UnsupportedAccessModeException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(String __a, Class<P> 
		__b, int __c, String... __d)
		throws IOException, UnsupportedDeviceTypeException, 
			DeviceNotFoundException, UnavailableDeviceException, 
			UnsupportedAccessModeException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		if (false)
			throw new UnsupportedAccessModeException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> P open(String __a, Class<P> 
		__b, String... __c)
		throws IOException, UnsupportedDeviceTypeException, 
			DeviceNotFoundException, UnavailableDeviceException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new UnavailableDeviceException();
		throw Debugging.todo();
	}
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	public static <P extends Device<? super P>> int register(int __a, Class<P
		> __b, DeviceConfig<? super P> __c, String __d, String... __e)
		throws IOException, UnsupportedDeviceTypeException, 
			InvalidDeviceConfigException, DeviceNotFoundException, 
			DeviceAlreadyExistsException
	{
		if (false)
			throw new IOException();
		if (false)
			throw new UnsupportedDeviceTypeException();
		if (false)
			throw new InvalidDeviceConfigException();
		if (false)
			throw new DeviceNotFoundException();
		if (false)
			throw new DeviceAlreadyExistsException();
		throw Debugging.todo();
	}
	
	@Api
	public static <P extends Device<? super P>> void 
		removeRegistrationListener(RegistrationListener<P> __a, Class<P> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void unregister(int __a)
	{
		throw Debugging.todo();
	}
}


