# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# -----------------------------------------------------------------------------
# Start code for IEEE1275 SPARC

.globl _start
.globl _die
.globl sjme_ieee1275BootArch
.globl sjme_ieee1275EntryFunc
.globl sjme_ieee1275EntryArgs
.globl sjme_ieee1275EntryNumArgs
.globl IEEE1275_WrappedEntryCall

.section .text.start

_start:

	# %o3 contains the interface function, so copy it to g_IEEE1275EntryFunc.
	sethi %hi(sjme_ieee1275EntryFunc), %o0
	or %o0, %lo(sjme_ieee1275EntryFunc), %o0
	st %o3, [%o0]

	# %o2 = args, %o3 = len

	# Jump to platform main
	bl sjme_ieee1275BootArch

	# If this is reached =(
_die:
	b _die

	# For SPARC IEEE1275, there is a different calling method used to pass to OFW
		# Prototype: void IEEE1275_WrappedEntryCall(IEEE1275_EntryFunc_t
		# a_Entry, const IEEE1275_Addr_t a_Data);
			# %o0 = Argument array, replaced with return value
			# %o7 + 8 = Return to this location
			# %o0 - %o5 are NOT preserved
IEEE1275_WrappedEntryCall:

	# Save room for  registers
	save %sp, -36, %sp
	st %o0, [%sp + 0]
	st %o1, [%sp + 4]
	st %o2, [%sp + 8]
	st %o3, [%sp + 12]
	st %o4, [%sp + 16]
	st %o5, [%sp + 20]
	st %o7, [%sp + 24]

	# Setup arguments
	mov %o0, %o2
	mov %o1, %o0

	sethi %hi(_return_point - 8), %o7
	or %o7, %lo(_return_point - 8), %o7

	# Call handler
	call %o2
	nop
_return_point:

	# Restore arguments
	ld [%sp + 0], %o0
	ld [%sp + 4], %o1
	ld [%sp + 8], %o2
	ld [%sp + 12], %o3
	ld [%sp + 16], %o4
	ld [%sp + 20], %o5
	ld [%sp + 24], %o7

	# Restore local area
	restore


