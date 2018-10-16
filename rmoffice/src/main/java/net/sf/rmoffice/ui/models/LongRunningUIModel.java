/*
 * Copyright 2012 Daniel Nettesheim
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
package net.sf.rmoffice.ui.models;

import net.sf.rmoffice.ui.panels.ProgressGlassPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 */
public class LongRunningUIModel {
	private final static Logger log = LoggerFactory.getLogger(LongRunningUIModel.class);
	private int steps = 0;
	private int stepsDone = 0;
	private final ProgressGlassPane glassPane;
	
	/**
	 * @param glassPane
	 */
	public LongRunningUIModel(ProgressGlassPane glassPane) {
		this.glassPane = glassPane;
	}

	public void startProgress(int steps) {
		glassPane.setVisible(true);
		this.steps = steps;
		this.stepsDone = 0;
	}
	
	/**
	 * 
	 * 
	 * @param stepsDone
	 * @param nextStep a resource key or a string
	 */
	public void workDone(int stepsDone, String nextStep) {
		this.stepsDone += stepsDone;
		int progress = (int) (((float)this.stepsDone / (float)steps) * 100f);
		if (log.isDebugEnabled()) log.debug("percent = "+progress + " next step="+nextStep);
		if (progress < 1 || progress > 100) {
			if (log.isWarnEnabled()) log.warn("1 < progress < 100: reset to progress=1");
			progress = 1;
		}
		glassPane.setProgress(progress, nextStep);
	}

	public void done() {
		glassPane.setVisible(false);
	}
	
}
