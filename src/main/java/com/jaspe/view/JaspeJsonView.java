package com.jaspe.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jaspe.context.JaspeContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
  private static final char FIELD_DELIMITER = ',';
  private static final String FIELD_SUBQUERY_DELIMITER = ".";
  private static final String FIELD_SUBQUERY_OPEN = "(";
  private static final String FIELD_SUBQUERY_CLOSE = ")";

  ObjectMapper objectMapper;

  public JaspeJsonView(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  protected void renderOK(Object value, JaspeContext jaspeContext) throws Exception {
    if (!jaspeContext.isJaspeDisabled()) {
      String fieldsQueryParam = jaspeContext.getRequest().getParameter(FIELDS_PARAMETER);

      fieldsQueryParam = buildQuery(new StringBuilder(fieldsQueryParam));

      String fields[] = fieldsQueryParam != null ? fieldsQueryParam.split(",") : null;

      JsonNode rootNode = objectMapper.valueToTree(value);
      if (rootNode.isArray()) {
        rootNode.forEach((node) -> applyFieldsFilter(node, new HashSet<String>(Arrays.asList(fields)), ""));
      } else {
        applyFieldsFilter(rootNode, new HashSet<>(Arrays.asList(fields)), "");
      }
      objectMapper.writeValue(jaspeContext.getResponse().getOutputStream(), rootNode);
    }else {
      objectMapper.writeValue(jaspeContext.getResponse().getOutputStream(), value);
    }
  }

  @Override
  protected void renderError(Object value, JaspeContext jaspeContext) throws Exception {
    objectMapper.writeValue(jaspeContext.getResponse().getOutputStream(), value);
  }

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

  private void applyFieldsFilter(JsonNode node, Set<String> fields, String parentField) {

    Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();

    while (fieldsIterator.hasNext()) {
      Map.Entry<String, JsonNode> mapEntry = fieldsIterator.next();
      String entryKey = mapEntry.getKey();
      JsonNode entryValue = mapEntry.getValue();
      String fieldPath;

      if (!parentField.isEmpty()) {
        fieldPath = parentField + "." + entryKey;
      } else {
        fieldPath = entryKey;
      }

      boolean contains = false;
      for (String field : fields) {
        if (field.equals(fieldPath) || field.startsWith(fieldPath + FIELD_SUBQUERY_DELIMITER) || field.equals("*")) {
          contains = true;
          break;
        }
      }

      if (!contains) {
        fieldsIterator.remove();
      } else {
        if (entryValue.isObject() || entryValue.isArray()) {

          Set<String> subFields = new HashSet<>();
          for (Iterator<String> iterator = fields.iterator(); iterator.hasNext(); ) {
            String requiredField = iterator.next();
            if (requiredField.contains(entryKey + FIELD_SUBQUERY_DELIMITER)) {
              subFields.add(requiredField);
            }
          }

          if (subFields.isEmpty()) {
            subFields.add("*");
          }

          if (entryValue.isObject()) {
            applyFieldsFilter(entryValue, subFields, fieldPath);
          } else if (entryValue.isArray()) {
            if (!fields.isEmpty()) {
              for (JsonNode arrayElementNode : entryValue) {
                applyFieldsFilter(arrayElementNode, subFields, fieldPath);
              }
            }
          }

        }
      }
    }
  }

  private String buildQuery(StringBuilder buffer) {
    StringBuilder query = new StringBuilder();
    StringBuilder field = new StringBuilder();
    String parentField = "";
    int index = 0;

    while (true) {
      if (buffer.charAt(index) == FIELD_DELIMITER) {
        index++;
        query.append(FIELD_DELIMITER);
        if (!parentField.isEmpty()) {
          query.append(parentField + FIELD_SUBQUERY_DELIMITER);
        }
        field = new StringBuilder();
      } else if (buffer.substring(index).startsWith(FIELD_SUBQUERY_OPEN)) {
        index++;
        query.append(FIELD_SUBQUERY_DELIMITER);
        parentField = field.toString();
        field = new StringBuilder();
      } else if (buffer.substring(index).startsWith(FIELD_SUBQUERY_CLOSE)) {
        index++;
        parentField = "";
      } else {
        field.append(buffer.charAt(index));
        query.append(buffer.charAt(index));
        index++;
      }

      if (index >= buffer.length()) {
        break;
      }
    }
    return query.toString();

  }

}
