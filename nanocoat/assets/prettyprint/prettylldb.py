# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Pretty printing specifically for LLDB

import lldb
import sys
from assets.prettyprint.prettycore import *
from typing import Any, List

class LLDBMaker(PrettyMaker):
	def __init__(self):
		super().__init__()

	def make_type(self, type_name: str) -> PrettyType | None:
		# See if we can actually find the type
		found = lldb.SBTarget().FindFirstType(type_name)
		if found is None:
			return None

		# Wrap it, use the passed name as the optimal name since we knew we
		# actually asked for it
		return LLDBType(found, type_name)

class LLDBRegister(PrettyRegister):
	def __init__(self):
		super().__init__()

	def register_string(self, debug_type: PrettyType,
			func: Callable[[PrettyValue], str]) -> None:
		# lldb uses commands to do this as follows:
		# type summary add --python-function CharSeqSummary sjme_charSeq
		# So we effectively need to duplicate this
		# There is the @lldb.summary annotation, however that is not available
		# with CLion's old lldb 9. So, this is more roundabout.
		do_not_care: lldb.SBCommandReturnObject = lldb.SBCommandReturnObject()
		(lldb.debugger.GetCommandInterpreter()
			.HandleCommand(f"type summary add "
				f"--python-script "
				f"\"return {func.__name__}(LLDBValue(valobj))\" "
				f"{debug_type.name()}",
			do_not_care))

class LLDBType(PrettyType):
	def __init__(self, debug_type: Any, optimal_name: str | None):
		super().__init__(debug_type, optimal_name)

	def is_pointer(self) -> bool:
		raise Exception("TODO")

	def names(self) -> list[str]:
		raise Exception("TODO")

class LLDBValue(PrettyValue):
	def __init__(self, debug_value: Any):
		super().__init__(debug_value)

	def cast_to(self, other: PrettyType) -> List['PrettyValue']:
		raise Exception("TODO")

	def int_value(self) -> int:
		return self.debug_value.GetValueAsSigned(0)

	def member(self, member_name: str) -> List['LLDBValue'] | None:
		child = self.debug_value.GetChildMemberWithName(member_name)
		if child is None:
			return None
		return LLDBValue(child)

	def pretty_type(self) -> PrettyType:
		raise Exception("TODO")

	def str_value(self) -> str:
		# If this is a pointer, maybe we can treat this as a string
		# I cannot find a way to have LLDB have actual strings... so this
		# will just have to read memory byte by byte sadly
		sub = self.debug_value.GetType()
		if sub.IsPointerType() or sub.IsArrayType():
			result = "\""
			error = lldb.SBError()
			for i in range(0, 128):
				child = self.debug_value.GetChildAtIndex(i, 0, True)
				if child is not None:
					char = child.GetData().GetUnsignedInt8(error, 0)
					if char == 0:
						break
					result += chr(char)
				else:
					break

			result += "\""
			return result

		# There can be nothing here
		data = self.debug_value.GetData()
		if data is not None:
			# Try getting as a string first
			error = lldb.SBError()
			maybe = data.GetString(error, 0)
			if maybe is not None and maybe != "None":
				return maybe

		# And there can also be no summary
		summary = self.debug_value.GetSummary()
		if summary is not None and summary != "None":
			return summary

		# Could not determine any value
		return "<???>"

# ---------------------------------------------------------------------------

# Register all base types
register_all_types(LLDBMaker(), LLDBRegister())
