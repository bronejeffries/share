
package org.alfresco.web.awe.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Tag used at the end of the body section of a page to indicate the end
 * of a page that potentially contains editable Alfresco content.
 * 
 * This tag extends the Web Editor end template tag to provide custom script.
 * 
 * @author Gavin Cornwell
 */
public class EndTemplateTag extends org.springframework.extensions.webeditor.taglib.EndTemplateTag
{
    private static final long serialVersionUID = 963212275947772271L;

    /**
     * @see org.springframework.extensions.webeditor.taglib.EndTemplateTag#includeCustomConfiguration(java.io.Writer)
     */
    public void includeCustomConfiguration(Writer out) throws IOException
    {
        // render JavaScript to configure toolbar and edit icons
        List<MarkedContent> markedContent = AlfrescoTagUtil.getMarkedContent(pageContext.getRequest());
        
        out.write("WEF.ConfigRegistry.registerConfig('org.alfresco.awe',{id:'awe',name:'awe',editables:[\n");
        boolean first = true;
        for (MarkedContent content : markedContent)
        {
            if (first == false)
            {
                out.write(",");
            }
            else
            {
                first = false;
            }

            out.write("\n{\n   id: \"");
            out.write(encode(content.getMarkerId()));
            out.write("\",\n   nodeRef: \"");
            out.write(encode(content.getContentId()));
            out.write("\",\n   title: \"");
            out.write(encode(content.getContentTitle()));
            out.write("\",\n   nested: ");
            out.write(Boolean.toString(content.isNested()));
            out.write(",\n   redirectUrl: window.location.href");
            if (content.getFormId() != null)
            {
                out.write(",\n   formId: \"");
                out.write(encode(content.getFormId()));
                out.write("\"");
            }
            out.write("\n}");
        }
        out.write("]});\n");

        if (logger.isDebugEnabled())
        {
            logger.debug("Completed endTemplate rendering for " + markedContent.size() + 
                " marked content items with toolbar location of: " + getToolbarLocation());
        }
    }
}
