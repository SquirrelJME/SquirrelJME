# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Makefile for building SquirrelJME RatufaCoat.

CC=gcc
CFLAGS=-O0 -g3

ratufacoat:	rc_main.o rc_args.o rc_mem.o rc_cpu.o rci_stdc.o
		$(CC) -o $@ $^ $(CFLAGS) $(LIBS)

%.o:		%.c ratufac.h rc_cpu.h
		$(CC) -c -o $@ $< $(CFLAGS)
