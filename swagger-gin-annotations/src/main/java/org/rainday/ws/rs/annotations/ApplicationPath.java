/*
 * Copyright (c) 2010, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.rainday.ws.rs.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationPath {

    /**
     * Defines the base URI for all resource URIs. A trailing '/' character will
     * be automatically appended if one is not present.
     *
     * <p>The supplied value is automatically percent
     * encoded to conform to the {@code path} production of
     * {@link <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 section 3.3</a>}.
     * Note that percent encoded values are allowed in the value, an
     * implementation will recognize such values and will not double
     * encode the '%' character.</p>
     */
    String value();
}
