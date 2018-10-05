package com.jaspe.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jaspe.context.JaspeContext;
import com.jaspe.parser.JaspeParser;

import java.util.Arrays;
import java.util.HashSet;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
public class JaspeJsonView extends JaspeView {

  private static final String FIELDS_PARAMETER = "fields";

  ObjectMapper objectMapper;

  public JaspeJsonView(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   *
   * @param value
   * @param jaspeContext
   * @throws Exception
   */
  @Override
  protected void renderOK(Object value, JaspeContext jaspeContext) throws Exception {
    if (!jaspeContext.isJaspeDisabled()) {
      String fieldsQueryParam = jaspeContext.getRequest().getParameter(FIELDS_PARAMETER);

      fieldsQueryParam = JaspeParser.buildQuery(new StringBuilder(fieldsQueryParam));

      String fields[] = fieldsQueryParam != null ? fieldsQueryParam.split(",") : null;

      JsonNode rootNode = objectMapper.valueToTree(value);
      if (rootNode.isArray()) {
        rootNode.forEach((node) -> JaspeParser.parse(node, new HashSet<String>(Arrays.asList(fields)), ""));
      } else {
        JaspeParser.parse(rootNode, new HashSet<>(Arrays.asList(fields)), "");
      }
      objectMapper.writeValue(jaspeContext.getResponse().getOutputStream(), rootNode);
    } else {
      objectMapper.writeValue(jaspeContext.getResponse().getOutputStream(), value);
    }
  }

  /**
   *
   * @param value
   * @param jaspeContext
   * @throws Exception
   */
  @Override
  protected void renderError(Object value, JaspeContext jaspeContext) throws Exception {
    objectMapper.writeValue(jaspeContext.getResponse().getOutputStream(), value);
  }

  /**
   *
   */
  public static class Builder implements JaspeViewBuilder<JaspeJsonView> {

    private ObjectMapper objectMapper;

    public Builder() {
      //Enable pretty-printing by default
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

      this.objectMapper = objectMapper;
    }

    public Builder withObjectMapper(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
      return this;
    }

    @Override
    public JaspeJsonView build() {
      return new JaspeJsonView(objectMapper);
    }

  }
}
