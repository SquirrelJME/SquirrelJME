/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * RetroArch GamePad and Controller Support.
 * 
 * @since 2021/02/27
 */

#include "frontend/libretro/lrjoypad.h"
#include "frontend/libretro/lrlocal.h"
#include "debug.h"

/** Input descriptions:
 * [SOFT LEFT] [SOFT RIGHT]
 * [BACK]
 * [1  o~o] [2  abc] [3  def]
 * [4  ghi] [5  jkl] [6  mno]
 * [7 pqrs] [8  tuv] [9 wxyz]
 * [*  shf] [0    +] [# spac]
 * RetroPad looks like so:
 *    X-1
 * Y-7   A-3
 *    B-9   
 */
const struct retro_input_descriptor sjme_inputDesc[] =
{
	/* SOFT LEFT. */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_SELECT, "Soft 1 (Left Shoulder)"},
	
	/* SOFT RIGHT. */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_START, "Soft 2 (Right Shoulder)"},
	
	/* [1  o~o] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_X, "1 Voicemail (Game A)"},
	
	/* [2  abc] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_UP, "2 ABC (Game Up)"},
	
	/* [3  def] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_A, "3 DEF (Game B)"},
	
	/* [4  ghi] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_LEFT, "4 GHI (Game Left)"},
	
	/* [5  jkl] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_L2, "5 JKL (Game Fire/Select)"},
	
	/* [6  mno] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_RIGHT, "6 MNO (Game Right)"},
	
	/* [7 pqrs] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_Y, "7 PQRS (Game C)"},
	
	/* [8  tuv] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_DOWN, "8 TUV (Game Down)"},
	
	/* [9 wxyz] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_B, "9 WXYZ (Game D)"},
	
	/* [*  shf] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_L, "Star (Shift)"},
	
	/* [0    +] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_R2, "0 Operator (Plus)"},
	
	/* [# spac] */
	{0, RETRO_DEVICE_JOYPAD, 0,
		RETRO_DEVICE_ID_JOYPAD_R, "Pound (Space)"},
	
	/* End. */
	{0, 0, 0, 0, NULL}
};

/** Sets controller port device. */
SJME_GCC_USED void retro_set_controller_port_device(unsigned port,
	unsigned device)
{
	sjme_todo("Set controller port?");
}

/**
 * Set input polling callback.
 * 
 * @param cb The callback for input polling.
 * @since 2021/02/27
 */
SJME_GCC_USED void retro_set_input_poll(retro_input_poll_t cb)
{
	g_libRetroCallbacks.inputPollFunc = cb;
}

/**
 * Set input state callback.
 * 
 * @param callback The callback for input state.
 * @since 2021/02/27
 */
SJME_GCC_USED void retro_set_input_state(retro_input_state_t callback)
{
	g_libRetroCallbacks.inputStateFunc = callback;
}

void sjme_libRetro_joyPadSetEnvironment(retro_environment_t callback)
{
	/* Describe the SquirrelJME Controller layout. */
	callback(RETRO_ENVIRONMENT_SET_INPUT_DESCRIPTORS, sjme_inputDesc);
}
