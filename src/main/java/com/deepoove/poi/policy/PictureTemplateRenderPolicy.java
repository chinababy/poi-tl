package com.deepoove.poi.policy;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.PictureTemplate;
import com.deepoove.poi.util.ReflectionUtils;
import com.deepoove.poi.xwpf.NiceXWPFDocument;

public class PictureTemplateRenderPolicy implements RenderPolicy {
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void render(ElementTemplate eleTemplate, Object dataObj, XWPFTemplate template) {
        PictureRenderData picdata = (PictureRenderData)dataObj;
        byte[] data = picdata.getData();
        int format = XWPFDocument.PICTURE_TYPE_PNG;
        PictureTemplate pictureTemplate = (PictureTemplate)eleTemplate;
        XWPFPicture t = pictureTemplate.getPicture();
        NiceXWPFDocument doc = template.getXWPFDocument();
        String docId = null;
        String hid = null;
        String fid = null;
        try {
            logger.info("Replace the picture data for the reference object: {}", t);
            XWPFRun run = (XWPFRun) ReflectionUtils.getValue("run", t);
            if (run.getParent().getPart() instanceof XWPFHeader) {
                XWPFHeaderFooter headerFooter = (XWPFHeaderFooter) run.getParent().getPart();
                    setPictureReference(t, hid == null ? hid = headerFooter.addPictureData(data, format) : hid);
               
            } else if (run.getParent().getPart() instanceof XWPFFooter) {
                XWPFHeaderFooter headerFooter = (XWPFHeaderFooter) run.getParent().getPart();
                setPictureReference(t, fid == null ? fid = headerFooter.addPictureData(data, format) : fid);
            } else {
                setPictureReference(t, docId == null ? docId = doc.addPictureData(data, format) : docId);
            }
        } catch (Exception e) {
            throw new RenderException("ReferenceRenderPolicy render error", e);
        }
    }
    
    private void setPictureReference(XWPFPicture t, String relationId) {
        CTPicture ctPic = t.getCTPicture();
        CTBlipFillProperties bill = ctPic.getBlipFill();
        CTBlip blip = bill.getBlip();
        blip.setEmbed(relationId);
    }

}
