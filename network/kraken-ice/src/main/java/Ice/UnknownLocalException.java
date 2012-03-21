// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.1

package Ice;

// <auto-generated>
//
// Generated from file `LocalException.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


/**
 * This exception is raised if an operation call on a server raises a
 * local exception. Because local exceptions are not transmitted by
 * the Ice protocol, the client receives all local exceptions raised
 * by the server as {@link UnknownLocalException}. The only exception to this
 * rule are all exceptions derived from {@link RequestFailedException},
 * which are transmitted by the Ice protocol even though they are
 * declared <tt>local</tt>.
 * 
 **/
public class UnknownLocalException extends UnknownException
{
    public UnknownLocalException()
    {
        super();
    }

    public UnknownLocalException(String unknown)
    {
        super(unknown);
    }

    public String
    ice_name()
    {
        return "Ice::UnknownLocalException";
    }
}