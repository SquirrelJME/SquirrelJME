/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JDWP Packet Support.
 * 
 * @file
 * @since 2024/02/04
 */

#ifndef SJME_C_JDWP_H
#define SJME_C_JDWP_H

#include "sjme/config.h"
#include "sjme/nvm/nvm.h"
#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JDWP_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A JDWP command set.
 *
 * @since 2025/09/07
 */
typedef enum sjme_jdwp_commandSet
{
	/** Unknown. */
	SJME_JDWP_COMMAND_SET_UNKNOWN = -1,
	
	/** Virtual machine. */
	SJME_JDWP_COMMAND_SET_VIRTUAL_MACHINE = 1,

	/** Reference types. */
	SJME_JDWP_COMMAND_SET_REFERENCE_TYPE = 2,

	/** Class types. */
	SJME_JDWP_COMMAND_SET_CLASS_TYPE = 3,

	/** Methods. */
	SJME_JDWP_COMMAND_SET_METHODS = 6,

	/** Objects. */
	SJME_JDWP_COMMAND_SET_OBJECT_REFERENCE = 9,

	/** Strings. */
	SJME_JDWP_COMMAND_SET_STRING_REFERENCE = 10,

	/** Threads. */
	SJME_JDWP_COMMAND_SET_THREAD_REFERENCE = 11,

	/** Thread groups. */
	SJME_JDWP_COMMAND_SET_THREAD_GROUP_REFERENCE = 12,

	/** Arrays. */
	SJME_JDWP_COMMAND_SET_ARRAY_REFERENCE = 13,

	/** Class loaders. */
	SJME_JDWP_COMMAND_SET_CLASS_LOADER = 14,

	/** Events. */
	SJME_JDWP_COMMAND_SET_EVENT_REQUEST = 15,

	/** Stack frames. */
	SJME_JDWP_COMMAND_SET_STACK_FRAMES = 16,

	/** Object references. */
	SJME_JDWP_COMMAND_SET_CLASS_OBJECT_REFERENCE = 17,
} sjme_jdwp_commandSet;

/**
 * A JDWP command.
 *
 * @since 2025/09/07
 */
typedef enum sjme_jdwp_command
{
	/** Unknown. */
	SJME_JDWP_COMMAND_UNKNOWN = -1,
} sjme_jdwp_command;

/**
 * JDWP Packet flags.
 *
 * @since 2025/09/07
 */
typedef enum sjme_jdwp_packetFlag
{
	/** The packet is a reply. */
	SJME_JDWP_FLAG_REPLY = 0x80,
} sjme_jdwp_packetFlag;

/**
 * JDWP packet structure.
 *
 * @since 2025/09/07
 */
typedef struct sjme_jdwp_packet
{
	/** The packet flags. */
	sjme_jubyte flags;

	/** The packet id. */
	sjme_jint id;

	/** The packet header. */
	union
	{
		/** The header if this is a command set. */
		struct
		{
			/** The command set of a packet. */
			sjme_jdwp_commandSet commandSet;

			/** The command. */
			sjme_jdwp_command command;
		} command;

		/** The header if this is a reply. */
		struct
		{
			/** The error code. */
			sjme_jshort error;
		} reply;
	} header;

	/** The length of the data. */
	sjme_jint length;

	/** The actual packet data. */
	sjme_alignPointer sjme_jbyte data[sjme_flexibleArrayCount];
} sjme_jdwp_packet;

/** Is the given packet a reply packet? */
#define SJME_JDWP_IS_REPLY(packet) \
	(((packet)->flags & SJME_JDWP_FLAG_REPLY) != 0)

/**
 * Function for any task that needs to be performed for JDWP.
 *
 * @param session The debugging session.
 * @param packet The optional packet associated with the task.
 * @param extraData Any optional extra data for the job.
 * @since 2025/09/07
 */
typedef sjme_errorCode (*sjme_jdwp_taskFunction)(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInNullable sjme_jdwp_packet* packet,
	sjme_attrInValue sjme_intPointer extraData);

/**
 * Represents a task that is waiting to be completed.
 *
 * @since 2025/09/07
 */
