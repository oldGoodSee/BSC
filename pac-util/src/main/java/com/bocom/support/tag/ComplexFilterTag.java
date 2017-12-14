package com.bocom.support.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.bocom.dto.session.SessionUserInfo;

@SuppressWarnings("serial")
public class ComplexFilterTag extends TagSupport
{
    String code;
    
    String person;
    
    String subject;
    
    @Override
    public int doStartTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        JspWriter out = this.pageContext.getOut();
        SessionUserInfo session = (SessionUserInfo) request.getSession()
                .getAttribute("sessionUserInfo");
        String policeCode = session.getPoliceCode();
        if (StringUtils.isNotBlank(policeCode)
                && policeCode.trim().equals(code))
        {
            try
            {
                out.print(subject);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }else{
            String result = subject.replace(person, person.substring(0, 1)
                    + "某");
            try
            {
                out.print(result);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return super.doStartTag();
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public void setPerson(String person)
    {
        this.person = person;
    }
    
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
}
