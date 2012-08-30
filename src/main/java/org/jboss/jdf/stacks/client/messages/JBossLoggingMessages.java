/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.jdf.stacks.client.messages;

import org.jboss.jdf.stacks.client.StacksClient;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
public class JBossLoggingMessages implements StacksMessages {
    
    private static final Logger log = Logger.getLogger(StacksClient.class.getName());

    /* (non-Javadoc)
     * @see org.jboss.jdf.stacks.client.messages.StacksMessages#showInfoMessage(java.lang.String)
     */
    @Override
    public void showInfoMessage(String infoMessage) {
        log.info(infoMessage);
    }

    /* (non-Javadoc)
     * @see org.jboss.jdf.stacks.client.messages.StacksMessages#showWarnMessage(java.lang.String)
     */
    @Override
    public void showWarnMessage(String warnMessage) {
        log.warn(warnMessage);
    }

    /* (non-Javadoc)
     * @see org.jboss.jdf.stacks.client.messages.StacksMessages#showDebugMessage(java.lang.String)
     */
    @Override
    public void showDebugMessage(String debugMessage) {
        log.debug(debugMessage);
    }

    /* (non-Javadoc)
     * @see org.jboss.jdf.stacks.client.messages.StacksMessages#showErrorMessage(java.lang.String)
     */
    @Override
    public void showErrorMessage(String errorMessage) {
        log.error(errorMessage);
    }

    /* (non-Javadoc)
     * @see org.jboss.jdf.stacks.client.messages.StacksMessages#showErrorMessageWithCause(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void showErrorMessageWithCause(String errorMessage, Throwable cause) {
        log.error(errorMessage, cause);
    }

}
