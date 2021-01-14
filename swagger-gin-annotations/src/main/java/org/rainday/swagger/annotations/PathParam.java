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

package org.rainday.swagger.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathParam {

    /**
     * Defines the name of the URI template parameter whose value will be used
     * to initialize the value of the annotated method parameter, class field or
     * property. See {@link Path#value()} for a description of the syntax of
     * template parameters.
     *
     * <p>E.g. a class annotated with: {@code @Path("widgets/{id}")}
     * can have methods annotated whose arguments are annotated
     * with {@code @PathParam("id")}.
     */
    String value();
}
