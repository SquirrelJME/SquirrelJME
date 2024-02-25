// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.PacketAddressBracket;
import cc.squirreljme.jvm.mle.brackets.PacketPortBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This shelf contains support for any packet based communication protocol
 * such as Serial, TCP/IP, UDP/IP, Bluetooth, and others. This is meant to
 * be as generic as possible.
 * 
 * This follows the model of
 * https://pubs.opengroup.org/onlinepubs/9699919799/functions/socket.html .
 *
 * @since 2024/02/25
 */
@SquirrelJMEVendorApi
public final class PacketCommShelf
{
	/**
	 * Not used.
	 *
	 * @since 2024/02/25
	 */
	private PacketCommShelf()
	{
	}
	
	public static native PacketPortBracket commAccept()
		throws MLECallError;
	
	public static native PacketPortBracket commBind()
		throws MLECallError;
	
	public static native PacketPortBracket commClose()
		throws MLECallError;
	
	public static native PacketPortBracket commConnect()
		throws MLECallError;
	
	public static native PacketPortBracket commConnectApprove()
		throws MLECallError;
	
	public static native PacketPortBracket commConnectDeny()
		throws MLECallError;
	
	public static native PacketPortBracket commConnectRequest()
		throws MLECallError;
	
	public static native PacketPortBracket commConnectRequestPoll()
		throws MLECallError;
	
	public static native void commFdCtl()
		throws MLECallError;
	
	public static native void commIoCtl()
		throws MLECallError;
	
	public static native PacketPortBracket commListen()
		throws MLECallError;
	
	public static native PacketAddressBracket[] hostLookup(String __hostLike)
		throws MLECallError;
	
	public static native PacketAddressBracket[] hostLookupScan()
		throws MLECallError;
	
	public static native void hostPair()
		throws MLECallError;
	
	public static native void portClose()
		throws MLECallError;
	
	public static native void portDataRecv()
		throws MLECallError;
	
	public static native void portDataRecvFrom()
		throws MLECallError;
	
	public static native void portDataSend()
		throws MLECallError;
	
	public static native void portDataSendTo()
		throws MLECallError;
	
	public static native void portFdCtl()
		throws MLECallError;
	
	public static native void portIoCtl()
		throws MLECallError;
	
	public static native void portName()
		throws MLECallError;
	
	public static native void portNameOfPeer()
		throws MLECallError;
	
	public static native String[] protocols()
		throws MLECallError;
}
