# Frequently Asked Questions

 * ***Q***: **PhoneME**'s source code is available, why do you not use that
   to make development faster?
   * ***A***: One of the major things is that **SquirrelJME** is a clean room
     reverse engineering and re-implementation based on books, API
     documentation, and observations. As such, it is not possible to use the
     code from this standpoint. Additionally, **PhoneME** is only a reference
     implementation and it would only be a base starting point so it would
     not bring much as vendors would have heavily modified it or written
     their own APIs. So from these, even if it were possible to use the
     project it would bring no true benefits.
 * ***Q***: What are the errors with `XX11`?
   * ***A***: To conserve space, **SquirrelJME** uses compactified error
     codes which are stored in an [error directory](error-list.md), this is
     so that descriptive messages can be stored externally without there
     being additional space being used by the message.
