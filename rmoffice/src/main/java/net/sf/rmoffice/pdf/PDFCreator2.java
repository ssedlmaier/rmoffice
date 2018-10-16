/*
 * Copyright 2013 Daniel Nettesheim
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
package net.sf.rmoffice.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.rmoffice.core.Characteristics;
import net.sf.rmoffice.core.RMSheet;
import net.sf.rmoffice.core.items.MagicalItem;
import net.sf.rmoffice.meta.MetaData;
import net.sf.rmoffice.meta.enums.ResistanceEnum;
import net.sf.rmoffice.ui.models.LongRunningUIModel;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Another layout on page 1 and background page.
 */
public class PDFCreator2 extends PDFCreator {
	private final static Logger log = LoggerFactory.getLogger(PDFCreator2.class);

	public PDFCreator2(RMSheet sheet, MetaData data, JFrame parent, LongRunningUIModel longRunningModel) {
		super(sheet, data, parent, longRunningModel);
	}
	
	@Override
	protected void createPage1(PdfContentByte canvas) throws BadElementException, MalformedURLException, IOException, DocumentException {
		page1LogoAndHeader(canvas);
        
		int leftColRightX = LEFT_X + (RIGHT_X - PAGE1_RIGHTBOX_LEFTX);
		int rightColBorderLeftX = RIGHT_X - 175;
		box(canvas, LEFT_X, 740, leftColRightX, BOTTOM_Y); /* left col */
    	box(canvas, rightColBorderLeftX, 740, RIGHT_X, BOTTOM_Y); /* right col */

		
		if (log.isDebugEnabled()) log.debug("loading image runes");
		Image raceRune = loadImage("/images/runes/race/" + sheet.getCulture().getRune());
        if (raceRune != null) {
        	raceRune.setAbsolutePosition(leftColRightX - 60, 653);
        	raceRune.scaleAbsolute(55f, 55f);
    		canvas.addImage(raceRune, true);
        }
        Image profRune = loadImage("/images/runes/prof/" + sheet.getProfession().getRune());
        if (profRune != null) {
        	profRune.setAbsolutePosition(leftColRightX - 60, 580);
        	profRune.scaleAbsolute(55, 55);
        	canvas.addImage(profRune, true);
        }
        
        if (log.isDebugEnabled()) log.debug("processing text page 1");
        float y = 740;
        /* right col*/
        y = page1UpperRightCharImage(canvas, y, rightColBorderLeftX);
		y = page1RaceProfArmorDB(canvas, rightColBorderLeftX + 5, RIGHT_X, y, rightColBorderLeftX);            
        y = page1Resistance(canvas, y, true, rightColBorderLeftX, RIGHT_X);
        y = page1RaceAttributes(canvas, y, rightColBorderLeftX, RIGHT_X);
        y = page1Characteristics(canvas, y, rightColBorderLeftX, RIGHT_X);
        /* fill rest with lines */
        int lineNumb = (int) Math.floor( (y - BOTTOM_Y) / PAGE1_LEFTBOX_LINE_HEIGHT );
        for (int i=0; i<lineNumb; i++) {
        	labeledUserText(canvas, "", "", rightColBorderLeftX + 4, y, rightColBorderLeftX - 4, fontRegular, 8);
        	y -= PAGE1_LEFTBOX_LINE_HEIGHT;
        }
        
        /* right col */
		y = page1Stats(canvas, LEFT_X, leftColRightX);
        y = page1Favorites(canvas, y, 120, true, LEFT_X, leftColRightX);
        if (y > 131) {
        	/* notes */
        	canvas.beginText();
        	canvas.setFontAndSize(fontHeadline, 8);
        	float centerX = LEFT_X + (leftColRightX - LEFT_X ) / 2;
        	canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.header.notes"), centerX, y, 0);
        	canvas.endText();
        }
        /* */
        page1drawHitsPPExhaust(canvas, 111, leftColRightX, LEFT_X);
    	
    	footer(canvas);
	}
	
	@Override
	protected float page1Background(PdfContentByte canvas, float y, float x1, float x2, Characteristics ch, float leftX, float rightX) {
		return y;
	}
	
