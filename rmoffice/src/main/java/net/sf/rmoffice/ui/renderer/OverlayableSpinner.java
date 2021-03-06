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
package net.sf.rmoffice.ui.renderer;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.jidesoft.swing.OverlayableUtils;


/**
 * 
 */
public class OverlayableSpinner extends JSpinner {
	private static final long serialVersionUID = 1L;

	public OverlayableSpinner(SpinnerNumberModel model) {
		super(model);
	}

	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
		super.repaint(tm, x, y, width, height);
		OverlayableUtils.repaintOverlayable(this);
	}
}
