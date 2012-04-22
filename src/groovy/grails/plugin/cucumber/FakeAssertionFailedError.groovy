/*
 * Copyright 2012 Martin Hauner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.cucumber

import junit.framework.AssertionFailedError


class FakeAssertionFailedError extends AssertionFailedError {
    Throwable error

    FakeAssertionFailedError (Throwable error) {
        this.error = error
    }
    
    @Override
    String getMessage () {
        return error.getMessage ()
    }

    @Override
    Throwable getCause () {
        return error.getCause ()
    }

    @Override
    String toString () {
        return error.toString ()
    }

    @Override
    void printStackTrace () {
        error.printStackTrace ()
    }

    @Override
    void printStackTrace (PrintStream printStream) {
        error.printStackTrace (printStream)
    }

    @Override
    void printStackTrace (PrintWriter printWriter) {
        error.printStackTrace (printWriter)
    }

    @Override
    synchronized Throwable fillInStackTrace () {
        //nop error.fillInStackTrace ()
    }

    @Override
    StackTraceElement[] getStackTrace () {
        return error.getStackTrace ()
    }

    @Override
    void setStackTrace (StackTraceElement[] stackTraceElements) {
        error.setStackTrace (stackTraceElements)
    }

    @Override
    synchronized Throwable initCause (Throwable throwable) {
        return error.initCause (throwable)
    }

    @Override
    String getLocalizedMessage () {
        return error.getLocalizedMessage ()
    }
}