typedef struct sjme_jdwp_taskItem
{
	/** The function to execute. */
	sjme_jdwp_taskFunction function;

	/** The packet data. */
	sjme_jdwp_packet* packet;

	/** Any extra data as needed. */
	sjme_intPointer extraData;
} sjme_jdwp_taskItem;

/** The maximum number of tasks that can be waiting for completion. */
#define SJME_JDWP_MAX_WAITING_TASKS 32

/** The maximum number of packets that can be discarded at once. */
#define SJME_JDWP_MAX_PACKET_DISCARDS 64
	
struct sjme_jdwpBase
{
	/** The allocation pool to use. */
	sjme_alloc_pool allocPool;

	/** The virtual machine state to access. */
	sjme_nvm inState;

	/** The lock for polling input. */
	sjme_thread_spinLock inLock;
	
	/** The stream to read data from the remote debugger. */
	sjme_stream_input in;

	/** The lock for writing output packets. */
	sjme_thread_spinLock outLock;

	/** The stream to write data to the remote debugger. */
	sjme_stream_output out;

	/** The lock for task handling. */
	sjme_thread_spinLock taskLock;

	/** The tasks to execute. */
	sjme_jdwp_taskItem tasks[SJME_JDWP_MAX_WAITING_TASKS];

	/** The number of tasks awaiting execution. */
	sjme_atomic(sjme_jint) awaitingTasks;

	/** The lock for discarded packets. */
	sjme_thread_spinLock discardLock;

	/** Discarded packets. */
	struct
	{
		/** The length of this discard. */
		sjme_jint length;

		/** The packet that has been discarded. */
		sjme_jdwp_packet* packet;
	} discards[SJME_JDWP_MAX_PACKET_DISCARDS];
};

/**
 * Receives the next packet.
 * 
 * @param session The session to read from.
 * @param outPacket The resultant packet, will
 * be @c NULL if no packet was read.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_commReceive(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrOutNotNull sjme_jdwp_packet** outPacket);

/**
 * Allocates a packet within the session, which may pull from a set of
 * previously allocated packets that have been discard.
 * 
 * @param session The session to allocate a packet within.
 * @param length The length of the packet.
 * @param outPacket The resultant packet.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_packetAlloc(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull sjme_jdwp_packet** outPacket);

/**
 * Discards the packet, this may place the packet into an internal discard
 * buffer so that allocations can be reused.
 * 
 * @param session The session the packet should be discarded into.
 * @param packet The packet to discard.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_packetDiscard(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInNotNull sjme_jdwp_packet* packet);

/**
 * Initializes a new JDWP session.
 * 
 * @param allocPool The allocation pool to use.
 * @param inState The state to debug.
 * @param outSession The resultant session.
 * @param in The input from the remote debugger.
 * @param out The output to the remote debugger.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_sessionNew(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInNotNull sjme_stream_input in,
	sjme_attrInNotNull sjme_stream_output out);

#if !defined(SJME_CONFIG_NETWORK_NONE)

/**
 * Initializes a new JDWP session that is connected over a TCP network.
 * 
 * @param allocPool The allocation pool to use.
 * @param inState The state to debug.
 * @param outSession The resultant session.
 * @param listening Is the debugging listening?
 * @param address The address to connect or to bind to.
 * @param port The port to connect to or to bind to.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_sessionNewTcpNetwork(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_jdwp* outSession,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port);

#endif

/**
 * Polls the JDWP session and perform any needed actions, this is considered
 * the main loop for JDWP.
 * 
 * @param session The session to poll.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_sessionPoll(
	sjme_attrInNotNull sjme_jdwp session);

/**
 * Pushes a task to be performed.
 * 
 * @param session The debugging session.
 * @param function The function to call.
 * @param packet The optional packet to reference.
 * @param extraData Any extra data to use.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
sjme_errorCode sjme_jdwp_taskPush(
	sjme_attrInNotNull sjme_jdwp session,
	sjme_attrInNotNull sjme_jdwp_taskFunction function,
	sjme_attrInNullable sjme_jdwp_packet* packet,
	sjme_attrInValue sjme_intPointer extraData);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JDWP_H
}
		#undef SJME_CXX_SQUIRRELJME_JDWP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JDWP_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JDWP_H */
