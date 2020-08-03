package com.servientrega.mail.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@JsonAutoDetect
@JsonSerialize
@ApiModel(description = "E-Mail Response DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailResponse implements Serializable {

    private static final long serialVersionUID = -6104876573750302537L;

    @JsonProperty
    @ApiModelProperty(dataType = "Boolean")
    private boolean success;

    @JsonProperty
    @ApiModelProperty(dataType = "String")
    private List<Map<String, String>> data;

    @JsonProperty
    @ApiModelProperty(dataType = "String")
    private List<Error> errors;

    public MailResponse() {
        //
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void clean() {
        errors = null;
        data = null;
    }

    public void addError(Error error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
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
