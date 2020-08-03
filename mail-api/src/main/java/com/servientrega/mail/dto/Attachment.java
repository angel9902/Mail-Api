package com.servientrega.mail.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
@ApiModel(description = "Attachment To Mail")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Size(min = 10, max = 45)
    @NotNull
    public String name;

    @JsonProperty
    @NotNull
    public String value;

    public Attachment() {
    }

    public Attachment(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
