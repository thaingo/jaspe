package com.jaspe.view;

/**
 * Interface that all Jaspe View Builders must implement.
 */
public interface JaspeViewBuilder<T extends JaspeView> {

    /**
     * @return fully configured Jaspe view
     */
    T build();
}
