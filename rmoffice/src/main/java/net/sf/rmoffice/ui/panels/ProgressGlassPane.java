/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.rmoffice.ui.panels;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;


/**
*
* @author Romain Guy
*/
public class ProgressGlassPane extends JComponent {
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale"); //$NON-NLS-1$
	
   private static final int BAR_WIDTH = 250;
   private static final int BAR_HEIGHT = 20;
   
   private static final Color TEXT_COLOR = Color.BLACK;
   
   private static final float[] GRADIENT_FRACTIONS = new float[] {
       0.0f, 0.499f, 0.5f, 1.0f
   };
   private static final Color[] GRADIENT_COLORS = new Color[] {
       Color.GRAY, Color.DARK_GRAY, Color.BLACK, Color.GRAY
   };
   private static final Color GRADIENT_COLOR2 = Color.WHITE;
   private static final Color GRADIENT_COLOR1 = Color.GRAY;

   private String message;
   private volatile int progress = 1;

   public ProgressGlassPane() {
	   message = getMessage("...");
       setBackground(Color.white);
       setLayout(new BorderLayout());
       addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			e.consume();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			e.consume();
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			e.consume();
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			e.consume();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			e.consume();
		}
	});
   }

   private String getMessage(String str) {
	   try {
		   str = RESOURCE.getString(str);
	   } catch (MissingResourceException e) {
		   /* ignore, we just use the str */
	   }
	   return MessageFormat.format(RESOURCE.getString("ui.generatecharacter.generate"), str); 
   }

   public void setProgress(final int progress, String stepResKey) {
	   this.message = getMessage(stepResKey);
	   this.progress = progress;
	   repaint();
   }
   
   @Override
   protected void paintComponent(Graphics g) {
       /* enables anti-aliasing */
       Graphics2D g2 = (Graphics2D) g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
       /* gets the current clipping area */
       Rectangle clip = g.getClipBounds();
       
       /* sets a 65% translucent composite */
       AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
       Composite composite = g2.getComposite();
       g2.setComposite(alpha);
       
       /* fills the background */
       g2.setColor(getBackground());
       g2.fillRect(clip.x, clip.y, clip.width, clip.height);
       
       /* centers the progress bar on screen */
       FontMetrics metrics = g.getFontMetrics();        
       int x = (getWidth() - BAR_WIDTH) / 2;
       int y = (getHeight() - BAR_HEIGHT - metrics.getDescent()) / 2;
       
       /* draws the text */
       g2.setColor(TEXT_COLOR);
       g2.drawString(message, x, y);
       
       /* goes to the position of the progress bar  */
       y += metrics.getDescent();
       
       /* computes the size of the progress indicator */
       int w = (int) (BAR_WIDTH * (progress / 100.0f));
       int h = BAR_HEIGHT;
       
       /* draws the content of the progress bar */
       Paint paint = g2.getPaint();
       
       /* bar's background */
       Paint gradient = new GradientPaint(x, y, GRADIENT_COLOR1,
               x, y + h, GRADIENT_COLOR2);
       g2.setPaint(gradient);
       g2.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);
       
       /* actual progress */
       gradient = new LinearGradientPaint(x, y, x, y + h,
               GRADIENT_FRACTIONS, GRADIENT_COLORS);
       g2.setPaint(gradient);
       g2.fillRect(x, y, w, h);
       
       g2.setPaint(paint);
       
       /* draws the progress bar border */
       g2.drawRect(x, y, BAR_WIDTH, BAR_HEIGHT);
       
       g2.setComposite(composite);
   }
}
