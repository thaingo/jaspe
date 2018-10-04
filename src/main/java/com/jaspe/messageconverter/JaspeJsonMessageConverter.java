package com.jaspe.messageconverter;

import com.jaspe.view.JaspeJsonView;

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
public class JaspeJsonMessageConverter extends JaspeAbstractMessageConverter<JaspeJsonView> {

  public JaspeJsonMessageConverter() {
    super(new JaspeJsonView.Builder().build());
  }

  public JaspeJsonMessageConverter(JaspeJsonView view) {
    super(view);
  }

}
