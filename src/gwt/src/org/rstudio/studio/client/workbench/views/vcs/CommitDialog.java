/*
 * CommitDialog.java
 *
 * Copyright (C) 2009-11 by RStudio, Inc.
 *
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.workbench.views.vcs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import org.rstudio.core.client.widget.ModalDialogBase;
import org.rstudio.core.client.widget.ProgressIndicator;
import org.rstudio.core.client.widget.ThemedButton;
import org.rstudio.studio.client.common.vcs.VCSServerOperations;
import org.rstudio.studio.client.server.VoidServerRequestCallback;
import org.rstudio.studio.client.workbench.views.vcs.VCS.CommitDisplay;

public class CommitDialog extends ModalDialogBase
   implements CommitDisplay
{
   @Inject
   public CommitDialog(VCSServerOperations server)
   {
      server_ = server;

      setText("Commit");
      ThemedButton ok = new ThemedButton("OK", new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            performCommit();
         }
      });
      addOkButton(ok);
      addCancelButton();

      progress_ = addProgressIndicator();
   }

   @Override
   protected Widget createMainWidget()
   {
      commitLabel_ = new Label("Message");

      commitMessage_ = new TextArea();
      commitMessage_.setCharacterWidth(40);
      commitMessage_.setVisibleLines(8);

      amend_ = new CheckBox("Amend\u00A0\u00A0");
      amend_.setTitle("Amend and replace the previous commit");

      signOff_ = new CheckBox("Sign-off");

      HorizontalPanel hpanel = new HorizontalPanel();
      hpanel.add(amend_);
      hpanel.add(signOff_);

      VerticalPanel vPanel = new VerticalPanel();
      vPanel.add(commitLabel_);
      vPanel.add(commitMessage_);
      vPanel.add(hpanel);
      return vPanel;
   }

   private void performCommit()
   {
      server_.vcsCommitGit(commitMessage_.getText(),
                           amend_.getValue().booleanValue(),
                           signOff_.getValue().booleanValue(),
                           new VoidServerRequestCallback(progress_));
   }

   private TextArea commitMessage_;
   private CheckBox amend_;
   private CheckBox signOff_;
   private Label commitLabel_;
   private final VCSServerOperations server_;
   private ProgressIndicator progress_;
}
