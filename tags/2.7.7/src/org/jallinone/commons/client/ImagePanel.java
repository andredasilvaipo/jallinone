package org.jallinone.commons.client;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Dimension;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Scrollpane that may contain an image.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class ImagePanel extends JScrollPane {


  private Image image = null;

  private JPanel img = new JPanel() {

    public void paint(Graphics g) {
      if (image!=null) {
        g.drawImage(image,0,0,image.getWidth(this),image.getHeight(this),this);
        img.setPreferredSize(new Dimension(image.getWidth(this),image.getHeight(this)));
        img.revalidate();
      }
    }

  };



  public ImagePanel() {
    this.getViewport().add(img,null);
  }


  public void setImage(byte[] bytes) {
    if (bytes==null)
      image = null;
    else {
      image = new ImageIcon(bytes).getImage();
      this.getViewport().setEnabled(true);
    }
    repaint();
    this.revalidate();
  }


}