package org.g6.caas.xml.web;

import lombok.Data;

@Data
public class SecurityConstraint {
    private WebResourceCollection webResource;
    private AuthConstraint constraint;
    private UserDataConstraint userConstraint;
}
