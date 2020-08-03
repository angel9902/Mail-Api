package com.servientrega.mail.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@XmlRootElement
@JsonAutoDetect
@JsonSerialize
@ApiModel(description = "E-Mail Request DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Size(min = 1, max = 45)
    @NotNull()
    private String from;

    @JsonProperty
    @Size(min = 1, max = 45)
    @NotNull()
    private String subject;

    @JsonProperty
    @Size(min = 1)
    @NotNull()
    @Valid
    private List<Recipient> recipients = new ArrayList<>();

    @JsonProperty
    @NotNull()
    @Size(min = 1)
    private String message;

    @JsonProperty
    @NotNull()
    private Boolean isTemplate = Boolean.FALSE;

    @JsonProperty
    private List<Attachment> attachments = new ArrayList<>();

    @JsonProperty
    private List<Parameters> parameters = new ArrayList<>();

    public MailMessage() {
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the recipients
     */
    public List<Recipient> getRecipients() {
        return recipients;
    }

    /**
     * @param recipients the recipients to set
     */
    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the isTemplate
     */
    public Boolean getIsTemplate() {
        return isTemplate;
    }

    /**
     * @param isTemplate the isTemplate to set
     */
    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the parameters
     */
    public List<Parameters> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<Parameters> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            json = this.toString();
        }
        return json;
    }
}
