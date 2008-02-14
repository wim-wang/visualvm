/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.visualvm.core.host;

import com.sun.tools.visualvm.core.datasource.DataSourceRoot;
import com.sun.tools.visualvm.core.datasource.Host;
import com.sun.tools.visualvm.core.explorer.ExplorerActionDescriptor;
import com.sun.tools.visualvm.core.explorer.ExplorerActionsProvider;
import com.sun.tools.visualvm.core.explorer.ExplorerContextMenuFactory;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;

/**
 *
 * @author Jiri Sedlacek
 */
class HostActionsProvider {

    private final RenameHostAction renameHostAction = new RenameHostAction();
    private final AddNewHostAction addNewHostAction = new AddNewHostAction();
    private final RemoveHostAction removeHostAction = new RemoveHostAction();


    void initialize() {
        ExplorerContextMenuFactory.sharedInstance().addExplorerActionsProvider(new HostActionProvider(), Host.class);
        ExplorerContextMenuFactory.sharedInstance().addExplorerActionsProvider(new RemoteHostsContainerActionProvider(), RemoteHostsContainer.class);
        ExplorerContextMenuFactory.sharedInstance().addExplorerActionsProvider(new DataSourceRootActionProvider(), DataSourceRoot.class);
    }    
    
    
    private class AddNewHostAction extends AbstractAction {
        
        public AddNewHostAction() {
            super("Add Remote Host...");
        }
        
        public void actionPerformed(ActionEvent e) {
            HostProperties hostDescriptor = HostUtils.defineHost();
            if (hostDescriptor != null) HostsSupport.getInstance().getHostProvider().createHost(hostDescriptor, true);
        }
        
    }
    
    private class RenameHostAction extends AbstractAction {
        
        public RenameHostAction() {
            super("Rename...");
        }
        
        public void actionPerformed(ActionEvent e) {
            Host host = (Host)e.getSource();
            HostProperties hostDescriptor = HostUtils.renameHost(host);
            if (hostDescriptor != null) host.setDisplayName(hostDescriptor.getDisplayName());
        }
        
    }
    
    private class RemoveHostAction extends AbstractAction {
        
        public RemoveHostAction() {
            super("Remove");
        }
        
        public void actionPerformed(ActionEvent e) {
            HostImpl host = (HostImpl)e.getSource();
            HostsSupport.getInstance().getHostProvider().removeHost(host, true);
        }
        
    }
    
    
    private class HostActionProvider implements ExplorerActionsProvider<Host> {
        
        public ExplorerActionDescriptor getDefaultAction(Host host) {
            return null;
        }

        public Set<ExplorerActionDescriptor> getActions(Host host) {
            if (host == null || host == Host.LOCALHOST) return Collections.EMPTY_SET;
            
            Set<ExplorerActionDescriptor> actions = new HashSet();

            actions.add(new ExplorerActionDescriptor(renameHostAction, 10));
            
            if (host instanceof HostImpl) actions.add(new ExplorerActionDescriptor(removeHostAction, 20));

            return actions;
        }
    }
    
    private class RemoteHostsContainerActionProvider implements ExplorerActionsProvider<RemoteHostsContainer> {

        public ExplorerActionDescriptor getDefaultAction(RemoteHostsContainer container) { return null; }

        public Set<ExplorerActionDescriptor> getActions(RemoteHostsContainer container) {
            Set<ExplorerActionDescriptor> actions = new HashSet();
            
            actions.add(new ExplorerActionDescriptor(addNewHostAction, 0));
            
            return actions;
        }
        
    }
    
    private class DataSourceRootActionProvider implements ExplorerActionsProvider<DataSourceRoot> {

        public ExplorerActionDescriptor getDefaultAction(DataSourceRoot root) { return null; }

        public Set<ExplorerActionDescriptor> getActions(DataSourceRoot root) {
            Set<ExplorerActionDescriptor> actions = new HashSet();
            
            actions.add(new ExplorerActionDescriptor(addNewHostAction, 10));
            
            return actions;
        }
        
    }

}