	/*
	 * Returns the buttom y value of the image.
	 */
	protected float page1UpperRightCharImage(PdfContentByte canvas, float y, float leftX) throws IOException, DocumentException {
		if (sheet.getImagePos().isPage1() && sheet.getCharacteristics().getCharImage() != null) {
			/* image */
			Image charImage = Image.getInstance(sheet.getCharacteristics().getCharImage());
			float maxWidth = RIGHT_X - leftX - 2;
			float maxHeight = 235;
			// we have to reduce the additional lines for training packages and magical items
			/*trainings packs */
			if (!StringUtils.isBlank(sheet.getApprenticeShip())) {
				maxHeight -= (2*PAGE1_LEFTBOX_LINE_HEIGHT);
			}
			/* magical items */
			List<MagicalItem> items = sheet.getMagicalitems();
			for (int i=0; i<items.size(); i++) {
				MagicalItem item = items.get(i);
				int times = 0;
				for (ResistanceEnum res : ResistanceEnum.values()) {
					if (Boolean.TRUE.equals(item.getFavorite()) && item.hasResistanceModifier(res)) {
						times++;
					}
				}
				if (Boolean.TRUE.equals(item.getFavorite()) && item.hasDBModifier()) {
					times++;
				}
				if (times > 0) {
					String asOneLine = item.asOneLine(i+1);
					float yDelta = showMagicalItems(canvas, asOneLine, 0, leftX + 6, RIGHT_X - 6, true);
					maxHeight += (times * yDelta);
				}
			}
			// resize image
			float height = charImage.getHeight(); 
			float width = charImage.getWidth();
			if (log.isDebugEnabled()) log.debug("character image size w x h = "+width+" x "+height );
			if (height > maxHeight) {
				float f = height / maxHeight;
				height = maxHeight;
				width = width / f;
				if (log.isDebugEnabled()) log.debug("character image scaled (height) to w x h = "+width+" x "+height );
			}
			if (width > maxWidth) {
				float f = width / maxWidth;
				width = maxWidth;
				height = height / f;
				if (log.isDebugEnabled()) log.debug("character image scaled (width) to w x h = "+width+" x "+height );
			}
			float absX = RIGHT_X - width - 1; // now it is aligned right
			absX -= ((RIGHT_X - leftX - 2) - width) / 2; // centerize the img
			float absY = y - 1 - height;
			charImage.setAbsolutePosition(absX, absY);
			charImage.scaleAbsolute(width, height);
			canvas.addImage(charImage, true);
			/* lines */
			hline(canvas, leftX, absY - 1, RIGHT_X);
			// set the return value
			y = absY;
		}
		y = y - PAGE1_LEFTBOX_LINE_HEIGHT - 6;
		return y;
	}
	
	@Override
	protected float page4TalentFlaws(PdfContentByte canvas, float rightX) throws DocumentException {
		// the talents & flaws
		float upperY = super.page4TalentFlaws(canvas, rightX);
		// the background
		upperY = page4BackgroundIgnoreEmpty(canvas, upperY, LEFT_X + 5, rightX - 5, sheet.getCharacteristics(), LEFT_X, rightX);
		return upperY;
	}
	
	/*
	 * Returns the new y
	 */
	protected float page4BackgroundIgnoreEmpty(PdfContentByte canvas, float bottomY, float labelTextX1, float labelTextX2, Characteristics ch, float leftX, float rightX) {
		float center = leftX + (rightX - leftX) / 2;
		// here we start with the last one
		if (StringUtils.isNotBlank(ch.getMisc4())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, "", ch.getMisc4(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getMisc3())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, "", ch.getMisc3(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getMisc2())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, "", ch.getMisc2(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getMisc1())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.misc")+":", ch.getMisc1(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getChildren())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.children")+":", ch.getChildren(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getSiblings())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.siblings")+":", ch.getSiblings(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getSpouse())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.spouse")+":", ch.getSpouse(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getParent())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.parent")+":", ch.getParent(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getLord())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.lord")+":", ch.getLord(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(sheet.getDivinestatus())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("ui.basic.divinestatus")+":", sheet.getDivinestatus(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getDeity())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.deity")+":", ch.getDeity(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getHomeTown())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.homeTown")+":", ch.getHomeTown(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		if (StringUtils.isNotBlank(ch.getNationality())) {
			bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
			labeledUserText(canvas, RESOURCE.getString("rolemaster.characteristics.nationality")+":", ch.getNationality(), labelTextX1, bottomY, labelTextX2, fontRegular, 8);
		}
		
		bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
		/* Background */
		canvas.beginText();
		canvas.setFontAndSize(fontHeadline, 8);
		canvas.showTextAligned(Element.ALIGN_CENTER, RESOURCE.getString("pdf.page1.background.header"), center, bottomY, 0);
		canvas.endText();
		bottomY += PAGE1_LEFTBOX_LINE_HEIGHT;
		hline(canvas, leftX, bottomY, rightX);
		return bottomY;
	}
}
